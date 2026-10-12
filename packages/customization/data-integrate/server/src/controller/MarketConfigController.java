package com.guhecloud.cube.base.dataintegrate.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.dataintegrate.server.entity.MarketConfig;
import com.guhecloud.cube.base.dataintegrate.server.param.MarketConfigParam;
import com.guhecloud.cube.base.dataintegrate.server.param.MarketConfigQueryParam;
import com.guhecloud.cube.base.dataintegrate.server.rocketmq.MarketConfigMessage;
import com.guhecloud.cube.base.dataintegrate.server.service.MarketConfigService;
import com.guhecloud.cube.base.dataintegrate.server.vo.MarketConfigVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Appet
 * @Description 采集市场配置
 * @date 2023-09-09 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketConfigController {
    private final MarketConfigService marketConfigService;

    /**
     * 功能描述： 采集市场配置添加
     */
    @PostMapping(value = "/market-config")
    public Long add(@Valid @RequestBody MarketConfigParam param) {
        Long marketId = marketConfigService.add(param);
        //消息推送
        RocketMQContextHolder.send(MarketConfigMessage.TOPIC, MarketConfigMessage.builder().marketId(marketId).build());
        return marketId;
    }

    /**
     * 功能描述： 采集市场配置修改
     */
    @PutMapping(value = "/market-config/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody MarketConfigParam param) {
        marketConfigService.update(id, param);
    }

    /**
     * 功能描述： 采集市场配置删除
     */
    @DeleteMapping(value = "/market-config/{id}")
    public void delete(@PathVariable Long id) {
        marketConfigService.delete(id);
    }

    /**
     * 功能描述： 采集市场配置批量删除
     */
    @DeleteMapping(value = "/market-config/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        marketConfigService.batchDelete(ids);
    }

    /**
     * 功能描述： 采集市场配置分页
     */
    @GetMapping(value = "/market-config/page")
    public Pagination<MarketConfigVO> getPage(MarketConfigQueryParam param) {
        return marketConfigService.getPage(param);
    }

    @GetMapping(value = "/market-config/list")
    public List<MarketConfigVO> getList(MarketConfigQueryParam param) {
        List<MarketConfig> list = marketConfigService.list(Wrappers.<MarketConfig>lambdaQuery()
                                                                   .like(StringUtils.isNotBlank(param.getKeyword()), MarketConfig::getMarketName, param.getKeyword()));
        return ObjectUtils.copying(list, MarketConfigVO.class);
    }

    /**
     * 功能描述： 采集市场配置详情
     */
    @GetMapping(value = "/market-config/{id}")
    public MarketConfigVO getById(@PathVariable Long id) {
        return marketConfigService.getById(id);
    }
}