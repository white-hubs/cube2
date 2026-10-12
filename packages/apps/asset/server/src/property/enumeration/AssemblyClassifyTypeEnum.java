package com.guhecloud.cube.srv.gen.property.management.server.property.enumeration;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.Getter;

/**
 * 分类类型
 *
 * @author : lijie
 * @date : 2023-3-16
 */
@Getter
public enum AssemblyClassifyTypeEnum {
    ASSET_TYPE("assetType", "资产分类"),
    DEPOSITORY("depository", "仓库"),
    ASSET_SOURCE("assetSource", "资产来源"),
    REASON_FOR_SCRAPPING("reasonForScrapping", "报废原因"),
    ;
    private final String code;
    private final String name;

    AssemblyClassifyTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public AssemblyClassifyTypeEnum of(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (AssemblyClassifyTypeEnum ex : AssemblyClassifyTypeEnum.values()) {
            if (code.equals(ex.code)) {
                return ex;
            }
        }
        return null;
    }
}
