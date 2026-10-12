package com.guhecloud.cube.srv.gen.securitymanagement.server.information.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceInfoVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.dto.InformationPlanDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.InformationPlanEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.InformationPlanParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.InformationPlanVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface InformationPlanMapper extends BaseMapper<InformationPlanEntity> {

    Page<InformationPlanVO> getInformationPlanPage(Page page, @Param("param") InformationPlanParam param);

    List<InformationPlanDTO> getInformationPlanListNeedStart(@Param("param") InformationPlanParam param, @Param("nowDate")
                                                             LocalDateTime nowDate
                                                            );

    List<InformationPlanDTO> getInformationPlanListNeedStop(@Param("param") InformationPlanParam param,
                                                            @Param("nowDate") LocalDateTime nowDate
                                                           );

    Page<SecurityDeviceInfoVO> getUnusedDevicePage(Page<Object> convert, @Param("param") InformationPlanParam param);
}
