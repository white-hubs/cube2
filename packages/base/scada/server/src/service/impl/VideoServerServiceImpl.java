package com.guhecloud.kaihe.scada.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.kaihe.scada.server.entity.VideoServer;
import com.guhecloud.kaihe.scada.server.mapper.VideoServerMapper;
import com.guhecloud.kaihe.scada.server.service.VideoServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideoServerServiceImpl extends ServiceImpl<VideoServerMapper, VideoServer> implements VideoServerService {
}
