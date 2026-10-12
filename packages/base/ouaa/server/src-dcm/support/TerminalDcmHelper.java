package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.DcmTerminal;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
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

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_TERMINAL;

public class TerminalDcmHelper extends AbstractGroupedDcmHelper<Terminal, DcmTerminal> {
    public static final String KW_KEY_PRODUCT_CODE = "prodCode";
    private final Map<Long, String> prodCodeById;
    private final Map<String, Long> prodIdByCode;

    public TerminalDcmHelper(List<Product> products) {
        super(Terminal.class, DcmTerminal.class);

        prodCodeById = new HashMap<>();
        prodIdByCode = new HashMap<>();
        for (Product product : products) {
            prodCodeById.put(product.getId(), product.getCode());
            prodIdByCode.put(product.getCode(), product.getId());
        }
    }

    public static IDcmRciConverter<DcmTerminal> createDcmRciConverter() {
        return new TerminalDcmHelper(Collections.emptyList());
    }

    @Override
    public String getBizKey(Terminal bizEntity) {
        return String.format("%s:%s", prodCodeById.get(bizEntity.getProductId()), bizEntity.getCode());
    }

    @Override
    public void updateBizWithDcm(Terminal input, DcmTerminal dcmEntity) {
        input.setProductId(prodIdByCode.get(dcmEntity.getProdCode()));
        input.setName(dcmEntity.getName());
        input.setRemark(dcmEntity.getRemark());
        input.setEnabled(dcmEntity.getEnabled());
    }

    @Override
    protected Object generateGroupKey(DcmTerminal x) {
        Objects.requireNonNull(x.getProdCode(), "GroupCode is NULL");
        return x.getProdCode();
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey, Collection<? extends DcmTerminal> dcmEntities) {
        RequestedConfigItem rci = getRciTemplate();
        rci.setKwargs(KW_KEY_PRODUCT_CODE, (String) groupKey);
        rci.setVargs(new ArrayList<>(dcmEntities));
        return rci;
    }

    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends DcmTerminal> apps, RequestedConfigItem rci) {
        String prodCode = rci.getKwargs(KW_KEY_PRODUCT_CODE, null);
        Objects.requireNonNull(prodCode, "RCI with not valid product code");
        for (DcmTerminal app : apps) {
            app.setProdCode(prodCode);
        }
    }

    @Override
    protected void updateBizGroupInfoWithDcm(Terminal bizEntity, DcmTerminal dcmEntity) {
        Long productId = prodIdByCode.get(dcmEntity.getProdCode());
        Objects.requireNonNull(productId, "no matched product id for product code " + dcmEntity.getProdCode());
        bizEntity.setProductId(prodIdByCode.get(dcmEntity.getProdCode()));
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(DcmTerminal dcmEntity, Terminal bizEntity) {
        String prodCode = prodCodeById.get(bizEntity.getProductId());
        Objects.requireNonNull(prodCode, "Entity's productId is invalid. ");
        dcmEntity.setProdCode(prodCode);
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_TERMINAL;
    }

    @Override
    protected Comparator<DcmTerminal> getDcmComparator() {
        return Comparator
                .comparing(DcmTerminal::getProdCode)
                .thenComparing(DcmTerminal::getCode);
    }
}
