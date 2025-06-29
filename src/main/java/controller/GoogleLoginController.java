package controller;

import dao.UserDAO;
import entites.User;
import jakarta.servlet.http.HttpSession;
import utils.DBContext;
import utils.GoogleUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/login-google")
public class GoogleLoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String code = req.getParameter("code");
        System.out.println("code: " + code);
        if (code == null || code.isEmpty()) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try (Connection conn = new DBContext().getConnection()) {
            User user = GoogleUtils.handleGoogleLogin(code);
            if (user == null) {
                resp.sendRedirect("login.jsp");
                return;
            }

            UserDAO userDAO = new UserDAO(conn);

            // Tìm user theo googleId hoặc email
            User existingUser = userDAO.getUserByEmail(user.getEmail());
            if (existingUser == null) {
                // Nếu chưa có thì thêm user mới
                userDAO.insertGoogleUser(user);
                existingUser = userDAO.getUserByEmail(user.getEmail());
            }

            // Đặt user vào session
            HttpSession session = req.getSession();
            session.setAttribute("user", existingUser);

            resp.sendRedirect("dashboard.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Google login failed.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
