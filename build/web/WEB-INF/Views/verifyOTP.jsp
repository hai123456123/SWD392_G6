<%-- 
    Document   : verifyOTP
    Created on : Mar 18, 2025, 2:08:33 AM
    Author     : DELL
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Xác nhận OTP</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                background: #ff0000; /* Red */
                background: -webkit-linear-gradient(to right, #ff0000, #ffffff, #000000);
                background: linear-gradient(to right, #ff0000, #ffffff, #000000);
            }
            .otp-container {
                background: white;
                padding: 2rem;
                border-radius: 10px;
                box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
                text-align: center;
            }
            .btn-custom {
                background-color: #ff4b2b;
                border: none;
            }
            .btn-custom:hover {
                background-color: #e03e25;
            }
        </style>
    </head>
    <body>
        <div class="otp-container">
            <h2 class="mb-3">Confirm Password</h2>
            <form action="verifyOTP" method="post">
                <div class="mb-3">
                    <label for="otp" class="form-label">Enter Password:</label>
                    <input type="text" name="otp" id="otp" class="form-control" required>
                </div>
                <button type="submit" class="btn btn-custom text-white w-100">Confirm</button>
            </form>
            <% if (session.getAttribute("error") != null) {%>
            <p class="mt-3 text-danger"><%= session.getAttribute("error")%></p>
            <% session.removeAttribute("error"); %>
            <% }%>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>