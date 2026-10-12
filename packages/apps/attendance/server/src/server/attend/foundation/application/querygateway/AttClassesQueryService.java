package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.querygateway;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesRecordMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesRecordPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttClassesPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttClassesParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttClassesDetailResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttClassesListResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttClassesPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingRuleQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.valueobject.AttSchedulingClasses;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRuleClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRulePO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 班次查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttClassesQueryService {

    private final AttClassesMapper attClassesMapper;
    private final OrganizationClient organizationClient;
    private final AttGroupMapper attGroupMapper;
    private final AttSchedulingRuleClassesMapper attSchedulingRuleClassesMapper;
    private final AttSchedulingRuleQueryService attSchedulingRuleQueryService;
    private final AttSchedulingRuleMapper attSchedulingRuleMapper;
    private final AttClassesRecordMapper classesRecordMapper;

    /**
     * 班次分页列表
     *
     * @param param 分页条件
     *
     * @return 分页列表
     */
    public Pagination<AttClassesPageResult> pageAttClasses(AttClassesPageParam param) {
        param.validBusiness();
        List<Long> classIdList = new ArrayList<>();
        if (param.getGroupId() != null) {
            AttGroupPO groupPO = attGroupMapper.selectById(param.getGroupId());
            if (groupPO != null && StringUtils.isNotBlank(groupPO.getGroupClassIds())) {
                String[] classIdArr = groupPO.getGroupClassIds().split(",");
                for (String classId : classIdArr) {
                    classIdList.add(Long.valueOf(classId));
                }
            }
        }
        Page<AttClassesPO> page = attClassesMapper.selectPage(PagingUtils.convert(param), Wrappers.<AttClassesPO>lambdaQuery()
                                                                      .like(StringUtils.isNotBlank(param.getName()), AttClassesPO::getName, param.getName())
                                                                      .in(!classIdList.isEmpty(), AttClassesPO::getId, classIdList)
                                                                      .and(param.getDeptId() != null, i -> i.like(AttClassesPO::getDeptIds, param.getDeptId()).or()
                                                                              .eq(AttClassesPO::getDeptIds, "0"))
                                                                      .orderByDesc(AttClassesPO::getUpdateTime)
                                                             );

        List<AttClassesPageResult> list = this.extClassList(param, page.getRecords());

        return new Pagination<>(page.getTotal(), page.getPages(), list);
    }

    /**
     * 班次详情
     *
     * @param id 主键
     *
     * @return 详情
     */
    public AttClassesDetailResult infoAttClasses(Long id) {
        AttClassesPO po = attClassesMapper.selectById(id);
        Assert.isNotFound(po, "班次不存在");
        AttClassesDetailResult result = new AttClassesDetailResult();
        BeanUtils.copyProperties(po, result);
        return result;
    }

    /**
     * 班次列表，不分页
     *
     * @param param 条件
     *
     * @return 列表
     */
    public List<AttClassesListResult> listAttClasses(AttClassesPageParam param) {

        return attClassesMapper.selectClassDropDown(Wrappers.<AttClassesPO>lambdaQuery()
                                                            .like(StringUtils.isNotBlank(param.getName()), AttClassesPO::getName, param.getName())
                                                            .orderByDesc(AttClassesPO::getUpdateTime)
                                                   );
    }

    /**
     * 扩展班次信息
     *
     * @param param  条件
     * @param poList 原集合
     *
     * @return 处理后的集合
     */
    private List<AttClassesPageResult> extClassList(AttClassesPageParam param, List<AttClassesPO> poList) {
        List<Long> deptList = new ArrayList<>();
        List<AttClassesPageResult> list = poList.stream().map(po -> {
            if (Boolean.TRUE.equals(param.getDeptWhere()) && StringUtils.isNotBlank(po.getDeptIds())) {
                String deptIdsStr = po.getDeptIds();
                String[] deptChars = deptIdsStr.split(",");
                for (String deptChar : deptChars) {
                    deptList.add(Long.valueOf(deptChar));
                }
            }
            AttClassesPageResult result = new AttClassesPageResult();
            BeanUtils.copyProperties(po, result);
            return result;
        }).collect(Collectors.toList());

        // 需要部门信息
        if (!deptList.isEmpty()) {
            // 获取部门信息
            Map<Long, OrganizationDTO> deptDotMap = new HashMap<>();
            List<OrganizationDTO> deptDtoList = organizationClient.getOrganizationsByIds(deptList);
            if (!deptDtoList.isEmpty()) {
                deptDotMap = deptDtoList.stream().collect(Collectors.toMap(OrganizationDTO::getId, dept -> dept));
            }
            // 设置建议应用范围的部门名称
            Map<Long, OrganizationDTO> finalDeptDotMap = deptDotMap;
            list.forEach(classesResult -> {
                StringBuilder deptNameBuffer = new StringBuilder();
                if (StringUtils.isNotBlank(classesResult.getDeptIds())) {
                    String deptIdsStr = classesResult.getDeptIds();
                    String[] deptChars = deptIdsStr.split(",");
                    for (String deptChar : deptChars) {
                        OrganizationDTO organizationDTO = finalDeptDotMap.get(Long.valueOf(deptChar));
                        deptNameBuffer.append(organizationDTO == null ? "" : "、" + organizationDTO.getName());
                    }
                }
                String deptStr = deptNameBuffer.toString();
                classesResult.setDeptNames(StringUtils.isBlank(deptStr) ? "" : deptStr.substring(1));
            });
        }

        return list;
    }

    /**
     * 预校验班次有哪些考勤组使用
     *
     * @param id 班次id
     *
     * @return 使用的考勤组提示
     */
    public String checkClassUseGroupStr(Long id) {
        String result = "";
        // 1.在使用的考勤组
        List<AttSchedulingRuleClassesPO> ruleClassesPOList = attSchedulingRuleClassesMapper.selectList(Wrappers
                                                                                                               .<AttSchedulingRuleClassesPO>lambdaQuery()
                                                                                                               .eq(AttSchedulingRuleClassesPO::getId, id)
                                                                                                               .groupBy(AttSchedulingRuleClassesPO::getRuleId)
                                                                                                      );
        if (ruleClassesPOList == null || ruleClassesPOList.isEmpty()) {
            return result;
        }

        List<Long> ruleIdList = ruleClassesPOList.stream().map(AttSchedulingRuleClassesPO::getRuleId).collect(Collectors.toList());
        if (ruleIdList.isEmpty()) {
            return result;
        }
        List<AttGroupPO> groupPOList = attGroupMapper.selectList(Wrappers.<AttGroupPO>lambdaQuery()
                                                                         .in(AttGroupPO::getSchedulingRuleId, ruleIdList)
                                                                );
        if (groupPOList == null || groupPOList.isEmpty()) {
            return result;
        }

        StringBuilder useGroupBuffer = new StringBuilder("该班次目前正应用于");
        for (AttGroupPO groupPO : groupPOList) {
            useGroupBuffer.append(groupPO.getName()).append(",");
        }
        useGroupBuffer.append("考勤组的排班规则，删除后涉及今日之后的未来数据将被清空");

        return useGroupBuffer.toString();
    }

    /**
     * 校验班次修改后规则是否交叉
     *
     * @param param 修改后的班次信息
     *
     * @return 是否交叉（true交叉
     */
    public boolean checkUseRule(AttClassesParam param) {
        // 使用班次的规则
        List<AttSchedulingRuleClassesPO> useRuleList = attSchedulingRuleClassesMapper.selectList(Wrappers
                                                                                                         .<AttSchedulingRuleClassesPO>lambdaQuery()
                                                                                                         .eq(AttSchedulingRuleClassesPO::getId, param.getId())
                                                                                                );
        if (useRuleList == null || useRuleList.isEmpty()) {
            return false;
        }
        // 所有使用的规则id集合
        List<Long> allRuleIdList = useRuleList.stream().map(AttSchedulingRuleClassesPO::getRuleId).distinct().collect(Collectors.toList());
        // 所有规则班次集合
        List<AttSchedulingRuleClassesPO> allRuleClassPOList = attSchedulingRuleClassesMapper.selectList(Wrappers
                                                                                                                .<AttSchedulingRuleClassesPO>lambdaQuery()
                                                                                                                .in(AttSchedulingRuleClassesPO::getRuleId, allRuleIdList)
                                                                                                       );
        if (allRuleClassPOList == null || allRuleClassPOList.isEmpty()) {
            return false;
        }
        List<Long> allClassIdList = allRuleClassPOList.stream().map(AttSchedulingRuleClassesPO::getId).distinct()
                .collect(Collectors.toList());
        if (allClassIdList.isEmpty()) {
            return false;
        }
        List<AttClassesPO> allClassPOList = attClassesMapper.selectByIds(allClassIdList);
        if (allClassPOList == null || allClassPOList.isEmpty()) {
            return false;
        }
        // 所有班次的map
        Map<Long, AttClassesPO> allClassPOMap = allClassPOList.stream().collect(Collectors.toMap(AttClassesPO::getId, v -> v));
        // 根据规则分组的规则班次map
        Map<Long, List<AttSchedulingRuleClassesPO>> allRuleClassMap = allRuleClassPOList.stream()
                .collect(Collectors.groupingBy(AttSchedulingRuleClassesPO::getRuleId));
        // 遍历规则
        allRuleClassMap.forEach((ruleId, list) -> {
            List<AttSchedulingClasses> ruleClassList = new ArrayList<>();
            for (AttSchedulingRuleClassesPO ruleClassesPO : list) {
                AttSchedulingClasses classDto = new AttSchedulingClasses();
                AttClassesPO cla = null;
                if (Objects.equals(ruleClassesPO.getId(), param.getId())) {
                    cla = AttClassesPO.builder().build();
                    BeanUtils.copyProperties(param, cla);
                } else {
                    cla = allClassPOMap.get(ruleClassesPO.getId());
                    if (cla == null) {
                        continue;
                    }
                }
                classDto = attSchedulingRuleQueryService.stringClassToDateClass(Integer.parseInt(ruleClassesPO.getWeek()) + 1, cla);
                ruleClassList.add(classDto);
            }
            // 校验
            boolean bool = attSchedulingRuleQueryService.checkRuleClass(ruleClassList, true);
            if (bool) {
                AttSchedulingRulePO rulePO = attSchedulingRuleMapper.selectById(ruleId);
                throw new ServiceException("修改导致’" + rulePO.getName() + "‘中的班次交叉");
            }
        });
        return false;
    }

    public AttClassesRecordPO getClassesRecordById(Long classesId, Integer version) {
        AttClassesRecordPO classesversion = null;
        if (ObjectUtils.isNotEmpty(version)) {
            classesversion = classesRecordMapper.selectOne(new LambdaQueryWrapper<AttClassesRecordPO>()
                                                                   .eq(AttClassesRecordPO::getClassesId, classesId)
                                                                   .eq(ObjectUtils.isNotEmpty(version), AttClassesRecordPO::getVersion, version)
                                                                   .last("limit 1"));
        }
        if (ObjectUtils.isEmpty(classesversion)) {
            AttClassesPO classes = attClassesMapper.selectById(classesId);
            classesversion = ObjectUtils.copying(classes, AttClassesRecordPO.class);
            classesversion.setClassesId(classes.getId());
            classesversion.setVersion(1);
        }
        return classesversion;
    }
}
