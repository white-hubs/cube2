package com.guhecloud.cube.biz.ouaa.dcm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.platform.dcm.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * role与terminal 都需要从属于一个特定的product. 需要基于product进行分组。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DcmRoleTerminal implements Identifiable {

    /**
     * product code
     */
    @JsonIgnore
    private String prodCode;

    @JsonIgnore
    /** 角色code */
    private String roleName;

    /**
     * 访问端code
     */
    private String terminalCode;

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s:%s:%s", prodCode, roleName, terminalCode);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return "";
    }
}
