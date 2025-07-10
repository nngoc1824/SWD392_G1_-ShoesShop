package entites;

public enum PaymentStatus {
    PAID("00"),
    Fail("01");

    private final String code;

    PaymentStatus(String code) {
        this.code = code;
    }

    public static PaymentStatus fromCode(String code) {
        for (PaymentStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }

    public String getCode() {
        return code;
    }
}