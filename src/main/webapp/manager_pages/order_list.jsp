<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<html>
<head>
    <title>Order Details</title>
    <style>
        table {
            width: 90%;
            margin: 30px auto;
            border-collapse: collapse;
        }

        th, td {
            border: 1px solid #999;
            padding: 10px;
            text-align: center;
        }

        img {
            width: 60px;
            height: 60px;
        }
    </style>
</head>
<body>

<h2 style="text-align:center;">Order #123 - Details</h2>

<table>
    <thead>
    <tr>
        <th>Product</th>
        <th>Cost</th>
        <th>Quantity</th>
        <th>Total</th>
    </tr>
    </thead>
    <tbody>
    <%-- Dữ liệu mẫu cho đơn hàng --%>
    <%
        class OrderItem {
            String name;
            int cost;
            int quantity;

            OrderItem(String name, int cost, int quantity) {
                this.name = name;
                this.cost = cost;
                this.quantity = quantity;
            }
        }

        List<OrderItem> items = Arrays.asList(
                new OrderItem("Shoes 1", 0, 1),
                new OrderItem("Shoes 2", 0, 1),
                new OrderItem("Shoes 3", 0, 1),
                new OrderItem("Shoes 4", 0, 1)
        );

        for (OrderItem item : items) {
    %>
    <tr>
        <td>
            <img src="placeholder.png" alt="Product Image"><br>
            <%= item.name %>
        </td>
        <td>đ<%= item.cost %></td>
        <td><%= item.quantity %></td>
        <td>đ<%= item.cost * item.quantity %></td>
    </tr>
    <% } %>
    </tbody>
</table>

</body>
</html>
