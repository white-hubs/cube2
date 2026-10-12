package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectScenePointService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectSceneTypeService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectSceneTypeMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectScenePointPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectSceneTypePO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.SceneTypeVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author wql
 * @describe 现场点类型
 * @date 2023/6/5 15:55
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectSceneTypeServiceImpl extends ServiceImpl<InspectSceneTypeMapper, InspectSceneTypePO> implements InspectSceneTypeService {
    private final InspectScenePointService inspectScenePointService;

    /**
     * @Author wql
     * @Description 现场点类型列表
     * @Date 16:07 2023/6/5
     **/
    @Override
    public void addType(String typeName) {
        List<InspectSceneTypePO> list = this.lambdaQuery().eq(InspectSceneTypePO::getName, typeName).list();
        Assert.isConflict(ObjectUtils.isNotEmpty(list), "该现场点类型名称已存在");
        InspectSceneTypePO build = InspectSceneTypePO.builder().name(typeName).build();
        this.save(build);
    }

    /**
     * @Author wql
     * @Description 现场点类型列表
     * @Date 16:30 2023/6/5
     **/
    @Override
    public List<SceneTypeVO> sceneTypeList() {
        List<InspectSceneTypePO> list = this.lambdaQuery().orderByDesc(InspectSceneTypePO::getCreateTime).list();
        List<SceneTypeVO> vos = new ArrayList<>();
        Optional.ofNullable(list).orElse(Collections.emptyList()).forEach(type -> {
            vos.add(SceneTypeVO.builder()
                            .id(type.getId())
                            .name(type.getName())
                            .build());
        });
        return vos;
    }

    /**
     * @Author wql
     * @Description 更新现场点类型信息
     * @Date 17:00 2023/6/5
     **/
    @Override
    public void updateType(String typeId, String typeName) {
        this.lambdaUpdate().eq(InspectSceneTypePO::getId, typeId)
                .set(StringUtils.isNotBlank(typeName), InspectSceneTypePO::getName, typeName)
                .update();
        //更新现场点类型
        inspectScenePointService.lambdaUpdate().set(InspectScenePointPO::getTypeName, typeName).eq(InspectScenePointPO::getTypeId, typeId)
                .update();
    }

    /**
     * @Author wql
     * @Description 删除现场点类型信息
     * @Date 17:03 2023/6/5
     **/
    @Override
    public void deleteType(String typeId) {
        this.lambdaUpdate()
                .set(InspectSceneTypePO::getDeleted, true)
                .eq(InspectSceneTypePO::getId, typeId)
                .update();
    }
}
