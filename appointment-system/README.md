# Appointment System

一个同时包含 Java Web 端和 Android 客户端的多功能预约系统。Web 与 Android 共用同一套 Servlet、JDBC 和 SQL Server 后端，实现用户注册登录、项目浏览、预约、取消预约和预约记录查询，并提供管理员项目管理与预约总览功能。

## 项目结构

```text
appointment-system/
|-- web/
|   `-- src/
|       |-- dao/          数据访问层
|       |-- entity/       实体模型
|       |-- filter/       登录和管理员权限过滤器
|       |-- servlet/      Web 与 Android API
|       |-- util/         数据库连接工具
|       `-- web/          JSP、CSS、图片和 WEB-INF 依赖
|-- android/
|   `-- app/src/
|       |-- main/java/    Activity、Fragment、Adapter 和数据模型
|       `-- main/res/     页面布局、颜色、主题和图片资源
`-- README.md
```

Web 和 Android 是两个相对独立的子项目：Web 端可使用 IntelliJ IDEA 配合 Tomcat 运行，Android 端使用 Android Studio 和 Gradle 构建。

## 技术栈

### Web 端

- Java 21
- Apache Tomcat 9
- JSP + Servlet（`javax.servlet`）
- JDBC
- SQL Server
- Gson 2.13.1
- Vue 3（CDN 引入）

### Android 端

- Java 8
- Android Gradle Plugin 7.2.1
- Gradle 7.3.3
- compileSdk 32
- targetSdk 32
- minSdk 21
- AndroidX、Material Components
- `HttpURLConnection`

## 已实现功能

### 普通用户

- 用户注册、登录和退出
- 按分类浏览预约项目
- 查看项目地点、预约时间、容量和剩余名额
- 提交预约
- 查询当前用户的有效预约
- 取消预约并恢复项目名额
- 防止同一用户重复预约同一项目

### 管理员

- 根据用户角色进入管理员页面
- 查看全部项目
- 新增项目
- 删除没有有效预约的项目
- 查看全部用户的预约记录
- 管理员页面和接口权限过滤

### Android 客户端

- 注册与登录
- 使用 `JSESSIONID` 保持当前应用进程内的登录会话
- 加载预约大厅项目数据
- 提交预约并刷新剩余名额
- 加载当前用户预约记录
- 取消预约并刷新列表

## 数据库配置

数据库连接配置位于：

```text
web/src/util/DBUtil.java
```

默认配置：

```text
SQL Server 实例：localhost\SQL2022
数据库名称：AppointmentSystem
数据库用户：sa
```

数据库密码不应写入源码或提交到 Git。运行 Web 项目前必须设置：

```text
APPOINTMENT_DB_PASSWORD=你的数据库密码
```

如需修改数据库用户名，可设置：

```text
APPOINTMENT_DB_USER=你的数据库用户名
```

项目当前未包含数据库初始化脚本，需要提前准备以下数据表及对应字段：

```text
users
  id, username, password, phone, create_time, role

categories
  id, category_name

projects
  id, category_id, project_name, description, location,
  appointment_time, capacity, remaining_count

reservations
  id, reservation_code, user_id, project_id, reserve_time, status
```

代码对管理员角色使用固定值：

```text
管理员
```

除“管理员”之外的其他角色值会按普通用户处理。

预约状态使用：

```text
已预约
已取消
```

## Web 端运行

1. 使用 IntelliJ IDEA 打开项目根目录。
2. 配置 Java 21 和 Apache Tomcat 9。
3. 将 `web/src` 设置为源码目录。
4. 将 Web 根目录设置为 `web/src/web`。
5. 配置环境变量 `APPOINTMENT_DB_PASSWORD`。
6. 将应用部署到根上下文 `/`。
7. 启动 Tomcat。

当前开发配置使用端口 `8081`，启动后访问：

```text
http://localhost:8081/
```

如果新增了 Servlet 或修改了服务端 Java 文件，需要重新构建项目并重启 Tomcat，确保最新 `.class` 文件已经部署。

## Android 端运行

1. 使用 Android Studio 打开 `android` 目录。
2. 等待 Gradle 同步完成。
3. 确保 Web 服务已运行在电脑的 `8081` 端口。
4. 使用 Android 模拟器运行 `app`。

Android 模拟器通过以下地址访问宿主机：

```text
http://10.0.2.2:8081
```

真机测试时，不能使用 `10.0.2.2`，需要将客户端接口地址改为运行 Web 服务电脑的局域网 IP，并确保手机和电脑处于同一网络。

AndroidManifest 已声明网络权限，并为当前开发环境允许明文 HTTP：

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## 主要接口

| 方法 | 路径 | 说明 | 返回类型 |
|---|---|---|---|
| POST | `/register` | Web 用户注册 | 重定向/文本 |
| POST | `/login` | Web 用户登录 | 重定向/文本 |
| GET | `/logout` | 退出登录 | 重定向 |
| POST | `/api/register` | Android 用户注册 | JSON |
| POST | `/api/login` | Android 用户登录 | JSON |
| GET | `/api/projects` | Android 查询全部项目 | JSON |
| GET | `/categories` | 查询项目分类 | JSON |
| GET | `/projects?categoryId={id}` | 按分类查询项目 | JSON |
| POST | `/projects` | 管理员新增项目 | 文本 |
| GET | `/reservation` | 查询当前用户有效预约 | JSON |
| POST | `/reservation` | 创建预约 | 文本 |
| POST | `/reservation` | 使用 `action=cancel` 取消预约 | 文本 |
| GET | `/admin/projects` | 管理员查询全部项目 | JSON |
| DELETE | `/admin/projects?projectId={id}` | 管理员删除项目 | 文本 |
| GET | `/admin/reservations` | 管理员查询预约总览 | JSON |

### Android 登录示例

```text
POST /api/login
Content-Type: application/x-www-form-urlencoded

username=test&password=123456
```

### 创建预约示例

```text
POST /reservation
Content-Type: application/x-www-form-urlencoded

projectId=1
```

### 取消预约示例

```text
POST /reservation
Content-Type: application/x-www-form-urlencoded

action=cancel&reservationId=1
```

除注册、登录和静态资源外，大部分接口依赖登录 Session。Android 客户端通过 `CookieManager` 保存登录响应中的 `JSESSIONID`，并在后续请求中继续使用该 Session。

## 构建与测试

Android 编译：

```powershell
cd android
.\gradlew.bat compileDebugJavaWithJavac
```

Android 单元测试：

```powershell
cd android
.\gradlew.bat testDebugUnitTest
```

当前测试目录仍以 Android Studio 模板测试为主，尚未覆盖登录、预约、取消预约、权限和并发场景。

## 当前限制

- 密码目前以明文方式存储和比较，不适合生产环境。
- 开发环境使用明文 HTTP，正式环境应切换到 HTTPS。
- Android 服务地址直接写在客户端代码中，尚未区分开发和生产环境。
- Android 登录 Session 只保存在当前应用进程中，尚未实现持久登录。
- 创建预约与扣减名额、取消预约与恢复名额尚未放入同一数据库事务。
- 名额扣减缺少完整的并发保护。
- 取消预约接口仍需加强预约所有权校验。
- Android 列表页面缺少加载中、空数据、失败重试等状态提示。
- Web 端通过 CDN 加载 Vue 3，离线环境下页面可能无法正常初始化。
- 项目未提供 SQL Server 建表和初始化数据脚本。

## 后续计划

- 增加数据库初始化脚本和示例数据
- 使用密码哈希替代明文密码
- 为预约和取消操作增加事务与并发控制
- 完善接口状态码和统一 JSON 响应格式
- 增加 Android 退出登录、登录过期处理和网络状态提示
- 增加服务端、Android 和接口集成测试
- 将 Android 接口地址迁移到统一配置
- 增加项目编辑、分类管理和更完整的管理员操作
