package com.guhecloud.cube.base.dictionary.server.param;

import com.guhecloud.cube.base.dictionary.server.entity.DataDictionary;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DataDictionaryParam {
    @NotNull(message = "id不能为空")
    private Long id;
    /**
     * 名称
     */
    @NotEmpty(message = "字典名称不能为空")
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

    public void fillDataDictionary(DataDictionary dataDictionary) {
        dataDictionary.setName(this.name);
        dataDictionary.setRemarks(this.remarks);
        dataDictionary.setVariable(this.variable);
        if (ObjectUtils.isNotEmpty(this.code)) {
            dataDictionary.setCode(this.code);
        }
        if (ObjectUtils.isNotEmpty(this.version)) {
            dataDictionary.setVersion(this.version);
        }
    }
}
