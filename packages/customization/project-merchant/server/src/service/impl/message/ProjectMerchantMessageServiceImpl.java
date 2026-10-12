package com.guhecloud.cube.deliver.project.merchant.service.impl.message;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.deliver.project.merchant.dto.message.KeyValueVO;
import com.guhecloud.cube.deliver.project.merchant.dto.message.ProjectMerchantAssemblyNoticeParam;
import com.guhecloud.cube.deliver.project.merchant.dto.message.ProjectMerchantAssemblyNoticeSendParam;
import com.guhecloud.cube.deliver.project.merchant.entity.message.ProjectMerchantAssemblyNotice;
import com.guhecloud.cube.deliver.project.merchant.entity.message.ProjectMerchantAssemblyNoticeCondition;
import com.guhecloud.cube.deliver.project.merchant.mapper.message.ProjectMerchantAssemblyNoticeConditionMapper;
import com.guhecloud.cube.deliver.project.merchant.mapper.message.ProjectMerchantAssemblyNoticeMapper;
import com.guhecloud.cube.deliver.project.merchant.service.message.ProjectMerchantMessageService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectMerchantMessageServiceImpl extends ServiceImpl<ProjectMerchantAssemblyNoticeMapper, ProjectMerchantAssemblyNotice> implements ProjectMerchantMessageService {
    private final UserClient userClient;
    private final ProjectMerchantAssemblyNoticeConditionMapper noticeConditionMapper;

    /**
     * 根据业务类型和条件模块类型查询消息条件配置
     *
     * @param businessType 必须 业务类型 moduleType 小模块类型
     *
     * @return 条件集合
     */
    @Override
    public JSONArray queryCondition(String businessType, String moduleType) {
        ProjectMerchantAssemblyNotice electronAssemblyNotice = baseMapper.selectOne(new LambdaQueryWrapper<ProjectMerchantAssemblyNotice>()
                                                                                            .eq(ProjectMerchantAssemblyNotice::getBusinessType, businessType)
                                                                                            .last("limit 1"));
        if (electronAssemblyNotice == null) {
            return null;
        }
        List<ProjectMerchantAssemblyNoticeCondition> conditionList = noticeConditionMapper.selectList(Wrappers
                                                                                                              .<ProjectMerchantAssemblyNoticeCondition>lambdaQuery()
                                                                                                              .eq(ProjectMerchantAssemblyNoticeCondition::getNoticeId, electronAssemblyNotice.getId())
                                                                                                              .eq(moduleType !=
                                                                                                                  null, ProjectMerchantAssemblyNoticeCondition::getModuleType, moduleType)
                                                                                                     );
        if (CollectionUtils.isEmpty(conditionList)) {
            return null;
        }

        return new JSONArray(conditionList);
    }

    @Override
    public Boolean insert(List<ProjectMerchantAssemblyNoticeParam> param) {
        ProjectMerchantAssemblyNotice projectMerchantAssemblyNotice = new ProjectMerchantAssemblyNotice();
        projectMerchantAssemblyNotice.setDataJson(JSON.toJSONString(param));
        baseMapper.insert(projectMerchantAssemblyNotice);
        param.forEach(ex -> {
            // 写入通知条件
            for (KeyValueVO condition : ex.getCondition()) {
                ProjectMerchantAssemblyNoticeCondition noticeCondition = new ProjectMerchantAssemblyNoticeCondition();
                noticeCondition.setNoticeId(projectMerchantAssemblyNotice.getId());
                noticeCondition.setName(condition.getName());
                noticeCondition.setValue(condition.getValue());
                noticeCondition.setModuleType(ex.getModuleType());
                noticeCondition.setBusinessType(ex.getBusinessType());
                noticeConditionMapper.insert(noticeCondition);
            }
        });
        return true;
    }

    /**
     * @param param 通知人对象
     */
    @Override
    public Boolean sendMsgUser(ProjectMerchantAssemblyNoticeSendParam param) {
        log.info("查询消息配置信息，配置id：{}", param.getConditionId());
        ProjectMerchantAssemblyNoticeCondition condition = noticeConditionMapper.selectById(param.getConditionId());
        if (condition == null) {
            log.info("未查询到消息配置");
            return false;
        }
        SessionContextHolder.requestTenant(condition.getTenant());
        return true;
    }
}
