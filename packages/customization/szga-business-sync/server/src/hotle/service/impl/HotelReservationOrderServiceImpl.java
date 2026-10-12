package com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration.FlowResult;
import com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.dao.HotelReservationOrderMapper;
import com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.entity.HotelReservationOrder;
import com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.enumeration.OrderStatus;
import com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.service.HotelReservationOrderService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.vo.HotelReservationFromVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.vo.HotelReservationVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.ProcessCompletedMessage;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import instance.command.SubmitInitFormCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/27 17:28
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HotelReservationOrderServiceImpl extends ServiceImpl<HotelReservationOrderMapper, HotelReservationOrder> implements HotelReservationOrderService {
    private final FlowClient flowClient;

    private final ProcessInstanceClient processInstanceClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String reserveHotelSubmit(SubmitInitFormCommand command) {
        HotelReservationVO hotelReservationVO = analyzeFormData(command.getFormDefinitionId(), command.getData());
        this.validate(hotelReservationVO);
        return flowClient.submitFirst(command);
    }

    @Override
    public Pagination<HotelReservationFromVO> getApprovalRecord(ProcessInstancePageQuery processInstancePageQuery) {
        Pagination<ProcessInstanceDTO> page = processInstanceClient.allPage(processInstancePageQuery);
        if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
            return new Pagination<>();
        }
        List<String> processIdList = page.getRecords().stream().map(ProcessInstanceDTO::getProcessInstanceId).distinct()
                .collect(Collectors.toList());
        List<HotelReservationOrder> hotelReservationOrders = this.lambdaQuery().in(HotelReservationOrder::getId, processIdList).list();
        Map<String, OrderStatus> orderStatusMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(hotelReservationOrders)) {
            orderStatusMap = hotelReservationOrders.stream()
                    .collect(Collectors.toMap(HotelReservationOrder::getId, HotelReservationOrder::getStatus));
        }
        Map<String, OrderStatus> finalOrderStatusMap = orderStatusMap;
        List<HotelReservationFromVO> hotelReservationFromVOS = page.getRecords().stream().map(record -> {
            HotelReservationFromVO vo = ObjectUtils.copying(record, HotelReservationFromVO.class);
            vo.setOrderStatus(finalOrderStatusMap.get(record.getProcessInstanceId()));
            return vo;
        }).collect(Collectors.toList());

        return PagingUtils.convert(page, hotelReservationFromVOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createHotelReservationOrder(ProcessCompletedMessage message) {
        var now = LocalDateTime.now();
        FlowResult result = message.getResult();
        HotelReservationVO hotelReservationVO = JsonUtils.deserialize(message.getData(), HotelReservationVO.class);
        if (Objects.isNull(hotelReservationVO)) {
            log.info("流程表单解析失败");
        }
        HotelReservationOrder reservationOrder = this.lambdaQuery().eq(HotelReservationOrder::getId, message.getProcessInstanceId())
                .last("limit 1").one();
        if (Objects.isNull(reservationOrder)) {
            HotelReservationOrder hotelReservationOrder = new HotelReservationOrder();
            buildHotelReservationOrder(hotelReservationOrder, hotelReservationVO, result);
            hotelReservationOrder.setId(message.getProcessInstanceId());
            hotelReservationOrder.setTenant(message.getTenant());
            hotelReservationOrder.setDeleted(0L);
            hotelReservationOrder.setCreateTime(now);
            hotelReservationOrder.setUpdateTime(now);
            hotelReservationOrder.setStatus(OrderStatus.NOT_ACCEPTED);
            this.save(hotelReservationOrder);
        } else {
            buildHotelReservationOrder(reservationOrder, hotelReservationVO, result);
            reservationOrder.setUpdateTime(now);
            this.updateById(reservationOrder);

        }
    }

    private void buildHotelReservationOrder(HotelReservationOrder reservationOrder, HotelReservationVO hotelReservationVO,
                                            FlowResult result
                                           ) {
        reservationOrder.setSubmitUser(hotelReservationVO.getSubmitUser());
        reservationOrder.setPhone(hotelReservationVO.getPhone());
        reservationOrder.setIdCard(hotelReservationVO.getIdCard());
        reservationOrder.setWorkUnit(hotelReservationVO.getWorkUnit());
        reservationOrder.setStayTime(hotelReservationVO.getStayTime());
        reservationOrder.setLeaveTime(hotelReservationVO.getLeaveTime());
        reservationOrder.setApprovalResult(result);
    }

    /**
     * 数据校验
     *
     * @param hotelReservationVO
     */
    private void validate(HotelReservationVO hotelReservationVO) {
        Assert.isParameterError(StringUtils.isEmpty(hotelReservationVO.getSubmitUser()), "姓名不能为空");
        Assert.isParameterError(hotelReservationVO.getSubmitUser().length() > 50, "姓名不能大于50个字符");
        Assert.isParameterError(StringUtils.isEmpty(hotelReservationVO.getPhone()), "联系电话不能为空");
        Assert.isParameterError(StringUtils.isEmpty(hotelReservationVO.getIdCard()), "身份证号不能为空");
        Assert.isParameterError(Objects.isNull(hotelReservationVO.getStayTime()), "入住时间不能为空");
        Assert.isParameterError(Objects.isNull(hotelReservationVO.getLeaveTime()), "退房时间不能为空");
        Assert.isParameterError(StringUtils.isNoneBlank(hotelReservationVO.getWorkUnit())
                                && hotelReservationVO.getWorkUnit().length() > 100, "单位名称不能大于100个字符");
    }

    /**
     * 解析表单内容
     *
     * @param data
     *
     * @return
     */
    private HotelReservationVO analyzeFormData(Long formDefinitionId, String data) {
        FormDataParam formDataParam = new FormDataParam(formDefinitionId, data);
        HotelReservationVO hotelReservationVO = null;
        try {
            String fromString = flowClient.formParse(formDataParam);
            hotelReservationVO = JSONObject.parseObject(fromString, HotelReservationVO.class);
        } catch (Exception exception) {
            throw new ServiceException("表单解析失败,请检查流程配置信息");
        }
        return hotelReservationVO;
    }
}
