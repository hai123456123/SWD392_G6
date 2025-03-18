package proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

public class GHNProxy {

    private static final String TOKEN = "c3c92472-0327-11f0-a742-a2917784f2cb"; // GHN token
    private static final int SHOP_ID = 5689316;
    private static final String GHN_API_URL = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";


    public static String fetchFromGHN(String urlString) {
        return fetchFromGHN(urlString, null);
    }

    public static String fetchFromGHN(String urlString, String jsonInput) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("token", TOKEN);
            conn.setDoOutput(true);

            if (jsonInput != null) {
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInput.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\":500, \"message\": \"" + e.toString() + "\"}";
        }
    }

    private int getServiceId(int fromDistrict, int toDistrict) throws IOException {
        String apiUrl = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services";

        JSONObject requestBody = new JSONObject();
        requestBody.put("shop_id", SHOP_ID);
        requestBody.put("from_district", fromDistrict);
        requestBody.put("to_district", toDistrict);

        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Token", TOKEN);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder responseText = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    responseText.append(responseLine.trim());
                }
                JSONObject responseJson = new JSONObject(responseText.toString());
                if (responseJson.has("data")) {
                    return responseJson.getJSONArray("data").getJSONObject(0).getInt("service_id");
                }
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy service_id
    }
    
    public static String GetShippingFee(String toDistrictId, String toWardCode) throws IOException {
        int toDistrict;
        try {
            toDistrict = Integer.parseInt(toDistrictId);
        } catch (NumberFormatException e) {
            return "{\"error\": \"toDistrictId không hợp lệ\"}";
        }


//        int serviceId = getServiceId(13005, Integer.parseInt(toDistrictId)); // 1447 là from_district (có thể thay đổi)
//        if (serviceId == -1) {
//            response.getWriter().write("{\"error\": \"Không tìm thấy dịch vụ vận chuyển\"}");
//            return;
//        }
        // Tạo request JSON
        JSONObject requestBody = new JSONObject();
        requestBody.put("shop_id", SHOP_ID);
        requestBody.put("to_ward_code", toWardCode);
        requestBody.put("from_district_id ", 13005);
        requestBody.put("to_district_id", Integer.parseInt(toDistrictId));
        requestBody.put("weight", 100);
        requestBody.put("service_type_id", 2);
        // Gửi request đến API GHN
        try {
            URL url = new URL(GHN_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Token", TOKEN);
            conn.setDoOutput(true);

            // Ghi dữ liệu vào request body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Nhận phản hồi từ API GHN
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder responseText = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        responseText.append(responseLine.trim());
                    }
                    return responseText.toString();
                }
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                    StringBuilder errorText = new StringBuilder();
                    String errorLine;
                    while ((errorLine = br.readLine()) != null) {
                        errorText.append(errorLine.trim());
                    }
                    return "{\"error\": \"Không thể kết nối đến API GHN\"}";
                }
            }
        } catch (Exception e) {
            return "{\"error\": \"Không thể kết nối đến API GHN\"}";
        }
    }
}

