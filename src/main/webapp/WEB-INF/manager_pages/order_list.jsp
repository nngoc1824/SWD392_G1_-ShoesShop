<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Order List</title>
    <!-- Common CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/header.css">
    <!-- Page CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/manager_pages/order_list.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/manager_pages/table_order.css">
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
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
        <div class="main-content p-4">
            <div class="card mb-4">
                <div class="card-body">
                    <h4 class="mb-0">Danh sách đơn hàng</h4>
                </div>
            </div>
            <form class="row g-2 mb-3" method="get" action="${pageContext.request.contextPath}/manager/orders">
                <div class="col-auto">
                    <input type="text" class="form-control" name="search" placeholder="Order Code hoặc Địa chỉ"
                           value="${search}">
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                </div>
            </form>
            <div class="table-responsive">
                <table class="table table-bordered table-hover align-middle">
                    <thead class="table-light">
                    <tr>
                        <th>#</th>
                        <th>Order ID</th>
                        <th>Order Code</th>
                        <th>Ngày đặt</th>
                        <th>Tổng giá</th>
                        <th>Đã giao?</th>
                        <th>Địa chỉ giao</th>
                        <th>Trạng thái thanh toán</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="order" items="${orders}" varStatus="status">
                        <tr>
                            <td>${status.index + 1}</td>
                            <td>${not empty order ? order.orderId : 'N/A'}</td>
                            <td>${not empty order ? order.orderCode : 'N/A'}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty order and not empty order.orderDate}">
                                        <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                                    </c:when>
                                    <c:otherwise>N/A</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty order}">
                                        <fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true"/>₫
                                    </c:when>
                                    <c:otherwise>0₫</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty order and order.delivered}">
                                        <span class="badge bg-success">Đã giao</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-warning text-dark">Chưa giao</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${not empty order ? order.shipAddress : 'N/A'}</td>
                            <td>${not empty order ? order.paymentStatus : 'N/A'}</td>
                            <td>
                                <c:if test="${not empty order}">
                                    <a href="${pageContext.request.contextPath}/manager/edit-order?orderId=${order.orderId}" class="btn btn-sm btn-info">Sửa</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center">
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link" href="?page=${currentPage - 1}&search=${fn:escapeXml(search)}">Previous</a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link" href="?page=${i}&search=${fn:escapeXml(search)}">${i}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="?page=${currentPage + 1}&search=${fn:escapeXml(search)}">Next</a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</div>
<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
        integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
        crossorigin="anonymous"></script>
</body>
</html>