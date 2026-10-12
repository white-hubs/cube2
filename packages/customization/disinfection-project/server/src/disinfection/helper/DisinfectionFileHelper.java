package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper;

import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.base.file.client.param.FileParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DinsinfectionFileInduceSceneCodeEnum;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class DisinfectionFileHelper {
    private final FileClient fileClient;
    private final FileOperateClient fileOperateClient;

    public Map<Long, FileVO> getFilesMap(List<Long> fileIds) {
        if (ListUtils.isEmpty(fileIds)) {
            return Collections.emptyMap();
        }
        try {
            List<FileVO> fileVOList = getFiles(fileIds);
            return MapUtils.listToMap(fileVOList, FileVO::getId, fileVO -> fileVO);
        } catch (Exception e) {
            log.error("获取文件信息失败", e);
            return Collections.emptyMap();
        }

    }

    public boolean induceFiles(List<Long> induceFileIds, DinsinfectionFileInduceSceneCodeEnum sceneCode) {
        if (ListUtils.isEmpty(induceFileIds)) {
            return true;
        }
        try {
            FileInduceParam fileInduceParam = new FileInduceParam();
            fileInduceParam.setFileIdList(induceFileIds);
            fileInduceParam.setSceneCode(sceneCode.getCode());
            return fileOperateClient.fileInduceSingleBody(fileInduceParam);
        } catch (Exception e) {
            log.error("获取文件信息失败", e);
            return false;
        }

    }

    public boolean induceFile(DinsinfectionFileInduceSceneCodeEnum sceneCodeEnum, long fileId) {
        try {
            fileOperateClient.fileInduceSingle(sceneCodeEnum.getCode(), null, fileId);
            return true;
        } catch (Exception e) {
            log.error("获取文件信息失败", e);
            return false;
        }
    }

    public List<FileVO> getFiles(List<Long> fileIds) {
        if (ListUtils.isEmpty(fileIds)) {
            return Collections.emptyList();
        }
        try {
            var fileParam = new FileParam();
            fileParam.setIds(fileIds);
            return fileClient.getFileList(fileParam);
        } catch (Exception e) {
            log.error("获取文件失败:{}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public FileVO getFileById(Long fileId) {
        if (Objects.isNull(fileId)) {
            return null;
        }
        try {
            return fileClient.getById(fileId);
        } catch (Exception e) {
            log.error("获取文件失败:{}", e.getMessage());
            return null;
        }
    }

    public Map<Long, List<FileVO>> getByBusinessKeys(List<Long> businessKeys) {
        BusinessKeysParam fileParam = new BusinessKeysParam();
        fileParam.setBusinessKeys(businessKeys);
        return fileClient.getByBusinessKeys(fileParam);
    }
}
