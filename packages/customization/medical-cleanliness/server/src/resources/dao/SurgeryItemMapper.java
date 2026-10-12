package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.entity.SurgeryItem;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param.SurgeryItemQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.ItemDevicePageDetailVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.ItemDevicePageVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.SurgeryItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (手术室）物品
 */
@Mapper
public interface SurgeryItemMapper extends BaseMapper<SurgeryItem> {

    /**
     * 查询物品设备分页(只有物品表的信息，没有关联信息)
     * @param param 条件
     * @return 详情的分页列表
     */
    @InterceptorIgnore(tenantLine = "true")
    <T> Page<ItemDevicePageVO> getSurgeryItemDevicePage(@Param("page") Page<T> page, @Param("param") SurgeryItemQueryParam param);

    /**
     * 查询物品设备分页(只有物品表的信息，没有关联信息)
     * @param param 条件
     * @return 详情的分页列表
     */
    @InterceptorIgnore(tenantLine = "true")
    List<ItemDevicePageVO> getSurgeryItemDeviceList(@Param("param") SurgeryItemQueryParam param);

    /**
     * 查询物品列表(只有物品表的信息，没有关联信息)
     * @param param 条件
     * @return 详情的分页列表
     */
    @InterceptorIgnore(tenantLine = "true")
    List<SurgeryItemVO> getSurgeryItemOnlyList(@Param("param") SurgeryItemQueryParam param);

    /**
     * 批量插入物品
     * @param list 物品集合
     */
    @InterceptorIgnore(tenantLine = "true")
    void insertItemBatch(@Param("list")List<SurgeryItem> list);

    /**
     * 获取全部物品
     * @return 物品集合
     */
    @Select("select id, device_id, name, tenant from surgery_item where deleted = 0")
    List<SurgeryItem> getSurgeryItemAll();
}
