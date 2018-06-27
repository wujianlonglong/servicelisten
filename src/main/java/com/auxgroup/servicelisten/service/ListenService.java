package com.auxgroup.servicelisten.service;

import com.alibaba.fastjson.JSONObject;
import com.auxgroup.servicelisten.exceptions.RetryRestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class ListenService {
    @Value("${sms.phone}")
    private String phone;

    @Value("${sms.url}")
    private String smsurl;

    @Value("${listenUrl}")
    private String listenUrl;

    private int num = 0;

    @Retryable(value = {RestClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000l, multiplier = 1))
    public void tryApplicationState() {
        System.out.println("num:" + num);
//        String erroMes = "app服务有问题！";
        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(listenUrl, String.class);
            JSONObject jsonObject = JSONObject.parseObject(response);
            if (!jsonObject.getString("code").equals("200")) {
                String message = jsonObject.getString("message");
//                erroMes = "inner服务有问题！";
                RetryRestException retryRestException = new RetryRestException(message);
                throw retryRestException;
            }
            num = 0;
        } catch (RestClientException ex) {
            log.error("请求失败:" + ex.toString());
            throw ex;
        }
    }

    @Recover
    public void recover(RestClientException e) {
        num++;
        if (num < 3)
            return;
        //发短信
        String mesContent = LocalDateTime.now().toString() + ":" + e.toString();
        List<String> phoneList = Arrays.asList(phone.split(","));
        for (String mobile : phoneList) {
            sendMsg(mobile, mesContent);
        }
        num = 0;
    }


    public void sendMsg(String mobile, String mesContent) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("systemName", "smarthome");
        jsonObject.put("userNumber", mobile);
        jsonObject.put("messageContent", mesContent);
        String str = jsonObject.toJSONString();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity entity = new HttpEntity(str, httpHeaders);
        restTemplate.postForObject(smsurl, entity, String.class);
    }

}
