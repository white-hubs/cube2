package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.CultivateTaskMapper;
import com.guhecloud.cube.srv.gen.safety.management.po.CultivateTaskPO;
import com.guhecloud.cube.srv.gen.safety.management.service.CultivateTaskService;
import org.springframework.stereotype.Service;

@Service(value = "safetyCultivateTaskService")
public class CultivateTaskServiceImpl extends ServiceImpl<CultivateTaskMapper, CultivateTaskPO> implements CultivateTaskService {
}
