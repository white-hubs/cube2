package com.guhecloud.cube.biz.ouaa.server.dto.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WechatUserInfoResponseDTO {
    private Integer errcode;
    private String errmsg;
    private String openid;
    private String nickname;
    private Integer sex;
    private String language;
    private String city;
    private String province;
    private String country;
    @JsonProperty("headimgurl")
    private String headImgUrl;
    private List<String> privilege;
    private String unionid;
}
