package com.guhecloud.cube.property.management.client.property.client.property;//package com.guhecloud.cube.property.managen.server.controller.employ;

import com.guhecloud.cube.property.management.client.property.param.property.AllotPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.AllotParam;
import com.guhecloud.cube.property.management.client.property.vo.property.AllotPageVO;
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
 * 资产派发、调拨
 *
 * @author : lijie
 * @date : 2023-3-8
 */
@FeignClient(name = "propertyManagement")
public interface PropertyAllotClient {
    /**
     * 通过ID查询详情
     *
     * @param id
     *
     * @return 派发详情
     */
    @GetMapping("/property/allot/queryById/{id}")
    AllotPageVO queryById(@PathVariable("id") Long id);

    /**
     * 分页查询
     *
     * @param outbound 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/property/allot/getAllotPage")
    Pagination<AllotPageVO> getAllotPage(@SpringQueryMap AllotPageParam outbound);

    /**
     * 新增数据
     *
     * @param outbound 实例对象
     *
     * @return 实例对象
     */
    @PostMapping("/property/allot/insert")
    Long insert(@RequestBody @Valid AllotParam outbound);

    /**
     * 通过id删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/property/allot/deleteById/{id}")
    Boolean deleteById(@PathVariable("id") Long id);

}
