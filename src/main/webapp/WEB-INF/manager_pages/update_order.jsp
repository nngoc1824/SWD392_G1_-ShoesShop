<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<html>
<head>
    <title>Update Order Details</title>
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

        .quantity-btn {
            padding: 0 10px;
            font-size: 16px;
            cursor: pointer;
        }

        .action-btn {
            padding: 5px 10px;
            margin: 2px;
            border-radius: 5px;
            border: 1px solid #666;
            cursor: pointer;
            background-color: #eee;
        }
    </style>
</head>
<body>

<h2 style="text-align:center;">Update Order Details</h2>

<table>
    <thead>
    <tr>
        <th>Product</th>
        <th>Cost</th>
        <th>Quantity</th>
        <th>Total</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <%-- Dữ liệu mẫu --%>
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

            int getTotal() {
                return cost * quantity;
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
        <td><%= item.cost %>đ</td>
        <td>
            <button class="quantity-btn">-</button>
            <%= item.quantity %>
            <button class="quantity-btn">+</button>
        </td>
        <td><%= item.getTotal() %>đ</td>
        <td>
            <button class="action-btn">Delete</button>
            <button class="action-btn">Edit</button>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>

</body>
</html>
