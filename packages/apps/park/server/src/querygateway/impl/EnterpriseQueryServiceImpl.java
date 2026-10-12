package com.guhecloud.cube.srv.gen.park.management.server.querygateway.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.srv.gen.park.management.server.dao.EnterpriseMapper;
import com.guhecloud.cube.srv.gen.park.management.server.dao.TypeDataMapper;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseDirectoryVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseVO;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterprisePO;
import com.guhecloud.cube.srv.gen.park.management.server.po.TypeDataPO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.EnterpriseQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.TypeDataQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.EnterpriseQuery;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnterpriseQueryServiceImpl extends ServiceImpl<EnterpriseMapper, EnterprisePO> implements EnterpriseQueryService {

    private final TypeDataQueryService typeDataQueryService;
    private final FileClient fileClient;
    private final TypeDataMapper typeDataMapper;

    @Override
    public EnterpriseVO getVoById(Long id) {
        EnterprisePO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的企业");

        EnterpriseVO vo = ObjectUtils.copying(po, EnterpriseVO.class);
        // 查询 行业类型名称
        this.setIndustryTypeName(vo);
        // 查询 文件
        this.extendFileVOS(vo);

        return vo;
    }

    @Override
    public List<EnterpriseVO> getVOList(EnterpriseQuery query) {
        List<EnterprisePO> pos = this.getQueryChainWrapper(query).list();
        List<EnterpriseVO> vos = ObjectUtils.copying(pos, EnterpriseVO.class);
        return vos;
    }

    @Override
    public Pagination<EnterpriseVO> getVOPage(EnterpriseQuery query) {
        Page<EnterprisePO> page = this.getQueryChainWrapper(query).page(PagingUtils.convert(query));

        List<EnterprisePO> pos = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList());
        List<EnterpriseVO> vos = ObjectUtils.copying(pos, EnterpriseVO.class);
        vos.forEach(this::setIndustryTypeName);

        return PagingUtils.convert(page, vos);
    }

    @Override
    public Pagination<EnterpriseDirectoryVO> getVOPageByDirectory(EnterpriseQuery query) {
        Page<EnterprisePO> page = this.getQueryChainWrapper(query).gt(EnterprisePO::getRoomRentTime, 0).page(PagingUtils.convert(query));
        List<EnterprisePO> pos = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList());
        List<EnterpriseDirectoryVO> vos = ObjectUtils.copying(pos, EnterpriseDirectoryVO.class);
        Pagination<EnterpriseDirectoryVO> convert = PagingUtils.convert(page, vos);
        convert.getRecords().forEach(s -> {
            TypeDataPO typeDataPO = typeDataMapper.selectById(s.getIndustryTypeId());
            if (ObjectUtils.isNotEmpty(typeDataPO)) {
                s.setIndustryTypeName(typeDataPO.getName());
            }
        });
        return convert;
    }

    private void setIndustryTypeName(EnterpriseVO vo) {
        if (vo != null) {
            TypeDataPO typeDataPO = typeDataQueryService.getById(vo.getIndustryTypeId());
            vo.setIndustryTypeName(typeDataPO != null ? typeDataPO.getName() : "");
        }
    }

    private LambdaQueryChainWrapper<EnterprisePO> getQueryChainWrapper(EnterpriseQuery query) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getKeyword()), EnterprisePO::getName, query.getKeyword())
                .eq(StringUtils.isNotEmpty(query.getType()), EnterprisePO::getType, query.getType())
                .eq(null != query.getIndustryTypeId(), EnterprisePO::getIndustryTypeId, query.getIndustryTypeId())
                .eq(StringUtils.isNotEmpty(query.getScale()), EnterprisePO::getScale, query.getScale())
                .between(query.getPeriod() !=
                         null, BasicEntity::getCreateTime, ObjectUtils.ifNull(query.getPeriod(), Period::getBegin), ObjectUtils.ifNull(query.getPeriod(), Period::getEnd))
                .orderByDesc(BasicEntity::getCreateTime);
    }

    private void extendFileVOS(EnterpriseVO vo) {
        try {
            vo.setFileVOS(fileClient.getByBusinessKey(vo.getId()));
        } catch (Exception e) {
            log.error("查询文件异常", e);
        }

    }

}
