<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>创建账号-多功能预约系统</title>
    <link rel="stylesheet" href="/css/register.css">
</head>
<body>
    <div class="logo-wrap">
        <img src="/images/1.png" width = "25%" >
        <div class="line"></div>
        <span>多功能预约系统</span>
    </div>
    <div id="center">
        <div class = "card">
            <h2>账号密码创建</h2>
            <hr>
            <form action="/register" method="post">
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
                        type="text"
                        name="phone"
                        placeholder="请输入手机号">
                </div>

                <div class="login-form">
                    <input
                        type="submit"
                        value="注册">
                </div>
            </form>
            <a href="index.jsp">已有帐号，返回登陆界面</a>
        </div>
    </div>
</body>
</html>