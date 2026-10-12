package com.guhecloud.cube.base.flow.context.interfaces;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.base.flow.context.commandgateway.FormFlowCommandService;
import com.guhecloud.cube.base.flow.context.commandgateway.command.FormFlowCommand;
import com.guhecloud.cube.base.flow.context.enumeration.FormFlowStatusEnum;
import com.guhecloud.cube.base.flow.context.enumeration.FormFlowTypeEnum;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowDetailVO;
import com.guhecloud.cube.base.flow.context.querygateway.FormFlowQueryService;
import com.guhecloud.cube.base.flow.context.querygateway.query.FormFlowQueryParam;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 表单配置接口
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormFlowController {

    private final FormFlowCommandService formFlowCommandService;
    private final FormFlowQueryService formFlowQueryService;

    /**
     * 获取详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/form-flow/{id}")
    public FormFlowDetailVO get(@PathVariable("id") Long id) {
        return formFlowQueryService.get(id);
    }

    /**
     * 获取表单配置列表（全量）
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/form-flow/list")
    public List<FormFlowDetailVO> list(FormFlowQueryParam param) {
        return formFlowQueryService.getList(param);
    }

    /**
     * 获取表单配置列表（分页）
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/form-flow/page")
    public Pagination<FormFlowDetailVO> getPage(FormFlowQueryParam param) {
        param.setType(FormFlowTypeEnum.FLOW_FORM.name());
        return formFlowQueryService.getPage(param);
    }

    /**
     * 获取表单配置列表（分页）
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/form-flow/type/page")
    public Pagination<FormFlowDetailVO> getTypeListPage(FormFlowQueryParam param) {
        param.setTypes(FormFlowTypeEnum.FLOW_FORM.name() + "," + FormFlowTypeEnum.CUSTOM_FORM.name());
        return formFlowQueryService.getPage(param);
    }

    /**
     * 获取流程表单类型
     *
     * @return
     */
    @GetMapping("/form-flow/type/list")
    public List<Map<String, String>> getTypeList() {
        return FormFlowTypeEnum.getList();
    }

    /**
     * 获表单状态
     *
     * @return
     */
    @GetMapping("/form-flow/status/list")
    public List<Map<String, String>> getStatusList() {
        return FormFlowStatusEnum.getList();
    }

    /**
     * 获取表单支持应用
     *
     * @return
     */
    @GetMapping("/form-flow/application/list")
    public List<ApplicationDTO> getApplicationList() {
        return formFlowQueryService.getApplicationList();
    }

    /**
     * 流程引擎表单发布
     *
     * @param formFlowCommand
     *
     * @return
     */
    @PostMapping("/form-flows")
    public Long saveOrUpdate(@Valid @RequestBody FormFlowCommand formFlowCommand) {
        return formFlowCommandService.saveOrUpdate(formFlowCommand);
    }

    /**
     * 启用表单
     *
     * @param id
     * @param enable
     */
    @PutMapping("/form-flow/enable")
    public void enable(@RequestParam(value = "id") Long id, @RequestParam(value = "enable") Boolean enable) {
        formFlowCommandService.enable(id, enable);
    }

    /**
     * 删除表单
     *
     * @param id
     */
    @DeleteMapping("/form-flow/{id}")
    public void delete(@PathVariable("id") Long id) {
        formFlowCommandService.delete(id);
    }

    /**
     * 公开表单分享链接生成二维码
     *
     * @param id
     * @param qrCodeUrl
     *
     * @return
     */
    @GetMapping("/form-flow/qr-code")
    public FileVO getQrCode(@RequestParam(value = "id") Long id, @RequestParam(value = "qrCodeUrl") String qrCodeUrl,
                            @RequestParam(value = "businessType") String businessType
                           ) {
        return formFlowQueryService.getQrCode(id, qrCodeUrl, businessType);
    }
}
