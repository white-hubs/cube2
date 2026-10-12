package com.guhecloud.cube.deliver.project.merchant.service.impl.store;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.deliver.project.merchant.entity.UserDeliver;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationRatio;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationRecord;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationRecordTag;
import com.guhecloud.cube.deliver.project.merchant.mapper.store.StallEvaluationRecordMapper;
import com.guhecloud.cube.deliver.project.merchant.service.UserDeliverService;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallEvaluationRatioService;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallEvaluationRecordService;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallEvaluationRecordTagService;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.BizCodeUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallEvaluationRecordDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluation;
import com.guhecloud.cube.deliver.project.merchant.mapper.store.StallEvaluationMapper;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 档位评价记录表 服务实现类
 * </p>
 *
 * @author benhuabao
 * @since 2021-12-22
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallEvaluationRecordServiceImpl extends ServiceImpl<StallEvaluationRecordMapper, StallEvaluationRecord> implements StallEvaluationRecordService {

    private final StallEvaluationRecordMapper mapper;
    private final StallEvaluationMapper seMapper;
    private final StallEvaluationRatioService ratioService;
    private final StallEvaluationRecordTagService recordTagService;
    private final FileClient fileClient;
    private final UserDeliverService userDeliverService;
    //    private final PurchaserApi purchaserApi;

    /**
     * 最高评分
     */
    public static final int STORE_MAX_MARK = 5;

    public static final String STORE_MAX_TIP = "最多只能选择五颗星！";

    /**
     * 最低评分
     */
    public static final int STORE_MIN_MARK = 1;

    public static final String STORE_MIN_TIP = "请至少选择一颗星！";
    /**
     * 具体某人，在一天中，对于某一个档位的评价数，
     * 不得超过 STORE_MAX_EVALUATIONS
     */
    public static final int STORE_MAX_EVALUATIONS = 3;

    @Override
    public Long save(StallEvaluationRecordDTO dto, Long userId) {
        //查找用户，补全用户信息
        UserDeliver userInfoDTO = userDeliverService.getById(userId);
        assert userInfoDTO != null;
        dto.setMobile(userInfoDTO.getMobile());
        dto.setNickname(userInfoDTO.getNickname());
        dto.setUsername(userInfoDTO.getUsername());
        dto.setUserId(userId);
        LocalDateTime nowDate = DateUtils.now();
        dto.setCreateTime(nowDate);
        // 计算诚信经营分值
        this.countHonestyScore(dto);
        //添加评论记录
        Long seRecordId = this.saveRecord(dto);
        //插入档位评价总表
        // 保存评价标签
        this.saveRecordTags(seRecordId, dto.getRecordTags(), nowDate);
        return seRecordId;
    }

    @Override
    public void examine(StallEvaluationRecordDTO dto) {
        StallEvaluationRecord record = mapper.selectById(dto.getId());
        if (record == null) {
            return;
        }
        // 审核通过则更新总表的档位评分信息
        if (EXAMINE_STATUS_PASS == dto.getExamineStatus()) {
            //插入档位评价总表
            StallEvaluation se = seMapper.selectOne(Wrappers.<StallEvaluation>lambdaQuery()
                                                            .eq(StallEvaluation::getProjectId, record.getProjectId())
                                                            .eq(StallEvaluation::getStallId, record.getStallId()));
            this.saveStoreEvaluation(se, new StallEvaluationRecordDTO(record));
        }
        record.setExamineStatus(dto.getExamineStatus());
        record.setExamineRemark(dto.getExamineRemark());
        record.setExamineTime(DateUtils.now());
        // 更新标签关联表
        recordTagService.lambdaUpdate().eq(StallEvaluationRecordTag::getRecordId, dto.getId())
                .set(StallEvaluationRecordTag::getExamineStatus, dto.getExamineStatus()).update();
        mapper.updateById(record);
    }

    @Override
    public void remove(String id) {
        StallEvaluationRecord record = mapper.selectById(id);
        if (record == null) {
            return;
        }
        // 更改档位的评价记录数以及把档位的平均评价分重新计算
        this.updateEvaluation(record);
        mapper.deleteById(id);
        //        fileApi.removeBatch(BUSINESS_TYPE_SERECORDS, id);
    }

    // 保存评价标签中间表
    private void saveRecordTags(Long recordId, List<StallEvaluationRecordTag> recordTags, LocalDateTime date) {
        if (recordTags == null || recordTags.size() < 1) {
            return;
        }
        recordTags.forEach(tag -> {
            tag.setRecordId(recordId);
            tag.setTime(date);
            tag.setExamineStatus(EXAMINE_STATUS_NO);
            recordTagService.save(tag);
        });
    }

    // 修改记录总表
    private void updateEvaluation(StallEvaluationRecord record) {
        StallEvaluation evaluation = seMapper.selectOne(Wrappers.<StallEvaluation>lambdaQuery()
                                                                .eq(StallEvaluation::getProjectId, record.getProjectId())
                                                                .eq(StallEvaluation::getStallId, record.getStallId()));
        if (evaluation != null) {
            if (evaluation.getTotal() - 1 != 0) {
                // 说明不是最后一条评论
                record.setId(evaluation.getId());
                seMapper.updateEvaluationByDelRecord(new StallEvaluationRecordDTO(record));
            } else {
                // 删除的是最后一条评论，直接将评价总表的记录删除
                seMapper.deleteById(evaluation.getId());
            }
        }
    }

    private void saveStoreEvaluation(StallEvaluation se, StallEvaluationRecordDTO dto) {
        //如果该档位没有记录，则添加记录
        int totalScore = dto.getZScore() + dto.getSScore() + dto.getTScore() + dto.getWScore();
        if (se == null) {
            StallEvaluation stallEvaluation = new StallEvaluation();
            stallEvaluation.setProjectId(dto.getProjectId());
            stallEvaluation.setStallId(dto.getStallId());
            stallEvaluation.setTotal(1);
            stallEvaluation.setZScore(dto.getZScore());
            stallEvaluation.setSScore(dto.getSScore());
            stallEvaluation.setTScore(dto.getTScore());
            stallEvaluation.setWScore(dto.getWScore());
            stallEvaluation.setZScoreAvg(dto.getZScore().floatValue());
            stallEvaluation.setSScoreAvg(dto.getZScore().floatValue());
            stallEvaluation.setTScoreAvg(dto.getTScore().floatValue());
            stallEvaluation.setWScoreAvg(dto.getWScore().floatValue());
            stallEvaluation.setTotalScore(totalScore);
            stallEvaluation.setHonestyScore(dto.getHonestyScore());
            seMapper.insert(stallEvaluation);
        } else {
            //如果该档位之前已有评分，就修改对应评分
            //使用SQL进行修改,防止多人同时提交出现的并发问题
            dto.setId(se.getId());
            seMapper.updateStoreEvaluation(dto);
        }
    }

    /**
     * 添加评论记录
     * 1）评分不超过5不低于1
     * 2）一个人一天对一个档位的评价不可以超过三次（防止恶意刷评论）
     */
    public Long saveRecord(StallEvaluationRecordDTO dto) {

        StallEvaluationRecord seRecord = dto.convert();

        if (dto.getSScore() > STORE_MAX_MARK || dto.getTScore() > STORE_MAX_MARK
            || dto.getWScore() > STORE_MAX_MARK || dto.getZScore() > STORE_MAX_MARK) {
            throw new ServiceException(STORE_MAX_TIP);
        }
        if (dto.getSScore() < STORE_MIN_MARK || dto.getTScore() < STORE_MIN_MARK
            || dto.getWScore() < STORE_MIN_MARK || dto.getZScore() < STORE_MIN_MARK) {
            throw new ServiceException(STORE_MIN_TIP);
        }
        //TODO 假数据，限制在测试时临时去掉
        int recordsCount = mapper.selectList(Wrappers.<StallEvaluationRecord>lambdaQuery()
                                                     .eq(StallEvaluationRecord::getProjectId, dto.getProjectId())
                                                     .eq(StallEvaluationRecord::getStallId, dto.getStallId())
                                                     .eq(StallEvaluationRecord::getUserId, dto.getUserId())
                                                     .between(StallEvaluationRecord::getCreateTime, DateUtils.beginOfDay(DateUtils.now()), DateUtils.endOfDay(DateUtils.now())))
                .size();
        if (recordsCount >= STORE_MAX_EVALUATIONS) {
            throw new ServiceException("一天对于同一档位评价不得超过" + STORE_MAX_EVALUATIONS + "次");
        }
        // 是否有图
        seRecord.setHasFile(dto.getFileIds() != null && dto.getFileIds().size() > 0);
        // 计算平均分
        Float aver = (dto.getSScore() + dto.getTScore() + dto.getWScore() + dto.getZScore()) / 4f;
        seRecord.setScore(aver);
        //添加code
        seRecord.setCode(BizCodeUtils.generate("PJ"));
        // 审核状态置为0
        seRecord.setExamineStatus(EXAMINE_STATUS_NO);
        // insert该条评价
        mapper.insert(seRecord);
        // 获取到ID关联File
        dto.setId(seRecord.getId());
        this.markFile(dto);
        return seRecord.getId();
    }

    private void markFile(StallEvaluationRecordDTO dto) {
        //        List<Long> fileIds = dto.getFileIds();
        //        if (fileIds != null && fileIds.size() > 0) {
        //            fileIds.forEach(fileId -> fileClient.getById(fileId, BUSINESS_TYPE_SERECORDS, dto.getId()));
        //        }
    }

    /**
     * 计算诚信经营值
     * 诚信经营值=（权重系数*商品质量+权重系数*诚实守信+ 权重系数*服务态度+权重系数*环境卫生），四舍五入保留小数点后一位
     *
     * @param dto
     */
    private void countHonestyScore(StallEvaluationRecordDTO dto) {
        float zRatio = 0.25f;
        float sRatio = 0.25f;
        float tRatio = 0.25f;
        float wRatio = 0.25f;
        StallEvaluationRatio ratio = ratioService.lambdaQuery().eq(StallEvaluationRatio::getProjectId, dto.getProjectId()).one();
        if (ratio != null) {
            zRatio = ratio.getZRatio();
            sRatio = ratio.getSRatio();
            tRatio = ratio.getTRatio();
            wRatio = ratio.getWRatio();
        }
        float honesty = zRatio * dto.getZScore() + sRatio * dto.getSScore() + tRatio * dto.getTScore() + wRatio * dto.getWScore();
        dto.setHonestyScore(BigDecimal.valueOf(honesty).setScale(0, RoundingMode.HALF_UP).intValue());
    }

}
