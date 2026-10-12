package com.guhecloud.cube.biz.ouaa.client;

import com.guhecloud.cube.biz.ouaa.client.dto.UserWechatDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserWechatQueryParam;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 用户微信信息
 *
 * @author lanchun
 * @date 2025/1/10 16:25
 */
@FeignClient(name = "ouaa")
public interface UserWechatClient {

    /**
     * 获取用户微信信息列表
     *
     * @param param 查询参数
     */
    @PostMapping("/user/wechat/list")
    List<UserWechatDTO> getUserWechatList(@Valid @RequestBody UserWechatQueryParam param);
}
