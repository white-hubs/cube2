package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.DutyWorkClassesCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.DutyWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.ClassesMarkEnum;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/29
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DutyWorkClassesCommandServiceImpl extends ServiceImpl<DutyWorkClassesMapper, DutyWorkClassesPO> implements DutyWorkClassesCommandService {

    private final DutyWorkClassesMapper dutyWorkClassesMapper;

    @Override
    public List<String> saveClassesByOneDay(List<Classes> classes) {
        List<String> dutyWorkClassesIds = new ArrayList<>();
        classes.forEach(c -> {
            DutyWorkClassesPO workClassesPO = ObjectUtils.copying(c, DutyWorkClassesPO.class);
            if (ClassesMarkEnum.All_DAY_REST.name().equals(workClassesPO.getMark())) {
                return;
            }
            workClassesPO.setId(null);
            dutyWorkClassesMapper.insert(workClassesPO);
            dutyWorkClassesIds.add(String.valueOf(workClassesPO.getId()));
        });
        return dutyWorkClassesIds;
    }
}
