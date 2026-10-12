package com.guhecloud.cube.srv.gen.conferencemanage.server.third.param;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/18 16:30
 * @Description:
 */

@Data
public class ProgramParam {

    private String name;

    private String uuid;

    private Integer version;

    private String type;

    private AttributeParam attributes;

    private List<ProgramParam> children;

    private List<ProgramParam> widgets;
}
