package com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces;

import com.guhecloud.cube.base.device.center.metadata.param.DeviceBaseInfoPageQry;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.command.OutsourceDeviceImportCommand;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.impl.OutsourceDeviceInstanceService;
import com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces.vo.OutsourceDeviceVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Collections;

/**
 * @author pengjian
 * @date 2022/5/19 14:29
 * @description
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OutsourceDeviceInstanceController {

    private final OutsourceDeviceInstanceService outsourceDeviceInstanceService;

    /**
     * 委外设备单个删除
     *
     * @param deviceInstanceId 设备实例id
     */
    @DeleteMapping("/outsource/device/instance/{deviceInstanceId}")
    public void delete(@PathVariable("deviceInstanceId") Long deviceInstanceId) {
        final OutsourceDeviceImportCommand command = new OutsourceDeviceImportCommand();
        command.setDeviceInstanceIds(Collections.singleton(deviceInstanceId));
        outsourceDeviceInstanceService.batchDelete(command);
    }

    /**
     * 委外设备批量移除
     *
     * @param importCommand 设备id集合
     */
    @PostMapping("/outsource/device/batchRemove")
    public void batchDelete(@RequestBody @Valid OutsourceDeviceImportCommand importCommand) {
        outsourceDeviceInstanceService.batchDelete(importCommand);
    }

    /**
     * 委外设备导入
     *
     * @param importCommand 导入设备id
     */
    @PostMapping("/outsource/device/import")
    public void outsourceDeviceImport(@RequestBody @Valid OutsourceDeviceImportCommand importCommand) {
        outsourceDeviceInstanceService.outsourceDeviceImport(importCommand);
    }

    /**
     * 委外设备列表
     *
     * @return 委外设备列表
     */
    @GetMapping("/outsource/device/list")
    public Pagination<OutsourceDeviceVO> outsourceDeviceList(@SpringQueryMap DeviceBaseInfoPageQry pageQry) {
        return outsourceDeviceInstanceService.outsourceDeviceList(pageQry);
    }

}
