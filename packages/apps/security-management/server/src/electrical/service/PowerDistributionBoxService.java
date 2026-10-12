package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.PowerDistributionBox;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param.PowerConsumptionParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param.RecordTimeParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.DistributionsExcelVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.DistributionsVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.ElectricityVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.RecordTimeVo;
import com.guhecloud.platform.base.model.Pagination;

import java.math.BigDecimal;
import java.util.List;

public interface PowerDistributionBoxService extends IService<PowerDistributionBox> {
    Pagination<RecordTimeVo> getElectricityTimeRecord(RecordTimeParam param);

    Pagination<RecordTimeVo> getDistributionTimeRecord(RecordTimeParam param);

    PowerDistributionBox getLastOne(Long deviceId);

    List<ElectricityVo> getElectricityTimeRecordDetails(RecordTimeParam recordTimeParam);

    List<DistributionsVo> getDistributionsTimeRecordDetails(RecordTimeParam recordTimeParam);

    List<DistributionsExcelVo> getDistributionsTimeRecordExcel(RecordTimeParam recordTimeParam);

    BigDecimal getPowerConsumption(PowerConsumptionParam param);

}
