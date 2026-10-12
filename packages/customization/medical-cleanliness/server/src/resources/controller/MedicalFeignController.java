package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.controller;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.service.MedicalFeignService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.DeptAndDeviceTreeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Feign调用处理
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalFeignController {
    private final MedicalFeignService medicalFeignService;

    @GetMapping("/medical/list/getDeptAndDeviceTree")
    public List<DeptAndDeviceTreeVO> getDeptAndDeviceTree() {
        return medicalFeignService.getDeptAndDeviceTree();
    }
}
