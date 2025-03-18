<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Login</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
        <style>
            .gradient-custom {
                background: #ff0000; /* Red */
                background: -webkit-linear-gradient(to right, #ff0000, #ffffff, #000000);
                background: linear-gradient(to right, #ff0000, #ffffff, #000000);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
            }
            .card-custom {
                border-radius: 1rem;
                padding: 2rem;
                background-color: rgba(255, 255, 255, 0.9);
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
            }
            .btn-custom {
                background-color: #4e54c8;
                border: none;
                color: white;
                padding: 10px;
                border-radius: 5px;
                transition: all 0.3s;
            }
            .btn-custom:hover {
                background-color: #3b43a1;
            }
            .form-control {
                border-radius: 0.5rem;
            }
            .logo {
                height: 100px;
            }
        </style>
    </head>
    <body>
        <section class="gradient-custom">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-6">
                        <div class="card card-custom text-center">
                            <div class="card-body">
                                <h2 class="fw-bold text-primary mb-4">
                                    <a href="home">
                                        <img src="https://png.pngtree.com/template/20190422/ourlarge/pngtree-phone-store-logo-design-image_145177.jpg" alt="Logo" class="logo">
                                    </a>
                                </h2>
                                <p class="text-danger">${resetPasswordDone}</p>
                                <p class="text-danger">${mess}</p>
                                <form action="login" method="post">
                                    <div class="form-group text-left">
                                        <label class="text-primary">*Email</label>
                                        <input type="email" name="email" class="form-control" required>
                                    </div>
                                    <div class="form-group text-left">
                                        <label class="text-primary">*Password</label>
                                        <input type="password" name="password" class="form-control" required>
                                    </div>
                                    <button type="submit" class="btn btn-custom btn-block">Login</button>
                                </form>
                                <a href="googleLoginServlet" class="btn btn-outline-primary btn-block mt-3">
                                    <img src="https://developers.google.com/identity/images/g-logo.png" style="height: 20px; margin-right: 10px;"> Login with Google
                                </a>
                                <p class="mt-3">
                                    <a href="Capcha" class="text-primary">Forgot Password?</a>
                                    &nbsp; | &nbsp;
                                    <a href="register" class="text-primary">Create new Account</a>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
