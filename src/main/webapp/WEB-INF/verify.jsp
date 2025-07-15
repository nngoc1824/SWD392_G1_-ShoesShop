<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Xác minh Email</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: Arial, sans-serif;
      background: url('https://images.pexels.com/photos/221457/pexels-photo-221457.jpeg?auto=compress&cs=tinysrgb&w=1600') no-repeat center center fixed;
      background-size: cover;
      height: 100vh;
    }

    .overlay {
      position: absolute;
      top: 0; left: 0;
      width: 100%; height: 100%;
      background: rgba(0, 0, 0, 0.6);
      z-index: 0;
    }

    .navbar {
      position: fixed;
      top: 0; left: 0;
      width: 100%;
      padding: 15px 30px;
      background-color: rgba(0, 92, 170, 0.9);
      display: flex;
      justify-content: space-between;
      align-items: center;
      color: white;
      z-index: 1;
    }

    .navbar .logo {
      font-size: 24px;
      font-weight: bold;
    }

    .navbar .nav-links a {
      color: white;
      text-decoration: none;
      margin-left: 20px;
    }

    .container {
      position: relative;
      z-index: 1;
      max-width: 400px;
      margin: 120px auto;
      background-color: white;
      padding: 30px;
      border-radius: 12px;
      box-shadow: 0 10px 25px rgba(0,0,0,0.3);
      text-align: center;
    }

    h2 {
      margin-bottom: 20px;
      color: #005caa;
    }

    input[type="text"] {
      width: 100%;
      padding: 12px;
      margin-top: 10px;
      margin-bottom: 20px;
      border: 1px solid #ccc;
      border-radius: 6px;
      font-size: 16px;
    }

    .btn {
      background-color: #005caa;
      color: white;
      border: none;
      padding: 12px;
      font-size: 16px;
      border-radius: 6px;
      width: 100%;
      cursor: pointer;
      transition: background-color 0.3s;
    }

    .btn:hover {
      background-color: #003f7f;
    }

    .error {
      color: red;
      margin-top: 15px;
    }

    .back-link {
      margin-top: 20px;
    }

    .back-link a {
      color: #005caa;
      font-weight: bold;
      text-decoration: underline;
    }

    @media screen and (max-width: 480px) {
      .container {
        margin: 80px 20px;
      }
    }
  </style>
</head>
<body>

<div class="overlay"></div>

<!-- Navbar -->
<div class="navbar">
  <div class="logo">Shoe Shop</div>
  <div class="nav-links">
    <a href="home.jsp">Trang chủ</a>
    <a href="register.jsp">Đăng ký</a>
  </div>
</div>

<!-- Verify Form -->
<div class="container">
  <h2>Nhập mã xác minh</h2>
  <form action="user?action=verify" method="post">
    <input type="text" name="code" placeholder="Mã xác minh" required maxlength="6" />
    <input type="submit" value="Xác minh" class="btn" />
  </form>


  <div class="error">
    <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
  </div>

  <div class="back-link">
    <a href="register.jsp">← Quay lại đăng ký</a>
  </div>
</div>

</body>
</html>
