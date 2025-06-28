<%--
  Created by IntelliJ IDEA.
  User: phand
  Date: 6/28/2025
  Time: 2:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <style>

        .table-content {
            width: 100%;
            height: 88%;
            padding: 5px;
        }

        .table-header {
            margin-bottom: 20px;
        }

        .table-header input {
            width: 100%;
            padding: 10px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
<div class="table-content">
    <div class="table-header d-flex">
        <select name="category" class="form-select w-25 me-2">
            <option value="">All Categories</option>
            <option value="electronics">Electronics</option>
            <option value="clothing">Clothing</option>
            <option value="home">Home</option>
        </select>
        <select name="status" class="form-select w-25 me-2">
            <option value="">All Status</option>
            <option value="true">In stock</option>
            <option value="false">Out of stock</option>
        </select>

        <input type="search" class="form-control w-5" placeholder="Enter keyword to search...">
        <button class="btn btn-primary ms-2">Add</button>
    </div>
    <div class="table-main-content">
        <table class="table table-striped table-bordered">
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Price</th>
                <th>Category</th>
                <th>Stock</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="product" items="${productList}">
                <tr>
                    <td>${product.productId}</td>
                    <td>${product.productName}</td>
                    <td>${product.price}</td>
                    <td>${product.category}</td>
                    <td>${product.stock}</td>
                    <td>${product.status ? 'In stock' : 'Out of stock'}</td>
                    <td>
                        <a href="editProduct?id=${product.productId}" class="btn btn-warning btn-sm">Edit</a>
                        <a href="deleteProduct?id=${product.productId}" class="btn btn-danger btn-sm">Delete</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
