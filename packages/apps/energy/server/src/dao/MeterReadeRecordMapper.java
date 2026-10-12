package com.guhecloud.cube.srv.gen.energymanagement.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterReadeRecord;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.MeterDeviceQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DataVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterReadeRecordVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeterReadeRecordMapper extends BaseMapper<MeterReadeRecord> {
    Page<MeterReadeRecordVo> getMeterReadeRecordPage(Page<?> page, @Param("param") MeterDeviceQueryParam param,
                                                     @Param("deviceIds") List<String> deviceIds
                                                    );

    List<DataVo> getReadeRecordList(@Param("deviceId") Long deviceId, String begin, String end);

    void delEnergyProjectItem(@Param("list") List<Long> list, @Param("delTime") Long delTime);

    void delEnergyAlarmRule(@Param("list") List<Long> list, @Param("delTime") Long delTime);

    void delMeterDevice(@Param("list") List<Long> list, @Param("delTime") Long delTime);
}
