package com.guhecloud.cube.srv.gen.conferencemanage.server.third.param;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/18 16:40
 * @Description:
 */

@Data
@Builder
public class AttributeParam {

    private Integer width;
    private Integer height;
    private Integer x;
    private Integer y;
    private Integer offsetX;
    private Integer offsetY;
    private Integer duration;
    private Integer refresh;
    private Integer style;
    private String url;
    private String language;

}
