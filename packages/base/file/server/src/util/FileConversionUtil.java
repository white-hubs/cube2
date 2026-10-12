package com.guhecloud.cube.base.file.server.util;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.SpringFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-26  09:25
 * @Description: 文件类型转换
 * @Version: 1.0
 */
@Slf4j
public class FileConversionUtil {
    /**
     * @description: MultipartFile 转 BufferedImage
     * @author: lijie
     * @date: 2024/1/26 9:26
     * @param: java.awt.image.BufferedImage
     * @return: [org.springframework.web.multipart.MultipartFile]
     **/
    public static BufferedImage convertToBufferedImage(MultipartFile multipartFile) {
        BufferedImage bufferedImage = null;
        try {
            // 读取文件内容并创建字节数组输入流
            ByteArrayInputStream inputStream = new ByteArrayInputStream(multipartFile.getBytes());
            // 使用ImageIO类从字节数组输入流中加载图像
            bufferedImage = ImageIO.read(inputStream);
        } catch (Exception e) {
            log.error("文件加载图形失败", e);
        }
        return bufferedImage;
    }

    public static MultipartFile convertByteToMultipartFile(byte[] bytes, String fileName, String contentType) {
        if (Objects.isNull(bytes)) {
            log.error("file:byte[] 转换失败，byte [] 为空");
            throw new ServiceException("由于输入byte数组为空，导致转换为MultipartFile失败");
        }
        try {
            return SpringFileUtils.fileByteConvertMultipartFile(fileName, bytes, contentType);
        } catch (IOException e) {
            log.error("file:byte[] 转换失败", e);
            throw new ServiceException("转换异常");
        }
    }
}
