package com.guhecloud.cube.base.dictionary.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;

@Data
@TableName("dictionary_item")
public class DictionaryItem extends BasicEnhancedEntity<Long> {
    private Long id;
    private String code;
    private String remarks;
    /**
     * 变更通知方式
     */
    private String changeNotice;
}
