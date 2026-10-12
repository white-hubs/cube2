package com.guhecloud.cube.property.management.client.property.client.property;

import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckPlanPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckPlanParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckPlanPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckTaskVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 盘点计划
 *
 * @author : lijie
 * @date : 2023-3-15
 */
@FeignClient(name = "propertyManagement")
public interface PropertyCheckTaskClient {
    /**
     * 通过ID查询详情
     *
     * @param id
     *
     * @return 入库详情
     */
    @GetMapping("/property/checkTask/{id}")
    PropertyCheckTaskVO queryById(@PathVariable("id") Long id);

    /**
     * 分页查询
     *
     * @param propertyCheckTask 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/property/checkTask/getPropertyCheckPlanPage")
    Pagination<PropertyCheckPlanPageVO> getPropertyCheckPlanPage(@SpringQueryMap @Valid PropertyCheckPlanPageParam propertyCheckTask);

    /**
     * 新增数据
     *
     * @param propertyCheckTask 实例对象
     *
     * @return 实例对象
     */
    @PostMapping("/property/checkTask/insert")
    Boolean insert(@RequestBody @Valid PropertyCheckPlanParam propertyCheckTask);

    /**
     * 通过id删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/property/checkTask/deleteById/{id}")
    Boolean deleteById(@PathVariable("id") Long id);
}
