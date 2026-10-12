package com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.support;

import com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.entity.DcmHiddenDangerCategory;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerCategory;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DcmHiddenDangerCategoryHelper extends AbstractSimpleDcmHelper<HiddenDangerCategory, DcmHiddenDangerCategory> {
    public DcmHiddenDangerCategoryHelper() {
        super(HiddenDangerCategory.class, DcmHiddenDangerCategory.class);
    }

    public static IDcmRciConverter<DcmHiddenDangerCategory> createDcmRciConverter() {
        return new DcmHiddenDangerCategoryHelper();
    }

    @Override
    public String getBizKey(HiddenDangerCategory hiddenDangerCategory) {
        return hiddenDangerCategory.getName();
    }

    @Override
    public void updateBizWithDcm(HiddenDangerCategory input, DcmHiddenDangerCategory dcmEntity) {

    }

    @Override
    protected String getConfigType() {
        return DcmHiddenDangerConstant.CONFIG_TYPE_HIDDEN_DANGER_CATEGORY;
    }
}
