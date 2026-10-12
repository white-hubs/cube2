package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.Company;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.repo.mapper.DetectionCompanyMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.IDetectionCompanyService;
import org.springframework.stereotype.Service;

@Service
public class DetectionCompanyServiceImpl extends ServiceImpl<DetectionCompanyMapper, Company> implements IDetectionCompanyService {
}