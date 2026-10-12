package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import lombok.Data;

import java.util.List;

@Data
public class GroupUserPageVO {
    /**
     * 分组id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 项目id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 用户数量
     */
    private Long count;
    /**
     * 用户id列表
     */
    private String userIds;

    /**
     * 成员用户列表
     */
    private List<UserDTO> groupUsers;

}
