package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.valueobject;

/**
 * @author pengjian
 * @date 2022/8/9 10:25
 * @description 健康等级
 * <p>
 * <p>
 * 设置四个级别：最佳健康（80分~100分）、普通健康（50分~80分）、亚健康（20分~50分）以及不健康（20分以下）
 */
public class HealthLevel {
    private HealthLevelEnum healthLevel;

    public HealthLevel(Integer healthValue) {

        if (healthValue >= 80 && healthValue <= 100) {
            this.healthLevel = HealthLevelEnum.BEST;
        }
        if (healthValue >= 50 && healthValue <= 80) {
            this.healthLevel = HealthLevelEnum.NORMAL;
        }
        if (healthValue >= 20 && healthValue <= 50) {
            this.healthLevel = HealthLevelEnum.SUB;
        }
        if (healthValue < 20) {
            this.healthLevel = HealthLevelEnum.UNHEALTHY;
        }
    }

    public HealthLevelEnum getHealthLevel() {
        return healthLevel;
    }

    public enum HealthLevelEnum {
        /**
         * 最佳健康
         */
        BEST,
        /**
         * 普通健康
         */
        NORMAL,
        /**
         * 亚健康
         */
        SUB,
        /**
         * 不健康
         */
        UNHEALTHY
    }
}
