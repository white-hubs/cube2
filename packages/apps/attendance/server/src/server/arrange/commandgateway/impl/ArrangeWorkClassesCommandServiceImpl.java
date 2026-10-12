package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.ArrangeWorkClassesCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.ClassesMarkEnum;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/23
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArrangeWorkClassesCommandServiceImpl extends ServiceImpl<ArrangeWorkClassesMapper, ArrangeWorkClassesPO> implements ArrangeWorkClassesCommandService {

    private final ArrangeWorkClassesMapper arrangeWorkClassesMapper;

    @Override
    public List<String> saveClassesByOneDay(@NonNull String date, List<Classes> classes) {
        List<String> arrangeWorkClassesIds = new ArrayList<>();
        classes.forEach(c -> {
            ArrangeWorkClassesPO workClassesPO = ObjectUtils.copying(c, ArrangeWorkClassesPO.class);
            if (ClassesMarkEnum.All_DAY_REST.name().equals(workClassesPO.getMark())) {
                return;
            }
            workClassesPO.findRealTime(date);
            workClassesPO.setId(null);
            arrangeWorkClassesMapper.insert(workClassesPO);
            arrangeWorkClassesIds.add(String.valueOf(workClassesPO.getId()));
        });
        return arrangeWorkClassesIds;
    }
}
