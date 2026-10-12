package com.guhecloud.cube.base.message2.server.sat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgTmpQueryParam;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgTmpSettingConfigParam;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgTmpService;
import com.guhecloud.cube.base.message2.server.sat.vo.SatMsgTmpPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 租户模板管理
 *
 * @author lanchun
 * @date 2023/6/27 13:51
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatMsgTmpController {

    private final SatMsgTmpService satMsgTmpService;

    /**
     * 模板分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/sat-msg-tmp/page")
    public Pagination<SatMsgTmpPageVO> getSatMsgTmpPage(@Valid SatMsgTmpQueryParam param) {
        Page<SatMsgTmpPageVO> satMsgTmpPageVOPage = this.satMsgTmpService.getSatMsgTmpPage(param);
        return PagingUtils.convert(satMsgTmpPageVOPage);
    }

    /**
     * 模板列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/sat-msg-tmp/list")
    public List<SatMsgTmpPageVO> getSatMsgTemplates(@Valid SatMsgTmpQueryParam param) {
        return this.satMsgTmpService.getSatMsgTemplates(param);
    }

    /**
     * 模板配置设置
     *
     * @param id
     * @param param
     */
    @PostMapping("/sat-msg-tmp/{id}/setting-config")
    public void settingSatMsgTmpConfig(@PathVariable("id") Long id, @RequestBody SatMsgTmpSettingConfigParam param) {
        this.satMsgTmpService.settingConfig(id, param);
    }

    /**
     * 模板刷新
     */
    @PostMapping("/sat-msg-tmp/refresh")
    public void refreshTemplate() {
        this.satMsgTmpService.refreshTemplate();
    }
}
