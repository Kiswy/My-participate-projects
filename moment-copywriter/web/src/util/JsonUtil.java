package util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    private static final Gson GSON = new Gson();
    private static final Type MAP_TYPE = new TypeToken<Map<String, Object>>() {
    }.getType();

    public static Map<String, Object> readJsonObject(HttpServletRequest request)
            throws IOException {
        String contentType = request.getContentType();
        if (contentType == null
                || !contentType.toLowerCase().contains("application/json")) {
            return new HashMap<>();
        }

        StringBuilder builder = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        String body = builder.toString().trim();
        if (body.isEmpty()) {
            return new HashMap<>();
        }

        try {
            Map<String, Object> data = GSON.fromJson(body, MAP_TYPE);
            return data == null ? new HashMap<>() : data;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public static String getString(
            HttpServletRequest request,
            Map<String, Object> body,
            String name
    ) {
        String parameter = request.getParameter(name);
        if (parameter != null && !parameter.trim().isEmpty()) {
            return parameter.trim();
        }

        Object value = body.get(name);
        if (value == null) {
            return null;
        }

        String text = String.valueOf(value).trim();
        return text.isEmpty() ? null : text;
    }

    public static int getInt(
            HttpServletRequest request,
            Map<String, Object> body,
            String name,
            int defaultValue
    ) {
        String parameter = request.getParameter(name);
        if (parameter != null && !parameter.trim().isEmpty()) {
            return parseInt(parameter, defaultValue);
        }

        Object value = body.get(name);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        if (value != null) {
            return parseInt(String.valueOf(value), defaultValue);
        }

        return defaultValue;
    }

    public static void writeJson(HttpServletResponse response, Object data)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(GSON.toJson(data));
    }

    public static Map<String, Object> success(Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "OK");
        result.put("data", data);
        return result;
    }

    public static Map<String, Object> fail(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        return result;
    }

    private static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
