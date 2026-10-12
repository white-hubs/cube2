package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.handlers;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjVO;

import java.util.Comparator;

/**
 * 自定义巡检对象类型排序(档位>现场点>其他)
 */
public class InspectionObjVOCategoryComparator implements Comparator<InspectionObjVO> {

    @Override
    public int compare(InspectionObjVO o1, InspectionObjVO o2) {
        if (o1.getTargetCategory() == null) {
            return 1;
        }
        if (o1.getTargetCategory().equals(o2.getTargetCategory())) {
            return 0;
        }
        boolean o1IsStall = InspectTargetCategoryEnum.STALL.name().equals(o1.getTargetCategory());
        boolean pointAndNotStall = InspectTargetCategoryEnum.SITE_POINT.name().equals(o1.getTargetCategory()) &&
                                   !InspectTargetCategoryEnum.STALL.name().equals(o2.getTargetCategory());
        if (o1IsStall || pointAndNotStall) {
            return -1;
        }
        return 1;
    }
}
