package com.guhecloud.cube.srv.gen.property.management.server.consumable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.property.management.client.common.param.WorkflowRelevance;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageExcelParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableAddRecord;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStockDetail;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableAddRecordParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableAddRecordQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableAddStockParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableAddPageVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableAddRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import instance.event.ProcessCompletedMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 描述：耗材入库服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
public interface ConsumableAddRecordService extends IService<ConsumableAddRecord> {

    Long add(ConsumableAddRecordParam param);

    void update(Long id, ConsumableAddRecordParam param);

    Boolean delete(Long id);

    void batchDelete(List<Long> ids);

    ConsumableAddRecordVO getById(Long id);

    Pagination<ConsumableAddPageVO> getPage(ConsumableAddRecordQueryParam param);

    /**
     * 增加入库记录并记录明细
     *
     * @param param 耗材入库信息
     */
    Long addWithDetail(ConsumableAddStockParam param);

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

    Boolean sendMsg(List<ConsumableStockDetail> stockDetail);

    void downloadTemplate(HttpServletRequest request, HttpServletResponse response);

    void importExcel(MultipartFile file);
}