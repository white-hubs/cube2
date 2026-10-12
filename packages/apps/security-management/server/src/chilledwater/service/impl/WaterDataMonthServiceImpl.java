package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.WaterDataMonthMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.WaterDataMonth;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.ReportDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.WaterDataMonthService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DataTotalVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.ReportDetailMonthVo;
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
public class WaterDataMonthServiceImpl extends ServiceImpl<WaterDataMonthMapper, WaterDataMonth> implements WaterDataMonthService {
    private final WaterDataMonthMapper waterDataMonthMapper;

    @Override
    public Map<String, Object> reportDetailMonth(ReportDetailParam reportDetailParam) {
        Map<String, Object> remap = new HashMap<>();
        Page<ReportDetailMonthVo> reportDetailDayVoPage = waterDataMonthMapper.reportDetailMonth(PagingUtils.convert(reportDetailParam), reportDetailParam);
        DataTotalVo dataTotalVo = waterDataMonthMapper.reportDetailMonthTotal(reportDetailParam);
        remap.put("total", dataTotalVo);
        remap.put("pageList", reportDetailDayVoPage);
        return remap;
    }

    @Override
    public List<ReportDetailMonthVo> reportDetailMonthExcel(ReportDetailParam reportDetailParam) {
        List<ReportDetailMonthVo> reportDetailMonthVos = waterDataMonthMapper.reportDetailMonthExcel(reportDetailParam);
        int num = 0;
        for (ReportDetailMonthVo reportDetailMonthVo : reportDetailMonthVos) {
            num += 1;
            reportDetailMonthVo.setNum(num);
        }
        return reportDetailMonthVos;
    }
}
