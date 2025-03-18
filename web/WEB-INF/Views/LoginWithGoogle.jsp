<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đăng nhập với Google</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background: #ff0000; /* Red */
                background: -webkit-linear-gradient(to right, #ff0000, #ffffff, #000000);
                background: linear-gradient(to right, #ff0000, #ffffff, #000000);
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                margin: 0;
            }
            .container {
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
                text-align: center;
                width: 300px;
            }
            h2 {
                color: #333;
            }
            label {
                display: block;
                margin-bottom: 8px;
                font-weight: bold;
            }
            input {
                width: 100%;
                padding: 8px;
                margin-bottom: 15px;
                border: 1px solid #ccc;
                border-radius: 4px;
                box-sizing: border-box;
            }
            button {
                background-color: #4285F4;
                color: white;
                border: none;
                padding: 10px;
                width: 100%;
                border-radius: 4px;
                cursor: pointer;
                font-size: 16px;
            }
            button:hover {
                background-color: #357ae8;
            }
            .error {
                color: red;
                margin-top: 10px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Login With Google</h2>
            <form action="googleLoginServlet" method="post">
                <label for="email">Enter Email:</label>
                <input type="email" name="email" required>
                <button type="submit">Send Password</button>
            </form>
            <% if (session.getAttribute("error") != null) { %>
                <p class="error"><%= session.getAttribute("error") %></p>
                <% session.removeAttribute("error"); %>
            <% } %>
        </div>
    </body>
</html>
