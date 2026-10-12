package com.guhecloud.cube.srv.gen.adapter.onecard.server.service.impl;

import com.antherd.smcrypto.sm2.Sm2;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.config.RiXinDaConfig;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.enumeration.ConsumptionRecordTypeEnum;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.enumeration.RiXinDaInterfaceEnum;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.param.AmountChangeParamQuery;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.param.CardParamQuery;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.service.OneCardAccountService;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.utils.OneCardHttpClientUtils;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AccountDetailsVO;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AccountOutVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AmountChangeRecordOutVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.CardVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.ConsumeRecordOutVO;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.CopiesChangeRecordOutVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.RiXinDaAccountVO;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.RiXinDaConsumeRecordOutVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/16 10:38
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiXinDaOneCardAccountServiceImpl implements OneCardAccountService {

    public static final String LOGIN_URL = "/auth/b/doLogin";

    private final SubsystemRedisUtils subsystemRedisUtils;

    @Override
    public Page<AmountChangeRecordOutVo> getAmountChange(AmountChangeParamQuery queryParam) {
        return null;
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
        queryParam.setType(RiXinDaInterfaceEnum.ACCOUNT_INFO.name());
        Map<String, Object> resultMap = executeHttpsRequest(queryParam);
        Page<RiXinDaAccountVO> accountVOPage = JsonUtils.deserialize(resultMap.get("data"),
                                                                     new TypeReference<Page<RiXinDaAccountVO>>() {});
        List<AccountDetailsVO> accountDetailsVOList = accountVOPage.getRecords().stream().map(record -> {
            AccountDetailsVO accountDetailsVO = new AccountDetailsVO();
            accountDetailsVO.setId(Long.valueOf(record.getId().trim()));
            accountDetailsVO.setUserCard(record.getCardNumber());
            accountDetailsVO.setSubsidyBalance(record.getSubsidyAmount());
            accountDetailsVO.setCashBalance(record.getCashBalance());
            accountDetailsVO.setHaircutCount(record.getHairCouponAmount());
            accountDetailsVO.setLaundryCount(record.getLaundryCount());
            return accountDetailsVO;
        }).collect(Collectors.toList());
        Page<AccountDetailsVO> consumeRecordOutVOS2 = new Page<>();
        consumeRecordOutVOS2.setRecords(accountDetailsVOList);
        return consumeRecordOutVOS2;
    }

    @Override
    public Page<ConsumeRecordOutVO> getConsumeRecord(AmountChangeParamQuery queryParam) {
        Map<String, Object> resultMap = executeHttpsRequest(queryParam);
        Page<RiXinDaConsumeRecordOutVO> consumeRecordOutVOS = JsonUtils.deserialize(resultMap.get("data"),
                                                                                    new TypeReference<Page<RiXinDaConsumeRecordOutVO>>() {});
        RiXinDaInterfaceEnum interfaceEnum = RiXinDaInterfaceEnum.valueOf(queryParam.getType());
        List<ConsumeRecordOutVO> consumeRecordOutVOS1 = consumeRecordOutVOS.getRecords().stream().map(record -> {
            ConsumeRecordOutVO consumeRecordOut = ObjectUtils.copying(record, ConsumeRecordOutVO.class);
            consumeRecordOut.setRecordType(interfaceEnum.getDesc());
            consumeRecordOut.setDevice(record.getOrgName());
            interfaceEnum.extendInfoTransfer(record, consumeRecordOut);
            consumeRecordOut.setMode(ConsumptionRecordTypeEnum.getEnumDesc(record.getMode()));
            return consumeRecordOut;
        }).collect(Collectors.toList());
        Page<ConsumeRecordOutVO> consumeRecordOutVOS2 = new Page<>();
        consumeRecordOutVOS2.setRecords(consumeRecordOutVOS1);
        return consumeRecordOutVOS2;
    }

    private Map<String, Object> executeHttpsRequest(AmountChangeParamQuery queryParam) {
        String key =
                SubsystemTypeConstants.ONECARD_SERVER + ":" + "RiXinDa" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        SubsystemAdapterConfig subAdapterRefreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        String ipPort = subAdapterRefreshConfig.getServiceAddr() + ":" + subAdapterRefreshConfig.getServicePort();
        RiXinDaInterfaceEnum riXinDaInterfaceEnum = RiXinDaInterfaceEnum.valueOf(queryParam.getType());
        String url = ipPort + riXinDaInterfaceEnum.getUrl();
        //获取token
        String token = this.getAccessToken(subAdapterRefreshConfig.getManufacturerJson(), ipPort);
        Map<String, Object> param = riXinDaInterfaceEnum.getQueryParam(queryParam);
        param.put("token", token);
        param.put("current", queryParam.getPageNumber());
        param.put("size", queryParam.getPageSize());
        String response = OneCardHttpClientUtils.doGet(url, null, param);
        Map<String, Object> resultMap = JsonUtils.deserialize(response, new TypeReference<Map<String, Object>>() {
        });
        if (!Objects.equals(200, resultMap.get("code"))) {
            throw new RuntimeException("查询失败" + resultMap.get("msg"));
        }
        return resultMap;
    }

    private String getAccessToken(Object manufacturerJson, String ipPort) {
        RiXinDaConfig riXinDaConfig = JsonUtils.deserialize(manufacturerJson, RiXinDaConfig.class);
        Map<String, String> param = new HashMap<>();
        param.put("account", riXinDaConfig.getUsername());
        param.put("password", this.sm2Encrypt(riXinDaConfig.getPassword(), riXinDaConfig.getPublicKey()));
        String response = OneCardHttpClientUtils.doPostJson(ipPort + LOGIN_URL, null, JsonUtils.serialize(param));
        if (StringUtils.isEmpty(response)) {
            throw new RuntimeException("登录失败");
        }
        Map<String, Object> resultMap = JsonUtils.deserialize(response, new TypeReference<Map<String, Object>>() {
        });
        if (!Objects.equals(200, resultMap.get("code"))) {
            throw new RuntimeException("登录失败：" + resultMap.get("msg"));
        }
        return resultMap.get("data").toString();
    }

    /**
     * sm2加密
     *
     * @param password
     *
     * @return
     */
    public String sm2Encrypt(String password, String publicKey) {
        String doEncrypt = Sm2.doEncrypt(password, publicKey);
        log.info("加密后密码：" + doEncrypt);
        return doEncrypt;

    }

}
