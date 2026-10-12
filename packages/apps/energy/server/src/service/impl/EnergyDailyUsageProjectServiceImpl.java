package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.AreaProjectMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyDailyUsageProjectMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyProjectItemMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyProjectMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.AreaProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailyUsageProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.BindTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyDailyUsageProjectService;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.TimeStatUtil;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageProjectResultVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectAreaStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendStatVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 描述：能耗分项的日报服务
 *
 * @author appet
 * @date 2021-12-30 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyDailyUsageProjectServiceImpl extends ServiceImpl<EnergyDailyUsageProjectMapper, EnergyDailyUsageProject> implements EnergyDailyUsageProjectService {
    private final AreaProjectMapper areaProjectMapper;
    private final EnergyProjectMapper energyProjectMapper;
    private final EnergyProjectItemMapper energyProjectItemMapper;
    private final EnergyDailyUsageProjectMapper energyDailyUsageProjectMapper;

    /**
     * 分项分页列表
     *
     * @param param 条件
     *
     * @return 分页
     */
    @Override
    public Pagination<EnergyDailyUsageProjectResultVO> getProjectPage(EnergyDailyUsageParam param) {
        Period period = param.getPeriod();
        // 获取一级id
        List<Long> projectIdList = new ArrayList<>();
        projectIdList.add(0L);
        if (StringUtils.isBlank(param.getProjectCategory())) {
            projectIdList.addAll(this.getAreaTopId());
            projectIdList.addAll(this.getProjectTopId());
        } else if (BindTypeEnum.ITEM.name().equals(param.getProjectCategory())) {
            projectIdList.addAll(this.getProjectTopId());
        } else if (BindTypeEnum.AREA.name().equals(param.getProjectCategory())) {
            projectIdList.addAll(this.getAreaTopId());
        }
        Page<EnergyDailyUsageProjectResultVO> page = energyDailyUsageProjectMapper.getProjectPage(PagingUtils.convert(param), Wrappers
                                                                                                          .<EnergyDailyUsageProject>lambdaQuery()
                                                                                                          .eq(param.getProjectId() != null, EnergyDailyUsageProject::getProjectId, param.getProjectId())
                                                                                                          .eq(StringUtils.isNotBlank(param.getProjectCategory()), EnergyDailyUsageProject::getProjectCategory, param.getProjectCategory())
                                                                                                          .eq(StringUtils.isNotBlank(param.getInstrumentType()), EnergyDailyUsageProject::getInstrumentType, param.getInstrumentType())
                                                                                                          .between(period !=
                                                                                                                   null, EnergyDailyUsageProject::getStatDate, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                                                          .in(EnergyDailyUsageProject::getProjectId, projectIdList)
                                                                                                          .orderByAsc(EnergyDailyUsageProject::getStatDate)
                                                                                                 );

        return PagingUtils.convert(page);

    }

    @Override
    public List<TrendStatVO> getProjectUsageStatForTrend(EnergyTrendUsageParam param) {
        if (StringUtils.isNotEmpty(param.getProjectIds())) {
            List<Long> projectIdList = Arrays.stream(param.getProjectIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
            param.setProjectIdList(projectIdList);
        }
        TimeStatUtil.initStatParam(param);
        return this.baseMapper.getProjectUsageStatForTrend(param);
    }

    @Override
    public List<TrendStatVO> getProjectUsageStatByTime(EnergyTrendUsageParam param) {
        if (StringUtils.isNotEmpty(param.getProjectIds())) {
            List<Long> projectIdList = Arrays.stream(param.getProjectIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
            param.setProjectIdList(projectIdList);
        }
        TimeStatUtil.initStatParam(param);
        return this.baseMapper.getProjectUsageStatByTime(param);
    }

    @Override
    public List<TrendStatVO> getProjectUsageStatByCategory(EnergyTrendUsageParam param) {
        if (StringUtils.isNotEmpty(param.getProjectIds())) {
            List<Long> projectIdList = Arrays.stream(param.getProjectIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
            param.setProjectIdList(projectIdList);
        }
        TimeStatUtil.initStatParam(param);
        if (BindTypeEnum.ITEM.name().equals(param.getProjectCategory())) {
            // 先看系统是否有分项配置，如果没有就直接返回空
            List<EnergyProject> energyProjectList = energyProjectMapper.selectList(Wrappers.emptyWrapper());
            if (CollectionUtils.isEmpty(energyProjectList)) {
                return Collections.emptyList();
            }
            if (CollectionUtils.isEmpty(param.getProjectIdList())) {
                List<Long> energyProjectIdList = energyProjectList.stream().map(EnergyProject::getId).filter(Objects::nonNull).distinct()
                        .collect(Collectors.toList());
                // 如果没有分项条件就默认全部分项
                param.setProjectIdList(energyProjectIdList);
            }
            return this.baseMapper.getProjectUsageStatByItem(param);

        } else if (BindTypeEnum.AREA.name().equals(param.getProjectCategory())) {
            List<AreaProject> areaProjectList = areaProjectMapper.selectList(Wrappers.emptyWrapper());
            if (CollectionUtils.isEmpty(areaProjectList)) {
                return Collections.emptyList();
            }
            if (CollectionUtils.isEmpty(param.getProjectIdList())) {
                List<Long> areaProjectIdList = areaProjectList.stream().map(AreaProject::getId).filter(Objects::nonNull).distinct()
                        .collect(Collectors.toList());
                param.setProjectIdList(areaProjectIdList);
            }
            return this.baseMapper.getProjectUsageStatByArea(param);
        }
        return this.baseMapper.getProjectUsageStatByCategory(param);
    }

    /**
     * 参考数据格式 https://echarts.apache.org/examples/zh/editor.html?c=sankey-simple
     *
     * @param param
     *
     * @return
     */
    @Override
    public Map<String, List<Map<String, String>>> getProjectUsageStatByTypeAndProject(EnergyTrendUsageParam param) {
        TimeStatUtil.initStatParam(param);
        List<EnergyProjectStatVO> dataList = this.baseMapper.getProjectUsageStatByTypeAndProject(param);
        //转换数据
        if (!CollectionUtils.isEmpty(dataList)) {
            //查询区域分项
            List<AreaProject> areaProjects = areaProjectMapper.selectList(null);
            Map<String, String> areaProjectMap = areaProjects.stream()
                    .collect(Collectors.toMap(a -> a.getId().toString(), a -> a.getAreaName()));
            List<Map<String, String>> linkList = dataList.stream().map(data -> {
                if (StringUtils.isEmpty(data.getParentId())) {
                    //parentId为空 说明是一级分项 生成一个ID，方面前端构建图形
                    data.setParentName(InstrumentTypeEnum.getFireinspEnum(data.getInstrumentType()));
                } else {
                    data.setParentName(areaProjectMap.get(data.getParentId()));
                }
                Map<String, String> map = new HashMap<>();
                map.put("source", data.getParentName());
                map.put("target", data.getProjectName());
                map.put("value", data.getValue());
                return map;
            }).collect(Collectors.toList());
            //构建前端需要的data
            List<Map<String, String>> itemList = dataList.stream().map(data -> Arrays.asList(data.getParentName(), data.getProjectName()))
                    .flatMap(array -> array.stream()).distinct()
                    .map(data -> {
                        Map<String, String> map = new HashMap<>();
                        map.put("name", data);
                        return map;
                    }).collect(Collectors.toList());

            Map<String, List<Map<String, String>>> finalData = new HashMap<>();
            finalData.put("data", itemList);
            finalData.put("links", linkList);
            return finalData;
        }
        return null;
    }

    /**
     * 分项/分区 汇总统计总和统计(分项/分区加起来)
     * 如果不传id就是一级分项/分区汇总起来
     * 如果传入id就是传入的id汇总起来
     *
     * @param param 条件
     *
     * @return 汇总
     */
    @Override
    public EnergyProjectAreaStatVO getDailyUsageProjectStatAllOne(EnergyTrendUsageParam param) {
        Assert.isNotFound(param.getProjectCategory(), "类型不能为空！");
        List<Long> projectIdList = new ArrayList<>();
        // 如果没有指定分项/区的id默认为一级分项/分区
        if (StringUtils.isBlank(param.getProjectIds())) {
            if (BindTypeEnum.ITEM.name().equals(param.getProjectCategory())) {
                projectIdList = this.getProjectTopId();
            } else if (BindTypeEnum.ROUTE.name().equals(param.getInstrumentType())) {
                projectIdList = new ArrayList<>();
            } else {
                projectIdList = this.getAreaTopId();
            }
            param.setProjectIdList(projectIdList);
        } else {
            param.setProjectIdList(Arrays.stream(param.getProjectIds().split(",")).map(Long::parseLong).collect(Collectors.toList()));
        }
        if (projectIdList.isEmpty()) {
            return new EnergyProjectAreaStatVO();
        }
        Period period = param.getPeriod();
        return energyDailyUsageProjectMapper.getDailyUsageProjectStatAllOne(Wrappers.<EnergyDailyUsageProject>lambdaUpdate()
                                                                                    .eq(EnergyDailyUsageProject::getProjectCategory, param.getProjectCategory())
                                                                                    .eq(StringUtils.isNotEmpty(param.getInstrumentType()), EnergyDailyUsageProject::getInstrumentType, param.getInstrumentType())
                                                                                    .between(period !=
                                                                                             null, EnergyDailyUsageProject::getStatDate, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                                    .in(EnergyDailyUsageProject::getProjectId, projectIdList)
                                                                           );
    }

    private List<Long> getAreaTopId() {
        List<Long> list = new ArrayList<>();
        List<AreaProject> areaProjectList = areaProjectMapper.selectList(Wrappers.<AreaProject>lambdaQuery()
                                                                                 .isNull(AreaProject::getParentId)
                                                                        );
        if (areaProjectList != null && !areaProjectList.isEmpty()) {
            return areaProjectList.stream().map(AreaProject::getId).collect(Collectors.toList());
        }
        return list;
    }

    private List<Long> getProjectTopId() {
        List<Long> list = new ArrayList<>();
        List<EnergyProject> areaProjectList = energyProjectMapper.selectList(Wrappers.<EnergyProject>lambdaQuery()
                                                                                     .isNull(EnergyProject::getParentId)
                                                                            );
        if (areaProjectList != null && !areaProjectList.isEmpty()) {
            return areaProjectList.stream().map(EnergyProject::getId).collect(Collectors.toList());
        }
        return list;
    }
}