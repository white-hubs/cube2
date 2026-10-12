package com.guhecloud.cube.base.iot.product.model.v3.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 品类级联工具类
 * 用于生成品类的级联名称,如: "家电 > 厨房电器 > 电饭煲"
 */
@Slf4j
public class CategoryChainUtil {

    /**
     * 生成品类级联名称
     *
     * @param categoryId 品类ID
     * @param categoryName 品类名称
     * @param parentIds 父级ID列表(从根到当前节点)
     * @param parentNames 父级名称列表(从根到当前节点)
     * @return 级联名称,如: "家电 > 厨房电器 > 电饭煲"
     */
    public static String generateCategoryChain(Long categoryId, String categoryName,
                                               List<Long> parentIds, List<String> parentNames) {
        if (categoryName == null) {
            return "";
        }

        // 如果没有父级,直接返回当前名称
        if (parentNames == null || parentNames.isEmpty()) {
            return categoryName;
        }

        // 构建级联名称
        List<String> chain = new ArrayList<>(parentNames);
        chain.add(categoryName);

        return String.join(" > ", chain);
    }

    /**
     * 生成品类级联名称(简化版)
     *
     * @param categoryNames 品类名称列表(从根到当前节点)
     * @return 级联名称
     */
    public static String generateCategoryChain(List<String> categoryNames) {
        if (categoryNames == null || categoryNames.isEmpty()) {
            return "";
        }

        return String.join(" > ", categoryNames);
    }

    /**
     * 解析品类级联名称
     *
     * @param chainName 级联名称,如: "家电 > 厨房电器 > 电饭煲"
     * @return 品类名称列表
     */
    public static List<String> parseCategoryChain(String chainName) {
        List<String> result = new ArrayList<>();

        if (chainName == null || chainName.isEmpty()) {
            return result;
        }

        String[] parts = chainName.split("\\s*>\\s*");
        for (String part : parts) {
            if (!part.trim().isEmpty()) {
                result.add(part.trim());
            }
        }

        return result;
    }

    /**
     * 获取品类链中的叶子节点名称
     *
     * @param chainName 级联名称
     * @return 叶子节点名称
     */
    public static String getLeafCategoryName(String chainName) {
        if (chainName == null || chainName.isEmpty()) {
            return "";
        }

        List<String> names = parseCategoryChain(chainName);
        if (names.isEmpty()) {
            return "";
        }

        return names.get(names.size() - 1);
    }

    /**
     * 获取品类链的层级深度
     *
     * @param chainName 级联名称
     * @return 层级深度
     */
    public static int getCategoryDepth(String chainName) {
        if (chainName == null || chainName.isEmpty()) {
            return 0;
        }

        return parseCategoryChain(chainName).size();
    }
}
