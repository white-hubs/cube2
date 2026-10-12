package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import lombok.Data;

import java.util.Collection;

@Data
public class GroupInfoVO {
    /**
     * 组内人员
     */
    private Collection<UserDTO> users;

    private Collection<Long> groupUserIds;

    /**
     * Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 组名称
     */
    private String groupName;
    /**
     * 组内人数
     */
    private Long count;

}