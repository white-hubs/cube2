package com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.entity.BoilerData;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.param.BoilerParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.vo.BoilerDataVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.vo.BoilerRecordPageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BoilerDataMapper extends BaseMapper<BoilerData> {
    List<BoilerData> getBoilerData(@Param("param") BoilerParam boilerParam);

    BigDecimal getWaterValue(@Param("ids") List<Long> ids, @Param("st") LocalDateTime st, @Param("et") LocalDateTime et);

    BigDecimal getElectricityValue(@Param("ids") List<Long> ids, @Param("st") LocalDateTime st, @Param("et") LocalDateTime et);

    List<BoilerDataVo> getDataTemperature(@Param("ids") List<Long> ids, @Param("st") LocalDateTime st, @Param("et") LocalDateTime et,
                                          @Param("dateFormat") String dateFormat
                                         );

    List<BoilerDataVo> getDataWater(@Param("ids") List<Long> ids, @Param("st") LocalDateTime st, @Param("et") LocalDateTime et,
                                    @Param("dateFormat") String dateFormat
                                   );

    List<BoilerDataVo> getDataElectricity(@Param("ids") List<Long> ids, @Param("st") LocalDateTime st, @Param("et") LocalDateTime et,
                                          @Param("dateFormat") String dateFormat
                                         );

    Page<BoilerRecordPageVo> getBoilerDataRecordPage(Page page, @Param("param") BoilerParam param);
}
