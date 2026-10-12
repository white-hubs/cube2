package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dao.HiddenDangerLevelMapper;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerLevel;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.HiddenDangerLevelQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lzy
 * @date 2022/4/24
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerLevelQueryServiceImpl extends ServiceImpl<HiddenDangerLevelMapper, HiddenDangerLevel> implements HiddenDangerLevelQueryService {

}
