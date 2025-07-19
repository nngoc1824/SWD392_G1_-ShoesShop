<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
          integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
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
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-triangle me-2"></i>${fn:escapeXml(error)}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>${fn:escapeXml(success)}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <!-- Order Information Section -->
            <div class="card mb-4">
                <div class="card-header">
                    <h5 class="mb-0"><i class="fas fa-info-circle me-2"></i>Thông tin đơn hàng</h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label class="form-label">Order Code</label>
                                <input type="text" class="form-control" value="${not empty order ? order.orderCode : 'N/A'}" readonly/>
                                <div class="form-text">Mã đơn hàng không thể thay đổi</div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Ngày đặt hàng</label>
                                <input type="text" class="form-control" value="<c:choose><c:when test='${not empty order and not empty order.orderDate}'><fmt:formatDate value='${order.orderDate}' pattern='dd/MM/yyyy HH:mm:ss'/></c:when><c:otherwise>N/A</c:otherwise></c:choose>" readonly/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label class="form-label">Số điện thoại</label>
                                <input type="text" class="form-control" value="${not empty order ? order.phone : 'N/A'}" readonly/>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">User ID</label>
                                <input type="text" class="form-control" value="${not empty order ? order.userId : 'N/A'}" readonly/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Editable Order Details Form -->
            <form method="post" action="${pageContext.request.contextPath}/edit-order" id="editOrderForm">
                <input type="hidden" name="orderId" value="${not empty order ? order.orderId : ''}"/>

                <!-- Payment Information Section -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="fas fa-credit-card me-2"></i>Thông tin thanh toán</h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="totalPrice" class="form-label">Tổng giá <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <input type="number" step="0.01" class="form-control" id="totalPrice" name="totalPrice"
                                               value="${not empty order ? order.totalPrice : 0}" required min="0"/>
                                        <span class="input-group-text">₫</span>
                                    </div>
                                    <div class="invalid-feedback">
                                        Vui lòng nhập tổng giá hợp lệ (lớn hơn 0)
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="paymentStatus" class="form-label">Trạng thái thanh toán <span class="text-danger">*</span></label>
                                    <select class="form-select" id="paymentStatus" name="paymentStatus" required>
                                        <option value="">-- Chọn trạng thái --</option>
                                        <c:forEach var="status" items="${paymentStatuses}">
                                            <option value="${status}" ${not empty order and status == order.paymentStatus ? 'selected' : ''}>${status}</option>
                                        </c:forEach>
                                    </select>
                                    <div class="invalid-feedback">
                                        Vui lòng chọn trạng thái thanh toán
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Delivery Information Section -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="fas fa-shipping-fast me-2"></i>Thông tin giao hàng</h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label for="shipAddress" class="form-label">Địa chỉ giao hàng <span class="text-danger">*</span></label>
                            <textarea class="form-control" id="shipAddress" name="shipAddress" rows="3"
                                      required placeholder="Nhập địa chỉ giao hàng đầy đủ">${not empty order ? order.shipAddress : ''}</textarea>
                            <div class="invalid-feedback">
                                Vui lòng nhập địa chỉ giao hàng
                            </div>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" id="delivered" name="delivered"
                                   ${not empty order and order.delivered ? 'checked' : ''}/>
                            <label class="form-check-label" for="delivered">
                                <i class="fas fa-check-circle me-1"></i>Đã giao hàng
                            </label>
                        </div>
                    </div>
                </div>

                <!-- Action Buttons -->
                <div class="d-flex justify-content-between">
                    <a href="${pageContext.request.contextPath}/orders" class="btn btn-secondary">
                        <i class="fas fa-arrow-left me-2"></i>Quay lại danh sách
                    </a>
                    <button type="submit" class="btn btn-success" id="submitBtn">
                        <i class="fas fa-save me-2"></i>Lưu thay đổi
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
        integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
        crossorigin="anonymous"></script>

<!-- Form Validation and UX Enhancement Script -->
<script>
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('editOrderForm');
    const submitBtn = document.getElementById('submitBtn');
    const totalPriceInput = document.getElementById('totalPrice');
    const shipAddressInput = document.getElementById('shipAddress');
    const paymentStatusSelect = document.getElementById('paymentStatus');

    // Form validation
    function validateForm() {
        let isValid = true;

        // Reset previous validation states
        form.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));

        // Validate total price
        const totalPrice = parseFloat(totalPriceInput.value);
        if (!totalPrice || totalPrice <= 0) {
            totalPriceInput.classList.add('is-invalid');
            isValid = false;
        }

        // Validate ship address
        if (!shipAddressInput.value.trim()) {
            shipAddressInput.classList.add('is-invalid');
            isValid = false;
        }

        // Validate payment status
        if (!paymentStatusSelect.value) {
            paymentStatusSelect.classList.add('is-invalid');
            isValid = false;
        }

        return isValid;
    }

    // Real-time validation
    totalPriceInput.addEventListener('input', function() {
        const value = parseFloat(this.value);
        if (value && value > 0) {
            this.classList.remove('is-invalid');
            this.classList.add('is-valid');
        } else {
            this.classList.remove('is-valid');
        }
    });

    shipAddressInput.addEventListener('input', function() {
        if (this.value.trim()) {
            this.classList.remove('is-invalid');
            this.classList.add('is-valid');
        } else {
            this.classList.remove('is-valid');
        }
    });

    paymentStatusSelect.addEventListener('change', function() {
        if (this.value) {
            this.classList.remove('is-invalid');
            this.classList.add('is-valid');
        } else {
            this.classList.remove('is-valid');
        }
    });

    // Form submission with confirmation
    form.addEventListener('submit', function(e) {
        e.preventDefault();

        if (!validateForm()) {
            // Scroll to first invalid field
            const firstInvalid = form.querySelector('.is-invalid');
            if (firstInvalid) {
                firstInvalid.scrollIntoView({ behavior: 'smooth', block: 'center' });
                firstInvalid.focus();
            }
            return;
        }

        // Show confirmation dialog
        if (confirm('Bạn có chắc chắn muốn cập nhật đơn hàng này không?')) {
            // Show loading state
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Đang lưu...';
            submitBtn.disabled = true;

            // Submit form
            this.submit();
        }
    });

    // Auto-dismiss alerts after 5 seconds
    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert-dismissible');
        alerts.forEach(alert => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);
});
</script>
</body>
</html>