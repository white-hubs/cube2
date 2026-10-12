package com.guhecloud.cube.base.dictionary.server.param;

import com.guhecloud.cube.base.dictionary.server.entity.CascadeDictionary;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Optional;

@Data
public class CascadeDictionaryUpdateParam {
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
     * 数据权限。
     */
    private String permissionState;

    /**
     * 版本号
     */
    private String version;
    /**
     * 变量
     */
    private String variable;
    /**
     * 级别
     */
    private Integer level;
    /**
     * 父id
     */
    private Long superiorsId;

    public void fillDataDictionary(CascadeDictionary cascadeDictionary) {
        cascadeDictionary.setName(this.name);
        cascadeDictionary.setRemarks(this.remarks);
        cascadeDictionary.setVariable(this.variable);
        cascadeDictionary.setLevel(Optional.ofNullable(this.level).orElse(1));
        cascadeDictionary.setSuperiorsId(Optional.ofNullable(this.superiorsId).orElse(0L));
        if (ObjectUtils.isNotEmpty(this.code)) {
            cascadeDictionary.setCode(this.code);
        }
        if (ObjectUtils.isNotEmpty(this.version)) {
            cascadeDictionary.setVersion(this.version);
        }

    }

    public CascadeTreeParam getCascadeTreeParam() {
        CascadeTreeParam treeParam = new CascadeTreeParam();
        treeParam.setDictionaryId(this.id);
        treeParam.setSuperiorsIds(ObjectUtils.isNotEmpty(superiorsId) ? superiorsId.toString() : null);
        treeParam.setLevel(this.level);
        return treeParam;
    }
}
