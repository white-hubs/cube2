package com.guhecloud.cube.srv.gen.adapter.information.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientHttpVo {
    //平台的ip端口
    private String host;
    //密钥appkey
    private String appKey;
    //密钥appSecret
    private String appSecret;

    private String pwd;

    private String account;

    private String companyId;

}
