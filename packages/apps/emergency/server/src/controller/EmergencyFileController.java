package com.guhecloud.cube.biz.emergency.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyEventType;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFile;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFileType;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFileModifyParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFileQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyEventTypeService;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyFileService;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyFileTypeService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileDataCollectVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFilePageVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileSafetyReportVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 预案文件管理接口
 *
 * @author lanchun
 * @date 2022/9/1 17:24
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyFileController {

    private final EmergencyFileService emergencyFileService;

    private final EmergencyFileTypeService emergencyFileTypeService;

    private final EmergencyEventTypeService emergencyEventTypeService;

    private final FileClient fileClient;

    /**
     * 预案文件新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/emergency-file")
    public Long save(@Valid @RequestBody EmergencyFileModifyParam param) {
        return this.emergencyFileService.save(param);
    }

    /**
     * 预案文件修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/emergency-file/{id}")
    public void update(@Min(value = 1, message = "预案文件主键不能小于1") @PathVariable("id") Long id,
                       @Valid @RequestBody EmergencyFileModifyParam param
                      ) {
        this.emergencyFileService.update(id, param);
    }

    /**
     * 预案文件详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/emergency-file/{id}")
    public EmergencyFileVO getEmergencyFile(@Min(value = 1, message = "预案文件主键不能小于1") @PathVariable("id") Long id) {
        EmergencyFile emergencyFile = this.emergencyFileService.getById(id);
        Assert.isNotFound(emergencyFile, "预案文件不存在");
        EmergencyFileVO emergencyFileVO = ObjectUtils.copying(emergencyFile, EmergencyFileVO.class);
        //获取事件分类信息
        EmergencyEventType emergencyEventType = null;
        if (StringUtils.isNotEmpty(emergencyFile.getEmergencyEventTypeCode())) {
            emergencyEventType = this.emergencyEventTypeService.getByCode(emergencyFile.getEmergencyEventTypeCode());
        }
        if (Objects.nonNull(emergencyEventType)) {
            emergencyFileVO.setEmergencyEventTypeName(emergencyEventType.getName());
        }
        EmergencyFileType emergencyFileType = null;
        if (Objects.nonNull(emergencyFile.getEmergencyFileTypeId())) {
            emergencyFileType = this.emergencyFileTypeService.getById(emergencyFile.getEmergencyFileTypeId());
        }
        if (Objects.nonNull(emergencyFileType)) {
            emergencyFileVO.setEmergencyFileTypeName(emergencyFileType.getName());
        }
        //获取附件信息map
        Map<Long, FileVO> fileVOMap = this.fileClient.getByBusinessKey(emergencyFileVO.getId()).stream()
                .collect(Collectors.toMap(FileVO::getId, fileVO -> fileVO, (o, n) -> n));
        fileVOMap.forEach((key, fileVO) -> {
            //文档附件信息
            if (key.toString().equals(emergencyFileVO.getDocumentFileId())) {
                emergencyFileVO.setDocumentFileVOS(Collections.singletonList(fileVO));
            }
            //流程附件信息
            if (key.toString().equals(emergencyFileVO.getProcessFileId())) {
                emergencyFileVO.setProcessFileVOS(Collections.singletonList(fileVO));
            }
        });
        return emergencyFileVO;
    }

    /**
     * 预案文件分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/emergency-files")
    public Pagination<EmergencyFilePageVO> getPage(@Valid EmergencyFileQueryParam param) {
        Page<EmergencyFilePageVO> emergencyFilePage = this.emergencyFileService.page(param);
        return PagingUtils.convert(emergencyFilePage);
    }

    /**
     * @Author wql
     * @Description 预案文件数据采集
     * @Date 16:13 2023/10/8
     **/
    @PreAuthorize("hasScope('emergency_files')")
    @GetMapping("/emergency-files/dataCollect")
    public List<EmergencyFileDataCollectVO> safetyManagementDataCollect(@RequestParam(required = false) Period period) {
        List<EmergencyFileDataCollectVO> emergencyFilePageVOS = emergencyFileService.safetyManagementDataCollect(period);
        if (CollectionUtils.isEmpty(emergencyFilePageVOS)) {
            return emergencyFilePageVOS;
        }
        //文件信息
        extendFile(emergencyFilePageVOS);
        return emergencyFilePageVOS;
    }

    private void extendFile(List<EmergencyFileDataCollectVO> emergencyFilePageVOS) {
        List<Long> ids = emergencyFilePageVOS.stream().map(EmergencyFileDataCollectVO::getId).collect(Collectors.toList());
        BusinessKeysParam businessKeysParam = new BusinessKeysParam();
        businessKeysParam.setBusinessKeys(ids);
        Map<Long, List<FileVO>> byBusinessKeys = this.fileClient.getByBusinessKeys(businessKeysParam);
        for (EmergencyFileDataCollectVO fileData : emergencyFilePageVOS) {
            if (!byBusinessKeys.containsKey(fileData.getId())) {
                continue;
            }
            List<FileVO> fileVOS = byBusinessKeys.get(fileData.getId());
            Optional<FileVO> documentFileOpt = fileVOS.stream().filter(file -> file.getId().equals(fileData.getDocumentFileId()))
                    .findFirst();
            Optional<FileVO> processFileOpt = fileVOS.stream().filter(file -> file.getId().equals(fileData.getProcessFileId()))
                    .findFirst();
            //文档附件信息
            documentFileOpt.ifPresent(documentFile -> {
                fileData.setDocumentFileName(documentFile.getName());
                fileData.setDocumentFilePath(documentFile.getPath());
            });
            //流程附件信息
            processFileOpt.ifPresent(processFile -> {
                fileData.setProcessFileName(processFile.getName());
                fileData.setProcessFilePath(processFile.getPath());
            });
        }
    }

    /**
     * 预案文件删除
     *
     * @param id
     */
    @DeleteMapping("/emergency-file/{id}")
    public void remove(@Min(value = 1, message = "预案文件主键不能小于1") @PathVariable("id") Long id) {
        this.emergencyFileService.remove(id);
    }

    /**
     * 安全报告预案文件数据采集
     *
     * @param period
     */
    @GetMapping("/emergency-file/safety-report")
    public EmergencyFileSafetyReportVO getEmergencyFileForSafetyReport(@RequestParam(value = "period", required = false) Period period) {
        return emergencyFileService.getEmergencyFileForSafetyReport(period);
    }
}
