package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectPlanQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectRecordQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectTaskQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectPlanExecStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectPlanPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectStatByTargetVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectStatOverviewVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 巡检统计
 * </p>
 *
 * @author zwb
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectStatController {

    private final InspectPlanQueryService planQueryService;
    private final InspectTaskQueryService taskQueryService;
    private final InspectRecordQueryService recordQueryService;
    private final UserClient userClient;

    @GetMapping("/inspect-stat/app/overview")
    public InspectStatOverviewVO getOverviewByApp() {

        Long userId = SecurityContextHolder.getPrincipal().getId();

        InspectStatOverviewVO vo = new InspectStatOverviewVO();
        this.statPlan(vo, userId);
        // 统计任务数，并获得 任务IDS
        this.statTask(vo, userId);
        this.statRecord(vo);

        return vo;
    }

    /*
        1. 巡检计划判断：设备是否纳入有效的巡检计划，没有扣除3分
        2. 巡检任务判断：设备近一个月是否进行有效的日常巡检，没有扣除3分
        3. 巡检异常判断：设备近一个月是否存在异常的日常巡检，每次扣除5分
    */

    /**
     * 巡检目标对象统计
     *
     * @param targetId 目标对象ID：设备ID，档位ID
     * @param period   时间
     *
     * @return 对象
     */
    @GetMapping("/inspect-stat/target-health")
    public InspectStatByTargetVO getHealthStatByTarget(@RequestParam("targetId") Long targetId,
                                                       @RequestParam(value = "period", required = false) Period period
                                                      ) {

        List<InspectRecordPO> recordPOS = recordQueryService.lambdaQuery()
                .eq(targetId != null, InspectRecordPO::getTargetId, targetId)
                .between(period !=
                         null, InspectRecordPO::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .select(InspectRecordPO::getTaskId, InspectRecordPO::getPlanId, InspectRecordPO::getResult)
                .list();

        long planNum = recordPOS.stream().map(InspectRecordPO::getPlanId).distinct().count();
        long taskNum = recordPOS.stream().map(InspectRecordPO::getTaskId).distinct().count();
        long abnormalNum = recordPOS.stream().filter(recordPO -> InspectRecordResultEnum.ABNORMAL == recordPO.getResult()).count();

        InspectStatByTargetVO vo = new InspectStatByTargetVO();
        vo.setPlanNum(planNum);
        vo.setTaskNum(taskNum);
        vo.setAbnormalNum(abnormalNum);

        return vo;
    }

    private void statPlan(InspectStatOverviewVO vo, Long userId) {
        UserDTO userDTO = getUserDTO(userId);
        if (userDTO != null) {
            List<Long> deptIds = new ArrayList<>();
            if (Objects.nonNull(userDTO.getOrganization())) {
                deptIds.add(userDTO.getOrganization().getId());
            }
            List<Long> postIds = userDTO.getPositions().stream().map(UserDTO.Position::getId).collect(Collectors.toList());
            List<InspectPlanPO> pos = planQueryService.lambdaQuery()
                    .eq(InspectPlanPO::getEnable, true)
                    .eq(InspectPlanPO::getStatus, InspectPlanExecStatusEnum.EXECUTING.name())
                    .select(InspectPlanPO::getReceiveType, InspectPlanPO::getReceiveDeptIds, InspectPlanPO::getReceivePostIds, InspectPlanPO::getReceiveIds)
                    .list();

            long count = pos.stream().filter(po -> {
                boolean b;
                if (StringUtils.isNotEmpty(po.getReceiveIds())) {
                    b = po.getReceiveIds().contains(userDTO.getId().toString());
                } else {
                    boolean existDept = false, existPost = false;
                    if (CollectionUtils.isNotEmpty(deptIds) && StringUtils.isNotEmpty(po.getReceiveDeptIds())) {
                        existDept = deptIds.stream().anyMatch(deptId -> po.getReceiveDeptIds().contains(deptId.toString()));
                    }
                    if (CollectionUtils.isNotEmpty(postIds) && StringUtils.isNotEmpty(po.getReceivePostIds())) {
                        existPost = postIds.stream().anyMatch(postId -> po.getReceivePostIds().contains(postId.toString()));
                    }
                    b = existDept && existPost;
                }
                return b;
            }).count();

            vo.setPlanNum((int) count);
        }

    }

    private void statTask(InspectStatOverviewVO vo, Long userId) {
        List<InspectTaskPO> pos = taskQueryService.lambdaQuery()
                .like(userId != null, InspectTaskPO::getReceiveIds, userId)
                .and(q -> q.eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.EXECUTING.name()).or()
                        .eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.ACCEPT.name()))
                .eq(InspectTaskPO::getIsSplit, 0)
                .select(InspectTaskPO::getId)
                .list();

        vo.setTaskNum(pos.size());
        vo.setTaskIds(pos.stream().map(InspectTaskPO::getId).collect(Collectors.toList()));

    }

    private void statRecord(InspectStatOverviewVO vo) {
        if (CollectionUtils.isNotEmpty(vo.getTaskIds())) {

            List<InspectRecordPO> pos = recordQueryService.lambdaQuery()
                    .in(InspectRecordPO::getTaskId, vo.getTaskIds())
                    .select(InspectRecordPO::getId, InspectRecordPO::getTaskId, InspectRecordPO::getStatus, InspectRecordPO::getResult)
                    .list();
            vo.setRecordNum(pos.size());
            long abnormalNum = pos.parallelStream().filter(po -> po.getResult() == InspectRecordResultEnum.ABNORMAL).count();
            vo.setRecordAbnormalNum(abnormalNum);

            pos.parallelStream().collect(Collectors.groupingBy(InspectRecordPO::getStatus))
                    .forEach((statusEnum, recordPOS) -> {
                        int size = recordPOS.size();
                        if (statusEnum == InspectRecordStatusEnum.EXECUTING) {
                            vo.setRecordExecutingNum(size);
                        }
                        if (statusEnum == InspectRecordStatusEnum.OVER) {
                            vo.setRecordOverNum(size);
                        }
                        if (statusEnum == InspectRecordStatusEnum.STOP) {
                            vo.setRecordStopNum(size);
                        }
                        if (statusEnum == InspectRecordStatusEnum.EXPIRE) {
                            vo.setRecordExpireNum(size);
                        }
                    });
        }
    }

    private UserDTO getUserDTO(Long userId) {
        UserDTO dto = null;
        try {
            dto = userClient.getUser(userId);
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
        }
        return dto;
    }
}
