package com.guhecloud.cube.biz.mdata.server.bizlog.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.guhecloud.cube.biz.mdata.client.BizLogClient;
import com.guhecloud.cube.biz.mdata.client.vo.BizLogRecordDTO;
import com.guhecloud.cube.biz.mdata.server.bizlog.param.BizLogParam;
import com.guhecloud.cube.biz.mdata.server.bizlog.param.BizLogRecordParam;
import com.guhecloud.cube.biz.mdata.server.bizlog.service.BizLogRecordService;
import com.guhecloud.cube.biz.mdata.server.bizlog.vo.BizLogRecordVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 业务日志
 *
 * @author appet
 * @date 2021/12/6 19:56
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BizLogRecordController {
    private final BizLogRecordService bizLogRecordService;
    private final BizLogClient bizLogClient;
    private final RedisTemplate redisTemplate;

    /**
     * 业务日志查询
     */
    @GetMapping("/biz-log-record/page")
    public Pagination<BizLogRecordVO> queryBizLogRecord(BizLogRecordParam param) {
        return bizLogRecordService.queryBizLogRecord(param);
    }

    @GetMapping("/biz-log-record/login/page")
    public Pagination<BizLogRecordVO> queryBizLogRecordForLogin(BizLogRecordParam param) {
        return bizLogRecordService.queryBizLogRecordForLogin(param);
    }

    /**
     * 多个bizCode用逗号分割
     *
     * @param bizCodes
     *
     * @return
     */
    @GetMapping("/biz-log-record/biz")
    public List<LogRecordBiz> queryBizLogBiz(String bizCodes) {
        List<LogRecordBiz> allLogRecordBiz = new ArrayList<>();
        ListOperations<String, Object> listOps = this.redisTemplate.opsForList();
        List<Object> allLogRecordAnnos = listOps.range("allLogRecordAnnos", 0, -1);
        if (!CollectionUtils.isEmpty(allLogRecordAnnos)) {
            Map<String, List<BizLogParam>> group = allLogRecordAnnos.stream().distinct()
                    .map(r -> {
                        String[] split = r.toString().split("-");
                        return BizLogParam.builder().type(split[0]).typeName(split[1]).subType(split[2]).subTypeName(split[3]).build();
                    }).collect(Collectors.groupingBy(r -> r.getType() + "-" + r.getTypeName()));
            group.entrySet().stream().forEach(entry -> {
                String key = entry.getKey();
                String[] split = key.split("-");
                LogRecordBiz biz = LogRecordBiz.builder().code(split[0]).value(split[1]).build();
                //组装子集
                List<BizLogParam> values = entry.getValue();
                List<LogRecordBiz> subFuns = values.stream()
                        .map(v -> LogRecordBiz.builder().code(v.getSubType()).value(v.getSubTypeName()).build())
                        .collect(Collectors.toList());
                biz.setChildren(subFuns);
                if (StringUtils.isBlank(bizCodes)
                    || (StringUtils.isNotBlank(bizCodes) && bizCodes.indexOf(biz.getCode()) > -1)) {
                    allLogRecordBiz.add(biz);
                }
            });
        }
        log.info("业务日志涉及的业务：" + JsonUtils.serialize(allLogRecordBiz));
        return allLogRecordBiz;
    }

    @DeleteMapping("/biz-log-record/duplication/clear")
    public void clearBizLogBiz() {
        bizLogRecordService.clearBizLogInRedis();
    }

    /**
     * 根据subtype清理
     *
     * @param clearCode
     */
    @DeleteMapping("/biz-log-record/clear")
    public void clearBizLogRedis(@RequestParam("clearCode") String clearCode) {
        if (!"all".equals(clearCode)) {
            ListOperations<String, Object> listOps = this.redisTemplate.opsForList();
            List<Object> allLogRecordAnnos = listOps.range("allLogRecordAnnos", 0, -1);
            List<Object> collect = allLogRecordAnnos.stream().distinct().collect(Collectors.toList());
            //先删除所有数据
            this.redisTemplate.delete("allLogRecordAnnos");
            //过滤调要清理的code
            if (StringUtils.isNotBlank(clearCode)) {
                collect = collect.stream().filter(c -> c.toString().indexOf(clearCode) < 0).collect(Collectors.toList());
            }
            //放入去重后的数据
            listOps.leftPushAll("allLogRecordAnnos", collect);
        } else {
            this.redisTemplate.delete("allLogRecordAnnos");
        }
    }

    /**
     * 手动插入
     *
     * @param biz
     */
    @PostMapping("/biz-log-record/add")
    public void addBizLogRedis(@RequestParam("biz") String biz) {
        ListOperations<String, Object> listOps = this.redisTemplate.opsForList();
        listOps.rightPush("allLogRecordAnnos", biz);
    }

    @Data
    @Builder
    public static class LogRecordBiz {
        private String code;
        private String value;
        private List<LogRecordBiz> children;
    }

    /**
     * client测试
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/biz-log-record/client/test")
    public Pagination<BizLogRecordVO> queryBizLogRecordForTest(BizLogRecordParam param) {
        com.guhecloud.cube.biz.mdata.client.param.BizLogRecordParam copying = ObjectUtils.copying(param, com.guhecloud.cube.biz.mdata.client.param.BizLogRecordParam.class);
        Pagination<BizLogRecordDTO> page = bizLogClient.queryBizLogRecord(copying);
        return PagingUtils.convert(page, ObjectUtils.copying(page.getRecords(), BizLogRecordVO.class));
    }

    @PostMapping("/export/biz-log-record/biz")
    public void bizLogRecordtExport(@RequestBody BizLogRecordParam param, HttpServletRequest request, HttpServletResponse response) {
        Period period = param.getPeriod();
        if (period == null) {
            //默认7天数据
            LocalDateTime endTime = DateUtils.endOfDay(DateUtils.now());
            LocalDateTime startTime = DateUtils.beginOfDay(DateUtils.offsetDay(endTime, -6));
            param.setPeriod(Period.of(startTime, endTime));
        }
        List<BizLogRecordVO> datas = bizLogRecordService.getBizLogRecordList(param);
        Assert.isNotFound(CollectionUtils.isEmpty(datas), "未查询到数据，无法导出");
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("createTime", "操作时间");
        writer.addHeaderAlias("userName", "操作人");
        writer.addHeaderAlias("typeName", "业务应用");
        writer.addHeaderAlias("operationType", "操作");
        writer.addHeaderAlias("subTypeName", "操作对象");
        writer.addHeaderAlias("action", "操作内容");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "业务日志";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    @PostMapping("/export/biz-log-record/login")
    public void loginRecordExport(@RequestBody BizLogRecordParam param, HttpServletRequest request, HttpServletResponse response) {
        Period period = param.getPeriod();
        if (period == null) {
            //默认7天数据
            LocalDateTime endTime = DateUtils.endOfDay(DateUtils.now());
            LocalDateTime startTime = DateUtils.beginOfDay(DateUtils.offsetDay(endTime, -6));
            param.setPeriod(Period.of(startTime, endTime));
        }
        List<BizLogRecordVO> datas = bizLogRecordService.getBizLogRecordListForLogin(param);
        Assert.isNotFound(CollectionUtils.isEmpty(datas), "未查询到数据，无法导出");

        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("createTime", "登录时间");
        writer.addHeaderAlias("userName", "用户");
        writer.addHeaderAlias("extra", "登录平台");
        writer.addHeaderAlias("action", "登录状态");
        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "登录日志";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }
}