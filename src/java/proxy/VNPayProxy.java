package proxy;

import model.VNPayConfig;
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import model.VNPayConfig;

public class VNPayProxy {

    public static String createPaymentUrl(long amount, String orderInfo, String returnUrl) {
        try {
            String vnp_TxnRef = VNPayConfig.getRandomNumber(8); // Mã giao dịch
            String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
            String vnp_HashSecret = VNPayConfig.vnp_HashSecret;
            String vnp_OrderInfo = orderInfo;
            String vnp_OrderType = "other";
            String vnp_Locale = "vn";
            String vnp_BankCode = "";
            String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String vnp_IpAddr = "127.0.0.1"; // Nếu lấy từ request thì dùng VNPayConfig.getIpAddress(request)

            // Tạo danh sách tham số
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // VNPay yêu cầu số tiền nhân 100
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_BankCode", vnp_BankCode);
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.put("vnp_OrderType", vnp_OrderType);
            vnp_Params.put("vnp_Locale", vnp_Locale);
            vnp_Params.put("vnp_ReturnUrl", returnUrl);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            // Sắp xếp tham số theo thứ tự alphabet để tạo chữ ký
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for (String fieldName : fieldNames) {
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                    hashData.append(fieldName).append("=").append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                    query.append(fieldName).append("=").append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                    if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                        hashData.append("&");
                        query.append("&");
                    }
                }
            }

            // Tạo chữ ký
            String vnp_SecureHash = VNPayConfig.hmacSHA512(vnp_HashSecret, hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);

            // Tạo URL thanh toán
            return VNPayConfig.vnp_PayUrl + "?" + query.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
