package com.guhecloud.cube.biz.meattrade.server.pork.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileUploadParam;
import com.guhecloud.cube.biz.mdata.client.VideoPtzClient;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.SpringFileUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.guhecloud.cube.biz.meattrade.server.constants.PorkFileSceneConstant.PORK_ARRANGE_IMAGE_CODE;

@Slf4j
@Component
public class FileTransfer {

    private final VideoPtzClient videoPtzClient;

    private final FileOperateClient fileOperateClient;

    public FileTransfer(VideoPtzClient videoPtzClient, FileOperateClient fileOperateClient) {
        this.videoPtzClient = videoPtzClient;
        this.fileOperateClient = fileOperateClient;
    }

    public List<Long> transferFiles(String[] picPaths, String[] transferFiles, Integer tenant) {
        if (0 == transferFiles.length) {
            return uploadPicPathConvertFile(picPaths, tenant);
        } else {
            return uploadPlaceHolderFile(picPaths, transferFiles, tenant);
        }
    }

    private List<Long> uploadPlaceHolderFile(String[] picPaths, String[] transferFiles, Integer tenant) {
        List<Long> fileIds = new CopyOnWriteArrayList<>();
        for (int i = 0; i < picPaths.length; i++) {
            var transferFile = transferFiles[i];
            var fileId = uploadPicPathConvertFile(picPaths[i], Long.valueOf(transferFile), tenant);
            if (Objects.nonNull(fileId)) {
                fileIds.add(fileId);
            }
        }
        return fileIds;
    }

    private List<Long> uploadPicPathConvertFile(String[] picPaths, Integer tenant) {
        List<Long> fileIds = new CopyOnWriteArrayList<>();
        //附件转存
        for (String picPath : picPaths) {
            var fileId = uploadPicPathConvertFile(picPath, null, tenant);
            if (Objects.nonNull(fileId)) {
                fileIds.add(fileId);
            }
        }
        return fileIds;
    }

    private Long uploadPicPathConvertFile(String picPath, Long fileId, Integer tenant) {
        var sessionDTOStr = this.videoPtzClient.getLoginSession();
        var session = "";
        if (StringUtils.isNotEmpty(sessionDTOStr)) {
            var sessionDTO = JsonUtils.deserialize(sessionDTOStr, new TypeReference<Map<String, String>>() {});
            session = sessionDTO.get("session");
        }
        var picPathUrl = new StringBuilder(picPath);
        picPathUrl.append("?session=").append(session);
        try {
            var fileNamePath = picPath.substring(picPath.lastIndexOf("/") + 1);
            var fileResponse = SpringFileUtils.downloadFile(picPathUrl.toString());
            var multipartFile = SpringFileUtils.fileByteConvertMultipartFile(fileNamePath, fileResponse.fileBytes());
            var fileModifyParam = new FileUploadParam();
            fileModifyParam.setFile(multipartFile);
            fileModifyParam.setFileId(fileId);
            fileModifyParam.setSceneCode(PORK_ARRANGE_IMAGE_CODE);
            fileModifyParam.setTenant(tenant);
            var fileVO = this.fileOperateClient.uploadSustain(fileModifyParam);
            return fileVO.getId();
        } catch (Exception e) {
            log.error("文件转存失败,失败原因:{}", e.getMessage(), e);
        }
        return null;
    }
}
