package com.guhecloud.cube.iot.mgt.dcm.support;

import com.guhecloud.cube.iot.mgt.base.model.SysDict;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;
import com.guhecloud.cube.iot.mgt.dcm.entity.DictType;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;

import static com.guhecloud.cube.iot.mgt.dcm.support.IotDcmSupport.CONFIG_TYPE_DICT_TYPE;

public class DictTypeDcmHelper extends AbstractSimpleDcmHelper<SysDict, DictType> {
    private final PlatformEvnCom platformEvn;

    public DictTypeDcmHelper(PlatformEvnCom platformEvn) {
        super(SysDict.class, DictType.class);
        this.platformEvn = platformEvn;
    }

    @Override
    public String getBizKey(SysDict bizEntity) {
        return bizEntity.getDictCode();
    }

    @Override
    public void updateBizWithDcm(SysDict input, DictType dcmEntity) {
        input.setDictName(dcmEntity.getName());
        // DictCode already matched & other fields should be updated automatically
    }

    @Override
    public DictType convertBiz2Dcm(SysDict bizEntity) {
        DictType dict = super.convertBiz2Dcm(bizEntity);
        if (dict != null) {
            dict.setName(bizEntity.getDictName());
            dict.setCode(bizEntity.getDictCode());
        }
        return dict;
    }

    @Override
    public SysDict convertDcm2Biz(DictType dcmEntity) {
        SysDict ret = super.convertDcm2Biz(dcmEntity);
        InnerDicEnum enumValue = InnerDicEnum.getEnumByValue(dcmEntity.getCode());
        if (enumValue != null) {
            ret.setId(enumValue.id);
        } else {
            ret.setId(platformEvn.nextSnowflakeId());
        }
        ret.setDictName(dcmEntity.getName());
        ret.setDictCode(dcmEntity.getCode());
        return ret;
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_DICT_TYPE;
    }

    @Override
    protected DictType dcmFromString(String object) {
        DictType ret = super.dcmFromString(object);
        if (ret == null) {
            DictType dictType = new DictType();
            dictType.setName(object);
            dictType.setCode(object);
            //ret = dictType;
            ret = DictType.builder().name(object).code(object).build();
        }
        return ret;
    }
}
