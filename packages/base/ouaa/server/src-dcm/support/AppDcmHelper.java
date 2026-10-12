package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.App;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationGroup;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.AbstractGroupedDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_APP;

@Slf4j
public class AppDcmHelper extends AbstractGroupedDcmHelper<Application, App> {
    public static final String DEFAULT_APP_GROUP = "未分组";
    public static final String KW_KEY_GROUP = "group";

    private final Map<Long, String> existedAppGroupNamesById;

    private final Map<String, Long> existedAppGroupIdsByName;

    public AppDcmHelper(List<ApplicationGroup> existedAppGroups) {
        super(Application.class, App.class);
        existedAppGroupNamesById = existedAppGroups.stream().collect(Collectors.toMap(ApplicationGroup::getId, ApplicationGroup::getName));
        existedAppGroupIdsByName = existedAppGroups.stream().collect(Collectors.toMap(ApplicationGroup::getName, ApplicationGroup::getId));
    }

    public static IDcmRciConverter<App> createDcmRciConverter() {
        return new AppDcmHelper(Collections.emptyList());
    }

    @Override
    public String getBizKey(Application bizEntity) {
        return String.format("%s", bizEntity.getCode());
    }

    @Override
    protected void updateBizGroupInfoWithDcm(Application bizEntity, App dcmEntity) {
        bizEntity.setApplicationGroupId(existedAppGroupIdsByName.get(dcmEntity.getGroup()));
    }

    @Override
    public void updateBizWithDcm(Application application, App app) {
        Objects.requireNonNull(application.getId());
        // code 是逻辑键，不需要更新。
        application.setApplicationGroupId(existedAppGroupIdsByName.get(app.getGroup()));
        application.setName(app.getName());
        application.setIcon(app.getIcon());
        application.setRemark(app.getRemark());
        application.setEnabled(app.getEnabled());
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_APP;
    }

    protected void updateDcmGroupInfoWithRci(Collection<? extends App> apps, RequestedConfigItem rci) {
        String groupName = rci.getKwargs(KW_KEY_GROUP, DEFAULT_APP_GROUP);
        apps.forEach(x -> x.setGroup(groupName));
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(App dcmEntity, Application bizEntity) {
        String groupName = bizEntity.getApplicationGroupId() == null ? null :
                           existedAppGroupNamesById.get(bizEntity.getApplicationGroupId());
        dcmEntity.setGroup(groupName == null ? DEFAULT_APP_GROUP : groupName);
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey, Collection<? extends App> dcmEntities) {
        RequestedConfigItem ret = getRciTemplate();
        ret.setKwargs(KW_KEY_GROUP, (String) groupKey);
        ret.setVargs(new ArrayList<>(dcmEntities));
        return ret;
    }

    @Override
    protected Object generateGroupKey(App dcmEntity) {
        return dcmEntity.getGroup() == null ? DEFAULT_APP_GROUP : dcmEntity.getGroup();
    }

    @Override
    protected Comparator<App> getDcmComparator() {
        return Comparator
                .comparing((App x) -> x.getGroup() == null ? "" : x.getGroup())
                .thenComparing((App x) -> x.getCode() == null ? "" : x.getCode());
    }
}
