package com.guhecloud.cube.base.flow.context.commandgateway.impl;

import com.guhecloud.cube.base.flow.context.commandgateway.CustomFormFlowService;
import com.guhecloud.cube.base.flow.context.commandgateway.FormFlowBusinessCommandService;
import com.guhecloud.cube.base.flow.context.commandgateway.FormFlowCommandService;
import com.guhecloud.cube.base.flow.context.commandgateway.FormFlowGroupService;
import com.guhecloud.cube.base.flow.context.commandgateway.FormPermissionService;
import com.guhecloud.cube.base.flow.context.commandgateway.command.FormFlowCommand;
import com.guhecloud.cube.base.flow.context.enumeration.FormFlowStatusEnum;
import com.guhecloud.cube.base.flow.context.enumeration.FormFlowTypeEnum;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFieldVO;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowDetailVO;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowGroupCountTreeVO;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowGroupCountVO;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormPermissionVO;
import com.guhecloud.cube.base.flow.context.po.FormFlowBusinessPO;
import com.guhecloud.cube.base.flow.context.po.FormFlowGroupPO;
import com.guhecloud.cube.base.flow.context.po.FormFlowPO;
import com.guhecloud.cube.base.flow.context.querygateway.FormFlowQueryService;
import com.guhecloud.cube.base.flow.context.querygateway.query.FormFlowGroupQueryParam;
import com.guhecloud.cube.base.flow.context.querygateway.query.FormFlowQueryParam;
import com.guhecloud.cube.biz.todo.client.TodoTaskClient;
import com.guhecloud.cube.biz.todo.client.param.TodoTagReferBusinessTypeModifyParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/4 18:27
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomFormFlowServiceImpl implements CustomFormFlowService {
    private final FormFlowCommandService formFlowCommandService;
    private final FormFlowQueryService formFlowQueryService;
    private final FormPermissionService formPermissionService;
    private final FormFlowGroupService formFlowGroupService;
    private final FormFlowBusinessCommandService formFlowBusinessCommandService;
    private final TodoTaskClient todoTaskClient;

    @Override
    public FormFlowDetailVO get(Long id) {
        FormFlowDetailVO formFlowDetailVO = formFlowQueryService.get(id);
        if (Objects.isNull(formFlowDetailVO)) {
            return null;
        }
        FormPermissionVO formPermissionVO = formPermissionService.getPermissionByFormFlowId(id);
        formFlowDetailVO.setPermissionType(formPermissionVO.getPermissionType());
        formFlowDetailVO.setPermissionList(formPermissionVO.getPermissionList());
        return formFlowDetailVO;
    }

    @Override
    public List<FormFlowDetailVO> getList(FormFlowQueryParam param) {
        param.setType(FormFlowTypeEnum.CUSTOM_FORM.name());
        return formFlowQueryService.getList(param);
    }

    @Override
    public Pagination<FormFlowDetailVO> getPage(FormFlowQueryParam param) {
        param.setType(FormFlowTypeEnum.CUSTOM_FORM.name());
        return formFlowQueryService.getPage(param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdate(FormFlowCommand formFlowCommand) {
        if (StringUtils.isBlank(formFlowCommand.getApplication())) {
            formFlowCommand.setApplication("APPROVAL_CENTER");
        }
        Long formFlowId = formFlowCommandService.saveOrUpdate(formFlowCommand);
        //查询是否已经生产业务编码
        FormFlowBusinessPO formFlowPO = formFlowBusinessCommandService.lambdaQuery()
                .eq(FormFlowBusinessPO::getFormFlowId, formFlowId)
                .last("limit 1").one();
        if (Objects.nonNull(formFlowPO)) {
            formFlowPO.setFormFlowId(formFlowId);
            formFlowPO.setBusinessTypeName(formFlowCommand.getShortName());
            formFlowBusinessCommandService.updateById(formFlowPO);
        } else {
            //新增待办业务类型
            TodoTagReferBusinessTypeModifyParam param = new TodoTagReferBusinessTypeModifyParam();
            param.setBusinessType(UUID.randomUUID().toString().replaceAll("-", ""));
            Long id = todoTaskClient.saveTodoTagReferBusinessType("APPROVE_TASK", param);
            FormFlowBusinessPO flowBusinessPO = new FormFlowBusinessPO();
            flowBusinessPO.setId(id);
            flowBusinessPO.setFormFlowId(formFlowId);
            flowBusinessPO.setBusinessType(param.getBusinessType());
            flowBusinessPO.setBusinessTypeName(formFlowCommand.getShortName());
            formFlowBusinessCommandService.save(flowBusinessPO);

        }
        formFlowCommandService.updateStatus(formFlowId, false);
        return formFlowId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean enable(Long id, Boolean enable) {
        return formFlowCommandService.enable(id, enable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        Boolean delete = formFlowCommandService.delete(id);
        FormFlowBusinessPO formFlowPO = formFlowBusinessCommandService.lambdaQuery()
                .eq(FormFlowBusinessPO::getFormFlowId, id)
                .last("limit 1").one();
        todoTaskClient.removeById(formFlowPO.getId());
        return delete;
    }

    @Override
    public List<FormFlowGroupCountVO> getFormFlowGroupCount(FormFlowGroupQueryParam param) {
        param = buildFormFlowGroupQueryParam(param);
        String application = getApplicationFilter(param);
        List<FormFlowGroupPO> flowGroupPOS = listOrderedFlowGroups(application);
        if (CollectionUtils.isEmpty(flowGroupPOS)) {
            return new ArrayList<>();
        }
        List<FormFlowGroupPO> leafGroupPOS = filterLeafGroups(flowGroupPOS);
        if (CollectionUtils.isEmpty(leafGroupPOS)) {
            return new ArrayList<>();
        }
        List<Long> groupIds = leafGroupPOS.stream().map(FormFlowGroupPO::getId).collect(Collectors.toList());
        List<FormFlowPO> formFlowPOS = listFormFlows(param, groupIds);
        if (CollectionUtils.isEmpty(formFlowPOS) && Boolean.TRUE.equals(param.getFilterEmpty())) {
            return new ArrayList<>();
        }
        Map<String, String> businessTypeMap = buildBusinessTypeMap(formFlowPOS);
        Map<Long, List<FormFlowPO>> formFlowMap = formFlowPOS.stream()
                .collect(Collectors.groupingBy(FormFlowPO::getFormGroupId));
        List<FormFlowGroupCountVO> result = new ArrayList<>();
        for (FormFlowGroupPO formFlowGroupPO : leafGroupPOS) {
            List<FormFieldVO> formFieldVOList = buildFormFieldList(formFlowMap.get(formFlowGroupPO.getId()), businessTypeMap);
            if (CollectionUtils.isNotEmpty(formFieldVOList) || !Boolean.TRUE.equals(param.getFilterEmpty())) {
                result.add(FormFlowGroupCountVO.builder()
                                   .formGroupId(formFlowGroupPO.getId())
                                   .formGroupName(formFlowGroupPO.getName())
                                   .formGroupCount(formFieldVOList.size())
                                   .formGroupFlowList(formFieldVOList)
                                   .build());
            }
        }
        return result;
    }

    @Override
    public List<FormFlowGroupCountVO> getFormFlowGroupCountPermission(FormFlowGroupQueryParam param) {
        //获取允许全部查看的表单
        List<Long> formFlowIds = formPermissionService.getCurrentUserPermissionList();
        if (CollectionUtils.isEmpty(formFlowIds)) {
            return new ArrayList<>();
        }
        param = buildFormFlowGroupQueryParam(param);
        param.setFormFlowIds(formFlowIds);
        return getFormFlowGroupCount(param);
    }

    @Override
    public List<FormFlowGroupCountTreeVO> getFormFlowGroupTreeCount(FormFlowGroupQueryParam param) {
        param = buildFormFlowGroupQueryParam(param);
        String application = getApplicationFilter(param);
        List<FormFlowGroupPO> flowGroupPOS = listOrderedFlowGroups();
        if (CollectionUtils.isEmpty(flowGroupPOS)) {
            return new ArrayList<>();
        }
        flowGroupPOS = filterGroupsByApplication(flowGroupPOS, application);
        if (CollectionUtils.isEmpty(flowGroupPOS)) {
            return new ArrayList<>();
        }
        List<Long> groupIds = flowGroupPOS.stream().map(FormFlowGroupPO::getId).collect(Collectors.toList());
        List<FormFlowPO> formFlowPOS = listFormFlows(param, groupIds);
        if (CollectionUtils.isEmpty(formFlowPOS) && Boolean.TRUE.equals(param.getFilterEmpty())) {
            return new ArrayList<>();
        }
        Map<String, String> businessTypeMap = buildBusinessTypeMap(formFlowPOS);
        Map<Long, List<FormFlowPO>> formFlowMap = formFlowPOS.stream()
                .collect(Collectors.groupingBy(FormFlowPO::getFormGroupId));
        return buildGroupCountTree(flowGroupPOS, formFlowMap, businessTypeMap, param.getFilterEmpty());
    }

    @Override
    public List<FormFlowGroupCountTreeVO> getFormFlowGroupTreeCountPermission(FormFlowGroupQueryParam param) {
        //获取允许全部查看的表单
        List<Long> formFlowIds = formPermissionService.getCurrentUserPermissionList();
        if (CollectionUtils.isEmpty(formFlowIds)) {
            return new ArrayList<>();
        }
        param = buildFormFlowGroupQueryParam(param);
        param.setFormFlowIds(formFlowIds);
        return getFormFlowGroupTreeCount(param);
    }

    private FormFlowGroupQueryParam buildFormFlowGroupQueryParam(FormFlowGroupQueryParam param) {
        return Objects.isNull(param) ? new FormFlowGroupQueryParam() : param;
    }

    private String getApplicationFilter(FormFlowGroupQueryParam param) {
        return param.getApplication();
    }

    private List<FormFlowGroupPO> listOrderedFlowGroups() {
        return listOrderedFlowGroups(null);
    }

    private List<FormFlowGroupPO> listOrderedFlowGroups(String application) {
        return formFlowGroupService.lambdaQuery()
                .eq(StringUtils.isNotBlank(application), FormFlowGroupPO::getApplication, application)
                .orderByAsc(FormFlowGroupPO::getLevel)
                .orderByAsc(FormFlowGroupPO::getCreateTime)
                .list();
    }

    private List<FormFlowGroupPO> filterGroupsByApplication(List<FormFlowGroupPO> flowGroupPOS, String application) {
        if (StringUtils.isBlank(application)) {
            return flowGroupPOS;
        }
        Map<Long, FormFlowGroupPO> groupMap = flowGroupPOS.stream()
                .collect(Collectors.toMap(FormFlowGroupPO::getId, group -> group, (k1, k2) -> k1));
        Set<Long> includeIds = new HashSet<>();
        for (FormFlowGroupPO group : flowGroupPOS) {
            if (Objects.equals(group.getApplication(), application)) {
                includeIds.add(group.getId());
                Long parentId = group.getParentId();
                while (Objects.nonNull(parentId) && groupMap.containsKey(parentId)) {
                    includeIds.add(parentId);
                    parentId = groupMap.get(parentId).getParentId();
                }
            }
        }
        return flowGroupPOS.stream()
                .filter(group -> includeIds.contains(group.getId()))
                .collect(Collectors.toList());
    }

    private List<FormFlowGroupPO> filterLeafGroups(List<FormFlowGroupPO> flowGroupPOS) {
        Set<Long> parentIds = flowGroupPOS.stream()
                .map(FormFlowGroupPO::getParentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        return flowGroupPOS.stream()
                .filter(group -> !parentIds.contains(group.getId()))
                .collect(Collectors.toList());
    }

    private List<FormFlowPO> listFormFlows(FormFlowGroupQueryParam param, List<Long> groupIds) {
        if (CollectionUtils.isEmpty(groupIds)) {
            return new ArrayList<>();
        }
        return Optional.ofNullable(formFlowCommandService.lambdaQuery()
                                           .eq(FormFlowPO::getType, FormFlowTypeEnum.CUSTOM_FORM.name())
                                           .eq(StringUtils.isNotBlank(getApplicationFilter(param)), FormFlowPO::getApplication, getApplicationFilter(param))
                                           .ne(FormFlowPO::getStatus, FormFlowStatusEnum.DELETED)
                                           .in(FormFlowPO::getFormGroupId, groupIds)
                                           .in(CollectionUtils.isNotEmpty(param.getFormFlowIds()), FormFlowPO::getId, param.getFormFlowIds())
                                           .like(StringUtils.isNotBlank(param.getKeyword()), FormFlowPO::getName, param.getKeyword())
                                           .list()).orElse(new ArrayList<>());
    }

    private Map<String, String> buildBusinessTypeMap(List<FormFlowPO> formFlowPOS) {
        Map<String, String> businessTypeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(formFlowPOS)) {
            Set<Long> formFlowIds = formFlowPOS.stream().map(FormFlowPO::getId).collect(Collectors.toSet());

            List<FormFlowBusinessPO> formFlowBusinessPO = Optional.ofNullable(formFlowBusinessCommandService
                                                                                      .lambdaQuery()
                                                                                      .in(FormFlowBusinessPO::getFormFlowId, formFlowIds)
                                                                                      .list()).orElse(new ArrayList<>());
            businessTypeMap = formFlowBusinessPO.stream()
                    .collect(Collectors.toMap(t -> t.getFormFlowId().toString(), FormFlowBusinessPO::getBusinessType, (k1, k2) -> k1));
        }
        return businessTypeMap;
    }

    private List<FormFlowGroupCountTreeVO> buildGroupCountTree(List<FormFlowGroupPO> flowGroupPOS, Map<Long, List<FormFlowPO>> formFlowMap,
                                                               Map<String, String> businessTypeMap, Boolean filterEmpty
                                                              ) {
        Map<Long, FormFlowGroupCountTreeVO> groupNodeMap = new LinkedHashMap<>();
        for (FormFlowGroupPO formFlowGroupPO : flowGroupPOS) {
            List<FormFlowPO> formFlowPOList = formFlowMap.get(formFlowGroupPO.getId());
            List<FormFieldVO> formFieldVOList = buildFormFieldList(formFlowPOList, businessTypeMap);
            groupNodeMap.put(formFlowGroupPO.getId(), FormFlowGroupCountTreeVO.builder()
                    .formGroupId(formFlowGroupPO.getId())
                    .formGroupName(formFlowGroupPO.getName())
                    .formGroupCount(formFieldVOList.size())
                    .formGroupFlowList(formFieldVOList)
                    .parentId(formFlowGroupPO.getParentId())
                    .level(formFlowGroupPO.getLevel())
                    .children(new ArrayList<>())
                    .build());
        }
        List<FormFlowGroupCountTreeVO> result = new ArrayList<>();
        for (FormFlowGroupPO formFlowGroupPO : flowGroupPOS) {
            FormFlowGroupCountTreeVO groupNode = groupNodeMap.get(formFlowGroupPO.getId());
            FormFlowGroupCountTreeVO parentNode = groupNodeMap.get(formFlowGroupPO.getParentId());
            if (Objects.nonNull(parentNode)) {
                parentNode.getChildren().add(groupNode);
            } else {
                result.add(groupNode);
            }
        }
        result.forEach(this::refreshGroupCount);
        if (Boolean.TRUE.equals(filterEmpty)) {
            result = filterEmptyGroup(result);
        }
        return result;
    }

    private List<FormFieldVO> buildFormFieldList(List<FormFlowPO> formFlowPOList, Map<String, String> businessTypeMap) {
        if (CollectionUtils.isEmpty(formFlowPOList)) {
            return new ArrayList<>();
        }
        List<FormFieldVO> formFieldVOList = ObjectUtils.copying(formFlowPOList, FormFieldVO.class);
        for (FormFieldVO formFieldVO : formFieldVOList) {
            formFieldVO.setBusinessType(businessTypeMap.get(formFieldVO.getId()));
        }
        return formFieldVOList;
    }

    private int refreshGroupCount(FormFlowGroupCountTreeVO groupCountTreeVO) {
        int count = CollectionUtils.isEmpty(groupCountTreeVO.getFormGroupFlowList()) ? 0 : groupCountTreeVO.getFormGroupFlowList().size();
        if (CollectionUtils.isNotEmpty(groupCountTreeVO.getChildren())) {
            for (FormFlowGroupCountTreeVO child : groupCountTreeVO.getChildren()) {
                count += refreshGroupCount(child);
            }
        }
        groupCountTreeVO.setFormGroupCount(count);
        return count;
    }

    private List<FormFlowGroupCountTreeVO> filterEmptyGroup(List<FormFlowGroupCountTreeVO> groupCountTreeVOS) {
        if (CollectionUtils.isEmpty(groupCountTreeVOS)) {
            return new ArrayList<>();
        }
        List<FormFlowGroupCountTreeVO> result = new ArrayList<>();
        for (FormFlowGroupCountTreeVO groupCountTreeVO : groupCountTreeVOS) {
            groupCountTreeVO.setChildren(filterEmptyGroup(groupCountTreeVO.getChildren()));
            if (Objects.nonNull(groupCountTreeVO.getFormGroupCount()) && groupCountTreeVO.getFormGroupCount() > 0) {
                result.add(groupCountTreeVO);
            }
        }
        return result;
    }

    @Override
    public List<FormFieldVO> getBusinessType(String application) {
        List<FormFlowPO> formFlowPOS = formFlowQueryService.lambdaQuery()
                .ne(FormFlowPO::getStatus, FormFlowStatusEnum.DELETED)
                .eq(FormFlowPO::getType, FormFlowTypeEnum.CUSTOM_FORM.name())
                .eq(StringUtils.isNotBlank(application), FormFlowPO::getApplication, application)
                .select(FormFlowPO::getId).list();
        if (CollectionUtils.isEmpty(formFlowPOS)) {
            return new ArrayList<>();
        }
        Set<Long> formFlowIds = formFlowPOS.stream().map(FormFlowPO::getId).collect(Collectors.toSet());
        List<FormFlowBusinessPO> formFlowBusinessPO = formFlowBusinessCommandService
                .lambdaQuery().in(FormFlowBusinessPO::getFormFlowId, formFlowIds)
                .list();
        if (CollectionUtils.isEmpty(formFlowBusinessPO)) {
            return new ArrayList<>();
        }
        return formFlowBusinessPO.stream().map(t -> FormFieldVO.builder()
                .id(t.getFormFlowId().toString())
                .businessType(t.getBusinessType())
                .businessTypeName(t.getBusinessTypeName())
                .build()).collect(Collectors.toList());
    }
}
