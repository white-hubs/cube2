package com.guhecloud.cube.base.iot.product.model.v3.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.iot.product.model.v3.model.SharedValueDict;

import java.util.List;

/**
 * 共享值字典Service接口
 * 负责自动收集和提供常用值的输入提示
 */
public interface V3SharedValueDictService extends IService<SharedValueDict> {

    /**
     * 收集共享值
     * 当用户输入新的单位符号、品类名称等时,自动收集到字典中
     *
     * @param dictType 字典类型: unit_symbol/category_name/brand_name
     * @param dictValue 字典值
     * @param dictLabel 显示标签(可选)
     */
    void collectValue(String dictType, String dictValue, String dictLabel);

    /**
     * 获取共享值列表
     * 按使用频率排序,用于输入提示
     *
     * @param dictType 字典类型
     * @return 共享值列表
     */
    List<SharedValueDict> listByType(String dictType);

    /**
     * 搜索共享值
     * 支持模糊搜索,用于输入提示
     *
     * @param dictType 字典类型
     * @param keyword 关键字
     * @return 匹配的共享值列表
     */
    List<SharedValueDict> searchValues(String dictType, String keyword);

    /**
     * 增加使用次数
     * 每次使用某个值时,增加其使用次数
     *
     * @param dictType 字典类型
     * @param dictValue 字典值
     */
    void increaseUseCount(String dictType, String dictValue);
}
