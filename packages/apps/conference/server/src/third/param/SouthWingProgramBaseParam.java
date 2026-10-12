package com.guhecloud.cube.srv.gen.conferencemanage.server.third.param;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/14 16:29
 * @Description:
 */
@Data
public class SouthWingProgramBaseParam {
    /**
     * 节目名称
     */
    private String name;

    /**
     * 节目的类型，n代表普通节目，l代表复合节目，i表示互动节目
     */
    private String state;

    /**
     * 节目的标签id
     */
    private String categoryId;

    /**
     * 节目内容
     */
    private String json;
}
