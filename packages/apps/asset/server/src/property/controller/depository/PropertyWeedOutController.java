package com.guhecloud.cube.srv.gen.property.management.server.property.controller.depository;

import com.guhecloud.cube.property.management.client.property.client.property.PropertyWeedOutClient;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyWeedOutPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyWeedOutParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyWeedOutPageVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyWeedOutService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 清理报废
 *
 * @author : lijie
 * @date : 2023-3-17
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/property/weedOut")
public class PropertyWeedOutController implements PropertyWeedOutClient {
    private final PropertyWeedOutService propertyWeedOutService;

    /**
     * 通过ID查询详情
     *
     * @param id 报废记录id
     *
     * @return 报废详情
     */
    @GetMapping("/{id}")
    public PropertyWeedOutPageVO queryById(@PathVariable Long id) {
        return propertyWeedOutService.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/getPropertyWeedOutPage")
    public Pagination<PropertyWeedOutPageVO> getPropertyWeedOutPage(PropertyWeedOutPageParam param) {
        return propertyWeedOutService.getPropertyWeedOutPage(param);
    }

    /**
     * 新增数据
     *
     * @param propertyWeedOut 实例对象
     *
     * @return 实例对象
     */
    @PostMapping("/insert")
    public Long insert(@RequestBody @Valid PropertyWeedOutParam propertyWeedOut) {
        return propertyWeedOutService.insert(propertyWeedOut);
    }

    /**
     * 通过id删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/deleteById/{id}")
    public Boolean deleteById(@PathVariable Long id) {
        return propertyWeedOutService.deleteById(id);
    }
}
