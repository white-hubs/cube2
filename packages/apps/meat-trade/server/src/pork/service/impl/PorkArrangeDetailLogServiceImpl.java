package com.guhecloud.cube.biz.meattrade.server.pork.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.PorkArrangeDetailLogMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetailLog;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeDetailLogService;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service("meattrade-porkArrangeDetailLogService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PorkArrangeDetailLogServiceImpl extends ServiceImpl<PorkArrangeDetailLogMapper, PorkArrangeDetailLog> implements PorkArrangeDetailLogService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePorkArrangeDetailLog(@NonNull List<PorkArrangeDetail> porkArrangeDetails) {
        if (!CollectionUtils.isEmpty(porkArrangeDetails)) {
            List<PorkArrangeDetailLog> porkArrangeDetailLogs = ObjectUtils.copying(porkArrangeDetails, PorkArrangeDetailLog.class);
            this.saveBatch(porkArrangeDetailLogs);
        }
    }

    @Override
    public List<PorkArrangeDetailLog> getPorkArrangeDetailLogs(String porkReportCode, String label) {
        return this.baseMapper.selectList(Wrappers.<PorkArrangeDetailLog>lambdaQuery()
                                                  .eq(PorkArrangeDetailLog::getPorkReportCode, porkReportCode)
                                                  .eq(PorkArrangeDetailLog::getLabel, label)
                                                  .orderByDesc(PorkArrangeDetailLog::getPorkArrangeTime));
    }
}
