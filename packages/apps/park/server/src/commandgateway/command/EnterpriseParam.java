package com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command;

import com.guhecloud.cube.srv.gen.park.management.server.enumeration.EnterpriseScaleEnum;
import com.guhecloud.cube.srv.gen.park.management.server.enumeration.EnterpriseTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

/**
 * 类型数据
 */
@Getter
public class EnterpriseParam {

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    private String name;

    /**
     * 法人
     */
    @NotEmpty(message = "法人不能为空")
    private String corporation;

    /**
     * 类型
     */
    private EnterpriseTypeEnum type;

    /**
     * 行业类型ID
     */
    private Long industryTypeId;

    /**
     * 规模
     */
    private EnterpriseScaleEnum scale;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系人电话
     */
    private String contactsPhone;

    /**
     * 文件IDs
     */
    private List<Long> fileIds;

}
