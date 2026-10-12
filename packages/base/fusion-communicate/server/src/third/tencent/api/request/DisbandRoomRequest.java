package com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisbandRoomRequest {

    /**
     * 房间id
     */
    private String roomId;
}
