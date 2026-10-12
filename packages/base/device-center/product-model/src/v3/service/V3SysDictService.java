package com.guhecloud.cube.base.iot.product.model.v3.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.iot.product.model.v3.model.SysDict;
import com.guhecloud.cube.base.iot.product.model.v3.model.SysDictItem;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemVO;

import java.util.List;

/**
 * 字典管理Service接口(增强版)
 * v3版本支持通过dict_code和item_code进行查询
 */
public interface V3SysDictService extends IService<SysDict> {

    /**
     * 通过字典编码和项编码查询字典项
     *
     * @param dictCode 字典编码
     * @param itemCode 项编码
     * @return 字典项对象
     */
    SysDictItem getItemByCode(String dictCode, String itemCode);

    /**
     * 通过字典项ID查询itemCode
     *
     * @param itemId 字典项ID
     * @return 字典项编码,不存在时返回null
     */
    String getItemCodeById(Long itemId, boolean ignoreTenant);
}
