package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XunYiAccount extends AbstractXunYiRequest {

    /**
     * 等级 (默认 10)
     */
    private Integer employeeLevel;

    /**
     * 是否录音：0-否 1-是 (默认 0)
     */
    private Integer isRecord;

    /**
     * 是否录像：0-否 1-是 (默认 0)
     */
    private Integer isVideo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 组号码 多个号码使用英文逗号隔开 (默认 null)
     */
    private String groupNum;

    /**
     * 账号
     */
    private String employeeId;

    /**
     * 密码
     */
    private String password;

    /**
     * 类型(详情看文档)
     */
    private String type;

    /**
     * 名称 (默认=账号)
     */
    private String name;

    @Override
    public String getUrl() {
        return null;
    }
}
