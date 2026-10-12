package com.guhecloud.cube.biz.mdata.client.dto;

import lombok.Data;

/**
 * 银行卡校验配置返回参数
 *
 * @author lanchun
 * @date 2021/12/9 20:01
 */
@Data
public class BankCardValidConfigDTO {

    private Long id;

    /**
     * 第三方APPCODE
     */
    private String appCode;

}
