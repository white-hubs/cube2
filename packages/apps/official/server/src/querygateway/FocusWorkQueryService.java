package com.guhecloud.cube.srv.gen.official.management.server.querygateway;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.util.CustomCellWriteUtil;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.FocusWorkFailMapper;
import com.guhecloud.cube.srv.gen.official.management.server.dao.FocusWorkMapper;
import com.guhecloud.cube.srv.gen.official.management.server.dao.FocusWorkReplyMapper;
import com.guhecloud.cube.srv.gen.official.management.server.enumeration.FocusWorkStatus;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.FocusWorkFailVO;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkFailPO;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkPO;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkReplyPO;
import com.guhecloud.cube.srv.gen.official.management.server.querygateway.dto.FocusWorkDTO;
import com.guhecloud.cube.srv.gen.official.management.server.querygateway.dto.FocusWorkExcelDTO;
import com.guhecloud.cube.srv.gen.official.management.server.querygateway.query.FocusWorkParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FocusWorkQueryService extends ServiceImpl<FocusWorkMapper, FocusWorkPO> {
    private final FocusWorkReplyMapper focusWorkReplyMapper;
    private final FocusWorkMapper focusWorkMapper;
    private final FocusWorkFailMapper focusWorkFailMapper;
    private final FileClient fileClient;

    /**
     * 督办工作列表
     * 附带流程信息 以便前端发起流程
     *
     * @param param
     *
     * @return
     */
    public Pagination<FocusWorkDTO> pageFocusWork(FocusWorkParam param) {
        Period period = param.getPeriod();

        Page<FocusWorkPO> page = this.page(PagingUtils.convert(param), Wrappers.<FocusWorkPO>lambdaQuery()
                .like(StringUtils.isNotBlank(param.getKeyword()), FocusWorkPO::getWorkDesc, param.getKeyword())
                .eq(param.getStatus() != null, FocusWorkPO::getStatus, param.getStatus())
                .like(StringUtils.isNotBlank(param.getDeptId()), FocusWorkPO::getDeptId, param.getDeptId())
                .like(StringUtils.isNotBlank(param.getFirstUserId()), FocusWorkPO::getFirstUserId, param.getFirstUserId())
                .like(StringUtils.isNotBlank(param.getMergeReferUser()), FocusWorkPO::getMergeReferUsers, param.getMergeReferUser())
                .and(period != null, q ->
                        q.between(FocusWorkPO::getCompleteTime, period.getBegin(), period.getEnd())
                                .or()
                                .between(FocusWorkPO::getUpdateCompleteTime, period.getBegin(), period.getEnd()))
                .orderByDesc(FocusWorkPO::getUpdateTime));
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            List<FocusWorkDTO> collect = page.getRecords().stream().map(d -> {
                FocusWorkDTO dto = ObjectUtils.copying(d, FocusWorkDTO.class);
                if (FocusWorkStatus.PROCESSING.name().equals(dto.getStatus())) {
                    var millis = 0l;
                    if (d.getUpdateCompleteTime() != null) {
                        millis = DateUtils.betweenMs(LocalDateTime.now(), d.getUpdateCompleteTime());
                    } else if (d.getCompleteTime() != null) {
                        millis = DateUtils.betweenMs(LocalDateTime.now(), d.getCompleteTime());
                    }
                    if (millis > 0) {
                        dto.setTimeLimitDesc(DateUtils.formatBetween(millis, ChronoUnit.HOURS));
                    }
                }
                return dto;
            }).collect(Collectors.toList());
            return PagingUtils.convert(page, collect);
        }
        return Pagination.empty();
    }

    public void focusWorkExcelExtendFlow(FocusWorkParam param) {
        Period period = param.getPeriod();
        List<FocusWorkPO> focusWorkPOList = super.list(Wrappers.<FocusWorkPO>lambdaQuery()
                                                               .like(StringUtils.isNotBlank(param.getKeyword()), FocusWorkPO::getWorkDesc, param.getKeyword())
                                                               .eq(param.getStatus() != null, FocusWorkPO::getStatus, param.getStatus())
                                                               .like(StringUtils.isNotBlank(param.getDeptId()), FocusWorkPO::getDeptId, param.getDeptId())
                                                               .like(StringUtils.isNotBlank(param.getFirstUserId()), FocusWorkPO::getFirstUserId, param.getFirstUserId())
                                                               .like(StringUtils.isNotBlank(param.getMergeReferUser()), FocusWorkPO::getMergeReferUsers, param.getMergeReferUser())
                                                               .and(period != null, q ->
                                                                       q
                                                                               .between(FocusWorkPO::getCompleteTime, period.getBegin(), period.getEnd())
                                                                               .or()
                                                                               .between(FocusWorkPO::getUpdateCompleteTime, period.getBegin(), period.getEnd()))
                                                               .orderByDesc(FocusWorkPO::getUpdateTime));
        Assert.parameterIsNull(focusWorkPOList, "暂无数据，无法导出");
        List<FocusWorkExcelDTO> records = ConvertUtils.toList(FocusWorkExcelDTO.class, focusWorkPOList);
        Assert.parameterIsNull(records, "暂无数据，无法导出");
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        response.setCharacterEncoding("utf-8");
        String excelName = null;
        try {
            excelName = URLEncoder.encode("事项处理统计", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
            EasyExcel.write(response.getOutputStream(), FocusWorkExcelDTO.class)
                    .registerWriteHandler(new CustomCellWriteUtil())
                    .sheet("事项处理统计")
                    .doWrite(records);
        } catch (Exception e) {
            log.debug("official:事项处理导出异常：", e);
            throw new ServiceException("下载异常");
        }
    }

    /**
     * 督办工作详情
     * 附带最新反馈信息
     *
     * @param focusWorkId
     *
     * @return
     */
    public FocusWorkReplyPO getLastestFocusWorkReply(Long focusWorkId) {
        List<FocusWorkReplyPO> list = focusWorkReplyMapper.selectList(Wrappers.<FocusWorkReplyPO>lambdaQuery()
                                                                              .eq(FocusWorkReplyPO::getFocusWorkId, focusWorkId)
                                                                              .orderByDesc(FocusWorkReplyPO::getCreateTime));
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    public List<FocusWorkReplyPO> getReplyByFocusWorkid(Long focusWorkId) {
        List<FocusWorkReplyPO> list = focusWorkReplyMapper.selectList(Wrappers.<FocusWorkReplyPO>lambdaQuery()
                                                                              .eq(FocusWorkReplyPO::getFocusWorkId, focusWorkId)
                                                                              .orderByDesc(FocusWorkReplyPO::getCreateTime));
        return list;
    }

    public List<Map> getFirstUsersInFocusWork() {
        List<Map> users = this.focusWorkMapper.getFirstUsersInFocusWork();
        List<Map> newDatas = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(users)) {
            for (Map data : users) {
                String[] ids = data.get("id").toString().split(",");
                String[] names = data.get("name").toString().split(",");
                for (int i = 0; i < ids.length && i < names.length; i++) {
                    Map<String, String> newData = new HashMap<>();
                    newData.put("id", ids[i]);
                    newData.put("name", names[i]);
                    newDatas.add(newData);
                }
            }
        }
        return newDatas.stream().distinct().collect(Collectors.toList());
    }

    public FocusWorkFailVO getFocusWorkFailVO(Long focusWorkId) {
        FocusWorkFailPO failPO = this.focusWorkFailMapper.selectOne(Wrappers.<FocusWorkFailPO>lambdaQuery()
                                                                            .eq(FocusWorkFailPO::getFocusWorkId, focusWorkId));
        if (failPO != null) {
            FocusWorkFailVO vo = ObjectUtils.copying(failPO, FocusWorkFailVO.class);
            //填充文件
            if (StringUtils.isNotBlank(vo.getFileIds())) {
                List<Long> fileIds = Arrays.stream(vo.getFileIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
                List<FileVO> fileList = fileClient.getByIds(fileIds);
                vo.setFileList(fileList);
            }
            return vo;
        }
        return null;
    }
}
