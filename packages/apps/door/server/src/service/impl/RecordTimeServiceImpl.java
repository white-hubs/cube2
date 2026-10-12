package com.guhecloud.cube.biz.door.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.door.server.dao.RecordTimeMapper;
import com.guhecloud.cube.biz.door.server.entity.RecordTime;
import com.guhecloud.cube.biz.door.server.service.RecordTimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecordTimeServiceImpl extends ServiceImpl<RecordTimeMapper, RecordTime> implements RecordTimeService {
}
