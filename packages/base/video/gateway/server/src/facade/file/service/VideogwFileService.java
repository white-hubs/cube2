package com.guhecloud.cube.base.videogw.server.facade.file.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileUploadParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import com.guhecloud.platform.base.util.SpringFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideogwFileService {

    private final FileOperateClient fileOperateClient;

    public FileVO uploadFileByPath(Long fileId, String url, String storeSceneCode) {
        try {
            var downLoadPictureResponse = SpringFileUtils.downloadFile(url);
            if (Objects.isNull(downLoadPictureResponse)) {
                throw new ResourceNotFoundException("文件下载失败,文件不存在");
            }
            MultipartFile multipartFile = SpringFileUtils.fileByteConvertMultipartFile(
                    IdWorker.getId() + "." + downLoadPictureResponse.fileType(),
                    downLoadPictureResponse.fileBytes());
            FileUploadParam fileUploadParam = new FileUploadParam();
            fileUploadParam.setFileId(fileId);
            fileUploadParam.setFile(multipartFile);
            fileUploadParam.setSceneCode(storeSceneCode);
            fileUploadParam.setTenant(SessionContextHolder.getRequester().getTenant());
            return this.fileOperateClient.uploadSustain(fileUploadParam);
        } catch (Exception e) {
            log.error("抓拍图片转存到文件服务失败,失败原因:", e);
        }
        return null;
    }
}
