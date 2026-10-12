package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.service;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.DeptAndDeviceTreeVO;

import java.util.List;

/**
 * Feign调用处理
 */
public interface MedicalFeignService {
    List<DeptAndDeviceTreeVO> getDeptAndDeviceTree();
}
