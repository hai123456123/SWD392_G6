<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Thanh toán VNPay</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 0;
                background-color: #f5f5f5;
            }
            .container {
                width: 60%;
                margin: 50px auto;
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            }
            h2 {
                text-align: center;
                color: #333;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }
            th, td {
                border: 1px solid #ddd;
                padding: 10px;
                text-align: left;
            }
            th {
                background-color: #4CAF50;
                color: white;
            }
            .total {
                text-align: right;
                font-weight: bold;
                margin-top: 10px;
            }
            .btn-checkout {
                display: block;
                width: 100%;
                text-align: center;
                margin-top: 20px;
                padding: 10px;
                background: #007bff;
                color: white;
                border: none;
                cursor: pointer;
                border-radius: 5px;
                text-decoration: none;
            }
            .btn-checkout:hover {
                background: #0056b3;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Xác nhận đơn hàng</h2>

            <h3>Thông tin khách hàng</h3>
            <table>
                <tr>
                    <th>Email</th>
                    <td><%= request.getAttribute("email") %></td>
                </tr>
                <tr>
                    <th>Họ và tên</th>
                    <td><%= request.getAttribute("fullName") %></td>
                </tr>
                <tr>
                    <th>Số điện thoại</th>
                    <td><%= request.getAttribute("phone") %></td>
                </tr>
                <tr>
                    <th>Tỉnh/Thành phố</th>
                    <td><%= request.getAttribute("province") %></td>
                </tr>
                <tr>
                    <th>Quận/Huyện</th>
                    <td><%= request.getAttribute("district") %></td>
                </tr>
                <tr>
                    <th>Phường/Xã</th>
                    <td><%= request.getAttribute("ward") %></td>
                </tr>
                <tr>
                    <th>Địa chỉ cụ thể</th>
                    <td><%= request.getAttribute("address") %></td>
                </tr>
            </table>

            <h3>Chi tiết đơn hàng</h3>
            <table>
                <tr>
                    <th>Thành tiền</th>
                    <td><p class="total">Tổng tiền: <%= request.getAttribute("totalPrice") %> VNĐ</p></td>
                </tr>

            </table>


            <form action="checkout" method="POST">
                <input type="hidden" name="email" value="<%= request.getAttribute("email") %>">
                <input type="hidden" name="fullName" value="<%= request.getAttribute("fullName") %>">
                <input type="hidden" name="phone" value="<%= request.getAttribute("phone") %>">
                <input type="hidden" name="province" value="<%= request.getAttribute("province") %>">
                <input type="hidden" name="district" value="<%= request.getAttribute("district") %>">
                <input type="hidden" name="ward" value="<%= request.getAttribute("ward") %>">
                <input type="hidden" name="address" value="<%= request.getAttribute("address") %>">
                <input type="hidden" name="totalPrice" value="<%= request.getAttribute("totalPrice") %>">

                <button type="submit" class="btn-checkout">Thanh toán với VNPay</button>
            </form>

        </div>
    </body>
</html>
