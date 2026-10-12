package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockRulesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupStaffParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupClassResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupDetailResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupListResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupStaffResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.ServletUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttGroupQueryService {
    private final AttGroupMapper attGroupMapper;
    private final AttGroupStaffMapper attGroupStaffMapper;
    private final AttClockRulesMapper attClockRulesMapper;
    private final UserClient userClient;
    private final AttGroupRepository attGroupRepository;
    private final AttClassesMapper attClassesMapper;
    private final OrganizationClient organizationClient;
    private final DataDictionaryClient dataDictionaryClient;

    /**
     * 考勤组分页列表
     *
     * @param param 条件对象
     *
     * @return 分页列表
     */
    public Pagination<AttGroupPageResult> pageAttGroup(AttGroupPageParam param) {
        Page<AttGroupPO> page = attGroupMapper.selectPage(PagingUtils.convert(param), Wrappers.<AttGroupPO>lambdaQuery()
                                                                  .like(StringUtils.isNotBlank(param.getName()), AttGroupPO::getName, param.getName())
                                                                  .orderByDesc(AttGroupPO::getUpdateTime)
                                                         );
        List<AttGroupPageResult> resultList = page.getRecords().stream().map(groupPo -> {
            AttGroupPageResult groupListResult = new AttGroupPageResult();
            BeanUtils.copyProperties(groupPo, groupListResult);
            groupListResult.setStaffNumber(this.getGroupStaffNumber(groupPo));
            return groupListResult;
        }).collect(Collectors.toList());

        return new Pagination<>(page.getTotal(), page.getPages(), resultList);
    }

    /**
     * 考勤组详情
     *
     * @param id 主键
     *
     * @return 详情
     */
    public AttGroupDetailResult infoAttGroup(Long id) {
        if (id == null) {
            throw new ServiceException("请选择要查询的考勤组！");
        }
        AttGroupDetailResult detailResult = new AttGroupDetailResult();
        // 获取考勤组信息
        AttGroupPO groupPo = attGroupMapper.selectById(id);
        BeanUtils.copyProperties(groupPo, detailResult);
        Boolean phoneEncryption = this.getSensitiveConfig();
        // 获取考勤组人员
        if (AttGroupRangesEnum.PART.name().equals(groupPo.getRanges())) {
            List<AttGroupStaffPO> groupStaffPOList = attGroupStaffMapper.selectList(Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                                            .eq(AttGroupStaffPO::getGroupId, id));
            List<AttGroupStaffResult> groupStaffResultList = groupStaffPOList.stream().map(groupStaffPo -> {
                AttGroupStaffResult groupStaffResult = new AttGroupStaffResult();
                BeanUtils.copyProperties(groupStaffPo, groupStaffResult);
                if (ObjectUtils.isNotEmpty(groupStaffResult.getMobile()) && phoneEncryption) {
                    groupStaffResult.setMobile(JsonUtils.serialize(groupStaffResult.getMobile()));
                }
                return groupStaffResult;
            }).collect(Collectors.toList());
            detailResult.setGroupStaffList(groupStaffResultList);
        }
        detailResult.setGroupClassList(this.extClassList(groupPo.getGroupClassIds()));
        // 设置补卡规则
        detailResult.setCardRuleList(attGroupRepository.getGroupCardRuleList(groupPo.getCardRuleIds()));
        // 设置加班规则
        detailResult.setOvertimeRule(attGroupRepository.getGroupOvretimeRuleList(groupPo.getOvertimeRuleId()));
        // 设置假期规则
        detailResult.setHolidayRuleList(attGroupRepository.getGroupHolidayRuleList(groupPo.getHolidayRuleIds()));
        // 设置离岗规则
        detailResult.setLeavePostRule(attGroupRepository.getGroupLeavePostRule(groupPo.getLeavePostRuleId()));
        // 设置出差规则
        detailResult.setAwayBusinessRule(attGroupRepository.getGroupAwayBusinessRule(groupPo.getAwayBusinessRuleId()));

        return detailResult;
    }

    /**
     * 考勤组下拉列表(不分页)
     *
     * @param param 条件
     *
     * @return 列表
     */
    public List<AttGroupListResult> listAttGroup(AttGroupPageParam param) {
        return attGroupMapper.selectGroupDropDown(Wrappers.<AttGroupPO>lambdaQuery()
                                                          .like(StringUtils.isNotBlank(param.getName()), AttGroupPO::getName, param.getName())
                                                          .orderByDesc(AttGroupPO::getUpdateTime)
                                                 );
    }

    /**
     * 校验考勤组人员照片
     *
     * @param param 考勤组入参
     *
     * @return 提示信息
     */
    public String checkGroupStaffPhoto(AttGroupParam param) {
        String result = "";
        AttClockRulePO clockRulePO = attClockRulesMapper.selectById(param.getClockRuleId());
        if (AttGroupRangesEnum.PART.name().equals(param.getRanges())) {
            List<Long> staffList = param.getGroupStaffList().stream().map(AttGroupStaffParam::getId).toList();
            if (clockRulePO != null && Boolean.TRUE.equals(clockRulePO.getFaceRecognition())) {
                // 人员是否有考勤照片
                UserQuery userQuery = new UserQuery();
                userQuery.setEnabled(true);
                userQuery.setUserIds(new HashSet<>(staffList));
                List<UserDTO> userDTOList = userClient.list(userQuery);
                StringBuilder notPhotoBuff = new StringBuilder();
                for (UserDTO userDTO : userDTOList) {
                    if (userDTO.getPhotographKey() == null || userDTO.getPhotographKey() == 0) {
                        notPhotoBuff.append(userDTO.getName()).append(",");
                    }
                }
                if (!notPhotoBuff.isEmpty()) {
                    result = notPhotoBuff.append("还未录入考勤照片").toString();
                }
            }
        } else if (AttGroupRangesEnum.ALL.name().equals(param.getRanges()) &&
                   (clockRulePO != null && Boolean.TRUE.equals(clockRulePO.getFaceRecognition()))) {
            // 人员是否有考勤照片
            UserQuery userQuery = new UserQuery();
            userQuery.setEnabled(true);
            List<UserDTO> userDTOList = userClient.list(userQuery);
            StringBuilder notPhotoBuff = new StringBuilder();
            for (UserDTO userDTO : userDTOList) {
                if (userDTO.getPhotographKey() == null || userDTO.getPhotographKey() == 0) {
                    notPhotoBuff.append(userDTO.getName()).append(",");
                }
            }
            if (!notPhotoBuff.isEmpty()) {
                result = notPhotoBuff.append("还未录入考勤照片").toString();
            }

        }
        return result;
    }

    /**
     * 校验考勤组人员是否存在
     *
     * @param id             考勤组id
     * @param groupStaffList 考勤组人员
     */
    public void checkGroupStaffExist(Long id, List<AttGroupStaffParam> groupStaffList) {
        if (id == null || groupStaffList == null || groupStaffList.isEmpty()) {
            return;
        }
        // 校验考勤组人员是否已存在
        List<Long> staffList = groupStaffList.stream().map(AttGroupStaffParam::getStaffId).collect(Collectors.toList());
        List<AttGroupStaffPO> groupStaffPOList = attGroupStaffMapper.selectList(Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                                        .in(AttGroupStaffPO::getStaffId, staffList)
                                                                                        .ne(AttGroupStaffPO::getGroupId, id));
        if (!(groupStaffPOList == null || groupStaffPOList.isEmpty())) {
            StringBuilder stringBuffer = new StringBuilder();
            int length = groupStaffPOList.size();
            for (int i = 0; i < length; i++) {
                stringBuffer.append(groupStaffPOList.get(i).getName());
                if (i < length - 1) {
                    stringBuffer.append(",");
                }
            }
            throw new ServiceException(stringBuffer + "已在其他考勤组");
        }
    }

    /**
     * 获取考勤组人员数量
     *
     * @param groupPo 考勤组
     *
     * @return 人数
     */
    private long getGroupStaffNumber(AttGroupPO groupPo) {
        long staffNumber = 0;
        if (AttGroupRangesEnum.PART.name().equals(groupPo.getRanges())) {
            staffNumber = attGroupStaffMapper.selectCount(Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                  .eq(AttGroupStaffPO::getGroupId, groupPo.getId()));
        }
        return staffNumber;
    }

    public List<AttGroupClassResult> extClassList(String classIds) {
        List<AttGroupClassResult> resultList = new ArrayList<>();
        if (StringUtils.isBlank(classIds)) {
            return resultList;
        }
        String[] classIdArr = classIds.split(",");
        List<Long> classIdList = new ArrayList<>();
        for (String classId : classIdArr) {
            classIdList.add(Long.valueOf(classId));
        }
        if (classIdList.isEmpty()) {
            return resultList;
        }
        List<AttClassesPO> classesPOList = attClassesMapper.selectByIds(classIdList);
        if (classesPOList == null || classesPOList.isEmpty()) {
            return resultList;
        }
        List<Long> classDeptIdList = new ArrayList<>();
        for (AttClassesPO classesPO : classesPOList) {
            AttGroupClassResult classResult = new AttGroupClassResult();
            BeanUtils.copyProperties(classesPO, classResult);
            resultList.add(classResult);
            if (StringUtils.isNotBlank(classesPO.getDeptIds())) {
                String[] deptIdArr = classesPO.getDeptIds().split(",");
                for (String deptId : deptIdArr) {
                    classDeptIdList.add(Long.valueOf(deptId));
                }
            }
        }
        if (!classIdList.isEmpty()) {
            Map<Long, String> deptIdNameMap = new HashMap<>();
            List<OrganizationDTO> organizationDTOList = organizationClient.getOrganizationsByIds(classDeptIdList);
            if (organizationDTOList != null) {
                for (OrganizationDTO organizationDTO : organizationDTOList) {
                    deptIdNameMap.put(organizationDTO.getId(), organizationDTO.getName());
                }
            }
            resultList.forEach(result -> {
                StringBuilder deptNameBuffer = new StringBuilder();
                if (com.guhecloud.platform.base.util.StringUtils.isNotBlank(result.getDeptIds())) {
                    String deptIdsStr = result.getDeptIds();
                    String[] deptChars = deptIdsStr.split(",");
                    for (String deptChar : deptChars) {
                        String deptName = deptIdNameMap.get(Long.valueOf(deptChar));
                        deptNameBuffer.append(deptName == null ? "" : "、" + deptName);
                    }
                }
                String deptStr = deptNameBuffer.toString();
                result.setDeptNames(com.guhecloud.platform.base.util.StringUtils.isBlank(deptStr) ? "" : deptStr.substring(1));
            });

        }
        return resultList;
    }

    /**
     * 获取脱敏配置
     */
    private Boolean getSensitiveConfig() {
        boolean phoneEncryption = false;
        try {
            String encryption = ServletUtils.getCurrentRequest().getHeader("encryption");
            if (ObjectUtils.isNotEmpty(encryption) && !Boolean.parseBoolean(encryption)) {
                return phoneEncryption;
            }
            DataDictionaryaramParam dictionaryaramParam = new DataDictionaryaramParam();
            dictionaryaramParam.setItemCode("SERVICE_CONFIGURATION");
            dictionaryaramParam.setCode("SERVICE_CONFIGURATION");
            List<DataDictionaryDTO> dictionarys = dataDictionaryClient.getDataDictionaryByItem(dictionaryaramParam);
            if (ObjectUtils.isNotEmpty(dictionarys)) {
                String variable = dictionarys.getFirst().getVariable();
                Map<String, String> json = JsonUtils.deserialize(variable, new TypeReference<>() {});
                if (json != null) {
                    phoneEncryption = Boolean.parseBoolean(json.get("phone_encryption"));
                }
            }
        } catch (Exception e) {
            log.error("staff:获取脱敏配置失败", e);
        }
        return phoneEncryption;
    }
}
