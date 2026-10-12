package com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 保存资料待完善的员工
 */
@Getter
@Setter
@Builder
public class SaveToBePerfectCommand {
    private Long userId;
    private String name;
    private String mobile;
    private String gender;
    private Long organizationId;
    private List<Long> postIds;
    private List<Long> appointmentIds;
    /**
     * 身份证号
     */
    private String idCard;
}
