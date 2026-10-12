package com.guhecloud.cube.srv.gen.product.market.server.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.product.market.server.common.dao.AdministrativeDivisionMapper;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.AdministrativeDivision;
import com.guhecloud.cube.srv.gen.product.market.server.common.service.AdministrativeDivisionService;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.AdministrativeDivisionTreeVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：行政区划服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdministrativeDivisionServiceImpl extends ServiceImpl<AdministrativeDivisionMapper, AdministrativeDivision> implements AdministrativeDivisionService {

    @Override
    public List<AdministrativeDivisionTreeVO> getAdministrativeDivisionTree() {
        List<AdministrativeDivision> list = this.list();
        List<AdministrativeDivisionTreeVO> convert = ObjectUtils.copying(list, AdministrativeDivisionTreeVO.class);
        List<AdministrativeDivisionTreeVO> responses = this.formatToTreeNodeArray(convert, "0");
        return responses;
    }

    private List<AdministrativeDivisionTreeVO> formatToTreeNodeArray(List<AdministrativeDivisionTreeVO> treeNodes, String root) {
        List<AdministrativeDivisionTreeVO> trees = new ArrayList<AdministrativeDivisionTreeVO>();
        for (AdministrativeDivisionTreeVO treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(treeNode);
            }
            for (AdministrativeDivisionTreeVO it : treeNodes) {
                if (it.getParentId() != null) {
                    if (it.getParentId().equals(treeNode.getId())) {
                        if (treeNode.getChildren() == null) {
                            treeNode.setChildren(new ArrayList<AdministrativeDivisionTreeVO>());
                        }
                        treeNode.getChildren().add(it);
                    }
                }
            }
        }
        return trees;
    }
}