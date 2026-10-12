package com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.dao.RiskSourceLedgerExtendInfoMapper;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.entity.RiskSourceLedgerExtendInfo;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.RiskSourceLedgerExtendInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: lijie
 * @CreateTime: 2024-11-08  10:33
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskSourceLedgerExtendInfoServiceImpl extends ServiceImpl<RiskSourceLedgerExtendInfoMapper, RiskSourceLedgerExtendInfo> implements RiskSourceLedgerExtendInfoService {
}
