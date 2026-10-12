package com.guhecloud.cube.srv.gen.maintain.server.electron.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.NfcDetailsMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.NfcDetails;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.DeleteStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.NfcStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.NfcDetailsPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.NfcDetailsParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.NfcDetailsService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.NfcDetailsVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NfcDetailsServiceImpl extends ServiceImpl<NfcDetailsMapper, NfcDetails> implements NfcDetailsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return nfc信息
     */
    @Override
    public NfcDetailsVO queryById(Long id) {
        NfcDetails nfcDetails = this.baseMapper.selectById(id);
        return ConvertUtils.convert(NfcDetailsVO.class, nfcDetails);
    }

    /**
     * 分页查询
     * q.isNull(PatrolTaskLocation::getResult).or().isNull(PatrolTaskLocation::getPatrolDate);
     *
     * @param param 筛选条件
     *
     * @return 分页数据
     */
    @Override
    public Pagination<NfcDetailsVO> queryNcfPage(NfcDetailsPageParam param) {
        Page<NfcDetails> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.selectPage(page, new LambdaQueryWrapper<NfcDetails>()
                .and(StringUtils.isNotBlank(param.getKeyword()),
                     q -> q.like(NfcDetails::getBluetoothCode, param.getKeyword()).or().like(NfcDetails::getNfcCode, param.getKeyword()))
                .in(NfcDetails::getStatus, NfcStatusEnum.UNBOUND.name(), NfcStatusEnum.BOUND.name())
                .orderByDesc(NfcDetails::getCreateTime));
        return new Pagination<>(page.getTotal(), page.getPages(), ConvertUtils.toList(NfcDetailsVO.class, page.getRecords()));
    }

    @Override
    public List<NfcDetailsVO> queryNcfAll(Long deviceId) {
        List<NfcDetails> nfcDetails = baseMapper.selectList(new LambdaQueryWrapper<NfcDetails>()
                                                                    .eq(NfcDetails::getStatus, NfcStatusEnum.UNBOUND.name())
                                                                    .orderByDesc(NfcDetails::getCreateTime));
        if (deviceId != null) {
            final NfcDetails detailsVO = this.getById(deviceId);
            if (ObjectUtils.isEmpty(nfcDetails)) {
                nfcDetails = Collections.singletonList(detailsVO);
            } else {
                nfcDetails.add(detailsVO);
            }
        }
        return ConvertUtils.toList(NfcDetailsVO.class, nfcDetails);
    }

    /**
     * 新增数据
     *
     * @param param nfc信息
     *
     * @return 新增结果
     */
    @Override
    public Boolean insert(NfcDetailsParam param) {
        checkNfcDetailsParam(param);
        NfcDetails nfcDetails = ConvertUtils.convert(NfcDetails.class, param);
        nfcDetails.setStatus(NfcStatusEnum.UNBOUND.name());
        return this.baseMapper.insert(nfcDetails) > 0;
    }

    public void checkNfcDetailsParam(NfcDetailsParam param) {
        Assert.isParameterError((StringUtils.isBlank(param.getBluetoothCode()) && StringUtils.isBlank(param.getNfcCode())),
                                "蓝牙编码和NFC编码不能同时为空!");
        NfcDetails details = baseMapper.selectOne(new LambdaQueryWrapper<NfcDetails>()
                                                          .eq(NfcDetails::getDeviceCode, param.getDeviceCode())
                                                          .ne(NfcDetails::getStatus, NfcStatusEnum.REMOVE.name())
                                                          .ne(param.getId() != null, NfcDetails::getId, param.getId()));
        Assert.isConflict(ObjectUtils.isNotEmpty(details), param.getDeviceCode() + "设备码已存在");
        if (StringUtils.isNotBlank(param.getNfcCode())) {
            details = baseMapper.selectOne(new LambdaQueryWrapper<NfcDetails>()
                                                   .eq(NfcDetails::getNfcCode, param.getNfcCode())
                                                   .ne(NfcDetails::getStatus, NfcStatusEnum.REMOVE.name())
                                                   .ne(param.getId() != null, NfcDetails::getId, param.getId()));
            String nfcCode = ObjectUtils.isNotEmpty(details) ? details.getNfcCode() : "";
            Assert.isConflict(ObjectUtils.isNotEmpty(details), nfcCode + "nfc芯片:已经绑定设备");
        }
        if (StringUtils.isNotBlank(param.getBluetoothCode())) {
            details = baseMapper.selectOne(new LambdaQueryWrapper<NfcDetails>()
                                                   .eq(NfcDetails::getBluetoothCode, param.getBluetoothCode())
                                                   .ne(NfcDetails::getStatus, NfcStatusEnum.REMOVE.name())
                                                   .ne(param.getId() != null, NfcDetails::getId, param.getId()));
            String bluetoothCode = ObjectUtils.isNotEmpty(details) ? details.getBluetoothCode() : "";
            Assert.isConflict(ObjectUtils.isNotEmpty(details), bluetoothCode + "蓝牙:已经绑定设备");
        }
    }

    /**
     * 批量插入
     *
     * @param param nfc信息
     */
    @Override
    public Boolean insert(List<NfcDetailsParam> param) {
        if (ObjectUtils.isNotEmpty(param) && !param.isEmpty()) {
            param.removeFirst();
        }
        param.forEach(this::insert);
        return true;
    }

    /**
     * 更新数据
     *
     * @param param nfc信息
     *
     * @return 更新结果
     */
    @Override
    public Boolean update(NfcDetailsParam param) {
        checkNfcDetailsParam(param);
        NfcDetails nfcDetails = this.baseMapper.selectById(param.getId());
        nfcDetails.setNfcCode(param.getNfcCode());
        nfcDetails.setNfcLongitude(param.getNfcLongitude());
        nfcDetails.setNfcDimension(param.getNfcDimension());
        nfcDetails.setRemarks(param.getRemarks());
        nfcDetails.setBluetoothCode(param.getBluetoothCode());
        nfcDetails.setDeviceCode(param.getDeviceCode());
        return baseMapper.updateById(nfcDetails) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @Override
    public Boolean deleteById(Long id) {
        NfcDetails nfcDetails = this.baseMapper.selectById(id);
        if (NfcStatusEnum.BOUND.name().equals(nfcDetails.getStatus())) {
            throw new ServiceException("该nfc芯片已经绑定巡更点，请先取消绑定");
        }
        nfcDetails.setStatus(DeleteStatusEnum.REMOVE.name());
        return baseMapper.updateById(nfcDetails) > 0;
    }
}
