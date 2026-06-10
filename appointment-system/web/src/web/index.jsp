<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录-多功能预约系统</title>
    <link rel="stylesheet" href="/css/index.css">
</head>
<body>
<!-- 识别“因权限不足而被跳转回来”，然后弹出警告 -->
<%boolean accessDenied = "true".equals(request.getParameter("accessDenied"));%>
<% if (accessDenied) { %>
<script>
    alert("您未有访问权限");
    window.history.replaceState(
        null,
        "",
        "<%= request.getContextPath() %>/index.jsp");
</script>
<% } %>

<div class="app-container">
    <div class="logo-wrap">
        <img src="/images/1.png" width = "25%" >
        <div class="line"></div>
        <span>多功能预约系统</span>
    </div>
    <div id="center">
        <div class = "card">
            <h2>账号密码登录</h2>
            <hr>
            <form action="login" method="post">
                <div class="login-form">
                    <label>
                        <input
                            type="text"
                            name="username"
                            placeholder="请输入用户名">
                    </label>
                </div>

                <div class="login-form">
                    <label>
                        <input
                            type="password"
                            name="password"
                            placeholder="请输入用户密码">
                    </label>
                </div>

                <div class="login-form">
                    <input
                        type="submit"
                        value="登录">
                </div>
            </form>
            <a href="register.jsp">还没有账号？点击注册</a>
        </div>
    </div>
</div>
</body>
</html>