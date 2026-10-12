package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.constants.LineLossConstants;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dto.LineLossRecordDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossStatQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossClickHouseService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossStatService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.QueryCondition;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossStatVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 线损统计服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineLossStatServiceImpl implements LineLossStatService {
    private final LineLossClickHouseService lineLossClickHouseService;

    /**
     * 线损统计分页列表
     *
     * @param param 查询参数
     *
     * @return 线损统计分页列表
     */
    @Override
    public Pagination<LineLossStatVO> pageLineLoss(LineLossStatQueryParam param) {
        // 构建查询条件
        QueryCondition queryCondition = buildQueryCondition(param);

        // 从 ClickHouse 查询分页数据
        Pagination<LineLossRecordDTO> pagination = lineLossClickHouseService.pageClickHouse(
                LineLossConstants.LINE_LOSS_STAT_TABLE_NAME,
                LineLossRecordDTO.class,
                queryCondition,
                null, // 默认按创建时间倒序
                param.getPageNumber(),
                param.getPageSize()
                                                                                           );

        // 转换为 VO 对象
        List<LineLossStatVO> voList = convertToVOList(pagination.getRecords());

        return Pagination.<LineLossStatVO>builder()
                .total(pagination.getTotal())
                .pages(pagination.getPages())
                .records(voList)
                .build();
    }

    /**
     * 构建查询条件
     *
     * @param param 查询参数
     *
     * @return 查询条件对象
     */
    private QueryCondition buildQueryCondition(LineLossStatQueryParam param) {
        String startTime = param.getStartTime();
        String endTime = param.getEndTime();
        Set<Long> deviceIdSet = param.getDeviceIdSet();

        // 如果开始时间和结束时间都为空，默认当前月
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime monthStart = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            startTime = monthStart.format(formatter);
            endTime = now.format(formatter);
        }

        // 使用 Builder 模式构建查询条件
        QueryCondition.Builder builder = QueryCondition.builder();

        // 添加条件
        builder.addTimeRange(startTime, endTime, "collect_time").addEqCondition("line_id", param.getLineId())
                .addInCondition("device_id", (Set<?>) deviceIdSet).addLikeCondition("line_name", param.getLineName());

        return builder.build();
    }

    /**
     * 导出线损统计 Excel
     *
     * @param param    查询参数
     * @param request  请求
     * @param response 响应
     */
    @Override
    public void export(LineLossStatQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        // 查询所有数据（不分页）
        List<LineLossRecordDTO> dtoList = lineLossClickHouseService.listClickHouse(
                LineLossConstants.LINE_LOSS_STAT_TABLE_NAME,
                LineLossRecordDTO.class,
                param.getStartTime(),
                param.getEndTime(),
                param.getDeviceIdSet(),
                null
                                                                                  );

        // 转换为 VO 对象
        List<LineLossStatVO> voList = convertToVOList(dtoList);

        // 导出 Excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 自定义标题别名
        writer.addHeaderAlias("lineId", "线路 ID");
        writer.addHeaderAlias("lineName", "线路名称");
        writer.addHeaderAlias("fromNodeName", "起点");
        writer.addHeaderAlias("toNodeName", "终点");
        writer.addHeaderAlias("fromNodeLocation", "起点位置");
        writer.addHeaderAlias("toNodeLocation", "终点位置");
        writer.addHeaderAlias("deviceName", "计量设备");
        writer.addHeaderAlias("inputEnergy", "输入电量 (kWh)");
        writer.addHeaderAlias("outputEnergy", "输出电量 (kWh)");
        writer.addHeaderAlias("lossEnergy", "设备损耗 (kWh)");
        writer.addHeaderAlias("lossRate", "设备线损率 (%)");
        writer.setOnlyAlias(true);
        // 默认配置
        writer.write(voList, true);
        // 设置列宽
        writer.setColumnWidth(-1, 20);
        // 设置文件名
        String fileName = "线损统计";
        // 设置 response 头信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里 URLEncoder.encode 可以防止中文乱码
        String encodedFileName = java.net.URLEncoder.encode(fileName, java.nio.charset.StandardCharsets.UTF_8).replace("+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");
        try {
            // 写出到输出流
            writer.flush(response.getOutputStream());
        } catch (IOException e) {
            log.error("导出线损统计 Excel 失败", e);
            throw new RuntimeException("导出失败", e);
        } finally {
            // 关闭
            writer.close();
        }
    }

    /**
     * 将 DTO 列表转换为 VO 列表
     *
     * @param dtoList DTO 列表
     *
     * @return VO 列表
     */
    private List<LineLossStatVO> convertToVOList(List<LineLossRecordDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return new ArrayList<>();
        }

        List<LineLossStatVO> voList = new ArrayList<>();
        for (LineLossRecordDTO dto : dtoList) {
            LineLossStatVO vo = new LineLossStatVO();
            vo.setCollectTime(dto.getCollectTime());
            vo.setLineId(dto.getLineId());
            vo.setLineName(dto.getLineName());
            vo.setFromNodeName(dto.getFromNodeName());
            vo.setToNodeName(dto.getToNodeName());
            vo.setFromNodeLocation(dto.getFromNodeLocation());
            vo.setToNodeLocation(dto.getToNodeLocation());
            vo.setDeviceName(dto.getFromDeviceName() + "-" + dto.getToDeviceName());
            vo.setInputEnergy(dto.getFromUseValue());
            vo.setOutputEnergy(dto.getToUseValue());
            vo.setLossEnergy(dto.getLossValue());
            vo.setLossRate(dto.getLossRate());
            voList.add(vo);
        }

        return voList;
    }
}
