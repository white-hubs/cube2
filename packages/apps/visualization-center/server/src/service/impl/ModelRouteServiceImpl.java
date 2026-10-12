package com.guhecloud.cube.base.visualization.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.visualization.server.dao.ModelRouteMapper;
import com.guhecloud.cube.base.visualization.server.entity.ModelRoute;
import com.guhecloud.cube.base.visualization.server.param.ModelRouteParam;
import com.guhecloud.cube.base.visualization.server.service.ModelRouteService;
import com.guhecloud.cube.base.visualization.server.vo.ModelRouteVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.param.AreaQueryParam;
import com.guhecloud.cube.biz.mdata.client.vo.AreaListVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * @Author: lijie
 * @CreateTime: 2023-08-29  14:45
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ModelRouteServiceImpl extends ServiceImpl<ModelRouteMapper, ModelRoute> implements ModelRouteService {
    private final AreaClient areaClient;

    @Override
    public List<ModelRouteVO> queryAll(Long modelId, String name) {
        Integer maxLevel = baseMapper.maxLevel(modelId);
        Map<Integer, List<ModelRoute>> map = new HashMap<>();
        if (ObjectUtils.isEmpty(maxLevel) || maxLevel < 1) {
            return new ArrayList<>();
        }
        for (Integer i = 1; i <= maxLevel; i++) {
            List<ModelRoute> modelRoute = baseMapper.selectList(new LambdaQueryWrapper<ModelRoute>()
                                                                        .eq(ModelRoute::getLevel, i)
                                                                        .eq(ModelRoute::getModelId, modelId)
                                                                        .orderByAsc(ModelRoute::getSerial));
            map.put(i, modelRoute);
        }
        List<ModelRouteVO> modelRouteList = ConvertUtils.toList(ModelRouteVO.class, map.get(1));
        List<ModelRouteVO> modelRouteData = new ArrayList<>();
        modelRouteList.stream().forEach(ex -> {
            this.getModelRoute(map, ex, 2, maxLevel, name);
            if (ObjectUtils.isEmpty(name) || ex.getName().contains(name) || ObjectUtils.isNotEmpty(ex.getChildren())) {
                modelRouteData.add(ex);
            }
        });
        return ObjectUtils.isNotEmpty(name) ? modelRouteData : modelRouteList;
    }

    @Override
    public Boolean insertAreaAll(Long modelId) {
        AreaQueryParam param = new AreaQueryParam();
        param.setPageNumber(1);
        param.setPageSize(9999);
        List<AreaListVO> areaList = areaClient.list(param);
        areaList.stream().forEach(ex -> {
            ModelRoute modelRoute = new ModelRoute();
            modelRoute.setParentId(ex.getParentId());
            modelRoute.setModelId(modelId);
            modelRoute.setCode(ex.getCode());
            modelRoute.setName(ex.getName());
            modelRoute.setLevel(ex.getLevel());
            modelRoute.setDirectory(ObjectUtils.isNotEmpty(ex.getAliasName()) ? ex.getAliasName() : ex.getName());
            modelRoute.setSerial(ObjectUtils.isNotEmpty(ex.getSort()) ? ex.getSort() : 1);
            modelRoute.setTag(ex.getId().toString());
            this.verificationName(modelRoute);
            baseMapper.insert(modelRoute);
            this.insertChildren(modelId, modelRoute.getId(), ex.getChildren(), modelRoute.getTag());
        });
        return true;
    }

    public void insertChildren(Long modelId, Long parentId, List<AreaListVO> children, String tag) {
        children.stream().forEach(ex -> {
            ModelRoute modelRoute = new ModelRoute();
            modelRoute.setParentId(ex.getParentId());
            modelRoute.setModelId(modelId);
            modelRoute.setParentId(parentId);
            modelRoute.setCode(ex.getCode());
            modelRoute.setName(ex.getName());
            modelRoute.setLevel(ex.getLevel());
            modelRoute.setDirectory(ex.getAliasName());
            modelRoute.setSerial(ObjectUtils.isNotEmpty(ex.getSort()) ? ex.getSort() : 1);
            modelRoute.setTag(tag);
            this.verificationName(modelRoute);
            baseMapper.insert(modelRoute);
            if (ObjectUtils.isNotEmpty(ex.getChildren())) {
                this.insertChildren(modelId, modelRoute.getId(), ex.getChildren(), tag);
            }
        });
    }

    public void getModelRoute(Map<Integer, List<ModelRoute>> map, ModelRouteVO modelRouteVO, Integer level, Integer maxLevel, String name) {
        if (level <= maxLevel) {
            List<ModelRouteVO> modelRouteList = ConvertUtils.toList(ModelRouteVO.class, map.get(level));
            List<ModelRouteVO> allSubordinates = new ArrayList<>();
            for (ModelRouteVO ex : modelRouteList) {
                if (ex.getParentId().equals(modelRouteVO.getId())) {
                    this.getModelRoute(map, ex, level + 1, maxLevel, name);
                    if (ObjectUtils.isEmpty(name) || ex.getName().contains(name) || ObjectUtils.isNotEmpty(ex.getChildren())) {
                        allSubordinates.add(ex);
                    }
                }
            }
            modelRouteVO.setChildren(allSubordinates);
        }
    }

    @Override
    public Boolean insertList(Long modelId, List<ModelRouteParam> param) {
        param.stream().forEach(ex -> {
            ex.setModelId(modelId);
            this.insert(ex);
        });
        return true;
    }

    @Override
    public Boolean insert(ModelRouteParam param) {
        ModelRoute modelRoute = ConvertUtils.convert(ModelRoute.class, param);
        modelRoute.setCode(this.generateCode());
        if (ObjectUtils.isNotEmpty(param.getParentId())) {
            ModelRoute parentRoute = baseMapper.selectById(param.getParentId());
            modelRoute.setLevel(parentRoute.getLevel() + 1);
        } else {
            modelRoute.setLevel(1);
            modelRoute.setParentId(0L);
        }
        this.verificationName(modelRoute);
        return baseMapper.insert(modelRoute) > 0;
    }

    @Override
    public Boolean update(ModelRouteParam param) {
        this.verificationName(ConvertUtils.convert(ModelRoute.class, param));
        ModelRoute modelRoute = baseMapper.selectById(param.getId());
        modelRoute.setName(param.getName());
        modelRoute.setSerial(param.getSerial());
        modelRoute.setDirectory(param.getDirectory());
        return baseMapper.updateById(modelRoute) > 0;
    }

    @Override
    public Boolean deleteById(Long id) {
        return DatabaseContextHolder.softDelete(this, id);
    }

    /**
     * 获取区域编号信息
     *
     * @return 区域编号信息
     */
    private String generateCode() {
        int i = 1;
        while (i < 10) {
            String code = "QY-" + String.format("%06d", new Random().nextInt(100000));
            ModelRoute areaByCode = baseMapper.selectOne(new LambdaQueryWrapper<ModelRoute>()
                                                                 .eq(ModelRoute::getCode, code));
            if (Objects.isNull(areaByCode)) {
                return code;
            }
            i++;
        }
        return "";
    }

    private void verificationName(ModelRoute modelRoute) {
        ModelRoute route = baseMapper.selectOne(new LambdaQueryWrapper<ModelRoute>()
                                                        .eq(ModelRoute::getName, modelRoute.getName())
                                                        .eq(ModelRoute::getModelId, modelRoute.getModelId())
                                                        .eq(ModelRoute::getParentId,
                                                            ObjectUtils.isNotEmpty(modelRoute.getParentId()) ? modelRoute.getParentId()
                                                                                                             : 0));
        if (ObjectUtils.isNotEmpty(route)) {
            Assert.isConflict(
                    !ObjectUtils.isNotEmpty(modelRoute.getId()) || !modelRoute.getId().equals(route.getId()),
                    modelRoute.getName() + "模型导航名称已经存在");
        }
    }
}
