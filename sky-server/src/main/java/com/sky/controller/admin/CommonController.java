package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import com.sky.utils.MyOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    // 依赖注入
    @Autowired
    private AliOssUtil aliOssUtil;
    @Autowired
    private MyOssUtil myOssUtil;
    @PostMapping("upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);

        try {
            // 原始文件名
            String originalFilename = file.getOriginalFilename();
            // 截取文件后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 使用UUID作为文件名
            String objectName = UUID.randomUUID() + extension;
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);

            return Result.success(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("文件上传失败");
        }
    }

    @PostMapping("uploadMedia")
    @ApiOperation("媒体文件上传")
    public Result<String> uploadMedia(MultipartFile file){
        log.info("媒体文件上传{}",file);
        try{
            String filePath = myOssUtil.uploadMedia(file);
            return Result.success(filePath);

        }catch (IOException e){
            e.printStackTrace();
            return Result.error("媒体文件上传失败");
        }
    }
}
