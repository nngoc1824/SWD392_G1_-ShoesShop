<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="entites.User" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect("login.jsp");
    return;
  }

  String addr = (String) session.getAttribute("ghn_address");
  String selectedProvince = (String) session.getAttribute("ghn_province");
  String selectedDistrict = (String) session.getAttribute("ghn_district");
  String selectedWard = (String) session.getAttribute("ghn_ward");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Cập nhật hồ sơ</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background: linear-gradient(to right, #e0eafc, #cfdef3);
      padding: 30px;
    }

    .container {
      max-width: 600px;
      background: white;
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 5px 20px rgba(0,0,0,0.1);
    }

    h2 {
      text-align: center;
      margin-bottom: 30px;
    }

    label {
      font-weight: bold;
    }

    .form-group input,
    .form-group select {
      border-radius: 5px;
    }

    .btn-primary {
      width: 100%;
      margin-top: 20px;
    }

    .error, .success {
      margin-top: 15px;
      padding: 10px;
      text-align: center;
      border-radius: 5px;
      font-size: 14px;
    }

    .error {
      background-color: #ffe6e6;
      color: #cc0000;
    }

    .success {
      background-color: #e6ffee;
      color: #008000;
    }
  </style>
</head>
<body>

<div class="container">
  <h2>Cập nhật hồ sơ</h2>
  <form action="user" method="post" enctype="multipart/form-data">
    <input type="hidden" name="action" value="updateProfile" />

    <div class="form-group">
      <label for="fullName">Họ và tên</label>
      <input type="text" class="form-control" name="fullName" id="fullName"
             value="<%= user.getFullName() %>" required />
    </div>

    <div class="form-group">
      <label for="email">Email</label>
      <input type="email" class="form-control" name="email" id="email"
             value="<%= user.getEmail() %>" required />
    </div>

    <div class="form-group">
      <label for="phone">Số điện thoại</label>
      <input type="text" class="form-control" name="phone" id="phone"
             value="<%= user.getPhone() %>" required />
    </div>

    <div class="form-group">
      <label for="image">Ảnh đại diện</label>
      <input type="file" class="form-control" accept="image/*" name="image" id="image" />
    </div>

    <div class="form-group">
      <label for="address">Địa chỉ cụ thể</label>
      <input type="text" class="form-control" name="address" id="address"
             value="<%= addr == null ? "" : addr %>"
             placeholder="Số nhà, tên đường..." />
    </div>

    <div class="form-group">
      <label for="province">Tỉnh/Thành phố</label>
      <select id="province" name="province" class="form-control" required>
        <option value="">-- Chọn Tỉnh/Thành phố --</option>
      </select>
    </div>

    <div class="form-group">
      <label for="district">Quận/Huyện</label>
      <select id="district" name="district" class="form-control" disabled required>
        <option value="">-- Chọn Quận/Huyện --</option>
      </select>
    </div>

    <div class="form-group">
      <label for="ward">Xã/Phường</label>
      <select id="ward" name="ward" class="form-control" disabled required>
        <option value="">-- Chọn Xã/Phường --</option>
      </select>
    </div>

    <input type="submit" class="btn btn-primary" value="Cập nhật" />
  </form>

  <% String error = (String) request.getAttribute("error");
    if (error != null) { %>
  <div class="error"><%= error %></div>
  <% } %>

  <% String success = (String) request.getAttribute("success");
    if (success != null) { %>
  <div class="success"><%= success %></div>
  <% } %>
</div>

<!-- Back -->
<div style="text-align: center; margin-top: 20px;">
  <a href="user?action=viewProfile" class="btn btn-secondary">← Về Profile</a>
</div>

<script>
  document.addEventListener("DOMContentLoaded", function () {
    const provinceSelect = document.getElementById("province");
    const districtSelect = document.getElementById("district");
    const wardSelect = document.getElementById("ward");

    const selectedProvince = "<%= selectedProvince == null ? "" : selectedProvince %>";
    const selectedDistrict = "<%= selectedDistrict == null ? "" : selectedDistrict %>";
    const selectedWard = "<%= selectedWard == null ? "" : selectedWard %>";

    // Load provinces
    fetch("address?action=province")
            .then(res => res.json())
            .then(data => {
              data.data.forEach(p => {
                const opt = document.createElement("option");
                opt.value = p.ProvinceID;
                opt.textContent = p.ProvinceName;
                if (p.ProvinceID == selectedProvince) opt.selected = true;
                provinceSelect.appendChild(opt);
              });

              if (selectedProvince) {
                provinceSelect.disabled = false;
                loadDistricts(selectedProvince);
              }
            });

    provinceSelect.addEventListener("change", function () {
      const provinceId = this.value;
      districtSelect.innerHTML = '<option value="">-- Chọn Quận/Huyện --</option>';
      wardSelect.innerHTML = '<option value="">-- Chọn Xã/Phường --</option>';
      wardSelect.disabled = true;

      if (provinceId) {
        loadDistricts(provinceId);
      } else {
        districtSelect.disabled = true;
      }
    });

    function loadDistricts(provinceId) {
      fetch("address?action=district&provinceId=" + provinceId)
              .then(res => res.json())
              .then(data => {
                data.data.forEach(d => {
                  const opt = document.createElement("option");
                  opt.value = d.DistrictID;
                  opt.textContent = d.DistrictName;
                  if (d.DistrictID == selectedDistrict) opt.selected = true;
                  districtSelect.appendChild(opt);
                });
                districtSelect.disabled = false;

                if (selectedDistrict) {
                  loadWards(selectedDistrict);
                }
              });
    }

    districtSelect.addEventListener("change", function () {
      const districtId = this.value;
      wardSelect.innerHTML = '<option value="">-- Chọn Xã/Phường --</option>';
      if (districtId) {
        loadWards(districtId);
      } else {
        wardSelect.disabled = true;
      }
    });

    function loadWards(districtId) {
      fetch("address?action=ward&districtId=" + districtId)
              .then(res => res.json())
              .then(data => {
                data.data.forEach(w => {
                  const opt = document.createElement("option");
                  opt.value = w.WardCode;
                  opt.textContent = w.WardName;
                  if (w.WardCode == selectedWard) opt.selected = true;
                  wardSelect.appendChild(opt);
                });
                wardSelect.disabled = false;
              });
    }
  });
</script>

</body>
</html>
