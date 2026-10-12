package com.guhecloud.cube.base.iot.product.model.v3.util;

import java.util.UUID;

/**
 * 业务编码生成器
 *
 * 提供产品、属性、事件、方法的业务编码生成
 * v3版本已统一使用IdCodec生成短码,旧方法标记为废弃
 */
public class CodeGenerator {

    /**
     * 生成产品编号
     *
     * 使用IdCodec生成12位Base62短码
     * 示例: 2Nq5zB7xK9m
     *
     * @return 产品编号
     */
    public static String generateProdNumber() {
        return IdCodec.generateShortCode();
    }

    /**
     * 生成属性编号
     *
     * 使用IdCodec生成12位Base62短码
     *
     * @return 属性编号
     */
    public static String generatePropCode() {
        return IdCodec.generateShortCode();
    }

    /**
     * 生成事件编号
     *
     * 使用IdCodec生成12位Base62短码
     *
     * @return 事件编号
     */
    public static String generateEventCode() {
        return IdCodec.generateShortCode();
    }

    /**
     * 生成方法编号
     *
     * 使用IdCodec生成12位Base62短码
     *
     * @return 方法编号
     */
    public static String generateMethodCode() {
        return IdCodec.generateShortCode();
    }

    /**
     * 生成通用编码(旧方案)
     *
     * @param prefix 前缀
     * @return 编码
     * @deprecated 已废弃,请使用 {@link IdCodec#generateShortCode()} 替代
     */
    @Deprecated
    private static String generate(String prefix) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return prefix + "_" + timestamp + "_" + random;
    }

    /**
     * 生成UUID编码(旧方案)
     *
     * @return UUID字符串
     * @deprecated 已废弃,请使用 {@link IdCodec#generateShortCode()} 替代
     */
    @Deprecated
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成版本号
     * 格式: v1.0.0
     *
     * @param major 主版本号
     * @param minor 次版本号
     * @param patch 修订号
     * @return 版本号
     */
    public static String generateVersionNumber(int major, int minor, int patch) {
        return String.format("v%d.%d.%d", major, minor, patch);
    }

    /**
     * 自增版本号
     * 将v1.0.0递增为v1.0.1
     *
     * @param currentVersion 当前版本号
     * @return 新版本号
     */
    public static String incrementVersion(String currentVersion) {
        if (currentVersion == null || !currentVersion.startsWith("v")) {
            return "v1.0.0";
        }

        String[] parts = currentVersion.substring(1).split("\\.");
        if (parts.length != 3) {
            return "v1.0.0";
        }

        try {
            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);
            int patch = Integer.parseInt(parts[2]);

            // 修订号+1
            patch++;

            return generateVersionNumber(major, minor, patch);
        } catch (NumberFormatException e) {
            return "v1.0.0";
        }
    }
}
