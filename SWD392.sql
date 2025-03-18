-- Tạo bảng Brand (Thương hiệu)
CREATE TABLE Brand (
    brandId INT IDENTITY(1,1) PRIMARY KEY,
    brandName NVARCHAR(50) NOT NULL
);

-- Tạo bảng Category (Danh mục sản phẩm)
CREATE TABLE Category (
    categoryId INT IDENTITY(1,1) PRIMARY KEY,
    categoryName NVARCHAR(50) NOT NULL
);

-- Tạo bảng Product (Sản phẩm)
CREATE TABLE Product (
    productId INT IDENTITY(1,1) PRIMARY KEY,
    categoryId INT NOT NULL,
    brandId INT NOT NULL,
    productName NVARCHAR(100) NOT NULL,
    productImage NVARCHAR(255),
    price INT NOT NULL,
    description NVARCHAR(MAX),
    stock INT NOT NULL DEFAULT 0,
    chipName NVARCHAR(100),
    ramName NVARCHAR(50),
    romName NVARCHAR(100),
    isHidden BIT NOT NULL DEFAULT 0,
    CONSTRAINT FK_Product_Category FOREIGN KEY (categoryId) REFERENCES Category(categoryId),
    CONSTRAINT FK_Product_Brand FOREIGN KEY (brandId) REFERENCES Brand(brandId)
);

-- Tạo bảng Color (Màu sắc sản phẩm)
CREATE TABLE Color (
    colorId INT IDENTITY(1,1) PRIMARY KEY,
    colorName NVARCHAR(50) NOT NULL
);

-- Tạo bảng ProductVariant (Biến thể sản phẩm theo màu)
CREATE TABLE ProductVariant (
    productVariantId INT IDENTITY(1,1) PRIMARY KEY,
    productId INT NOT NULL,
    colorId INT NOT NULL,
    stock INT NOT NULL,
    CONSTRAINT FK_ProductVariant_Product FOREIGN KEY (productId) REFERENCES Product(productId),
    CONSTRAINT FK_ProductVariant_Color FOREIGN KEY (colorId) REFERENCES Color(colorId)
);

-- Tạo bảng Roles (Vai trò)
CREATE TABLE [Role] (
    roleId INT IDENTITY(1,1) PRIMARY KEY,
    roleName NVARCHAR(50) NOT NULL UNIQUE
);

-- Tạo bảng Users (Người dùng)
CREATE TABLE [User] (
    userId INT IDENTITY(1,1) PRIMARY KEY,
    email NVARCHAR(50) NOT NULL UNIQUE,
    password NVARCHAR(50) NOT NULL,
    firstName NVARCHAR(100),
    lastName NVARCHAR(100),
    phone VARCHAR(15),
    address NVARCHAR(1000),
    roleId INT NOT NULL,
    gender NVARCHAR(10),
    dob DATE,
    CONSTRAINT FK_Users_Roles FOREIGN KEY (roleId) REFERENCES Role(roleId)
);

-- Tạo bảng Feedback (Đánh giá sản phẩm)
CREATE TABLE Feedback (
    feedbackId INT IDENTITY(1,1) PRIMARY KEY,
    userId INT NOT NULL,
    productId INT NOT NULL,
    comment NVARCHAR(MAX),
    rate INT NOT NULL CHECK (rate >= 1 AND rate <= 5),
    createdAt DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Feedback_User FOREIGN KEY (userId) REFERENCES [User](userId),
    CONSTRAINT FK_Feedback_Product FOREIGN KEY (productId) REFERENCES Product(productId)
);

-- Tạo bảng Cart (Giỏ hàng)
CREATE TABLE Cart (
    cartId INT IDENTITY(1,1) PRIMARY KEY,
    userId INT NULL, -- Cho phép NULL để hỗ trợ khách vãng lai (guest)
    productId INT NOT NULL,
    productVariantId INT NOT NULL,
    quantity INT NOT NULL,
    price INT NOT NULL,
    CONSTRAINT FK_Cart_Product FOREIGN KEY (productId) REFERENCES Product(productId),
    CONSTRAINT FK_Cart_User FOREIGN KEY (userId) REFERENCES [User](userId),
    CONSTRAINT FK_Cart_ProductVariant FOREIGN KEY (productVariantId) REFERENCES ProductVariant(productVariantId)
);

-- Tạo bảng Status (Trạng thái đơn hàng)
CREATE TABLE [Status] (
    statusId INT IDENTITY(1,1) PRIMARY KEY,
    statusName NVARCHAR(100) NOT NULL
);

-- Tạo bảng Orders (Đơn hàng)
CREATE TABLE [Order] (
    orderId INT IDENTITY(1,1) PRIMARY KEY,
    userId INT NULL, -- Cho phép NULL để hỗ trợ khách vãng lai
    name NVARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    province NVARCHAR(100) NOT NULL,
    district NVARCHAR(100) NOT NULL,
    commune NVARCHAR(100) NOT NULL,
    detailedAddress NVARCHAR(1000),
    orderDate DATETIME DEFAULT GETDATE(),
    total INT NOT NULL, -- Tổng tiền sản phẩm (chưa bao gồm phí vận chuyển)
    shippingFee INT NOT NULL DEFAULT 0, -- Phí vận chuyển từ GHN
    grandTotal INT NOT NULL, -- Tổng tiền cuối cùng (total + shippingFee)
    statusId INT NOT NULL,
    CONSTRAINT FK_Orders_User FOREIGN KEY (userId) REFERENCES [User](userId),
    CONSTRAINT FK_Orders_Status FOREIGN KEY (statusId) REFERENCES [Status](statusId)
);

-- Tạo bảng OrderDetail (Chi tiết đơn hàng)
CREATE TABLE OrderDetail (
    orderDetailId INT IDENTITY(1,1) PRIMARY KEY,
    orderId INT NOT NULL,
    productId INT NOT NULL,
    nameProduct NVARCHAR(100) NOT NULL,
    productVariantId INT NOT NULL,
    price INT NOT NULL,
    quantity INT NOT NULL,
    total INT NOT NULL, -- Giá x Số lượng
    CONSTRAINT FK_OrderDetail_Order FOREIGN KEY (orderId) REFERENCES [Order](orderId),
    CONSTRAINT FK_OrderDetail_Product FOREIGN KEY (productId) REFERENCES Product(productId),
    CONSTRAINT FK_OrderDetail_ProductVariant FOREIGN KEY (productVariantId) REFERENCES ProductVariant(productVariantId)
);

-- Tạo bảng PaymentMethod (Phương thức thanh toán)
CREATE TABLE PaymentMethod (
    paymentMethodId INT IDENTITY(1,1) PRIMARY KEY,
    methodName NVARCHAR(50) NOT NULL -- 'COD', 'Online Payment', 'Bank Transfer'
);

-- Tạo bảng Payment (Thanh toán)
CREATE TABLE Payment (
    paymentId INT IDENTITY(1,1) PRIMARY KEY,
    orderId INT NOT NULL,
    paymentMethodId INT NOT NULL,
    date DATETIME NOT NULL DEFAULT GETDATE(),
    amount INT NOT NULL, -- Số tiền thanh toán
    status NVARCHAR(50) NOT NULL DEFAULT 'Pending', -- Pending, Completed, Failed
    CONSTRAINT FK_Payment_Order FOREIGN KEY (orderId) REFERENCES [Order](orderId),
    CONSTRAINT FK_Payment_Method FOREIGN KEY (paymentMethodId) REFERENCES PaymentMethod(paymentMethodId)
);

-- Tạo bảng Shipping (Thông tin giao hàng)
CREATE TABLE Shipping (
    shippingId INT IDENTITY(1,1) PRIMARY KEY,
    orderId INT NOT NULL,
    trackingNumber NVARCHAR(50) NULL, -- Mã vận đơn từ GHN
    shippingFee INT NOT NULL DEFAULT 0, -- Phí vận chuyển từ GHN
    estimatedDelivery DATE NULL,
    actualDelivery DATE NULL,
    status NVARCHAR(50) NOT NULL DEFAULT 'Pending', -- Pending, Processing, In Transit, Delivered, Canceled
    CONSTRAINT FK_Shipping_Order FOREIGN KEY (orderId) REFERENCES [Order](orderId)
);

-- Trigger cập nhật tồn kho tự động
GO
CREATE OR ALTER TRIGGER UpdateProductStock
ON ProductVariant
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    UPDATE p
    SET p.stock = ISNULL(pv.total_stock, 0)
    FROM Product p
    LEFT JOIN (
        SELECT productId, SUM(stock) AS total_stock
        FROM ProductVariant
        GROUP BY productId
    ) pv ON p.productId = pv.productId
    WHERE p.productId IN (
        SELECT DISTINCT productId FROM inserted
        UNION
        SELECT DISTINCT productId FROM deleted
    );
END;
GO

-- Tạo các bảng (giữ nguyên schema của bạn, không cần sửa)

-- Chèn dữ liệu mẫu
GO

-- Chèn dữ liệu mẫu vào bảng Brand
SET IDENTITY_INSERT Brand ON;
INSERT INTO Brand (brandId, brandName) VALUES
(1, 'Apple'), (2, 'Samsung'), (3, 'Xiaomi'), (4, 'OPPO'), (5, 'ASUS'),
(6, 'Sony'), (7, 'DELL'), (8, 'HP'), (9, 'LG'), (10, 'Acer');
SET IDENTITY_INSERT Brand OFF;

GO

-- Chèn dữ liệu mẫu vào bảng Category
SET IDENTITY_INSERT Category ON;
INSERT INTO Category (categoryId, categoryName) VALUES
(1, 'Laptop'), (2, 'Điện thoại'), (3, 'Tablet'), (4, 'Tai nghe'), (5, 'Smartwatch');
SET IDENTITY_INSERT Category OFF;

GO

-- Chèn dữ liệu mẫu vào bảng Product
INSERT INTO Product (categoryId, brandId, productName, productImage, price, description, stock, chipName, ramName, romName, isHidden) VALUES
(1, 1, 'MacBook Air M2', 'macbook_air_m2.jpg', 12000000, 'Laptop siêu mỏng của Apple', 10, 'M2', '8GB', '256GB', 0),
(2, 2, 'Samsung Galaxy S23', 'samsung_s23.jpg', 9000000, 'Điện thoại flagship của Samsung', 15, 'Snapdragon 8 Gen 2', '12GB', '256GB', 0),
(3, 3, 'Xiaomi Pad 5', 'xiaomi_pad_5.jpg', 6000000, 'Máy tính bảng giá rẻ hiệu năng cao', 20, 'Snapdragon 860', '6GB', '128GB', 0),
(4, 4, 'OPPO Enco X', 'oppo_enco_x.jpg', 2500000, 'Tai nghe True Wireless cao cấp', 30, NULL, NULL, NULL, 0),
(5, 5, 'ASUS ROG Zephyrus', 'asus_rog.jpg', 18000000, 'Laptop gaming cực mạnh', 5, 'Ryzen 9 6900HS', '32GB', '1TB SSD', 0);

GO

-- Chèn dữ liệu mẫu vào bảng Color
SET IDENTITY_INSERT Color ON;
INSERT INTO Color (colorId, colorName) VALUES
(1, 'Đen'), (2, 'Trắng'), (3, 'Xanh'), (4, 'Đỏ'), (5, 'Vàng');
SET IDENTITY_INSERT Color OFF;

GO

-- Chèn dữ liệu mẫu vào bảng ProductVariant 
INSERT INTO ProductVariant (productId, colorId, stock) VALUES
(1, 1, 5), (1, 2, 5), (2, 3, 8), (3, 4, 12), (4, 5, 10);

GO

-- Chèn dữ liệu mẫu vào bảng Role
INSERT INTO [Role] (roleName) VALUES
('Customer'), ('Admin'), ('Sale'), ('Guest');

GO

-- Chèn dữ liệu mẫu vào bảng User
INSERT INTO [User] (email, password, firstName, lastName, phone, address, roleId, gender, dob) VALUES
('john.doe@gmail.com', '123456', 'John', 'Doe', '0987654321', 'Hà Nội, Việt Nam', 1, 'Nam', '1995-06-15'),
('jane.smith@gmail.com', 'abcdef', 'Jane', 'Smith', '0975321678', 'TP. HCM, Việt Nam', 2, 'Nữ', '1992-03-22'),
('alex.nguyen@gmail.com', 'qwerty', 'Alex', 'Nguyen', '0909876543', 'Đà Nẵng, Việt Nam', 3, 'Nam', '1998-12-10'),
('susan.le@gmail.com', 'pass123', 'Susan', 'Le', '0912345678', 'Cần Thơ, Việt Nam', 4, 'Nữ', '2000-07-01');

GO

-- Chèn dữ liệu mẫu vào bảng Feedback
INSERT INTO Feedback (userId, productId, comment, rate, createdAt) VALUES
(1, 1, 'Sản phẩm rất tốt, đáng tiền!', 5, '2025-03-01 10:00:00'),
(2, 2, 'Màn hình đẹp, camera xuất sắc!', 4, '2025-03-02 14:30:00'),
(3, 3, 'Hiệu năng ổn định, pin trâu.', 4, '2025-03-03 09:15:00'),
(4, 4, 'Tai nghe bass mạnh, nghe rất thích!', 5, '2025-03-04 16:45:00');

GO

-- Chèn dữ liệu mẫu vào bảng Cart
INSERT INTO Cart (userId, productId, productVariantId, quantity, price) VALUES
(1, 1, 1, 1, 12000000),
(2, 2, 3, 1, 9000000),
(3, 3, 4, 2, 6000000),
(4, 4, 5, 1, 2500000);

GO

-- Chèn dữ liệu mẫu vào bảng Status
SET IDENTITY_INSERT [Status] ON;
INSERT INTO [Status] (statusId, statusName) VALUES
(1, 'Chờ xác nhận'), (2, 'Đang chuẩn bị'), (3, 'Đang giao hàng'), (4, 'Hoàn thành'), (5, 'Đã hủy');
SET IDENTITY_INSERT [Status] OFF;

GO

-- Chèn dữ liệu mẫu vào bảng Order
INSERT INTO [Order] (userId, name, phone, province, district, commune, detailedAddress, orderDate, total, shippingFee, grandTotal, statusId) VALUES
(1, 'John Doe', '0987654321', 'Hà Nội', 'Ba Đình', 'Điện Biên', 'Số 1 Tràng Tiền', '2025-03-01 08:00:00', 12000000, 30000, 12030000, 1),
(2, 'Jane Smith', '0975321678', 'TP. HCM', 'Quận 1', 'Bến Nghé', 'Số 5 Nguyễn Huệ', '2025-03-02 12:00:00', 9000000, 25000, 9025000, 2),
(3, 'Alex Nguyen', '0909876543', 'Đà Nẵng', 'Hải Châu', 'Hòa Cường', 'Số 10 Trần Phú', '2025-03-03 15:00:00', 12000000, 20000, 12020000, 3),
(4, 'Susan Le', '0912345678', 'Cần Thơ', 'Ninh Kiều', 'An Khánh', 'Số 15 Võ Văn Kiệt', '2025-03-04 09:00:00', 2500000, 15000, 2515000, 4);

GO

-- Chèn dữ liệu mẫu vào bảng OrderDetail
INSERT INTO OrderDetail (orderId, productId, nameProduct, productVariantId, price, quantity, total) VALUES
(1, 1, 'MacBook Air M2', 1, 12000000, 1, 12000000),
(2, 2, 'Samsung Galaxy S23', 3, 9000000, 1, 9000000),
(3, 3, 'Xiaomi Pad 5', 4, 6000000, 2, 12000000),
(4, 4, 'OPPO Enco X', 5, 2500000, 1, 2500000);

GO

-- Chèn dữ liệu mẫu vào bảng PaymentMethod
SET IDENTITY_INSERT PaymentMethod ON;
INSERT INTO PaymentMethod (paymentMethodId, methodName) VALUES
(1, 'COD'), (2, 'Online Payment'), (3, 'Bank Transfer');
SET IDENTITY_INSERT PaymentMethod OFF;

GO

-- Chèn dữ liệu mẫu vào bảng Payment
INSERT INTO Payment (orderId, paymentMethodId, date, amount, status) VALUES
(1, 1, '2025-03-01 08:05:00', 12030000, 'Pending'),
(2, 2, '2025-03-02 12:05:00', 9025000, 'Completed'),
(3, 1, '2025-03-03 15:05:00', 12020000, 'Pending'),
(4, 3, '2025-03-04 09:05:00', 2515000, 'Completed');

GO

-- Chèn dữ liệu mẫu vào bảng Shipping
INSERT INTO Shipping (orderId, trackingNumber, shippingFee, estimatedDelivery, actualDelivery, status) VALUES
(1, 'GHN123456', 30000, '2025-03-05', NULL, 'Pending'),
(2, 'GHN123457', 25000, '2025-03-06', NULL, 'In Transit'),
(3, 'GHN123458', 20000, '2025-03-07', '2025-03-06', 'Delivered'),
(4, 'GHN123459', 15000, '2025-03-08', NULL, 'Processing');

GO