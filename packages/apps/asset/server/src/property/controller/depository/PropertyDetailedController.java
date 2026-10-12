package com.guhecloud.cube.srv.gen.property.management.server.property.controller.depository;

import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.property.management.client.property.client.property.PropertyDetailedClient;
import com.guhecloud.cube.property.management.client.property.param.property.OveragePageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedAllParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedByIdListParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedDuplicatesParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedExcelParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedParam;
import com.guhecloud.cube.property.management.client.property.vo.PropertyDetailIdTypeDTO;
import com.guhecloud.cube.property.management.client.property.vo.SafetyManageCollectPropertyVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyByDepAll;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedTagPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedTotalVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyStatisticsVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyDetailedService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 资产明细
 *
 * @author : lijie
 * @date : 2023-3-8
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/property/detailed")
public class PropertyDetailedController implements PropertyDetailedClient {
    private final PropertyDetailedService propertyDetailedService;

    /**
     * 通过ID查询详情
     *
     * @param id
     *
     * @return 资产详情
     */
    @GetMapping("/{id}")
    public PropertyDetailedVO queryById(@PathVariable Long id) {
        return propertyDetailedService.queryById(id);
    }

    /**
     * 通过id批量查询
     *
     * @return 入库详情
     */
    @GetMapping("/batch/get")
    public List<PropertyDetailedVO> queryPropertyDetailBatchGet(@RequestParam(value = "idList") List<Long> idList) {
        return propertyDetailedService.queryPropertyDetailBatch(idList);
    }

    /**
     * 通过id批量查询
     *
     * @return 入库详情
     */
    @PostMapping("/batch/post")
    public List<PropertyDetailedVO> queryPropertyDetailBatchPost(@RequestBody PropertyDetailedByIdListParam param) {
        return propertyDetailedService.queryPropertyDetailBatch(param.getIdList());
    }

    /**
     * 通过bimCode查询详情
     *
     * @param bimCode
     *
     * @return 资产详情
     */
    @GetMapping("queryByBimCode/{bimCode}")
    public PropertyDetailedVO queryByBimCode(@PathVariable String bimCode) {
        return propertyDetailedService.queryByBimCode(bimCode);
    }

    /**
     * 盘点资产编码是否有重复
     *
     * @param params 资产信息
     *
     * @return 重复项
     */
    @PostMapping("/getDuplicates")
    public Boolean getDuplicates(@RequestBody List<PropertyDetailedDuplicatesParam> params) {
        return propertyDetailedService.getDuplicates(params);
    }

    /**
     * 通过ID查询详情
     *
     * @param id
     *
     * @return 入库详情
     */
    @GetMapping("/queryByTaskId/{id}")
    public PropertyDetailedVO queryByTaskId(@PathVariable Long id, String taskId) {
        return propertyDetailedService.queryByTaskId(id, taskId);
    }

    /**
     * 分页查询
     *
     * @param propertyDetailed 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/getPropertyDetailedPage")
    public Pagination<PropertyDetailedPageVO> getPropertyDetailedPage(@SpringQueryMap @Valid PropertyDetailedPageParam propertyDetailed) {
        return propertyDetailedService.getPropertyDetailedPage(propertyDetailed);
    }

    @PostMapping("/postPropertyDetailedPage")
    public Pagination<PropertyDetailedTagPageVO> postPropertyDetailedPage(@RequestBody PropertyDetailedPageParam propertyDetailed) {
        var page = getPropertyDetailedPage(propertyDetailed);
        var list = page.getRecords().stream().map(record -> {
            return PropertyDetailedTagPageVO.builder()
                    .id(record.getId())
                    .serialNumber(record.getSerialNumber())
                    .name(record.getName())
                    .typeId(record.getTypeId())
                    .typeName(record.getTypeName())
                    .norms(record.getNorms())
                    .unit(record.getUnit())
                    .price(record.getPrice())
                    .priceTotal(record.getPriceTotal())
                    .sourceName(record.getSourceName())
                    .sourceId(record.getSourceId())
                    .purchaseDate(record.getPurchaseDate())
                    .startTime(record.getStartTime())
                    .maintenanceDate(record.getMaintenanceDate())
                    .maintenanceTime(record.getMaintenanceTime())
                    .usageDate(record.getUsageDate())
                    .usageTime(record.getUsageTime())
                    .status(record.getStatus())
                    .statusName(record.getStatusName())
                    .adminId(record.getAdminId())
                    .adminName(record.getAdminName())
                    .areaId(record.getAreaId())
                    .area(record.getArea())
                    .receiveDepId(record.getReceiveDepId())
                    .receiveDepName(record.getReceiveDepName())
                    .receiveUserId(record.getReceiveUserId())
                    .receiveUserName(record.getReceiveUserName())
                    .createBy(record.getCreateBy())
                    .createByName(record.getCreateByName())
                    .createTime(record.getCreateTime())
                    .imgs(record.getImgs())
                    .file(record.getFile())
                    .imgsStr(record.getImgsStr())
                    .fileStr(record.getFileStr())
                    .number(record.getNumber())
                    .storageLocation(record.getStorageLocation())
                    .areaName(record.getAreaName())
                    .financeNumber(record.getFinanceNumber())
                    .maintenanceDepId(record.getMaintenanceDepId())
                    .maintenanceDepName(record.getMaintenanceDepName())
                    .deviceNumber(record.getDeviceNumber())
                    .latAndLon(record.getLatAndLon())
                    .location(record.getLocation())
                    .build();
        }).toList();

        return PagingUtils.convert(page, list);
    }

    /**
     * 分页查询
     *
     * @param propertyDetailed 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/getPropertyDetailedPage-permission")
    @DataPermissionSwitch(dataModel = "property:propertyDetailed")
    public Pagination<PropertyDetailedPageVO> getPropertyDetailedPagePermission(
            @SpringQueryMap @Valid PropertyDetailedPageParam propertyDetailed
                                                                               ) {
        return propertyDetailedService.getPropertyDetailedPage(propertyDetailed);
    }

    /**
     * 风险源分析列表下钻
     *
     * @param propertyDetailed 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/getRiskSourceDetailedPage")
    public Pagination<PropertyDetailedPageVO> getRiskSourceDetailedPage(@SpringQueryMap @Valid PropertyDetailedPageParam propertyDetailed) {
        return propertyDetailedService.getRiskSourceDetailedPage(propertyDetailed);
    }

    /**
     * @Author wql
     * @Description 安全管理资产数据采集接口
     * @Date 14:27 2023/9/13
     **/
    @Override
    @PreAuthorize("hasScope('property_management')")
    @GetMapping("/getPropertyDetailedAll")
    public List<SafetyManageCollectPropertyVO> getPropertyDetailedAll(@SpringQueryMap @Valid PropertyDetailedAllParam propertyDetailed) {
        return propertyDetailedService.getPropertyDetailedAll(propertyDetailed);
    }

    /**
     * 查询资产金额、数量
     *
     * @return 资产数量、金额
     */
    @GetMapping("/getPropertyStatistics")
    public PropertyStatisticsVO getPropertyStatistics(PropertyDetailedPageParam param) {
        return propertyDetailedService.getPropertyStatistics(param);
    }

    /**
     * 添加盘赢
     *
     * @param params 实例对象
     *
     * @return 添加盘赢结果
     */
    @PostMapping("/insertCheckWin")
    public Boolean insertCheckWin(@RequestBody @Valid PropertyDetailedParam params) {
        return propertyDetailedService.insert(params);
    }

    /**
     * 更新数据
     *
     * @param propertyDetailed 实例对象
     *
     * @return 实例对象
     */
    @PutMapping("/update")
    public Boolean update(@RequestBody @Valid PropertyDetailedParam propertyDetailed) {
        return propertyDetailedService.update(propertyDetailed);
    }

    /**
     * 更新资产编码
     *
     * @param propertyDetailed 实例对象
     *
     * @return 实例对象
     */
    @PutMapping("/updateCode")
    public Boolean updateCode(@RequestBody @Valid PropertyDetailedParam propertyDetailed) {
        return propertyDetailedService.updateCode(propertyDetailed);
    }

    /**
     * 通过id删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Boolean deleteById(@PathVariable Long id) {
        return propertyDetailedService.deleteById(id);
    }

    /**
     * 部门资产汇总
     *
     * @param param 筛选条件
     *
     * @return 部门资产汇总
     */
    @GetMapping("/getPropertyByDepAllPage")
    public PropertyByDepAll getPropertyByDepAllPage(@Valid OrganizationQueryParam param) {
        return propertyDetailedService.getPropertyByDepAllPage(param);
    }

    /**
     * 部门资产汇总导出
     *
     * @param param 筛选条件
     *
     * @return excel
     */
    @GetMapping("/getPropertyByDepAllExcel")
    public void getPropertyByDepAllExcel(@Valid OrganizationQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        propertyDetailedService.getPropertyByDepAllExcel(param, request, response);
    }

    /**
     * 维保到期、逾龄资产
     *
     * @param param 筛选条件
     *
     * @return 维保到期、逾龄资产
     */
    @GetMapping("/getOveragePage")
    public Pagination<PropertyDetailedPageVO> getOveragePage(@Valid OveragePageParam param) {
        return propertyDetailedService.getOveragePage(param);
    }

    /**
     * 维保到期、逾龄资产导出
     *
     * @param param 筛选条件
     *
     * @return excel
     */
    @GetMapping("/getOverageExcel")
    public void getOverageExcel(@Valid OveragePageParam param, HttpServletRequest request, HttpServletResponse response) {
        propertyDetailedService.getOverageExcel(param, request, response);
    }

    /**
     * 维保到期、逾龄资产金额
     *
     * @param param 筛选条件
     *
     * @return 维保到期、逾龄资产金额
     */
    @GetMapping("/getOverageAmount")
    public PropertyDetailedTotalVO getOverageAmount(OveragePageParam param) {
        return propertyDetailedService.getOverageAmount(param);
    }

    /**
     * 资产清单导出
     *
     * @author: lijie
     * @date: 2023/10/23 10:06
     * @param:
     * @return:
     **/
    @GetMapping("/getExcel")
    public void getPropertyDetailedExcel(HttpServletRequest request, HttpServletResponse response, PropertyDetailedExcelParam param) {
        propertyDetailedService.getPropertyDetailedExcel(request, response, param);
    }

    /**
     * 资产清单下载模板
     *
     * @author: lijie
     * @date: 2023/10/23 10:06
     * @param:
     * @return:
     **/
    @GetMapping("/getTemplateExcel")
    public void getTemplateExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            propertyDetailedService.getTemplateExcel(request, response);
        } catch (Exception e) {
            log.error("资产清单模板下载失败", e);
            throw new ServiceException("模板下载失败，请联系管理员");
        }
    }

    /**
     * 资产清单导入
     *
     * @author: lijie
     * @date: 2023/10/23 10:07
     * @param:
     * @return:
     **/
    @PostMapping("/import/template")
    public void importTemplate(@RequestParam("file") MultipartFile file) {
        propertyDetailedService.importTemplate(file);
    }

    /**
     * 校验资产编码是否重复
     *
     * @param serialNumber 资产编码
     *
     * @return true 表示不重复
     */
    @GetMapping("/checkCode")
    public Boolean checkPropertyCode(@RequestParam("serialNumber") String serialNumber) {
        return propertyDetailedService.checkPropertyCode(serialNumber);
    }

    /**
     * 校验资产编码是否重复
     *
     * @param param checkSerialNumberList 资产编码集合
     *
     * @return 异常编码集合
     */
    @PostMapping("/checkCodeBath")
    public List<String> checkPropertyCodeBath(@RequestBody OveragePageParam param) {
        return propertyDetailedService.checkPropertyCodeBath(param);
    }

    /**
     * 获取资产明细ID列表
     *
     * @param param 查询参数
     *
     * @return 资产明细ID列表
     */
    @PostMapping("/idTypes")
    public List<PropertyDetailIdTypeDTO> getPropertyDetailedIdTypes(@RequestBody @Valid PropertyDetailedPageParam param) {
        return propertyDetailedService.getPropertyDetailedIdTypes(param);
    }

    /**
     * 根据区域id列表批量获取资产数统计
     *
     * @param areaIds 区域id列表
     *
     * @return 区域id与资产数的映射，key为区域id，value为资产数
     */
    @Override
    @PostMapping("/count/by/areaIds")
    public Map<Long, Long> countByAreaIds(@RequestBody List<Long> areaIds) {
        return propertyDetailedService.countByAreaIds(areaIds);
    }
}
