package com.guhecloud.cube.srv.gen.visitor.management.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.BlacklistMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.BlacklistPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.service.BlacklistService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.service.InteVisitorPersonService;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.BlacklistParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.KeyWordPageParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.BlacklistResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 访问配置(时段、权限、黑名单等
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlacklistServiceImpl implements BlacklistService {
    private static final String VISITOR = "VISITOR";

    private final BlacklistMapper blacklistMapper;
    private final InteVisitorPersonService inteVisitorPersonService;
    private final FileRefClient fileRefClient;

    /**
     * 黑名单分页
     *
     * @param param 入参
     *
     * @return 黑名单列表
     */
    @Override
    public Pagination<BlacklistResult> blacklistPage(KeyWordPageParam param) {
        LambdaQueryWrapper<BlacklistPO> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(param.getKeyword())) {
            wrapper.like(BlacklistPO::getPhone, param.getKeyword())
                    .or().like(BlacklistPO::getName, param.getKeyword());
        }
        Page<BlacklistPO> page = blacklistMapper.selectPage(PagingUtils.convert(param), wrapper);
        return PagingUtils.convert(page, ConvertUtils.toList(BlacklistResult.class, page.getRecords()));
    }

    /**
     * 黑名单详情
     *
     * @param id 主键
     *
     * @return 详情
     */
    @Override
    public BlacklistResult blackListDetail(Long id) {
        BlacklistPO po = blacklistMapper.selectById(id);
        BlacklistResult result = new BlacklistResult();
        if (po != null) {
            BeanUtils.copyProperties(po, result);
        }
        return result;
    }

    /**
     * 添加黑名单
     *
     * @param param 入参
     */
    @Override
    public void addBlacklist(BlacklistParam param) {
        // 添加英特灵达监控人员
        Integer personCode = inteVisitorPersonService.addPerson(param);
        // 存储业务数据
        BlacklistPO po = new BlacklistPO();
        BeanUtils.copyProperties(param, po);
        po.setId(IdWorker.getId());
        po.setThirdUserCode(personCode);
        //文件持久化
        if (StringUtils.isNotBlank(param.getPictures())) {
            FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
            fileRefCreateParam.setFileIdStr(param.getPictures());
            fileRefCreateParam.setBusinessKey(po.getId());
            fileRefCreateParam.setSceneCode(VISITOR);
            fileRefClient.refInduce(fileRefCreateParam);

        }
        blacklistMapper.insert(po);
    }

    /**
     * 移除黑名单
     *
     * @param id 入参
     */
    @Override
    public void removeBlacklist(Long id) {
        BlacklistPO po = blacklistMapper.selectById(id);
        if (po == null) {
            return;
        }
        if (po.getThirdUserCode() != null) {
            inteVisitorPersonService.removePerson(po.getThirdUserCode());
        }
        fileRefClient.removeByBusinessKey(po.getId());
        blacklistMapper.deleteById(id);
    }
}
