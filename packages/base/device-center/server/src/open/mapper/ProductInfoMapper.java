package com.guhecloud.cube.base.device.center.open.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductCountDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoWithDeviceDTO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceTagVO;
import com.guhecloud.cube.base.device.center.open.entity.ProductCategoryInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author pengjian
 * @date 2024/1/16 15:47
 * @description
 */
@Mapper
public interface ProductInfoMapper extends BaseMapper {

    DeviceProductInfoDTO selectProductInfoByDeviceId(@Param("deviceId") Long deviceId);

    List<DeviceProductInfoDTO> selectList(@Param("productIds") Set<Long> productIds);

    List<DeviceProductInfoWithDeviceDTO> selectListByDeviceId(@Param("deviceIds") Set<Long> deviceIds);

    List<DeviceProductInfoDTO> selectListByProdCategoryCodeConcatLeftLike(@Param("categoryCodeConcat") String categoryCodeConcat,
                                                                          @Param("tenant") Integer tenant
                                                                         );

    List<ProductCategoryInfoEntity> selectProdCategoryListByCode(@Param("codes") Set<String> codes, @Param("tenant") Integer tenant);

    Set<DeviceTagVO> selectDeviceTagsByProductId(@Param("productId") Long productId);

    Page<ProductCategoryInfoEntity> categoryPage(Page page, @Param("codes") Set<String> codes, @Param("tenant") Integer tenant);

    /**
     * 获取产品设备数量
     *
     * @param productIds 产品ids
     * @param deviceIds  设备ids
     *
     * @return 产品设备数量集合
     */
    List<DeviceProductCountDTO> deviceProductCount(@Param("productIds") Set<Long> productIds, @Param("deviceIds") Set<Long> deviceIds);
}
