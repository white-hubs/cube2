package com.guhecloud.cube.property.management.client.property.client.property;

import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedAllParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedByIdListParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedParam;
import com.guhecloud.cube.property.management.client.property.vo.PropertyDetailIdTypeDTO;
import com.guhecloud.cube.property.management.client.property.vo.SafetyManageCollectPropertyVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedTagPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 资产明细
 *
 * @author : lijie
 * @date : 2023-3-8
 */
@FeignClient(name = "propertyManagement")
public interface PropertyDetailedClient {

    /**
     * 通过ID查询详情
     *
     * @param id
     *
     * @return 入库详情
     */
    @GetMapping("/property/detailed/{id}")
    PropertyDetailedVO queryById(@PathVariable("id") Long id);

    /**
     * 通过id批量查询
     *
     * @return 入库详情
     */
    @GetMapping("/property/detailed/batch/get")
    List<PropertyDetailedVO> queryPropertyDetailBatchGet(@RequestParam(value = "idList") List<Long> idList);

    /**
     * 通过id批量查询
     *
     * @return 入库详情
     */
    @PostMapping("/property/detailed/batch/post")
    List<PropertyDetailedVO> queryPropertyDetailBatchPost(@RequestBody PropertyDetailedByIdListParam param);

    /**
     * 分页查询
     *
     * @param propertyDetailed 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/property/detailed/getPropertyDetailedPage")
    Pagination<PropertyDetailedPageVO> getPropertyDetailedPage(@SpringQueryMap PropertyDetailedPageParam propertyDetailed);

    @PostMapping("/property/detailed/postPropertyDetailedPage")
    Pagination<PropertyDetailedTagPageVO> postPropertyDetailedPage(@RequestBody PropertyDetailedPageParam propertyDetailed);

    @GetMapping("/property/detailed/getPropertyDetailedAll")
    List<SafetyManageCollectPropertyVO> getPropertyDetailedAll(@SpringQueryMap @Valid PropertyDetailedAllParam propertyDetailed);

    /**
     * 更新数据
     *
     * @param propertyDetailed 实例对象
     *
     * @return 实例对象
     */
    @PutMapping("/property/detailed/update")
    Boolean update(@RequestBody @Valid PropertyDetailedParam propertyDetailed);

    /**
     * 通过id删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/property/detailed/{id}")
    Boolean deleteById(@PathVariable("id") Long id);

    /**
     * 获取资产明细ID列表
     *
     * @param param 查询参数
     *
     * @return 资产明细ID列表
     */
    @PostMapping("/property/detailed/idTypes")
    List<PropertyDetailIdTypeDTO> getPropertyDetailedIdTypes(@RequestBody PropertyDetailedPageParam param);

    @PostMapping("/property/detailed/count/by/areaIds")
    Map<Long, Long> countByAreaIds(@RequestBody List<Long> gridAreaIds);
}
