package org.example.personaltaskmanagementsystem;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootTest
class PersonalTaskManagementSystemApplicationTests {

    @Test
    public void askLocalAnythingLLM() throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:3001/api/v1/workspace/01/thread/e8491285-ffcf-4f2f-bf71-719d499eac3f/chat")
                .header("Authorization", "Bearer PJ3GT1C-44TMCMW-G84R1T7-K8RM2DF")
                .header("Accept", "application/json")
                .header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .header("Host", "localhost:3001")
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("message", "0和1谁大")
                .field("mode", "chat")
                .asString();
        JSONObject jsonResponse = JSONObject.parseObject(response.getBody());
        String textResponse = jsonResponse.getString("textResponse");
        System.out.println(textResponse);
    }
//        final String TARGET_URL = "http://localhost:3001/api/v1/workspace/01/thread/e8491285-ffcf-4f2f-bf71-719d499eac3f/chat";
//        final String ACCEPT_HEADER = "application/json";
//        final String AUTHORIZATION_HEADER = "Bearer PJ3GT1C-44TMCMW-G84R1T7-K8RM2DF";
//        final String CONTENT_TYPE_HEADER = "application/json";
//        StringBuilder response = new StringBuilder();
//        try {
//            // 创建URL对象
//            URL url = new URL(TARGET_URL);
//
//            // 打开连接
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            // 设置请求方法为POST
//            connection.setRequestMethod("POST");
//
//            // 设置请求头
//            connection.setRequestProperty("accept", ACCEPT_HEADER);
//            connection.setRequestProperty("Authorization", AUTHORIZATION_HEADER);
//            connection.setRequestProperty("Content-Type", CONTENT_TYPE_HEADER);
//
//            // 允许向连接中写入数据
//            connection.setDoOutput(true);
//
//            // 创建JSON请求体
//            String jsonInputString = String.format("{ \"message\": \"%s\", \"mode\": \"query\" }", "0和1谁大");
//
//            // 向连接中写入数据
//            try (OutputStream os = connection.getOutputStream()) {
//                byte[] input = jsonInputString.getBytes("utf-8");
//                os.write(input, 0, input.length);
//            }
//
//            int responseCode = connection.getResponseCode();
//            System.out.println("POST Response Code :: " + responseCode);
//
//            // 读取响应内容
//            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
//                String responseLine = null;
//                while ((responseLine = br.readLine()) != null) {
//                    response.append(responseLine.trim());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // 创建JSONObject对象
//        JSONObject jsonObject = new JSONObject(Boolean.parseBoolean(response.toString()));
//        // 提取textResponse字段
//        String textResponse = jsonObject.getString("textResponse");
//        System.out.println(textResponse);
//
//    }

}
