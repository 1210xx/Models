<%@ page import="trysome.webtest.User" %>
<%@ page import="trysome.webtest.School"%>
<%  out.println("test test");%>
<%
    User user = (User)request.getAttribute("user");
%>

<%--
JSP页面内置了几个变量：
     out：表示HttpServletResponse的PrintWriter；
     session：表示当前HttpSession对象；
     request：表示HttpServletRequest对象。
--%>

<html>
<head>
    <title>Hello World - JSP</title>
</head>
<body>
    <h1>Hello <%= user.name %>!</h1>
    <p>School Name:
    <span style="color:red">
        <%= user.school.name %>
    </span>
    </p>
    <p>School Address:
    <span style="color:red">
        <%= user.school.address %>
    </span>
    </p>
</body>
</html>