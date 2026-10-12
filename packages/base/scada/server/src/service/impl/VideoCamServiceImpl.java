package com.guhecloud.kaihe.scada.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.kaihe.scada.server.entity.VideoCam;
import com.guhecloud.kaihe.scada.server.mapper.VideoCamMapper;
import com.guhecloud.kaihe.scada.server.service.VideoCamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideoCamServiceImpl extends ServiceImpl<VideoCamMapper, VideoCam> implements VideoCamService {
}
