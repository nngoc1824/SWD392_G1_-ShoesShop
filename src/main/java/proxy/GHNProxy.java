package proxy;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GHNProxy {

    private static final String TOKEN = "796859fa-5fca-11f0-9b80-96385126568f";
    private static final String SHOP_ID = "5888673";
    private static final int FROM_DISTRICT_ID = 1451;

    private static final String GHN_PROVINCE_API = "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";
    private static final String GHN_DISTRICT_API = "https://online-gateway.ghn.vn/shiip/public-api/master-data/district";
    private static final String GHN_WARD_API = "https://online-gateway.ghn.vn/shiip/public-api/master-data/ward";
    private static final String GHN_FEE_API = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";
    private static final Logger log = LoggerFactory.getLogger(GHNProxy.class);

    public String getProvinces() throws Exception {
        return sendPost(GHN_PROVINCE_API, "{}", TOKEN);
    }

    public String getDistricts(int provinceId) throws Exception {
        String body = "{\"province_id\":" + provinceId + "}";
        return sendPost(GHN_DISTRICT_API, body, TOKEN);
    }

    public String getWards(int districtId) throws Exception {
        String body = "{\"district_id\":" + districtId + "}";
        return sendPost(GHN_WARD_API, body, TOKEN);
    }

    public int calculateShippingFee(int toDistrictId, String toWardCode) throws Exception {
        log.info("Calculating shipping fee to district ID: {}, ward code: {}", toDistrictId, toWardCode);
        JSONObject json = new JSONObject();
        json.put("from_district_id", 1454); // ✅ ID kho của bạn
        json.put("from_ward_code", "21211"); // ✅ Bắt buộc GHN
        json.put("service_id", 53320); // ✅ Lấy đúng ID dịch vụ GHN cung cấp
        json.put("service_type_id", 2);
        json.put("to_district_id", toDistrictId);

        json.put("to_ward_code", toWardCode+"");
        json.put("height", 50);
        json.put("length", 20);
        json.put("weight", 200);
        json.put("width", 20);
        json.put("insurance_value", 10000);
        json.put("cod_failed_amount", 2000);
        json.put("coupon", JSONObject.NULL);

        JSONArray items = new JSONArray();
        JSONObject item = new JSONObject();
        item.put("name", "TEST1");
        item.put("quantity", 1);
        item.put("height", 200);
        item.put("weight", 1000);
        item.put("length", 200);
        item.put("width", 200);
        items.put(item);

        json.put("items", items);

        System.out.println("Payload: " + json.toString());

        String result = sendPost(GHN_FEE_API, json.toString(), TOKEN, SHOP_ID);

        System.out.println("GHN trả về: " + result);

        JSONObject resJson = new JSONObject(result);

        if (resJson.has("data") && !resJson.isNull("data")) {
            JSONObject data = resJson.getJSONObject("data");
            return data.getInt("total");
        } else {
            throw new Exception("GHN không trả về data hợp lệ: " + result);
        }
    }

    private String sendPost(String apiUrl, String jsonBody, String token) throws Exception {
        return sendPost(apiUrl, jsonBody, token, null);
    }

    private String sendPost(String apiUrl, String jsonBody, String token, String shopId) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Token", token);
        if (shopId != null) {
            conn.setRequestProperty("ShopId", shopId);
        }
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes("utf-8"));
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        reader.close();

        return sb.toString();
    }
}
