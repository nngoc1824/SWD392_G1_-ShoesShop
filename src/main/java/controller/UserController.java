package controller;

import dao.UserDAO;
import entites.User;
import jakarta.servlet.annotation.MultipartConfig;
import proxy.CloudinaryConfig;
import utils.DBContext;
import utils.EmailValidate;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@MultipartConfig
@WebServlet(name = "UserController", urlPatterns = {"/user"})
public class UserController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        Connection conn = null;

        try {
            conn = new DBContext().getConnection();
            UserDAO userDAO = new UserDAO(conn);

            switch (action) {
                case "login":
                    handleLogin(request, response, userDAO);
                    break;
                case "register":
                    handleRegister(request, response, userDAO);
                    break;
                case "logout":
                    handleLogout(request, response);
                    break;
                case "updateProfile":
                    handleUpdateProfile(request, response, userDAO);
                    break;
                case "viewProfile":
                    handleViewProfile(request, response, userDAO);
                    break;

                case "changePassword":
                    handleChangePassword(request, response, userDAO);
                    break;
                case "verify":
                    handleVerify(request, response);
                    break;

                default:
                    response.sendRedirect("WEB-INF/login.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi kết nối CSDL: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);

        } finally {
            try {
                if (conn != null && !conn.isClosed()) conn.close();
            } catch (Exception ignored) {
            }
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response, UserDAO userDAO)
            throws ServletException, IOException {

        String username = request.getParameter("userName");
        String password = request.getParameter("password");

        User user = userDAO.login(username, password);
        if (user != null) {
            HttpSession session = request.getSession();
            List<String> roles = userDAO.getRole(user.getUserId());
            user.setRoles(roles);
            session.setAttribute("user", user);

            if (roles.contains("Manager")) {
                response.sendRedirect("product"); // Nếu là manager thì chuyển đến product
            } else {
                response.sendRedirect("home"); // Người dùng bình thường về home
            }
        } else {
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
            request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
        }
    }


    private void handleRegister(HttpServletRequest request, HttpServletResponse response, UserDAO userDAO)
            throws ServletException, IOException {

        String username = request.getParameter("userName");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");

        if (userDAO.isUsernameTaken(username)) {
            request.setAttribute("error", "Tên đăng nhập đã được sử dụng.");
            request.getRequestDispatcher("WEB-INF/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp.");
            request.getRequestDispatcher("WEB-INF/register.jsp").forward(request, response);
            return;
        }

        User user = User.builder()
                .userName(username)
                .password(password)
                .email(email)
                .fullName(fullName)
                .phone(phone)
                .status(0) // chưa kích hoạt
                .build();

        boolean isSuccess = userDAO.register(user);
        if (isSuccess) {
            String code = String.valueOf((int) (Math.random() * 900000) + 100000);
            EmailValidate.send(email, "Mã xác minh tài khoản", code);

            HttpSession session = request.getSession();
            session.setAttribute("verificationCode", code);
            session.setAttribute("pendingEmail", email);

            response.sendRedirect("WEB-INF/verify.jsp");
        } else {
            request.setAttribute("error", "Đăng ký thất bại.");
            request.getRequestDispatcher("WEB-INF/register.jsp").forward(request, response);
        }
    }

    private void handleVerify(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String expectedCode = (String) session.getAttribute("verificationCode");
        String enteredCode = request.getParameter("code");

        if (expectedCode != null && expectedCode.equals(enteredCode)) {
            String email = (String) session.getAttribute("pendingEmail");

            try (Connection conn = new DBContext().getConnection()) {
                if (UserDAO.activateUserByEmail(email, conn)) {
                    session.removeAttribute("verificationCode");
                    session.removeAttribute("pendingEmail");
                    request.setAttribute("message", "Xác minh thành công! Bạn có thể đăng nhập.");
                    request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Không thể kích hoạt tài khoản.");
                    request.getRequestDispatcher("WEB-INF/verify.jsp").forward(request, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Đã xảy ra lỗi khi xác minh tài khoản.");
                request.getRequestDispatcher("WEB-INF/verify.jsp").forward(request, response);
            }

        } else {
            request.setAttribute("error", "Mã xác minh không đúng.");
            request.getRequestDispatcher("WEB-INF/verify.jsp").forward(request, response);
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("home");
    }

    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response, UserDAO userDAO)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("WEB-INF/login.jsp");
            return;
        }

        User sessionUser = (User) session.getAttribute("user");

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (sessionUser.getPassword() == null) {
            request.setAttribute("error", "Tài khoản không hỗ trợ đổi mật khẩu.");
            request.getRequestDispatcher("WEB-INF/change-password.jsp").forward(request, response);
            return;
        }

        if (!sessionUser.getPassword().equals(currentPassword)) {
            request.setAttribute("error", "Mật khẩu hiện tại không đúng.");
            request.getRequestDispatcher("WEB-INF/change-password.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp.");
            request.getRequestDispatcher("WEB-INF/change-password.jsp").forward(request, response);
            return;
        }

        boolean updated = userDAO.updateUserPassword(sessionUser.getUserId(), newPassword);
        if (updated) {
            sessionUser.setPassword(newPassword);
            request.setAttribute("success", "Đổi mật khẩu thành công.");
        } else {
            request.setAttribute("error", "Lỗi khi đổi mật khẩu.");
        }

        request.getRequestDispatcher("WEB-INF/change-password.jsp").forward(request, response);
    }

    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response, UserDAO userDAO)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User sessionUser = (User) session.getAttribute("user");
        Part imagePart = request.getPart("image");
        if (sessionUser == null) {
            response.sendRedirect("WEB-INF/login.jsp");
            return;
        }
        String imageUrl = "";

        if (imagePart != null && imagePart.getSize() > 0) {
            try {
                imageUrl = CloudinaryConfig.updloadImage(imagePart);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Image URL: " + imageUrl);
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String provinceId = request.getParameter("province");
        String districtId = request.getParameter("district");
        String wardCode = request.getParameter("ward");


        sessionUser.setFullName(fullName);
        sessionUser.setEmail(email);
        sessionUser.setPhone(phone);
        sessionUser.setImage(imageUrl);
        session.setAttribute("ghn_address", address);
        session.setAttribute("ghn_province", provinceId);
        session.setAttribute("ghn_district", districtId);
        session.setAttribute("ghn_ward", wardCode);




        boolean updated = userDAO.updateUser(sessionUser);

        if (updated) {
            session.setAttribute("user", sessionUser);
            request.setAttribute("success", "Cập nhật thông tin thành công.");
        } else {
            request.setAttribute("error", "Cập nhật thất bại. Vui lòng thử lại.");
        }

        request.getRequestDispatcher("WEB-INF/profile.jsp").forward(request, response);
    }
    private void handleViewProfile(HttpServletRequest request, HttpServletResponse response, UserDAO userDAO)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("WEB-INF/login.jsp");  // Hoặc forward nếu cần
            return;
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("WEB-INF/view-profile.jsp").forward(request, response);
    }

}
