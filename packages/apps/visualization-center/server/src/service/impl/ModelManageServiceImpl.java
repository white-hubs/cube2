package com.guhecloud.cube.base.visualization.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.base.visualization.server.dao.ModelManageMapper;
import com.guhecloud.cube.base.visualization.server.entity.ModelManage;
import com.guhecloud.cube.base.visualization.server.param.ModelManagePageParam;
import com.guhecloud.cube.base.visualization.server.param.ModelManageParam;
import com.guhecloud.cube.base.visualization.server.service.ModelManageService;
import com.guhecloud.cube.base.visualization.server.service.ModelRouteService;
import com.guhecloud.cube.base.visualization.server.service.ProjectManageService;
import com.guhecloud.cube.base.visualization.server.vo.ModelManageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-08-29  10:41
 * @Description: 模型管理实现类
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ModelManageServiceImpl extends ServiceImpl<ModelManageMapper, ModelManage> implements ModelManageService {
    private final SysDictionaryClient sysDictionaryClient;
    private final FileClient fileClient;
    private final ProjectManageService projectManageService;
    private final ModelRouteService modelRouteService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return 实例对象
     */
    public ModelManageVO queryById(Long id) {
        ModelManage model = baseMapper.selectById(id);
        ModelManageVO modelManage = ConvertUtils.convert(ModelManageVO.class, model);
        try {
            SysDictionaryDTO dictionary = sysDictionaryClient.getSysDictionaryByCode(model.getModelType());
            modelManage.setModelTypeName(dictionary.getName());
            FileVO file = fileClient.getById(model.getFile());
            modelManage.setFile(file);
        } catch (Exception e) {
        }
        return modelManage;
    }

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @returnR
     */
    public Pagination<ModelManageVO> queryPage(ModelManagePageParam param) {
        Page<ModelManage> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = baseMapper.selectPage(page, new LambdaQueryWrapper<ModelManage>()
                .like(ObjectUtils.isNotEmpty(param.getId()), ModelManage::getId, param.getId())
                .eq(ObjectUtils.isNotEmpty(param.getModelType()), ModelManage::getModelType, param.getModelType())
                .like(ObjectUtils.isNotEmpty(param.getName()), ModelManage::getName, param.getName())
                .orderByDesc(ModelManage::getCreateTime));
        List<ModelManageVO> modelManageList = new ArrayList<>();
        page.getRecords().stream().forEach(ex -> {
            ModelManageVO modelManage = ConvertUtils.convert(ModelManageVO.class, ex);
            try {
                SysDictionaryDTO dictionary = sysDictionaryClient.getSysDictionaryByCode(ex.getModelType());
                modelManage.setModelTypeName(dictionary.getName());
                if (ObjectUtils.isNotEmpty(ex.getFile())) {
                    FileVO file = fileClient.getById(ex.getFile());
                    modelManage.setFile(file);
                }
            } catch (Exception e) {

            }
            modelManageList.add(modelManage);
        });
        return PagingUtils.convert(page, ConvertUtils.toList(ModelManageVO.class, modelManageList));
    }

    @Override
    public List<ModelManageVO> queryAll(String modelType) {
        List<ModelManage> modelManageList = baseMapper.selectList(new LambdaQueryWrapper<ModelManage>()
                                                                          .eq(ObjectUtils.isNotEmpty(modelType), ModelManage::getModelType, modelType)
                                                                          .orderByDesc(ModelManage::getCreateTime));
        List<ModelManageVO> modelList = new ArrayList<>();
        modelManageList.stream().forEach(ex -> {
            ModelManageVO modelManage = ConvertUtils.convert(ModelManageVO.class, ex);
            if (ObjectUtils.isNotEmpty(ex.getFile())) {
                FileVO file = fileClient.getById(ex.getFile());
                modelManage.setFile(file);
            }
            modelManage.setModelRoute(modelRouteService.queryAll(ex.getId(), null));
            modelList.add(modelManage);
        });
        return modelList;
    }

    /**
     * 新增数据
     *
     * @param param 实例对象
     *
     * @return 实例对象
     */
    public ModelManageVO insert(ModelManageParam param) {
        ModelManage modelManage = ConvertUtils.convert(ModelManage.class, param);
        this.verificationName(modelManage);
        baseMapper.insert(modelManage);
        modelRouteService.insertList(modelManage.getId(), param.getModelRoute());
        return ConvertUtils.convert(ModelManageVO.class, modelManage);
    }

    /**
     * 更新数据
     *
     * @param param 实例对象
     *
     * @return 实例对象
     */
    public Boolean update(ModelManageParam param) {
        ModelManage modelManage = ConvertUtils.convert(ModelManage.class, param);
        this.verificationName(modelManage);
        ModelManage model = baseMapper.selectById(param.getId());
        model.setModelType(param.getModelType());
        model.setName(param.getName());
        model.setFile(param.getFile());
        model.setAddress(param.getAddress());
        return baseMapper.updateById(model) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    public Boolean deleteById(Long id) {
        Boolean bool = projectManageService.queryByModelId(id);
        Assert.isNotAcceptable(bool, "如需删除请到项目中移除模型");
        return DatabaseContextHolder.softDelete(this, id);
    }

    private void verificationName(ModelManage modelManage) {
        ModelManage model = baseMapper.selectOne(new LambdaQueryWrapper<ModelManage>()
                                                         .eq(ModelManage::getName, modelManage.getName()));
        if (ObjectUtils.isNotEmpty(model)) {
            Assert.isConflict(!ObjectUtils.isEmpty(modelManage.getId()) ||
                              !modelManage.getId().equals(model.getId()), "模型名称已经存在");
        }
    }
}
