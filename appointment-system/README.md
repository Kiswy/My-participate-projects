# Appointment System

多功能预约系统的 Web 端和 Android 端统一存放在同一个项目目录中。

## 目录结构

```text
appointment-system/
|-- web/      Java Web：JSP、Servlet、JDBC、SQL Server、Vue 3
`-- android/  Android：Java、Gradle、AndroidX
```

两个子项目保持独立，可以分别使用 IntelliJ IDEA 和 Android Studio 打开、构建和运行。

## Web 端

- Java 21
- Apache Tomcat 9
- JSP + Servlet（`javax.servlet`）
- SQL Server JDBC Driver
- Web 根目录：`web/src/web`

数据库密码未提交到 GitHub。运行 Web 项目前需要设置环境变量：

```text
APPOINTMENT_DB_PASSWORD=你的本地数据库密码
```

数据库用户名默认使用 `sa`，也可以通过 `APPOINTMENT_DB_USER` 覆盖。

## Android 端

- Android Gradle Plugin 7.2.1
- compileSdk 32
- minSdk 21
- Java 8

首次打开后，需要由 Android Studio 在本机生成 `local.properties`，该文件不会提交到 GitHub。

## 当前开发状态

Web 端已经实现基础注册、登录和角色页面跳转。预约大厅、我的预约、预约概览和项目管理页面目前主要使用静态演示数据，预约相关后端接口仍待完成。

Android 端包含登录、注册、预约大厅和我的预约等页面，目前尚未连接统一后端接口。
