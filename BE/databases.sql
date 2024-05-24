CREATE DATABASE shopapp;
use shopapp;

CREATE TABLE users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(100) NOT NULL,
    email VARCHAR(100) DEFAULT '',
    password VARCHAR(100) NOT NULL,
    address VARCHAR(100) DEFAULT '',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
ALTER TABLE users ADD is_active TINYINT(1) DEFAULT 1;
ALTER TABLE users ADD date_of_birth DATE;
ALTER TABLE users ADD facebook_id INT DEFAULT 0;
ALTER TABLE users ADD google_id INT DEFAULT 0;
ALTER TABLE users MODIFY COLUMN password VARCHAR(255) NOT NULL DEFAULT '';
ALTER TABLE users ADD role_id INT;

-- bảng vai trò
CREATE TABLE roles(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) DEFAULT ''
);
ALTER TABLE users ADD FOREIGN KEY (role_id) REFERENCES roles(id);

-- tương tự như khoá nhà
CREATE TABLE tokens(
    id INT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(100) NOT NULL,
    expriration_date DATETIME,
    revoked TINYINT(1) NOT NULL,
    expired TINYINT(1) NOT NULL,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
);

-- hỗ trợ đăng nhập facebook, google
CREATE TABLE social_accounts(
    id INT PRIMARY KEY AUTO_INCREMENT,
    provider VARCHAR(100) NOT NULL COMMENT 'tên nhà social, facebook, google, ...',
    provider_id VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL COMMENT 'email của người dùng',
    name VARCHAR(100) NOT NULL COMMENT 'tên người dùng',
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
);

-- danh mục sản phẩm
CREATE TABLE categories(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL DEFAULT '' COMMENT 'tên danh mục'
);

-- sản phẩm
CREATE TABLE products(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(350) NOT NULL DEFAULT '' COMMENT 'tên sản phẩm',
    price DECIMAL(10, 2) NOT NULL COMMENT 'giá sản phẩm',
    image VARCHAR(255) DEFAULT '' COMMENT 'ảnh sản phẩm',
    description LONGTEXT DEFAULT '' COMMENT 'mô tả sản phẩm',
    category_id INT COMMENT 'id danh mục',
    FOREIGN KEY (category_id) REFERENCES categories(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- bảng ảnh sản phẩm
CREATE TABLE product_images(
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT COMMENT 'id sản phẩm',
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE COMMENT "cha mẹ die con ngỏm",
    image_url VARCHAR(255)
);

-- bảng đặt hàng
CREATE TABLE orders(
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100) COMMENT 'tên người đặt hàng',
    email VARCHAR(100) COMMENT 'email người đặt hàng',
    phone_number VARCHAR(100) NOT NULL COMMENT 'số điện thoại người đặt hàng',
    address VARCHAR(255) NOT NULL COMMENT 'địa chỉ người đặt hàng',
    note  LONGTEXT DEFAULT '' COMMENT 'ghi chú',
    user_id INT COMMENT 'id người đặt hàng',
    FOREIGN KEY (user_id) REFERENCES users(id),
    total_monney DECIMAL(10, 2) NOT NULL COMMENT 'tổng tiền',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(100) NOT NULL DEFAULT 'pending' COMMENT 'trạng thái đơn hàng'
);
ALTER TABLE orders ADD COLUMN shipping_method VARCHAR(100);
ALTER TABLE orders ADD COLUMN payment_method VARCHAR(100);
ALTER TABLE orders ADD COLUMN shipping_date TIMESTAMP;
ALTER TABLE orders ADD COLUMN tracking_number VARCHAR(100);
ALTER TABLE orders ADD COLUMN shipping_address VARCHAR(255);
-- xoá đơn hàng -> xoá mềm ->thêm trường active
ALTER TABLE orders ADD COLUMN active TINYINT(1);
--trạng thái đơn hàng: pending, confirmation, processing, shipping, shipped, completed, canceled
ALTER TABLE orders MODIFY COLUMN status ENUM('pending', 'confirmation', 'processing', 'shipping', 'shipped', 'completed', 'canceled') 
COMMENT 'trạng thái đơn hàng';


-- chi tiết đặt hàng
CREATE TABLE order_details(
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT COMMENT 'id đơn hàng',
    FOREIGN KEY (order_id) REFERENCES orders(id),
    product_id INT COMMENT 'id sản phẩm',
    FOREIGN KEY (product_id) REFERENCES products(id),
    quantity INT NOT NULL COMMENT 'số lượng sản phẩm',
    price DECIMAL(10, 2) NOT NULL COMMENT 'giá sản phẩm',
    total_monney DECIMAL(10, 2) NOT NULL COMMENT 'tổng tiền',
    color VARCHAR(100) DEFAULT '' COMMENT 'màu sắc sản phẩm',
);
-- bảng giỏ hàng
Create TABLE carts(
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT COMMENT 'id người dùng',
    FOREIGN KEY (user_id) REFERENCES users(id),
    product_id INT COMMENT 'id sản phẩm',
    FOREIGN KEY (product_id) REFERENCES products(id),
    quantity INT NOT NULL COMMENT 'tổng số lượng sản phẩm'
)
-- bảng phương thức thanh toán
CREATE TABLE payment_methods(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT 'tên phương thức thanh toán',
    description VARCHAR(255) DEFAULT '' COMMENT 'mô tả phương thức thanh toán'
);
-- -- bảng chi tiết phương thức cụ thể là thẻ thanh toán quôc tế
-- CREATE TABLE credit_cards(
--     id INT PRIMARY KEY AUTO_INCREMENT,
--     card_number VARCHAR(100) NOT NULL COMMENT 'số thẻ',
--     card_holder VARCHAR(100) NOT NULL COMMENT 'tên chủ thẻ',
--     expired_date DATE NOT NULL COMMENT 'ngày hết hạn',
--     cvv VARCHAR(10) NOT NULL COMMENT 'mã cvv',
--     user_id INT COMMENT 'id người dùng',
--     FOREIGN KEY (user_id) REFERENCES users(id),
--     payment_method_id INT COMMENT 'id phương thức thanh toán',
--     FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id)
-- );
-- -- bảng phương thức thanh toán thẻ atm nội địa
-- CREATE TABLE atm_cards(
--     id INT PRIMARY KEY AUTO_INCREMENT,
--     card_number VARCHAR(100) NOT NULL COMMENT 'số thẻ',
--     card_holder VARCHAR(100) NOT NULL COMMENT 'tên chủ thẻ',
--     issued_date DATE NOT NULL COMMENT 'ngày phát hành thẻ',
--     bank_name VARCHAR(100) NOT NULL COMMENT 'tên ngân hàng',
--     user_id INT COMMENT 'id người dùng',
--     FOREIGN KEY (user_id) REFERENCES users(id),
--     payment_method_id INT COMMENT 'id phương thức thanh toán',
--     FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id)
-- );
-- -- bảng phương thức thanh toán ví điện tử
-- CREATE TABLE e_wallets(
--     id INT PRIMARY KEY AUTO_INCREMENT,
--     wallet_number VARCHAR(100) NOT NULL COMMENT 'số ví',
--     user_id INT COMMENT 'id người dùng',
--     FOREIGN KEY (user_id) REFERENCES users(id),
--     payment_method_id INT COMMENT 'id phương thức thanh toán',
--     FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id)
-- );