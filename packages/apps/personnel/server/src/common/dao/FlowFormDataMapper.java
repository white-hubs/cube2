package com.guhecloud.cube.srv.gen.personnel.management.server.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po.LeaveOfficePO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.interfaces.vo.ApprovalDataVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.po.FlowFormDataPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.querygateway.query.FlowFormDataQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author bq
 * @date 2023/02/17
 */
@Repository("personnelFlowFormDataMapper")
public interface FlowFormDataMapper extends BaseMapper<FlowFormDataPO> {

    List<FlowFormDataPO> getRecruitList(@Param("param") FlowFormDataQueryParam param);

    Page<ApprovalDataVO> getStatisticsRecruitDetail(Page<?> page, @Param("id") String id, @Param("param") FlowFormDataQueryParam param);

    Page<ApprovalDataVO> getStatisticsInductionPage(Page<?> page, @Param("param") FlowFormDataQueryParam param);

    List<ApprovalDataVO> getStatisticsInductionList(@Param("param") FlowFormDataQueryParam param);

    Page<ApprovalDataVO> getStatisticsInductionDetail(Page<?> page, @Param("id") String id, @Param("param") FlowFormDataQueryParam param);

    Page<ApprovalDataVO> getStatisticsAssessPage(Page<?> page, @Param("param") FlowFormDataQueryParam param);

    List<ApprovalDataVO> getStatisticsAssessList(@Param("param") FlowFormDataQueryParam param);

    Page<ApprovalDataVO> getStatisticsAssessDetail(Page<?> page, @Param("id") String id, @Param("param") FlowFormDataQueryParam param);

    Page<ApprovalDataVO> getStatisticsRegularWorkerPage(Page<?> page, @Param("param") FlowFormDataQueryParam param);

    List<ApprovalDataVO> getStatisticsRegularWorkerList(@Param("param") FlowFormDataQueryParam param);

    Page<ApprovalDataVO> getStatisticsRegularWorkerDetail(Page<?> page, @Param("id") String id,
                                                          @Param("param") FlowFormDataQueryParam param
                                                         );

    Page<ApprovalDataVO> getStatisticsLeaveOfficePage(Page<?> page, @Param("param") FlowFormDataQueryParam param);

    List<ApprovalDataVO> getStatisticsLeaveOfficeList(@Param("param") FlowFormDataQueryParam param);

    Page<ApprovalDataVO> getStatisticsLeaveOfficeDetail(Page<?> page, @Param("id") String id, @Param("param") FlowFormDataQueryParam param);

    Page<ApprovalDataVO> getStatisticsRotationPostPage(Page<?> page, @Param("param") FlowFormDataQueryParam param);

    List<ApprovalDataVO> getStatisticsRotationPostList(@Param("param") FlowFormDataQueryParam param);

    Page<ApprovalDataVO> getStatisticsRotationPostDetail(Page<?> page, @Param("id") String id,
                                                         @Param("param") FlowFormDataQueryParam param
                                                        );

    /**
     * 查询员工未处理的离职申请
     *
     * @param staffId      员工ID
     * @param businessType 审批类型（离职审批、离职手续）
     *
     * @return
     */
    List<LeaveOfficePO> getNoUpdateLeaveOffice(@Param("staffId") String staffId, @Param("businessType") String businessType);

}
