package com.guhecloud.cube.srv.gen.conferencemanage.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.ConferenceTypeMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.ConferenceType;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConferenceTypeServiceImpl extends ServiceImpl<ConferenceTypeMapper, ConferenceType> implements ConferenceTypeService {
}
