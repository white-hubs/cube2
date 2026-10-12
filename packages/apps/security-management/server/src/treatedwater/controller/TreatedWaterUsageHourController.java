package com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.param.TreatedWaterUsageHourParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.param.TreatedWaterUsageHourQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.param.TreatedWaterUsageStatQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.service.TreatedWaterUsageHourService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.vo.TreatedWaterStatVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.vo.TreatedWaterTypeTrendStatVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.vo.TreatedWaterUsageHourVO;
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
 * @author Appet
 * @Description 净化水使用量-小时表
 * @date 2023-10-30 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TreatedWaterUsageHourController {
    private final TreatedWaterUsageHourService treatedWaterUsageHourService;

    /**
     * 功能描述： 净化水使用量-小时表添加
     */
    @PostMapping(value = "/treated-water-usage-hour")
    public String add(@Valid @RequestBody TreatedWaterUsageHourParam param) {
        return treatedWaterUsageHourService.add(param);
    }

    /**
     * 功能描述： 净化水使用量-小时表修改
     */
    @PutMapping(value = "/treated-water-usage-hour/{id}")
    public void update(@PathVariable String id, @Valid @RequestBody TreatedWaterUsageHourParam param) {
        treatedWaterUsageHourService.update(id, param);
    }

    /**
     * 功能描述： 净化水使用量-小时表删除
     */
    @DeleteMapping(value = "/treated-water-usage-hour/{id}")
    public void delete(@PathVariable String id) {
        treatedWaterUsageHourService.delete(id);
    }

    /**
     * 功能描述： 净化水使用量-小时表批量删除
     */
    @DeleteMapping(value = "/treated-water-usage-hour/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<String> ids) {
        treatedWaterUsageHourService.batchDelete(ids);
    }

    /**
     * 功能描述： 净化水使用量-小时表分页
     */
    @GetMapping(value = "/treated-water-usage-hour/page")
    public Pagination<TreatedWaterUsageHourVO> getPage(TreatedWaterUsageHourQueryParam param) {
        return treatedWaterUsageHourService.getPage(param);
    }

    /**
     * 功能描述： 净化水使用量-小时表详情
     */
    @GetMapping(value = "/treated-water-usage-hour/{id}")
    public TreatedWaterUsageHourVO getById(@PathVariable String id) {
        return treatedWaterUsageHourService.getById(id);
    }

    /**
     * 按类型统计用量
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/treated-water-usage-hour/usageStatByType")
    public List<TreatedWaterStatVO> getUsageStatByType(TreatedWaterUsageStatQueryParam param) {
        return treatedWaterUsageHourService.getUsageStatByType(param);
    }

    /**
     * 按类型、时间统计用量
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/treated-water-usage-hour/usageStatByTypeAndTime")
    public List<TreatedWaterTypeTrendStatVO> getUsageStatByTypeAndTime(TreatedWaterUsageStatQueryParam param) {
        return treatedWaterUsageHourService.getUsageStatByTypeAndTime(param);
    }

    /**
     * 按区域统计用量
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/treated-water-usage-hour/usageStatByArea")
    public List<TreatedWaterStatVO> getUsageStatByArea(TreatedWaterUsageStatQueryParam param) {
        return treatedWaterUsageHourService.getUsageStatByArea(param);
    }

}