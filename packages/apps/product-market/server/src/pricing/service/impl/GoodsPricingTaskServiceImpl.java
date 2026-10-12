package com.guhecloud.cube.srv.gen.product.market.server.pricing.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.product.market.server.common.service.MarketGoodsCategoryService;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.MarketGoodsCategoryVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingRecordMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingTaskDetailMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingTaskMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingTaskTransferMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingRecord;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTask;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTaskDetail;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTaskTransfer;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.TaskStatusEnum;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.UrgencyEnum;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingTaskDetailService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingTaskService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.CommonStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingTaskVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.MyTaskStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.UserVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 描述：采价任务服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPricingTaskServiceImpl extends ServiceImpl<GoodsPricingTaskMapper, GoodsPricingTask> implements GoodsPricingTaskService {
    private final GoodsPricingTaskDetailService goodsPricingTaskDetailService;
    private final GoodsPricingTaskDetailMapper goodsPricingTaskDetailMapper;
    private final MarketGoodsCategoryService marketGoodsCategoryService;
    private final GoodsPricingTaskMapper goodsPricingTaskMapper;
    private final GoodsPricingRecordMapper goodsPricingRecordMapper;
    private final GoodsPricingTaskTransferMapper goodsPricingTaskTransferMapper;
    private final UserClient userClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(GoodsPricingTaskParam param) {
        GoodsPricingTask goodsPricingTask = ObjectUtils.copying(param, GoodsPricingTask.class);
        goodsPricingTask.setCreateUserName(SecurityContextHolder.getPrincipal().getName());
        goodsPricingTask.setStatus(TaskStatusEnum.WAIT.name());//待完成
        goodsPricingTask.setDeadline(param.getTaskValidityTime(LocalDateTime.now()));
        this.baseMapper.insert(goodsPricingTask);
        updateGoodsPricingTaskDetail(goodsPricingTask, param);
        return goodsPricingTask.getId();
    }

    private void updateGoodsPricingTaskDetail(GoodsPricingTask task, GoodsPricingTaskParam param) {
        Assert.isParameterError(CollectionUtils.isEmpty(param.getCategoryIds()) ||
                                CollectionUtils.isEmpty(param.getUserIds()), "采集信息缺失：品类和采集员不能为空");
        //删除详情信息
        goodsPricingTaskDetailService.remove(Wrappers.<GoodsPricingTaskDetail>lambdaQuery()
                                                     .eq(GoodsPricingTaskDetail::getTaskId, task.getId()));
        //保存
        String categoryIds = param.getCategoryIds().stream().map(Object::toString).collect(Collectors.joining(","));
        String userIds = param.getUserIds().stream().map(Object::toString).collect(Collectors.joining(","));

        goodsPricingTaskDetailService.addAndNotice(task, new GoodsPricingTaskDetail(task.getId(), categoryIds, userIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, GoodsPricingTaskParam param) {
        GoodsPricingTask goodsPricingTask = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsPricingTask, "未查询到相关信息");
        ObjectUtils.copying(param, goodsPricingTask);
        updateGoodsPricingTaskDetail(goodsPricingTask, param);
        this.baseMapper.updateById(goodsPricingTask);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public GoodsPricingTaskVO getById(Long id) {
        GoodsPricingTask goodsPricingTask = this.baseMapper.selectById(id);
        GoodsPricingTaskVO taskDetail = ObjectUtils.copying(goodsPricingTask, GoodsPricingTaskVO.class);
        //查询品类和人员
        GoodsPricingTaskDetail goodsPricingTaskDetails = goodsPricingTaskDetailMapper.selectOne(Wrappers
                                                                                                        .<GoodsPricingTaskDetail>lambdaQuery()
                                                                                                        .eq(GoodsPricingTaskDetail::getTaskId, id));
        if (goodsPricingTaskDetails != null) {
            List<MarketGoodsCategoryVO> allCategories = new ArrayList<>();
            List<UserVO> allUsers = new ArrayList<>();
            //查询品类信息
            List<MarketGoodsCategoryVO> goodsCategoryVOS = marketGoodsCategoryService.getListByIds(goodsPricingTaskDetails.getGoodsCategoryIds());
            allCategories.addAll(goodsCategoryVOS);

            List<UserVO> userList = Arrays.stream(goodsPricingTaskDetails.getUserIds().split(",")).map(u -> {
                UserDTO user = userClient.getUser(Long.valueOf(u));
                if (user != null) {
                    return UserVO.builder().userId(user.getId())
                            .userName(user.getName())
                            .deptName(user.getOrganization() != null ? user.getOrganization().getName() : "无部门")
                            .build();
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            allUsers.addAll(userList);

            taskDetail.setGoodsCategoryList(allCategories);
            taskDetail.setUserList(allUsers);
            //来源
            taskDetail.setTaskFrom(taskDetail.getPlanId() != null ? "计划创建" : "手动创建");
            //紧急程度
            taskDetail.setUrgencyName(UrgencyEnum.valueOf(taskDetail.getUrgency()).getLabel());
        }
        return taskDetail;
    }

    @Override
    public GoodsPricingTaskVO getByIdForApp(Long id) {
        GoodsPricingTaskVO taskVO = this.getById(id);
        //检查任务中 哪些是采过价的，哪些是没采过的
        List<GoodsPricingRecord> goodsPricingRecords = goodsPricingRecordMapper.selectList(Wrappers.<GoodsPricingRecord>lambdaQuery()
                                                                                                   .eq(GoodsPricingRecord::getTaskId, taskVO.getId()));
        if (CollectionUtils.isNotEmpty(goodsPricingRecords)) {
            taskVO.getGoodsCategoryList().stream().forEach(g -> {
                GoodsPricingRecord pricingRecord = goodsPricingRecords.stream()
                        .filter(p -> p.getGoodsCategoryId().toString().equals(g.getId().toString())).findFirst().orElse(null);
                g.setPriced(pricingRecord != null);
                if (pricingRecord != null) {
                    g.setPricingRecordId(pricingRecord.getId());
                    g.setUserName(pricingRecord.getUserName());
                    g.setCreateTime(pricingRecord.getCreateTime());
                }
            });
        }
        return taskVO;
    }

    @Override
    public Pagination<GoodsPricingTaskVO> getPage(GoodsPricingTaskQueryParam param) {
        //请自行添加查询条件
        Page<GoodsPricingTask> page = this.baseMapper.getGoodsPricingTaskPage(PagingUtils.convert(param), param);
        Pagination<GoodsPricingTaskVO> pagination = PagingUtils.convert(page, GoodsPricingTaskVO.class);
        if (CollectionUtils.isNotEmpty(pagination.getRecords())) {
            pagination.getRecords().stream().forEach(d -> {
                extendPricingTask(d);
            });
        }
        return pagination;
    }

    @Override
    public Pagination<GoodsPricingTaskVO> getPageForApp(GoodsPricingTaskQueryParam param) {
        //请自行添加查询条件
        if (param.getReceived() != null && param.getReceived()) {
            param.setReceiverId(SecurityContextHolder.getPrincipal().getId().toString());
        }
        Page<GoodsPricingTask> page = goodsPricingTaskMapper.getGoodsPricingTaskPage(PagingUtils.convert(param), param);

        Pagination<GoodsPricingTaskVO> pagination = PagingUtils.convert(page, GoodsPricingTaskVO.class);
        if (CollectionUtils.isNotEmpty(pagination.getRecords())) {
            pagination.getRecords().stream().forEach(d -> {
                extendPricingTask(d);
            });
        }
        return pagination;
    }

    private void extendPricingTask(GoodsPricingTaskVO data) {
        //查询品类
        GoodsPricingTaskDetail goodsPricingTaskDetails = goodsPricingTaskDetailMapper.selectOne(Wrappers
                                                                                                        .<GoodsPricingTaskDetail>lambdaQuery()
                                                                                                        .eq(GoodsPricingTaskDetail::getTaskId, data.getId()));
        if (goodsPricingTaskDetails != null) {
            List<MarketGoodsCategoryVO> allGoodsCategoryList;
            //查询品类信息
            if (StringUtils.isBlank(goodsPricingTaskDetails.getGoodsCategoryNames())) {
                allGoodsCategoryList = marketGoodsCategoryService.getListByIds(goodsPricingTaskDetails.getGoodsCategoryIds());
            } else {
                List<Long> categoryIds = Arrays.stream(goodsPricingTaskDetails.getGoodsCategoryIds().split(",")).map(Long::parseLong)
                        .collect(Collectors.toList());
                List<String> categoryNames = Arrays.stream(goodsPricingTaskDetails.getGoodsCategoryNames().split(","))
                        .collect(Collectors.toList());
                allGoodsCategoryList = IntStream.range(0, Math.min(categoryIds.size(), categoryNames.size()))
                        .mapToObj(i -> new MarketGoodsCategoryVO(categoryIds.get(i), categoryNames.get(i)))
                        .collect(Collectors.toList());
            }
            //商品品类
            data.setGoodsCategoryList(allGoodsCategoryList);
            //来源
            data.setTaskFrom(data.getPlanId() != null ? "计划创建" : "手动创建");
            //紧急程度
            data.setUrgencyName(UrgencyEnum.valueOf(data.getUrgency()).getLabel());
            //采价人员
            List<UserVO> pricingUsers;
            if (StringUtils.isBlank(goodsPricingTaskDetails.getUserNames())) {
                pricingUsers = Arrays.stream(goodsPricingTaskDetails.getUserIds().split(",")).map(u -> {
                    UserDTO user = userClient.getUser(Long.valueOf(u));
                    if (user != null) {
                        return UserVO.builder().userId(user.getId())
                                .userName(user.getName())
                                .build();
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
            } else {
                List<Long> userIds = Arrays.stream(goodsPricingTaskDetails.getUserIds().split(",")).map(Long::parseLong)
                        .collect(Collectors.toList());
                List<String> userNames = Arrays.stream(goodsPricingTaskDetails.getUserNames().split(",")).collect(Collectors.toList());
                pricingUsers = IntStream.range(0, Math.min(userIds.size(), userNames.size()))
                        .mapToObj(i -> new UserVO(userIds.get(i), userNames.get(i)))
                        .collect(Collectors.toList());
            }

            data.setUserList(pricingUsers);
            //接受人
            data.setUserId(goodsPricingTaskDetails.getUserId());
            data.setUserName(goodsPricingTaskDetails.getUserName());
        }
    }

    @Override
    public void overPricingTask(Long id, String remark) {
        GoodsPricingTask goodsPricingTask = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsPricingTask, "未找到要更新的任务");
        goodsPricingTask.setStatus(TaskStatusEnum.OVER.name());//已完成
        goodsPricingTask.setRemark(remark);
        this.baseMapper.updateById(goodsPricingTask);
    }

    @Override
    public Long overPricingTaskCheck(Long id) {
        GoodsPricingTaskDetail taskDetail = this.goodsPricingTaskDetailMapper.selectOne(Wrappers.<GoodsPricingTaskDetail>lambdaQuery()
                                                                                                .eq(GoodsPricingTaskDetail::getTaskId, id));
        Assert.isUnknownError(
                taskDetail == null || StringUtils.isBlank(taskDetail.getGoodsCategoryIds()), "采价任务数据异常，请找管理员确认");
        //查询已经采过价的记录
        Long pricedNum = this.goodsPricingRecordMapper.selectCount(Wrappers.<GoodsPricingRecord>lambdaQuery()
                                                                           .eq(GoodsPricingRecord::getTaskId, id));
        //计算没有采价的数量
        long totalNum = taskDetail.getGoodsCategoryIds().split(",").length;
        return totalNum - (pricedNum != null ? pricedNum : 0);
    }

    @Override
    public void receivePricingTask(Long id) {
        GoodsPricingTaskDetail taskDetail = this.goodsPricingTaskDetailMapper.selectOne(Wrappers.<GoodsPricingTaskDetail>lambdaQuery()
                                                                                                .eq(GoodsPricingTaskDetail::getTaskId, id));
        Assert.isNotFound(taskDetail, "未找到对应的的任务");
        taskDetail.setUserId(SecurityContextHolder.getPrincipal().getId());
        taskDetail.setUserName(SecurityContextHolder.getPrincipal().getName());
        this.goodsPricingTaskDetailMapper.updateById(taskDetail);
        //变更任务状态
        GoodsPricingTask goodsPricingTask = this.baseMapper.selectById(id);
        goodsPricingTask.setStatus(TaskStatusEnum.EXEC.name());
        this.baseMapper.updateById(goodsPricingTask);
    }

    @Override
    public List<CommonStatVO> getGoodsPricingTaskStat(GoodsPricingTaskQueryParam param) {
        List<CommonStatVO> statList = goodsPricingTaskMapper.getGoodsPricingTaskStat(param);
        List<CommonStatVO> collect = Arrays.stream(TaskStatusEnum.values()).map(em -> {
            String code = em.name();
            CommonStatVO commonStatVO = statList.stream().filter(c -> c.getCode().equals(code)).findFirst().orElse(null);
            if (commonStatVO == null) {
                commonStatVO = new CommonStatVO(code, em.getLabel(), "0");
            } else {
                commonStatVO.setName(em.getLabel());
            }
            return commonStatVO;
        }).collect(Collectors.toList());

        CommonStatVO st = new CommonStatVO();
        st.setCode("ALL");
        st.setName("全部");
        st.setValue(collect.stream().mapToInt(c -> Integer.valueOf(c.getValue())).sum() + "");
        collect.add(st);

        return collect;
    }

    @Override
    public MyTaskStatVO getMyPricingTaskStat(GoodsPricingTaskQueryParam param) {
        MyTaskStatVO myPricingTaskStat = goodsPricingTaskMapper.getMyPricingTaskStat(param);
        if (myPricingTaskStat == null) {
            myPricingTaskStat = new MyTaskStatVO();
        }
        return myPricingTaskStat;
    }

    /**
     * 目前支持转交一个人
     *
     * @param id
     * @param fromUserId
     * @param toUserId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferPricingTask(Long id, String fromUserId, String toUserId) {
        GoodsPricingTaskDetail taskDetail = goodsPricingTaskDetailMapper.selectOne(Wrappers.<GoodsPricingTaskDetail>lambdaQuery()
                                                                                           .eq(GoodsPricingTaskDetail::getTaskId, id));
        Assert.isNotAcceptable(taskDetail == null || taskDetail.getUserId() == null, "任务不存在或该任务尚未接收，不能移交");
        //构建实体
        GoodsPricingTaskTransfer taskTransfer = this.buildTransfer(taskDetail, toUserId);
        this.goodsPricingTaskTransferMapper.insert(taskTransfer);
        //更改任务人
        if (StringUtils.isNotBlank(taskDetail.getUserIds())
            && taskDetail.getUserIds().indexOf(taskTransfer.getToUserIds()) < 0) {
            taskDetail.setUserIds(taskDetail.getUserIds().replace(taskDetail.getUserId().toString(), taskTransfer.getToUserIds()));
            taskDetail.setUserNames(taskDetail.getUserNames().replace(taskDetail.getUserName(), taskTransfer.getToUserNames()));
        }
        taskDetail.setUserId(Long.valueOf(taskTransfer.getToUserIds()));//目前支持一个人
        taskDetail.setUserName(taskTransfer.getToUserNames());
        this.goodsPricingTaskDetailMapper.updateById(taskDetail);
    }

    private GoodsPricingTaskTransfer buildTransfer(GoodsPricingTaskDetail taskDetail, String toUserId) {
        GoodsPricingTaskTransfer transfer = new GoodsPricingTaskTransfer();
        transfer.setTaskId(taskDetail.getId());
        transfer.setFromUserId(taskDetail.getUserId());
        transfer.setFromUserName(taskDetail.getUserName());
        transfer.setToUserIds(toUserId);
        UserDTO user = userClient.getUser(Long.valueOf(toUserId));
        if (user != null) {
            transfer.setToUserNames(user.getName());
        }
        transfer.setStatus("SUCCESS");
        return transfer;
    }
}