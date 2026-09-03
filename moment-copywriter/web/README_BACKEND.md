# Moment Copywriter Backend

This is a simple Servlet + JDBC backend for a WeChat Moments copywriting mini program.

## Project Layout

- `src/entity`: JavaBean data objects.
- `src/dao`: JDBC database access objects.
- `src/servlet`: HTTP API logic.
- `src/util`: database, JSON, CORS, password, and AI helper classes.
- `sql/init.sql`: SQL Server database initialization script.

## Required Runtime

- JDK 8 or newer for this source code.
- Tomcat 9.x or another Servlet container that supports `javax.servlet`.
- SQL Server.
- `gson-2.13.1.jar` in `src/web/WEB-INF/lib`.
- `mssql-jdbc-13.2.1.jre11.jar` in `src/web/WEB-INF/lib`.

If your Tomcat runs on Java 8, replace the copied SQL Server driver with a
`mssql-jdbc` build whose file name contains `jre8`.

## Environment Variables

Database:

```text
MOMENT_DB_URL=jdbc:sqlserver://localhost\SQL2022;databaseName=MomentCopywriter;encrypt=true;trustServerCertificate=true
MOMENT_DB_USER=sa
MOMENT_DB_PASSWORD=your_sql_server_password
```

AI service:

```text
AI_API_URL=https://api.openai.com/v1/chat/completions
AI_API_KEY=your_ai_api_key
AI_MODEL=your_model_name
```

The AI call uses an OpenAI-compatible chat completions request body.

## API List

Health check:

```text
GET /api/health
```

Register:

```text
POST /api/register
username=demo&password=123456&phone=13800000000
```

Login:

```text
POST /api/login
username=demo&password=123456
```

Logout:

```text
POST /api/logout
```

Current user:

```text
GET /api/current-user
```

Generate copywriting:

```text
POST /api/copywriting/generate
scene=coffee with friends&mood=relaxed&style=literary&keywords=weekend
```

History:

```text
GET /api/copywriting/history
```

Favorites:

```text
GET /api/copywriting/favorites
```

Add favorite:

```text
POST /api/copywriting/favorite/add
recordId=3
```

Remove favorite:

```text
POST /api/copywriting/favorite/delete
recordId=3
```

Delete history:

```text
POST /api/copywriting/delete
id=3
```

Clear history:

```text
POST /api/copywriting/clear-history
```

All APIs return JSON in this shape:

```json
{
  "success": true,
  "message": "OK",
  "data": {}
}
```
