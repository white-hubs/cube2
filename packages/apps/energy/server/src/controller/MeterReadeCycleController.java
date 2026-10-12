package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterReadeCycle;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterReadeCycleParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.MeterReadeCycleQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.MeterDeviceService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.MeterReadeCycleService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterReadeCycleVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.servlet.http.HttpServletRequest;
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

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * @author lrj
 * @Description 抄表周期
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeterReadeCycleController {
    private final MeterReadeCycleService meterReadeCycleService;

    private final MeterDeviceService meterDeviceService;

    private final HttpServletRequest httpServletRequest;

    /**
     * 插入或更新抄表测略
     *
     * @param param
     */
    @PostMapping(value = "/meter-reade-cycle/task")
    public void addOrUpdate(@Valid @RequestBody MeterReadeCycleParam param) {
        meterReadeCycleService.addOrUpdate(param);
    }

    /**
     * 功能描述： 抄表周期添加
     */
    @PostMapping(value = "/meter-reade-cycle")
    public Long add(@Valid @RequestBody MeterReadeCycleParam param) {
        return meterReadeCycleService.add(param);
    }

    /**
     * 功能描述： 抄表周期修改
     */
    @PutMapping(value = "/meter-reade-cycle/{id}")
    public void update(@PathVariable Long id, @RequestBody MeterReadeCycleParam param) {
        meterReadeCycleService.update(id, param);
    }

    /**
     * 功能描述： 抄表周期删除
     */
    @DeleteMapping(value = "/meter-reade-cycle/{id}")
    public void delete(@PathVariable Long id) {
        meterReadeCycleService.delete(id);
    }

    /**
     * 功能描述： 抄表周期批量删除
     */
    @DeleteMapping(value = "/meter-reade-cycle/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        meterReadeCycleService.batchDelete(ids);
    }

    /**
     * 功能描述： 抄表周期分页
     */
    @GetMapping(value = "/meter-reade-cycle/page")
    public Pagination<MeterReadeCycleVO> getPage(MeterReadeCycleQueryParam param) {
        return meterReadeCycleService.getPage(param);
    }

    /**
     * 目前的设计 抄表测略只有一条数据
     *
     * @return
     */
    @GetMapping(value = "/meter-reade-cycle/one")
    public MeterReadeCycleVO getOne() {
        List<MeterReadeCycle> list = meterReadeCycleService.list();
        if (!CollectionUtils.isEmpty(list)) {
            return ObjectUtils.copying(list.get(0), MeterReadeCycleVO.class);
        }
        return new MeterReadeCycleVO();
    }

    /**
     * 功能描述： 抄表周期详情
     */
    @GetMapping(value = "/meter-reade-cycle/{id}")
    public MeterReadeCycleVO getById(@PathVariable Long id) {
        return meterReadeCycleService.getById(id);
    }

    /**
     * 功能描述： 手动抄表
     */
    @GetMapping(value = "/manual-meter-reading")
    public Map<String, Object> manualMeterReading() throws URISyntaxException {
        String tenantFlag = httpServletRequest.getHeader("tenantFlag");
        Integer tenant = 100;
        if (tenantFlag.equals("interior")) {
            tenant = SessionContextHolder.getRequester().getTenant();
        }
        return meterDeviceService.readeRecord(tenant.toString());
    }

}