package com.guhecloud.cube.biz.meattrade.server.pork.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetailLog;

import java.util.List;

public interface PorkArrangeDetailLogService extends IService<PorkArrangeDetailLog> {

    void savePorkArrangeDetailLog(List<PorkArrangeDetail> porkArrangeDetails);

    List<PorkArrangeDetailLog> getPorkArrangeDetailLogs(String porkReportCode, String label);
}
