package com.guhecloud.cube.srv.gen.property.management.server.property.controller.depository;

import com.guhecloud.cube.property.management.client.property.client.property.PropertyCheckTaskClient;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageExcelParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckPlanPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckPlanParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckPlanPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckTaskEvolveVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckTaskVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyCheckPlanService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 盘点计划控制层
 *
 * @author : lijie
 * @date : 2023-3-17
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/property/check/plan")
public class PropertyCheckPlanController implements PropertyCheckTaskClient {
    private final PropertyCheckPlanService propertyCheckPlanService;

    /**
     * 通过ID查询盘点计划详情
     *
     * @param id
     *
     * @return 盘点计划详情
     */
    @GetMapping("/{id}")
    public PropertyCheckTaskVO queryById(@PathVariable Long id) {
        return propertyCheckPlanService.queryById(id);
    }

    /**
     * 查询盘点计划统计
     *
     * @param id
     *
     * @return 查询盘点计划统计
     */
    @GetMapping("/queryByPalan/{id}")
    public PropertyCheckTaskEvolveVO queryByPalan(@PathVariable Long id) {
        return propertyCheckPlanService.queryByPalan(id);
    }

    /**
     * 查询盘点计划列表
     *
     * @param propertyCheckTask 筛选条件
     *
     * @return 盘点计划
     */
    @GetMapping("/getPropertyCheckPlanPage")
    public Pagination<PropertyCheckPlanPageVO> getPropertyCheckPlanPage(PropertyCheckPlanPageParam propertyCheckTask) {
        return propertyCheckPlanService.getPropertyCheckPlanPage(propertyCheckTask);
    }

    /**
     * 新增盘点计划
     *
     * @param param 盘点计划数据
     *
     * @return 新增盘点计划结果
     */
    @PostMapping("/insert")
    public Boolean insert(@RequestBody @Valid PropertyCheckPlanParam param) {
        return propertyCheckPlanService.insert(param);
    }

    /**
     * 通过id删除盘点计划
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Boolean deleteById(@PathVariable Long id) {
        return propertyCheckPlanService.deleteById(id);
    }

    /**
     * 提交盘点计划、任务
     *
     * @param planId 计划id
     * @param taskId 计划id
     *
     * @return 提交结果
     */
    @GetMapping("/submit")
    public Boolean submit(Long planId, Long taskId) {
        return propertyCheckPlanService.submit(planId, taskId);
    }

    /**
     * 同步盘赢资产到清单
     *
     * @param planId 计划id
     * @param taskId 计划id
     *
     * @return 同步结果
     */
    @GetMapping("/submitWin")
    public Boolean submitWin(Long planId, Long taskId) {
        return propertyCheckPlanService.submitWin(planId, taskId);
    }

    /**
     * 资产盘点导出
     *
     * @param param 盘点计划id
     */
    @PostMapping("/getExcel")
    public void getExcel(HttpServletResponse response, @RequestBody DepositoryDetailedPageExcelParam param) throws IOException {
        propertyCheckPlanService.getExcel(response, param.getIds());
    }
}
