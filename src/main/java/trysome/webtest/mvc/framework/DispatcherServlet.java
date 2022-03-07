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

//Servlet注解，标注servlet以及请求地址
@WebServlet(urlPatterns = "/")
/**
 * 继承基本的HttpServlet实现DispatcherServlet
 * 整个框架如下：
 *    HTTP Request    ┌─────────────────┐
 * ──────────────────>│DispatcherServlet│
 *                    └─────────────────┘
 *                             │
 *                ┌────────────┼────────────┐
 *                ▼            ▼            ▼
 *          ┌───────────┐┌───────────┐┌───────────┐
 *          │Controller1││Controller2││Controller3│
 *          └───────────┘└───────────┘└───────────┘
 *                │            │            │
 *                └────────────┼────────────┘
 *                             ▼
 *    HTTP Response ┌────────────────────┐
 * <────────────────│render(ModelAndView)│
 *                  └────────────────────┘
 */
public class DispatcherServlet extends HttpServlet {

//    private static final Set<Class<?>> supportedGetParameterTypes = Arrays.asList(int.class, long.class, boolean.class,
//            String.class, HttpServletRequest.class, HttpServletResponse.class, HttpSession.class);
//
//    private static final Set<Class<?>> supportedPostParameterTypes = Arrays.asList(HttpServletRequest.class,
//            HttpServletResponse.class, HttpSession.class);
    //GET请求的列表，应该是不可修改的Set，java8麻烦，就懒得改了
    private static final List<Class<?>> supportedGetParameterTypes = Arrays.asList(int.class, long.class, boolean.class,
            String.class, HttpServletRequest.class, HttpServletResponse.class, HttpSession.class);
    //如上，POST请求的参数列表
    private static final List<Class<?>> supportedPostParameterTypes = Arrays.asList(HttpServletRequest.class,
            HttpServletResponse.class, HttpSession.class);

    //日志
    private final Logger logger = LoggerFactory.getLogger(getClass());
    //GET请求的路径和对应的GetDispatcher
    private Map<String, GetDispatcher> getMappings = new HashMap<>();
    //POST请求的路径和对应的PostDispatcher
    private Map<String, PostDispatcher> postMappings = new HashMap<>();
    //MVC中的C的列表
    private List<Class<?>> controllers = Arrays.asList(IndexController.class, UserController.class);
    //渲染引擎
    private ViewEngine viewEngine;

    /**
     * HttpServlet的初始化方法
     * 初始化所有Get和Post的映射，以及用于渲染的模板引擎
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        //日志打印
        logger.info("init {} ....", getClass().getSimpleName());
        //提供一些功能将数据集与对象转换的实现，jackson类
        ObjectMapper objectMapper = new ObjectMapper();
        //配置
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //初始化映射
        for (Class<?> controllerClass : controllers) {
            try {
                Object controllerInstance = controllerClass.getConstructor().newInstance();
                for (Method method : controllerClass.getMethods()) {
                    //是否存在注解为GetMapping的类
                    if (method.getAnnotation(GetMapping.class) != null) {
                        //返回值判断
                        if (method.getReturnType() != ModelAndView.class && method.getReturnType() != void.class) {
                            throw new UnsupportedOperationException("Unsupported return type : " + method.getReturnType() + " for method: " + method);
                        }
                        for (Class<?> parameterClass : method.getParameterTypes()) {
                            //判断GET支持的类型列表
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

/**
 * 创建基本Dispatcher的抽象类
 */
abstract class AbstractDispatcher {
    public abstract ModelAndView invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ReflectiveOperationException;
}

/**
 * 继承抽象类，实现GetDispatcher
 */
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

/**
 * 继承抽象类，实现PostDispatcher
 */
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