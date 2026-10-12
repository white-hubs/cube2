package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyProjectItemParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyProjectItemQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyProjectItemService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectItemVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * @author lrj
 * @Description 分项及区域仪表绑定
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyProjectItemController {
    private final EnergyProjectItemService energyProjectItemService;

    /**
     * 功能描述： 分项及区域仪表绑定添加
     */
    @PostMapping(value = "/energy-project-item")
    public Long add(@Valid @RequestBody EnergyProjectItemParam param) {
        return energyProjectItemService.add(param);
    }

    /**
     * 功能描述： 分项及区域仪表绑定修改
     */
    @PutMapping(value = "/energy-project-item/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EnergyProjectItemParam param) {
        energyProjectItemService.update(id, param);
    }

    /**
     * 功能描述： 分项及区域仪表绑定删除
     */
    @DeleteMapping(value = "/energy-project-item/{id}")
    public void delete(@PathVariable Long id) {
        energyProjectItemService.delete(id);
    }

    /**
     * 功能描述： 分项及区域仪表绑定批量删除
     */
    @DeleteMapping(value = "/energy-project-item/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        energyProjectItemService.batchDelete(ids);
    }

    /**
     * 功能描述： 分项及区域仪表绑定分页
     */
    @GetMapping(value = "/energy-project-item/page")
    public Pagination<EnergyProjectItemVO> getPage(EnergyProjectItemQueryParam param) {
        return energyProjectItemService.getPage(param);
    }

    /**
     * 功能描述： 分项及区域仪表绑定详情
     */
    @GetMapping(value = "/energy-project-item/{id}")
    public EnergyProjectItemVO getById(@PathVariable Long id) {
        return energyProjectItemService.getById(id);
    }

}