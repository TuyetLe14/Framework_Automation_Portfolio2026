package com.tuyet.db;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class DbBaseTest {

    protected Connection conn;

    @BeforeMethod(alwaysRun = true)
    public void openConnection() throws SQLException {
        DatabaseManager.resetDatabase();
        conn = DatabaseManager.getConnection();
    }

    @AfterMethod(alwaysRun = true)
    public void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    protected long countOf(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = prepare(sql, params);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        }
    }

    protected BigDecimal decimalOf(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = prepare(sql, params);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getBigDecimal(1);
        }
    }

    protected List<String> stringColumn(String sql, Object... params) throws SQLException {
        List<String> values = new ArrayList<>();
        try (PreparedStatement ps = prepare(sql, params);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                values.add(rs.getString(1));
            }
        }
        return values;
    }

    protected int execute(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = prepare(sql, params)) {
            return ps.executeUpdate();
        }
    }

    private PreparedStatement prepare(String sql, Object... params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps;
    }
}
