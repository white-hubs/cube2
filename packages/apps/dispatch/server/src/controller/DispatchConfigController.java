package com.guhecloud.cube.biz.dispatch.server.controller;

import com.guhecloud.cube.biz.dispatch.server.entity.DispatchConfig;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchConfigParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchConfigQueryParam;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchConfigService;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchDeviceService;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchConfigVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceVO;
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
import java.util.Objects;

/**
 * @author Appet
 * @Description 调度台配置
 * @date 2022-07-26 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DispatchConfigController {
    private final DispatchConfigService dispatchConfigService;
    private final DispatchDeviceService dispatchDeviceService;

    /**
     * 功能描述： 调度台配置添加
     */
    @PostMapping(value = "/dispatch-config")
    public Long add(@Valid @RequestBody DispatchConfigParam param) {
        return dispatchConfigService.add(param);
    }

    /**
     * 功能描述： 调度台配置修改
     */
    @PutMapping(value = "/dispatch-config/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody DispatchConfigParam param) {
        dispatchConfigService.update(id, param);
    }

    /**
     * 功能描述： 调度台配置删除
     */
    @DeleteMapping(value = "/dispatch-config/{id}")
    public void delete(@PathVariable Long id) {
        dispatchConfigService.delete(id);
    }

    /**
     * 功能描述： 调度台配置批量删除
     */
    @DeleteMapping(value = "/dispatch-config/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        dispatchConfigService.batchDelete(ids);
    }

    /**
     * 功能描述： 调度台配置分页
     */
    @GetMapping(value = "/dispatch-config/page")
    public Pagination<DispatchConfigVO> getPage(DispatchConfigQueryParam param) {
        return dispatchConfigService.getPage(param);
    }

    /**
     * 功能描述： 调度台配置详情
     */
    @GetMapping(value = "/dispatch-config/{id}")
    public DispatchConfigVO getById(@PathVariable Long id) {
        return dispatchConfigService.getById(id);
    }

    /**
     * 功能描述： 获取租户的调度台配置
     */
    @GetMapping(value = "/dispatch-config")
    public DispatchConfigVO getConfigByTenant() {
        DispatchConfig one = dispatchConfigService.getOne(null);
        if (Objects.isNull(one)) {
            return null;
        }
        DispatchConfigVO dispatchConfigVO = ObjectUtils.copying(one, DispatchConfigVO.class);
        DispatchDeviceVO dispatchDevice = this.dispatchDeviceService.getById(one.getAccount());
        if (Objects.nonNull(dispatchDevice)) {
            dispatchConfigVO.setName(dispatchDevice.getName());
        }
        return dispatchConfigVO;
    }

}