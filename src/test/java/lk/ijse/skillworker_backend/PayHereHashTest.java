package lk.ijse.skillworker_backend;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PayHereHashTest {

    public static void main(String[] args) {
        // Test hash generation with your actual values
        String merchantId = "1232075";
        String merchantSecret = "MzQyMTA5NTc1MzMyMTczNzMxNTgxMzMxMjUwMTAzMTA5NjI3MDQzNA==";
        String orderId = "SW-TEST-12345";
        BigDecimal amount = new BigDecimal("2499.00");
        String currency = "LKR";

        try {
            String hash = generateTestHash(merchantId, orderId, amount, currency, merchantSecret);
            System.out.println("Test Hash Generated: " + hash);
            System.out.println("Merchant ID: " + merchantId);
            System.out.println("Order ID: " + orderId);
            System.out.println("Amount: " + String.format("%.2f", amount));
            System.out.println("Currency: " + currency);
        } catch (Exception e) {
            System.err.println("Error generating hash: " + e.getMessage());
        }
    }

    private static String generateTestHash(String merchantId, String orderId, BigDecimal amount, String currency, String merchantSecret) throws Exception {
        String amountStr = String.format("%.2f", amount);
        String hashedMerchantSecret = md5(merchantSecret).toUpperCase();

        String hashString = merchantId + orderId + amountStr + currency + hashedMerchantSecret;
        System.out.println("Hash String: " + hashString);

        return md5(hashString).toUpperCase();
    }

    private static String md5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
