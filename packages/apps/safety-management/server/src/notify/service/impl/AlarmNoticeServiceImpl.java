package com.guhecloud.cube.srv.gen.safety.management.notify.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.notify.dao.AlarmNoticeMapper;
import com.guhecloud.cube.srv.gen.safety.management.notify.dto.request.AlarmNoticeModifyRequest;
import com.guhecloud.cube.srv.gen.safety.management.notify.dto.vo.AlarmNoticeVO;
import com.guhecloud.cube.srv.gen.safety.management.notify.po.AlarmNoticeObjPO;
import com.guhecloud.cube.srv.gen.safety.management.notify.po.AlarmNoticePO;
import com.guhecloud.cube.srv.gen.safety.management.notify.po.AlarmNoticeWayPO;
import com.guhecloud.cube.srv.gen.safety.management.notify.service.AlarmNoticeObjService;
import com.guhecloud.cube.srv.gen.safety.management.notify.service.AlarmNoticeService;
import com.guhecloud.cube.srv.gen.safety.management.notify.service.AlarmNoticeWayService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmNoticeServiceImpl extends ServiceImpl<AlarmNoticeMapper, AlarmNoticePO> implements AlarmNoticeService {

    private final AlarmNoticeWayService alarmNoticeWayService;

    private final AlarmNoticeObjService alarmNoticeObjService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull AlarmNoticeModifyRequest request) {
        List<AlarmNoticeModifyRequest.AlarmNotifySceneConfigRequest> sceneConfigDTOS = request.getSceneConfigDTOS();
        if (ObjectUtils.isNotEmpty(sceneConfigDTOS)) {
            //先删除再新增
            deleteMessageConfig(request.getBusinessType());
            for (AlarmNoticeModifyRequest.AlarmNotifySceneConfigRequest config : sceneConfigDTOS) {
                AlarmNoticePO notice = AlarmNoticePO.builder()
                        .id(IdWorker.getId())
                        .alarmLevelCode(config.getAlarmLevelCode())
                        .enabled(config.getEnabled())
                        .businessType(request.getBusinessType())
                        .sceneCode(config.getSceneCode())
                        .value(config.getValue())
                        .tenant(SessionContextHolder.getRequester().getTenant())
                        .build();
                EntityAuditor.populateAudit(notice);
                this.baseMapper.insertOrUpdate(notice);
                if (ObjectUtils.isNotEmpty(config.getNoticeWays())) {
                    List<AlarmNoticeModifyRequest.AlarmNoticeWayRequest> noticeWays = config.getNoticeWays();
                    List<AlarmNoticeWayPO> saveNoticeWay = new ArrayList<>();
                    noticeWays.forEach(way -> {
                        AlarmNoticeWayPO messageAssemblyNoticeWay = new AlarmNoticeWayPO();
                        ObjectUtils.copying(way, messageAssemblyNoticeWay);
                        messageAssemblyNoticeWay.setAlarmNoticeId(notice.getId());
                        saveNoticeWay.add(messageAssemblyNoticeWay);
                    });
                    alarmNoticeWayService.saveBatch(saveNoticeWay);
                }
                if (ObjectUtils.isNotEmpty(config.getNoticeObjs())) {
                    List<AlarmNoticeModifyRequest.AlarmNoticeObjRequest> noticeObjs = config.getNoticeObjs();
                    List<AlarmNoticeObjPO> saveNoticeObj = new ArrayList<>();
                    noticeObjs.forEach(obj -> {
                        AlarmNoticeObjPO noticeObj = new AlarmNoticeObjPO();
                        ObjectUtils.copying(obj, noticeObj);
                        noticeObj.setAlarmNoticeId(notice.getId());
                        saveNoticeObj.add(noticeObj);
                    });
                    alarmNoticeObjService.saveBatch(saveNoticeObj);
                }
            }
        }
    }

    @Override
    public List<AlarmNoticeVO> getAlarmNoticeConfigInfo(@NonNull String businessType, Boolean enabled) {
        List<AlarmNoticePO> list = this.lambdaQuery()
                .eq(AlarmNoticePO::getBusinessType, businessType)
                .eq(Objects.nonNull(enabled), AlarmNoticePO::getEnabled, enabled)
                .list();
        List<AlarmNoticeVO> alarmNoticeVOS = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(list)) {
            list.forEach(notice -> {
                AlarmNoticeVO alarmNoticeVO = new AlarmNoticeVO();
                ObjectUtils.copying(notice, alarmNoticeVO);
                List<AlarmNoticeWayPO> noticeWays = this.alarmNoticeWayService.list(new LambdaQueryWrapper<AlarmNoticeWayPO>()
                                                                                            .eq(AlarmNoticeWayPO::getAlarmNoticeId, notice.getId()));
                if (ObjectUtils.isNotEmpty(noticeWays)) {
                    List<AlarmNoticeVO.AlarmNoticeWayVO> alarmNoticeWayVOS = new ArrayList<>();
                    noticeWays.forEach(way -> {
                        AlarmNoticeVO.AlarmNoticeWayVO alarmNoticeWayVO = new AlarmNoticeVO.AlarmNoticeWayVO();
                        ObjectUtils.copying(way, alarmNoticeWayVO);
                        alarmNoticeWayVOS.add(alarmNoticeWayVO);
                    });
                    alarmNoticeVO.setNoticeWays(alarmNoticeWayVOS);
                }
                List<AlarmNoticeObjPO> noticeObjs = this.alarmNoticeObjService.list(new LambdaQueryWrapper<AlarmNoticeObjPO>()
                                                                                            .eq(AlarmNoticeObjPO::getAlarmNoticeId, notice.getId()));
                if (ObjectUtils.isNotEmpty(noticeObjs)) {
                    List<AlarmNoticeVO.AlarmNoticeObjVO> alarmNoticeObjVOS = new ArrayList<>();
                    noticeObjs.forEach(obj -> {
                        AlarmNoticeVO.AlarmNoticeObjVO alarmNoticeObjVO = new AlarmNoticeVO.AlarmNoticeObjVO();
                        ObjectUtils.copying(obj, alarmNoticeObjVO);
                        alarmNoticeObjVOS.add(alarmNoticeObjVO);
                    });
                    alarmNoticeVO.setNoticeObjs(alarmNoticeObjVOS);
                }
                alarmNoticeVOS.add(alarmNoticeVO);
            });
        }
        return alarmNoticeVOS;
    }

    @Override
    public List<AlarmNoticeVO> getEnabledAlarmNoticeConfig(@NonNull String businessType) {
        return this.getAlarmNoticeConfigInfo(businessType, true);
    }

    @Override
    public AlarmNoticeVO getEnabledAlarmNotice(@NonNull String businessType, @NonNull String alarmLevelCode) {
        List<AlarmNoticePO> alarmNoticePOS = this.baseMapper.selectList(Wrappers.<AlarmNoticePO>lambdaQuery()
                                                                                .eq(AlarmNoticePO::getBusinessType, businessType)
                                                                                .eq(AlarmNoticePO::getAlarmLevelCode, alarmLevelCode)
                                                                                .eq(AlarmNoticePO::getEnabled, true));
        if (CollectionUtils.isEmpty(alarmNoticePOS)) {
            return null;
        }
        AlarmNoticePO alarmNoticePO = alarmNoticePOS.get(0);
        List<AlarmNoticeWayPO> noticeWays = this.alarmNoticeWayService.list(new LambdaQueryWrapper<AlarmNoticeWayPO>()
                                                                                    .eq(AlarmNoticeWayPO::getAlarmNoticeId, alarmNoticePO.getId()));
        List<AlarmNoticeObjPO> noticeObjs = this.alarmNoticeObjService.list(new LambdaQueryWrapper<AlarmNoticeObjPO>()
                                                                                    .eq(AlarmNoticeObjPO::getAlarmNoticeId, alarmNoticePO.getId()));
        AlarmNoticeVO alarmNoticeVO = ObjectUtils.copying(alarmNoticePO, AlarmNoticeVO.class);
        alarmNoticeVO.setNoticeWays(ObjectUtils.copying(noticeWays, AlarmNoticeVO.AlarmNoticeWayVO.class));
        alarmNoticeVO.setNoticeObjs(ObjectUtils.copying(noticeObjs, AlarmNoticeVO.AlarmNoticeObjVO.class));
        return alarmNoticeVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMessageConfig(@NonNull String businessType) {
        List<AlarmNoticePO> list = this.lambdaQuery()
                .eq(AlarmNoticePO::getBusinessType, businessType)
                .list();
        if (ObjectUtils.isNotEmpty(list)) {
            List<Long> ids = list.stream().map(AlarmNoticePO::getId).collect(Collectors.toList());
            this.removeByIds(ids);
        }
    }
}
