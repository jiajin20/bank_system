package com.banksystem.application.web.util;

import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.entity.UserInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReIPAddress {
    private static final String GEOLOCATION_API = "http://ipinfo.io/";
    public static String getIP(){
        return getOutIP();
    }

    public static String getOutIP() {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
        return "";
    }
    public static String getCity(String ipAddress) {
        StringBuilder result = new StringBuilder();
        try {
            // 查询 IP 地址
            URL url = new URL(GEOLOCATION_API + ipAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000); // 5秒连接超时
            conn.setReadTimeout(5000); // 5秒读取超时

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String inputLine;
                    StringBuilder jsonResponse = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        jsonResponse.append(inputLine);
                    }

                    // 解析 JSON
                    JSONObject jsonObject = new JSONObject(Boolean.parseBoolean(jsonResponse.toString()));
                    String jsonResponseStr = jsonResponse.toString();
                    if ("0".equals(jsonObject.getString("code"))) {
                        String city = jsonObject.getJSONObject("data").getString("city");
                        result.append("城市: ").append(city);
                    } else {
                        String city = extractValueByKey(jsonResponseStr, "city");
                        return city;
                    }
                }
            } else {
                result.append("请求失败: ").append(responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.append("请求错误: ").append(e.getMessage());
        }

        return result.toString();
    }
    public static String getRegion(String ipAddress) {
        StringBuilder result = new StringBuilder();
        try {
            // 查询 IP 地址
            URL url = new URL(GEOLOCATION_API + ipAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000); // 5秒连接超时
            conn.setReadTimeout(5000); // 5秒读取超时

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String inputLine;
                    StringBuilder jsonResponse = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        jsonResponse.append(inputLine);
                    }

                    // 解析 JSON
                    JSONObject jsonObject = new JSONObject(Boolean.parseBoolean(jsonResponse.toString()));
                    String jsonResponseStr = jsonResponse.toString();
                    if ("0".equals(jsonObject.getString("code"))) {
                        String region = jsonObject.getJSONObject("data").getString("region");
                        result.append("省份: ").append(region);
                    } else {
                        String region = extractValueByKey(jsonResponseStr, "region");
                        return region;
                    }
                }
            } else {
                result.append("请求失败: ").append(responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.append("请求错误: ").append(e.getMessage());
        }

        return result.toString();
    }
    public static String extractValueByKey(String jsonString, String key) {
        String searchKey = "\"" + key + "\": \"";
        int startIndex = jsonString.indexOf(searchKey);

        if (startIndex != -1) {
            // 找到开始位置后，计算出实际的值开始位置
            startIndex += searchKey.length();
            int endIndex = jsonString.indexOf("\"", startIndex);

            if (endIndex != -1) {
                return jsonString.substring(startIndex, endIndex);
            }
        }
        return ""; // 如果找不到，返回空字符串
    }
}
