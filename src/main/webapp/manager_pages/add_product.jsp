<%@ page import="entites.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add Product</title>
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
            border-radius: 10px;
            background-color: #ffffff !important;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            padding: 15px;
            border: none;
            margin-bottom: 20px;
        }

        label {
            font-weight: bold;
            font-size: 16px;
            margin-bottom: 5px;
        }


        .image-title {
            font-weight: bold;
            margin-bottom: 15px;
        }

        .upload-area {
            border: 2px dashed #92b5e7;
            border-radius: 12px;
            padding: 15px;
            cursor: pointer;
            position: relative;
            display: block;
            min-height: 150px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .upload-area input[type="file"] {
            display: none;
        }

        .upload-area img {
            max-width: 100px;
            max-height: 100px;
            border-radius: 12px;
        }

        .note-text {
            font-size: 12px;
            color: #6c757d;
            margin-top: 10px;
        }

        .main-content {
            height: fit-content;
        }

        .btn-ocean {
            background-color: #a6cbf8;
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
                Add Product
            </div>
            <form action="add-product" method="post" enctype="multipart/form-data">
                <div class="main-content container-fluid row d-flex mt-3 p-0">
                    <div class="large-container col-9">
                        <div class="general card">
                            <span class="text-large mb-2">General</span>
                            <div class="form-group">
                                <label for="productName">Product Name*</label>
                                <input type="text" class="form-control" id="productName" name="productName"
                                       placeholder="Enter product name">
                            </div>
                            <div class="form-group">
                                <label for="description">Product Description</label>
                                <textarea class="form-control" name="description" id="description"></textarea>
                            </div>
                        </div>
                        <div class="price card">
                            <span class="text-large mb-2">Pricing</span>
                            <div class="form-group">
                                <label for="purchaseCost">Product Purchase Cost*</label>
                                <input type="number" min="0" class="form-control" id="purchaseCost" name="purchaseCost"
                                       placeholder="Enter product purchase cost">
                            </div>
                            <div class="form-group">
                                <label for="price">Product Price*</label>
                                <input type="number" min="0" class="form-control" id="price" name="price"
                                       placeholder="Enter product price">
                            </div>
                        </div>
                        <div class="card stock">
                            <span class="text-large mb-2">Stock</span>
                            <label for="category">Stock</label>
                            <input type="number" min="0" class="form-control" id="stock" name="stock"
                                   placeholder="Enter product stock">
                        </div>
                    </div>
                    <div class="small-container col-3">
                        <div class="image-card card">
                            <span class="text-large mb-2">Image</span>
                            <label for="productImage" class="upload-area">
                                <img id="previewImage" src="" alt="Image Preview" style="display: none;">
                                <input type="file" id="productImage" accept="image/png, image/jpeg, image/jpg" name="image">
                            </label>
                            <p class="note-text mt-2">
                                Set the product image. Only <strong>*.png</strong>, <strong>*.jpg</strong>, and <strong>*.jpeg</strong>
                                image files are accepted.
                            </p>
                        </div>
                        <div class="card category">
                            <span class="text-large mb-2">Category</span>
                            <label for="category">Select Category*</label>
                            <select class="form-select" id="category" name="category">
                                <option value="" selected disabled>Select a category</option>
                                <c:forEach var="category" items="${categories}">
                                    <option value="${category.categoryId}">${category.categoryName}</option>
                                </c:forEach>
                            </select>
                            <div class="form-group mt-3">
                                <label for="newCategory">Add New Category If Not Existed</label>
                                <input type="text" class="form-control" id="newCategory"
                                       placeholder="Enter new category name" name="newCategory">
                            </div>
                        </div>
                    </div>
                    <div class="button-container col-12">
                        <button class="btn btn-ocean" type="submit" id="addProductBtn">Add Product</button>
                        <button class="btn btn-danger" id="cancelBtn">Cancel</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

</div>
<script>
    const toggleBtn = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('sidebar');

    toggleBtn.addEventListener('click', function () {
        sidebar.classList.toggle('sidebar-collapsed');
    });
</script>
<script src="https://cdn.ckeditor.com/ckeditor5/41.0.0/classic/ckeditor.js"></script>
<script>
    ClassicEditor
        .create(document.querySelector('#description'))
        .catch(error => {
            console.error(error);
        });
</script>
<script>
    document.getElementById('productImage').addEventListener('change', function (event) {
        const file = event.target.files[0];
        const preview = document.getElementById('previewImage');

        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                preview.src = e.target.result;
                preview.style.display = 'block';
            };
            reader.readAsDataURL(file);
        }
    });
</script>


<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
        integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
        crossorigin="anonymous"></script>
</body>
</html>
