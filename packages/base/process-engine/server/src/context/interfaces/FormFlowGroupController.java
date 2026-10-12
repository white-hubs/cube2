package com.guhecloud.cube.base.flow.context.interfaces;

import com.guhecloud.cube.base.flow.context.commandgateway.FormFlowGroupService;
import com.guhecloud.cube.base.flow.context.commandgateway.command.FormFlowGroupCommand;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFieldVO;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowGroupTreeVO;
import com.guhecloud.cube.base.flow.context.po.FormFlowGroupPO;
import com.guhecloud.platform.base.util.ObjectUtils;
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

/**
 *
 * 流程表单分组接口
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/form-flow-group")
public class FormFlowGroupController {
    private final FormFlowGroupService formFlowGroupService;

    /**
     * 新增或更新
     *
     * @param command
     */
    @PostMapping("/save-or-update")
    public Boolean saveOrUpdate(@RequestBody FormFlowGroupCommand command) {
        return formFlowGroupService.saveOrUpdate(command);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Boolean deleted(@PathVariable Long id) {
        return formFlowGroupService.deletedById(id);
    }

    /**
     * 表单流程分组-列表(最末级)
     *
     * @return
     */
    @GetMapping("/list")
    public List<FormFieldVO> getConferenceTypeVos(@RequestParam(required = false) String keyword,
                                                  @RequestParam(value = "application", required = false) String application) {
        List<FormFlowGroupPO> list = formFlowGroupService.listLeafGroups(keyword, application);
        return ObjectUtils.copying(list, FormFieldVO.class);
    }

    /**
     * 表单流程分组-树
     *
     * @return
     */
    @GetMapping("/tree")
    public List<FormFlowGroupTreeVO> tree(@RequestParam(required = false) String keyword,
                                          @RequestParam(value = "application", required = false) String application) {
        return formFlowGroupService.getTree(keyword, application);
    }
}
