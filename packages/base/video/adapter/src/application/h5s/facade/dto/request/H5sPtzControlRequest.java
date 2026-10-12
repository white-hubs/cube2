package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class H5sPtzControlRequest extends BasicRequest {

    /**
     * 必须
     * 云台指令
     * up/upright/upleft/down/downrig
     * ht/downleft/left/right/zoomin/zoo
     * mout/focusin/focusout/irisin/iriso
     * ut/stop/preset/lighton/lightoff/wi
     * peron/wiperoff
     */
    private String action;

    /**
     * 必须
     * stop 指令可以不带speed 范围为0-1
     * 的小数
     */
    private Double speed;

    /**
     * 视频源token
     */
    private String token;

    /**
     * 可选
     * 当action为preset生效，预置点的
     * token 支持1 2 3 4 5… 类的数字
     */
    private String preset;

    @Override
    public String getUrl() {
        return getUrl("/api/v1/Ptz")
                .append("?action={action}")
                .append("&speed={speed}")
                .append("&token={token}")
                .append("&preset={preset}")
                .append("&session={session}")
                .toString();
    }
}
