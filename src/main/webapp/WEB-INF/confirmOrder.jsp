<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="entites.CartItem" %>
<%@ page import="entites.Product" %>
<%@ page import="service.ProductService" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Confirm Order</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<%@ include file="header.jsp" %>
<%
    String addr = (String) session.getAttribute("ghn_address");
    String selectedProvince = (String) session.getAttribute("ghn_province");
    String selectedDistrict = (String) session.getAttribute("ghn_district");
    String selectedWard = (String) session.getAttribute("ghn_ward");
%>
<div class="container my-5">
    <div class="row">
        <!-- Left Side: Cart + Customer Info -->
        <div class="col-md-8">
            <h4 class="mb-4">Your Order</h4>
            <%
                List<CartItem> cart = (List<CartItem>) request.getAttribute("cart");
                ProductService productService = new ProductService();
                double subtotal = 0;
                if (cart != null && !cart.isEmpty()) {
                    for (CartItem item : cart) {
                        int quantity = item.getQuantity();
                        Product p = productService.getProductById(item.getProductId());
                        double price = p.getPrice();
                        double itemTotal = price * quantity;
                        subtotal += itemTotal;
            %>
            <div class="d-flex justify-content-between border-bottom py-2">
                <div>
                    <img src="<%= p.getImage() %>" alt="Product Image" style="width: 80px; height: 80px; object-fit: cover;">
                    <strong><%= p.getProductName() %></strong><br>
                </div>
                <div> x<%= quantity %></div>
                <div> <%= String.format("%,d", (long) itemTotal) %>₫ </div>
            </div>
            <%  }
            } else { %>
            <p>No items in cart.</p>
            <% }
                int shippingFee = request.getAttribute("shippingFee") != null ? (Integer) request.getAttribute("shippingFee") : 0;
                double total = subtotal + shippingFee;
            %>

            <hr class="my-4">

            <h5>Shipping Customer Information</h5>
            <form action="${pageContext.request.contextPath}/checkout/create" method="post">
                <div class="form-group">
                    <input type="email" name="email" class="form-control" value="<%= (user != null) ? user.getEmail() : "" %>" placeholder="Email" required>
                </div>


                <div class="form-group">
                    <input type="text" name="name" value="<%= (user != null) ? user.getFullName() : "" %>" class="form-control" placeholder="Name" required>
                </div>


                <div class="form-group">
                    <input type="tel" name="phone" value="<%= (user != null) ? user.getPhone() : "" %>" class="form-control" placeholder="Phone" required>
                </div>
                <div class="form-group">
                <input type="text" name="address"
                       value="<%= addr != null ? addr : "" %>"
                       class="form-control" placeholder="Address" required>
                </div>

                <div class="form-row">
                    <div class="form-group col-md-4">
                        <select id="province" name="province" class="form-control" required>
                            <option value="">Province</option>
                        </select>
                    </div>
                    <div class="form-group col-md-4">
                        <select id="district" name="district" class="form-control" required disabled>
                            <option value="">District</option>
                        </select>
                    </div>
                    <div class="form-group col-md-4">
                        <select id="ward" name="ward" class="form-control" required disabled>
                            <option value="">Commune</option>
                        </select>
                    </div>
                </div>

                <!-- Hidden shipping fee + total -->
                <input type="hidden" id="shippingFeeInput" name="shippingFee" value="<%= shippingFee %>">
                <input type="hidden" id="totalFeeInput" name="total" value=" <%= String.format("%,d", (long) total) %>₫">

                <div class="text-right">
                    <button type="submit" class="btn btn-primary">Checkout</button>
                </div>
            </form>
        </div>

        <!-- Right Side: Summary -->
        <div class="col-md-4">
            <div class="border p-3">
                <h5>Summary (<%= (cart != null) ? cart.size() : 0 %> items)</h5>
                <hr>
                <p class="d-flex justify-content-between">
                    <span>Subtotal</span><span><%= String.format("%,d", (long) subtotal) %>₫</span>
                </p>
                <p class="d-flex justify-content-between">
                    <span>Shipping</span>
                    <span id="shipping-fee">
                         <%= shippingFee > 0 ? String.format("%,d₫", (long) shippingFee) : "0₫" %>
                    </span>

                </p>
                <hr>
                <p class="d-flex justify-content-between font-weight-bold">
                    <span>Total</span><span id="total-fee" data-subtotal="<%= String.format("%,d", (long) total) %>₫</span>
                </p>
                <p class="d-flex justify-content-between">
                    <span>Estimated Delivery</span><span>3–5 days</span>
                </p>
                <hr>
                <div class="form-group">
                    <label>Gift card or discount code</label>
                    <div class="input-group">
                        <input type="text" class="form-control">
                        <div class="input-group-append">
                            <button class="btn btn-outline-secondary">Apply</button>
                        </div>
                    </div>
                </div>
                <hr>
                <p class="d-flex justify-content-between font-weight-bold">
                    <span>Total</span><span><%= String.format("%,d", (long) total) %>₫ %></span>
                </p>
            </div>
        </div>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const provinceSelect = document.getElementById("province");
        const districtSelect = document.getElementById("district");
        const wardSelect = document.getElementById("ward");

        const shippingFeeInput = document.getElementById("shippingFeeInput");
        const totalFeeInput = document.getElementById("totalFeeInput");
        const shippingFeeSpan = document.getElementById("shipping-fee");
        const totalFeeSpan = document.getElementById("total-fee");

        // Load Provinces
        fetch("confirmOrder?action=province")
            .then(res => res.json())
            .then(data => {
                data.data.forEach(p => {
                    const opt = document.createElement("option");
                    opt.value = p.ProvinceID;
                    opt.textContent = p.ProvinceName;
                    provinceSelect.appendChild(opt);
                });
            });

        // Province change => load Districts
        provinceSelect.addEventListener("change", function () {
            const provinceId = this.value;

            districtSelect.innerHTML = '<option value="">District</option>';
            wardSelect.innerHTML = '<option value="">Commune</option>';
            districtSelect.disabled = true;
            wardSelect.disabled = true;

            if (provinceId) {
                fetch("confirmOrder?action=district&provinceId=" + provinceId)
                    .then(res => res.json())
                    .then(data => {
                        data.data.forEach(d => {
                            const opt = document.createElement("option");
                            opt.value = d.DistrictID;
                            opt.textContent = d.DistrictName;
                            districtSelect.appendChild(opt);
                        });
                        districtSelect.disabled = false;
                    });
            }
        });

        // District change => load Wards
        districtSelect.addEventListener("change", function () {
            const districtId = this.value;

            wardSelect.innerHTML = '<option value="">Commune</option>';
            wardSelect.disabled = true;

            if (districtId) {
                fetch("confirmOrder?action=ward&districtId=" + districtId)
                    .then(res => res.json())
                    .then(data => {
                        data.data.forEach(w => {
                            const opt = document.createElement("option");
                            opt.value = w.WardCode;
                            opt.textContent = w.WardName;
                            wardSelect.appendChild(opt);
                        });
                        wardSelect.disabled = false;
                    });
            }
        });

        // Ward change => calculate shipping fee
        wardSelect.addEventListener("change", function () {
            const districtIdChecked = districtSelect.value;
            const wardCodeChecked = this.value;
            console.log("Selected District ID:", districtIdChecked);
            console.log("Selected Ward Code:", wardCodeChecked);
            if (districtIdChecked && wardCodeChecked) {
                fetch(`confirmOrder?action=shippingFee&districtId=\${districtIdChecked}&wardCode=\${wardCodeChecked}`)
                    .then(res => res.json())
                    .then(data => {
                        const shippingFee = data.data.fee; // Giá trị trả về từ API
                        console.log("Shipping fee:", shippingFee);
                        const subtotal = parseFloat(totalFeeSpan.dataset.subtotal);
                        const total = subtotal + shippingFee;

                        // Cập nhật UI
                        shippingFeeSpan.textContent = `\${shippingFee}`;
                        totalFeeSpan.textContent = `\${total.toFixed(2)}`;

                        // Cập nhật input ẩn
                        shippingFeeInput.value = shippingFee;
                        totalFeeInput.value = total.toFixed(2);
                    })
                    .catch(err => console.error("Shipping fee fetch error:", err));
            }
        });
    });
</script>

</body>
</html>
