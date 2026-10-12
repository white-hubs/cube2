package com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.support;

import com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.entity.DcmHiddenDangerType;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerCategory;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerType;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.AbstractGroupedDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.support.DcmHiddenDangerConstant.DEFAULT_HIDDEN_DANGER_CATEGORY;
import static com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.support.DcmHiddenDangerConstant.KW_KEY_HIDDEN_DANGER_CATEGORY;

public class DcmHiddenDangerTypeHelper extends AbstractGroupedDcmHelper<HiddenDangerType, DcmHiddenDangerType> {

    private final Map<Long, String> existedHiddenDangerCategoryNamesById;

    private final Map<String, Long> existedHiddenDangerCategoryIdsByName;

    public DcmHiddenDangerTypeHelper(List<HiddenDangerCategory> existedHiddenDangerCategoryList) {
        super(HiddenDangerType.class, DcmHiddenDangerType.class);
        existedHiddenDangerCategoryNamesById = existedHiddenDangerCategoryList.stream()
                .collect(Collectors.toMap(HiddenDangerCategory::getId, HiddenDangerCategory::getName));
        existedHiddenDangerCategoryIdsByName = existedHiddenDangerCategoryList.stream()
                .collect(Collectors.toMap(HiddenDangerCategory::getName, HiddenDangerCategory::getId));
    }

    public static IDcmRciConverter<DcmHiddenDangerType> createDcmRciConverter() {
        return new DcmHiddenDangerTypeHelper(Collections.emptyList());
    }

    @Override
    protected Object generateGroupKey(DcmHiddenDangerType dcmHiddenDangerType) {
        return dcmHiddenDangerType.getHiddenDangerCategory() == null ? DEFAULT_HIDDEN_DANGER_CATEGORY
                                                                     : dcmHiddenDangerType.getHiddenDangerCategory();
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey, Collection<? extends DcmHiddenDangerType> dcmEntities) {
        RequestedConfigItem rci = getRciTemplate();
        rci.setKwargs(KW_KEY_HIDDEN_DANGER_CATEGORY, (String) groupKey);
        rci.setVargs(new ArrayList<>(dcmEntities));
        return rci;
    }

    /**
     * rci的请求数据赋值到DCM中
     *
     * @param dcmEntities
     * @param rci
     */
    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends DcmHiddenDangerType> dcmEntities, RequestedConfigItem rci) {
        String hiddenDangerCategory = rci.getKwargs(KW_KEY_HIDDEN_DANGER_CATEGORY, null);
        Objects.requireNonNull(hiddenDangerCategory, "hiddenDangerCategory is null");
        dcmEntities.forEach(dcm -> dcm.setHiddenDangerCategory(hiddenDangerCategory));
    }

    /**
     * Dcm数据转给Biz实体  字段名相同的数据进行赋值更新
     *
     * @param bizEntity 数据库实体
     * @param dcmEntity Dcm实体
     */
    @Override
    protected void updateBizGroupInfoWithDcm(HiddenDangerType bizEntity, DcmHiddenDangerType dcmEntity) {
        bizEntity.setHiddenDangerCategoryId(existedHiddenDangerCategoryIdsByName.get(dcmEntity.getHiddenDangerCategory()));
        bizEntity.setHiddenDangerCategoryName(dcmEntity.getHiddenDangerCategory());
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(DcmHiddenDangerType dcmEntity, HiddenDangerType bizEntity) {
        String groupName = bizEntity.getHiddenDangerCategoryId() == null ? null :
                           existedHiddenDangerCategoryNamesById.get(bizEntity.getHiddenDangerCategoryId());
        dcmEntity.setHiddenDangerCategory(groupName == null ? DEFAULT_HIDDEN_DANGER_CATEGORY : groupName);
    }

    @Override
    public String getBizKey(HiddenDangerType hiddenDangerType) {
        return String.format("%s:%s", existedHiddenDangerCategoryNamesById.get(hiddenDangerType.getHiddenDangerCategoryId()), hiddenDangerType.getName());
    }

    /**
     * dcm输入实体更新到数据库实体
     *
     * @param input
     * @param dcmEntity
     */
    @Override
    public void updateBizWithDcm(HiddenDangerType input, DcmHiddenDangerType dcmEntity) {
        Objects.requireNonNull(input.getId());
        input.setName(dcmEntity.getName());
    }

    @Override
    protected String getConfigType() {
        return DcmHiddenDangerConstant.CONFIG_TYPE_HIDDEN_DANGER_TYPE;
    }

    @Override
    protected DcmHiddenDangerType dcmFromString(String object) {
        DcmHiddenDangerType dcmHiddenDangerType = new DcmHiddenDangerType();
        dcmHiddenDangerType.setName(object);
        return dcmHiddenDangerType;
    }
}
