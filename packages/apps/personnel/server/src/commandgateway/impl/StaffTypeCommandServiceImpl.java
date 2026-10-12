package com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.StaffTypeCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.dao.StaffTypeMapper;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.params.StaffTypeParam;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffTypeVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffTypePO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.BizCodeUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.PinYinUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/5 19:07
 * @Description:
 */
@Service
public class StaffTypeCommandServiceImpl extends ServiceImpl<StaffTypeMapper, StaffTypePO> implements StaffTypeCommandService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateStaffType(StaffTypeVO projectTypeVO) {
        if (Objects.nonNull(projectTypeVO.getId())) {
            StaffTypePO projectTypePO = this.getById(projectTypeVO.getId());
            Assert.isNotFound(Objects.isNull(projectTypePO), "员工类型类型不存在");
            projectTypePO.setName(projectTypeVO.getName());
            projectTypePO.setRemark(projectTypeVO.getRemark());
            return this.updateById(projectTypePO);
        } else {
            StaffTypePO projectTypePO = ObjectUtils.copying(projectTypeVO, StaffTypePO.class);
            projectTypePO.setName(projectTypeVO.getName());
            projectTypePO.setCode(BizCodeUtils.generate(PinYinUtils.getHead(projectTypeVO.getName())));
            projectTypePO.setId(IdWorker.getId());
            return this.save(projectTypePO);
        }
    }

    @Override
    public Pagination<StaffTypeVO> getStaffTypePage(StaffTypeParam param) {
        Page<StaffTypePO> staffTypePOPage = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getName()), StaffTypePO::getName, param.getName())
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(staffTypePOPage, StaffTypeVO.class);
    }

    @Override
    public List<Map<String, String>> getStaffTypeMap() {
        return this.lambdaQuery().list().stream().map(staffTypePO -> {
            Map<String, String> map = new HashMap<>(2);
            map.put("name", staffTypePO.getName());
            map.put("value", staffTypePO.getCode());
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public String valueOfType(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        Optional<StaffTypePO> staffTypePO = this.lambdaQuery()
                .eq(StaffTypePO::getCode, code)
                .list().stream().filter(t -> code.equals(t.getCode())).findFirst();
        return staffTypePO.map(StaffTypePO::getName).orElse(null);
    }

    @Override
    public <T> Map<String, String> getStaffTypeByVOList(List<T> voClassList, String codeField) {
        Map<String, String> staffMap = new HashMap<>();
        if (CollectionUtils.isEmpty(voClassList) || StringUtils.isBlank(codeField)) {
            return staffMap;
        }
        List<String> staffTypeList = new ArrayList<>();
        for (T voClass : voClassList) {
            Field field = ReflectionUtils.findField(voClass.getClass(), codeField);
            if (Objects.isNull(field)) {
                continue;
            }
            field.setAccessible(true);
            Object type = ReflectionUtils.getField(field, voClass);
            if (Objects.nonNull(type)) {
                staffTypeList.add(type.toString());
            }
        }
        if (!CollectionUtils.isEmpty(staffTypeList)) {
            staffMap.putAll(this.lambdaQuery()
                                    .in(StaffTypePO::getCode, staffTypeList)
                                    .list()
                                    .stream().collect(Collectors.toMap(StaffTypePO::getCode, StaffTypePO::getName)));
        }
        return staffMap;
    }

}
