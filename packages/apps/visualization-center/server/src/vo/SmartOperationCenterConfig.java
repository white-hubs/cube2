package com.guhecloud.cube.base.visualization.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/2 14:42
 * @Description:
 */
@Data
public class SmartOperationCenterConfig {
    private String title;
    private String entry;
    private UE5Config ue5;
    private ThreeConfig three;
    private ScadaConfig scada;

    @Data
    public static class UE5Config {
        private List<ModelServer> modelServer;
        private List<ModelPerspective> modelPerspective;
        private List<Perspective> perspective;
        private List<RoamingRoute> roamingRoute;
        private BigDecimal iconScale;
        private String isFilterArea;
        private String navType;
        private String cardType;
        private String deviceStyle;
        private Boolean correct;
    }

    @Data
    public static class ThreeConfig {
        private List<ModelServer> modelServer;
        private List<ModelPerspective> modelPerspective;
        private List<Perspective> perspective;
        private List<RoamingRoute> roamingRoute;
        private BigDecimal iconScale;
        private String isFilterArea;
        private String navType;
        private String cardType;
        private String deviceStyle;
        private Scene scene;
    }

    @Data
    public static class ScadaConfig {
        private List<ScadaUrl> url;
        private List<Background> background;
    }

    @Data
    public static class ModelServer {
        private String name;
        private String url;
        private List<String> area; // UE5专用字段
    }

    @Data
    public static class ModelPerspective {
        private String label;
        private String value;
        private String buildTagLocation;
        private String id;
        private String area;
        private String isDefault;
    }

    @Data
    public static class Perspective {
        private String label;
        private String area;
        private String camera;
        private List<String> hideActors;
        private String buildTagLocation;
        private String isDefault;
        private String id;
    }

    @Data
    public static class RoamingRoute {
        private String name;
        private String id;
        private List<RoutePoint> route;
    }

    @Data
    public static class RoutePoint {
        private String perspective;
        private String pointName;
        private String id;
        private BigDecimal speed;
        private String name;
        private Integer sort;
    }

    @Data
    public static class Scene {
        private BackgroundConfig background;
    }

    @Data
    public static class BackgroundConfig {
        private List<String> images;
        private Double scale;
    }

    @Data
    public static class ScadaUrl {
        private String name;
        private String url;
        private String menu;
        private String area;
        private String id;
    }

    @Data
    public static class Background {
        private String url;
        private String area;
        private String id;
    }
}

