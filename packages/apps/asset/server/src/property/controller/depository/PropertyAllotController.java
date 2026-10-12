package com.guhecloud.cube.srv.gen.property.management.server.property.controller.depository;

import com.guhecloud.cube.property.management.client.property.client.property.PropertyAllotClient;
import com.guhecloud.cube.property.management.client.property.param.property.AllotPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.AllotParam;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageExcelParam;
import com.guhecloud.cube.property.management.client.property.vo.property.AllotPageVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyAllotService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资产派发记录控制层
 *
 * @author : lijie
 * @date : 2023-3-8
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyAllotController implements PropertyAllotClient {
    private final PropertyAllotService propertyAllotService;

    /**
     * 通过ID查询详情
     *
     * @param id
     *
     * @return 派发详情
     */
    @GetMapping("/property/allot/queryById/{id}")
    public AllotPageVO queryById(@PathVariable Long id) {
        return propertyAllotService.queryById(id);
    }

    /**
     * 分页查询派发、调拨
     *
     * @param param 筛选条件
     *
     * @return 派发、调拨列表
     */
    @GetMapping("/property/allot/getAllotPage")
    public Pagination<AllotPageVO> getAllotPage(@Valid AllotPageParam param) {
        return propertyAllotService.getAllotPage(param);
    }

    /**
     * 新增派发、调拨
     *
     * @param outbound 资产信息
     *
     * @return 派发、调拨结果
     */
    @PostMapping("/property/allot/insert")
    public Long insert(@RequestBody @Valid AllotParam outbound) {
        return propertyAllotService.insert(outbound);
    }

    /**
     * 通过id删除派发、调拨记录
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/property/allot/deleteById/{id}")
    public Boolean deleteById(@PathVariable Long id) {
        return propertyAllotService.deleteById(id);
    }

    /**
     * 资产领用导出
     *
     * @param param 入库id
     */
    @PostMapping("/property/allot/getExcel")
    public void getExcel(HttpServletResponse response, @RequestBody DepositoryDetailedPageExcelParam param) {
        propertyAllotService.getExcel(response, param);
    }
}
