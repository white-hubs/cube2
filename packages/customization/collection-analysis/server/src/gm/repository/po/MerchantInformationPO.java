package com.guhecloud.cube.biz.collection.analysis.gm.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.BusinessScopeEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.SexEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.VehicleTypeEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.PurchaserExcelDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.WholesalerExcelDTO;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商户信息
 */
@Data
@TableName("merchant_information")
@EqualsAndHashCode(callSuper = true)
public class MerchantInformationPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String name;
    /**
     * 性别(SexEnum)
     */
    private String sex;
    private Integer age;
    /**
     * 商户类型（MerchantTypeEnum）
     */
    private String merchantType;

    /* 采购商 start */
    /**
     * 区域
     */
    private String area;
    /**
     * 经营类型(BusinessScopeEnum)
     */
    private String businessScope;
    /**
     * 采购车类型(VehicleTypeEnum)
     */
    private String vehicleType;
    /* 采购商 end */


    /* 批发商 start */
    /**
     * 省
     */
    private Long nativePlaceProvinceId;
    /**
     * 市
     */
    private Long nativePlaceCityId;
    /**
     * 县/区
     */
    private Long nativePlaceCountyId;
    /**
     * 籍贯(省市区名称字符串短横线-隔开)
     */
    private String nativePlace;
    /**
     * 省
     */
    private Long upstreamSourceProvinceId;
    /**
     * 市
     */
    private Long upstreamSourceCityId;
    /**
     * 县/区
     */
    private Long upstreamSourceCountyId;
    /**
     * 上游来源(省市区名称字符串短横线-隔开)
     */
    private String upstreamSource;
    /**
     * 经营规模
     */
    private Integer businessScaleNumber;
    /* 批发商 end */

    public String getNativePlaceIds() {
        StringBuilder stringBuilder = new StringBuilder();
        if (nativePlaceProvinceId != null) {
            stringBuilder.append(nativePlaceProvinceId).append(",");
        }
        if (nativePlaceCityId != null) {
            stringBuilder.append(nativePlaceCityId).append(",");
        }
        if (nativePlaceCountyId != null) {
            stringBuilder.append(nativePlaceCountyId).append(",");
        }
        if (StringUtils.isNotBlank(stringBuilder)) {
            return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
        }
        return "";
    }

    public String getUpstreamSourceIds() {
        StringBuilder stringBuilder = new StringBuilder();
        if (upstreamSourceProvinceId != null) {
            stringBuilder.append(upstreamSourceProvinceId).append(",");
        }
        if (upstreamSourceCityId != null) {
            stringBuilder.append(upstreamSourceCityId).append(",");
        }
        if (upstreamSourceCountyId != null) {
            stringBuilder.append(upstreamSourceCountyId).append(",");
        }
        if (StringUtils.isNotBlank(stringBuilder)) {
            return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
        }
        return "";
    }

    public WholesalerExcelDTO toWholesalerExcelDTO() {
        WholesalerExcelDTO dto = new WholesalerExcelDTO();
        dto.setName(name);
        dto.setAge(age);
        dto.setSex(SexEnum.getDescribe(sex));
        dto.setBusinessScaleNumber(businessScaleNumber);
        if (StringUtils.isNotBlank(nativePlace)) {
            String[] nativePlaces = nativePlace.split("-");
            if (nativePlaces.length > 0) {
                dto.setNativePlaceProvince(nativePlaces[0] + "_" + nativePlaceProvinceId);
            }
            if (nativePlaces.length > 1) {
                dto.setNativePlaceCity(nativePlaces[1] + "_" + nativePlaceCityId);
            }
        }
        if (StringUtils.isNotBlank(upstreamSource)) {
            String[] upstreamSources = upstreamSource.split("-");
            if (upstreamSources.length > 0) {
                dto.setUpstreamSourceProvince(upstreamSources[0] + "_" + upstreamSourceProvinceId);
            }
            if (upstreamSources.length > 1) {
                dto.setUpstreamSourceCity(upstreamSources[1] + "_" + upstreamSourceCityId);
            }
        }
        return dto;
    }

    public PurchaserExcelDTO toPurchaserExcelDTO() {
        PurchaserExcelDTO dto = new PurchaserExcelDTO();
        dto.setName(name);
        dto.setAge(age);
        dto.setSex(SexEnum.getDescribe(sex));
        dto.setArea(area);
        dto.setBusinessScope(BusinessScopeEnum.getDescribe(businessScope));
        dto.setVehicleType(VehicleTypeEnum.getDescribe(vehicleType));
        return dto;
    }
}
