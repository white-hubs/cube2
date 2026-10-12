package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.Basic;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.repo.mapper.DetectionBasicMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.IDetectionBasicService;
import org.springframework.stereotype.Service;

@Service
public class DetectionBasicServiceImpl extends ServiceImpl<DetectionBasicMapper, Basic> implements IDetectionBasicService {
}