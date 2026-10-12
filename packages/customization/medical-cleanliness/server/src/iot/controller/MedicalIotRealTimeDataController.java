package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.controller;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.param.MedicalIotRealTimeDataParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.service.MedicalIotRealTimeDataService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.MedicalIotRealTimeDataVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.RealTimeDataAbnormalPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 实时监测
 * @Author: lijie
 * @CreateTime: 2024-10-22  20:03
 * @Description:
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalIotRealTimeDataController {
    private final MedicalIotRealTimeDataService medicalIotRealTimeDataService;

    /**
     * 实时监测分页
     */
    @PostMapping("/get/real/time/data/page")
    public Pagination<MedicalIotRealTimeDataVO> getRealTimeDataPage(@RequestBody MedicalIotRealTimeDataParam param) {
        return medicalIotRealTimeDataService.getRealTimeDataPage(param);
    }
    /**
     * 实时监测异常指标分页
     */
    @GetMapping("/get/real/time/data/abnormal/page")
    public Pagination<RealTimeDataAbnormalPageVO> getRealTimeDataAbnormalPage(Paging param) {
        return medicalIotRealTimeDataService.getRealTimeDataAbnormalPage(param);
    }
    /**查询实时监测详情*/
    @GetMapping("/get/real/time/data/details/{id}")
    public MedicalIotRealTimeDataVO getRealTimeDataDetails(@PathVariable("id") Long id) {
        return medicalIotRealTimeDataService.getRealTimeDataDetails(id);
    }
}
