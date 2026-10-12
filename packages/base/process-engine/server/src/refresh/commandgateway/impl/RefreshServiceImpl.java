package com.guhecloud.cube.base.flow.refresh.commandgateway.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.flow.context.commandgateway.FormFlowCommandService;
import com.guhecloud.cube.base.flow.context.commandgateway.command.FormFlowCommand;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowDetailVO;
import com.guhecloud.cube.base.flow.context.querygateway.FormFlowQueryService;
import com.guhecloud.cube.base.flow.context.querygateway.query.FormFlowQueryParam;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.ChildNode;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.OperateAlias;
import com.guhecloud.cube.base.flow.refresh.commandgateway.RefreshService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RefreshServiceImpl implements RefreshService {

    private final FormFlowCommandService formFlowCommandService;
    private final FormFlowQueryService formFlowQueryService;

    @Override
    @Transactional
    public void refreshProcdef() {
        //1.获取所有的审批示例
        FormFlowQueryParam param = new FormFlowQueryParam();
        param.setPageNumber(1);
        param.setPageSize(Integer.MAX_VALUE);
        List<FormFlowDetailVO> list = formFlowQueryService.getDetailList(param);
        //2.更新所有的审批示例
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(formFlowCommand -> {
                FormFlowCommand saveCmd = new FormFlowCommand();
                ObjectUtils.copying(formFlowCommand, saveCmd);
                ChildNode childNode = JsonUtils.deserialize(formFlowCommand.getFlowJson(), new TypeReference<ChildNode>() {});
                updateAlisaName(childNode);
                saveCmd.setFlowJson(JsonUtils.serialize(childNode));
                //与前端入参保持一致，将状态置空
                saveCmd.setStatus(null);
                formFlowCommandService.saveOrUpdate(saveCmd);
            });
        }
    }

    /**
     * export const DEFAULT_ROOT_OPERATE_ALIAS = [
     * { code: 'ADD', name: '发起申请', alias: '', perm: 'S' },
     * { code: 'REPEAT', name: '再次发起', alias: '', perm: 'S' },
     * { code: 'UNDO', name: '撤销', alias: '', perm: 'S' },
     * ]
     * export const DEFAULT_APPROVAL_OPERATE_ALIAS = [
     * { code: 'PASS', name: '同意', alias: '', perm: 'S' },
     * { code: 'BACK', name: '退回', alias: '', perm: 'S' },
     * { code: 'TRANSFER', name: '转交', alias: '', perm: 'S' },
     * { code: 'REJECT', name: '驳回', alias: '', perm: 'S' },
     * { code: 'WITHDRAW', name: '撤回', alias: '', perm: 'S' },
     * { code: 'COMMENT', name: '评论', alias: '', perm: 'H' },
     * ]
     *
     * @param children
     */
    private void updateAlisaName(ChildNode children) {
        if (Objects.isNull(children) || Objects.isNull(children.getProps())) {
            return;
        }
        List<OperateAlias> operateAlias = children.getProps().getOperateAlias();
        if (CollectionUtils.isEmpty(operateAlias)) {
            operateAlias = new ArrayList<>();
            if ("ROOT".equals(children.getType())) {
                operateAlias.add(OperateAlias.getReStartAlias());
                operateAlias.add(OperateAlias.getUndoAlias());
                OperateAlias alias = new OperateAlias();
                alias.setCode("ADD");
                alias.setName("发起申请");
                alias.setAlias("");
                alias.setPerm("S");
                operateAlias.add(alias);
            } else {
                operateAlias.addAll(OperateAlias.getActiveOperateAlias());
            }
        } else {
            if ("ROOT".equals(children.getType())) {
                Optional<OperateAlias> repeatOptional = operateAlias.stream().filter(t -> "REPEAT".equals(t.getCode()))
                        .findFirst();
                if (!repeatOptional.isPresent()) {
                    operateAlias.add(OperateAlias.getReStartAlias());
                }
                Optional<OperateAlias> undoOptional = operateAlias.stream().filter(t -> "UNDO".equals(t.getCode()))
                        .findFirst();
                if (!undoOptional.isPresent()) {
                    operateAlias.add(OperateAlias.getUndoAlias());
                }
            } else {
                operateAlias = operateAlias.stream().filter(t -> !OperateAlias.getStartAlias().contains(t.getCode()))
                        .collect(Collectors.toList());
            }
        }
        children.getProps().setOperateAlias(operateAlias);

        if (!CollectionUtils.isEmpty(children.getBranchs())) {
            for (ChildNode branch : children.getBranchs()) {
                updateAlisaName(branch);
            }
        }
        if (Objects.nonNull(children.getChildren())) {
            updateAlisaName(children.getChildren());
        }
    }
}
