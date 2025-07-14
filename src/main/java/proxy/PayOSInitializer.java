package proxy;

import vn.payos.PayOS;
import java.io.InputStream;
import java.util.Properties;

public class PayOSInitializer {
    private static final PayOS INSTANCE;

    static {
        try {
            Properties props = new Properties();
            InputStream in = PayOSInitializer.class.getClassLoader()
                    .getResourceAsStream("config.properties");
            props.load(in);

            String clientId = props.getProperty("payos.clientId");
            String apiKey = props.getProperty("payos.apiKey");
            String checksumKey = props.getProperty("payos.checksumKey");

            if (clientId == null || apiKey == null || checksumKey == null) {
                throw new RuntimeException("🚫 Thiếu thông tin PayOS trong config.properties");
            }

            INSTANCE = new PayOS(clientId, apiKey, checksumKey);

        } catch (Exception e) {
            throw new RuntimeException("🚫 Lỗi load config PayOS: " + e.getMessage());
        }
    }

    public static PayOS getInstance() {
        return INSTANCE;
    }
}
