package com.guhecloud.cube.biz.meattrade.server.order.controller;

import com.guhecloud.cube.biz.meattrade.server.order.entity.SettlementConfig;
import com.guhecloud.cube.biz.meattrade.server.order.enumeration.SettlementConfigEnum;
import com.guhecloud.cube.biz.meattrade.server.order.param.SettlementConfigCreateParam;
import com.guhecloud.cube.biz.meattrade.server.order.param.SettlementConfigUpdateParam;
import com.guhecloud.cube.biz.meattrade.server.order.service.SettlementConfigService;
import com.guhecloud.cube.biz.meattrade.server.order.vo.SettlementConfigPageVO;
import com.guhecloud.cube.biz.meattrade.server.order.vo.SettlementConfigVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 结算配置接口
 */
@Slf4j
@RestController("meattrade-settlementConfigController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SettlementConfigController {

    private final SettlementConfigService settlementConfigService;

    /**
     * 结算配置新增
     *
     * @param param 结算配置新增信息
     */
    @PostMapping("/settlement-config")
    public void save(@RequestBody List<SettlementConfigCreateParam> param) {
        settlementConfigService.save(param);
    }

    /**
     * 结算配置更新
     *
     * @param param 结算配置更新信息
     */
    @PutMapping("/settlement-config")
    public void update(@RequestBody List<SettlementConfigUpdateParam> param) {
        settlementConfigService.update(param);
    }

    /**
     * 结算配置删除
     *
     * @param id 结算配置主键
     */
    @DeleteMapping("/settlement-config/{id}")
    public void remove(@PathVariable("id") Long id) {
        settlementConfigService.remove(id);
    }

    /**
     * 结算配置列表
     *
     * @return 结算配置信息
     */
    @GetMapping("/settlement-config")
    public List<SettlementConfigPageVO> list() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<SettlementConfig> settlementConfigs = settlementConfigService.lambdaQuery().eq(SettlementConfig::getTenant, tenant).list();
        List<SettlementConfigVO> configVOS = ObjectUtils.copying(settlementConfigs, SettlementConfigVO.class);
        List<SettlementConfigPageVO> result = new ArrayList<>();
        List<SettlementConfigVO> trades = new ArrayList<>();
        configVOS.forEach(config -> {
            config.setKey(config.getId());
            if (SettlementConfigEnum.TRADE.equals(config.getWay())) {
                trades.add(config);
            }
        });

        configVOS.stream().collect(Collectors.groupingBy(SettlementConfigVO::getWay)).forEach((way, list) -> {
            SettlementConfigPageVO pageVO = new SettlementConfigPageVO();
            pageVO.setWay(way);
            pageVO.setName(list.get(0).getName());
            pageVO.setFlag(list.get(0).getFlag());
            if (SettlementConfigEnum.TRADE.equals(way)) {
                pageVO.setRules(trades);
            } else {
                pageVO.setRules(list);
            }
            result.add(pageVO);
        });
        return result;
    }

}
