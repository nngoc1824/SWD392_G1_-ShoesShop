package utils;

import vn.payos.PayOS;

public class PayOSInitializer {
    private static final PayOS INSTANCE = new PayOS(
            System.getenv("PAYOS_CLIENT_ID"),
            System.getenv("PAYOS_API_KEY"),
            System.getenv("PAYOS_CHECKSUM_KEY")
    );

    public static PayOS getInstance() {
        return INSTANCE;
    }
}

