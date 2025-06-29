package utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailValidate {

    private static final String USERNAME = "tohoanghiep240503@gmail.com";
    private static final String PASSWORD = "eqoeqavflpbnigri"; // App Password (NOT real password)

    public static void send(String toEmail, String subject, String code) {
        // Cấu hình thuộc tính SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");                    // bật xác thực
        props.put("mail.smtp.starttls.enable", "true");         // bật TLS
        props.put("mail.smtp.host", "smtp.gmail.com");          // máy chủ SMTP của Gmail
        props.put("mail.smtp.port", "587");                     // cổng TLS

        // Tạo phiên gửi email
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            // Tạo đối tượng email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME, "Hệ thống xác minh", "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setReplyTo(InternetAddress.parse(USERNAME));
            message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));

            // Thêm header hỗ trợ hủy đăng ký (tùy chọn)
            String unsubscribeLink = "https://yourdomain.com/unsubscribe?email=" + toEmail;
            message.setHeader("List-Unsubscribe", "<" + unsubscribeLink + ">");

            // Nội dung HTML
            String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px;'>"
                    + "<h2 style='color: #26c680;'>Xác minh đăng ký</h2>"
                    + "<p>Chào bạn,</p>"
                    + "<p>Đây là mã xác minh cho tài khoản của bạn:</p>"
                    + "<h1 style='color: #007bff;'>" + code + "</h1>"
                    + "<p>Nếu bạn không yêu cầu đăng ký, vui lòng bỏ qua email này.</p>"
                    + "<hr>"
                    + "<p style='font-size: 0.85em; color: #999;'>"
                    + "Email được gửi tự động từ hệ thống xác minh của chúng tôi. "
                    + "Nếu có câu hỏi, vui lòng liên hệ: "
                    + "<a href='mailto:support@example.com'>support@example.com</a>."
                    + "</p>"
                    + "</div>";

            message.setContent(htmlContent, "text/html; charset=UTF-8");

            // Gửi email
            Transport.send(message);
            System.out.println(">>> Đã gửi email xác minh tới: " + toEmail);
        } catch (Exception e) {
            System.err.println("Lỗi gửi email:");
            e.printStackTrace();
        }
    }
}
