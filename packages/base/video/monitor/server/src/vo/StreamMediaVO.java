package com.guhecloud.cube.biz.vmonitor.server.vo;

import com.guhecloud.cube.biz.mdata.client.dto.StreamMediaConfigDTO;
import lombok.Data;

import java.util.Objects;

/**
 * 流媒体信息
 *
 * @author lanchun
 * @date 2024/1/18 15:31
 */
@Data
public class StreamMediaVO {

    private Long steamMediaConfigId;

    private String steamMediaName;

    private String steamMediaIpAddress;

    private String steamMediaIntranetAddress;

    public static StreamMediaVO convertStreamMediaInfo(StreamMediaConfigDTO streamMediaConfigDTO) {
        if (Objects.nonNull(streamMediaConfigDTO)) {
            StreamMediaVO streamMediaVO = new StreamMediaVO();
            streamMediaVO.setSteamMediaConfigId(streamMediaConfigDTO.getId());
            streamMediaVO.setSteamMediaIntranetAddress(streamMediaConfigDTO.getIntranetAddress());
            streamMediaVO.setSteamMediaIpAddress(streamMediaConfigDTO.getIpAddress());
            streamMediaVO.setSteamMediaName(streamMediaConfigDTO.getName());
            return streamMediaVO;
        }
        return null;
    }

}
