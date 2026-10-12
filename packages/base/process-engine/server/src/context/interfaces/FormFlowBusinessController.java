package com.guhecloud.cube.base.flow.context.interfaces;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.context.commandgateway.FormFlowBusinessCommandService;
import com.guhecloud.cube.base.flow.context.commandgateway.command.FormFlowBusinessCommand;
import com.guhecloud.cube.base.flow.context.enumeration.FormFlowTypeEnum;
import com.guhecloud.cube.base.flow.context.enumeration.SubmitCallTypeEnum;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowDetailExtendVO;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowDetailVO;
import com.guhecloud.cube.base.flow.context.po.FormFlowBusinessPO;
import com.guhecloud.cube.base.flow.context.querygateway.FormFlowBusinessQueryService;
import com.guhecloud.cube.base.flow.form.po.FormControlBusinessPO;
import com.guhecloud.cube.base.flow.form.querygateway.query.FormControlBusinessQueryService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vo.CommonEnumVO;
import vo.FormBizFieldMapVO;
import vo.FormFlowBaseVO;

import java.util.Collections;
import java.util.List;

/**
 * 表单流程业务关联接口
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormFlowBusinessController {

    private final FormFlowBusinessQueryService formFlowBusinessQueryService;
    private final FormFlowBusinessCommandService businessCommandService;
    private final FormControlBusinessQueryService formControlBusinessQueryService;

    /**
     * 根据业务类型获取表单流程明细
     *
     * @param businessType
     *
     * @return
     */
    @GetMapping("/form-flow-business/type")
    public FormFlowDetailExtendVO getByBusinessType(String businessType) {
        FormFlowDetailVO formFlowDetailVO = formFlowBusinessQueryService.getByBusinessType(businessType);
        //查询字段映射
        List<FormControlBusinessPO> formControlBusinessPOS = formControlBusinessQueryService.lambdaQuery()
                .eq(FormControlBusinessPO::getFormDefinitionId, formFlowDetailVO.getFormDefinitionId())
                .list();
        List<FormBizFieldMapVO> formBizFieldMapVOS = ObjectUtils.copying(formControlBusinessPOS, FormBizFieldMapVO.class);
        FormFlowDetailExtendVO formFlowDetailExtendVO = ObjectUtils.copying(formFlowDetailVO, FormFlowDetailExtendVO.class);
        formFlowDetailExtendVO.setFormBizFieldMapList(formBizFieldMapVOS);
        formFlowDetailExtendVO.setCallType(SubmitCallTypeEnum.INTERNAL.name());
        return formFlowDetailExtendVO;
    }

    /**
     * 通过业务类型 返回流程ID
     *
     * @param businessType
     *
     * @return
     */
    @GetMapping("/form-flow-business/{businessType}/flow")
    public Long getFlowByBusinessType(@PathVariable String businessType) {
        List<FormFlowBusinessPO> list = formFlowBusinessQueryService.list(Wrappers.<FormFlowBusinessPO>lambdaQuery()
                                                                                  .eq(FormFlowBusinessPO::getBusinessType, businessType));
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0).getFormFlowId();
        }
        return null;
    }

    /**
     * 保存或更新表单流程业务关联
     *
     * @param formFlowCommand
     */
    @PostMapping("/form-flow-business")
    public void saveOrUpdate(@Valid @RequestBody FormFlowBusinessCommand formFlowCommand) {
        businessCommandService.saveOrUpdate(formFlowCommand);
    }

    /**
     * 通过表单定义ID，查询绑定关系
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/form-business/bind/{id}")
    public List<FormBizFieldMapVO> get(@PathVariable("id") Long id) {
        List<FormControlBusinessPO> list = formControlBusinessQueryService.list(Wrappers.<FormControlBusinessPO>lambdaQuery()
                                                                                        .eq(FormControlBusinessPO::getFormDefinitionId, id));
        if (CollectionUtils.isNotEmpty(list)) {
            return ObjectUtils.copying(list, FormBizFieldMapVO.class);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 获取当前绑定的业务表单类型
     *
     * @return
     */
    @GetMapping("/form-business/type/list")
    public List<CommonEnumVO> getBusinessType(@RequestParam(value = "formType", required = false) FormFlowTypeEnum formType) {
        return formFlowBusinessQueryService.getBusinessType(formType);
    }

    /**
     * 根据业务类型获取表单信息
     *
     * @param businessType
     *
     * @return
     */
    @GetMapping("/form-flow-business/base/type")
    public FormFlowBaseVO getFormFlowBaseInfoByBusinessType(@RequestParam(value = "businessType") String businessType) {
        return formFlowBusinessQueryService.getFormFlowBaseInfoByBusinessType(businessType);
    }

    /**
     * 根据业务类型获取当前表单流程节点名称
     */
    @GetMapping("/form-flow-business/node/name")
    public List<String> getFormNodeNameListByBusinessType(@RequestParam(value = "businessType", required = false) String businessType,
                                                          @RequestParam(value = "application", required = false) String application) {
        return formFlowBusinessQueryService.getFormNodeNameListByBusinessType(businessType, application);
    }
}
