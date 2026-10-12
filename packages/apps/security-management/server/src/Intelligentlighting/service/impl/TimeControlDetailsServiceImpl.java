package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.dao.TimeControlDetailsMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity.TimeControlDetails;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.TimeControlDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TimeControlDetailsServiceImpl extends ServiceImpl<TimeControlDetailsMapper, TimeControlDetails> implements TimeControlDetailsService {

}
