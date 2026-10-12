package com.guhecloud.cube.onecard.server.consumption.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("copies_change_record")
public class CopiesChangeRecord extends BasicEnhancedTenantEntity<Long> {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String userCard;
    private LocalDateTime transactionTime;
    private Integer transactionCount; //交易份数
    private Integer transactionType; //交易类型 7 份消费 8 份补贴 9 份清零
    private LocalDateTime subsidyBeginDate; //补贴开始时间
    private LocalDateTime subsidyEndDate;  //补贴结束时间
    private String depName;
    private Integer transactionStatus; //交易状态 1成功 0 失败
}
