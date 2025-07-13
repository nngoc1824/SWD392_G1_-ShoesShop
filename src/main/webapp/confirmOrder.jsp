<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Confirm Order - Footwear Shop</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:300,400,500,600,700" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Rokkitt:100,300,400,700" rel="stylesheet">
    <link rel="stylesheet" href="css/animate.css">
    <link rel="stylesheet" href="css/icomoon.css">
    <link rel="stylesheet" href="css/ionicons.min.css">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/magnific-popup.css">
    <link rel="stylesheet" href="css/flexslider.css">
    <link rel="stylesheet" href="css/owl.carousel.min.css">
    <link rel="stylesheet" href="css/owl.theme.default.min.css">
    <link rel="stylesheet" href="css/bootstrap-datepicker.css">
    <link rel="stylesheet" href="fonts/flaticon/font/flaticon.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class=""></div>
<div id="page">

    <div class="breadcrumbs">
        <div class="container">
            <div class="row">
                <div class="col">
                    <p class="bread"><span><a href="home.jsp">Home</a></span> / <span>Confirm Order</span></p>
                </div>
            </div>
        </div>
    </div>

    <div class="colorlib-product">
        <div class="container">
            <div class="row row-pb-lg">
                <div class="col-sm-10 offset-md-1">
                    <div class="process-wrap">
                        <div class="process text-center active">
                            <p><span>01</span></p>
                            <h3>Shopping Cart</h3>
                        </div>
                        <div class="process text-center active">
                            <p><span>02</span></p>
                            <h3>Checkout</h3>
                        </div>
                        <div class="process text-center active">
                            <p><span>03</span></p>
                            <h3>Confirm Order</h3>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <!-- Billing Info -->
                <div class="col-lg-8">
                    <form method="post" action="placeOrder" class="colorlib-form">
                        <h2>Billing Details</h2>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="fname">First Name</label>
                                    <input type="text" id="fname" name="firstName" class="form-control" placeholder="Your first name">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="lname">Last Name</label>
                                    <input type="text" id="lname" name="lastName" class="form-control" placeholder="Your last name">
                                </div>
                            </div>

                            <div class="col-md-12">
                                <div class="form-group">
                                    <label for="email">Email</label>
                                    <input type="email" id="email" name="email" class="form-control" placeholder="example@email.com">
                                </div>
                            </div>

                            <div class="col-md-12">
                                <div class="form-group">
                                    <label for="phone">Phone Number</label>
                                    <input type="text" id="phone" name="phone" class="form-control" placeholder="Your phone number">
                                </div>
                            </div>

                            <div class="col-md-12">
                                <div class="form-group">
                                    <label for="address">Address</label>
                                    <input type="text" id="address" name="address" class="form-control" placeholder="Street name, house number...">
                                </div>
                            </div>

                            <!-- Province -->
                            <div class="col-md-12">
                                <div class="form-group">
                                    <label for="province">Province/City (Tỉnh/Thành phố)</label>
                                    <select id="province" name="province" class="form-control" required>
                                        <option value="">-- Select Province --</option>
                                    </select>
                                </div>
                            </div>

                            <!-- District -->
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="district">District (Quận/Huyện)</label>
                                    <select id="district" name="district" class="form-control" disabled required>
                                        <option value="">-- Select District --</option>
                                    </select>
                                </div>
                            </div>

                            <!-- Ward -->
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="ward">Ward/Commune (Xã/Phường)</label>
                                    <select id="ward" name="ward" class="form-control" disabled required>
                                        <option value="">-- Select Ward --</option>
                                    </select>
                                </div>
                            </div>

                        </div>
                    </form>
                </div>

                <!-- Order Summary -->
                <div class="col-lg-4">
                    <div class="cart-detail">
                        <h2>Order Summary</h2>
                        <ul>
                            <li><span>Subtotal</span> <span>$100.00</span></li>
                            <li><span>Shipping</span> <span>$0.00</span></li>
                            <li><span>Order Total</span> <span>$100.00</span></li>
                        </ul>
                    </div>
                    <div class="text-center mt-3">
                        <form action="placeOrder" method="post">
                            <button type="submit" class="btn btn-primary">Place Order</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<div class="gototop js-top">
    <a href="#" class="js-gotop"><i class="ion-ios-arrow-up"></i></a>
</div>

<!-- Scripts -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<script>
     document.addEventListener("DOMContentLoaded", function () {
        // Load provinces
        fetch("getProvinces")
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

        // On province change → load districts
         // Khi chọn Tỉnh → gọi getDistricts
         document.getElementById("province").addEventListener("change", function () {
             const provinceId = this.value;
             const districtSelect = document.getElementById("district");
             const wardSelect = document.getElementById("ward");

             districtSelect.innerHTML = '<option value="">-- Chọn Quận/Huyện --</option>';
             wardSelect.innerHTML = '<option value="">-- Chọn Phường/Xã --</option>';
             wardSelect.disabled = true;

             if (provinceId) {
                 fetch("getDistricts?provinceId=" + provinceId)
                     .then(async res => {
                         if (!res.ok) {
                             const errText = await res.text();
                             throw new Error("Lỗi server khi tải quận/huyện: " + errText);
                         }
                         return res.json();
                     })
                     .then(data => {
                         data.data.forEach(d => {
                             const opt = document.createElement("option");
                             opt.value = d.DistrictID;
                             opt.textContent = d.DistrictName;
                             districtSelect.appendChild(opt);
                         });
                         districtSelect.disabled = false;
                     })
                     .catch(err => {
                         console.error(err);
                         alert("Không thể tải danh sách Quận/Huyện. Vui lòng thử lại.");
                     });
             } else {
                 districtSelect.disabled = true;
             }
         });

// Khi chọn Quận → gọi getWards
         document.getElementById("district").addEventListener("change", function () {
             const districtId = this.value;
             const wardSelect = document.getElementById("ward");

             wardSelect.innerHTML = '<option value="">-- Chọn Phường/Xã --</option>';

             if (districtId) {
                 fetch("getWards?districtId=" + districtId)
                     .then(async res => {
                         if (!res.ok) {
                             const errText = await res.text();
                             throw new Error("Lỗi server khi tải phường/xã: " + errText);
                         }
                         return res.json();
                     })
                     .then(data => {
                         data.data.forEach(w => {
                             const opt = document.createElement("option");
                             opt.value = w.WardCode;
                             opt.textContent = w.WardName;
                             wardSelect.appendChild(opt);
                         });
                         wardSelect.disabled = false;
                     })
                     .catch(err => {
                         console.error(err);
                         alert("Không thể tải danh sách Phường/Xã. Vui lòng thử lại.");
                     });
             } else {
                 wardSelect.disabled = true;
             }
         });

     });
</script>

</body>
</html>
