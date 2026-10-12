package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyCollectPeriod;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyCollectPeriodBatchParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyCollectPeriodParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyCollectPeriodQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyCollectPeriodService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyCollectPeriodVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
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
 * @author Appet
 * @Description 能耗采集时段定义
 * @date 2024-01-26 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyCollectPeriodController {
    private final EnergyCollectPeriodService energyCollectPeriodService;

    /**
     * 功能描述： 能耗采集时段定义添加
     */
    @PostMapping(value = "/energy-collect-period")
    public Long add(@Valid @RequestBody EnergyCollectPeriodParam param) {
        return energyCollectPeriodService.add(param);
    }

    /**
     * 功能描述： 能耗采集时段定义添加
     */
    @PostMapping(value = "/energy-collect-period/batch")
    public void addBatch(@Valid @RequestBody EnergyCollectPeriodBatchParam batchParam) {
        energyCollectPeriodService.addBatch(batchParam);
    }

    /**
     * 功能描述： 能耗采集时段定义修改
     */
    @PutMapping(value = "/energy-collect-period/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EnergyCollectPeriodParam param) {
        energyCollectPeriodService.update(id, param);
    }

    /**
     * 功能描述： 能耗采集时段定义删除
     */
    @DeleteMapping(value = "/energy-collect-period/{id}")
    public void delete(@PathVariable Long id) {
        energyCollectPeriodService.delete(id);
    }

    /**
     * 功能描述： 能耗采集时段定义批量删除
     */
    @DeleteMapping(value = "/energy-collect-period/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        energyCollectPeriodService.batchDelete(ids);
    }

    /**
     * 功能描述： 能耗采集时段定义分页
     */
    @GetMapping(value = "/energy-collect-period/page")
    public Pagination<EnergyCollectPeriodVO> getPage(EnergyCollectPeriodQueryParam param) {
        return energyCollectPeriodService.getPage(param);
    }

    /**
     * 功能描述： 能耗采集时段定义详情
     */
    @GetMapping(value = "/energy-collect-period/{id}")
    public EnergyCollectPeriodVO getById(@PathVariable Long id) {
        return energyCollectPeriodService.getById(id);
    }

    /**
     * 功能描述： 根据仪表类型查询峰平谷
     */
    @GetMapping(value = "/energy-collect-period/itype/{code}")
    public List<EnergyCollectPeriodVO> getByInstrumentType(@PathVariable String code) {
        List<EnergyCollectPeriod> list = energyCollectPeriodService.list(Wrappers.<EnergyCollectPeriod>lambdaQuery()
                                                                                 .eq(EnergyCollectPeriod::getInstrumentType, code)
                                                                                 .orderByAsc(EnergyCollectPeriod::getSort));
        return ObjectUtils.copying(list, EnergyCollectPeriodVO.class);
    }

}