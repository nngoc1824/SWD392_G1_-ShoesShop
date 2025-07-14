<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entites.Product, java.util.List" %>
<%
    Product product = (Product) request.getAttribute("product");
//    List<Size> sizeList = (List<Size>) request.getAttribute("sizeList");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%= product != null ? product.getProductName() : "Product Detail" %></title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="header.jsp" %>

<% if (product == null) { %>
<div class="container text-center mt-5">
    <h2 class="text-danger">Không tìm thấy sản phẩm</h2>
    <a href="home" class="btn btn-outline-primary mt-3">Quay lại trang chủ</a>
</div>
<% } else { %>

<!-- Breadcrumb -->
<div class="container mt-3">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb bg-white px-0">
            <li class="breadcrumb-item"><a href="home">Home</a></li>
            <li class="breadcrumb-item active" aria-current="page">Product Detail</li>
        </ol>
    </nav>
</div>

<!-- Product Detail -->
<div class="container mb-5">
    <div class="row">
        <!-- Image -->
        <div class="col-md-6">
            <img src="<%= product.getImage() %>" alt="Product Image" class="img-fluid rounded border">
        </div>

        <!-- Detail -->
        <div class="col-md-6">
            <h3><%= product.getProductName() %></h3>
            <p class="text-muted"><%= product.getDescription() %></p>
            <h4 class="text-primary">$<%= product.getPrice() %></h4>

            <form action="cart" method="post" class="col-md-4" >
                <input type="hidden" name="productId" value="<%= product.getProductId() %>"/>

<%--                <!-- Size -->--%>
<%--                <div class="form-group">--%>
<%--                    <label>Chọn size</label>--%>
<%--                    <select name="sizeId" class="form-control" required>--%>
<%--                        <option value="">-- Chọn size --</option>--%>
<%--                        <% if (sizeList != null) {--%>
<%--                            for (Size s : sizeList) { %>--%>
<%--                        <option value="<%= s.getSizeId() %>"><%= s.getSizeNumber() %></option>--%>
<%--                        <% } } %>--%>
<%--                    </select>--%>
<%--                </div>--%>

                <!-- Quantity -->
                <div class="input-group mb-3">
                    <div class="input-group-prepend">
                        <button type="button" class="btn btn-outline-secondary quantity-left-minus">
                            <span>-</span>
                        </button>
                    </div>
                    <input type="text" id="quantity" name="quantity" class="form-control text-center" value="1" min="1" max="100" required>
                    <div class="input-group-append">
                        <button type="button" class="btn btn-outline-secondary quantity-right-plus">
                            <span>+</span>
                        </button>
                    </div>
                </div>

                <!-- Submit -->
                <div class="text-center">
                    <button type="submit" class="btn btn-primary">
                        <i class="icon-shopping-cart"></i> Add to cart
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<% } %>

<!-- JS -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script>
    $(document).ready(function () {
        $('.quantity-right-plus').click(function (e) {
            e.preventDefault();
            let quantity = parseInt($('#quantity').val());
            $('#quantity').val(quantity + 1);
        });

        $('.quantity-left-minus').click(function (e) {
            e.preventDefault();
            let quantity = parseInt($('#quantity').val());
            if (quantity > 1) $('#quantity').val(quantity - 1);
        });
    });
</script>
</body>
</html>
