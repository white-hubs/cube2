package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.DcmProdApp;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.ProductApplication;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.AbstractGroupedDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_PROD_APP;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.KW_KEY_PROD_CODE;

public class ProdAppDcmHelper extends AbstractGroupedDcmHelper<ProductApplication, DcmProdApp> {
    private final Map<String, Long> prodIdByCode = new HashMap<>();
    private final Map<Long, String> prodCodeById = new HashMap<>();
    private final Map<String, Long> appIdByCode = new HashMap<>();
    private final Map<Long, String> appCodeById = new HashMap<>();

    public ProdAppDcmHelper(List<Product> productions, List<Application> applications) {
        super(ProductApplication.class, DcmProdApp.class);
        for (Product product : productions) {
            prodCodeById.put(product.getId(), product.getCode());
            prodIdByCode.put(product.getCode(), product.getId());
        }
        for (Application application : applications) {
            appCodeById.put(application.getId(), application.getCode());
            appIdByCode.put(application.getCode(), application.getId());
        }
    }

    public static IDcmRciConverter<DcmProdApp> createDcmRciConverter() {
        return new ProdAppDcmHelper(Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public DcmProdApp convertBiz2Dcm(ProductApplication bizEntity) {
        String prodCode = prodCodeById.get(bizEntity.getProductId());
        String appCode = appCodeById.get(bizEntity.getApplicationId());
        Objects.requireNonNull(prodCode, "productId invalid");
        Objects.requireNonNull(appCode, "applicationId invalid");
        return new DcmProdApp(prodCode, appCode);
    }

    @Override
    public String getBizKey(ProductApplication bizEntity) {
        return String.format("%s:%s", prodCodeById.get(bizEntity.getProductId()), appCodeById.get(bizEntity.getApplicationId()));
    }

    @Override
    public void updateBizWithDcm(ProductApplication input, DcmProdApp dcmEntity) {
        // 都是key字段， 不需要更新。
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_PROD_APP;
    }

    @Override
    protected Object generateGroupKey(DcmProdApp dcmEntity) {
        return dcmEntity.getProdCode();
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey, Collection<? extends DcmProdApp> dcmEntities) {
        RequestedConfigItem rci = getRciTemplate();
        rci.setKwargs(KW_KEY_PROD_CODE, (String) groupKey);
        rci.setVargs(new ArrayList<>(dcmEntities));
        return rci;
    }

    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends DcmProdApp> dcmEntities, RequestedConfigItem rci) {
        String prodCode = rci.getKwargs(KW_KEY_PROD_CODE, null);
        Objects.requireNonNull(prodCode, "product code is null");
        dcmEntities.forEach(dcm -> dcm.setProdCode(prodCode));
    }

    @Override
    protected void updateBizGroupInfoWithDcm(ProductApplication bizEntity, DcmProdApp dcmEntity) {
        Long prodId = prodIdByCode.get(dcmEntity.getProdCode());
        Objects.requireNonNull(prodId, "product code invalid " + dcmEntity.getProdCode());
        bizEntity.setProductId(prodId);

        Long appId = appIdByCode.get(dcmEntity.getAppCode());
        Objects.requireNonNull(appId, "app code invalid " + dcmEntity.getAppCode());
        bizEntity.setApplicationId(appId);
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(DcmProdApp dcmEntity, ProductApplication bizEntity) {
        String prodCode = prodCodeById.get(bizEntity.getProductId());
        Objects.requireNonNull(prodCode, "product id invalid");
        dcmEntity.setProdCode(prodCode);

        String appCode = appCodeById.get(bizEntity.getApplicationId());
        Objects.requireNonNull(appCode, "app id invalid");
        dcmEntity.setAppCode(appCode);
    }

    @Override
    protected Comparator<DcmProdApp> getDcmComparator() {
        return Comparator.comparing(DcmProdApp::getProdCode)
                .thenComparing(DcmProdApp::getAppCode);
    }
}
