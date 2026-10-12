package com.guhecloud.cube.srv.gen.property.management.server.risk.source.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.dictionary.dto.CascadeDictionaryDTO;
import com.guhecloud.cube.property.management.client.property.param.property.RiskSourceDeviceQueryParam;
import com.guhecloud.cube.property.management.client.property.vo.SafetyManageCollectPropertyVO;
import com.guhecloud.cube.property.management.client.property.vo.property.RiskSourceDeviceVO;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerAllParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerPageParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceIdTypeVO;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.entity.RiskSourceLedger;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.param.RiskSourceExcelTemplateParam;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.param.RiskSourceLedgerExcelParam;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  16:45
 * @Description:
 * @Version: 1.0
 */
public interface RiskSourceLedgerService extends IService<RiskSourceLedger> {
    RiskSourceLedgerVO queryById(Long id);

    RiskSourceLedgerVO queryByBimCode(String bimCode);

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return 查询结果
     */
    Pagination<RiskSourceLedgerVO> queryByPage(RiskSourceLedgerPageParam param);

    /**
     * 风险源台账数据采集
     *
     * @param param 筛选条件
     *
     * @return 查询结果
     */
    List<SafetyManageCollectPropertyVO> queryAll(RiskSourceLedgerAllParam param);

    List<SafetyManageCollectPropertyVO> queryAllByCreateTime();

    /**
     * 新增数据
     *
     * @param param 实例对象
     *
     * @return 实例对象
     */
    Boolean insert(RiskSourceLedgerParam param);

    Boolean insertBatch(List<RiskSourceLedger> param);

    /**
     * 更新数据
     *
     * @param param 实例对象
     *
     * @return 实例对象
     */
    Boolean update(RiskSourceLedgerParam param);

    Boolean updateStatus();

    /**
     * 通过主键删除数据
     *
     * @param ids 主键
     */
    void deleteByIds(Set<Long> ids);

    void getTemplateExcel(List<CascadeDictionaryDTO> type, HttpServletRequest request, HttpServletResponse response);

    void importTemplate(MultipartFile file);

    /**
     * 定时发送到期提醒
     */
    void overageJob();

    /**
     * 风险源同步资产数据
     */
    void propertyCopyRiskSource(List<String> typeNames);

    List<RiskSourceDeviceVO> getRiskSourceDeviceNum(RiskSourceDeviceQueryParam param);

    /**
     * 根据编码查询执行数量风险源台账
     *
     * @param typeCodes
     * @param size
     *
     * @return
     */
    List<RiskSourceLedgerVO> queryRiskSourceLedgerWithLocation(String typeCodes, Integer size);

    /**
     * 查询档位和现场点关联的风险源
     * 只实现了类型、状态、档位id集合、现场点集合的条件
     * 需要其他条件自己再加
     *
     * @param param 条件
     *
     * @return 集合
     */
    List<RiskSourceLedgerVO> riskSourceLedgerVOList(RiskSourceLedgerPageParam param);

    /**
     * 风险源导入数据填充
     *
     * @param templateParam
     * @param remark
     * @param orgMap
     * @param areaMap
     * @param bimCodeMap
     * @param serialNumberMap
     * @param serialNumberIdMap
     *
     * @return
     */
    RiskSourceLedgerExcelParam riskSourceExcelParam(RiskSourceExcelTemplateParam templateParam,
                                                    String remark,
                                                    Map<String, Long> orgMap,
                                                    Map<String, Long> areaMap, Map<String, Long> bimCodeMap,
                                                    Map<String, Integer> serialNumberMap, Map<String, RiskSourceLedger> serialNumberIdMap
                                                   );

    /**
     * 批量查询风险源台账详情
     *
     * @param idList
     *
     * @return
     */
    List<RiskSourceLedgerVO> queryRiskSourceLedgerBatch(List<Long> idList);

    /**
     * 获取风险源ID列表
     *
     * @param param 查询参数
     *
     * @return 风险源ID列表
     */
    List<RiskSourceIdTypeVO> getRiskSourceIdTypes(RiskSourceLedgerPageParam param);
}
