package com.guhecloud.cube.biz.fusion.communicate.server.conference.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.po.ConferenceRoomPO;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.repository.mapper.ConferenceRoomMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConferenceRoomService extends ServiceImpl<ConferenceRoomMapper, ConferenceRoomPO> {
}
