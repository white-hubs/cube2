package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.WaterDataYearMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.WaterDataYear;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.ReportDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.WaterDataYearService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DataTotalVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.ReportDetailYearVo;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WaterDataYearServiceImpl extends ServiceImpl<WaterDataYearMapper, WaterDataYear> implements WaterDataYearService {

    private final WaterDataYearMapper waterDataYearMapper;

    @Override
    public Map<String, Object> reportDetailYear(ReportDetailParam reportDetailParam) {
        Map<String, Object> remap = new HashMap<>();
        Page<ReportDetailYearVo> reportDetailYearVoPage = waterDataYearMapper.reportDetailYear(PagingUtils.convert(reportDetailParam), reportDetailParam);
        DataTotalVo dataTotalVo = waterDataYearMapper.reportDetailYearTotal(reportDetailParam);
        remap.put("total", dataTotalVo);
        remap.put("pageList", reportDetailYearVoPage);
        return remap;
    }

    @Override
    public List<ReportDetailYearVo> reportDetailYearExcel(ReportDetailParam reportDetailParam) {
        return waterDataYearMapper.reportDetailYearExcel(reportDetailParam);
    }
}
