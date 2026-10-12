INSERT INTO customers (full_name, email, city, created_at) VALUES
    ('Nguyen Van An', 'an.nguyen@example.com', 'Ho Chi Minh', DATE '2026-01-10'),
    ('Tran Thi Binh', 'binh.tran@example.com', 'Ha Noi', DATE '2026-02-15'),
    ('Le Van Cuong', 'cuong.le@example.com', 'Da Nang', DATE '2026-03-20'),
    ('Pham Thi Dung', 'dung.pham@example.com', 'Ho Chi Minh', DATE '2026-04-05'),
    ('Hoang Van Em', 'em.hoang@example.com', 'Can Tho', DATE '2026-05-12');

INSERT INTO products (name, price, stock) VALUES
    ('Keyboard', 50.00, 20),
    ('Mouse', 20.00, 50),
    ('Monitor', 200.00, 10),
    ('Headset', 35.50, 0);

INSERT INTO orders (customer_id, status, total_amount, created_at) VALUES
    (1, 'PAID', 90.00, DATE '2026-02-01'),
    (1, 'SHIPPED', 200.00, DATE '2026-02-20'),
    (2, 'NEW', 131.00, DATE '2026-03-05'),
    (3, 'CANCELLED', 100.00, DATE '2026-04-01'),
    (4, 'PAID', 450.00, DATE '2026-05-10'),
    (2, 'SHIPPED', 20.00, DATE '2026-05-25');

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
    (1, 1, 1, 50.00),
    (1, 2, 2, 20.00),
    (2, 3, 1, 200.00),
    (3, 2, 3, 20.00),
    (3, 4, 2, 35.50),
    (4, 1, 2, 50.00),
    (5, 3, 2, 200.00),
    (5, 1, 1, 50.00),
    (6, 2, 1, 20.00);
