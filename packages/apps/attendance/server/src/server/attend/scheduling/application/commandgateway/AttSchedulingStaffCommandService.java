package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.commandgateway;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupStaffParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttRenewUserInfoParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 排班人员
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttSchedulingStaffCommandService extends ServiceImpl<AttSchedulingStaffMapper, AttSchedulingStaffPO> {

    private final AttGroupMapper attGroupMapper;
    private final AttGroupStaffMapper attGroupStaffMapper;
    private final UserClient userClient;
    private final DataDictionaryClient dataDictionaryClient;
    private final AttSchedulingStaffMapper attSchedulingStaffMapper;

    /**
     * 初始化排班人员
     *
     * @param yearMonth (yyyy-MM)
     */
    public void initMonthSchedulingStaff(String yearMonth) {
        if (StringUtils.isBlank(yearMonth)) {
            yearMonth = DateUtils.format(LocalDateTime.now(), "yyyy-MM");
        }
        AttGroupPO allGroup = attGroupMapper.selectOne(Wrappers.<AttGroupPO>lambdaQuery()
                                                               .eq(AttGroupPO::getRanges, AttGroupRangesEnum.ALL.name())
                                                               .last("limit 1")
                                                      );

        List<AttSchedulingStaffPO> insertList = new ArrayList<>();
        if (allGroup != null) {
            UserQueryParam userQueryParam = new UserQueryParam();
            userQueryParam.setEnabled(true);
            userQueryParam.setPageSize(999);
            userQueryParam.setPageNumber(1);
            Pagination<UserDTO> pagination = userClient.listUsers(userQueryParam);
            if (pagination.getRecords() != null) {
                for (UserDTO userDTO : pagination.getRecords()) {
                    AttSchedulingStaffPO po = AttSchedulingStaffPO.builder()
                            .schedulingMonth(yearMonth)
                            .staffId(userDTO.getId())
                            .staffName(userDTO.getName())
                            .groupId(allGroup.getId())
                            .build();
                    insertList.add(po);
                    if (insertList.size() > AttConstants.bachSize) {
                        this.saveBatch(insertList);
                        insertList = new ArrayList<>();
                    }
                }
            }
        } else {
            List<AttGroupPO> attGroupPOList = attGroupMapper.selectList(Wrappers.emptyWrapper());
            if (attGroupPOList != null && !attGroupPOList.isEmpty()) {
                List<AttGroupStaffPO> groupStaffPOList = attGroupStaffMapper.selectList(Wrappers.lambdaQuery());
                if (groupStaffPOList != null && !groupStaffPOList.isEmpty()) {
                    for (AttGroupStaffPO groupStaffPO : groupStaffPOList) {
                        AttSchedulingStaffPO po = AttSchedulingStaffPO.builder()
                                .schedulingMonth(yearMonth)
                                .staffId(groupStaffPO.getStaffId())
                                .staffName(groupStaffPO.getName())
                                .groupId(groupStaffPO.getGroupId())
                                .build();
                        insertList.add(po);
                        if (insertList.size() > AttConstants.bachSize) {
                            this.saveBatch(insertList);
                            insertList = new ArrayList<>();
                        }
                    }
                }
            }
        }
        if (!insertList.isEmpty()) {
            this.saveBatch(insertList);
        }
    }

    /**
     * 更新人员考勤照片
     *
     * @param command 入参
     */
    public void renewStaffPhoto(AttRenewUserInfoParam command) {

        // 查询更新考勤的请求地址
        DataDictionaryaramParam param = new DataDictionaryaramParam();
        param.setCode("att_update_staff_photograph");
        param.setItemCode("att-face-recognition");
        List<DataDictionaryDTO> list = dataDictionaryClient.getDataDictionaryByItem(param);
        if (list != null && !list.isEmpty()) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userId", command.getId());
            paramMap.put("userName", command.getName());
            paramMap.put("photographKey", command.getPhotographKey());
            String str = HttpUtil.post(list.getFirst().getName(), JsonUtils.serialize(paramMap));
            if (!"success".equals(str)) {
                log.error("更新" + command.getName() + "考勤照片失败:" + str);
            }
        } else {
            log.error("未配置考勤照片更新地址！");
        }
    }

    /**
     * 更新排班人员
     *
     * @param command 入参
     */
    public void renewSchedulingStaff(AttRenewUserInfoParam command) {
        AttGroupPO allGroup = attGroupMapper.selectOne(Wrappers.<AttGroupPO>lambdaQuery()
                                                               .eq(AttGroupPO::getRanges, AttGroupRangesEnum.ALL.name())
                                                               .last("limit 1")
                                                      );
        String yearMonth = DateUtils.format(LocalDateTime.now(), "yyyy-MM");
        if (allGroup != null) {
            // 查询是否有这个人
            AttSchedulingStaffPO schedulingStaffPO = attSchedulingStaffMapper.selectOne(Wrappers.<AttSchedulingStaffPO>lambdaQuery()
                                                                                                .eq(AttSchedulingStaffPO::getSchedulingMonth, yearMonth)
                                                                                                .eq(AttSchedulingStaffPO::getStaffId, command.getId())
                                                                                                .last("limit 1")
                                                                                       );
            AttSchedulingStaffPO po = AttSchedulingStaffPO.builder()
                    .groupId(allGroup.getId())
                    .staffId(command.getId())
                    .staffName(command.getName())
                    .schedulingMonth(yearMonth)
                    .build();
            po.setTenant(command.getTenant());
            if (schedulingStaffPO == null) {
                // 如果没有就添加
                attSchedulingStaffMapper.insert(po);
            }
        } else {
            // 修改
            attSchedulingStaffMapper.update(null, Wrappers.<AttSchedulingStaffPO>lambdaUpdate()
                                                    .eq(AttSchedulingStaffPO::getStaffId, command.getId())
                                                    .eq(AttSchedulingStaffPO::getSchedulingMonth, yearMonth)
                                                    .set(AttSchedulingStaffPO::getStaffName, command.getName())
                                           );
        }
    }

    /**
     * 考勤组变更新增考勤组人员
     *
     * @param groupPO     考勤组
     * @param yearMonth   年月yyyy-MM
     * @param staffPOList 新增的人员集合
     */
    public void schedulingStaffAdd(AttGroupPO groupPO, String yearMonth, List<AttGroupStaffParam> staffPOList) {
        if (groupPO == null) {
            return;
        }
        List<AttSchedulingStaffPO> schedulingStaffPOList = new ArrayList<>();
        // 获取变更人员
        if (AttGroupRangesEnum.ALL.name().equals(groupPO.getRanges())) {
            UserQueryParam userQueryParam = new UserQueryParam();
            userQueryParam.setEnabled(true);
            userQueryParam.setPageNumber(1);
            userQueryParam.setPageSize(999);
            Pagination<UserDTO> pagination = userClient.listUserByTenant(SessionContextHolder.getRequester().getTenant(), userQueryParam);
            schedulingStaffPOList = pagination.getRecords().stream().map(user ->
                                                                                 AttSchedulingStaffPO.builder()
                                                                                         .id(IdWorker.getId())
                                                                                         .groupId(groupPO.getId())
                                                                                         .staffId(user.getId())
                                                                                         .staffName(user.getName())
                                                                                         .schedulingMonth(yearMonth)
                                                                                         .build()
                                                                        ).collect(Collectors.toList());
        } else {
            if (staffPOList != null) {
                schedulingStaffPOList = staffPOList.stream().map(groupStaff ->
                                                                         AttSchedulingStaffPO.builder()
                                                                                 .id(IdWorker.getId())
                                                                                 .groupId(groupPO.getId())
                                                                                 .staffId(groupStaff.getId())
                                                                                 .staffName(groupStaff.getName())
                                                                                 .schedulingMonth(yearMonth)
                                                                                 .build()
                                                                ).collect(Collectors.toList());
            }
        }
        if (schedulingStaffPOList.isEmpty()) {
            return;
        }
        // 获取处理排班人员所需数据
        Map<Long, Boolean> staffMonthMap = new HashMap<>();
        List<AttSchedulingStaffPO> staffMonthList = attSchedulingStaffMapper.selectList(Wrappers.<AttSchedulingStaffPO>lambdaQuery()
                                                                                                .eq(AttSchedulingStaffPO::getSchedulingMonth, yearMonth)
                                                                                       );
        if (staffMonthList != null && !staffMonthList.isEmpty()) {
            for (AttSchedulingStaffPO schedulingStaffPO : staffMonthList) {
                staffMonthMap.put(schedulingStaffPO.getStaffId(), true);
            }
        }
        List<AttSchedulingStaffPO> insList = new ArrayList<>();

        // 遍历人员处理
        schedulingStaffPOList.forEach(schedulingStaff -> {
            boolean notExist = staffMonthMap.get(schedulingStaff.getStaffId()) == null;

            if (notExist) {
                insList.add(schedulingStaff);
            } else {
                attSchedulingStaffMapper.update(null, Wrappers.<AttSchedulingStaffPO>lambdaUpdate()
                                                        .eq(AttSchedulingStaffPO::getStaffId, schedulingStaff.getStaffId())
                                                        .eq(AttSchedulingStaffPO::getSchedulingMonth, yearMonth)
                                                        .set(AttSchedulingStaffPO::getGroupId, groupPO.getId())
                                                        .set(AttSchedulingStaffPO::getStaffName, schedulingStaff.getStaffName())
                                               );
            }
        });
        if (!insList.isEmpty()) {
            this.saveBatch(insList);
        }

    }
}
