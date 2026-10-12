package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.DcmTenantApp;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantApplication;
import com.guhecloud.cube.biz.ouaa.server.service.TenantApplicationService;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.AbstractGroupedDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.KW_KEY_PROD_CODE;

public class TenantAppDcmHelper extends AbstractGroupedDcmHelper<TenantApplication, DcmTenantApp> {
    private final RmdpHelperSupportRepo supportRepo;
    private final TenantApplicationService service;

    public TenantAppDcmHelper(List<Product> products, List<Application> applications, TenantApplicationService service) {
        super(TenantApplication.class, DcmTenantApp.class);
        supportRepo = new RmdpHelperSupportRepo(products, Collections.emptyList(),
                                                Collections.emptyList(), applications, Collections.emptyList());
        this.service = service;
    }

    @Override
    protected boolean isTenanted() {
        return true;
    }

    @Override
    protected String getTenantInfo(TenantApplication entity) {
        return String.valueOf(entity.getTenant());
    }

    @Override
    public String getBizKey(TenantApplication bizEntity) {
        return String.format("%s:%s", supportRepo.getProductCode(bizEntity.getProductId()), supportRepo.getAppCode(bizEntity.getApplicationId()));
    }

    @Override
    public void updateBizWithDcm(TenantApplication input, DcmTenantApp dcmEntity) {
        // nothing should be done
    }

    @Override
    public DcmTenantApp convertBiz2Dcm(TenantApplication bizEntity) {
        DcmTenantApp dcmTenantApp = new DcmTenantApp();
        dcmTenantApp.setProdCode(supportRepo.getProductCode(bizEntity.getProductId()));
        dcmTenantApp.setAppCode(supportRepo.getAppCode(bizEntity.getApplicationId()));
        return dcmTenantApp;
    }

    @Override
    public TenantApplication convertDcm2Biz(DcmTenantApp dcmEntity) {
        TenantApplication tenantApplication = new TenantApplication();
        tenantApplication.setApplicationId(supportRepo.getAppIdByCode(dcmEntity.getAppCode()));
        tenantApplication.setProductId(supportRepo.getProductId(dcmEntity.getProdCode()));
        return tenantApplication;
    }

    @Override
    protected Object generateGroupKey(DcmTenantApp dcmEntity) {
        return dcmEntity.getProdCode();
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey, Collection<? extends DcmTenantApp> dcmEntities) {
        RequestedConfigItem rci = getRciTemplate();
        rci.setKwargs(KW_KEY_PROD_CODE, (String) groupKey);
        rci.setVargs(new ArrayList<>(dcmEntities));
        return rci;
    }

    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends DcmTenantApp> dcmEntities, RequestedConfigItem rci) {
        String prodCode = rci.getKwargs(KW_KEY_PROD_CODE, "");
        if (prodCode.isEmpty()) {
            throw new RuntimeException("ProductCode not specified or empty");
        }
        for (DcmTenantApp dcmEntity : dcmEntities) {
            dcmEntity.setProdCode(prodCode);
        }
    }

    @Override
    protected void updateBizGroupInfoWithDcm(TenantApplication bizEntity, DcmTenantApp dcmEntity) {
        Long productId = supportRepo.getProductId(dcmEntity.getProdCode());
        bizEntity.setProductId(productId);
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(DcmTenantApp dcmEntity, TenantApplication bizEntity) {
        dcmEntity.setProdCode(supportRepo.getProductCode(bizEntity.getProductId()));
    }

    @Override
    protected String getConfigType() {
        return OuaaDcmSupport.CONFIG_TYPE_TENANT_APP;
    }

    public Integer batchAdd(Collection<TenantApplication> bizEntities, Collection<DcmTenantApp> dcmEntities,
                            Integer tenant
                           ) {
        for (TenantApplication bizEntity : bizEntities) {
            bizEntity.setTenant(tenant);
        }
        service.saveBatch(bizEntities);
        return bizEntities.size();
    }

    public Integer batchUpdate(Collection<TenantApplication> bizEntities, Collection<DcmTenantApp> dcmEntities,
                               Integer tenant
                              ) {
        if (bizEntities.size() > 0) {
            throw new UnsupportedOperationException("NO update should happen.");
        }
        return bizEntities.size();
    }

    public Integer batchRemove(Collection<TenantApplication> bizEntities, Collection<DcmTenantApp> dcmEntities,
                               Integer tenant
                              ) {
        if (bizEntities.size() == 0) {
            return 0;
        }
        Collection<Long> ids = bizEntities.stream().map(TenantApplication::getId).collect(Collectors.toList());
        service.removeByIds(ids);
        return ids.size();
    }

    public static IDcmRciConverter<DcmTenantApp> createDcmRciConverter() {
        return new TenantAppDcmHelper(Collections.emptyList(), Collections.emptyList(), null);
    }

    @Override
    protected Comparator<DcmTenantApp> getDcmComparator() {
        return Comparator.comparing(DcmTenantApp::getProdCode)
                .thenComparing(DcmTenantApp::getAppCode);
    }
}
