package com.guhecloud.cube.iot.mgt.dcm.support;

import com.guhecloud.cube.iot.mgt.base.model.SysDict;
import com.guhecloud.cube.iot.mgt.base.model.SysDictItem;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.comm.translate.ThingsConvert;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;
import com.guhecloud.cube.iot.mgt.dcm.IotProtocolEnum;
import com.guhecloud.cube.iot.mgt.dcm.entity.ProdEvent;
import com.guhecloud.cube.iot.mgt.dcm.entity.ProdModel;
import com.guhecloud.cube.iot.mgt.dcm.entity.ProdProperty;
import com.guhecloud.cube.iot.mgt.dcm.support.ProductHelperRepo.DictEntryKey;
import com.guhecloud.cube.iot.mgt.things.model.MdProduct;
import com.guhecloud.cube.iot.mgt.things.model.MdProductEvent;
import com.guhecloud.cube.iot.mgt.things.model.MdProductProp;
import com.guhecloud.cube.iot.mgt.things.server.MdProductEventService;
import com.guhecloud.cube.iot.mgt.things.server.MdProductPropService;
import com.guhecloud.cube.iot.mgt.things.server.MdProductService;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.guhecloud.cube.iot.mgt.dcm.support.IotDcmSupport.CONFIG_TYPE_PROD_MODEL;
import static com.guhecloud.cube.iot.mgt.dcm.support.IotDcmSupport.parseParent;
import static com.guhecloud.cube.iot.mgt.things.vo.EventConfig.buildNatureEventConfig;

public class ProdModelDcmHelper extends AbstractSimpleDcmHelper<MdProduct, ProdModel> {
    private final MdProductEventService eventService;
    private final MdProductPropService propService;
    private final MdProductService productService;
    private final ProductHelperRepo helperRepo;
    private final PlatformEvnCom platformEvnCom;

    public ProdModelDcmHelper(MdProductService productService,
                              MdProductPropService propService,
                              MdProductEventService eventService,
                              PlatformEvnCom platformEvnCom,
                              List<SysDict> sysDicts,
                              List<SysDictItem> sysDictItems
                             ) {
        super(MdProduct.class, ProdModel.class);
        this.propService = propService;
        this.eventService = eventService;
        this.productService = productService;
        this.platformEvnCom = platformEvnCom;
        this.helperRepo = new ProductHelperRepo(sysDicts, sysDictItems);
    }

    public static ProdModelDcmHelper createRciComparator() {
        return new ProdModelDcmHelper(null, null, null, null,
                                      Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public String getBizKey(MdProduct bizEntity) {
        return String.format("%s:%s:%s",
                             helperRepo.getDictEntryKey(bizEntity.getProdBrandId()).getEntryCode(),
                             bizEntity.getProdName(), bizEntity.getProdModel() == null ? "" : bizEntity.getProdModel()
                            );
    }

    @Override
    public void updateBizWithDcm(MdProduct input, ProdModel dcmEntity) {
        // name, brandId, model is the key to match. no need to update
        input.setIsCustom(dcmEntity.getIsCustom());

        input.setProdCategoryId(helperRepo.getDictEntryId(
                new DictEntryKey(InnerDicEnum.prod_category.value,
                                 parseParent(dcmEntity.getCategory()), dcmEntity.getCategory())));

        Integer nodeTypeId = null;
        try {
            nodeTypeId = Integer.parseInt(dcmEntity.getNodeType());
        } catch (NumberFormatException nfe) {
            // ignore
            nodeTypeId = ThingsConvert.nodeTypeNameToId(dcmEntity.getNodeType());
        }
        input.setNodeType(nodeTypeId.longValue());
        String nodeTypeDictType = helperRepo.getDictTypeCode(ThingsConvert.toNodeDictTypeId(nodeTypeId));
        input.setAccessTypeId(helperRepo
                                      .getDictEntryId(
                                              new DictEntryKey(nodeTypeDictType, "", dcmEntity.getAccessType())));

        input.setProtocolId((long) IotProtocolEnum.getByName(dcmEntity.getProtocol()).getId());
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_PROD_MODEL;
    }

    @Override
    public ProdModel convertBiz2Dcm(MdProduct bizEntity) {
        ProdModel dcmEntity = super.convertBiz2Dcm(bizEntity);

        // 更新字段更名和需要转换的字段
        dcmEntity.setCategory(helperRepo.getDictEntryKey(bizEntity.getProdCategoryId()).getEntryCode());
        dcmEntity.setName(bizEntity.getProdName());
        dcmEntity.setModel(bizEntity.getProdModel() == null ? "" : bizEntity.getProdModel());
        dcmEntity.setBrand(helperRepo.getDictEntryKey(bizEntity.getProdBrandId()).getEntryCode());
        DictEntryKey accessTypeKey = helperRepo.getDictEntryKey(bizEntity.getAccessTypeId());
        dcmEntity.setAccessType(accessTypeKey.getEntryCode());
        dcmEntity.setNodeType(ThingsConvert.toNodeTypeName(bizEntity.getNodeType().intValue()));
        dcmEntity.setRemark(bizEntity.getRemark() == null ? "" : bizEntity.getRemark());
        dcmEntity.setExtendConfig(bizEntity.getExtendConfig() == null ? "" : bizEntity.getExtendConfig());
        // load extended info
        dcmEntity.setProperties(loadProperties(bizEntity.getId()));
        dcmEntity.setEvents(loadEvents(bizEntity.getId()));

        // XXX: iconPath/picPath now ignored.

        return dcmEntity;
    }

    private List<ProdEvent> loadEvents(Long id) {
        List<MdProductEvent> bizEntities = eventService.lambdaQuery()
                .eq(MdProductEvent::getProdId, id).list();
        return bizEntities.stream()
                .map(this::convertBiz2Dcm)
                .collect(Collectors.toList());
    }

    private ProdEvent convertBiz2Dcm(MdProductEvent bizEntity) {
        ProdEvent prodEvent = new ProdEvent();
        prodEvent.setName(bizEntity.getName());
        prodEvent.setCode(bizEntity.getEventCode());
        prodEvent.setConfig(bizEntity.getConfig() == null ? buildNatureEventConfig() : bizEntity.getConfig());
        prodEvent.setCategory(bizEntity.getCategoryId() == null ? "" :
                              helperRepo.getDictEntryKey(bizEntity.getCategoryId()).getEntryCode());
        prodEvent.setRemark(bizEntity.getRemark() == null ? "" : bizEntity.getRemark());
        if (bizEntity.getStatus() != null) {
            prodEvent.setStatus(bizEntity.getStatus());
        }
        return prodEvent;
    }

    private MdProductEvent convertDcm2Biz(ProdEvent dcmEntity) {
        MdProductEvent ret = new MdProductEvent();
        ret.setName(dcmEntity.getName());
        ret.setEventCode(dcmEntity.getCode());
        ret.setConfig(dcmEntity.getConfig());
        // XXX
        ret.setCategoryId(helperRepo.getDictEntryId(new DictEntryKey(InnerDicEnum.event_type.value, "",
                                                                     dcmEntity.getCategory())));
        ret.setRemark(dcmEntity.getRemark());
        ret.setStatus(dcmEntity.getStatus());
        return ret;
    }

    private ProdProperty convertBiz2Dcm(MdProductProp mdProductProp) {
        ProdProperty prodProperty = new ProdProperty();
        prodProperty.setName(mdProductProp.getName());
        prodProperty.setCode(mdProductProp.getPropCode());
        prodProperty.setDataType(mdProductProp.getDataType());
        prodProperty.setPrecision(mdProductProp.getPrecision());
        //        prodProperty.setUnit(mdProductProp.getUnitId() == null ? "" :
        //                helperRepo.getDictEntryKey(mdProductProp.getUnitId()).getEntryCode());
        prodProperty.setDataWrite(mdProductProp.getDataWrite());
        prodProperty.setDataConfig(mdProductProp.getDataConfig() == null ? "" : mdProductProp.getDataConfig());
        prodProperty.setRemark(mdProductProp.getRemark() == null ? "" : mdProductProp.getRemark());
        return prodProperty;
    }

    private MdProductProp convertDcm2Biz(ProdProperty dcmEntity) {
        MdProductProp ret = new MdProductProp();
        ret.setName(dcmEntity.getName());
        ret.setPropCode(dcmEntity.getCode());
        ret.setDataType(dcmEntity.getDataType());
        ret.setPrecision(dcmEntity.getPrecision());
        //        ret.setUnitId(helperRepo.getDictEntryIdOrDefault(
        //                new DictEntryKey(InnerDicEnum.prop_unit.value, "", dcmEntity.getUnit()), null));
        ret.setDataWrite(dcmEntity.getDataWrite());
        ret.setDataConfig(dcmEntity.getDataConfig());
        ret.setRemark(dcmEntity.getRemark());
        return ret;
    }

    private List<ProdProperty> loadProperties(Long id) {
        List<MdProductProp> bizProps = propService.lambdaQuery()
                .eq(MdProductProp::getProdId, id).list();
        return bizProps.stream()
                .map(this::convertBiz2Dcm)
                .collect(Collectors.toList());
    }

    @Override
    public MdProduct convertDcm2Biz(ProdModel dcmEntity) {
        MdProduct ret = super.convertDcm2Biz(dcmEntity);
        ret.setId(platformEvnCom.nextSnowflakeId());
        DictEntryKey key = new DictEntryKey(InnerDicEnum.prod_category.value,
                                            parseParent(dcmEntity.getCategory()), dcmEntity.getCategory());
        ret.setProdCategoryId(helperRepo.getDictEntryId(key));
        ret.setProdName(dcmEntity.getName());
        ret.setProdModel(dcmEntity.getModel());
        ret.setProdBrandId(helperRepo.getDictEntryId(
                new DictEntryKey(InnerDicEnum.prod_brand.value, "", dcmEntity.getBrand())));
        Integer nodeTypeId = null;
        try {
            nodeTypeId = Integer.valueOf(dcmEntity.getNodeType());
        } catch (NumberFormatException nfe) {
            nodeTypeId = ThingsConvert.nodeTypeNameToId(dcmEntity.getNodeType());
        }
        ret.setNodeType(nodeTypeId.longValue());
        String nodeTypeDictType = helperRepo.getDictTypeCode(ThingsConvert.toNodeDictTypeId(nodeTypeId));
        ret.setAccessTypeId(helperRepo.getDictEntryId(
                new DictEntryKey(nodeTypeDictType, "", dcmEntity.getAccessType())));

        // ignore props, events, icon path, pic path
        return ret;
    }

    /**
     * 前置条件： bizEntities 必须都已经有id
     */
    void generateAffiliateRecords(Collection<MdProduct> bizEntities, Collection<ProdModel> dcmEntities,
                                  List<MdProductEvent> mdProductEvents, List<MdProductProp> mdProductProps
                                 ) {
        Map<String, ProdModel> dcmEntitiesByBizKey = dcmEntities.stream()
                .collect(Collectors.toMap(this::getDcmKey, Function.identity()));
        for (MdProduct bizEntity : bizEntities) {
            try {
                String bizKey = getBizKey(bizEntity);
                ProdModel dcmEntity = dcmEntitiesByBizKey.get(bizKey);
                if (dcmEntity == null) {
                    log.debug("no dcm entity found for biz entity: {}", bizKey);
                    // should not happen
                    throw new IllegalStateException("no matching in dcmEntities" + bizKey);
                }

                mdProductEvents.addAll(dcmEntity.getEvents().stream()
                                               .map(x -> generateAffiliateEvent(bizEntity, x))
                                               .filter(Objects::nonNull)
                                               .collect(Collectors.toList())
                                      );
                mdProductProps.addAll(dcmEntity.getProperties().stream()
                                              .map(x -> generateAffiliateProp(bizEntity, x))
                                              .filter(Objects::nonNull)
                                              .collect(Collectors.toList())
                                     );
            } catch (NullPointerException npe) {
                log.debug("error in generate AffiliateRecords", npe);
            }
        }
    }

    private MdProductProp generateAffiliateProp(MdProduct bizEntity, ProdProperty prop) {
        try {
            MdProductProp bizProp = convertDcm2Biz(prop);
            bizProp.setProdId(bizEntity.getId());
            bizProp.setId(platformEvnCom.nextSnowflakeId());
            return bizProp;
        } catch (RuntimeException re) {
            log.debug("failed to generate prop {} for prod: {}", prop, bizEntity, re);
            return null;
        }
    }

    private MdProductEvent generateAffiliateEvent(MdProduct bizEntity, ProdEvent event) {
        try {
            MdProductEvent bizEvent = convertDcm2Biz(event);
            bizEvent.setProdId(bizEntity.getId());
            bizEvent.setId(platformEvnCom.nextSnowflakeId());
            return bizEvent;
        } catch (RuntimeException re) {
            log.debug("failed to generate event {} for prod: {}", event, bizEntity, re);
            return null;
        }
    }

    /**
     * 前置条件: biz entities 都是有id的
     */
    public int batchUpdate(Collection<MdProduct> bizEntities, Collection<ProdModel> dcmEntities) {
        // 批量保存:简单删除, 再全量保存
        List<Long> prodIds = bizEntities.stream().map(MdProduct::getId).collect(Collectors.toList());
        removeAffiliateRecords(prodIds);

        batchAddAffiliateRecordsInner(bizEntities, dcmEntities);
        productService.updateBatchById(bizEntities, 100);
        return bizEntities.size();
    }

    private void batchAddAffiliateRecordsInner(Collection<MdProduct> bizEntities, Collection<ProdModel> dcmEntities) {
        List<MdProductEvent> events = new ArrayList<>();
        List<MdProductProp> props = new ArrayList<>();
        generateAffiliateRecords(bizEntities, dcmEntities, events, props);
        eventService.saveBatch(events, 100);
        propService.saveBatch(props, 100);
    }

    private void removeAffiliateRecords(Collection<Long> prodIds) {
        log.debug("remove affiliate records for {} products: ids: {}", prodIds.size(), prodIds);
        List<Long> eventIdsToRemove = eventService.lambdaQuery().in(MdProductEvent::getProdId, prodIds)
                .list()
                .stream().map(MdProductEvent::getId).collect(Collectors.toList());
        eventService.removeByIds(eventIdsToRemove);
        log.debug("removed {} events", eventIdsToRemove.size());

        List<Long> propIdsToRemove = propService.lambdaQuery().in(MdProductProp::getProdId, prodIds)
                .list()
                .stream().map(MdProductProp::getId).collect(Collectors.toList());
        propService.removeByIds(propIdsToRemove);
        log.debug("removed {} props", propIdsToRemove.size());
    }

    public int batchRemove(Collection<MdProduct> bizEntities, Collection<ProdModel> dcmEntities) {
        log.debug("batch remove {} products/{} dcmEntities.", bizEntities.size(), dcmEntities.size());
        List<Long> prodIds2Remove = bizEntities.stream().map(MdProduct::getId).collect(Collectors.toList());
        removeAffiliateRecords(prodIds2Remove);
        productService.removeByIds(prodIds2Remove);
        log.debug("batch removed {} records", bizEntities.size());
        return prodIds2Remove.size();
    }

    public int batchAdd(Collection<MdProduct> bizEntities, Collection<ProdModel> dcmEntities) {
        log.debug("batch add {} products/{} dcmEntities.", bizEntities.size(), dcmEntities.size());
        for (MdProduct bizEntity : bizEntities) {
            if (bizEntity.getId() == null) {
                bizEntity.setId(platformEvnCom.nextSnowflakeId());
            }
        }
        // 批量新增
        batchAddAffiliateRecordsInner(bizEntities, dcmEntities);
        productService.saveBatch(bizEntities, 100);
        return bizEntities.size();
    }

    @Override
    protected Comparator<ProdModel> getDcmComparator() {
        return Comparator.comparing(ProdModel::getNodeType)
                .thenComparing(ProdModel::getCategory)
                .thenComparing(ProdModel::getName);
    }
}
