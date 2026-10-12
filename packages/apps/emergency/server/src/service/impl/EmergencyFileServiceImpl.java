package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageChannelRequest;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSpecifyTmpSendRequest;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyFileMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFile;
import com.guhecloud.cube.biz.emergency.server.eumeration.EmergencyFileExpirationStatusEnum;
import com.guhecloud.cube.biz.emergency.server.eumeration.EmergencyFileSceneCodeEnum;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFileModifyParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFileQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyFileNoticeService;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyFileService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileDataCollectVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileEventTypeVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileEventTypeVO1;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileNoticeVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFilePageVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileSafetyReportVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyFileServiceImpl extends ServiceImpl<EmergencyFileMapper, EmergencyFile> implements EmergencyFileService {

    private final EmergencyFileNoticeService emergencyFileNoticeService;

    private final FileRefClient fileRefClient;

    private final MessageSendClient messageSendClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull EmergencyFileModifyParam param) {
        EmergencyFile emergencyFile = ObjectUtils.copying(param, EmergencyFile.class);
        emergencyFile.setExpirationStatus(EmergencyFileExpirationStatusEnum.NORMAL);
        expirationStatus(emergencyFile);
        this.baseMapper.insert(emergencyFile);
        //附件关联
        Set<Long> fileIds = Stream.of(Long.valueOf(param.getDocumentFileId()), Long.valueOf(param.getProcessFileId()))
                .collect(Collectors.toCollection(HashSet::new));
        refInduceBySet(fileIds, emergencyFile);
        this.msgSend(emergencyFile);
        return emergencyFile.getId();
    }

    private void refInduceBySet(Set<Long> fileIds, EmergencyFile emergencyFile) {
        FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
        fileRefCreateParam.setSceneCode(EmergencyFileSceneCodeEnum.EMERGENCY_FILE.getCode());
        fileRefCreateParam.setBusinessKey(emergencyFile.getId());
        fileRefCreateParam.setFileIdList(new ArrayList<>(fileIds));
        fileRefClient.refInduce(fileRefCreateParam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull EmergencyFileModifyParam param) {
        EmergencyFile emergencyFile = this.getById(id);
        Assert.isNotFound(emergencyFile, "预案附件不存在");
        Long updateExpirationDateBefore = null;
        if (Objects.nonNull(emergencyFile.getExpirationDate())) {
            updateExpirationDateBefore = DateUtils.toTimestamp(emergencyFile.getExpirationDate());
        }
        Set<Long> beforeFiles = Stream.of(Long.valueOf(emergencyFile.getDocumentFileId()), Long.valueOf(emergencyFile.getProcessFileId()))
                .collect(Collectors.toCollection(HashSet::new));

        ObjectUtils.copying(param, emergencyFile);
        expirationStatus(emergencyFile);
        //附件关联
        Set<Long> fileIds = Stream.of(Long.valueOf(param.getDocumentFileId()), Long.valueOf(param.getProcessFileId()))
                .collect(Collectors.toCollection(HashSet::new));
        fileIds.removeAll(beforeFiles);
        if (CollectionUtils.isNotEmpty(fileIds)) {
            // 移除之前的文件
            fileRefClient.removeByBusinessKeyAndScene(emergencyFile.getId(), EmergencyFileSceneCodeEnum.EMERGENCY_FILE.getCode());
            refInduceBySet(fileIds, emergencyFile);
        }
        this.baseMapper.updateById(emergencyFile);
        //时间修改过才发送消息
        if (EmergencyFileExpirationStatusEnum.EXPIRED == emergencyFile.getExpirationStatus() &&
            (Objects.isNull(updateExpirationDateBefore) ||
             (!updateExpirationDateBefore.equals(DateUtils.toTimestamp(param.getExpirationDate()))))) {
            this.msgSend(emergencyFile);
        }
    }

    @Override
    public List<EmergencyFile> getExpirationEmergencyFiles() {
        return this.baseMapper.selectList(Wrappers.<EmergencyFile>lambdaQuery()
                                                  .eq(EmergencyFile::getExpirationStatus, EmergencyFileExpirationStatusEnum.NORMAL)
                                                  .le(EmergencyFile::getExpirationDate, DateUtils.now())
                                         );
    }

    @Override
    public void expirationStatus(EmergencyFile emergencyFile) {
        emergencyFile.setExpirationStatus(EmergencyFileExpirationStatusEnum.NORMAL);
        if (Objects.nonNull(emergencyFile.getExpirationDate())) {
            LocalDateTime now = DateUtils.now();
            emergencyFile.setExpirationStatus(now.isAfter(emergencyFile.getExpirationDate()) ?
                                              EmergencyFileExpirationStatusEnum.EXPIRED :
                                              EmergencyFileExpirationStatusEnum.NORMAL);
        }
    }

    @Override
    public Page<EmergencyFilePageVO> page(EmergencyFileQueryParam param) {
        return this.baseMapper.selectEmergencyFilePage(PagingUtils.convert(param), param);
    }

    @Override
    public EmergencyFile getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public List<EmergencyFileDataCollectVO> safetyManagementDataCollect(Period period) {
        return this.baseMapper.safetyManagementDataCollect(period);
    }

    @Override
    public EmergencyFileSafetyReportVO getEmergencyFileForSafetyReport(Period period) {
        List<EmergencyFileEventTypeVO> emergencyFileEventTypeVOS = baseMapper.selectEmergencyFileEventType(period);
        List<String> eventTypeCodeList = new ArrayList<>();
        Long totalNum = 0L;
        if (CollectionUtils.isNotEmpty(emergencyFileEventTypeVOS)) {
            eventTypeCodeList = emergencyFileEventTypeVOS.stream().map(EmergencyFileEventTypeVO::getEmergencyEventTypeCode)
                    .distinct().collect(Collectors.toList());
            totalNum = emergencyFileEventTypeVOS.stream().map(EmergencyFileEventTypeVO::getTotalNum).reduce(Long::sum).get();
        }
        List<EmergencyFileEventTypeVO1> emergencyFileEventTypeVO1s = baseMapper.selectNoSetEmergencyFileEventType(eventTypeCodeList);
        if (CollectionUtils.isNotEmpty(emergencyFileEventTypeVO1s)) {
            for (int i = 0; i < emergencyFileEventTypeVO1s.size(); i++) {
                emergencyFileEventTypeVO1s.get(i).setIndex(i + 1);
            }
        }
        return EmergencyFileSafetyReportVO.builder()
                .totalNum(totalNum)
                .emergencyFileEventTypeList(emergencyFileEventTypeVOS)
                .noSetEmergencyFileList(emergencyFileEventTypeVO1s)
                .build();
    }

    @Override
    public void msgSend(EmergencyFile emergencyFile) {
        if (EmergencyFileExpirationStatusEnum.EXPIRED == emergencyFile.getExpirationStatus()) {
            msgSend(this.emergencyFileNoticeService.getEmergencyFileNotice(), emergencyFile);
        }
    }

    @Override
    public void msgSend(EmergencyFileNoticeVO emergencyFileNotice, EmergencyFile emergencyFile) {
        try {
            if (Objects.isNull(emergencyFile.getEmergencyFileTypeId())) {
                return;
            }
            if (Objects.nonNull(emergencyFileNotice) && !CollectionUtils.isEmpty(emergencyFileNotice.getNoticeWays()) &&
                !CollectionUtils.isEmpty(emergencyFileNotice.getEmergencyFileNoticeObjs())) {
                List<EmergencyFileNoticeVO.EmergencyFileNoticeObjVO> noticeObjs = emergencyFileNotice.getEmergencyFileNoticeObjs().stream()
                        .filter(emergencyFileNoticeObj -> emergencyFile.getEmergencyFileTypeId()
                                .equals(emergencyFileNoticeObj.getEmergencyFileTypeId()))
                        .toList();
                if (CollectionUtils.isEmpty(noticeObjs)) {
                    return;
                }
                List<MessageNotifyTarget> messageNotifyTargets = noticeObjs.stream().map(noticeObj ->
                                                                                                 MessageNotifyTarget.builder()
                                                                                                         .notifyTargetType(MessageNotifyTargetEnum.valueOf(noticeObj.getObjType()))
                                                                                                         .ids(noticeObj.getObjId())
                                                                                                         .build()
                                                                                        ).collect(Collectors.toList());
                List<MessageChannelRequest> channelRequests = emergencyFileNotice.getNoticeWays().stream().map(noticeWay ->
                                                                                                                       MessageChannelRequest
                                                                                                                               .builder()
                                                                                                                               .channelCode(MessageChannelEnum.valueOf(noticeWay))
                                                                                                                               .build()
                                                                                                              )
                        .collect(Collectors.toList());
                Map<String, String> tmpParams = new HashMap<>();
                tmpParams.put("emergencyFileName", emergencyFile.getName());
                MessageSpecifyTmpSendRequest messageSpecifyTmpSendRequest = MessageSpecifyTmpSendRequest.builder()
                        .msgSceneCode(emergencyFileNotice.getSceneCode())
                        .businessId(emergencyFile.getId().toString())
                        .tmpParams(tmpParams)
                        .messageNotifyTargets(messageNotifyTargets)
                        .channel(channelRequests)
                        .build();
                this.messageSendClient.sceneSpecifyTmpSend(messageSpecifyTmpSendRequest);
            }
        } catch (Exception e) {
            log.info("预案文件到期消息发送失败,失败原因:", e);
        }
    }
}
