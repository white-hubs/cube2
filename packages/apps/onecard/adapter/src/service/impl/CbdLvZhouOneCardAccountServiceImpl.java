package com.guhecloud.cube.srv.gen.adapter.onecard.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.dto.cbd.Config;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.dto.cbd.response.BasePageResponse;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.dto.cbd.response.BaseResponse;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.dto.cbd.response.CbdCanteenOrderListResponse;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.dto.cbd.response.TokenResponse;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.param.AmountChangeParamQuery;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.param.CardParamQuery;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.service.OneCardAccountService;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.utils.OneCardHttpClientUtils;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AccountDetailsVO;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AccountOutVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AmountChangeRecordOutVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.CanteenOrderListVO;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.CardVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.ConsumeRecordOutVO;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.CopiesChangeRecordOutVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CbdLvZhouOneCardAccountServiceImpl implements OneCardAccountService {

    private static final String CACHE_KEY_TOKEN = "onecard:token:%s";

    private static final String TOKEN_URL = "%s/api/token/getToken?appId=%s&code=%s";

    private static final String CANTEEN_ORDER_LIST_URL = "%s/api/mobile/v1/third/order/list";

    private final RedisTemplate<String, String> redisTemplate;
    private final SubsystemRedisUtils subsystemRedisUtils;

    @Override
    public Page<AmountChangeRecordOutVo> getAmountChange(AmountChangeParamQuery queryParam) {
        return null;
    }

    @Override
    public Pagination<CanteenOrderListVO> getCanteenOrderList(AmountChangeParamQuery queryParam) {
        var params = new HashMap<String, Object>();
        params.putIfAbsent("createStartTime", queryParam.getBeginDate());
        params.put("orderStatus", "5");//写死传5，代表已就餐订单
        params.put("pageNum", queryParam.getPageNumber());
        params.put("pageSize", queryParam.getPageSize());
        var cbdOrderListResponseBasePageResponse = executeOrderTokenGet(params, new TypeReference<BasePageResponse<List<CbdCanteenOrderListResponse>>>() {});
        if (Objects.isNull(cbdOrderListResponseBasePageResponse) || !cbdOrderListResponseBasePageResponse.getCode().equals("0")) {
            log.error("get canteen order list failed, cbdOrderListResponseBasePageResponse: {}", cbdOrderListResponseBasePageResponse);
            return null;
        }
        var pageInfo = cbdOrderListResponseBasePageResponse.getData();
        var cbdOrderList = cbdOrderListResponseBasePageResponse.getRecords();
        if (Objects.isNull(cbdOrderList) || cbdOrderList.isEmpty()) {
            return Pagination.empty();
        }
        var canteenOrderList = cbdOrderList.stream()
                .map(this::convertToCanteenOrderListVO)
                .filter(Objects::nonNull)
                .toList();
        // 构建分页结果
        return Pagination.<CanteenOrderListVO>builder()
                .total(pageInfo != null ? pageInfo.getTotal() : canteenOrderList.size())
                .pages(pageInfo != null ? pageInfo.getPages() : 0L)
                .records(canteenOrderList)
                .build();
    }

    @Override
    public Page<CopiesChangeRecordOutVo> getCopiesChange(AmountChangeParamQuery queryParam) {
        return null;
    }

    @Override
    public AccountOutVo getAccountStatusInfo(AmountChangeParamQuery queryParam) {
        return null;
    }

    @Override
    public Page<CardVo> getCards(CardParamQuery queryParam) {
        return null;
    }

    @Override
    public Page<AccountDetailsVO> getAccountStatusInfoBatch(AmountChangeParamQuery queryParam) {
        return null;
    }

    @Override
    public Page<ConsumeRecordOutVO> getConsumeRecord(AmountChangeParamQuery queryParam) {
        return null;
    }

    private String getCanteenOrderToken() {
        var cacheKey = String.format(CACHE_KEY_TOKEN, SessionContextHolder.getRequester().getTenant().toString());
        var token = this.redisTemplate.opsForValue().get(cacheKey);
        if (token != null) {
            return token;
        }
        var adapterConfig = getConfig();
        if (Objects.isNull(adapterConfig)) {
            log.error("get token failed, adapterConfig is null");
            return null;
        }
        try {
            var response = OneCardHttpClientUtils.doGet(String.format(TOKEN_URL, adapterConfig.getCanteenTokenBaseApiUrl(), adapterConfig.getCanteenAppId(), adapterConfig.getCanteenCode()), null, null);
            var tokenResponse = JsonUtils.deserialize(response, new TypeReference<BaseResponse<TokenResponse>>() {});
            if (Objects.isNull(tokenResponse) || !tokenResponse.getSuccess() || Objects.isNull(tokenResponse.getData())) {
                log.error("get token failed, tokenResponse: {}", tokenResponse);
                return null;
            }
            token = tokenResponse.getData().getToken();
            this.redisTemplate.opsForValue()
                    .set(cacheKey, token, tokenResponse.getData().getExpire() - 1000 * 60 * 10, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("get token failed, adapterConfig: {}", adapterConfig, e);
        }
        return token;
    }

    private <T> T executeOrderTokenGet(Map<String, Object> params, TypeReference<T> typeReference) {
        var token = getCanteenOrderToken();
        if (Objects.isNull(token)) {
            log.error("get token failed, token is null");
            return null;
        }
        var config = getConfig();
        if (Objects.isNull(config)) {
            return null;
        }
        try {
            var response = OneCardHttpClientUtils.doGet(String.format(CANTEEN_ORDER_LIST_URL, config.getCanteenOrderListBaseApiUrl()), Map.of("X-H3C-TOKEN", token), params);
            return JsonUtils.deserialize(response, typeReference);
        } catch (Exception e) {
            log.error("execute get failed, url: {}, token: {}", CANTEEN_ORDER_LIST_URL, token, e);
            return null;
        }
    }

    private Config getConfig() {
        var adapterConfig = subsystemRedisUtils.getSubAdapterRefreshConfig();
        if (Objects.isNull(adapterConfig) || Objects.isNull(adapterConfig.getManufacturerJson())) {
            return null;
        }
        return JsonUtils.deserialize(adapterConfig.getManufacturerJson(), Config.class);
    }

    private CanteenOrderListVO convertToCanteenOrderListVO(CbdCanteenOrderListResponse source) {
        if (Objects.isNull(source)) {
            return null;
        }
        var target = new CanteenOrderListVO();
        // 直接映射相同字段
        target.setOrderId(source.getOrderId());
        target.setCardId(source.getCardId());
        target.setCardNo(source.getCardNo());
        target.setDepId(source.getDepId());
        target.setUserId(source.getUserId());
        target.setName(source.getName());
        target.setDepName(source.getDepName());
        target.setCanteenName(source.getCanteenName());
        target.setWindowName(source.getWindowName());
        target.setOrderStatusStr(source.getOrderStatusStr());
        target.setPayTypeStr(source.getPayTypeStr());
        target.setPayType(source.getPayType());
        target.setPrice(source.getPrice());
        target.setPersonalPrice(source.getPersonalPrice());
        target.setCardPrice(source.getCardPrice());
        target.setCashBalance(source.getCashBalance());
        target.setPaymentType(source.getPaymentType());
        target.setCanteenId(source.getCanteenId());
        target.setWindowId(source.getWindowId());
        target.setOrderStatus(source.getOrderStatus());
        target.setCreateTime(source.getCreateTime());
        target.setPayTime(source.getPayTime());
        target.setCostPrice(source.getCostPrice());
        target.setDiscounts(source.getDiscounts());
        target.setTime(source.getTime());
        target.setRefundAmount(source.getRefundAmount());
        target.setAbnormal(source.getAbnormal());
        target.setAbnormalDesc(source.getAbnormalDesc());
        target.setReceivedPrice(source.getReceivedPrice());
        return target;
    }
}
