package com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.SupplementCardRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.SupplementCardRuleVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.SupplementCardRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.query.SupplementCardRuleQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/10
 * @description 补卡规则查询
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplementCardRuleQueryService extends ServiceImpl<SupplementCardRuleMapper, SupplementCardRulePO> {

    public List<SupplementCardRuleVO> getList(SupplementCardRuleQueryParam param) {
        List<SupplementCardRulePO> pos = this.getQueryChainWrapper(param).list();
        List<SupplementCardRuleVO> datas = new ArrayList<>();
        pos.forEach(ex -> datas.add(this.extend(ex)));
        return datas;
    }

    public Pagination<SupplementCardRuleVO> getPage(SupplementCardRuleQueryParam param) {
        Page<SupplementCardRulePO> page = this.getQueryChainWrapper(param)
                .page(PagingUtils.convert(param));
        List<SupplementCardRuleVO> datas = new ArrayList<>();
        page.getRecords().parallelStream().forEach(ex -> datas.add(this.extend(ex)));
        return new Pagination<>(page.getTotal(), page.getSize(), datas);
    }

    private LambdaQueryChainWrapper<SupplementCardRulePO> getQueryChainWrapper(SupplementCardRuleQueryParam param) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), SupplementCardRulePO::getName, param.getKeyword())
                .orderByDesc(BasicEntity::getUpdateTime)
                ;
    }

    public SupplementCardRuleVO extend(SupplementCardRulePO supplementCardRulePO) {
        SupplementCardRuleVO supplementCardRuleVO = ObjectUtils.copying(supplementCardRulePO, SupplementCardRuleVO.class);
        List<String> types = Arrays.asList(supplementCardRulePO.getType().split(","));
        supplementCardRuleVO.setTypes(types);
        // 规则内容
        List<String> content = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(supplementCardRuleVO.getTypes())) {
            String work = "补卡类型：" + supplementCardRuleVO.getTypeName();
            content.add(work);
        }
        if (null != supplementCardRuleVO.getTimes()) {
            String work = "补卡次数：每月允许补卡" + supplementCardRuleVO.getTimes() + "次";
            content.add(work);
        }
        if (null != supplementCardRuleVO.getDays()) {
            String work = "补卡时限：" + supplementCardRuleVO.getDays() + "日内允许补卡";
            content.add(work);
        }
        supplementCardRuleVO.setContent(content);
        return supplementCardRuleVO;
    }

}
