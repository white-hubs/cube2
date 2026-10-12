package com.guhecloud.cube.iot.mgt.base.service.impl;

import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.iot.mgt.base.model.SysDictItemRef;
import com.guhecloud.cube.iot.mgt.base.param.ProdBrandModifyParam;
import com.guhecloud.cube.iot.mgt.base.service.SysDictItemRefService;
import com.guhecloud.cube.iot.mgt.base.service.SysDictItemService;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemVo;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

/**
 * 产品品牌
 */
@Service
@Slf4j
public class ProdBrandService {
    @Resource
    private CommonDictItemService commonDictItemService;
    @Resource
    private SysDictItemService sysDictItemService;

    @Resource
    private SysDictItemRefService sysDictItemRefService;

    @Resource
    private FileRefClient fileRefClient;

    public boolean saveProdBrand(ProdBrandModifyParam param) {
        Long sourceId = commonDictItemService.saveDictItem(param.toCommonDictItemModifyParam(), InnerDicEnum.prod_brand.id);
        Assert.isTrue(Objects.nonNull(sourceId), "保存产品品牌失败");

        if (Objects.nonNull(param.getItemValue())) {
            fileRefClient.ref(sourceId, Collections.singleton(Long.valueOf(param.getItemValue())));
        }
        return true;
    }

    public boolean updateProdBrand(ProdBrandModifyParam param) {
        DictItemVo dictItemVo = commonDictItemService.getSingleById(param.getId());
        if (Objects.isNull(dictItemVo)) {
            return false;
        }

        commonDictItemService.updateDictItem(param.toCommonDictItemModifyParam(), InnerDicEnum.prod_brand.id);

        if (Objects.nonNull(param.getItemValue())) {

            fileRefClient.ref(param.getId(), Collections.singleton(Long.valueOf(param.getItemValue())));
        }
        return true;
    }

    public boolean deleteProdBrand(Long id) {
        //检查品牌是否已被使用
        long count = sysDictItemService.countBrandUse(id);
        Assert.isTrue(count == 0, "该品牌已关联产品，无法删除");
        commonDictItemService.deleteDictItem(id);

        SysDictItemRef sysDictItemRef = sysDictItemRefService.lambdaQuery()
                .eq(SysDictItemRef::getSourceDictId, InnerDicEnum.prod_brand.id)
                .eq(SysDictItemRef::getSourceItemId, id)
                .eq(SysDictItemRef::getTargetDictId, InnerDicEnum.prod_pic.id)
                .one();

        //删除图片关联,和图片Item字典项
        if (Objects.nonNull(sysDictItemRef)) {
            sysDictItemRefService.removeById(sysDictItemRef.getId());
            sysDictItemService.removeById(sysDictItemRef.getTargetItemId());
            return true;
        }

        return true;
    }

}
