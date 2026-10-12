package com.guhecloud.cube.srv.gen.conferencemanage.server.third.param;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/24 15:18
 * @Description:
 */

@Data
@Builder
public class InstructCommand {

    private String command;

    private List<String> devices;

    private Args args;

    @Data
    public static class Args {
        private Integer displayId;
    }
}
