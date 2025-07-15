package filter;

import entites.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(
        urlPatterns = {"/*"}, // Filter applies to all URLs under /*
        filterName = "ManagerFilter"
)
public class ServletFilter extends HttpFilter {
    private static final Logger log = LoggerFactory.getLogger(ManagerFilter.class);

    @Override
    public void init() {
        // This method can be used to initialize resources or configurations for the filter
        System.out.println("ManagerFilter initialized");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        // This method is called for each request that matches the filter's URL patterns
        log.info("ManagerFilter processing request: " + req.getRequestURI());
        if (req.getSession().getAttribute("user") == null) {
            // If the user is not logged in, redirect to the login page
            log.warn("Unauthorized access attempt to: " + req.getRequestURI());
            res.sendRedirect(req.getContextPath() + "/user?action=login");
            return;
        }
        // If the user is logged in, continue with the filter chain
        // You can add additional logic here if needed, such as checking user roles or permissions
        else {
            log.info("User is authorized, proceeding with request: " + req.getRequestURI());
            User user = (User) req.getSession().getAttribute("user");
            if (user.getRoles().contains("Manager")) {
                log.info("User has manager role, proceeding with request: " + req.getRequestURI());
            } else {
                log.warn("User does not have manager role, redirecting to home page: " + req.getRequestURI());
                res.sendRedirect(req.getContextPath() + "/home");
                return;
            }
        }

        super.doFilter(req, res, chain);
    }

    @Override
    public void destroy() {
        // This method can be used to clean up resources when the filter is destroyed
        System.out.println("ManagerFilter destroyed");
    }
}
