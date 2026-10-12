package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj;

import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ThemeService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ThemePO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 主题信息
 *
 * @author lanchun
 * @date 2023/4/18 16:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThemeInfo {

    /**
     * 主题主键
     */
    private Long id;

    /**
     * 主题名称
     */
    private String name;

    public String getName() {
        //插入主题名称
        ThemeService themeService = ApplicationContextHolder.getBean(ThemeService.class);
        ThemePO theme = themeService.getById(id);
        if (Objects.nonNull(theme)) {
            return theme.getName();
        }
        return name;
    }
}
