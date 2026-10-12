package com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ThemeService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.command.ChangeThemeCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.command.IncreaseThemeCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.querygateway.query.ThemeQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ThemePO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ThemeVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 主题接口管理
 *
 * @author lanchun
 * @date 2023/4/18 11:09
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThemeController {

    private final ThemeService themeService;

    /**
     * 主题新增
     *
     * @param increaseThemeCommand
     */
    @PostMapping("/theme")
    public Long save(@Valid @RequestBody IncreaseThemeCommand increaseThemeCommand) {
        ThemePO theme = ObjectUtils.copying(increaseThemeCommand, ThemePO.class);
        this.themeService.save(theme);
        return theme.getId();
    }

    /**
     * 主题修改
     *
     * @param changeThemeCommand
     */
    @PutMapping("/theme/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody ChangeThemeCommand changeThemeCommand) {
        ThemePO theme = this.themeService.getById(id);
        Assert.isNotFound(theme, "主题不存在");
        ObjectUtils.copying(changeThemeCommand, theme);
        this.themeService.updateById(theme);
    }

    /**
     * 主题详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/theme/{id}")
    public ThemeVO getTheme(@PathVariable("id") Long id) {
        ThemePO theme = this.themeService.getById(id);
        Assert.isNotFound(theme, "主题不存在");
        return ObjectUtils.copying(theme, ThemeVO.class);
    }

    /**
     * 主题分页列表
     *
     * @param themeQuery
     *
     * @return
     */
    @GetMapping("/theme/page")
    public Pagination<ThemeVO> getThemePage(@Valid ThemeQuery themeQuery) {
        Page<ThemePO> page = this.themeService.page(PagingUtils.convert(themeQuery), Wrappers.<ThemePO>lambdaQuery()
                .like(StringUtils.isNotEmpty(themeQuery.getKeyword()), ThemePO::getName, themeQuery.getKeyword())
                .orderByDesc(ThemePO::getCreateTime));
        return PagingUtils.convert(page, ThemeVO.class);
    }

    /**
     * 主题列表
     *
     * @param themeQuery
     *
     * @return
     */
    @GetMapping("/themes")
    public List<ThemeVO> getThemes(@Valid ThemeQuery themeQuery) {
        List<ThemePO> themePOS = this.themeService.list(Wrappers.<ThemePO>lambdaQuery()
                                                                .like(StringUtils.isNotEmpty(themeQuery.getKeyword()), ThemePO::getName, themeQuery.getKeyword())
                                                                .orderByDesc(ThemePO::getCreateTime));
        return ObjectUtils.copying(themePOS, ThemeVO.class);
    }

    /**
     * 主题删除
     *
     * @param id
     */
    @DeleteMapping("/theme/{id}")
    public void removeTheme(@PathVariable("id") Long id) {
        DatabaseContextHolder.softDelete(ThemePO.class, id);
    }
}
