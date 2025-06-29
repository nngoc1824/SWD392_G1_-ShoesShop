package controller;

import java.util.Date;

import entites.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestParam;
import service.OrderService;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

@Controller
public class CheckoutController {
    OrderService orderService;
    private final PayOS payOS;

    public CheckoutController(PayOS payOS) {
        super();
        this.payOS = payOS;
    }

    @RequestMapping(value = "/create")
    public String Index(@RequestParam("id") Integer id, HttpServletRequest request) {
        Order order = orderService.findByOrderId(id); // Lấy từ DB
        request.setAttribute("order", order);    // Gửi sang JSP
        return "create";                         // Tên file create.jsp
    }

    @RequestMapping(value = "/success")
    public String Success() {
        return "success";
    }

    @RequestMapping(value = "/cancel")
    public String Cancel() {
        return "cancel";
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/create-payment-link",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public void checkout(@ModelAttribute Order order, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        try {
            final String baseUrl = getBaseUrl(request);
            final String returnUrl = baseUrl + "/success";
            final String cancelUrl = baseUrl + "/cancel";

            // Gen order code
            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

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

            httpServletResponse.setHeader("Location", data.getCheckoutUrl());
            httpServletResponse.setStatus(302);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        String url = scheme + "://" + serverName;
        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
            url += ":" + serverPort;
        }
        url += contextPath;
        return url;
    }
}
