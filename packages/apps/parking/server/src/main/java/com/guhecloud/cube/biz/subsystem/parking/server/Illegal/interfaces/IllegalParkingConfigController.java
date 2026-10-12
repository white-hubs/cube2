package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces;

import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.command.IllegalParkingConfigCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.service.IllegalParkingConfigService;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingConfigPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.IllegalParkingConfigVO;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 车辆违停扣分配置
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 17:03
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/illegal/parking/config")
public class IllegalParkingConfigController {
    private final IllegalParkingConfigService illegalParkingConfigService;

    /**
     * 新增或更新
     *
     * @param param
     */
    @PostMapping("/save-or-update")
    public Boolean saveOrUpdate(@RequestBody List<IllegalParkingConfigCommand> param) {
        return illegalParkingConfigService.saveOrUpdate(param);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Boolean deleted(@PathVariable Long id) {
        return DatabaseContextHolder.softDelete(IllegalParkingConfigPO.class, id);
    }

    /**
     * 获取违停配置
     */
    @GetMapping("/get/config")
    public List<IllegalParkingConfigVO> getConfig() {
        return illegalParkingConfigService.getConfig();
    }
}