package com.guhecloud.cube.biz.mdata.server.config.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.config.dao.StreamMediaConfigMapper;
import com.guhecloud.cube.biz.mdata.server.config.entity.StreamMediaConfig;
import com.guhecloud.cube.biz.mdata.server.config.param.StreamMediaConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.config.param.StreamMediaConfigQueryParam;
import com.guhecloud.cube.biz.mdata.server.config.param.StreamMediaConfigUpdateParam;
import com.guhecloud.cube.biz.mdata.server.config.service.StreamMediaConfigService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DigestUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 流媒体服务配置服务实现
 *
 * @author lanchun
 * @date 2021/12/7 15:10
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StreamMediaConfigServiceImpl extends ServiceImpl<StreamMediaConfigMapper, StreamMediaConfig> implements StreamMediaConfigService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull StreamMediaConfigModifyParam param) {
        StreamMediaConfig streamMediaConfig = ObjectUtils.copying(param, StreamMediaConfig.class);
        //密码进行md5加密
        streamMediaConfig.setEncryptPassword(DigestUtils.md5Hex(param.getPassword()).toLowerCase());
        this.baseMapper.insert(streamMediaConfig);
        return streamMediaConfig.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull StreamMediaConfigUpdateParam param) {
        StreamMediaConfig streamMediaConfig = this.getById(id);
        Assert.isNotFound(streamMediaConfig, "配置不存在");
        streamMediaConfig.setName(param.getName());
        streamMediaConfig.setIntranetAddress(param.getIntranetAddress());
        streamMediaConfig.setIpAddress(param.getIpAddress());
        streamMediaConfig.setUsername(param.getUsername());
        if (StringUtils.isNotBlank(param.getPassword())) {
            streamMediaConfig.setPassword(param.getPassword());
            //密码进行md5加密
            streamMediaConfig.setEncryptPassword(DigestUtils.md5Hex(param.getPassword()).toLowerCase());
        }
        this.baseMapper.updateById(streamMediaConfig);
    }

    @Override
    public StreamMediaConfig getOne() {
        List<StreamMediaConfig> streamMediaConfigs = this.lambdaQuery().list();
        return CollectionUtils.isEmpty(streamMediaConfigs) ? null : streamMediaConfigs.get(0);
    }

    @Override
    public StreamMediaConfig getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<StreamMediaConfig> listStreamMediaConfigs(@NonNull Collection<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.lambdaQuery().in(StreamMediaConfig::getId, ids).list();
    }

    @Override
    public Page<StreamMediaConfig> listStreamMediaConfigs(StreamMediaConfigQueryParam param) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), StreamMediaConfig::getName, param.getKeyword())
                .page(PagingUtils.convert(param));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        redisTemplate.delete("h5s_stream_login_session" + ":" + SessionContextHolder.getRequester().getTenant());
        DatabaseContextHolder.softDelete(this, id);
    }
}
