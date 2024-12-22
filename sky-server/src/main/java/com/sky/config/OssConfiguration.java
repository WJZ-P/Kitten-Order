package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.properties.MyOssProperties;
import com.sky.utils.AliOssUtil;
import com.sky.utils.MyOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 配置类，创建AliOssUtils对象
 */
@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("开始创建阿里云文件上传工具类对象：{}", aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
    @Bean
    @ConditionalOnMissingBean
    public MyOssUtil myOssUtil(MyOssProperties myOssProperties){
        log.info("开始创建我的上传工具类对象{}",myOssProperties);
        //下面新建的是一个新的RestTemplate
        return new MyOssUtil(myOssProperties.getUrl(),new RestTemplate());
    }
}
