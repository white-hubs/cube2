package com.guhecloud.cube.biz.vmonitor.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.videogw.client.VideoClient;
import com.guhecloud.cube.base.videogw.client.command.VideoSnapCaptureCommand;
import com.guhecloud.cube.base.videogw.client.dto.VideoSnapCaptureDTO;
import com.guhecloud.cube.biz.vmonitor.server.dao.CameraPatrolRecordMapper;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolLine;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolRecord;
import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolDeviceResultEnum;
import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolDeviceStatusEnum;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolRecordModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolRecordQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolSnapCapture;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolLineService;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolRecordDeviceService;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolRecordService;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolRecordUserVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolSnapCaptureVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 视频巡逻记录服务实现
 *
 * @author lanchun
 * @date 2021/12/6 19:52
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CameraPatrolRecordServiceImpl extends ServiceImpl<CameraPatrolRecordMapper, CameraPatrolRecord> implements CameraPatrolRecordService {

    private final CameraPatrolRecordDeviceService cameraPatrolRecordDeviceService;

    private final CameraPatrolLineService cameraPatrolLineService;

    private final VideoClient videoClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull CameraPatrolRecordModifyParam param) {
        CameraPatrolLine cameraPatrolLine = this.cameraPatrolLineService.getById(param.getCameraPatrolLineId());
        Assert.isNotAcceptable(!cameraPatrolLine.getEnabled(), "巡逻路线已禁用");
        CameraPatrolRecord cameraPatrolRecord = ObjectUtils.copying(param, CameraPatrolRecord.class);
        //巡逻点数
        cameraPatrolRecord.setNum(param.getDeviceModifyParams().size());
        //完成数量
        cameraPatrolRecord.setCompleteNum(param.getDeviceModifyParams().stream()
                                                  .filter(deviceModifyParam -> CameraPatrolDeviceStatusEnum.CAMERA_PATROL_COMPLETE ==
                                                                               deviceModifyParam.getStatus()).collect(Collectors.toList())
                                                  .size());
        //未巡数量
        cameraPatrolRecord.setFreeNum(cameraPatrolRecord.getNum() - cameraPatrolRecord.getCompleteNum());
        //异常数量
        cameraPatrolRecord.setAbnormalNum(param.getDeviceModifyParams().stream()
                                                  .filter(deviceModifyParam -> CameraPatrolDeviceStatusEnum.CAMERA_PATROL_COMPLETE ==
                                                                               deviceModifyParam.getStatus() &&
                                                                               CameraPatrolDeviceResultEnum.CAMERA_PATROL_ABNORMAL ==
                                                                               deviceModifyParam.getResult())
                                                  .map(deviceModifyParam ->
                                                               deviceModifyParam.getDeviceRemarksModifyParams()
                                                      ).collect(Collectors.toList()).size());
        //正常数量
        cameraPatrolRecord.setNormalNum(param.getDeviceModifyParams().stream().filter(deviceModifyParam ->
                                                                                              CameraPatrolDeviceStatusEnum.CAMERA_PATROL_COMPLETE ==
                                                                                              deviceModifyParam.getStatus() &&
                                                                                              CameraPatrolDeviceResultEnum.CAMERA_PATROL_NORMAL ==
                                                                                              deviceModifyParam.getResult())
                                                .collect(Collectors.toList()).size());
        cameraPatrolRecord.setDuration(this.extendDuration(cameraPatrolRecord.getPatrolTime(), cameraPatrolRecord.getCompleteTime()));
        cameraPatrolRecord.setUserId(SecurityContextHolder.getPrincipal().getId());
        cameraPatrolRecord.setUserName(SecurityContextHolder.getPrincipal().getName());
        this.baseMapper.insert(cameraPatrolRecord);
        //巡逻设备记录
        this.cameraPatrolRecordDeviceService.save(cameraPatrolRecord.getId(), param.getDeviceModifyParams());
        //修改最后巡逻时间
        this.cameraPatrolLineService.updateLastPatrol(cameraPatrolRecord.getCameraPatrolLineId(), cameraPatrolRecord.getCompleteTime());
        return cameraPatrolRecord.getId();
    }

    @Override
    public CameraPatrolRecord getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public Page<CameraPatrolRecord> page(CameraPatrolRecordQueryParam param) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), CameraPatrolRecord::getCameraPatrolLineName, param.getKeyword())
                .ge(Objects.nonNull(param.getPeriod()) && Objects.nonNull(param.getPeriod()
                                                                                  .getBegin()), CameraPatrolRecord::getPatrolTime, ObjectUtils.ifNull(param.getPeriod(), Period::getBegin))
                .eq(Objects.nonNull(param.getCameraPatrolLineId()), CameraPatrolRecord::getCameraPatrolLineId, param.getCameraPatrolLineId())
                .lt(Objects.nonNull(param.getPeriod()) && Objects.nonNull(param.getPeriod()
                                                                                  .getEnd()), CameraPatrolRecord::getPatrolTime, ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                .orderByDesc(CameraPatrolRecord::getPatrolTime)
                .page(PagingUtils.convert(param));
    }

    @Override
    public List<CameraPatrolRecord> getList(CameraPatrolRecordQueryParam param) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), CameraPatrolRecord::getCameraPatrolLineName, param.getKeyword())
                .ge(Objects.nonNull(param.getPeriod()) && Objects.nonNull(param.getPeriod()
                                                                                  .getBegin()), CameraPatrolRecord::getPatrolTime, ObjectUtils.ifNull(param.getPeriod(), Period::getBegin))
                .eq(Objects.nonNull(param.getCameraPatrolLineId()), CameraPatrolRecord::getCameraPatrolLineId, param.getCameraPatrolLineId())
                .lt(Objects.nonNull(param.getPeriod()) && Objects.nonNull(param.getPeriod()
                                                                                  .getEnd()), CameraPatrolRecord::getPatrolTime, ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                .orderByDesc(CameraPatrolRecord::getPatrolTime)
                .list();
    }

    @Override
    public List<String> getPatrolUserNamesByLineId(@NonNull Long cameraPatrolLineId) {
        return this.baseMapper.selectPatrolUserNamesByLineId(cameraPatrolLineId);
    }

    @Override
    public List<CameraPatrolRecordUserVO> getPatrolUserNameByLineIds(@NonNull List<Long> cameraPatrolLineIds) {
        return CollectionUtils.isEmpty(cameraPatrolLineIds) ? Collections.emptyList()
                                                            : this.baseMapper.selectPatrolUserNameByLineIds(cameraPatrolLineIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
        this.cameraPatrolRecordDeviceService.removeByCameraPatrolRecordId(id);
    }

    @Override
    public CameraPatrolSnapCaptureVO snapCapture(CameraPatrolSnapCapture param) {
        VideoSnapCaptureCommand videoSnapCaptureCommand = new VideoSnapCaptureCommand();
        videoSnapCaptureCommand.setLocallyStored(true);
        videoSnapCaptureCommand.setSceneCode("CAMERA_PATROL::SNAP_CAPTURE");
        long videoFilePathId = IdWorker.getId();
        videoSnapCaptureCommand.setId(videoFilePathId);
        VideoSnapCaptureDTO videoSnapCaptureDTO = this.videoClient.videoSnapCapture(param.getDeviceId(), videoSnapCaptureCommand);
        String previewUrl = videoSnapCaptureDTO.getPreviewUrl();
        if (StringUtils.isNotBlank(videoSnapCaptureDTO.getPreviewUrlOut())) {
            previewUrl = videoSnapCaptureDTO.getPreviewUrlOut();
        }
        return CameraPatrolSnapCaptureVO.builder()
                .id(videoSnapCaptureDTO.getId())
                .previewUrl(previewUrl)
                .build();
    }

    private String extendDuration(LocalDateTime start, LocalDateTime end) {
        //巡逻完成耗时计算相差的秒数
        long seconds = DateUtils.between(start, end, ChronoUnit.SECONDS);
        return this.getDateMinPoor(seconds);
    }

    private String getDateMinPoor(long seconds) {
        long nm = 60;
        // 计算差多少分钟
        long min = seconds / nm;
        long sec = seconds % nm;
        return min + "分钟" + sec + "秒";
    }
}