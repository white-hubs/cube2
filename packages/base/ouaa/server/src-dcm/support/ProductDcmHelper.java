package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.DcmProduct;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_PRODUCT;

/**
 * Helper for Product Configuration.
 */
public class ProductDcmHelper extends AbstractSimpleDcmHelper<Product, DcmProduct> {
    public ProductDcmHelper() {
        super(Product.class, DcmProduct.class);
    }

    public static IDcmRciConverter<?> createDcmRciConverter() {
        return new ProductDcmHelper();
    }

    @Override
    public String getBizKey(Product bizEntity) {
        return bizEntity.getCode();
    }

    @Override
    public void updateBizWithDcm(Product input, DcmProduct dcmEntity) {
        input.setName(dcmEntity.getName());
        input.setDomain(dcmEntity.getDomain() == null ? "" : dcmEntity.getDomain());
        input.setEnabled(dcmEntity.getEnabled());
        input.setRemark(dcmEntity.getRemark());
    }

    @Override
    public Product convertDcm2Biz(DcmProduct dcmEntity) {
        Product product = super.convertDcm2Biz(dcmEntity);
        if (product != null && product.getDomain() == null) {
            product.setDomain("");
        }
        return product;
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_PRODUCT;
    }
}
