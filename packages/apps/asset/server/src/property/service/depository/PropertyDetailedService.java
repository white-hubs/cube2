package com.guhecloud.cube.srv.gen.property.management.server.property.service.depository;

import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.property.management.client.property.param.property.OveragePageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedAllParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedDuplicatesParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedExcelParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedWorkflowParam;
import com.guhecloud.cube.property.management.client.property.vo.PropertyDetailIdTypeDTO;
import com.guhecloud.cube.property.management.client.property.vo.SafetyManageCollectPropertyVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyByDepAll;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedTotalVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyStatisticsVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.DepositoryDetailed;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyDetailed;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertyStatusEnum;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 资产明细;(property_detailed)服务接口
 *
 * @author : lijie
 * @date : 2023-3-8
 */
public interface PropertyDetailedService {

    /**
     * 通过ID查询详情
     *
     * @param id
     *
     * @return 入库详情
     */
    PropertyDetailedVO queryById(Long id);

    PropertyDetailedVO queryByBimCode(String bimCode);

    /**
     * 盘点资产编码是否有重复
     *
     * @param params 资产信息
     *
     * @return 重复项
     */
    Boolean getDuplicates(List<PropertyDetailedDuplicatesParam> params);

    /**
     * 通过ID查询未盘点的资产
     *
     * @param id
     *
     * @return 资产信息
     */
    PropertyDetailedVO queryByTaskId(Long id, String taskId);

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return 查询结果
     */
    Pagination<PropertyDetailedPageVO> getPropertyDetailedPage(PropertyDetailedPageParam param);

    void getPropertyDetailedExcel(HttpServletRequest request, HttpServletResponse response, PropertyDetailedExcelParam param);

    List<SafetyManageCollectPropertyVO> getPropertyDetailedAll(PropertyDetailedAllParam propertyDetailed);

    /**
     * 查询资产金额、数量
     *
     * @return 资产数量、金额
     */
    PropertyStatisticsVO getPropertyStatistics(PropertyDetailedPageParam param);

    /**
     * 新增资产
     *
     * @param params             资产信息
     * @param depositoryDetailed 入库信息
     *
     * @return 新增资产结果
     */
    Boolean insert(List<PropertyDetailedWorkflowParam> params, DepositoryDetailed depositoryDetailed);

    /**
     * 新增盘赢
     *
     * @param params 资产信息
     *
     * @return 新增资产结果
     */
    Boolean insert(PropertyDetailedParam params);

    Boolean insert(List<PropertyDetailed> params);

    /**
     * 更新资产信息
     *
     * @param propertyDetailed 资产信息
     *
     * @return 更新资产信息结果
     */
    Boolean update(PropertyDetailedParam propertyDetailed);

    /**
     * 更新资产编码
     *
     * @param propertyDetailed 资产信息
     *
     * @return 更新资产信息结果
     */
    Boolean updateCode(PropertyDetailedParam propertyDetailed);

    /**
     * 更新资产状态
     *
     * @param ids        资产id
     * @param statusEnum 资产状态
     *
     * @return 更新结果
     */
    Boolean update(PropertyStatusEnum statusEnum, List<Long> ids);

    Boolean update(PropertyStatusEnum statusEnum, Long id);

    /**
     * 通过id删除数据
     *
     * @param id id
     *
     * @return 删除结果
     */
    Boolean deleteById(Long id);

    /**
     * 通过入库编号删除
     *
     * @param depositoryNumbers 入库编号
     *
     * @return 删除结果
     */
    Boolean deleteByDepositoryNumbers(String depositoryNumbers);

    /**
     * 部门资产汇总
     *
     * @param param 筛选条件
     *
     * @return 部门资产汇总
     */
    PropertyByDepAll getPropertyByDepAllPage(OrganizationQueryParam param);

    /**
     * 部门资产汇总导出
     *
     * @param param 筛选条件
     *
     * @return excel
     */
    void getPropertyByDepAllExcel(OrganizationQueryParam param, HttpServletRequest request, HttpServletResponse response);

    /**
     * 维保到期、逾龄资产
     *
     * @param param 筛选条件
     *
     * @return 维保到期、逾龄资产
     */
    Pagination<PropertyDetailedPageVO> getOveragePage(OveragePageParam param);

    /**
     * 维保到期、逾龄资产导出
     *
     * @param param 筛选条件
     *
     * @return excel
     */
    void getOverageExcel(OveragePageParam param, HttpServletRequest request, HttpServletResponse response);

    /**
     * 维保到期、逾龄资产金额
     *
     * @param param 筛选条件
     *
     * @return 维保到期、逾龄资产金额
     */
    PropertyDetailedTotalVO getOverageAmount(OveragePageParam param);

    /**
     * 资产维保到期、使用到期检查
     */
    Boolean OverageJob();

    void getTemplateExcel(HttpServletRequest request, HttpServletResponse response);

    void importTemplate(MultipartFile file);

    List<Integer> getAllTenant();

    /**
     * 获取风险源所有的分类信息
     *
     * @return 风险源分类
     */
    Map<String, List<String>> getAllPropertyType();

    /**
     * 风险源分析列表下钻
     *
     * @param propertyDetailed 筛选条件
     *
     * @return 查询结果
     */
    Pagination<PropertyDetailedPageVO> getRiskSourceDetailedPage(PropertyDetailedPageParam propertyDetailed);

    Boolean checkPropertyCode(String serialNumber);

    List<String> checkPropertyCodeBath(OveragePageParam param);

    /**
     * 通过id批量获取资产数据
     *
     * @param idList
     *
     * @return
     */
    List<PropertyDetailedVO> queryPropertyDetailBatch(List<Long> idList);

    /**
     * 获取资产明细ID列表
     *
     * @param param 查询参数
     *
     * @return 资产明细ID列表
     */
    List<PropertyDetailIdTypeDTO> getPropertyDetailedIdTypes(PropertyDetailedPageParam param);

    /**
     * 根据区域id列表批量获取资产数统计
     *
     * @param areaIds 区域id列表
     * @return 区域id与资产数的映射，key为区域id，value为资产数
     */
    Map<Long, Long> countByAreaIds(List<Long> areaIds);
}
