package com.guhecloud.cube.srv.gen.park.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.park.management.server.enumeration.EnterpriseScaleEnum;
import com.guhecloud.cube.srv.gen.park.management.server.enumeration.EnterpriseTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 企业
 * </p>
 *
 * @author zwb
 * @since 2022-09-15
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("enterprise")
public class EnterprisePO extends BasicEnhancedTenantEntity<Long> {
    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 法人
     */
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
     * 房间租赁次数
     */
    private int roomRentTime;

}
