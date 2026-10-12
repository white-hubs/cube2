package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectItemTemplateCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectItemTemplateQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectItemBatchCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectItemCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectItemTemplateQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectItemTemplateVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 巡检标准 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectItemTemplateController {

    private final InspectItemTemplateCommandService commandService;
    private final InspectItemTemplateQueryService queryService;

    /**
     * 添加巡检项
     */
    @PostMapping("/inspect-item-template")
    public void save(@Valid @RequestBody InspectItemBatchCommandModel command) {
        commandService.addInspectItem(command);
    }

    /**
     * 修改巡检项
     */
    @PutMapping("/inspect-item-template/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody InspectItemCommandModel command) {
        commandService.updateInspectItem(id, command);
    }

    /**
     * 修改巡检项（stand）
     */
    @PutMapping("/inspect-item-template/standard/{id}")
    public void updateStandard(@PathVariable Long id, @Valid @RequestBody InspectItemBatchCommandModel command) {
        Assert.isParameterError(CollectionUtils.isEmpty(command.getItems()), "巡检项为空");
        command.getItems().forEach(t -> {
            t.setAlias(command.getAlias());
            t.setReferType(command.getReferType());
            t.setIsResultItem(command.getIsResultItem());
        });
        commandService.updateInspectItem(id, command.getItems().get(0));
    }

    /**
     * 删除巡检项
     */
    @DeleteMapping("/inspect-item-template/{id}")
    public void delete(@PathVariable Long id) {
        commandService.deleteInspectItem(id);
    }

    /**
     * 分页查询
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/inspect-item-template/page")
    public Pagination<InspectItemTemplateVO> page(InspectItemTemplateQueryParam param) {
        return queryService.getPage(param);
    }

    @GetMapping("/inspect-item-template/{id}/detail")
    public InspectItemTemplateVO getDetailById(@PathVariable Long id) {
        return queryService.getInspectItemDetail(id);
    }

}

