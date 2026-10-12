package com.guhecloud.cube.srv.gen.loose.works.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.loose.works.dao.ProjectScheduleExtendMapper;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.param.ProjectScheduleParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectScheduleDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectScheduleExtendPO;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectScheduleExtendService;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/21 15:29
 * @Description: 工程监管扩展信息服务层默认实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectScheduleExtendServiceImpl extends ServiceImpl<ProjectScheduleExtendMapper, ProjectScheduleExtendPO> implements ProjectScheduleExtendService {
    private final UserClient userClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateProjectScheduleExtend(Long id, ProjectScheduleParam param) {
        ProjectScheduleExtendPO scheduleExtendPO = this.lambdaQuery().eq(ProjectScheduleExtendPO::getId, id)
                .last("limit 1").one();
        if (Objects.nonNull(scheduleExtendPO)) {
            this.lambdaUpdate()
                    .eq(ProjectScheduleExtendPO::getId, id)
                    .set(ProjectScheduleExtendPO::getPartyBMobile, param.getPartyBMobile())
                    .set(ProjectScheduleExtendPO::getStallCode, param.getStallCode())
                    .set(ProjectScheduleExtendPO::getStallLiaison, param.getStallLiaison())
                    .set(ProjectScheduleExtendPO::getStallMobile, param.getStallMobile())
                    .set(ProjectScheduleExtendPO::getSafetyDockUserId, param.getSafetyDockUserId())
                    .set(ProjectScheduleExtendPO::getConstructB, param.getConstructB())
                    .update();
        } else {
            ProjectScheduleExtendPO projectScheduleExtendPO = this.projectScheduleExtendConvert(param);
            projectScheduleExtendPO.setId(id);
            this.save(projectScheduleExtendPO);
        }
    }

    @Override
    public void extendInfo(ProjectScheduleDetailVO vo) {
        ProjectScheduleExtendPO scheduleExtendPO = this.lambdaQuery().eq(ProjectScheduleExtendPO::getId, vo.getId())
                .last("limit 1").one();
        if (Objects.nonNull(scheduleExtendPO)) {
            vo.setPartyBMobile(scheduleExtendPO.getPartyBMobile());
            vo.setStallCode(scheduleExtendPO.getStallCode());
            vo.setStallLiaison(scheduleExtendPO.getStallLiaison());
            vo.setStallMobile(scheduleExtendPO.getStallMobile());
            if (StringUtils.isNotBlank(scheduleExtendPO.getSafetyDockUserId())) {
                List<UserDTO> userDTOS = userClient.listUserByIds(Arrays.stream(scheduleExtendPO.getSafetyDockUserId().split(","))
                                                                          .map(Long::valueOf)
                                                                          .collect(Collectors.toSet()));
                vo.setSafetyDockUser(userDTOS);
            }
            vo.setConstructB(scheduleExtendPO.getConstructB());
        }
    }

    private ProjectScheduleExtendPO projectScheduleExtendConvert(ProjectScheduleParam param) {
        return ProjectScheduleExtendPO.builder()
                .partyBMobile(param.getPartyBMobile())
                .stallCode(param.getStallCode())
                .stallLiaison(param.getStallLiaison())
                .stallMobile(param.getStallMobile())
                .safetyDockUserId(param.getSafetyDockUserId())
                .constructB(param.getConstructB())
                .build();

    }
}
