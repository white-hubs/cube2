package com.guhecloud.platform.security.permission.model;

import com.guhecloud.platform.security.model.Principal;
import lombok.Data;

@Data
public class PrincipalPermission {

    /**
     * 主体主键
     */
    private String principalId;

    /**
     * 主体类型
     */
    private Principal.Type principalType;

}
