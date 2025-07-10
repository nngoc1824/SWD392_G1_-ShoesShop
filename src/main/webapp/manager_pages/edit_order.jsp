<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Chỉnh sửa đơn hàng</title>
    <!-- Common CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/header.css">
    <!-- Page CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/manager_pages/edit_order.css">
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
                    <h4 class="mb-0">Chỉnh sửa đơn hàng</h4>
                </div>
            </div>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${fn:escapeXml(error)}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success">${fn:escapeXml(success)}</div>
            </c:if>
            <form method="post" action="${pageContext.request.contextPath}/manager/edit-order">
                <input type="hidden" name="orderId" value="${order.orderId}"/>
                <div class="mb-3">
                    <label for="orderCode" class="form-label">Order Code</label>
                    <input type="text" class="form-control" id="orderCode" name="orderCode" value="${order.orderCode}" required/>
                </div>
                <div class="mb-3">
                    <label for="totalPrice" class="form-label">Tổng giá</label>
                    <input type="number" step="0.01" class="form-control" id="totalPrice" name="totalPrice" value="${order.totalPrice}" required/>
                </div>
                <div class="form-check mb-3">
                    <input type="checkbox" class="form-check-input" id="isDelivered" name="isDelivered" ${order.isDelivered ? 'checked' : ''}/>
                    <label class="form-check-label" for="isDelivered">Đã giao</label>
                </div>
                <div class="mb-3">
                    <label for="shipAddress" class="form-label">Địa chỉ giao</label>
                    <textarea class="form-control" id="shipAddress" name="shipAddress" rows="2" required>${order.shipAddress}</textarea>
                </div>
                <div class="mb-3">
                    <label for="paymentStatus" class="form-label">Trạng thái thanh toán</label>
                    <select class="form-select" id="paymentStatus" name="paymentStatus">
                        <c:forEach var="status" items="${paymentStatuses}">
                            <option value="${status}" ${status == order.paymentStatus ? 'selected' : ''}>${status}</option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="btn btn-success">Lưu thay đổi</button>
                <a href="${pageContext.request.contextPath}/manager/orders" class="btn btn-secondary ms-2">Hủy</a>
            </form>
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