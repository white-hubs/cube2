package com.guhecloud.cube.biz.ouaa.client.message;

import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncreaseTenantUserMessage {
    public final static String TOPIC = "TENANT_USER_INCREASE";

    private Long id;

    private String name;

    private String mobile;

    private Long organizationId;

    private List<Long> positionIds;
    /**
     * 人员照片业务主键
     */
    private Long photographKey;

    private Gender gender;
    private List<Long> appointmentIds;

    private Integer tenant;

    /**
     * 身份证号
     */
    private String idCard;
}
