package controller;

import dao.UserDAO;
import entites.User;
import jakarta.servlet.annotation.MultipartConfig;
import utils.CloudinaryConfig;
import utils.DBContext;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import utils.GoogleUtils;

import java.io.IOException;
import java.sql.Connection;
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


                default:
                    response.sendRedirect("login.jsp");
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
            request.getSession().setAttribute("user", user);
            response.sendRedirect("dashboard.jsp");
        } else {
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
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
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        User user = User.builder()
                .userName(username)
                .password(password)
                .email(email)
                .fullName(fullName)
                .phone(phone)
                .status(1)
                .build();

        boolean isSuccess = userDAO.register(user);
        if (isSuccess) {
            response.sendRedirect("login.jsp");
        } else {
            request.setAttribute("error", "Đăng ký thất bại.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("login.jsp");
    }

    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response, UserDAO userDAO)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User sessionUser = (User) session.getAttribute("user");
        Part imagePart = request.getPart("image");
        if (sessionUser == null) {
            response.sendRedirect("login.jsp");
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

        // Cập nhật đối tượng User trong session
        sessionUser.setFullName(fullName);
        sessionUser.setEmail(email);
        sessionUser.setPhone(phone);
        sessionUser.setImage(imageUrl);

        boolean updated = userDAO.updateUser(sessionUser);

        if (updated) {
            session.setAttribute("user", sessionUser);
            request.setAttribute("success", "Cập nhật thông tin thành công.");
        } else {
            request.setAttribute("error", "Cập nhật thất bại. Vui lòng thử lại.");
        }

        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
}




