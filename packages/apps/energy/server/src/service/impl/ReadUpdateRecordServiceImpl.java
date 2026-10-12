package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.ReadUpdateRecordMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.ReadUpdateRecord;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.ReadUpdateRecordParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.ReadUpdateRecordQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.ReadUpdateRecordService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ReadUpdateRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：抄表修改记录服务层
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReadUpdateRecordServiceImpl extends ServiceImpl<ReadUpdateRecordMapper, ReadUpdateRecord> implements ReadUpdateRecordService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(ReadUpdateRecordParam param) {
        ReadUpdateRecord readUpdateRecord = ObjectUtils.copying(param, ReadUpdateRecord.class);
        this.baseMapper.insert(readUpdateRecord);
        return readUpdateRecord.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, ReadUpdateRecordParam param) {
        ReadUpdateRecord readUpdateRecord = this.baseMapper.selectById(id);
        Assert.isNotFound(readUpdateRecord, "未查询到相关信息");
        ObjectUtils.copying(param, readUpdateRecord);
        this.baseMapper.updateById(readUpdateRecord);
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
    public ReadUpdateRecordVO getById(Long id) {
        ReadUpdateRecord readUpdateRecord = this.baseMapper.selectById(id);
        return ObjectUtils.copying(readUpdateRecord, ReadUpdateRecordVO.class);
    }

    @Override
    public Pagination<ReadUpdateRecordVO> getPage(ReadUpdateRecordQueryParam param) {
        //请自行添加查询条件
        Page<ReadUpdateRecord> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, ReadUpdateRecordVO.class);
    }
}