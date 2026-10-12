package com.guhecloud.cube.base.flow.form.interfaces;

import com.guhecloud.cube.base.flow.form.commandgateway.FormDefinitionCommandService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.PublishFormDefinitionCommand;
import com.guhecloud.cube.base.flow.form.interfaces.param.FormDataParam;
import com.guhecloud.cube.base.flow.form.interfaces.vo.FormDefinitionVO;
import com.guhecloud.cube.base.flow.form.querygateway.FormDefinitionQueryService;
import com.guhecloud.cube.base.flow.form.querygateway.query.FormDefinitionQueryParam;
import com.guhecloud.cube.base.flow.form.resolver.FormResultDataResolver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 动态表单接口
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormDefinitionController {

    private final FormDefinitionCommandService formDefinitionCommandService;
    private final FormDefinitionQueryService queryService;
    private final FormResultDataResolver formResultDataResolver;

    /**
     * 查询表单
     *
     * @param queryParam
     *
     * @return
     */
    @GetMapping("/form-definitions")
    public List<FormDefinitionVO> get(FormDefinitionQueryParam queryParam) {
        return queryService.get(queryParam);
    }

    /**
     * 发布表单
     *
     * @param publishFormDefinitionCommand
     *
     * @return
     */
    @PostMapping("/form-definition/deploy")
    public Long publish(@Valid @RequestBody PublishFormDefinitionCommand publishFormDefinitionCommand) {
        return formDefinitionCommandService.publish(publishFormDefinitionCommand);
    }

    /**
     * 表单和业务字段解析
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/form-definition/parse")
    public String formParse(@RequestBody FormDataParam param) {
        return formResultDataResolver.parseByFormId2(param.getFormId(), param.getFormData());
    }

}
