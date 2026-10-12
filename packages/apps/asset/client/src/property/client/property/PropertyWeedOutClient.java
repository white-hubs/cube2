package com.guhecloud.cube.property.management.client.property.client.property;

import com.guhecloud.cube.property.management.client.property.param.property.PropertyWeedOutPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyWeedOutParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyWeedOutPageVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 资产清理报废
 *
 * @author : lijie
 * @date : 2023-3-15
 */
@FeignClient(name = "propertyManagement")
public interface PropertyWeedOutClient {

    /**
     * 通过ID查询详情
     *
     * @param id
     *
     * @return 入库详情
     */
    @GetMapping("/property/weed/out/queryById/{id}")
    PropertyWeedOutPageVO queryById(@PathVariable("id") Long id);

    /**
     * 分页查询
     *
     * @param propertyWeedOut 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/property/weed/out/getPropertyWeedOutPage")
    Pagination<PropertyWeedOutPageVO> getPropertyWeedOutPage(@SpringQueryMap PropertyWeedOutPageParam propertyWeedOut);

    /**
     * 新增数据
     *
     * @param param 实例对象
     *
     * @return 实例对象
     */
    @PostMapping("/property/weedOut/insert")
    Long insert(@RequestBody @Valid PropertyWeedOutParam param);

    /**
     * 通过id删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/property/weedOut/deleteById")
    Boolean deleteById(@RequestParam("id") Long id);
}
