package com.guhecloud.cube.srv.gen.product.market.server.common.controller;

import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.dto.StallDTO;
import com.guhecloud.cube.biz.mdata.client.param.StallCreateParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketStall;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.MarketStallParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.MarketStallQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.service.MarketStallService;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.MarketStallGroupByBossVO;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.MarketStallVO;
import com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.service.ThirdDataQueryService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
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
 * @Description 档位管理
 * @date 2023-05-10 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketStallController {
    private final MarketStallService marketStallService;
    private final ThirdDataQueryService thirdDataQueryService;
    private final MarketStallService markStallService;
    private final StallClient stallClient;

    /**
     * 功能描述： 档位管理添加
     */
    @PostMapping(value = "/market-stall")
    public Long add(@Valid @RequestBody MarketStallParam param) {
        return marketStallService.add(param);
    }

    /**
     * 功能描述： 档位管理修改
     */
    @PutMapping(value = "/market-stall/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody MarketStallParam param) {
        marketStallService.update(id, param);
    }

    /**
     * 功能描述： 档位管理删除
     */
    @DeleteMapping(value = "/market-stall/{id}")
    public void delete(@PathVariable Long id) {
        marketStallService.delete(id);
    }

    /**
     * 功能描述： 档位管理批量删除
     */
    @DeleteMapping(value = "/market-stall/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        marketStallService.batchDelete(ids);
    }

    /**
     * 功能描述： 档位管理分页
     */
    @GetMapping(value = "/market-stall/page")
    public Pagination<MarketStallVO> getPage(MarketStallQueryParam param) {
        return marketStallService.getPage(param);
    }

    /**
     * 功能描述：查询档主带档位信息
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/market-stall/group-by-boss/page")
    public Pagination<MarketStallGroupByBossVO> getPageGroupByBoss(MarketStallQueryParam param) {
        return marketStallService.getStallGroupByBossPage(param);
    }

    /**
     * 功能描述： 档位管理详情
     */
    @GetMapping(value = "/market-stall/{id}")
    public MarketStallVO getById(@PathVariable Long id) {
        return marketStallService.getById(id);
    }

    /**
     * 将第三方数据（成农批）的档位同步到mdata中
     */
    @PostMapping("/market-stall/mdata/sync")
    public void syncMarketStallDataToMdata() {
        //获取第三方的档位数据
        List<MarketStall> stalls = thirdDataQueryService.getStalls();
        //保存档位数据
        if (CollectionUtils.isNotEmpty(stalls)) {
            stalls.stream().forEach(stall -> {
                //查询档位
                StallDTO stallDTO = stallClient.getStallByCode(stall.getCode());
                if (stallDTO == null) {
                    StallCreateParam copying = ObjectUtils.copying(stall, StallCreateParam.class);
                    copying.setType(null);
                    stallClient.save(copying);
                }
            });
        }
    }

    @PostMapping("/market-stall/sync")
    public void syncMarketStallData() {
        List<MarketStall> stalls = thirdDataQueryService.getStalls();
        //保存档位数据
        if (CollectionUtils.isNotEmpty(stalls)) {
            markStallService.remove(null);
            markStallService.saveBatch(stalls);
        }
    }

}