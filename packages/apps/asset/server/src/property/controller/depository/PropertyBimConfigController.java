package com.guhecloud.cube.srv.gen.property.management.server.property.controller.depository;

import com.guhecloud.cube.property.management.client.property.param.property.PropertyBimConfigDeleteParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyBimConfigPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyBimConfigParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyBimConfigVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyBimConfigService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资产关联可视化菜单
 *
 * @Author: lijie
 * @CreateTime: 2024-04-07  09:46
 * @Description:
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyBimConfigController {
    private final PropertyBimConfigService propertyBimConfigService;

    /**
     * 分页查询
     *
     * @param param: 筛选条件
     *
     * @return 查询结果
     *
     * @author : lijie
     */
    @GetMapping("/property/bim/config/page")
    public Pagination<PropertyBimConfigVO> queryByPage(PropertyBimConfigPageParam param) {
        return propertyBimConfigService.queryByPage(param);
    }

    /**
     * 新增数据
     *
     * @param param: 新增数据
     *
     * @return 新增结果
     *
     * @author : lijie
     */
    @PostMapping("/property/bim/config/insert")
    public Boolean insert(@RequestBody PropertyBimConfigParam param) {
        return propertyBimConfigService.insert(param);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 删除结果
     *
     * @author : lijie
     */
    @DeleteMapping("/property/bim/config/delete")
    public Boolean deleteById(@RequestBody PropertyBimConfigDeleteParam ids) {
        return propertyBimConfigService.deleteById(ids);
    }
}
