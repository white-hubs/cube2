package com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.open;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ThemeService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.querygateway.query.ThemeQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ThemePO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ThemeVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 主题接口管理
 *
 * @author lanchun
 * @date 2024/4/16 10:14
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThemePublicController {

    private final ThemeService themeService;

    /**
     * 主题列表
     *
     * @param themeQuery
     *
     * @return
     */
    @GetMapping("/public/themes")
    public List<ThemeVO> getThemes(@Valid ThemeQuery themeQuery) {
        List<ThemePO> themePOS = this.themeService.list(Wrappers.<ThemePO>lambdaQuery()
                                                                .like(StringUtils.isNotEmpty(themeQuery.getKeyword()), ThemePO::getName, themeQuery.getKeyword())
                                                                .eq(ThemePO::getTenant, themeQuery.getTenant())
                                                                .orderByDesc(ThemePO::getCreateTime));
        return ObjectUtils.copying(themePOS, ThemeVO.class);
    }
}
