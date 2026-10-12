package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public enum RiskLevelEnum {
    MAJOR(4, "红色"),
    LARGE(3, "橙色"),
    GENERAL(2, "黄色"),
    LOW(1, "蓝色");

    private final int weight;
    private final String color;

    RiskLevelEnum(int weight, String color) {
        this.weight = weight;
        this.color = color;
    }

    public int getWeight() {
        return weight;
    }

    public String getColor() {
        return color;
    }

    public static String highest(List<String> levels) {
        return levels.stream()
                .filter(Objects::nonNull)
                .map(RiskLevelEnum::valueOf)
                .max(Comparator.comparingInt(RiskLevelEnum::getWeight))
                .map(Enum::name)
                .orElse(null);
    }
}
