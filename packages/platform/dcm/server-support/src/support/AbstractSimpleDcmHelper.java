package com.guhecloud.platform.dcm.support;

import com.guhecloud.platform.dcm.Identifiable;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractSimpleDcmHelper<BE, DE extends Identifiable> extends AbstractBaseDcmHelper<BE, DE> {
    protected AbstractSimpleDcmHelper(Class<BE> bizClass, Class<DE> dcmClass) {
        super(bizClass, dcmClass);
    }

    @Override
    public Collection<RequestedConfigItem> convertDcm2Rci(Collection<? extends DE> dcmEntities) {
        if (dcmEntities == null || dcmEntities.isEmpty()) {
            return Collections.emptyList();
        }
        RequestedConfigItem ret = getRciTemplate();
        ret.setVargs(new ArrayList<>(dcmEntities));
        return Collections.singletonList(ret);
    }

    @Override
    public Collection<? extends DE> convertRci2Dcm(RequestedConfigItem rci) {
        Objects.requireNonNull(rci);
        Collection<DE> convertedDcms = new ArrayList<>();
        DE converted;
        for (Object object : rci.getVargs()) {
            converted = null;
            try {
                if (dcmClass.isAssignableFrom(object.getClass())) {
                    converted = (DE) object;
                } else if (object instanceof String) {
                    converted = dcmFromString((String) object);
                } else if (object instanceof Map) {
                    converted = dcmFromMap((Map<String, Object>) object);
                }
                if (converted != null) {
                    convertedDcms.add(converted);
                } else {
                    log.warn("convertRci2Dcm: unsupported varg item ignored: {}", object);
                }
            } catch (RuntimeException re) {
                log.warn("convertRci2Dcm: failed to convert entry and ignored: {} to {}", object, dcmClass.getName());
            }
        }
        return convertedDcms;
    }
}
