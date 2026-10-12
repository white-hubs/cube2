package com.guhecloud.cube.base.flow.context.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.flow.context.dao.FormFlowBusinessMapper;
import com.guhecloud.cube.base.flow.context.enumeration.FormFlowStatusEnum;
import com.guhecloud.cube.base.flow.context.enumeration.FormFlowTypeEnum;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowDetailVO;
import com.guhecloud.cube.base.flow.context.po.FormFlowBusinessPO;
import com.guhecloud.cube.base.flow.context.po.FormFlowPO;
import com.guhecloud.cube.base.flow.flowable.definition.application.dto.ProcessDefinitionDTO;
import com.guhecloud.cube.base.flow.flowable.definition.application.querygateway.ProcessDefinitionQueryService;
import com.guhecloud.cube.base.flow.flowable.extension.node.NodeType;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.ChildNode;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.CommonEnumVO;
import vo.FormFlowBaseVO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/10/28
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormFlowBusinessQueryService extends ServiceImpl<FormFlowBusinessMapper, FormFlowBusinessPO> {

    private final FormFlowQueryService formFlowQueryService;
    private final FormFlowBusinessMapper formFlowBusinessMapper;
    private final ProcessDefinitionQueryService processDefinitionQueryService;

    /**
     * 查询关联业务名称
     *
     * @param businessType 业务类型
     *
     * @return
     */
    public String getByBusinessTypeName(String businessType) {
        List<FormFlowBusinessPO> businessPOS = this.lambdaQuery().eq(FormFlowBusinessPO::getBusinessType, businessType).list();
        if (CollectionUtils.isEmpty(businessPOS)) {
            return StringUtils.EMPTY_STRING;
        }
        FormFlowBusinessPO formFlowBusinessPO = businessPOS.get(0);
        return formFlowBusinessPO.getBusinessTypeName();
    }

    /**
     * ID查询
     *
     * @param businessType 业务类型
     *
     * @return
     */
    public FormFlowDetailVO getByBusinessType(@NonNull String businessType) {
        List<FormFlowBusinessPO> businessPOS = this.lambdaQuery().eq(FormFlowBusinessPO::getBusinessType, businessType).list();
        Assert.isNotFound(CollectionUtils.isEmpty(businessPOS), "未查询到相关流程表单配置");
        FormFlowBusinessPO formFlowBusinessPO = businessPOS.getFirst();
        FormFlowDetailVO formFlowDetailVO = formFlowQueryService.get(formFlowBusinessPO.getFormFlowId());
        formFlowDetailVO.setBusinessTypeName(formFlowBusinessPO.getBusinessTypeName());
        formFlowDetailVO.setAutoRemoveDuplication(
                formFlowDetailVO.getAutoRemoveDuplication() != null && formFlowDetailVO.getAutoRemoveDuplication());
        return formFlowDetailVO;
    }

    /**
     * 表单流程是否被业务关联
     *
     * @param formFlowId 表单流程ID
     *
     * @return
     */
    public boolean isReferenced(Long formFlowId) {
        Long count = formFlowBusinessMapper.selectCount(Wrappers.<FormFlowBusinessPO>lambdaQuery()
                                                                .eq(FormFlowBusinessPO::getFormFlowId, formFlowId));
        return count > 0;
    }

    public FormFlowBaseVO getFormFlowBaseInfoByBusinessType(String businessType) {
        FormFlowBusinessPO businessPO = this.lambdaQuery()
                .select(FormFlowBusinessPO::getFormFlowId, FormFlowBusinessPO::getBusinessTypeName)
                .eq(FormFlowBusinessPO::getBusinessType, businessType)
                .last("limit 1")
                .one();
        Assert.isNotFound(Objects.isNull(businessPO), "未查询到相关流程表单配置");
        FormFlowBaseVO formFlowBaseVO = formFlowQueryService.getFormFlowBaseInfoById(businessPO.getFormFlowId());
        formFlowBaseVO.setBusinessTypeName(businessPO.getBusinessTypeName());
        return formFlowBaseVO;
    }

    public List<CommonEnumVO> getBusinessType(FormFlowTypeEnum formType) {
        Set<Long> formflowIds = new HashSet<>();
        if (Objects.nonNull(formType)) {
            List<FormFlowPO> formFlowPOS = formFlowQueryService.lambdaQuery().select(FormFlowPO::getId)
                    .ne(FormFlowPO::getType, formType.name())
                    .list();
            if (CollectionUtils.isEmpty(formFlowPOS)) {
                return new ArrayList<>();
            }
            formflowIds = formFlowPOS.stream().map(FormFlowPO::getId).collect(Collectors.toSet());
        }
        List<FormFlowBusinessPO> formFlowBusinessPOS = this.lambdaQuery()
                .select(FormFlowBusinessPO::getBusinessType, FormFlowBusinessPO::getBusinessTypeName)
                .in(CollectionUtils.isNotEmpty(formflowIds), FormFlowBusinessPO::getFormFlowId, formflowIds)
                .list();
        if (CollectionUtils.isEmpty(formFlowBusinessPOS)) {
            return null;
        }
        return formFlowBusinessPOS.stream().map(businessPO -> CommonEnumVO.builder()
                        .code(businessPO.getBusinessType())
                        .name(businessPO.getBusinessTypeName())
                        .build())
                .collect(Collectors.toList());

    }

    public List<String> getFormNodeNameListByBusinessType(String businessType, String application) {
        List<FormFlowPO> formFlowPOS = listNodeFormFlows(businessType, application);
        if (CollectionUtils.isEmpty(formFlowPOS)) {
            return new ArrayList<>();
        }
        Set<String> nodeName = new HashSet<>();
        for (FormFlowPO formFlowPO : formFlowPOS) {
            collectFormFlowNodeNames(nodeName, formFlowPO);
        }
        return new ArrayList<>(nodeName);
    }

    private List<FormFlowPO> listNodeFormFlows(String businessType, String application) {
        if (StringUtils.isBlank(businessType) && StringUtils.isBlank(application)) {
            return new ArrayList<>();
        }
        Set<Long> formFlowIds = new HashSet<>();
        if (StringUtils.isNotBlank(businessType)) {
            List<FormFlowBusinessPO> businessPOS = this.lambdaQuery()
                    .select(FormFlowBusinessPO::getFormFlowId)
                    .eq(FormFlowBusinessPO::getBusinessType, businessType)
                    .list();
            if (CollectionUtils.isEmpty(businessPOS)) {
                return new ArrayList<>();
            }
            formFlowIds = businessPOS.stream().map(FormFlowBusinessPO::getFormFlowId).collect(Collectors.toSet());
        }
        return formFlowQueryService.lambdaQuery()
                .select(FormFlowPO::getId, FormFlowPO::getFlowKey)
                .in(CollectionUtils.isNotEmpty(formFlowIds), FormFlowPO::getId, formFlowIds)
                .eq(StringUtils.isNotBlank(application), FormFlowPO::getApplication, application)
                .ne(FormFlowPO::getStatus, FormFlowStatusEnum.DELETED)
                .list();
    }

    private void collectFormFlowNodeNames(Set<String> nodeName, FormFlowPO formFlowPO) {
        if (Objects.isNull(formFlowPO) || StringUtils.isBlank(formFlowPO.getFlowKey())) {
            return;
        }
        //获取审批节点名称
        ProcessDefinitionDTO processDefinitionDTO = processDefinitionQueryService.get(formFlowPO.getFlowKey());
        if (Objects.isNull(processDefinitionDTO) || StringUtils.isBlank(processDefinitionDTO.getProcessFormJson())) {
            return;
        }
        ChildNode childNode = JsonUtils.deserialize(processDefinitionDTO.getProcessFormJson(), new TypeReference<ChildNode>() {});
        this.getNodeName(nodeName, childNode.getChildren());
    }

    private void getNodeName(Set<String> nodeName, ChildNode children) {
        if (Objects.isNull(children)) {
            return;
        }
        if (NodeType.APPROVAL.name().equals(children.getType())) {
            nodeName.add(children.getName());
        }
        if (!CollectionUtils.isEmpty(children.getBranchs())) {
            for (ChildNode branch : children.getBranchs()) {
                getNodeName(nodeName, branch);
            }
        }
        if (Objects.nonNull(children.getChildren())) {
            getNodeName(nodeName, children.getChildren());
        }
    }
}
