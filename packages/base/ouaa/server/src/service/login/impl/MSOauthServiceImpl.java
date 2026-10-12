package com.guhecloud.cube.biz.ouaa.server.service.login.impl;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.base.dictionary.adapter.SysDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.DictionaryEnum;
import com.guhecloud.cube.biz.ouaa.server.dto.login.MSAccesstokenDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.login.MSLoginUserDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.login.MSOauthService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-05  10:19
 * @Description:
 * @Version: 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MSOauthServiceImpl implements MSOauthService {

    private final UserService userService;
    private final SysDictionaryAdapter dictionaryAdapter;

    @Override
    public User getAccessToken(String code, String redirectUri) {
        MSLoginUserDTO msAccesstokenDTO = this.getUser(code, redirectUri);
        return userService.getOne(new LambdaQueryWrapper<User>()
                                          .eq(User::getUsername, msAccesstokenDTO.getId())
                                          .last("limit 1"));
    }

    /**
     * @description: 获取token
     * @author: lijie
     * @date: 2024/12/5 13:41
     * @param: code code码
     * @param: redirectUri 重定向地址
     * @return: MSAccesstokenDTO
     **/
    private MSAccesstokenDTO getMSAccesstokenDTO(String code, String redirectUri) {
        SysDictionaryDTO sysDictionaryDTOList = dictionaryAdapter.getSysDictionaryByCodeAndItemCode(DictionaryEnum.MEI_SHAN_URL_ADDRESS.name(), DictionaryEnum.ACCESS_TOCKEN.name());
        Assert.parameterIsNull(sysDictionaryDTOList, "未配置环天智慧园区数字大脑鉴权地址");
        try {
            String url = sysDictionaryDTOList.getVariable() + "&code=" + code + "&redirect_uri=" + redirectUri;
            String result = HttpUtil.get(url);
            MSAccesstokenDTO msAccesstokenDTO = JsonUtils.deserialize(result, MSAccesstokenDTO.class);
            return msAccesstokenDTO;
        } catch (Exception e) {
            log.error("获取环天智慧园区数字大脑鉴权失败", e);
            throw new RuntimeException("获取环天智慧园区数字大脑鉴权失败");
        }
    }

    public MSLoginUserDTO getUser(String code, String redirectUri) {
        MSAccesstokenDTO accesstokenDTO = this.getMSAccesstokenDTO(code, redirectUri);
        SysDictionaryDTO sysDictionaryDTOList = dictionaryAdapter.getSysDictionaryByCodeAndItemCode(DictionaryEnum.MEI_SHAN_URL_ADDRESS.name(), DictionaryEnum.LOGIN_OAUTH.name());
        Assert.parameterIsNull(sysDictionaryDTOList, "未配置环天智慧园区数字大脑获取用户信息地址");
        try {
            String result = HttpUtil.get(sysDictionaryDTOList.getVariable() + accesstokenDTO.getAccess_token());
            MSLoginUserDTO userDTO = JsonUtils.deserialize(result, MSLoginUserDTO.class);
            return userDTO;
        } catch (Exception e) {
            log.error("获取环天智慧园区数字大脑用户信息失败", e);
            throw new RuntimeException("获取环天智慧园区数字大脑用户信息失败");
        }
    }
}
