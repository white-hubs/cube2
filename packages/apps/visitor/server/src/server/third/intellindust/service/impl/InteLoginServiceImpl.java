package com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.SystemLoginInfoMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.SystemLoginInfoPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.service.InteLoginService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.utils.InteVisitorHttpUtils;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.ThirdTokenVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 英特灵达(intellindust)
 * 登录
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InteLoginServiceImpl implements InteLoginService {
    private final SystemLoginInfoMapper systemLoginInfoMapper;
    private static final String SCENE_CODE = "intellindust-visitor";
    private static final String TOKEN_URL = "/openapi/login";

    private ThirdTokenVO getToken(String username, String password, String baseUrl) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("userCode", username);
        dataMap.put("password", password);
        Object obj = InteVisitorHttpUtils.sendThirdPost(baseUrl + TOKEN_URL, dataMap, null);
        if (obj == null) {
            throw new ServiceException("鉴权连接失败！");
        }
        JSONObject jsonObject = (JSONObject) obj;
        ThirdTokenVO thirdTokenVO = new ThirdTokenVO();
        thirdTokenVO.setAccessToken(jsonObject.getString("sessionId"));
        thirdTokenVO.setUserId(jsonObject.getString("userId"));
        thirdTokenVO.setUsername(username);
        thirdTokenVO.setPassword(password);

        return thirdTokenVO;
    }

    @Override
    public List<SystemLoginInfoPO> loginToken() {
        List<SystemLoginInfoPO> loginInfoPOList = systemLoginInfoMapper.selectList(Wrappers.<SystemLoginInfoPO>lambdaQuery()
                                                                                           .eq(SystemLoginInfoPO::getSceneCode, SCENE_CODE)
                                                                                  );
        if (loginInfoPOList != null && !loginInfoPOList.isEmpty()) {
            for (SystemLoginInfoPO loginInfoPO : loginInfoPOList) {
                //                 过期的token需要重新获取
                if (loginInfoPO.getTokenExpires() != null && !loginInfoPO.getTokenExpires().isAfter(LocalDateTime.now())) {
                    ThirdTokenVO thirdTokenVO = this.getToken(loginInfoPO.getUsername(), loginInfoPO.getPassword(), loginInfoPO.getBaseUrl());
                    if (StringUtils.isBlank(thirdTokenVO.getAccessToken())) {
                        throw new ServiceException("授权失败！");
                    }
                    loginInfoPO.setAccessToken(thirdTokenVO.getAccessToken());
                    loginInfoPO.setTokenExpires(DateUtils.parse(System.currentTimeMillis() + 25 * 1000));
                    loginInfoPO.setRefreshToken(thirdTokenVO.getRefreshToken());
                    loginInfoPO.setUserId(thirdTokenVO.getUserId());
                    systemLoginInfoMapper.updateById(loginInfoPO);
                }
            }
        }

        return loginInfoPOList;
    }
}
