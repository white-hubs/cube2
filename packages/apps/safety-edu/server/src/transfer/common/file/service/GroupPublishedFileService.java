package com.guhecloud.cube.srv.gen.safety.education.server.transfer.common.file.service;

import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileParam;
import com.guhecloud.cube.base.file.client.param.FileUploadParam;
import com.guhecloud.cube.srv.gen.safety.education.server.common.enumeration.SafetyEducationFileSceneCodeEnum;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectIdUtils;
import com.guhecloud.platform.base.util.SpringFileUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 集团下发文件上传
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupPublishedFileService {

    private final FileOperateClient fileOperateClient;

    public String uploadFileByPath(Integer tenantCode, String url) {
        try {
            var downLoadFileResponse = SpringFileUtils.downloadFile(url);
            var multipartFile = SpringFileUtils.fileByteConvertMultipartFile(
                    ObjectIdUtils.next() + "." + downLoadFileResponse.fileType(),
                    downLoadFileResponse.fileBytes());
            var fileUploadParam = new FileUploadParam();
            fileUploadParam.setFile(multipartFile);
            fileUploadParam.setSceneCode(SafetyEducationFileSceneCodeEnum.SAFETY_EDUCATION_GROUP_PUBLISH.getCode());
            fileUploadParam.setTenant(tenantCode);
            var upload = fileOperateClient.uploadSustain(fileUploadParam);
            return String.valueOf(upload.getId());
        } catch (Exception e) {
            log.error("集团文件转存到市场文件服务失败,失败原因:{}", e.getMessage());
        }
        return StringUtils.EMPTY_STRING;
    }

    public void fileBatchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            var fileParam = new FileParam();
            fileParam.setIds(idList);
            fileOperateClient.physicalDeleteByIds(fileParam);
        }
    }
}
