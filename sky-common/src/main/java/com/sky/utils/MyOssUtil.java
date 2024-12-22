package com.sky.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Objects;

@Data
@AllArgsConstructor
@Slf4j
public class MyOssUtil {
    private String url;
    private RestTemplate restTemplate;

    // 计算文件的 MD5 值
    private String calculateMD5(byte[] imgBuffer) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(imgBuffer);
            return byteArrayToHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // 将字节数组转换为十六进制字符串
    private String byteArrayToHex(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    /**
     * 上传媒体文件:图片 or 视频，限制20M
     * @param mediaFile 媒体文件
     * @return
     */
    public String uploadMedia(MultipartFile mediaFile) throws IOException {


        HttpHeaders headers = new HttpHeaders();//创建请求头
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Connection", "close"); // 禁用 Keep-Alive，因为使用频率不高

        // 创建请求体
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        String md5Filename = calculateMD5(mediaFile.getBytes()) + ".png"; // 使用 MD5 作为文件名
        ByteArrayResource byteArrayResource = new ByteArrayResource(mediaFile.getBytes()) {
            @Override
            public String getFilename() {
                return md5Filename; // 使用 MD5 作为文件名
            }
        };
        body.add("media", byteArrayResource);

        // 创建请求实体
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 发送请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        log.info("上传媒体文件的响应：{}", responseEntity);

        JsonObject jsonObject = JsonParser.parseString(Objects.requireNonNull(responseEntity.getBody())).getAsJsonObject();

        // 返回媒体文件的url
        return jsonObject.get("url").getAsString();
    }
}
