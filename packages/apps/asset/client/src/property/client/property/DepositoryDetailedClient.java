package com.guhecloud.cube.property.management.client.property.client.property;

import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedParam;
import com.guhecloud.cube.property.management.client.property.vo.property.DepositoryDetailedPageVO;
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
 * 入库基本信息
 *
 * @author : lijie
 * @date : 2023-3-3
 */
@FeignClient(name = "propertyManagement")
public interface DepositoryDetailedClient {

    /**
     * 通过ID查询详情
     *
     * @param id
     *
     * @return 入库详情
     */
    @GetMapping("/property/depository/queryById/{id}")
    DepositoryDetailedPageVO queryById(@PathVariable("id") Long id);

    /**
     * 分页查询
     *
     * @param depositoryDetailed 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/property/depository/getDepositoryDetailedPage")
    Pagination<DepositoryDetailedPageVO> getDepositoryDetailedPage(@SpringQueryMap @Valid DepositoryDetailedPageParam depositoryDetailed);

    /**
     * 新增-资产入库
     *
     * @param depositoryDetailed 入库信息
     *
     * @return 入库结果
     */
    @PostMapping("/property/depository/insertd")
    Long insert(@RequestBody @Valid DepositoryDetailedParam depositoryDetailed);

    /**
     * 删除资产入库记录
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/property/depository/deleteById/{id}")
    Boolean deleteById(@PathVariable("id") Long id);
}