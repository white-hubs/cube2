package com.guhecloud.cube.base.flow.form.interfaces;

import com.guhecloud.cube.base.flow.form.commandgateway.FormInstanceCommandService;
import com.guhecloud.cube.base.flow.form.commandgateway.FormInstanceDataCommandService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.FormInstanceCommand;
import com.guhecloud.cube.base.flow.form.commandgateway.command.FormInstanceDataCommand;
import com.guhecloud.cube.base.flow.form.interfaces.vo.FormInstanceDetailVO;
import com.guhecloud.cube.base.flow.form.querygateway.FormInstanceQueryService;
import com.guhecloud.cube.base.flow.form.querygateway.query.FormInstanceQueryParam;
import com.guhecloud.cube.base.flow.form.resolver.DynamicDataResolver;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 动态表达正在运行的流程实例
 */

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/form/instance")
public class FormInstanceController {

    private final FormInstanceCommandService formInstanceCommandService;
    private final FormInstanceDataCommandService formInstanceDataCommandService;
    private final FormInstanceQueryService formInstanceQueryService;
    private final DynamicDataResolver dynamicDataResolver;

    @GetMapping("/{id}")
    public FormInstanceDetailVO get(@PathVariable("id") Long id) {
        return formInstanceQueryService.get(id);
    }

    @GetMapping("/{id}/definition")
    public Long getDefinitionId(@PathVariable("id") Long id) {
        return formInstanceQueryService.getDefinitionId(id);
    }

    @GetMapping("/list")
    public List<FormInstanceDetailVO> list(FormInstanceQueryParam param) {
        return formInstanceQueryService.getList(param);
    }

    @GetMapping("/page")
    public Pagination<FormInstanceDetailVO> getPage(FormInstanceQueryParam param) {
        return formInstanceQueryService.getPage(param);
    }

    @GetMapping("/business")
    public FormInstanceDetailVO getByBusiness(@RequestParam(value = "businessType") String businessType,
                                              @RequestParam(value = "businessKey") String businessKey
                                             ) {
        return formInstanceQueryService.getByBusiness(businessType, businessKey);
    }

    @PostMapping("/dynamic-data")
    public Object getDynamicData(@RequestBody FormInstanceQueryParam param) {
        return dynamicDataResolver.parse(param.getDynamicDataJson());
    }

    @PostMapping("/start")
    public Long start(@RequestBody FormInstanceCommand formInstanceCommand) {
        return formInstanceCommandService.start(formInstanceCommand);
    }

    @PostMapping("/data")
    public String saveOrUpdateData(@RequestBody FormInstanceDataCommand formInstanceDataCommand) {
        return formInstanceDataCommandService.saveOrUpdateData(formInstanceDataCommand);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        formInstanceCommandService.delete(id);
    }

    /**
     * 批量删除审批流程
     */
    @PostMapping("/form-flows/delete")
    public Boolean deleteFormFlow(@RequestBody Set<String> processInsIds) {
        return formInstanceCommandService.batchDeleteProcessInstance(processInsIds);
    }
}
