package controller.CheckoutController;

import entites.Order;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;


@WebServlet("/create-payment-link")
public class PaymentLinkServlet extends HttpServlet {
    private PayOS payOS = new PayOS(System.getenv("PAYOS_CLIENT_ID"),System.getenv("PAYOS_API_KEY"),System.getenv("PAYOS_CHECKSUM_KEY")); // cấu hình đối tượng PayOS

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseUrl = UrlUtil.getBaseUrl(req);
            String returnUrl = baseUrl + "/success";
            String cancelUrl = baseUrl + "/cancel";

            Order order = new Order();
            order.setShipAddress(req.getParameter("shipAddress"));
            order.setTotalPrice(Double.parseDouble(req.getParameter("totalPrice")));

            long orderCode = System.currentTimeMillis() % 1_000_000;

            ItemData item = ItemData.builder()
                    .name("Đơn hàng #" + orderCode)
                    .quantity(1)
                    .price(order.getTotalPrice().intValue())
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount(order.getTotalPrice().intValue())
                    .description("Thanh toán đơn hàng: " + order.getShipAddress())
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .item(item)
                    .build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);
            resp.sendRedirect(data.getCheckoutUrl());

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500, "Có lỗi xảy ra");
        }
    }

    public class UrlUtil {
        public static String getBaseUrl(HttpServletRequest request) {
            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int port = request.getServerPort();
            String context = request.getContextPath();

            return scheme + "://" + serverName + ((port == 80 || port == 443) ? "" : ":" + port) + context;
        }
    }
}
