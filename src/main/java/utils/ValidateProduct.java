package utils;

public class ValidateProduct {
    public static int getIntegerValue(String number){
        try{
            return Integer.parseInt(number);
        }catch (NumberFormatException e){
            System.out.println("Invalid number format: " + e.getMessage());
            return -1; // Hoặc một giá trị mặc định khác để chỉ ra lỗi
        }
    }
}
