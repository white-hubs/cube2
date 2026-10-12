package com.guhecloud.cube.base.flow.context.interfaces;

import com.guhecloud.cube.base.flow.context.commandgateway.CustomFormFlowService;
import com.guhecloud.cube.base.flow.context.commandgateway.command.FormFlowCommand;
import com.guhecloud.cube.base.flow.context.enumeration.FormFlowStatusEnum;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFieldVO;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowDetailVO;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowGroupCountVO;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowGroupCountTreeVO;
import com.guhecloud.cube.base.flow.context.querygateway.query.FormFlowGroupQueryParam;
import com.guhecloud.cube.base.flow.context.querygateway.query.FormFlowQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 自定义表单流程配置接口
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/custom-form-flow")
public class CustomFormFlowController {

    private final CustomFormFlowService customFormFlowService;

    /**
     * 获取详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/{id}")
    public FormFlowDetailVO get(@PathVariable("id") Long id) {
        return customFormFlowService.get(id);
    }

    /**
     * 获取表单配置列表（全量）
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/list")
    public List<FormFlowDetailVO> list(FormFlowQueryParam param) {
        return customFormFlowService.getList(param);
    }

    /**
     * 获取表单配置列表（分页）
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/page")
    public Pagination<FormFlowDetailVO> getPage(FormFlowQueryParam param) {
        return customFormFlowService.getPage(param);
    }

    /**
     * 获表单状态
     *
     * @return
     */
    @GetMapping("/status/list")
    public List<Map<String, String>> getStatusList() {
        return FormFlowStatusEnum.getList();
    }

    /**
     * 表单发布
     *
     * @param formFlowCommand
     *
     * @return
     */
    @PostMapping("save-or-update")
    public Long saveOrUpdate(@Valid @RequestBody FormFlowCommand formFlowCommand) {
        return customFormFlowService.saveOrUpdate(formFlowCommand);
    }

    /**
     * 启用表单
     *
     * @param id
     * @param enable
     */
    @PutMapping("/enable")
    public Boolean enable(@RequestParam(value = "id") Long id, @RequestParam(value = "enable") Boolean enable) {
        return customFormFlowService.enable(id, enable);
    }

    /**
     * 删除表单
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable("id") Long id) {
        return customFormFlowService.delete(id);
    }

    /**
     * 获取分组下的工单个数(全量)
     */
    @GetMapping("/group/count")
    public List<FormFlowGroupCountVO> getFormFlowGroupCount(FormFlowGroupQueryParam param) {
        return customFormFlowService.getFormFlowGroupCount(param);
    }

    /**
     * 获取分组下的工单个数(带权限配置)
     */
    @GetMapping("/group/permission/count")
    public List<FormFlowGroupCountVO> getFormFlowGroupCountPermission(FormFlowGroupQueryParam param) {
        return customFormFlowService.getFormFlowGroupCountPermission(param);
    }

    /**
     * 获取分组下的工单个数(树形)
     */
    @GetMapping("/group/tree/count")
    public List<FormFlowGroupCountTreeVO> getFormFlowGroupTreeCount(FormFlowGroupQueryParam param) {
        return customFormFlowService.getFormFlowGroupTreeCount(param);
    }

    /**
     * 获取分组下的工单个数(树形，带权限配置)
     */
    @GetMapping("/group/tree/permission/count")
    public List<FormFlowGroupCountTreeVO> getFormFlowGroupTreeCountPermission(FormFlowGroupQueryParam param) {
        return customFormFlowService.getFormFlowGroupTreeCountPermission(param);
    }

    /**
     * 获取自定义表单下的业务类型
     */
    @GetMapping("/business/type")
    public List<FormFieldVO> getBusinessType(@RequestParam(value = "application", required = false) String application) {
        return customFormFlowService.getBusinessType(application);
    }
}
