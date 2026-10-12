package com.guhecloud.cube.biz.mdata.server.bizlog.service.impl;

import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.bizlog.entity.BizLogRecord;
import com.guhecloud.cube.biz.mdata.server.bizlog.enumeration.OperationTypeEnum;
import com.guhecloud.cube.biz.mdata.server.bizlog.mapper.BizLogRecordMapper;
import com.guhecloud.cube.biz.mdata.server.bizlog.param.BizLogRecordParam;
import com.guhecloud.cube.biz.mdata.server.bizlog.service.BizLogRecordService;
import com.guhecloud.cube.biz.mdata.server.bizlog.vo.BizLogRecordVO;
import com.guhecloud.cube.biz.ouaa.client.TerminalClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TerminalDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.cloud.logger.message.BizLogRecordDTO;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BizLogRecordServiceImpl extends ServiceImpl<BizLogRecordMapper, BizLogRecord> implements BizLogRecordService {
    private final UserClient userClient;
    private final TerminalClient terminalClient;
    private final RedisTemplate redisTemplate;

    @Override
    public void addBizLog(BizLogRecordDTO bizLogRecordDTO) {
        BizLogRecord bizLogRecord = ObjectUtils.copying(bizLogRecordDTO, BizLogRecord.class);
        bizLogRecord.setTenant(SessionContextHolder.getRequester().getTenant());
        if ("LOGIN".equals(bizLogRecord.getSubType())) {//如果是登录，记录来自哪个端
            if (StringUtils.isNotBlank(SessionContextHolder.getRequester().getTerminal())) {
                TerminalDTO terminal = terminalClient.getTerminalByCode(SessionContextHolder.getRequester().getTerminal());
                bizLogRecord.setExtra(terminal != null ? terminal.getName() : "");
                if (StringUtils.isBlank(bizLogRecord.getUserName())) {
                    UserDTO user = userClient.getUserByUserName(bizLogRecord.getAccount());
                    bizLogRecord.setUserName(user != null ? user.getName() : "");
                }
            }
        }
        this.baseMapper.insert(bizLogRecord);
    }

    @Override
    public Pagination<BizLogRecordVO> queryBizLogRecord(BizLogRecordParam param) {
        Period period = param.getPeriod();
        Page<BizLogRecord> page = this.baseMapper.selectPage(PagingUtils.convert(param), Wrappers.<BizLogRecord>lambdaQuery()
                                                                     .like(StringUtils.isNotBlank(param.getAccount()), BizLogRecord::getAccount, param.getAccount())
                                                                     .like(StringUtils.isNotBlank(param.getUserName()), BizLogRecord::getUserName, param.getUserName())
                                                                     .eq(param.getFail() != null, BizLogRecord::getFail, param.getFail())
                                                                     .eq(StringUtils.isNotBlank(param.getType()), BizLogRecord::getType, param.getType())
                                                                     .eq(StringUtils.isNotBlank(param.getSubType()), BizLogRecord::getSubType, param.getSubType())
                                                                     .ne(StringUtils.isBlank(param.getType()), BizLogRecord::getType, "OUAA")
                                                                     .ne(StringUtils.isBlank(param.getSubType()), BizLogRecord::getSubType, "LOGIN")
                                                                     .eq(StringUtils.isNotBlank(param.getOperationType()), BizLogRecord::getOperationType, param.getOperationType())
                                                                     .eq(BizLogRecord::getTenant, SessionContextHolder.getRequester().getTenant())
                                                                     .between(period !=
                                                                              null, BizLogRecord::getCreateTime, ObjectUtils.ifNull(period, p -> p.getBegin()), ObjectUtils.ifNull(period, p -> p.getEnd()))
                                                            );
        return PagingUtils.convert(page, extendInfo(page.getRecords()));
    }

    @Override
    public Pagination<BizLogRecordVO> queryBizLogRecordForLogin(BizLogRecordParam param) {
        Period period = param.getPeriod();
        Page<BizLogRecord> page = this.baseMapper.selectPage(PagingUtils.convert(param), Wrappers.<BizLogRecord>lambdaQuery()
                                                                     .like(StringUtils.isNotBlank(param.getAccount()), BizLogRecord::getAccount, param.getAccount())
                                                                     .like(StringUtils.isNotBlank(param.getUserName()), BizLogRecord::getUserName, param.getUserName())
                                                                     .eq(param.getFail() != null, BizLogRecord::getFail, param.getFail())
                                                                     .eq(BizLogRecord::getType, "OUAA")
                                                                     .eq(BizLogRecord::getSubType, "LOGIN")
                                                                     .eq(StringUtils.isNotBlank(param.getOperationType()), BizLogRecord::getOperationType, param.getOperationType())
                                                                     .between(period !=
                                                                              null, BizLogRecord::getCreateTime, ObjectUtils.ifNull(period, p -> p.getBegin()), ObjectUtils.ifNull(period, p -> p.getEnd()))
                                                                     .orderByDesc(BizLogRecord::getCreateTime)
                                                            );
        return PagingUtils.convert(page, extendInfo(page.getRecords()));
    }

    private List<BizLogRecordVO> extendInfo(List<BizLogRecord> datas) {
        if (CollectionUtils.isNotEmpty(datas)) {
            return datas.stream().map(r -> {
                BizLogRecordVO br = ObjectUtils.copying(r, BizLogRecordVO.class);
                if (StringUtils.isNotBlank(br.getOperationType())) {
                    br.setOperationType(OperationTypeEnum.valueOf(br.getOperationType()).getLabel());
                }
                if (StringUtils.isBlank(br.getUserName())) {
                    UserDTO user = userClient.getUserByUserName(br.getAccount());
                    br.setUserName(user != null ? user.getName() : "");
                }
                return br;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<BizLogRecordVO> getBizLogRecordList(BizLogRecordParam param) {
        Period period = param.getPeriod();
        List<BizLogRecord> datas = this.list(Wrappers.<BizLogRecord>lambdaQuery()
                                                     .like(StringUtils.isNotBlank(param.getAccount()), BizLogRecord::getAccount, param.getAccount())
                                                     .like(StringUtils.isNotBlank(param.getUserName()), BizLogRecord::getUserName, param.getUserName())
                                                     .eq(param.getFail() != null, BizLogRecord::getFail, param.getFail())
                                                     .eq(StringUtils.isNotBlank(param.getType()), BizLogRecord::getType, param.getType())
                                                     .eq(StringUtils.isNotBlank(param.getSubType()), BizLogRecord::getSubType, param.getSubType())
                                                     .ne(StringUtils.isBlank(param.getType()), BizLogRecord::getType, "OUAA")
                                                     .ne(StringUtils.isBlank(param.getSubType()), BizLogRecord::getSubType, "LOGIN")
                                                     .eq(StringUtils.isNotBlank(param.getOperationType()), BizLogRecord::getOperationType, param.getOperationType())
                                                     .eq(BizLogRecord::getTenant, SessionContextHolder.getRequester().getTenant())
                                                     .between(period !=
                                                              null, BizLogRecord::getCreateTime, ObjectUtils.ifNull(period, p -> p.getBegin()), ObjectUtils.ifNull(period, p -> p.getEnd()))
                                                     .orderByDesc(BizLogRecord::getCreateTime)
                                            );
        return this.extendInfo(datas);
    }

    @Override
    public List<BizLogRecordVO> getBizLogRecordListForLogin(BizLogRecordParam param) {
        Period period = param.getPeriod();
        List<BizLogRecord> datas = this.baseMapper.selectList(Wrappers.<BizLogRecord>lambdaQuery()
                                                                      .like(StringUtils.isNotBlank(param.getAccount()), BizLogRecord::getAccount, param.getAccount())
                                                                      .like(StringUtils.isNotBlank(param.getUserName()), BizLogRecord::getUserName, param.getUserName())
                                                                      .eq(param.getFail() != null, BizLogRecord::getFail, param.getFail())
                                                                      .eq(BizLogRecord::getType, "OUAA")
                                                                      .eq(BizLogRecord::getSubType, "LOGIN")
                                                                      .eq(StringUtils.isNotBlank(param.getOperationType()), BizLogRecord::getOperationType, param.getOperationType())
                                                                      .between(period !=
                                                                               null, BizLogRecord::getCreateTime, ObjectUtils.ifNull(period, p -> p.getBegin()), ObjectUtils.ifNull(period, p -> p.getEnd()))
                                                                      .orderByDesc(BizLogRecord::getCreateTime)
                                                             );
        return this.extendInfo(datas);
    }

    @Override
    public void clearBizLogInRedis() {
        ListOperations<String, Object> listOps = this.redisTemplate.opsForList();
        List<Object> allLogRecordAnnos = listOps.range("allLogRecordAnnos", 0, -1);
        List<Object> collect = allLogRecordAnnos.stream().distinct().collect(Collectors.toList());
        //先删除所有数据
        this.redisTemplate.delete("allLogRecordAnnos");
        //放入去重后的数据
        listOps.leftPushAll("allLogRecordAnnos", collect);
    }

}
