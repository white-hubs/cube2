package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dao.LineLossAlarmRuleLineMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossAlarmRuleLine;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossAlarmRuleLineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineLossAlarmRuleLineServiceImpl extends ServiceImpl<LineLossAlarmRuleLineMapper, LineLossAlarmRuleLine> implements LineLossAlarmRuleLineService {
}
