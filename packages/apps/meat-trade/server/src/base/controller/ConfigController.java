package com.guhecloud.cube.biz.meattrade.server.base.controller;

import com.guhecloud.cube.biz.meattrade.server.base.entity.Config;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.ConfigEnum;
import com.guhecloud.cube.biz.meattrade.server.base.param.ConfigCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.ConfigUpdateParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.ConfigService;
import com.guhecloud.cube.biz.meattrade.server.base.vo.ConfigForScanMarkVO;
import com.guhecloud.cube.biz.meattrade.server.base.vo.ConfigVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 基础配置接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigController {

    private final ConfigService configService;

    /**
     * 基础配置新增
     *
     * @param param 基础配置新增信息
     */
    @PostMapping("/config")
    public void save(@Valid @RequestBody ConfigCreateParam param) {
        this.configService.save(param);
    }

    /**
     * 基础配置更新
     *
     * @param param 基础配置更新信息
     */
    @PutMapping("/config")
    public void update(@Valid @RequestBody ConfigUpdateParam param) {
        this.configService.update(param);
    }

    /**
     * 基础配置ID查询
     *
     * @return 基础配置信息
     */
    @GetMapping("/config/{id}")
    public ConfigVO getById(@NotNull(message = "主键不能为空") @Min(value = 1, message = "主键不能小于1")
                            @PathVariable("id") Long id
                           ) {
        Config config = this.configService.getById(id);
        return ObjectUtils.copying(config, ConfigVO.class);
    }

    /**
     * 根据业务类型查看基础配置
     *
     * @param type 业务类型
     *
     * @return 基础配置信息
     */
    @GetMapping("/config/type")
    public ConfigVO getByType(@RequestParam("type") ConfigEnum type,
                              @NotNull(message = "市场租户编号不能为空") @RequestParam(value = "tenant") Integer tenant
                             ) {
        SessionContextHolder.requestTenant(tenant);
        Config config = this.configService.getByType(type);
        ConfigVO configVO = ObjectUtils.copying(config, ConfigVO.class);
        if (configVO == null) {
            configVO = new ConfigVO();
            configVO.setValue(config == null && ConfigEnum.PORK_STANDARD_WEIGHT == type ? BigDecimal.valueOf(30) : null);
            configVO.setType(type);
        }
        return configVO;
    }

    /**
     * 查询是否强制扫描角标
     *
     * @return
     */
    @GetMapping("/config/type/force-scan-mark")
    public ConfigForScanMarkVO getFlagForScanMark() {
        Config config = this.configService.getByType(ConfigEnum.FORCE_SCAN_MARK);
        if (config == null) {
            Config temp = new Config();
            temp.setType(ConfigEnum.FORCE_SCAN_MARK);
            temp.setValue(0f);
            //插入记录
            this.configService.save(temp);
            //构造返回
            return ObjectUtils.copying(temp, ConfigForScanMarkVO.class);
        } else {
            return ObjectUtils.copying(config, ConfigForScanMarkVO.class);
        }
    }

}
