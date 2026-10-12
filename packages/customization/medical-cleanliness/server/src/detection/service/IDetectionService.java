package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.CompanyCommandParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.CompanyQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.DetectionBasicParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.DetectionReportCommandParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.DetectionReportQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.StandardTreeQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.CompanyVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.DetectionBasicDetailVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.DetectionBasicVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.DetectionReportVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.ReportRecordDetailVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.StandardTreeVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.NonNull;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IDetectionService {
    /**
     * 新增检测依据
     *
     * @param basicParam 检测依据新增参数
     */
    void insertDetectionBasic(@NonNull DetectionBasicParam basicParam, long basicId);

    /**
     * 删除检测依据
     *
     * @param id 检测依据id
     */
    void deleteDetectionBasic(long id);

    /**
     * 修改检测依据
     *
     * @param basicId    检测依据id
     * @param basicParam 修改参数
     */
    void updateDetectionBasic(long basicId, DetectionBasicParam basicParam);

    /**
     * 分页查询
     *
     * @param keyword
     *
     * @return
     */
    Pagination<DetectionBasicVO> detectionBasicPage(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 获取依据详情
     *
     * @param id
     *
     * @return
     */
    DetectionBasicDetailVO getDetectionBasicById(long id);

    /**
     * 获取受检公司分页列表
     *
     * @param queryParam 条件
     *
     * @return 分页列表
     */
    Pagination<CompanyVO> getCompanyPage(CompanyQueryParam queryParam);

    /**
     * 检测公司详情
     *
     * @param tenant 租户
     *
     * @return 详情
     */
    CompanyVO getCompanyDetail(Integer tenant);

    /**
     * 编辑检测公司
     *
     * @param commandParam 入参
     *
     * @return 租户id
     */
    Integer editCompany(@RequestBody CompanyCommandParam commandParam);

    /**
     * 清除检测公司信息
     *
     * @param tenantId 租户id
     *
     * @return 是否成功
     */
    Boolean cleanCompany(Integer tenantId);

    /**
     * 获取剩余校验天数
     *
     * @return 是否合格
     */
    Integer getCountdown();

    /**
     * 获取标准数形集合
     *
     * @param param 产品id集合
     *
     * @return 标准数形集合
     */
    List<StandardTreeVO> getStandardTree(StandardTreeQueryParam param);

    /**
     * 检测报告记录分页列表
     *
     * @param param 分页条件
     *
     * @return 报告记录
     */
    Pagination<DetectionReportVO> getReportPage(DetectionReportQueryParam param);

    /**
     * 报告详情
     *
     * @param id 主键id
     *
     * @return 详情
     */
    ReportRecordDetailVO getReportDetail(Long id);

    /**
     * admin端检测报告记录分页列表
     *
     * @param param 分页条件
     *
     * @return 报告记录
     */
    Pagination<DetectionReportVO> getAdminReportPage(DetectionReportQueryParam param);

    /**
     * admin端报告详情
     *
     * @param id 主键id
     *
     * @return 详情
     */
    ReportRecordDetailVO getAdminReportDetail(Long id);

    /**
     * 生成报告
     *
     * @param commandParam 报告入参
     *
     * @return 是否成功
     */
    ReportRecordDetailVO generateReport(DetectionReportCommandParam commandParam);

    /**
     * 下载报告的数据源列表
     *
     * @param recordId
     */
    void downloadExcelSourceList(long recordId);
}
