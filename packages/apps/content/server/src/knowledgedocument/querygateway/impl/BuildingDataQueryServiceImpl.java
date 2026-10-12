package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.dao.BuildingDataMapper;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.BuildingDataVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.BuildingData;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.BuildingDataQueryService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query.BuildingDataCollectionQuery;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @date 2022/5/19
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BuildingDataQueryServiceImpl extends ServiceImpl<BuildingDataMapper, BuildingData> implements BuildingDataQueryService {
    private final BuildingDataMapper buildingDataMapper;

    private final FileClient fileClient;
    private final AreaClient areaClient;
    private final OrganizationClient organizationClient;
    private final UserClient userClient;

    @Override
    public Page<BuildingDataVO> pageBuildingData(@NonNull BuildingDataCollectionQuery query) {
        //扩展区域查询
        if (Objects.nonNull(query.getAreaId())) {
            Set<Long> areaIds = this.areaClient.getChildrenIds(query.getAreaId());
            if (CollectionUtils.isEmpty(areaIds)) {
                areaIds = new HashSet<>();
            }
            areaIds.add(query.getAreaId());
            query.setAreaIds(areaIds);
        }
        Page<BuildingDataVO> buildingPage = buildingDataMapper.getBuildingPage(PagingUtils.convert(query), query);

        // 构建信息和获取文件信息
        List<BuildingDataVO> records = buildingPage.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            records.forEach(record -> {

                List<FileVO> fileVOS = fileClient.getByBusinessKey(record.getId());
                if (CollectionUtils.isNotEmpty(fileVOS)) {
                    FileVO fileVO = fileVOS.getFirst();
                    record.setFormat(fileVO.getSuffix());
                    record.setSize(BigDecimal.valueOf((double) fileVO.getSize() / 1048576));
                    record.setFileVO(fileVO);
                }
                if (Objects.nonNull(record.getAreaId())) {
                    AreaDTO areaById = areaClient.getAreaById(record.getAreaId());
                    if (Objects.nonNull(areaById)) {
                        List<Long> areaIds = Arrays.stream(areaById.getSequence().split("-")).map(Long::valueOf)
                                .collect(Collectors.toList());
                        Map<Long, String> areaIdNameMapping = areaClient.getByIds(areaIds).stream()
                                .collect(Collectors.toMap(AreaDTO::getId, AreaDTO::getName));
                        List<String> areaNames = areaIds.stream().map(areaId -> {
                            if (areaIdNameMapping.containsKey(areaId)) {
                                return areaIdNameMapping.get(areaId);
                            }
                            return "";
                        }).toList();
                        record.setAreasName(String.join("-", areaNames));
                    }
                }
                if (Objects.nonNull(record.getDeptId())) {
                    OrganizationDTO organizationsById = organizationClient.getOrganizationsById(record.getDeptId());
                    record.setDeptsName(organizationsById == null ? "" : organizationsById.getName());
                }
                UserDTO userByMobile = userClient.getUserByUserName(record.getCreateBy());
                if (Objects.nonNull(userByMobile)) {
                    record.setCreateBy(userByMobile.getName());
                }
            });
        }

        return buildingPage;
    }
}
