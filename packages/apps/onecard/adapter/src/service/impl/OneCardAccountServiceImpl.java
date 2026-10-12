package com.guhecloud.cube.srv.gen.adapter.onecard.server.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.param.AmountChangeParamQuery;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.param.CardParamQuery;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.param.SearchParam;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.service.OneCardAccountService;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AccountDetailsVO;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AccountOutVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AccountVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AmountChangeRecordOutVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AmountChangeRecordVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.CardVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.ConsumeRecordOutVO;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.CopiesChangeRecordOutVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.CopiesChangeRecordVo;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OneCardAccountServiceImpl implements OneCardAccountService {

    private final RestTemplate restTemplate;

    private final SubsystemRedisUtils subsystemRedisUtils;

    @Override
    public Page<AmountChangeRecordOutVo> getAmountChange(AmountChangeParamQuery queryParam) {
        String key =
                SubsystemTypeConstants.ONECARD_SERVER + ":" + "oneCard" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        SubsystemAdapterConfig subAdapterRefreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        String ipPort = subAdapterRefreshConfig.getServiceAddr() + subAdapterRefreshConfig.getServicePort();
        String url = ipPort + "/api/consume/sel-xf-detail";
        //获取请求
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        MultiValueMap<String, Object> requestMap = requestMap(queryParam);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(requestMap, headers);
        JSONObject jsonObject = restTemplate.postForObject(url, httpEntity, JSONObject.class);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray records = data.getJSONArray("records");

        List<AmountChangeRecordVo> amountChangeRecordDTOS = JsonUtils.deserialize(records, new TypeReference<List<AmountChangeRecordVo>>() {
        });
        List<AmountChangeRecordOutVo> copying = ObjectUtils.copying(amountChangeRecordDTOS, AmountChangeRecordOutVo.class);
        Page page = new Page<>();
        page.setRecords(copying);
        Long totalRecord = Long.valueOf(data.get("totalRecord").toString());
        page.setTotal(totalRecord);
        return page;
    }

    @Override
    public Page<CopiesChangeRecordOutVo> getCopiesChange(AmountChangeParamQuery queryParam) {

        String key =
                SubsystemTypeConstants.ONECARD_SERVER + ":" + "oneCard" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        SubsystemAdapterConfig subAdapterRefreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        String ipPort = subAdapterRefreshConfig.getServiceAddr() + subAdapterRefreshConfig.getServicePort();
        String url = ipPort + "/api/consume/selectRecordListFen";
        //获取请求
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        MultiValueMap<String, Object> requestMap = requestMap(queryParam);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(requestMap, headers);
        JSONObject jsonObject = restTemplate.postForObject(url, httpEntity, JSONObject.class);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray records = data.getJSONArray("records");
        List<CopiesChangeRecordVo> copiesChangeRecordDTOS = JsonUtils.deserialize(records, new TypeReference<List<CopiesChangeRecordVo>>() {
        });
        List<CopiesChangeRecordOutVo> copying = ObjectUtils.copying(copiesChangeRecordDTOS, CopiesChangeRecordOutVo.class);
        Page page = new Page<>();
        page.setRecords(copying);
        Long totalRecord = Long.valueOf(data.get("totalRecord").toString());
        page.setTotal(totalRecord);
        return page;
    }

    @Override
    public AccountOutVo getAccountStatusInfo(AmountChangeParamQuery queryParam) {
        String key =
                SubsystemTypeConstants.ONECARD_SERVER + ":" + "oneCard" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        SubsystemAdapterConfig subAdapterRefreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        String ipPort = subAdapterRefreshConfig.getServiceAddr() + subAdapterRefreshConfig.getServicePort();
        String url = ipPort + "/api/consume/account-status-info";
        //获取请求
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap();
        requestMap.add("userNo", queryParam.getUserNo());
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(requestMap, headers);
        JSONObject jsonObject = restTemplate.postForObject(url, httpEntity, JSONObject.class);
        JSONObject data = jsonObject.getJSONObject("data");
        AccountVo accountVo = JsonUtils.deserialize(data, new TypeReference<AccountVo>() {
        });
        AccountOutVo copying = ObjectUtils.copying(accountVo, AccountOutVo.class);
        return copying;
    }

    @Override
    public Page<CardVo> getCards(CardParamQuery queryParam) {

        String key =
                SubsystemTypeConstants.ONECARD_SERVER + ":" + "oneCard" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        SubsystemAdapterConfig subAdapterRefreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        String ipPort = subAdapterRefreshConfig.getServiceAddr() + subAdapterRefreshConfig.getServicePort();
        String url = ipPort + "/api/card/cardIssueList";
        //获取请求
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap();
        if (StringUtils.isNotEmpty(queryParam.getKeyword())) {
            requestMap.add("keys", queryParam.getKeyword());
        }
        if (StringUtils.isNotEmpty(queryParam.getUserDep())) {
            requestMap.add("userDep", queryParam.getUserDep());
        }
        requestMap.add("pageIndex", queryParam.getPageNumber());
        requestMap.add("pageSize", queryParam.getPageSize());
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(requestMap, headers);
        JSONObject jsonObject = restTemplate.postForObject(url, httpEntity, JSONObject.class);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray records = data.getJSONArray("records");
        List<CardVo> cardVos = JsonUtils.deserialize(records, new TypeReference<List<CardVo>>() {
        });
        Page page = new Page<>();
        page.setRecords(cardVos);
        Long totalRecord = Long.valueOf(data.get("totalRecord").toString());
        page.setTotal(totalRecord);
        return page;
    }

    @Override
    public Page<AccountDetailsVO> getAccountStatusInfoBatch(AmountChangeParamQuery queryParam) {
        return null;
    }

    @Override
    public Page<ConsumeRecordOutVO> getConsumeRecord(AmountChangeParamQuery queryParam) {
        return null;
    }

    private MultiValueMap<String, Object> requestMap(AmountChangeParamQuery queryParam) {
        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap();
        SearchParam searchParam = new SearchParam();
        if (StringUtils.isNotEmpty(queryParam.getDevSerial())) {
            searchParam.setDevSerial(queryParam.getDevSerial());
        }
        if (StringUtils.isNotEmpty(queryParam.getBeginDate())) {
            searchParam.setBeginDate(queryParam.getBeginDate());
        }
        if (StringUtils.isNotEmpty(queryParam.getEndDate())) {
            searchParam.setEndDate(queryParam.getEndDate());
        }
        if (StringUtils.isNotEmpty(queryParam.getType())) {
            searchParam.setType(Integer.valueOf(queryParam.getType()));
        }
        requestMap.add("pageIndex", queryParam.getPageNumber());
        requestMap.add("pageSize", queryParam.getPageSize());
        requestMap.add("search", searchParam);
        return requestMap;
    }
}
