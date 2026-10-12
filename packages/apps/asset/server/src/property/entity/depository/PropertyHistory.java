package com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.HandleTypeEnum;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 资产使用历史记录;
 *
 * @author : lijie
 * @date : 2023-3-8
 */
@Data
@TableName("property_history")
public class PropertyHistory extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 资产id
     */
    private Long propertyId;
    /**
     * 处理时间
     */
    private LocalDateTime handleTime;
    /**
     * 处理方式id
     */
    private String handleTypeId;
    /**
     * 处理方式名称
     */
    private String handleTypeName;
    /**
     * 处理内容
     */
    private String remarks;
    /**
     * 处理人部门id
     */
    private String departmentId;

    /**
     * 组装履历信息
     *
     * @param handleTypeEnum   处理方式枚举
     * @param userDTO          用户信息
     * @param propertyDetailed 资产信息
     * @param remarks          处理内容
     * @param bool             是否需要拼接
     *
     * @return 履历信息
     */
    public static PropertyHistory getPropertyHistory(HandleTypeEnum handleTypeEnum, UserDTO userDTO, PropertyDetailed propertyDetailed,
                                                     String remarks, Boolean bool
                                                    ) {
        PropertyHistory propertyHistory = new PropertyHistory();
        propertyHistory.setPropertyId(propertyDetailed.getId());
        propertyHistory.setRemarks(userDTO.getOrganization().getName());
        propertyHistory.setHandleTypeName(handleTypeEnum.getRemarks());
        propertyHistory.setHandleTypeId(handleTypeEnum.name());
        propertyHistory.setHandleTime(DateUtils.now());
        if (ObjectUtils.isNotEmpty(userDTO)) {
            if (ObjectUtils.isNotEmpty(userDTO.getOrganization())) {
                propertyHistory.setDepartmentId(userDTO.getOrganization().getId().toString());
            }
        }
        if (bool) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(userDTO.getOrganization().getName());
            buffer.append(userDTO.getName());
            buffer.append(remarks);
            buffer.append(propertyDetailed.getName());
            propertyHistory.setRemarks(buffer.toString());
        } else {
            propertyHistory.setRemarks(remarks);
        }
        return propertyHistory;
    }
}
