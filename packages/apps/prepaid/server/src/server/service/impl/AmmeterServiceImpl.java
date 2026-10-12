package com.guhecloud.cube.srv.prepaid.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.prepaid.common.constants.Constants;
import com.guhecloud.cube.srv.prepaid.common.vo.AmmeterDTO;
import com.guhecloud.cube.srv.prepaid.server.dao.AmmeterMapper;
import com.guhecloud.cube.srv.prepaid.server.param.AmmeterPageParam;
import com.guhecloud.cube.srv.prepaid.server.po.AmmeterPO;
import com.guhecloud.cube.srv.prepaid.server.result.AmmeterPageResult;
import com.guhecloud.cube.srv.prepaid.server.result.UnConnectAmmeterStatResult;
import com.guhecloud.cube.srv.prepaid.server.service.AmmeterService;
import com.guhecloud.cube.srv.prepaid.server.third.client.AmmeterClient;
import com.guhecloud.cube.srv.prepaid.server.utils.PageResponse;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 电表服务
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AmmeterServiceImpl extends ServiceImpl<AmmeterMapper, AmmeterPO> implements AmmeterService {

    private final AmmeterClient ammeterClient;

    /**
     * 电表分页列表
     *
     * @param param 入参
     *
     * @return 分页
     */
    @Override
    public Pagination<AmmeterPageResult> getAmmeterPage(AmmeterPageParam param) {
        List<AmmeterDTO> dtoList = ammeterClient.getAmmeterList(param.getMeterId());
        // 条件
        boolean oweMoney = param.getOweMoney() != null && Boolean.TRUE.equals(param.getOweMoney());
        boolean unConnect = param.getUnConnect() != null && Boolean.TRUE.equals(param.getUnConnect());
        if (oweMoney || unConnect) {
            Stream<AmmeterDTO> stream = dtoList.stream();
            if (oweMoney) {
                stream = stream.filter(dto -> dto.getOweMoney() != null && Boolean.TRUE.equals(dto.getOweMoney()));
            }
            if (unConnect) {
                stream = stream.filter(dto -> dto.getUnConnect() != null && Boolean.TRUE.equals(dto.getUnConnect()));
            }
            dtoList = stream.collect(Collectors.toList());
        }
        // 排序
        dtoList.sort(Comparator.comparing(AmmeterDTO::getMeterId));
        // 内存分页
        Pagination<AmmeterDTO> dtoPagination = PageResponse.doPage(dtoList, param.getPageNumber(), param.getPageSize());

        return new Pagination<>(dtoPagination.getTotal(), dtoPagination.getPages(), dtoPagination.getRecords().stream().map(po -> {
            AmmeterPageResult pageResult = new AmmeterPageResult();
            BeanUtils.copyProperties(po, pageResult);
            return pageResult;
        }).collect(Collectors.toList()));
    }

    /**
     * 获取电表编号下拉列表
     *
     * @return 电表编号列表
     */
    @Override
    public List<String> getAmmeterIdList() {
        List<String> resultList = new ArrayList<>();
        List<AmmeterDTO> dtoList = ammeterClient.getAmmeterList(null);
        if (dtoList == null || dtoList.isEmpty()) {
            return resultList;
        }
        return dtoList.stream().map(AmmeterDTO::getMeterId).distinct().collect(Collectors.toList());
    }

    /**
     * 获取失联电表统计
     *
     * @return 统计对象
     */
    @Override
    public UnConnectAmmeterStatResult getUnConnectAmmeterStat() {
        UnConnectAmmeterStatResult result = new UnConnectAmmeterStatResult();
        result.setTotal(Constants.ZERO_INT);
        result.setOweMoney(Constants.ZERO_INT);
        result.setUnOweMoney(Constants.ZERO_INT);
        result.setUnConnect(Constants.ZERO_INT);
        result.setConnect(Constants.ZERO_INT);
        result.setUnConnectList(new ArrayList<>());
        List<AmmeterDTO> dtoList = ammeterClient.getAmmeterList(null);
        if (dtoList == null || dtoList.isEmpty()) {
            return result;
        }
        result.setTotal(dtoList.size());

        // 欠费数
        int oweMoneyCount = Constants.ZERO_INT;
        // 未欠费数
        int unOweMoneyCount = Constants.ZERO_INT;
        // 正常连接数
        int connectCount = Constants.ZERO_INT;
        // 失联电表列表
        List<AmmeterPageResult> unConnectList = new ArrayList<>();

        for (AmmeterDTO ammeterDTO : dtoList) {
            if (Boolean.FALSE.equals(ammeterDTO.getOweMoney())) {
                unOweMoneyCount++;
            } else if (Boolean.TRUE.equals(ammeterDTO.getOweMoney())) {
                oweMoneyCount++;
            }
            if (Boolean.FALSE.equals(ammeterDTO.getUnConnect())) {
                connectCount++;
            } else if (Boolean.TRUE.equals(ammeterDTO.getUnConnect())) {
                AmmeterPageResult pageResult = new AmmeterPageResult();
                BeanUtils.copyProperties(ammeterDTO, pageResult);
                unConnectList.add(pageResult);
            }
        }
        result.setOweMoney(oweMoneyCount);
        result.setUnOweMoney(unOweMoneyCount);
        result.setConnect(connectCount);
        result.setUnConnect(unConnectList.size());
        result.setUnConnectList(unConnectList);
        return result;
    }

}
