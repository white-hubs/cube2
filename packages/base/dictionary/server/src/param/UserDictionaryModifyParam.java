package com.guhecloud.cube.base.dictionary.server.param;

import com.guhecloud.cube.base.dictionary.server.entity.UserDictionary;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/28 15:00
 * @Description: [{"name":"true","code":"SERVICE_CONFIGURATION","itemCode":"SERVICE_CONFIGURATION","remarks":"指定人员手机号加密","userId":"1993949421025443842"}]
 */
@Data
public class UserDictionaryModifyParam implements Serializable {

    private static final long serialVersionUID = 8595062517115124039L;

    private Long id;

    /**
     * 名称
     */
    @Size(min = 1, max = 50, message = "字典名称只能在1-50个字符")
    private String name;
    /**
     * 编号
     */
    @Size(max = 50, message = "字典编码不能超过50个字符")
    private String code;
    /**
     * 备注
     */
    @Size(max = 255, message = "备注不能超过255个字符")
    private String remarks;

    /**
     * 版本号
     */
    private String version;
    /**
     * 变量
     */
    private String variable;
    /**
     * 状态
     */
    private String status;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 字典项
     */
    private String itemCode;

    public void fillDataDictionary(UserDictionary userDictionary) {
        userDictionary.setName(this.name);
        userDictionary.setRemarks(this.remarks);
        userDictionary.setVariable(this.variable);
        if (ObjectUtils.isNotEmpty(this.status)) {
            userDictionary.setStatus(this.status);
        }
        if (ObjectUtils.isNotEmpty(this.itemCode)) {
            userDictionary.setItemCode(this.itemCode);
        }
        if (ObjectUtils.isNotEmpty(this.code)) {
            userDictionary.setCode(this.code);
        }
        if (ObjectUtils.isNotEmpty(this.version)) {
            userDictionary.setVersion(this.version);
        }
    }
}
