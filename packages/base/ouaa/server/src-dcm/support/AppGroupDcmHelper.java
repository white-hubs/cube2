package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.AppGroup;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationGroup;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_APP_GROUP;

@Slf4j
public class AppGroupDcmHelper extends AbstractSimpleDcmHelper<ApplicationGroup, AppGroup> {
    public AppGroupDcmHelper() {
        super(ApplicationGroup.class, AppGroup.class);
    }

    public static IDcmRciConverter<AppGroup> createDcmRciConverter() {
        return new AppGroupDcmHelper();
    }

    @Override
    public String getBizKey(ApplicationGroup bizEntity) {
        return bizEntity.getName();
    }

    @Override
    protected AppGroup dcmFromMap(Map<String, Object> object) {
        return AppGroup.from(object);
    }

    @Override
    protected AppGroup dcmFromString(String object) {
        return AppGroup.builder().name(object).build();
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_APP_GROUP;
    }

    @Override
    public void updateBizWithDcm(ApplicationGroup group, AppGroup appGroup) {
        Objects.requireNonNull(group.getId());
        group.setRemark(appGroup.getRemark());
    }

    @Override
    protected Comparator<AppGroup> getDcmComparator() {
        return Comparator.comparing(AppGroup::getName);
    }
}
