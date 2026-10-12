package com.guhecloud.cube.iot.mgt.device.controller;

import com.guhecloud.cube.iot.mgt.device.param.DevDataShareBatchDelParam;
import com.guhecloud.cube.iot.mgt.device.param.DevDataShareCreateParam;
import com.guhecloud.cube.iot.mgt.device.param.DevDataShareUpdateParam;
import com.guhecloud.cube.iot.mgt.device.service.DevDataShareService;
import com.guhecloud.cube.iot.mgt.device.vo.DevDataShareSummaryVo;
import com.guhecloud.cube.iot.mgt.device.vo.DevDataShareVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.guhecloud.cube.iot.mgt.Const.IOT_MGT_FRONTEND_SUFFIX;

/**
 * 设备数据共享配置接口
 *
 * 路径前缀：/device/mgt/data-share
 */
@Slf4j
@RestController
@RequestMapping(IOT_MGT_FRONTEND_SUFFIX + "/device/mgt/data-share")
public class DevDataShareController {

    @Resource
    private DevDataShareService devDataShareService;

    @PostMapping("")
    public void create(@RequestBody DevDataShareCreateParam param) {
        devDataShareService.create(param);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody DevDataShareUpdateParam param) {
        if (param != null) {
            param.setId(id);
        }
        devDataShareService.update(param);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        devDataShareService.delete(id);
    }

    @PostMapping("/batch/del")
    public void batchDelete(@RequestBody DevDataShareBatchDelParam param) {
        devDataShareService.batchDelete(param);
    }

    /**
     * 批量创建数据共享配置
     *
     * @param items 批量创建的共享配置列表
     */
    @PostMapping("/batch")
    public void batchCreate(@RequestBody List<DevDataShareCreateParam> items) {
        devDataShareService.batchCreate(items);
    }

    @GetMapping("/{deviceId}/as-source")
    public List<DevDataShareVo> listAsSource(@PathVariable Long deviceId) {
        return devDataShareService.listAsSource(deviceId);
    }

    @GetMapping("/{deviceId}/as-target")
    public List<DevDataShareVo> listAsTarget(@PathVariable Long deviceId) {
        return devDataShareService.listAsTarget(deviceId);
    }

    @GetMapping("/{deviceId}/all")
    public DevDataShareSummaryVo summary(@PathVariable Long deviceId) {
        return devDataShareService.summary(deviceId);
    }
}

