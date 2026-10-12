package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.querygateway;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.dao.AttendanceOperationLogMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.enumeration.OperationObjEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.enumeration.OperationTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.param.AttendanceLogQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.po.AttendanceOperationLog;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.vo.AttendanceOperationLogVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author wql
 * @describe 考勤操作日志查询
 * @date 2023/6/12 15:10
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendanceLogQueryService extends ServiceImpl<AttendanceOperationLogMapper, AttendanceOperationLog> {

    /**
     * @Author wql
     * @Description 考勤管理操作日志记录分页列表
     * @Date 15:41 2023/6/12
     **/
    public Pagination<AttendanceOperationLogVO> getPage(AttendanceLogQueryParam param) {
        Period period = param.getPeriod();
        Page<AttendanceOperationLog> page = this.lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getBusinessType()) && !BusinessTypeEnum.ALL.name()
                        .equals(param.getBusinessType()), AttendanceOperationLog::getBusinessType, param.getBusinessType())
                .like(ObjectUtils.isNotEmpty(param.getUserId()), AttendanceOperationLog::getUserId, param.getUserId())
                .like(StringUtils.isNotBlank(param.getUserName()), AttendanceOperationLog::getUserName, param.getUserName())
                .like(ObjectUtils.isNotEmpty(param.getKeyword()), AttendanceOperationLog::getOperationContent, param.getKeyword())
                .eq(ObjectUtils.isNotEmpty(param.getObjType()), AttendanceOperationLog::getOperationObj, param.getObjType())
                .between(ObjectUtils.isNotEmpty(period), AttendanceOperationLog::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .orderByDesc(AttendanceOperationLog::getCreateTime)
                .page(PagingUtils.convert(param));
        List<AttendanceOperationLogVO> list = new ArrayList<>();
        Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList()).forEach(record -> {
            AttendanceOperationLogVO vo = new AttendanceOperationLogVO();
            ObjectUtils.copying(record, vo);
            vo.setOperationTime(record.getCreateTime());
            vo.setBusinessType(BusinessTypeEnum.getValueByCode(record.getBusinessType()));
            vo.setOperationType(OperationTypeEnum.getValueByCode(record.getOperationType()));
            vo.setOperationObj(OperationObjEnum.getValueByCode(record.getOperationObj()));
            list.add(vo);
        });
        return PagingUtils.convert(page, list);
    }

    /**
     * @Author wql
     * @Description 导出考勤管理操作日志记录列表
     * @Date 16:39 2023/6/13
     **/
    public ExcelWriter export(AttendanceLogQueryParam param) {
        Period period = param.getPeriod();
        List<AttendanceOperationLog> list = this.lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getBusinessType()) && !BusinessTypeEnum.ALL.name()
                        .equals(param.getBusinessType()), AttendanceOperationLog::getBusinessType, param.getBusinessType())
                .like(ObjectUtils.isNotEmpty(param.getUserId()), AttendanceOperationLog::getUserId, param.getUserId())
                .like(StringUtils.isNotBlank(param.getUserName()), AttendanceOperationLog::getUserName, param.getUserName())
                .like(ObjectUtils.isNotEmpty(param.getKeyword()), AttendanceOperationLog::getOperationContent, param.getKeyword())
                .eq(ObjectUtils.isNotEmpty(param.getObjType()), AttendanceOperationLog::getOperationObj, param.getObjType())
                .between(ObjectUtils.isNotEmpty(period), AttendanceOperationLog::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .orderByDesc(AttendanceOperationLog::getCreateTime)
                .list();
        List<AttendanceOperationLogVO> results = new ArrayList<>();
        Optional.ofNullable(list).orElse(Collections.emptyList()).forEach(record -> {
            AttendanceOperationLogVO vo = new AttendanceOperationLogVO();
            ObjectUtils.copying(record, vo);
            vo.setOperationTime(record.getCreateTime());
            vo.setBusinessType(BusinessTypeEnum.getValueByCode(record.getBusinessType()));
            vo.setOperationType(OperationTypeEnum.getValueByCode(record.getOperationType()));
            vo.setOperationObj(OperationObjEnum.getValueByCode(record.getOperationObj()));
            results.add(vo);
        });
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("operationTime", "操作时间");
        writer.addHeaderAlias("userName", "操作人");
        writer.addHeaderAlias("businessType", "业务应用");
        writer.addHeaderAlias("operationType", "操作");
        writer.addHeaderAlias("operationObj", "操作对象");
        writer.addHeaderAlias("operationContent", "操作内容");
        writer.write(results, true);
        return writer;
    }
}
