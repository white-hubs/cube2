package com.guhecloud.cube.deliver.project.merchant.service.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallEvaluationRecordDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationRecord;


/**
 * <p>
 * 档位评价记录表 服务类
 * </p>
 *
 * @author benhuabao
 * @since 2021-12-22
 */
public interface StallEvaluationRecordService extends IService<StallEvaluationRecord> {

    /**
     * 评价类型
     */
    String EVALUATION_TYPE_Z = "z_score"; // 商品质量
    String EVALUATION_TYPE_S = "s_score"; // 诚实守信
    String EVALUATION_TYPE_T = "t_score"; // 服务态度
    String EVALUATION_TYPE_W = "w_score"; // 环境卫生


    /**
     * 审核状态：0未审核，1通过，2未通过
     */
    int EXAMINE_STATUS_NO = 0;
    int EXAMINE_STATUS_PASS = 1;

    /**
     * FAQ图片,文件
     */
    String BUSINESS_TYPE_SERECORDS = "stall-evaluation-record";

    /**
     * 新增评价记录
     * @param dto 采购商评论的DTO
     * @param userId 根据userId获取当前的电话，昵称，姓名等信息
     * @return 返回新增记录的ID
     */

    Long save(StallEvaluationRecordDTO dto, Long userId);

    /**
     * 审核评论
     */
    void examine(StallEvaluationRecordDTO dto);

    /**
     * 删除评论，并且解绑对应的File
     * @param id 根据ID删除评论
     *
     */
    void remove(String id);

}
