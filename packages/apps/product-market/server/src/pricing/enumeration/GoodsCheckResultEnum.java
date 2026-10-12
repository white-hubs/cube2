package com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration;

import lombok.Getter;
import com.guhecloud.platform.base.util.StringUtils;

/**
 * 商品检测结果
 */
public enum GoodsCheckResultEnum {

    POS("阳性"),

    NEG("阴性");

    @Getter
    String label;

    GoodsCheckResultEnum(String label) {
        this.label = label;
    }

    // 根据 label 获取对应的 code
    public static String getCodeByLabel(String label) {
        for (GoodsCheckResultEnum result : GoodsCheckResultEnum.values()) {
            if (result.getLabel().equals(label)) {
                return result.name();
            }
        }
        return null; // 如果找不到对应的 label，则返回 null 或抛出异常等处理方式
    }

    public static String getLabelByCode(String code) {
        for (GoodsCheckResultEnum result : GoodsCheckResultEnum.values()) {
            if (!StringUtils.isEmpty(code) && result.name().equals(code)) {
                return result.getLabel();
            }
        }
        return null;
    }
}
