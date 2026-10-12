package com.guhecloud.cube.deliver.project.merchant.controller.system;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.HttpUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WxcodeController {
	
	private static final String API_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 员工小程序
     * @param code
     * @return
     */
	@GetMapping("/public/wx/code-to-openid")
	public JSONObject getWxOpenid(@RequestParam("code") String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("appid", "wxf2e2abc0df1be4cf");
        params.add("secret", "17be69ce2d4f025508014f5e0198e4b0");
        params.add("js_code", code);
        return HttpUtils.restGet(API_URL, params, null);
	}

    /**
     * 商户小程序
     * @param code
     * @return
     */
    @GetMapping("/public/store-wx/code-to-openid")
    public JSONObject getStoreWxOpenid(@RequestParam("code") String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("appid", "wxfe34a9cb2dae74ec");
        params.add("secret", "93b2bdd5b6ccad74bc6c683a599b921e");
        params.add("js_code", code);
        return HttpUtils.restGet(API_URL, params, null);
    }
}
