package com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.ClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.ClassesMarkEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.ClassesVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.ClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.query.ClassesQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/11/10
 * @description 班次查询
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClassesQueryService extends ServiceImpl<ClassesMapper, ClassesPO> {

    private final OrganizationClient organizationClient;
    private final AttendanceGroupMapper attendanceGroupMapper;

    public List<ClassesVO> getList(ClassesQueryParam param) {
        if (param != null && param.getGroupId() != null) {
            AttendanceGroupPO groupPO = attendanceGroupMapper.selectById(param.getGroupId());
            if (groupPO != null && StringUtils.isNotEmpty(groupPO.getClassesIds())) {
                List<Long> classList = Arrays.stream(groupPO.getClassesIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
                param.setClassIdList(classList);
            }
        }
        List<ClassesPO> classesPOS = this.getQueryChainWrapper(param).list();
        List<ClassesVO> classesVOS = ObjectUtils.copying(classesPOS, ClassesVO.class);
        classesVOS.parallelStream().forEach(this::extend);
        return classesVOS;
    }

    public Pagination<ClassesVO> getPage(ClassesQueryParam param) {
        Page<ClassesPO> page = this.getQueryChainWrapper(param)
                .page(PagingUtils.convert(param));
        Pagination<ClassesVO> pagination = PagingUtils.convert(page, ClassesVO.class);
        pagination.getRecords().forEach(this::extend);
        return pagination;
    }

    private LambdaQueryChainWrapper<ClassesPO> getQueryChainWrapper(ClassesQueryParam param) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), ClassesPO::getName, param.getKeyword())
                .like(param.getOrganizationId() != null, ClassesPO::getSuitOrganizationIds, param.getOrganizationId())
                .in(CollectionUtils.isNotEmpty(param.getClassIdList()), ClassesPO::getId, param.getClassIdList())
                .ne(param.getHasRest() != null && !param.getHasRest(), ClassesPO::getMark, ClassesMarkEnum.All_DAY_REST.name())
                .orderByDesc(BasicEntity::getUpdateTime)
                ;
    }

    private void extend(ClassesVO classesVO) {
        classesVO.setSummaryName(classesVO.getName() +
                                 (StringUtils.isNotEmpty(classesVO.getStartWorkTime()) ? (":" + classesVO.getStartWorkTime() + "-" +
                                                                                          classesVO.getEndWorkTime()) : ""));
        if (StringUtils.isNotEmpty(classesVO.getSuitOrganizationIds())) {
            List<OrganizationDTO> organizationDTOS = organizationClient.getOrganizationsByIds(Arrays.stream(classesVO
                                                                                                                    .getSuitOrganizationIds()
                                                                                                                    .split(","))
                                                                                                      .map(Long::valueOf)
                                                                                                      .collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(organizationDTOS)) {
                classesVO.setOrganizationNames(organizationDTOS.stream().map(OrganizationDTO::getName).collect(Collectors.joining("、")));
            }
        }
    }

}
