package com.guhecloud.cube.srv.gen.stall.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.stall.server.dao.GenStallMapper;
import com.guhecloud.cube.srv.gen.stall.server.entity.GenStall;
import com.guhecloud.cube.srv.gen.stall.server.param.GenStallCreateParam;
import com.guhecloud.cube.srv.gen.stall.server.param.GenStallQueryParam;
import com.guhecloud.cube.srv.gen.stall.server.param.GenStallUpdateParam;
import com.guhecloud.cube.srv.gen.stall.server.service.GenStallService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GenStallServiceImpl extends ServiceImpl<GenStallMapper, GenStall> implements GenStallService {

    @Override
    public void save(GenStallCreateParam param) {
        Assert.isConflict(Objects.nonNull(this.getByCode(param.getCode())), "存在档位编号");
        Assert.isConflict(Objects.nonNull(this.getByName(param.getName())), "存在档位名称");
        GenStall stall = ObjectUtils.copying(param, GenStall.class);
        stall.setDeptId(String.join(",", param.getDeptIds()));
        stall.setArea(String.join(",", param.getAreaIds()));
        stall.setLeased(Boolean.FALSE);//默认false未出租
        this.save(stall);
    }

    @Override
    public void excelSave(List<GenStallCreateParam> param) {
        List<GenStall> stalls = ObjectUtils.copying(param, GenStall.class);
        this.saveBatch(stalls);
    }

    @Override
    public void update(GenStallUpdateParam param) {
        GenStall stall = this.getById(param.getId());
        Assert.isNotFound(stall, "不存在档位!");
        ObjectUtils.copying(param, stall);
        if (Boolean.FALSE.equals(param.getEnabled())) {
            stall.setLeaseType(false);
        }
        stall.setArea(String.join(",", param.getAreaIds()));
        stall.setDeptId(String.join(",", param.getDeptIds()));
        this.updateById(stall);
    }

    @Override
    public Page<GenStall> page(GenStallQueryParam queryParam) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), s -> s
                        .like(StringUtils.isNotEmpty(queryParam.getKeyword()), GenStall::getCode, queryParam.getKeyword())
                        .or().like(StringUtils.isNotEmpty(queryParam.getKeyword()), GenStall::getName, queryParam.getKeyword()))
                .like(Objects.nonNull(queryParam.getAreaId()), GenStall::getArea, queryParam.getAreaId())
                .page(PagingUtils.convert(queryParam));
    }

    @Override
    public void genRemove(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public GenStall getByName(String name) {
        return this.lambdaQuery().eq(GenStall::getName, name).one();
    }

    @Override
    public GenStall getByCode(@NotNull String code) {
        return this.lambdaQuery().eq(GenStall::getCode, code).one();
    }

    @Override
    public List<GenStall> list(GenStallQueryParam queryParam) {
        return this.lambdaQuery().like(StringUtils.isNotEmpty(queryParam.getKeyword()), GenStall::getName, queryParam.getKeyword()).list();
    }

    @Override
    public List<GenStall> listByArea(String area) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(area), GenStall::getArea, area).list();
    }

}
