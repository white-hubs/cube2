package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.DcmTerminalApp;
import com.guhecloud.cube.biz.ouaa.dcm.support.RmdpHelperSupportRepo.TerminalKey;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.entity.TerminalApplication;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.AbstractGroupedDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_TERMINAL_APP;

public class TerminalAppDcmHelper extends AbstractGroupedDcmHelper<TerminalApplication, DcmTerminalApp> {
    public static final String KW_KEY_PROD_CODE = "prodCode";
    public static final String KW_KEY_TERMINAL_CODE = "terminalCode";
    RmdpHelperSupportRepo supportRepo;

    public TerminalAppDcmHelper(List<Product> productions, List<Terminal> terminals, List<Application> applications) {
        super(TerminalApplication.class, DcmTerminalApp.class);
        supportRepo = new RmdpHelperSupportRepo(productions, Collections.emptyList(), terminals, applications, Collections.emptyList());
    }

    public static IDcmRciConverter<DcmTerminalApp> createDcmRciConverter() {
        return new TerminalAppDcmHelper(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public String getBizKey(TerminalApplication bizEntity) {
        TerminalKey terminalKey = supportRepo.getTerminalKey(bizEntity.getTerminalId());
        String appCode = supportRepo.getAppCode(bizEntity.getApplicationId());
        return String.format("%s:%s:%s:%s",
                             terminalKey.getProdCode(), terminalKey.getTerminalCode(), appCode,
                             bizEntity.getPath() == null ? "" : bizEntity.getPath());
    }

    @Override
    public void updateBizWithDcm(TerminalApplication input, DcmTerminalApp dcmEntity) {
        Long appId = supportRepo.getAppIdByCode(dcmEntity.getAppCode());
        Long terminalId = supportRepo.getTerminalIdByKey(new TerminalKey(dcmEntity.getProdCode(), dcmEntity.getTerminalCode()));
        input.setApplicationId(appId);
        input.setTerminalId(terminalId);
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_TERMINAL_APP;
    }

    @Override
    protected Object generateGroupKey(DcmTerminalApp dcmEntity) {
        return new TerminalKey(dcmEntity.getProdCode(), dcmEntity.getTerminalCode());
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey, Collection<? extends DcmTerminalApp> dcmEntities) {
        RequestedConfigItem rci = getRciTemplate();
        TerminalKey key = (TerminalKey) groupKey;
        rci.setKwargs(KW_KEY_PROD_CODE, key.getProdCode());
        rci.setKwargs(KW_KEY_TERMINAL_CODE, key.getTerminalCode());
        rci.setVargs(new ArrayList<>(dcmEntities));
        return rci;
    }

    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends DcmTerminalApp> dcmEntities, RequestedConfigItem rci) {
        String prodCode = rci.getKwargs(KW_KEY_PROD_CODE, null);
        String terminalCode = rci.getKwargs(KW_KEY_TERMINAL_CODE, null);
        Objects.requireNonNull(prodCode, "prodCode not set");
        Objects.requireNonNull(terminalCode, "terminalCode not set");
        for (DcmTerminalApp entity : dcmEntities) {
            entity.setProdCode(prodCode);
            entity.setTerminalCode(terminalCode);
        }
    }

    @Override
    protected void updateBizGroupInfoWithDcm(TerminalApplication bizEntity, DcmTerminalApp dcmEntity) {
        TerminalKey tk = new TerminalKey(dcmEntity.getProdCode(), dcmEntity.getTerminalCode());
        Long terminalId = supportRepo.getTerminalIdByKey(tk);
        bizEntity.setTerminalId(terminalId);

        Long appId = supportRepo.getAppIdByCode(dcmEntity.getAppCode());
        bizEntity.setApplicationId(appId);
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(DcmTerminalApp dcmEntity, TerminalApplication bizEntity) {
        TerminalKey tk = supportRepo.getTerminalKey(bizEntity.getTerminalId());
        dcmEntity.setProdCode(tk.getProdCode());
        dcmEntity.setTerminalCode(tk.getTerminalCode());
        String appCode = supportRepo.getAppCode(bizEntity.getApplicationId());
        dcmEntity.setAppCode(appCode);
    }

    @Override
    protected Comparator<DcmTerminalApp> getDcmComparator() {
        return Comparator.comparing(DcmTerminalApp::getProdCode)
                .thenComparing(DcmTerminalApp::getTerminalCode)
                .thenComparing(DcmTerminalApp::getAppCode);
    }
}
