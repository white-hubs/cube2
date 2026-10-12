package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.RoomScreenConfig;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/19 19:08
 * @Description: 会议屏配置VO
 */

@Data
public class RoomScreenConfigVO extends RoomScreenConfig {

    private String conferenceName;
}
