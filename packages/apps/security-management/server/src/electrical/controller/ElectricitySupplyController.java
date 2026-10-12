package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param.ElectricitySupplyParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.ElectricitySupplyService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.ElectricitySupplyDetailVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 供电关系控制层
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ElectricitySupplyController {

    private final ElectricitySupplyService electricitySupplyService;

    /**
     * 获取新id
     *
     * @return 新id
     */
    @GetMapping("/electricity-supply/get-new-id")
    private List<Long> getNewIdFromService(Long num) {
        var ids = new ArrayList<Long>();
        if (num == null || num <= 0 || num > 1000) {
            ids.add(IdWorker.getId());
            return ids;
        }
        for (int i = 0; i < num; i++) {
            ids.add(IdWorker.getId());
        }
        return ids;
    }

    /**
     * 保存
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @PostMapping("/electricity-supply/save")
    public Boolean save(@RequestBody @Valid ElectricitySupplyParam param) {
        return electricitySupplyService.save(param);
    }

    /**
     * 详情
     *
     * @return 详情
     */
    @GetMapping("/electricity-supply/detail")
    public ElectricitySupplyDetailVO detail() {
        return electricitySupplyService.detail();
    }
}
