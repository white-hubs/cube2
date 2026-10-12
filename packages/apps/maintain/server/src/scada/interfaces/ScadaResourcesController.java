package com.guhecloud.cube.srv.gen.maintain.server.scada.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.scada.commandgateway.ScadaResourcesCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.scada.commandgateway.command.ScadaResourcesCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.scada.interfaces.vo.ScadaResourcesVO;
import com.guhecloud.cube.srv.gen.maintain.server.scada.querygateway.ScadaResourcesQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.scada.querygateway.query.ScadaResourcesQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.constraints.NotBlank;
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

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScadaResourcesController {

    private final ScadaResourcesQueryService queryService;
    private final ScadaResourcesCommandService commandService;

    /**
     * ID查询
     */
    @GetMapping("/scada-resources/{id}")
    public ScadaResourcesVO getById(@PathVariable Long id) {
        return queryService.getVOById(id);
    }

    /**
     * 分页
     */
    @GetMapping("/scada-resources/page")
    public Pagination<ScadaResourcesVO> getPage(ScadaResourcesQueryParam param) {
        return queryService.getPage(param);
    }

    /**
     * 列表
     */
    @GetMapping("/scada-resources/list")
    public List<ScadaResourcesVO> getList(ScadaResourcesQueryParam param) {
        return queryService.getList(param);
    }

    /**
     * 新增
     */
    @PostMapping("/scada-resources")
    public Long save(@RequestBody @Validated ScadaResourcesCommandModel commandModel) {
        return commandService.save(commandModel);
    }

    /**
     * 修改
     */
    @PutMapping("/scada-resources/{id}")
    public void updateInspectResult(@PathVariable("id") Long id, @RequestBody @Validated ScadaResourcesCommandModel commandModel) {
        commandService.update(id, commandModel);
    }

    /**
     * 删除
     */
    @DeleteMapping("/scada-resources/{id}")
    public void deleteInspectResult(@PathVariable Long id) {
        commandService.remove(id);
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/scada-resources/batch")
    public void deleteInspectResult(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") String ids) {
        //各模块相关批量删除接口，和前端约定了下，前端传id字符串例如 234454,45454  后台做下相应转换
        commandService.removeBatch(ids);
    }

}

