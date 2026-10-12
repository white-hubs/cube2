package com.guhecloud.cube.iot.mgt.base.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.iot.mgt.base.model.SysDictItem;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemThinVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {
    List<SysDictItem> queryAssociatedFromSourceId(@Param("sourceItemId") Long sourceItemId,  @Param("targetDictId") Long targetDictId);

    List<SysDictItem> queryAssociatedFromTargetId(@Param("targetItemId") Long targetItemId, @Param("sourceDictId") Long sourceDictId);

    Long queryDictIdByAdminId(@Param("adminId") Long adminId);

    @InterceptorIgnore(tenantLine = "true")
    List<SysDictItem> queryDictItemByIdAndTenantId(@Param("dictId") long dictId, @Param("tenantId") Integer tenantId);

    long countProdUse(@Param("ids") List<Long> ids);

    List<Long> queryIdsByLikeLevel(@Param("itemLevel") String itemLevel, @Param("dictId") Long dictId);

    int countNumberUniqueInBrand(@Param("numberDictId") long numberDictId, @Param("brandItemId") Long brandItemId,@Param("numberLabel") String numberLabel);

    @InterceptorIgnore(tenantLine = "true")
    List<SysDictItem> queryItemListByParentIdAndDictId(@Param("parentId") long parentId, @Param("dictId") long dictId, @Param("tenant") Integer tenant);

    List<SysDictItem> queryIdsByLikeLevelAndSort(@Param("dictId") Long dictId, @Param("itemLevel") String itemLevel);

    @InterceptorIgnore(tenantLine = "true")
    List<DictItemThinVo> queryDictItemByIdThin(@Param("dictId") long dictId);

    @InterceptorIgnore(tenantLine = "true")
    List<DictItemThinVo> queryDictItemByDictIdAndItemIdThin(@Param("dictId") long dictId, @Param("itemId") long itemId);

    long countBrandUse(@Param("brandId") Long id);

}
