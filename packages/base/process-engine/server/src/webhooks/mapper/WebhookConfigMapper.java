package com.guhecloud.cube.base.flow.webhooks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.flow.webhooks.domain.WebhookConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 15561
 * @description 针对表【webhook_config(业务回调配置表)】的数据库操作Mapper
 * @createDate 2024-06-14 16:34:36
 * @Entity generator.domain.WebhookConfig
 */
@Mapper
public interface WebhookConfigMapper extends BaseMapper<WebhookConfig> {

}




