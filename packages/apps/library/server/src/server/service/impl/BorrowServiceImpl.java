package com.guhecloud.cube.srv.gen.library.management.server.service.impl;

import com.guhecloud.cube.srv.gen.library.management.common.dto.KeyValueDTO;
import com.guhecloud.cube.srv.gen.library.management.server.enums.BorrowHotTypeEnum;
import com.guhecloud.cube.srv.gen.library.management.server.service.BorrowService;
import com.guhecloud.cube.srv.gen.library.management.server.third.lamp.service.ThirdBorrowService;
import com.guhecloud.cube.srv.gen.library.management.server.vo.param.BorrowRecordPageParam;
import com.guhecloud.cube.srv.gen.library.management.server.vo.result.BorrowRecordResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 借阅服务
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BorrowServiceImpl implements BorrowService {
    private final ThirdBorrowService thirdBorrowService;
    private final RedisTemplate redisTemplate;
    // 图书管理年都借阅记录的redis key
    private static final String LIBRARY_YEAR_BORROW_RECORD_REDIS_KEY = "libraryYearBorrowRecordRedisKey";

    /**
     * 借阅记录分页
     *
     * @param param 入参
     *
     * @return 借阅记录列表
     */
    @Override
    public Pagination<BorrowRecordResult> borrowRecordPage(BorrowRecordPageParam param) {
        return thirdBorrowService.borrowRecordPage(param);
    }

    /**
     * 借阅记录趋势
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @Override
    public List<KeyValueDTO> getBorrowRecordTrend(BorrowRecordPageParam param) {
        List<KeyValueDTO> resultList = new ArrayList<>();
        List<BorrowRecordResult> yearList = this.getYearBorrowRecord(param);
        if (yearList.isEmpty()) {
            return new ArrayList<>();
        }
        Map<String, List<BorrowRecordResult>> map = yearList.stream()
                .collect(Collectors.groupingBy(r -> DateUtils.format(r.getBorrowTime(), "yyyy-MM")));
        map.forEach((yearMonth, list) -> {
            KeyValueDTO dto = new KeyValueDTO();
            dto.setKey(yearMonth);
            dto.setValue(String.valueOf(list.size()));
            resultList.add(dto);
        });

        resultList.sort(Comparator.comparing(KeyValueDTO::getKey).reversed());
        return resultList;
    }

    /**
     * 借阅记录热门top(一年内的)
     *
     * @param param 条件
     *
     * @return 热门
     */
    @Override
    public List<KeyValueDTO> getBorrowRecordHotTop(BorrowRecordPageParam param) {
        List<KeyValueDTO> resultList = new ArrayList<>();
        boolean periodIsNull = param.getPeriod() == null || param.getPeriod().getBegin() == null || param.getPeriod().getEnd() == null;
        Assert.isParameterError(periodIsNull, "时间段不能为空！");

        List<BorrowRecordResult> yearList = this.getYearBorrowRecord(param);
        if (yearList == null || yearList.isEmpty()) {
            return resultList;
        }
        // 获取时间段内的记录，并分组
        Stream<BorrowRecordResult> stream = yearList.stream()
                .filter(r -> r.getBorrowTime() != null &&
                             DateUtils.toTimestamp(r.getBorrowTime()) >= DateUtils.toTimestamp(param.getPeriod().getBegin()) &&
                             DateUtils.toTimestamp(r.getBorrowTime()) <= DateUtils.toTimestamp(param.getPeriod().getEnd()));
        Map<String, List<BorrowRecordResult>> map = new HashMap<>();
        if (BorrowHotTypeEnum.BOOK_NAME.name().equals(param.getHostType())) {
            map = stream.filter(r -> StringUtils.isNotBlank(r.getBookName()))
                    .collect(Collectors.groupingBy(BorrowRecordResult::getBookName));
        } else if (BorrowHotTypeEnum.BOOK_AUTHOR.name().equals(param.getHostType())) {
            map = stream.filter(r -> StringUtils.isNotBlank(r.getBookAuthor()))
                    .collect(Collectors.groupingBy(BorrowRecordResult::getBookAuthor));
        }
        map.forEach((name, list) -> {
            KeyValueDTO dto = new KeyValueDTO();
            dto.setKey(name);
            dto.setValue(String.valueOf(list.size()));
            resultList.add(dto);
        });
        // 排序
        return resultList.parallelStream().sorted(Comparator.comparing(KeyValueDTO::getValue, (a, b) -> {
            int bef = Integer.parseInt(a);
            int aft = Integer.parseInt(b);
            if (bef == aft) {
                return BigDecimal.ZERO.intValue();
            } else if (bef > aft) {
                return -BigDecimal.ONE.intValue();
            }
            return BigDecimal.ONE.intValue();
        })).limit(param.getTopNumber() == null ? BigDecimal.TEN.intValue() : param.getTopNumber()).collect(Collectors.toList());
    }

    /**
     * 获取年度借阅记录(有缓存15分钟)
     *
     * @param param 条件
     *
     * @return 年度借阅记录
     */
    protected List<BorrowRecordResult> getYearBorrowRecord(BorrowRecordPageParam param) {
        List<BorrowRecordResult> resultList = new ArrayList<>();
        BorrowRecordPageParam thirdParam = new BorrowRecordPageParam();
        ObjectUtils.copying(param, thirdParam);
        if (thirdParam.getPeriod() == null || thirdParam.getPeriod().getBegin() == null || thirdParam.getPeriod().getEnd() == null) {
            LocalDateTime nowDate = DateUtils.now();
            Period period = new Period(DateUtils.beginOfYear(nowDate), DateUtils.parse(
                    DateUtils.toTimestamp(DateUtils.endOfYear(nowDate)) - 500));
            thirdParam.setPeriod(period);
        }
        thirdParam.setPageNumber(1);
        thirdParam.setPageSize(100);
        String yearString = DateUtils.format(param.getPeriod().getBegin(), "yyyy");
        String redisTemplateKey = LIBRARY_YEAR_BORROW_RECORD_REDIS_KEY + yearString + param.getStatus();
        ListOperations<String, BorrowRecordResult> listOperations = redisTemplate.opsForList();
        Boolean boolHasKey = redisTemplate.hasKey(redisTemplateKey);
        if (Boolean.TRUE.equals(boolHasKey)) {
            List<BorrowRecordResult> list = listOperations.range(redisTemplateKey, 0, -1);
            if (CollectionUtils.isEmpty(list) || list.get(BigDecimal.ZERO.intValue()) == null) {
                return new ArrayList<>();
            }
            return listOperations.range(redisTemplateKey, 0, -1);
        }

        // 获取年数据
        this.getAllBorrowRecord(resultList, thirdParam);
        boolean resultListIsNull = resultList.isEmpty();
        // 存入缓存
        if (resultListIsNull) {
            resultList.add(null);
        }
        resultList.forEach(r -> listOperations.leftPush(redisTemplateKey, r));
        // TimeUnit.MILLISECONDS毫秒（有效期15分钟）
        redisTemplate.expire(redisTemplateKey, 1000L * 60L * 15L, TimeUnit.MILLISECONDS);

        if (resultListIsNull) {
            return new ArrayList<>();
        }
        return resultList;
    }

    private void getAllBorrowRecord(List<BorrowRecordResult> allList, BorrowRecordPageParam param) {
        Pagination<BorrowRecordResult> pagination = thirdBorrowService.borrowRecordPage(param);
        if (!CollectionUtils.isEmpty(pagination.getRecords())) {
            allList.addAll(pagination.getRecords());
        }
        if (param.getPageNumber() < pagination.getPages()) {
            param.setPageNumber(param.getPageNumber() + 1);
            this.getAllBorrowRecord(allList, param);
        }
    }
}
