package trysome.webtest.mvc.framework;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import trysome.webtest.mvc.controller.IndexController;
import trysome.webtest.mvc.controller.UserController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.*;


@WebServlet(urlPatterns = "/")
public class DispatcherServlet extends HttpServlet {

//    private static final Set<Class<?>> supportedGetParameterTypes = Arrays.asList(int.class, long.class, boolean.class,
//            String.class, HttpServletRequest.class, HttpServletResponse.class, HttpSession.class);
//
//    private static final Set<Class<?>> supportedPostParameterTypes = Arrays.asList(HttpServletRequest.class,
//            HttpServletResponse.class, HttpSession.class);

    private static final List<Class<?>> supportedGetParameterTypes = Arrays.asList(int.class, long.class, boolean.class,
            String.class, HttpServletRequest.class, HttpServletResponse.class, HttpSession.class);

    private static final List<Class<?>> supportedPostParameterTypes = Arrays.asList(HttpServletRequest.class,
            HttpServletResponse.class, HttpSession.class);


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, GetDispatcher> getMappings = new HashMap<>();

    private Map<String, PostDispatcher> postMappings = new HashMap<>();

    private List<Class<?>> controllers = Arrays.asList(IndexController.class, UserController.class);

    private ViewEngine viewEngine;

    @Override
    public void init() throws ServletException {
        logger.info("init {} ....", getClass().getSimpleName());

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        for (Class<?> controllerClass : controllers) {
            try {
                Object controllerInstance = controllerClass.getConstructor().newInstance();
                for (Method method : controllerClass.getMethods()) {
                    if (method.getAnnotation(GetMapping.class) != null) {
                        if (method.getReturnType() != ModelAndView.class && method.getReturnType() != void.class) {
                            throw new UnsupportedOperationException("Unsupported return type : " + method.getReturnType() + " for method: " + method);
                        }
                        for (Class<?> parameterClass : method.getParameterTypes()) {
                            if (!supportedGetParameterTypes.contains(parameterClass)) {
                                throw new UnsupportedOperationException("Unsupported parameter type : " + parameterClass + " for method: " + method);
                            }
                        }
                        String[] parameterNames = Arrays.stream(method.getParameters()).map(p -> p.getName()).toArray(String[]::new);

                        String path = method.getAnnotation(GetMapping.class).value();

                        logger.info("Found GET : {} => {}", path, method);

                        this.getMappings.put(path, new GetDispatcher(controllerInstance, method, parameterNames, method.getParameterTypes()));

                    } else if (method.getAnnotation(PostMapping.class) != null) {
                        if (method.getReturnType() != ModelAndView.class && method.getReturnType() != void.class) {
                            throw new UnsupportedOperationException("Unsupported return type:" + method.getReturnType() + " for method: " + method);
                        }
                        Class<?> requestBodyClass = null;

                        for (Class<?> parameterClass : method.getParameterTypes()) {
                            if (!supportedPostParameterTypes.contains(parameterClass)) {
                                if (requestBodyClass == null) {
                                    requestBodyClass = parameterClass;
                                } else {
                                    throw new UnsupportedOperationException("Unsupported duplicate request body type:" + parameterClass + " for method: " + method);
                                }
                            }
                        }

                        String path = method.getAnnotation(PostMapping.class).value();
                        logger.info("Found POST: {} => {}", path, method);
                        this.postMappings.put(path, new PostDispatcher(controllerInstance, method, method.getParameterTypes(), objectMapper));
                    }
                }
            } catch (ReflectiveOperationException e) {
                throw new ServletException(e);
            }
        }
        this.viewEngine = new ViewEngine(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req,resp,this.getMappings);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req,resp,this.postMappings);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp, Map<String, ? extends  AbstractDispatcher> dispatcherMap) throws IOException, ServletException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getRequestURI().substring(req.getContextPath().length());
        AbstractDispatcher dispatcher = dispatcherMap.get(path);

        if (dispatcher == null){
            resp.sendError(404);
            return;
        }

        ModelAndView mv = null;

        try {
            mv = dispatcher.invoke(req,resp);
        }catch (ReflectiveOperationException e){
            throw new ServletException(e);
        }
        if (mv == null){
            return;
        }

        if (mv.view.startsWith("redirect")){
            resp.sendRedirect(mv.view.substring(9));
        }
        PrintWriter pw = resp.getWriter();
        this.viewEngine.render(mv, pw);
        pw.flush();
    }
}

abstract class AbstractDispatcher {
    public abstract ModelAndView invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ReflectiveOperationException;
}


class GetDispatcher extends AbstractDispatcher {
    final Object instance;
    final Method method;
    final String[] parameterNames;
    final Class<?>[] parameterClasses;

    GetDispatcher(Object instance, Method method, String[] parameterNames, Class<?>[] parameterClasses) {
        this.instance = instance;
        this.method = method;
        this.parameterNames = parameterNames;
        this.parameterClasses = parameterClasses;
    }

    @Override
    public ModelAndView invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ReflectiveOperationException {
        Object[] arguments = new Object[parameterClasses.length];
        for (int i = 0; i < parameterClasses.length; i++) {
            String parameterName = parameterNames[i];
            Class<?> parameterClass = parameterClasses[i];
            if (parameterClass == HttpServletRequest.class) {
                arguments[i] = request;
            } else if (parameterClass == HttpServletResponse.class) {
                arguments[i] = response;
            } else if (parameterClass == HttpSession.class) {
                arguments[i] = request.getSession();
            } else if (parameterClass == int.class) {
                arguments[i] = Integer.valueOf(getOrDefault(request, parameterName, "0"));
            } else if (parameterClass == long.class) {
                arguments[i] = Long.valueOf(getOrDefault(request, parameterName, "0"));
            } else if (parameterClass == boolean.class) {
                arguments[i] = Boolean.valueOf(getOrDefault(request, parameterName, "false"));
            } else if (parameterClass == boolean.class) {
                arguments[i] = Boolean.valueOf(getOrDefault(request, parameterName, "false"));
            } else if (parameterClass == String.class) {
                arguments[i] = getOrDefault(request, parameterName, "");
            } else {
                throw new RuntimeException("Missing handler for type: " + parameterName);
            }
        }
        return (ModelAndView) this.method.invoke(this.instance, arguments);
    }

    private String getOrDefault(HttpServletRequest request, String name, String defalutValue) {
        String s = request.getParameter(name);
        return s == null ? defalutValue : s;
    }
}

class PostDispatcher extends AbstractDispatcher {
    final Object instance;
    final Method method;
    final Class<?>[] parameterClasses;
    final ObjectMapper ObjectMapper;

    PostDispatcher(Object instance, Method method, Class<?>[] parameterClasses, com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        this.instance = instance;
        this.method = method;
        this.parameterClasses = parameterClasses;
        ObjectMapper = objectMapper;
    }

    @Override
    public ModelAndView invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ReflectiveOperationException {
        Object[] arguments = new Object[parameterClasses.length];
        for (int i = 0; i < parameterClasses.length; i++) {
            Class<?> parameterClass = parameterClasses[i];
            if (parameterClass == HttpServletRequest.class) {
                arguments[i] = request;
            } else if (parameterClass == HttpServletResponse.class) {
                arguments[i] = response;
            } else if (parameterClass == HttpSession.class) {
                arguments[i] = request.getSession();
            } else {
                BufferedReader reader = request.getReader();
                arguments[i] = this.ObjectMapper.readValue(reader, parameterClass);
            }
        }
        return (ModelAndView) this.method.invoke(instance, arguments);
    }
}