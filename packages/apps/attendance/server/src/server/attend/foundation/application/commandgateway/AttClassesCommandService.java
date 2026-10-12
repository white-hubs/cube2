package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.commandgateway;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.querygateway.AttClassesQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttClassesRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesRecordMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesRecordPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttClassesParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRuleClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttPoAttributeSet;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 考勤班次服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttClassesCommandService extends ServiceImpl<AttClassesMapper, AttClassesPO> {

    private final AttClassesMapper attClassesMapper;
    private final AttClassesRecordMapper attClassesRecordMapper;
    private final AttClassesRepository attClassesRepository;
    private final AttClassesQueryService attClassesQueryService;
    private final AttSchedulingRuleClassesMapper attSchedulingRuleClassesMapper;
    private final AttSchedulingMapper attSchedulingMapper;

    /**
     * 新增班次
     *
     * @param param 班次对象入参
     *
     * @return 对象code
     */
    public Long addAttClasses(AttClassesParam param) {
        // 校验
        param.validBusiness();
        // 简称不能重复
        AttClassesPO nickClass = attClassesMapper.selectOne(Wrappers.<AttClassesPO>lambdaQuery()
                                                                    .eq(AttClassesPO::getNickName, param.getNickName())
                                                                    .last(AttConstants.limitOneSql)
                                                           );
        Assert.isConflict(nickClass != null, "已有【" + param.getNickName() + "】的班次简称！");
        // 拷贝对象
        AttClassesPO po = AttClassesPO.builder().build();
        BeanUtils.copyProperties(param, po);
        // 初始化
        AttPoAttributeSet.initAddBasePoAttribute(po);
        po.setId(IdWorker.getId());
        // 插入
        attClassesRepository.insertAttClasses(po);
        AttClassesRecordPO record = ObjectUtils.copying(param, AttClassesRecordPO.class);
        record.setClassesId(po.getId());
        record.setVersion(1);
        attClassesRecordMapper.insert(record);
        return po.getId();
    }

    /**
     * 编辑班次
     *
     * @param param 班次对象入参
     */
    public void editAttClasses(AttClassesParam param) {
        // 校验
        param.validBusiness();
        // 简称不能重复
        AttClassesPO nickClass = attClassesMapper.selectOne(Wrappers.<AttClassesPO>lambdaQuery()
                                                                    .eq(AttClassesPO::getNickName, param.getNickName())
                                                                    .ne(AttClassesPO::getId, param.getId())
                                                                    .last(AttConstants.limitOneSql)
                                                           );
        Assert.isConflict(nickClass != null, "已有【" + param.getNickName() + "】的班次简称！");
        // 校验使用班次的规则是否交叉
        attClassesQueryService.checkUseRule(param);
        AttClassesPO oldPo = attClassesMapper.selectById(param.getId());
        AttClassesPO newPo = new AttClassesPO();
        BeanUtils.copyProperties(param, newPo);
        attClassesRepository.updateAttClasses(param, oldPo, newPo);
        AttClassesRecordPO record = ObjectUtils.copying(param, AttClassesRecordPO.class);
        AttClassesRecordPO classesRecord = attClassesRecordMapper.selectOne(new LambdaQueryWrapper<AttClassesRecordPO>()
                                                                                    .eq(AttClassesRecordPO::getClassesId, param.getId())
                                                                                    .orderByDesc(AttClassesRecordPO::getVersion)
                                                                                    .last("limit 1"));
        record.setVersion(ObjectUtils.isNotEmpty(classesRecord) ? classesRecord.getVersion() + 1 : 1);
        record.setId(null);
        record.setClassesId(param.getId());
        attClassesRecordMapper.insert(record);
        // 修改班次时，需要修改打卡记录。暂时注释，后续增加特性开关在启用
        //        clockInRecordsRepository.updateClockInByClasses(record);
    }

    /**
     * 删除班次
     *
     * @param id 主键
     */
    public void removeAttClass(Long id) {
        // 清除规则班次
        attSchedulingRuleClassesMapper.remove(DateUtils.toTimestamp(LocalDateTime.now()), Wrappers.<AttSchedulingRuleClassesPO>lambdaQuery()
                                                      .in(AttSchedulingRuleClassesPO::getId, id)
                                             );
        // 清除当前日期之后的手动班次排班
        attSchedulingMapper.remove(DateUtils.toTimestamp(LocalDateTime.now()), Wrappers.<AttSchedulingPO>lambdaQuery()
                                           .eq(AttSchedulingPO::getClassesId, id)
                                           .gt(AttSchedulingPO::getSchedulingDate, LocalDateTime.now())
                                  );
        // 清除班次
        AttClassesPO classesPO = attClassesMapper.selectById(id);
        attClassesRepository.deleteAttClasses(classesPO);
    }

}
