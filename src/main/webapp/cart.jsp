<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entites.CartItem, entites.Product, service.ProductService, java.util.List" %>
<%
    List<CartItem> cartItems = (List<CartItem>) request.getAttribute("cartItems");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Your Shopping Cart</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container mt-5">
    <h3 class="mb-4">Your Shopping Cart</h3>

    <% if (cartItems == null || cartItems.isEmpty()) { %>
    <div class="alert alert-info">Your cart is currently empty.</div>
    <% } else { %>
    <table class="table table-bordered">
        <thead class="thead-light">
        <tr>
            <th>Image</th>
            <th>Product Name</th>
            <th>Unit Price</th>
            <th>Quantity</th>
            <th>Subtotal</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <% double total = 0; %>
        <% ProductService productService = new ProductService();%>
        <% for (CartItem item : cartItems) {
            Product product = productService.getProductById(item.getProductId());
            double subTotal = product.getPrice() * item.getQuantity();
            total += subTotal;
        %>
        <tr>
            <td><img src="<%= product.getImage() %>" alt="image" width="80"></td>
            <td><%= product.getProductName() %></td>
            <td>$<%= String.format("%.2f", product.getPrice()) %></td>
            <td>
                <form method="post" action="cart">
                    <input type="hidden" name="productId" value="<%= product.getProductId() %>">
                    <input type="number" name="quantity" value="<%= item.getQuantity() %>" min="1" class="form-control w-50 d-inline">
                    <button type="submit" class="btn btn-sm btn-primary ml-2">Update</button>
                </form>
            </td>
            <td>$<%= String.format("%.2f", subTotal) %></td>
            <td>
                <a href="cart?action=remove&productId=<%= product.getProductId() %>" class="btn btn-danger btn-sm">Remove</a>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>

    <div class="text-right">
        <h4>Total: $<%= String.format("%.2f", total) %></h4>
        <a href="confirmOrder.jsp" class="btn btn-success mt-2">Proceed to Checkout</a>
    </div>
    <% } %>
</div>

</body>
</html>
