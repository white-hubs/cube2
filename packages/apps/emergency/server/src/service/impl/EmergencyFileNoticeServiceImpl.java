package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyFileNoticeMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFileNotice;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFileNoticeObj;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFileNoticeModifyParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyFileNoticeObjService;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyFileNoticeService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileNoticeVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyFileNoticeServiceImpl extends ServiceImpl<EmergencyFileNoticeMapper, EmergencyFileNotice> implements EmergencyFileNoticeService {

    private final EmergencyFileNoticeObjService emergencyFileNoticeObjService;

    private static final String EMERGENCY_FILE_NOTICE_SCENE_CODE = "EMERGENCY_FILE_EXPIRE_NOTIFY";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull EmergencyFileNoticeModifyParam param) {
        EmergencyFileNotice emergencyFileNotice = this.getBySceneCode(EMERGENCY_FILE_NOTICE_SCENE_CODE);
        String noticeWay = null;
        if (!CollectionUtils.isEmpty(param.getNoticeWays())) {
            noticeWay = String.join(",", param.getNoticeWays());
        }
        if (Objects.isNull(emergencyFileNotice)) {
            //新增
            emergencyFileNotice = new EmergencyFileNotice();
            emergencyFileNotice.setSceneCode(EMERGENCY_FILE_NOTICE_SCENE_CODE);
            emergencyFileNotice.setNoticeWay(noticeWay);
            this.baseMapper.insert(emergencyFileNotice);
        } else {
            //修改
            emergencyFileNotice.setNoticeWay(noticeWay);
            this.baseMapper.updateById(emergencyFileNotice);
        }
        this.emergencyFileNoticeObjService.removeBySceneCode(EMERGENCY_FILE_NOTICE_SCENE_CODE);
        if (!CollectionUtils.isEmpty(param.getEmergencyFileNoticeObjs())) {
            List<EmergencyFileNoticeObj> emergencyFileNoticeObjs = param.getEmergencyFileNoticeObjs().stream()
                    .map(emergencyFileNoticeObjParam -> {
                        String objIds = CollectionUtils.isEmpty(emergencyFileNoticeObjParam.getObjId()) ? null : emergencyFileNoticeObjParam
                                .getObjId().stream().map(String::valueOf)
                                .collect(joining(","));
                        return EmergencyFileNoticeObj.builder()
                                .sceneCode(EMERGENCY_FILE_NOTICE_SCENE_CODE)
                                .emergencyFileTypeId(emergencyFileNoticeObjParam.getEmergencyFileTypeId())
                                .objType(emergencyFileNoticeObjParam.getObjType())
                                .objId(objIds)
                                .build();
                    }).collect(toList());
            this.emergencyFileNoticeObjService.saveBatch(emergencyFileNoticeObjs);
        }
        return emergencyFileNotice.getId();
    }

    @Override
    public EmergencyFileNoticeVO getEmergencyFileNotice() {
        EmergencyFileNotice emergencyFileNotice = this.getBySceneCode(EMERGENCY_FILE_NOTICE_SCENE_CODE);
        if (Objects.isNull(emergencyFileNotice)) {
            return null;
        }
        EmergencyFileNoticeVO emergencyFileNoticeVO = EmergencyFileNoticeVO.builder()
                .sceneCode(emergencyFileNotice.getSceneCode())
                .noticeWays(StringUtils.isNotBlank(emergencyFileNotice.getNoticeWay()) ? new HashSet<>(Arrays.asList(emergencyFileNotice
                                                                                                                             .getNoticeWay()
                                                                                                                             .split(",")))
                                                                                       : Collections.emptySet())
                .build();
        List<EmergencyFileNoticeObj> emergencyFileNoticeObjs = this.emergencyFileNoticeObjService.getEmergencyFileNoticeObjsBySceneCode(EMERGENCY_FILE_NOTICE_SCENE_CODE);
        if (!CollectionUtils.isEmpty(emergencyFileNoticeObjs)) {
            Set<EmergencyFileNoticeVO.EmergencyFileNoticeObjVO> emergencyFileNoticeObjVOS = emergencyFileNoticeObjs.stream()
                    .map(emergencyFileNoticeObj ->
                                 EmergencyFileNoticeVO.EmergencyFileNoticeObjVO.builder()
                                         .emergencyFileTypeId(emergencyFileNoticeObj.getEmergencyFileTypeId())
                                         .objType(emergencyFileNoticeObj.getObjType())
                                         .objId(StringUtils.isNotBlank(emergencyFileNoticeObj.getObjId()) ?
                                                Arrays.stream(emergencyFileNoticeObj.getObjId().split(","))
                                                        .map(Long::parseLong)
                                                        .collect(toSet())
                                                                                                          : Collections.emptySet())
                                         .build()
                        ).collect(toSet());
            emergencyFileNoticeVO.setEmergencyFileNoticeObjs(emergencyFileNoticeObjVOS);
        }
        return emergencyFileNoticeVO;
    }

    @Override
    public EmergencyFileNotice getBySceneCode(@NonNull String sceneCode) {
        return this.baseMapper.selectOne(Wrappers.<EmergencyFileNotice>lambdaQuery()
                                                 .eq(EmergencyFileNotice::getSceneCode, sceneCode)
                                        );
    }
}
