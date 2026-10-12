package com.guhecloud.cube.srv.gen.property.management.server.consumable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.property.management.client.common.param.WorkflowRelevance;
import com.guhecloud.cube.property.management.client.common.vo.PieChartValueVO;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageExcelParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableReduceRecord;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableReduceRecordParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableReduceRecordQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableReduceStockParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableStockDetailParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableReduceRecordVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableReduceStockStatVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStoreStockExtendVO;
import com.guhecloud.platform.base.model.Pagination;
import instance.event.ProcessCompletedMessage;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 描述：耗材出库记录服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
public interface ConsumableReduceRecordService extends IService<ConsumableReduceRecord> {

    Long add(ConsumableReduceRecordParam param);

    void update(Long id, ConsumableReduceRecordParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    ConsumableReduceRecordVO getById(Long id);

    Pagination<ConsumableReduceRecordVO> getPage(ConsumableReduceRecordQueryParam param);

    /**
     * 增加出库记录并记录明细
     *
     * @param param
     */
    Long reduceWithDetail(ConsumableReduceStockParam param);

    List<ConsumableReduceStockStatVO> getReduceStockStat(ConsumableReduceRecordParam param);

    List<PieChartValueVO> getReduceStockStatByDep(String depId, String period);

    Pagination<ConsumableStoreStockExtendVO> getReduceDetail(ConsumableReduceRecordParam param);

    void getExcel(HttpServletResponse response, DepositoryDetailedPageExcelParam param);

    /**
     * 流引擎回调，绑定流程id
     *
     * @param param
     */
    Boolean update(WorkflowRelevance param);

    /**
     * 审批
     *
     * @param message 审批信息
     *
     * @return 是否成功
     */
    Boolean submit(ProcessCompletedMessage message);

    /**
     * 检查耗材是否可以出入库
     *
     * @param storeId    仓库id
     * @param details    耗材数据
     * @param reduceDate 出库时间
     * @param claimant   领用人
     */
    void checking(String flowResult, Long storeId, List<ConsumableStockDetailParam> details,
                  LocalDateTime reduceDate, Long claimant
                 );
}