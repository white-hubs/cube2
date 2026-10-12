package com.guhecloud.cube.biz.linkage.server.util;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileUploadParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.guhecloud.platform.base.util.SpringFileUtils.downloadFile;
import static com.guhecloud.platform.base.util.SpringFileUtils.fileByteConvertMultipartFile;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/15 18:30
 * @Description:
 */
@Slf4j
public class FileUtils {

    public static FileVO uploadFileByPath(String url, String storeBucket) throws Exception {
        try {
            FileOperateClient fileOperateClient = ApplicationContextHolder.getBean(FileOperateClient.class);
            var downLoadPictureResponse = downloadFile(url);
            if (Objects.isNull(downLoadPictureResponse)) {
                throw new ResourceNotFoundException("文件下载失败,文件不存在");
            }
            MultipartFile multipartFile = fileByteConvertMultipartFile(
                    IdWorker.getId() + "." + downLoadPictureResponse.fileType(),
                    downLoadPictureResponse.fileBytes());
            FileUploadParam fileUploadParam = new FileUploadParam();
            fileUploadParam.setFile(multipartFile);
            fileUploadParam.setSceneCode(storeBucket);
            fileUploadParam.setTenant(SessionContextHolder.getRequester().getTenant());
            return fileOperateClient.uploadSustain(fileUploadParam);
        } catch (Exception e) {
            log.error("图片转存到文件服务失败,失败原因:", e);
            throw e;
        }
    }

}

