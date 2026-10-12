package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossEditParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossTrendParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.ElectricitySupplyChangeVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossSupplyDetailVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossTrendVO;

import java.util.List;

public interface LineLossSupplyService {
    ElectricitySupplyChangeVO getElectricitySupplyChange();

    /**
     * 同步供电关系到线损关系
     */
    Boolean syncFromElectricitySupply();

    LineLossSupplyDetailVO detail();

    Boolean editLossLineDevice(LineLossEditParam param);

    /**
     * 根据线路 ID 查询线损趋势
     *
     * @param param 查询参数
     * @return 线损趋势数据列表
     */
    List<LineLossTrendVO> getLineLossTrend(LineLossTrendParam param);
}
