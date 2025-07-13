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
<div class="container my-5">
    <div class="row">
        <!-- Left Side: Cart + Customer Info -->
        <div class="col-md-8">
            <h4 class="mb-4">Your Order</h4>
            <%
                List<CartItem> cart = (List<CartItem>) request.getAttribute("cart");
                ProductService productService = new ProductService();
                int subtotal = 0;
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
                <div>$<%= itemTotal %></div>
            </div>
            <%
                }
            } else {
            %>
            <p>No items in cart.</p>
            <%
                }
                int shippingFee = request.getAttribute("shippingFee") != null ? (Integer) request.getAttribute("shippingFee") : 0;
                int total = subtotal + shippingFee;
            %>

            <hr class="my-4">

            <h5>Customer Information</h5>
            <form action="placeOrder" method="post">
                <div class="form-group">
                    <input type="email" name="email" class="form-control" placeholder="Email" required>
                </div>

                <h5>Shipping Address</h5>
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <input type="text" name="firstName" class="form-control" placeholder="First Name" required>
                    </div>
                    <div class="form-group col-md-6">
                        <input type="text" name="lastName" class="form-control" placeholder="Last Name" required>
                    </div>
                </div>
                <div class="form-group">
                    <input type="text" name="company" class="form-control" placeholder="Company (optional)">
                </div>
                <div class="form-group">
                    <input type="text" name="address" class="form-control" placeholder="Address" required>
                </div>
                <div class="form-row">
                    <div class="form-group col-md-4">
                        <select id="province" name="province" class="form-control" required>
                            <option value="">Province</option>
                        </select>
                    </div>
                    <div class="form-group col-md-4">
                        <select id="district" name="district" class="form-control" disabled required>
                            <option value="">District</option>
                        </select>
                    </div>
                    <div class="form-group col-md-4">
                        <select id="ward" name="ward" class="form-control" disabled required>
                            <option value="">Commune</option>
                        </select>
                    </div>
                </div>

                <div class="text-right">
                    <button type="submit" class="btn btn-primary">Checkout</button>
                </div>
            </form>
        </div>

        <!-- Right Side: Summary -->
        <div class="col-md-4">
            <div class="border p-3">
                <h5>Summary (<%= (cart != null) ? cart.size() : 0 %> item)</h5>
                <hr>
                <p class="d-flex justify-content-between">
                    <span>Subtotal</span><span>$<%= subtotal %></span>
                </p>
                <p class="d-flex justify-content-between">
                    <span>Shipping</span><span><%= shippingFee > 0 ? "$" + shippingFee : "-" %></span>
                </p>
                <p class="d-flex justify-content-between">
                    <span>Estimated Delivery</span><span>3–5 days</span>
                </p>
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
                    <span>Total</span><span>$<%= total %></span>
                </p>
            </div>
        </div>
    </div>
</div>

<script>
    // Load Province
    document.addEventListener("DOMContentLoaded", function () {
        fetch("address?action=province")
            .then(res => res.json())
            .then(data => {
                const provinceSelect = document.getElementById("province");
                data.data.forEach(p => {
                    const opt = document.createElement("option");
                    opt.value = p.ProvinceID;
                    opt.textContent = p.ProvinceName;
                    provinceSelect.appendChild(opt);
                });
            });

        document.getElementById("province").addEventListener("change", function () {
            const provinceId = this.value;
            const districtSelect = document.getElementById("district");
            const wardSelect = document.getElementById("ward");

            districtSelect.innerHTML = '<option value="">District</option>';
            wardSelect.innerHTML = '<option value="">Commune</option>';
            wardSelect.disabled = true;

            if (provinceId) {
                fetch("address?action=district&provinceId=" + provinceId)
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
            } else {
                districtSelect.disabled = true;
            }
        });

        document.getElementById("district").addEventListener("change", function () {
            const districtId = this.value;
            const wardSelect = document.getElementById("ward");

            wardSelect.innerHTML = '<option value="">Commune</option>';

            if (districtId) {
                fetch("address?action=ward&districtId=" + districtId)
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
            } else {
                wardSelect.disabled = true;
            }
        });
    });
</script>
</body>
</html>
