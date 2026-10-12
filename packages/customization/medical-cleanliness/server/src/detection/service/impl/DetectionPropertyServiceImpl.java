package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.Property;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.repo.mapper.DetectionPropertyMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.IDetectionPropertyService;
import org.springframework.stereotype.Service;

@Service
public class DetectionPropertyServiceImpl extends ServiceImpl<DetectionPropertyMapper, Property> implements IDetectionPropertyService {
}