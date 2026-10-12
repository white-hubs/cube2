package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.DcmCard;
import com.guhecloud.cube.biz.ouaa.dcm.support.RmdpHelperSupportRepo.TerminalKey;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Card;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.AbstractGroupedDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_CARD;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.KW_KEY_APP_CODE;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.KW_KEY_PROD_CODE;

public class CardDcmHelper extends AbstractGroupedDcmHelper<Card, DcmCard> {

    private RmdpHelperSupportRepo supportRepo;

    public CardDcmHelper(Collection<Product> products, Collection<Terminal> terminals,
                         Collection<Application> applications
                        ) {
        super(Card.class, DcmCard.class);
        supportRepo = new RmdpHelperSupportRepo(products, Collections.emptyList(), terminals, applications,
                                                Collections.emptyList());
    }

    public static IDcmRciConverter<DcmCard> createDcmRciConverter() {
        return new CardDcmHelper(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public String getBizKey(Card bizEntity) {
        TerminalKey terminalKey = supportRepo.getTerminalKey(bizEntity.getTerminalId());
        String appCode = supportRepo.getAppCode(bizEntity.getApplicationId());
        return String.format("%s:%s:%s:%s",
                             terminalKey.getProdCode(),
                             terminalKey.getTerminalCode(),
                             appCode,
                             bizEntity.getCode()
                            );
    }

    @Override
    public void updateBizWithDcm(Card input, DcmCard dcmEntity) {
        input.setName(dcmEntity.getName());
        input.setEnabled(dcmEntity.getEnabled());
        input.setRemark(dcmEntity.getRemark());
    }

    protected String getConfigType() {
        return CONFIG_TYPE_CARD;
    }

    @Override
    protected Object generateGroupKey(DcmCard dcmEntity) {
        return GroupKey.builder()
                .prodCode(dcmEntity.getProdCode())
                .terminalCode(dcmEntity.getTerminalCode())
                .appCode(dcmEntity.getAppCode())
                .build();
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey, Collection<? extends DcmCard> dcmEntities) {
        RequestedConfigItem rci = getRciTemplate();
        rci.setKwargs(KW_KEY_PROD_CODE, ((GroupKey) groupKey).getProdCode());
        rci.setKwargs(OuaaDcmSupport.KW_KEY_TERMINAL_CODE, ((GroupKey) groupKey).getTerminalCode());
        rci.setKwargs(KW_KEY_APP_CODE, ((GroupKey) groupKey).getAppCode());
        rci.setVargs(new ArrayList<>(dcmEntities));
        return rci;
    }

    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends DcmCard> dcmEntities, RequestedConfigItem rci) {
        for (DcmCard dcmEntity : dcmEntities) {
            // TODO: null value?
            dcmEntity.setAppCode(rci.getKwargs(KW_KEY_APP_CODE, null));
            dcmEntity.setTerminalCode(rci.getKwargs(OuaaDcmSupport.KW_KEY_TERMINAL_CODE, null));
            dcmEntity.setProdCode(rci.getKwargs(KW_KEY_PROD_CODE, null));
        }
    }

    @Override
    protected void updateBizGroupInfoWithDcm(Card bizEntity, DcmCard dcmEntity) {
        bizEntity.setApplicationId(supportRepo.getAppIdByCode(dcmEntity.getAppCode()));
        bizEntity.setTerminalId(supportRepo.getTerminalIdByKey(new TerminalKey(dcmEntity.getProdCode(),
                                                                               dcmEntity.getTerminalCode())));
        bizEntity.setApplicationCode(dcmEntity.getAppCode());
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(DcmCard dcmEntity, Card bizEntity) {
        TerminalKey terminalKey = supportRepo.getTerminalKey(bizEntity.getTerminalId());
        String appCode = supportRepo.getAppCode(bizEntity.getApplicationId());
        dcmEntity.setProdCode(terminalKey.getProdCode());
        dcmEntity.setTerminalCode(terminalKey.getTerminalCode());
        dcmEntity.setAppCode(appCode);
    }

    @Builder
    @Data
    static class GroupKey {
        String prodCode;
        String terminalCode;
        String appCode;
    }

    @Override
    protected Comparator<DcmCard> getDcmComparator() {
        return Comparator.comparing(DcmCard::getProdCode)
                .thenComparing(DcmCard::getTerminalCode)
                .thenComparing(DcmCard::getAppCode)
                .thenComparing(DcmCard::getCode);
    }
}
