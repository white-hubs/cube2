package com.guhecloud.cube.iot.mgt.base.service.impl;

import com.guhecloud.cube.iot.mgt.base.mapper.SysDictItemMapper;
import com.guhecloud.cube.iot.mgt.base.param.CommonDictItemModifyParam;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemVo;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.PlatformEvn;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 产品品类(类别)
 */
@Service
public class ProdCategoryService {

    @Resource
    private CommonDictItemService commonDictItemService;

    @Resource
    private SysDictItemMapper sysDictItemMapper;

    @Resource
    private PlatformEvnCom platformEvnCom;

    public void saveProdCategory(CommonDictItemModifyParam param) {
        if (StringUtils.isBlank(param.getItemValue())) {
            param.setItemValue(param.getItemLabel());//name == val
        }

        if (Objects.nonNull(param.getParentId())) {
            DictItemVo dictItemVo = commonDictItemService.getSingleById(param.getParentId());

            Assert.isNotAcceptable(dictItemVo.getItemLevel()
                    .startsWith(PlatformEvn.STORAGE_PLATFORM.singleCode), "标准产品不允许新增");
        }
        Assert.isUnknownError(Objects.isNull(commonDictItemService.saveDictItem(param, InnerDicEnum.prod_category.id)), "保存字典失败");
    }

    public void updateProdCategory(CommonDictItemModifyParam param) {
        if (StringUtils.isBlank(param.getItemValue())) {
            param.setItemValue(param.getItemLabel());//name == val
        }

        DictItemVo dictItemVo = commonDictItemService.getSingleById(param.getId());

        Assert.isNotAcceptable(dictItemVo.getItemLevel()
                .startsWith(PlatformEvn.STORAGE_PLATFORM.singleCode), "标准产品不允许修改");
        Assert.isUnknownError(!commonDictItemService.updateDictItem(param, InnerDicEnum.prod_category.id), "更新字典失败");
    }

    public void deleteProdCategory(Long id) {
        DictItemVo dictItemVo = commonDictItemService.getSingleById(id);

        List<Long> ids = commonDictItemService.queryIdsByLikeLevel(dictItemVo.getItemLevel(), dictItemVo.getDictId());

        long count = sysDictItemMapper.countProdUse(ids);

        Assert.isNotAcceptable(count != 0, "该品类或下级品类已被使用，无法删除");

        commonDictItemService.batchDeleteDictItem(ids);
    }
}
