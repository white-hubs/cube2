package com.guhecloud.cube.srv.gen.appraisals.server.record.service;

import com.guhecloud.cube.srv.gen.appraisals.server.record.param.FormAppraisalRecordPageParam;
import com.guhecloud.cube.srv.gen.appraisals.server.record.param.FormAppraisalRecordStatisticsParam;
import com.guhecloud.cube.srv.gen.appraisals.server.record.vo.FormAppraisalRecordDetailVO;
import com.guhecloud.cube.srv.gen.appraisals.server.record.vo.FormAppraisalRecordGroupVO;
import com.guhecloud.cube.srv.gen.appraisals.server.record.vo.FormAppraisalStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 考核记录查询服务接口
 * 提供考核记录的查询操作
 */
public interface FormAppraisalRecordQueryService {
    /**
     * 根据 ID 获取考核记录详情
     *
     * @param id 记录 ID
     * @return 考核记录详情 VO
     */
    FormAppraisalRecordDetailVO getById(Long id);

    /**
     * 分页获取考核记录列表
     *
     * @param param 分页查询参数
     * @return 考核记录分页数据
     */
    Pagination<FormAppraisalRecordGroupVO> getPage(FormAppraisalRecordPageParam param);

    /**
     * 获取考核统计看板
     *
     * @param param 统计查询参数
     * @return 统计看板结果
     */
    FormAppraisalStatisticsVO statistics(FormAppraisalRecordStatisticsParam param);

    /**
     * 导出考核记录
     *
     * @param param    查询条件
     * @param request  请求
     * @param response 响应
     */
    void export(FormAppraisalRecordPageParam param, HttpServletRequest request, HttpServletResponse response);
}
