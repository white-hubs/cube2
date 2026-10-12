package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.DcmPosition;
import com.guhecloud.cube.biz.ouaa.server.entity.Position;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_POSITION;

public class PositionDcmHelper extends AbstractSimpleDcmHelper<Position, DcmPosition> {
    public PositionDcmHelper() {
        super(Position.class, DcmPosition.class);
    }

    public static IDcmRciConverter<DcmPosition> createDcmRciConverter() {
        return new PositionDcmHelper();
    }

    @Override
    public String getBizKey(Position bizEntity) {
        return bizEntity.getName();
    }

    @Override
    public void updateBizWithDcm(Position input, DcmPosition dcmEntity) {
        input.setEnabled(dcmEntity.getEnabled() == null || dcmEntity.getEnabled());
        input.setExtension(dcmEntity.getExtension() == null ? "" : dcmEntity.getExtension());
        input.setSort(dcmEntity.getSort() == null ? Float.valueOf("1.0") : dcmEntity.getSort());
        input.setRemark(dcmEntity.getRemark() == null ? "" : dcmEntity.getRemark());
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_POSITION;
    }

}
