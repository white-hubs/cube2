package com.guhecloud.cube.srv.gen.safety.education.server.common.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ThemePO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.infrastructure.repository.mapper.ThemeMapper;
import org.springframework.stereotype.Service;

@Service
public class ThemeService extends ServiceImpl<ThemeMapper, ThemePO> {
}
