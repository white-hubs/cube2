package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.ProductClient;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserMapQuery;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.dao.KnowledgeBaseMapper;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.dao.KnowledgeTypeMapper;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.KeyValueStringInteger;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.KnowledgeBaseVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.KnowledgeTypeStatVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.KnowledgeBase;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.KnowledgeType;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.KnowledgeBaseQueryService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query.KnowledgeBaseQuery;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 知识文档查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KnowledgeBaseQueryServiceImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBase> implements KnowledgeBaseQueryService {
    private final FileClient fileClient;
    private final ProductClient productClient;
    private final OrganizationClient organizationClient;
    private final KnowledgeTypeMapper knowledgeTypeMapper;
    private final UserClient userClient;

    /**
     * 查询详情
     *
     * @param query id 主键
     *
     * @return 详情
     */
    @Override
    public KnowledgeBaseVO getDetail(KnowledgeBaseQuery query) {
        Assert.isParameterError(query.getId() == null, "id 不能为空!");
        KnowledgeBase po = this.getById(query.getId());
        List<KnowledgeBaseVO> vos = this.extToVO(Collections.singletonList(po));
        if (CollectionUtils.isNotEmpty(vos)) {
            return vos.getFirst();
        }
        return new KnowledgeBaseVO();
    }

    /**
     * 分页列表
     *
     * @param query 条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<KnowledgeBaseVO> getPage(KnowledgeBaseQuery query) {
        Page<KnowledgeBase> page = this.page(PagingUtils.convert(query), Wrappers.<KnowledgeBase>lambdaQuery()
                                                     .like(StringUtils.isNotBlank(query.getName()), KnowledgeBase::getName, query.getName())
                                                     .eq(query.getTypeId() != null, KnowledgeBase::getTypeId, query.getTypeId())
                                                     .eq(query.getDeptId() != null, KnowledgeBase::getDeptId, query.getDeptId())
                                                     .eq(query.getProductId() != null, KnowledgeBase::getProductId, query.getProductId())
                                                     .orderByDesc(KnowledgeBase::getCreateTime)
                                            );
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }

        List<KnowledgeBaseVO> list = this.extToVO(page.getRecords());
        return PagingUtils.convert(page, list);
    }

    /**
     * 统计文档各类型数量
     *
     * @return 统计结果
     */
    @Override
    public KnowledgeTypeStatVO statKnowledgeByType() {
        KnowledgeTypeStatVO vo = KnowledgeTypeStatVO.builder().build();
        List<KeyValueStringInteger> list = this.baseMapper.statKnowledgeGroupType();
        if (CollectionUtils.isNotEmpty(list)) {
            AtomicInteger total = new AtomicInteger(0);
            list.forEach(kv -> total.getAndAdd(kv.getValue()));
            vo.setTotal(total.get());
            vo.setStatList(list);
        }
        return vo;
    }

    /**
     * 扩展
     *
     * @param list po集合
     *
     * @return 扩展后的vo集合
     */
    private List<KnowledgeBaseVO> extToVO(List<KnowledgeBase> list) {
        List<KnowledgeBaseVO> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }

        Map<Long, KnowledgeType> typeMap = new HashMap<>();
        Map<Long, OrganizationDTO> organizationDTOMap = new HashMap<>();
        Map<Long, DeviceProductInfoDTO> productDTOMap = new HashMap<>();

        Set<Long> typeIds = new HashSet<>();
        Set<Long> deptIds = new HashSet<>();
        Set<Long> productIds = new HashSet<>();
        Set<Long> fileRefIds = new HashSet<>();
        // 账号集合
        Set<String> userNames = new HashSet<>();
        list.forEach(data -> {
            if (data.getDeptId() != null) {
                deptIds.add(data.getDeptId());
            }
            if (data.getProductId() != null) {
                productIds.add(data.getProductId());
            }
            if (data.getTypeId() != null) {
                typeIds.add(data.getTypeId());
            }
            if (StringUtils.isNotBlank(data.getCreateBy())) {
                userNames.add(data.getCreateBy());
            }
            fileRefIds.add(data.getId());
        });
        List<KnowledgeType> typeList = knowledgeTypeMapper.selectByIds(typeIds);
        Optional.ofNullable(typeList).orElse(new ArrayList<>()).forEach(type -> typeMap.put(type.getId(), type));
        List<OrganizationDTO> organizationDTOList =
                CollectionUtils.isEmpty(deptIds) ? new ArrayList<>() : organizationClient.getOrganizationsByIds(new ArrayList<>(deptIds));
        Optional.ofNullable(organizationDTOList).orElse(new ArrayList<>()).forEach(data -> organizationDTOMap.put(data.getId(), data));
        List<DeviceProductInfoDTO> productInfoDTOList =
                CollectionUtils.isEmpty(productIds) ? new ArrayList<>() : productClient.getProductList(productIds);
        Optional.ofNullable(productInfoDTOList).orElse(new ArrayList<>()).forEach(data -> productDTOMap.put(data.getId(), data));
        UserMapQuery userMapQuery = new UserMapQuery();
        userMapQuery.setUserNames(userNames);
        Map<String, UserDTO> userDTOMap = userClient.getTenantUserMap(userMapQuery);
        BusinessKeysParam fileParam = new BusinessKeysParam();
        fileParam.setBusinessKeys(new ArrayList<>(fileRefIds));
        Map<Long, List<FileVO>> fileVOMap = fileClient.getByBusinessKeys(fileParam);

        list.forEach(data -> {
            KnowledgeBaseVO vo = ObjectUtils.copying(data, KnowledgeBaseVO.class);

            KnowledgeType type = typeMap.get(data.getTypeId());
            if (type != null) {
                vo.setTypeName(type.getName());
            }

            OrganizationDTO organizationDTO = organizationDTOMap.get(data.getDeptId());
            if (organizationDTO != null) {
                vo.setDeptName(organizationDTO.getName());
            }

            DeviceProductInfoDTO productInfoDTO = productDTOMap.get(data.getProductId());
            if (productInfoDTO != null) {
                vo.setProductName(productInfoDTO.getName());
            }
            UserDTO userDTO = userDTOMap.get(vo.getCreateBy());
            if (userDTO != null) {
                vo.setCreateBy(userDTO.getName());
            }

            List<FileVO> fileList = fileVOMap.get(data.getId());
            vo.setFileList(fileList);
            if (CollectionUtils.isNotEmpty(fileList)) {
                FileVO file = fileList.getFirst();
                vo.setFormat(file.getType());
                vo.setSize(file.getSize() == null ? BigDecimal.ZERO : new BigDecimal(file.getSize()));
            }
            result.add(vo);
        });
        return result;
    }

}
