package com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.juhe;

import lombok.Data;

@Data
public class JuHeSendRequestDTO {

    /**
     * appKey
     */
    private String key;

    /**
     * 接收短信的手机号码
     */
    private String mobile;

    /**
     * 短信模板ID
     */
    private Integer tpl_id;

    /**
     * 模板中含有#code#和#name两个变量，则vars={"name":"张三","code":1234},建议用户使用post方式提交
     * Content-Type：application/x-www-form-urlencoded
     */
    private String vars;

    /**
     * 如：#code#=431515，整串值需要urlencode，得到结果为：%23code%23%3d431515。
     * 如果你的变量名或者变量值中带有#&=+中的任意一个特殊符号，请先分别进行utf-8 urlencode编码后再传递
     */
    private String tpl_value;

    /**
     * 扩展码，9位以内的数字，具体模板支持的长度可以提前咨询客服
     */
    private String ext;
}