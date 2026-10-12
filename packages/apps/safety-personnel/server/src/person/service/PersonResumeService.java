package com.guhecloud.cube.srv.gen.safety.personnel.server.person.service;

import com.guhecloud.cube.srv.gen.safety.personnel.server.person.param.ResumeQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.ResumeDayGroupVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.ResumeItemVO;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 安全履历聚合服务。
 */
public interface PersonResumeService {

    /**
     * 查询人员履职时间线，按日期分组返回。
     *
     * @param userId OUAA用户ID
     * @param param  日期范围参数
     *
     * @return 日期分组履职记录。
     */
    List<ResumeDayGroupVO> timeline(Long userId, ResumeQueryParam param);

    /**
     * 查询指定类型履职明细。
     *
     * @param userId OUAA用户ID
     * @param type   履职类型，patrol/alarm/approval/meeting/training/other
     * @param param  日期范围参数
     *
     * @return 履职明细列表。
     */
    List<ResumeItemVO> listByType(Long userId, String type, ResumeQueryParam param);

    /**
     * 导出人员履职记录，扁平化为时间与任务名称两列。
     *
     * @param userId   OUAA用户ID
     * @param userName 用户姓名，用于生成文件名
     * @param param    日期范围及类型筛选参数
     * @param response HTTP响应
     */
    void export(Long userId, String userName, ResumeQueryParam param, HttpServletResponse response) throws IOException;
}
