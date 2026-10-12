package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.DcmTenantProduct;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantProduct;
import com.guhecloud.cube.biz.ouaa.server.service.TenantProductService;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TenantProductDcmHelper extends AbstractSimpleDcmHelper<TenantProduct, DcmTenantProduct> {
    private final RmdpHelperSupportRepo supportRepo;
    private final TenantProductService tenantProductService;

    public TenantProductDcmHelper(Collection<Product> products, TenantProductService tenantProductService) {
        super(TenantProduct.class, DcmTenantProduct.class);
        supportRepo = new RmdpHelperSupportRepo(products, Collections.emptyList(),
                                                Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        this.tenantProductService = tenantProductService;
    }

    public static IDcmRciConverter<DcmTenantProduct> createDcmRciConverter() {
        return new TenantProductDcmHelper(Collections.emptyList(), null);
    }

    @Override
    protected boolean isTenanted() {
        return true;
    }

    @Override
    protected String getTenantInfo(TenantProduct entity) {
        return String.valueOf(entity.getTenant());
    }

    @Override
    public DcmTenantProduct convertBiz2Dcm(TenantProduct bizEntity) {
        DcmTenantProduct dcmTenantProduct = new DcmTenantProduct();
        dcmTenantProduct.setProdCode(supportRepo.getProductCode(bizEntity.getProductId()));
        return dcmTenantProduct;
    }

    @Override
    public TenantProduct convertDcm2Biz(DcmTenantProduct dcmEntity) {
        TenantProduct tenantProduct = new TenantProduct();
        tenantProduct.setProductId(supportRepo.getProductId(dcmEntity.getProdCode()));
        return tenantProduct;
    }

    @Override
    public String getBizKey(TenantProduct bizEntity) {
        return supportRepo.getProductCode(bizEntity.getProductId());
    }

    @Override
    public void updateBizWithDcm(TenantProduct input, DcmTenantProduct dcmEntity) {
        // if we already know this input is matched with dcmEntity, then nothing should be done
    }

    @Override
    protected String getConfigType() {
        return OuaaDcmSupport.CONFIG_TYPE_TENANT_PROD;
    }

    public int batchRemove(Collection<TenantProduct> bizEntities, Collection<DcmTenantProduct> dcmEntities, Long tenantId) {
        log.debug("batch remove {} bizEntities/{} dcmEntities.", bizEntities.size(), dcmEntities.size());
        List<Long> prodIds2Remove = bizEntities.stream().map(TenantProduct::getId).collect(Collectors.toList());
        tenantProductService.removeByIds(prodIds2Remove);
        log.debug("batch removed {} records", bizEntities.size());
        return prodIds2Remove.size();
    }

    public int batchAdd(Collection<TenantProduct> bizEntities, Collection<DcmTenantProduct> dcmEntities, Long tenantId) {
        if (bizEntities.size() == 0) {
            return 0;
        }
        log.debug("batch add {} bizEntities/{} dcmEntities.", bizEntities.size(), dcmEntities.size());
        // 批量新增
        Integer tenant = tenantId.intValue();
        for (TenantProduct product : bizEntities) {
            product.setTenant(tenant);
        }
        tenantProductService.saveBatch(bizEntities, 100);
        return bizEntities.size();
    }

    public int batchUpdate(Collection<TenantProduct> bizEntities, Collection<DcmTenantProduct> dcmEntities, Long tenantId) {
        if (bizEntities.size() > 0) {
            throw new UnsupportedOperationException("There should have not updates");
        }
        // no need to update
        return 0;
    }
}
