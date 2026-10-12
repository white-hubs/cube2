package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectCronCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectCronQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectCronCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectCronQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectCronVO;
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

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * <p>
 * 时间表达式表 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectCronController {

    private final InspectCronCommandService cronCommandService;
    private final InspectCronQueryService queryService;

    /**
     * 详情
     */
    @GetMapping("/inspect-cron/{id}")
    public InspectCronVO getById(@PathVariable Long id) {
        return queryService.getVoById(id);
    }

    /**
     * 分页
     */
    @GetMapping("/inspect-cron/page")
    public Pagination<InspectCronVO> getPage(InspectCronQueryParam param) {
        return queryService.getPage(param);
    }

    /**
     * 列表
     */
    @GetMapping("/inspect-cron/list")
    public List<InspectCronVO> getList(InspectCronQueryParam param) {
        return queryService.getList(param);
    }

    /**
     * 新增
     */
    @PostMapping("/inspect-cron")
    public void save(@RequestBody @Validated InspectCronCommandModel param) {
        cronCommandService.save(param);
    }

    /**
     * 修改
     */
    @PutMapping("/inspect-cron/{id}")
    public void update(@PathVariable Long id, @RequestBody @Validated InspectCronCommandModel param) {
        cronCommandService.update(id, param);
    }

    /**
     * 删除
     */
    @DeleteMapping("/inspect-cron/{id}")
    public void remove(@PathVariable Long id) {
        cronCommandService.remove(id);
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/inspect-cron/batch")
    public void removeBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") String ids) {
        cronCommandService.removeBatch(ids);
    }

}

