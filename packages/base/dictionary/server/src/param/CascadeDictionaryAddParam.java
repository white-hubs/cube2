package com.guhecloud.cube.base.dictionary.server.param;

import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Optional;

@Data
public class CascadeDictionaryAddParam {
    /**
     * 名称
     */
    @NotEmpty(message = "字典名称不能为空")
    @Size(min = 1, max = 50, message = "字典名称只能在1-50个字符")
    private String name;
    /**
     * 编号
     */
    @Size(max = 100, message = "字典编码只能在1-100个字符")
    private String code;
    /**
     * 备注
     */
    @Size(max = 255, message = "备注不能超过255个字符")
    private String remarks;
    /**
     * 版本号
     */
    @Size(max = 20, message = "版本号不能超过20个字符")
    private String version;
    /**
     * 变量
     */
    private String variable;
    /**
     * 字典项编码
     */
    @NotEmpty(message = "字典项不能为空")
    private String itemCode;
    /**
     * 级别
     */
    private Integer level;
    /**
     * 父id
     */
    private Long superiorsId;
    /**
     * 状态
     */
    private String status;
    /**
     * 数据权限
     */
    private String permissionState;

    public CascadeTreeParam getCascadeTreeParam(Long id) {
        CascadeTreeParam treeParam = new CascadeTreeParam();
        treeParam.setDictionaryId(id);
        treeParam.setSuperiorsIds(ObjectUtils.isNotEmpty(superiorsId) ? superiorsId.toString() : null);
        treeParam.setLevel(Optional.ofNullable(this.level).orElse(1));
        return treeParam;
    }
}
