package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.YearEnergy;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.YearEnergyParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageStatParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.YearEnergyQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyTypeStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.YearEnergyVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 年用能
 */
public interface YearEnergyService extends IService<YearEnergy> {
    Pagination<YearEnergyVO> getYearEnergyPage(YearEnergyQueryParam param);

    YearEnergyVO getYearEnergyDetail(YearEnergyQueryParam param);

    Long addYearEnergy(YearEnergyParam param);

    void editYearEnergy(@RequestBody YearEnergyParam param);

    void removeYearEnergy(Long id);

    List<EnergyTypeStatVO> getYearEnergyTrend(YearEnergyQueryParam param);

    EnergyTypeStatVO getYearEnergyConversionDaily(EnergyDailyUsageStatParam param);
}
