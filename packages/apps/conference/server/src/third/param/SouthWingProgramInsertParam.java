package com.guhecloud.cube.srv.gen.conferencemanage.server.third.param;

import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/14 16:31
 * @Description:
 */
@Data
public class SouthWingProgramInsertParam {
    private String id;
    private String type;
    private String layout;
    private String width;
    private String height;
    private String homeId;
    private String resourceId;
    private List<LayoutElement> elements;

    @Data
    public static class LayoutElement {
        private String id;
        private String type;
        private String left;
        private String top;
        private String width;
        private String height;
        private String len;
        private String times;
        private String fixed;
        private String start;
        private String flex;
        private String name;
        private String resolution;
        private String scale;
        private String isScrollBar;
        private List<WebItem> items;
    }

    @Data
    public static class WebItem {
        private String id;
        private String url;
        private String len;
    }

    /**
     * {
     * "id": "0be6f36a-7714-4f4b-9f57-592de7aa158a",
     * "type": "web",
     * "left": "0",
     * "top": "0",
     * "width": "1920",
     * "height": "1080",
     * "len": "3600",
     * "times": "1",
     * "fixed": "false",
     * "start": "08:00:00",
     * "flex": "1",
     * "name": "web-0",
     * "resolution": "640x360",
     * "scale": "100",
     * "isScrollBar": "false",
     * "items": [{
     * "id": "22a8204f-a6b7-42a2-9517-b01bb563300e",
     * "url": "http://192.168.119.2:40201/apps/@ghcloud/cube-meet-room-data-viz/#/entry?id=1920744461765771266",
     * "len": "180"
     * }]    * 	}
     */
    public static LayoutElement buildSouthWingProgramElement(List<WebItem> webItems) {
        LayoutElement element = new LayoutElement();
        element.setId(UUID.randomUUID().toString());
        element.setType("web");
        element.setLeft("0");
        element.setTop("0");
        element.setWidth("1920");
        element.setHeight("1080");
        element.setLen("3600");
        element.setTimes("1");
        element.setFixed("false");
        element.setStart("08:00:00");
        element.setFlex("1");
        element.setName("web-0");
        element.setResolution("640x360");
        element.setScale("100");
        element.setIsScrollBar("false");
        element.setItems(webItems);
        return element;
    }

}

