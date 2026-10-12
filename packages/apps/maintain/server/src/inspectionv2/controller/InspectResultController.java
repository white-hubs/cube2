package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectResultCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectResultQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultOptionCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectResultVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * <p>
 * 巡检结果  实际为巡检结果项  冗余了名称 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectResultController {

    private final InspectResultCommandService resultCommandService;
    private final InspectResultQueryService resultQueryService;

    /**
     * ID查询
     */
    @GetMapping("/inspect-result/{id}")
    public InspectResultVO getById(@PathVariable Long id) {
        return resultQueryService.getById(id);
    }

    /**
     * 分页
     */
    @GetMapping("/inspect-result/page")
    public Pagination<InspectResultVO> getPage(InspectResultQueryParam param) {
        return resultQueryService.getPage(param);
    }

    /**
     * 列表
     */
    @GetMapping("/inspect-result/list")
    public List<InspectResultVO> getList(InspectResultQueryParam param) {
        return resultQueryService.getList(param);
    }

    /**
     * 选项列表
     */
    @GetMapping("/inspect-result/{id}/options")
    public List<InspectResultOptionCommandModel> getOptions(@PathVariable Long id) {
        return resultQueryService.getOptions(id);
    }

    /**
     * 新增
     */
    @PostMapping("/inspect-result")
    public void addInspectResult(@RequestBody @Valid InspectResultCommandModel commandModel) {
        resultCommandService.save(commandModel);
    }

    /**
     * 修改
     */
    @PutMapping("/inspect-result/{id}")
    public void updateInspectResult(@PathVariable Long id, @RequestBody @Validated InspectResultCommandModel commandModel) {
        resultCommandService.update(id, commandModel);
    }

    /**
     * 删除
     */
    @DeleteMapping("/inspect-result/{id}")
    public void deleteInspectResult(@PathVariable Long id) {
        resultCommandService.remove(id);
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/inspect-result/batch")
    public void deleteInspectResult(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") String ids) {
        //各模块相关批量删除接口，和前端约定了下，前端传id字符串例如 234454,45454  后台做下相应转换
        resultCommandService.removeBatch(ids);
    }
}

