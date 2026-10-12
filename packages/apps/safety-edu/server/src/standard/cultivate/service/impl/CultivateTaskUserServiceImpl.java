package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.dao.CultivateTaskUserMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTaskUser;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.TrainSignEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskUserParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskUserQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.CultivateTaskUserService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.util.ExcelUtils;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskUserVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：培训人员服务层
 *
 * @author Appet
 * @date 2023-04-18 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CultivateTaskUserServiceImpl extends ServiceImpl<CultivateTaskUserMapper, CultivateTaskUser> implements CultivateTaskUserService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(CultivateTaskUserParam param) {
        CultivateTaskUser cultivateTaskUser = ObjectUtils.copying(param, CultivateTaskUser.class);
        cultivateTaskUser.setUserSignStatus(TrainSignEnum.UNSIGNED.name());
        this.baseMapper.insert(cultivateTaskUser);
        return cultivateTaskUser.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, CultivateTaskUserParam param) {
        CultivateTaskUser cultivateTaskUser = this.baseMapper.selectById(id);
        Assert.isNotFound(cultivateTaskUser, "未查询到相关信息");
        ObjectUtils.copying(param, cultivateTaskUser);
        this.baseMapper.updateById(cultivateTaskUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public CultivateTaskUserVO getById(Long id) {
        CultivateTaskUser cultivateTaskUser = this.baseMapper.selectById(id);
        return ObjectUtils.copying(cultivateTaskUser, CultivateTaskUserVO.class);
    }

    @Override
    public Pagination<CultivateTaskUserVO> getPage(CultivateTaskUserQueryParam param) {
        //请自行添加查询条件
        Page<CultivateTaskUser> page = this.lambdaQuery()
                .eq(param.getCultivateTaskId() != null, CultivateTaskUser::getCultivateTaskId, param.getCultivateTaskId())
                .eq(StringUtils.isNotBlank(param.getUserSignStatus()), CultivateTaskUser::getUserSignStatus, param.getUserSignStatus())
                .eq(param.getBelated() != null, CultivateTaskUser::getBelated, param.getBelated())
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, CultivateTaskUserVO.class);
    }

    @Override
    public void exportCultivateUser(CultivateTaskUserQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        List<CultivateTaskUser> datas = this.lambdaQuery()
                .eq(param.getCultivateTaskId() != null, CultivateTaskUser::getCultivateTaskId, param.getCultivateTaskId())
                .eq(StringUtils.isNotBlank(param.getUserSignStatus()), CultivateTaskUser::getUserSignStatus, param.getUserSignStatus())
                .eq(param.getBelated() != null, CultivateTaskUser::getBelated, param.getBelated())
                .list();
        Assert.isNotFound(CollectionUtils.isEmpty(datas), "未查询到数据，无法导出");
        //转换枚举
        datas.forEach(d -> {
            String desc = TrainSignEnum.valueOf(d.getUserSignStatus()).getDesc();
            if (d.getBelated() != null && d.getBelated()) {
                desc = desc + "(迟到)";
            }
            d.setUserSignStatus(desc);
        });

        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("userName", "姓名");
        writer.addHeaderAlias("userPhone", "电话");
        writer.addHeaderAlias("userDepartmentName", "部门");
        writer.addHeaderAlias("userPostName", "岗位");
        writer.addHeaderAlias("userSignStatus", "状态");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "培训人员";
        ExcelUtils.downloadExcel(request, response, fileName, writer);

    }

    @Override
    public List<CultivateTaskUserVO> getCultivateUsersByCultivateId(Long cultivateId) {
        List<CultivateTaskUser> cultivateTaskUsers = this.baseMapper.selectList(Wrappers.<CultivateTaskUser>lambdaQuery()
                                                                                        .eq(CultivateTaskUser::getCultivateTaskId, cultivateId)
                                                                                        .orderByDesc(CultivateTaskUser::getUserSignStatus));
        return ObjectUtils.copying(cultivateTaskUsers, CultivateTaskUserVO.class);
    }
}