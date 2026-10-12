package com.tuyet.db;

import com.tuyet.constants.ConfigsData;
import org.h2.tools.RunScript;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseManager {

    private DatabaseManager() {
    }

    private static String url() {
        return System.getProperty("db.url", ConfigsData.getProperty(
                "db.url", "jdbc:h2:mem:portfolio_qa;DB_CLOSE_DELAY=-1"));
    }

    private static String username() {
        return System.getProperty("db.username",
                ConfigsData.getProperty("db.username", "sa"));
    }

    private static String password() {
        return System.getProperty("db.password",
                ConfigsData.getProperty("db.password", ""));
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url(), username(), password());
    }

    /** Xóa sạch và nạp lại schema + dữ liệu mẫu để mỗi test độc lập. */
    public static void resetDatabase() {
        try (Connection conn = getConnection()) {
            try (Statement st = conn.createStatement()) {
                st.execute("DROP ALL OBJECTS");
            }
            runScript(conn, "db/schema.sql");
            runScript(conn, "db/data.sql");
        } catch (Exception e) {
            throw new IllegalStateException("Cannot reset test database", e);
        }
    }

    private static void runScript(Connection conn, String resource) throws Exception {
        try (InputStream in = DatabaseManager.class.getClassLoader()
                .getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("Missing script: " + resource);
            }
            RunScript.execute(conn, new InputStreamReader(in, StandardCharsets.UTF_8));
        }
    }
}
