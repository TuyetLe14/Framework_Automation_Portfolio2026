package com.tuyet.db;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class OrdersDbTest extends DbBaseTest {

    @Test(groups = {"db", "smoke"}, description = "Dữ liệu mẫu được nạp đủ")
    public void seedDataIsLoaded() throws SQLException {
        Assert.assertEquals(countOf("SELECT COUNT(*) FROM customers"), 5);
        Assert.assertEquals(countOf("SELECT COUNT(*) FROM products"), 4);
        Assert.assertEquals(countOf("SELECT COUNT(*) FROM orders"), 6);
        Assert.assertEquals(countOf("SELECT COUNT(*) FROM order_items"), 9);
    }

    @DataProvider(name = "dataQualityRules")
    public Object[][] dataQualityRules() {
        return new Object[][]{
                {"Email khách hàng không trùng",
                        "SELECT COUNT(*) FROM (SELECT email FROM customers "
                                + "GROUP BY email HAVING COUNT(*) > 1) d"},
                {"Mọi đơn hàng đều có khách hàng",
                        "SELECT COUNT(*) FROM orders o LEFT JOIN customers c "
                                + "ON o.customer_id = c.id WHERE c.id IS NULL"},
                {"Mọi dòng chi tiết đều thuộc một đơn hàng",
                        "SELECT COUNT(*) FROM order_items i LEFT JOIN orders o "
                                + "ON i.order_id = o.id WHERE o.id IS NULL"},
                {"Tổng tiền đơn bằng tổng các dòng chi tiết",
                        "SELECT COUNT(*) FROM orders o WHERE o.total_amount <> "
                                + "(SELECT SUM(i.quantity * i.unit_price) "
                                + "FROM order_items i WHERE i.order_id = o.id)"},
                {"Đơn hàng nào cũng có ít nhất một dòng chi tiết",
                        "SELECT COUNT(*) FROM orders o WHERE NOT EXISTS "
                                + "(SELECT 1 FROM order_items i WHERE i.order_id = o.id)"},
                {"Tồn kho không âm",
                        "SELECT COUNT(*) FROM products WHERE stock < 0"}
        };
    }

    @Test(groups = {"db"}, dataProvider = "dataQualityRules",
            description = "Quy tắc chất lượng dữ liệu: không có bản ghi vi phạm")
    public void dataQualityRule(String rule, String violationSql) throws SQLException {
        Assert.assertEquals(countOf(violationSql), 0,
                "Có bản ghi vi phạm quy tắc: " + rule);
    }

    @DataProvider(name = "constraintViolations")
    public Object[][] constraintViolations() {
        return new Object[][]{
                {"Email trùng",
                        "INSERT INTO customers (full_name, email, city, created_at) "
                                + "VALUES ('Dup', 'an.nguyen@example.com', 'Ho Chi Minh', DATE '2026-06-01')",
                        "23505"},
                {"Tên để trống",
                        "INSERT INTO customers (full_name, email, city, created_at) "
                                + "VALUES (NULL, 'new@example.com', 'Ho Chi Minh', DATE '2026-06-01')",
                        "23502"},
                {"Đơn hàng của khách không tồn tại",
                        "INSERT INTO orders (customer_id, status, total_amount, created_at) "
                                + "VALUES (999, 'NEW', 10.00, DATE '2026-06-01')",
                        "23506"},
                {"Trạng thái đơn hàng không hợp lệ",
                        "INSERT INTO orders (customer_id, status, total_amount, created_at) "
                                + "VALUES (1, 'UNKNOWN', 10.00, DATE '2026-06-01')",
                        "23513"},
                {"Giá sản phẩm âm",
                        "INSERT INTO products (name, price, stock) VALUES ('Bad', -5.00, 1)",
                        "23513"},
                {"Số lượng mua bằng 0",
                        "INSERT INTO order_items (order_id, product_id, quantity, unit_price) "
                                + "VALUES (1, 1, 0, 50.00)",
                        "23513"}
        };
    }

    @Test(groups = {"db"}, dataProvider = "constraintViolations",
            description = "Database từ chối dữ liệu sai quy tắc")
    public void databaseRejectsInvalidData(String caseName, String insertSql,
                                           String expectedSqlState) {
        SQLException ex = Assert.expectThrows(SQLException.class, () -> execute(insertSql));
        Assert.assertEquals(ex.getSQLState(), expectedSqlState,
                "Sai mã lỗi cho trường hợp: " + caseName);
    }

    @Test(groups = {"db"}, description = "Khách hàng chưa từng đặt hàng (LEFT JOIN)")
    public void customersWithoutOrders() throws SQLException {
        List<String> emails = stringColumn(
                "SELECT c.email FROM customers c LEFT JOIN orders o "
                        + "ON o.customer_id = c.id WHERE o.id IS NULL");
        Assert.assertEquals(emails, List.of("em.hoang@example.com"));
    }

    @Test(groups = {"db"}, description = "Doanh thu các đơn PAID và SHIPPED")
    public void revenueOfPaidAndShippedOrders() throws SQLException {
        BigDecimal revenue = decimalOf(
                "SELECT SUM(total_amount) FROM orders WHERE status IN ('PAID', 'SHIPPED')");
        Assert.assertEquals(revenue.compareTo(new BigDecimal("760.00")), 0,
                "Doanh thu sai: " + revenue);
    }

    @Test(groups = {"db"}, description = "Khách chi tiêu nhiều nhất (bỏ đơn đã hủy)")
    public void topCustomerBySpend() throws SQLException {
        List<String> top = stringColumn(
                "SELECT c.email FROM customers c JOIN orders o ON o.customer_id = c.id "
                        + "WHERE o.status <> 'CANCELLED' GROUP BY c.email "
                        + "ORDER BY SUM(o.total_amount) DESC LIMIT 1");
        Assert.assertEquals(top, List.of("dung.pham@example.com"));
    }

    @Test(groups = {"db"}, description = "Tham số truy vấn được xem là dữ liệu, không phải câu lệnh")
    public void preparedStatementTreatsInputAsData() throws SQLException {
        String malicious = "x' OR '1'='1";
        Assert.assertEquals(
                countOf("SELECT COUNT(*) FROM customers WHERE email = ?", malicious), 0);
        Assert.assertEquals(
                countOf("SELECT COUNT(*) FROM customers WHERE email = ?",
                        "an.nguyen@example.com"), 1);
    }

    @Test(groups = {"db"}, description = "Rollback hủy thay đổi chưa commit")
    public void transactionRollbackUndoesChanges() throws SQLException {
        conn.setAutoCommit(false);
        execute("INSERT INTO customers (full_name, email, city, created_at) "
                        + "VALUES (?, ?, ?, ?)",
                "Temp User", "temp@example.com", "Ho Chi Minh",
                java.sql.Date.valueOf("2026-06-01"));
        Assert.assertEquals(countOf("SELECT COUNT(*) FROM customers"), 6);

        conn.rollback();

        Assert.assertEquals(countOf("SELECT COUNT(*) FROM customers"), 5);
    }

    @Test(groups = {"db"}, description = "Cập nhật trạng thái đơn hàng")
    public void updateOrderStatus() throws SQLException {
        int updated = execute("UPDATE orders SET status = ? WHERE id = ?", "CANCELLED", 3);
        Assert.assertEquals(updated, 1);
        Assert.assertEquals(
                stringColumn("SELECT status FROM orders WHERE id = ?", 3).get(0),
                "CANCELLED");
    }
}
