package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    private static final String DRIVER =
            "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static final String URL =
        "jdbc:sqlserver://localhost\\SQL2022;"
        + "databaseName=AppointmentSystem;"
        + "encrypt=true;"                // 不启用加密校验证书
        + "trustServerCertificate=true"; // 信任服务器证书

    private static final String USER =
            System.getenv().getOrDefault("APPOINTMENT_DB_USER", "sa");

    private static final String PASSWORD =
            System.getenv("APPOINTMENT_DB_PASSWORD");

    public static Connection getConnection() throws Exception {

        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new IllegalStateException(
                    "Environment variable APPOINTMENT_DB_PASSWORD is required"
            );
        }

        Class.forName(DRIVER);

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}
