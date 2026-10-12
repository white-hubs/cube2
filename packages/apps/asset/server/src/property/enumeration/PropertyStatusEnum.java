package com.guhecloud.cube.srv.gen.property.management.server.property.enumeration;

import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.PropertyApprovalBizEnum;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 资产状态
 *
 * @author : lijie
 * @date : 2023-3-16
 */
@Getter
public enum PropertyStatusEnum {
    UNDER("审批中"),
    LEAVE_UNUSED("闲置"),
    MAKE_USE_OF("在用"),
    WEED_OUT("报废"),
    REPAIR_REPORT("报修"),
    DELETE("已删除"),
    ;
    private final String remarks;

    PropertyStatusEnum(String remarks) {
        this.remarks = remarks;
    }

    public static List<String> getStatusByBusinessType(String businessType, String status) {
        List<String> statusList = new ArrayList<>();
        if (StringUtils.isNotEmpty(status)) {
            statusList.add(status);
        } else if (StringUtils.isEmpty(businessType)) {
            statusList.add(PropertyStatusEnum.LEAVE_UNUSED.name());
            statusList.add(PropertyStatusEnum.MAKE_USE_OF.name());
            statusList.add(PropertyStatusEnum.WEED_OUT.name());
            statusList.add(PropertyStatusEnum.REPAIR_REPORT.name());
        } else if (PropertyApprovalBizEnum.PROPERTY_ALLOT_PERSONAL.name().equals(businessType)) {
            statusList.add(PropertyStatusEnum.LEAVE_UNUSED.name());
        } else if (PropertyApprovalBizEnum.PROPERTY_ALLOT_UNIT.name().equals(businessType)) {
            statusList.add(PropertyStatusEnum.MAKE_USE_OF.name());
        } else if (PropertyApprovalBizEnum.PROPERTY_WEED_OUT.name().equals(businessType)) {
            statusList.add(PropertyStatusEnum.LEAVE_UNUSED.name());
            statusList.add(PropertyStatusEnum.MAKE_USE_OF.name());
        }
        return statusList;
    }

    public static String getRemarks(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        for (PropertyStatusEnum ex : PropertyStatusEnum.values()) {
            if (ex.name().equals(name)) {
                return ex.getRemarks();
            }
        }
        return "";
    }

    public static PropertyStatusEnum getPropertyStatusEnum(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return null;
        }
        for (PropertyStatusEnum ex : PropertyStatusEnum.values()) {
            if (ex.name().equals(name)) {
                return ex;
            }
        }
        return null;
    }
}
