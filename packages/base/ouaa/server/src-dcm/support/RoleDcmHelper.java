package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.DcmRole;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.Role;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.AbstractGroupedDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_ROLE;

public class RoleDcmHelper extends AbstractGroupedDcmHelper<Role, DcmRole> {
    public static final String KW_KEY_PRODUCT = "prodCode";
    private Map<String, Long> productIdByCode;
    private Map<Long, String> prodCodeById;

    public RoleDcmHelper(List<Product> products) {
        super(Role.class, DcmRole.class);
        prodCodeById = products.stream().collect(Collectors.toMap(Product::getId, Product::getCode));
        productIdByCode = products.stream().collect(Collectors.toMap(Product::getCode, Product::getId));
    }

    public static IDcmRciConverter<DcmRole> createDcmRciConverter() {
        return new RoleDcmHelper(Collections.emptyList());
    }

    @Override
    public String getBizKey(Role bizEntity) {
        Objects.requireNonNull(bizEntity.getProductId(), "ProductId cannot be null for Role");
        String prodCode = prodCodeById.get(bizEntity.getProductId());
        Objects.requireNonNull(prodCode, "no product code found for id " + bizEntity.getProductId());
        return String.format("%s:%s", prodCode, bizEntity.getName());
    }

    @Override
    public void updateBizWithDcm(Role input, DcmRole dcmEntity) {
        input.setName(dcmEntity.getName());
        input.setIcon(dcmEntity.getIcon() == null ? "" : dcmEntity.getIcon());
        input.setBuiltin(dcmEntity.getBuiltin() == null ? Boolean.TRUE : dcmEntity.getBuiltin());
        input.setSort(dcmEntity.getSort() == null ? Float.valueOf("1.0") : dcmEntity.getSort());
        input.setRemark(dcmEntity.getRemark() == null ? "" : dcmEntity.getRemark());
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_ROLE;
    }

    @Override
    protected Object generateGroupKey(DcmRole dcmEntity) {
        return dcmEntity.getProdCode();
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey, Collection<? extends DcmRole> dcmEntities) {
        RequestedConfigItem ret = getRciTemplate();
        ret.setKwargs(KW_KEY_PRODUCT, (String) groupKey);
        ret.setVargs(dcmEntities.stream().collect(Collectors.toList()));
        return ret;
    }

    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends DcmRole> dcmEntities, RequestedConfigItem rci) {
        dcmEntities.forEach(dcmEntity ->
                                    dcmEntity.setProdCode(rci.getKwargs().get(KW_KEY_PRODUCT)));
    }

    @Override
    protected void updateBizGroupInfoWithDcm(Role bizEntity, DcmRole dcmEntity) {
        Long productId = productIdByCode.get(dcmEntity.getProdCode());
        Objects.requireNonNull(productId, "no product entity found for product code " + dcmEntity.getProdCode());
        bizEntity.setProductId(productId);
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(DcmRole dcmEntity, Role bizEntity) {
        Objects.requireNonNull(bizEntity.getProductId(), "Role with null productId");
        String prodCode = prodCodeById.get(bizEntity.getProductId());
        Objects.requireNonNull(prodCode, "invalid product id " + bizEntity.getProductId());
        dcmEntity.setProdCode(prodCode);
    }

    @Override
    protected Comparator<DcmRole> getDcmComparator() {
        return Comparator.comparing(DcmRole::getProdCode).thenComparing(DcmRole::getName);
    }
}
