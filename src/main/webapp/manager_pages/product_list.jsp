<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <title>Product List</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/manager_pages/product_list.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/manager_pages/table_product.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
          integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <style>
        * {
            font-family: 'Segoe UI', sans-serif;
            margin: 0;
            padding: 0;
        }

        .sidebar-collapsed {
            width: 0 !important;
            overflow: hidden;
            transition: all 0.3s ease-in-out;
        }

        .content {
            transition: all 0.3s ease-in-out;
        }

        .main-content {
            border-radius: 5px;
            width: 100%;
            height: 100%;
            background-color: #e5f1fd;
        }

        .card {
            font-size: 22px;
            border-radius: 5px;
            background-color: #ffffff !important;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            padding: 10px;
            border: none;
        }

        .table-container {
            margin-top: 20px;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

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

<div class="container-fluid p-0 d-flex">
    <div class="sidebar-container" id="sidebarContainer">
        <jsp:include page="../common/manager_sidebar.jsp"/>
    </div>
    <div class="content flex-grow-1" id="contentContainer">
        <div class="header px-2">
            <jsp:include page="../common/manager_header.jsp"/>
        </div>
        <div class="main-content p-3">
            <div class="card text-dark">
                Product list
            </div>
            <div class="table-container">
                <div class="table-content ">
                    <div class="table-header d-flex">
                        <select name="category" class="form-select w-25 me-2">
                            <option value="0">All Categories</option>
                            <c:forEach var="category" items="${categories}">
                                <option ${categoryCrr == category.categoryId ? "selected" : ""}
                                        value="${category.categoryId}">${category.categoryName}</option>
                            </c:forEach>
                        </select>
                        <select name="status" class="form-select w-25 me-2">
                            <option value="">All Status</option>
                            <option value="1" ${statusCrr ==  1 ? "selected" : ''}>In stock</option>
                            <option value="0" ${statusCrr ==  0 ? "selected" : ''}>Out of stock</option>
                        </select>

                        <input type="search" class="form-control w-5" placeholder="Enter keyword to search...">
                        <button class="btn btn-primary ms-2" onclick="window.location.href='?action=add-product'">Add</button>
                    </div>
                    <div class="table-main-content">
                        <table class="table table-hover table-bordered text-center">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Image</th>
                                <th>Name</th>
                                <th>Price</th>
                                <th>Category</th>
                                <th>Stock</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${empty productList}">
                                <tr>
                                    <td colspan="8" class="text-center">No products found</td>
                                </tr>
                            </c:if>
                            <c:forEach var="product" items="${productList}">
                                <tr>
                                    <td>${product.productId}</td>
                                    <td>
                                        <c:if test="${empty product.image}">
                                            No image
                                        </c:if>
                                        <c:if test="${not empty product.image}">
                                            <img src="${product.image}"
                                                 alt="${product.productName}" class="img-fluid"
                                                 style="width: 100px">
                                        </c:if>
                                    </td>
                                    <td>${product.productName}</td>
                                    <td>${product.price}</td>
                                    <td>
                                        <c:forEach var="category" items="${categories}">
                                            <c:if test="${category.categoryId == product.categoryId}">
                                                ${category.categoryName}
                                            </c:if>
                                        </c:forEach>
                                    </td>

                                    <td>${product.stock}</td>
                                        ${product.status == 1 ? '<td class= "text-success">In stock</td>' : '<td class="text-danger">Out of stock</td>'}
                                    <td>
                                        <a href="?action=update-product&id=${product.productId}" class="btn btn-warning btn-sm ">Edit</a>
                                        <c:if test="${product.status == 1}">
                                            <a href="?action=disable-product&id=${product.productId}"
                                               class="btn btn-danger btn-sm ms-3">Disable</a>
                                        </c:if>
                                        <c:if test="${product.status == 0}">
                                            <a href="?action=enable-product&id=${product.productId}"
                                               class="btn btn-success btn-sm ms-3">Enable</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                        <ul class="pagination">
                            <li class="page-item  ${currentPage == 1 ? "disabled" : ""}"><a
                                    href="product?pageNo=${currentPage - 1}&category=${categoryCrr}&status=${statusCrr}"
                                    class="page-link">Previous</a></li>
                            <c:forEach var="page" begin="1" end="${pageCount}">
                                <li class="page-item  ${currentPage == page ? "active" : ""}"><a a
                                                                                                 href="product?pageNo=${page}&category=${categoryCrr}&status=${statusCrr}"
                                                                                                 class="page-link">${page}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item  ${currentPage == pageCount ? "disabled" : ""}"><a
                                    href="product?pageNo=${currentPage + 1}&category=${categoryCrr}&status=${statusCrr}"
                                    class="page-link">Next</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    const filterCategory = document.querySelector("select[name='category']")
    console.log(filterCategory)
    filterCategory.addEventListener("change", (e) => {
        let category = filterCategory.value
        console.log(category)

        window.location.href = "http://localhost:8080/SWD392_ShoesShop_war_exploded/product?pageNo=${currentPage}&category=" + category + "&status=${statusCrr}";
    })
    const filterStatus = document.querySelector("select[name='status']")
    filterStatus.addEventListener("change", (e) => {
        let status = filterStatus.value
        console.log(status)
        window.location.href = "http://localhost:8080/SWD392_ShoesShop_war_exploded/product?pageNo=${currentPage}&status=" + status + "&category=${categoryCrr}";
    })

</script>
<script>
    const toggleBtn = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('sidebar');

    toggleBtn.addEventListener('click', function () {
        sidebar.classList.toggle('sidebar-collapsed');
    });
</script>


<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
        integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
        crossorigin="anonymous"></script>
</body>
</html>
