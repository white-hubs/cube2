package com.guhecloud.cube.iot.mgt.things.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.iot.mgt.base.mapper.IotSysUnitMapper;
import com.guhecloud.cube.iot.mgt.base.model.IotSysUnitEntity;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.core.data.lang.IotDataType;
import com.guhecloud.cube.iot.mgt.things.mapper.MdProductMapper;
import com.guhecloud.cube.iot.mgt.things.mapper.MdProductPropMapper;
import com.guhecloud.cube.iot.mgt.things.model.MdProductProp;
import com.guhecloud.cube.iot.mgt.things.server.MdProductPropService;
import com.guhecloud.cube.iot.mgt.things.vo.ProductPropVo;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.iot.mgt.comm.translate.CfgConvertUtil.propDataConfigCheckOrDefault;
import static com.guhecloud.cube.iot.mgt.things.ProductUtils.incrementProductVersion;

@Service
public class MdProductPropServiceImpl extends ServiceImpl<MdProductPropMapper, MdProductProp> implements MdProductPropService {

    @Resource
    private PlatformEvnCom platformEvnCom;

    @Resource
    private MdProductMapper mdProductMapper;

    @Resource
    private IotSysUnitMapper iotSysUnitMapper;

    @Override
    public void saveProdProp(ProductPropVo productPropVo, boolean pushCreateEvent) {
        assertProdPropCodeNotExist(String.valueOf(productPropVo.getProdId()), productPropVo.getPropCode());
        MdProductProp mdProductProp = convert2ProductProp(productPropVo);

        IotDataType propDataType = IotDataType.fromCode(productPropVo.getDataType());
        mdProductProp.setId(platformEvnCom.nextSnowflakeId());
        mdProductProp.setDataConfig(propDataConfigCheckOrDefault(mdProductProp.getDataConfig(), propDataType));
        this.save(mdProductProp);

        if (platformEvnCom.isIotPlatform() && pushCreateEvent) {
            incrementProductVersion(productPropVo.getProdId(), mdProductMapper);
        }
    }

    @Override
    public void updateProdProp(ProductPropVo productPropVo) {

        this.getBaseMapper().deleteById(productPropVo.getId());

        saveProdProp(productPropVo, true);
    }

    @Override
    public void deleteProdProp(Long propId) {
        MdProductProp mdProductProp = this.getById(propId);
        if (mdProductProp == null) {
            return;
        }

        DatabaseContextHolder.softDelete(this, propId);

        if (platformEvnCom.isIotPlatform()) {
            incrementProductVersion(mdProductProp.getProdId(), mdProductMapper);
        }
    }

    @Override
    public ProductPropVo prodPropDetail(Long propId) {
        MdProductProp mdProductProp = this.getById(propId);
        Assert.notNull(mdProductProp, "产品属性不存在");

        return convert2ProductPropVo(mdProductProp);
    }

    @Override
    public List<ProductPropVo> listProdProp(Long prodId) {
        List<MdProductProp> productProps = this.list(new LambdaQueryWrapper<MdProductProp>()
                                                             .eq(MdProductProp::getProdId, prodId)
                                                             .orderByDesc(MdProductProp::getPropCode));//属性code降序

        if (Objects.isNull(productProps)) {
            return new LinkedList<>();
        }

        return productProps.stream()
                .map(this::convert2ProductPropVo)
                .collect(Collectors.toList());
    }

    /**
     * 断言产品属性Code不存在
     *
     * @param prodId   产品ID
     * @param propCode 产品属性Code
     */
    private void assertProdPropCodeNotExist(String prodId, String propCode) {
        var count = this.count(new LambdaQueryWrapper<MdProductProp>()
                                       .eq(MdProductProp::getProdId, Long.valueOf(prodId))
                                       .eq(MdProductProp::getPropCode, propCode));

        if (count > 0) {
            throw new ServiceException("属性标识[" + propCode + "]重复");
        }
    }

    /**
     * 转换为产品属性
     *
     * @param productPropVo 产品属性VO
     *
     * @return 产品属性
     */
    private MdProductProp convert2ProductProp(ProductPropVo productPropVo) {
        MdProductProp mdProductProp = new MdProductProp();
        mdProductProp.setProdId(productPropVo.getProdId());
        mdProductProp.setName(productPropVo.getName());
        mdProductProp.setPropCode(productPropVo.getPropCode());
        mdProductProp.setDataType(productPropVo.getDataType());
        mdProductProp.setPrecision(productPropVo.getPrecision());
        mdProductProp.setUnitCode(productPropVo.getUnitCode());
        mdProductProp.setDataWrite(productPropVo.getDataWrite());
        mdProductProp.setDataConfig(productPropVo.getDataConfig());
        mdProductProp.setRemark(productPropVo.getRemark());
        mdProductProp.setDefaultValue(productPropVo.getDefaultValue());
        return mdProductProp;
    }

    private ProductPropVo convert2ProductPropVo(MdProductProp mdProductProp) {
        IotDataType iotDataType = IotDataType.fromCode(mdProductProp.getDataType());
        IotSysUnitEntity iotSysUnitEntity = iotSysUnitMapper.queryByUnitCode(mdProductProp.getUnitCode());

        ProductPropVo productPropVo = new ProductPropVo();
        productPropVo.setId(mdProductProp.getId());
        productPropVo.setProdId(mdProductProp.getProdId());
        productPropVo.setName(mdProductProp.getName());
        productPropVo.setPropCode(mdProductProp.getPropCode());
        productPropVo.setDataType(mdProductProp.getDataType());
        productPropVo.setDataTypeName(iotDataType.label);
        productPropVo.setPrecision(mdProductProp.getPrecision());
        productPropVo.setUnitId(mdProductProp.getUnitCode());
        productPropVo.setUnitCode(mdProductProp.getUnitCode());
        productPropVo.setUnitName(iotSysUnitEntity == null ? "-" : iotSysUnitEntity.getUnitName());
        productPropVo.setDataWrite(mdProductProp.getDataWrite());
        productPropVo.setDataConfig(mdProductProp.getDataConfig());
        productPropVo.setRemark(mdProductProp.getRemark());
        productPropVo.setCreateTimeFormat(DateUtils.format(mdProductProp.getCreateTime()));
        productPropVo.setDefaultValue(mdProductProp.getDefaultValue());
        return productPropVo;
    }
}
