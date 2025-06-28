<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <style>
        .sidebar {
            transform: translateX(0);
        }
        .sidebar-collapsed {
            transform: translateX(-100%);
        }
        .sidebar {
            width: 250px;
            transition: all 0.3s ease-in-out;
            background-color: #fff;
            border-right: 1px solid #e4e6ef;
        }
        .sidebar::-webkit-scrollbar {
            display: none;              /* Chrome/Safari */
        }

        body {
            font-family: 'Segoe UI', sans-serif;
            margin: 0;
            padding: 0;
            overflow-x: hidden;
        }



        .sidebar .nav-small-cap {
            font-size: 11px;
            letter-spacing: 1px;
            text-transform: uppercase;
            color: #999;
            margin: 1rem 0 0.5rem;
        }

        .sidebar .sidebar-link {
            color: #555;
            text-decoration: none;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0.5rem 0.75rem;
            border-radius: 0.375rem;
            font-weight: 500;
            transition: all 0.2s;
        }

        .sidebar .sidebar-link:hover,
        .sidebar .sidebar-item.active > .sidebar-link {
            background: #eceeff;
            color: #5a48f2;
        }

        .sidebar .sidebar-link i {
            margin-right: 10px;
            font-size: 14px;
        }

        .sidebar .collapse .sidebar-link {
            padding-left: 2.5rem;
        }

        .sidebar ul {
            padding-left: 0;
        }

        .sidebar ul li {
            list-style: none;
        }

        .sidebar .menu-icon {
            margin-right: 0.5rem;
        }

    </style>
</head>
<body>

<div class="sidebar"  id="sidebar">
    <div class="text-center mb-3">
        <h4 class="text-primary fw-bold">OSS</h4>
    </div>

    <ul class="list-unstyled" id="sidebarnav">
        <li class="nav-small-cap">Product</li>

        <li class="sidebar-item">
            <a class="sidebar-link" data-bs-toggle="collapse" data-bs-target="#ecommerceMenu"
               role="button" aria-expanded="false" aria-controls="ecommerceMenu">
                <span><i class="fa-solid fa-basket-shopping menu-icon"></i> Products</span>
                <i class="fa fa-chevron-down small"></i>
            </a>
            <div class="collapse" id="ecommerceMenu">
                <ul class="list-unstyled ps-2">
                    <li><a class="sidebar-link text-primary" href="product">Product List</a></li>
                    <li><a class="sidebar-link" href="add-product">Add Product</a></li>
                </ul>
            </div>
        </li>
    </ul>
</div>



</body>
</html>
