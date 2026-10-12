package com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.depository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.property.management.client.common.param.WorkflowRelevance;
import com.guhecloud.cube.property.management.client.property.enumeration.MessageTypeEnum;
import com.guhecloud.cube.property.management.client.property.event.message.PropertyMessage;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedExcelParam;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedParam;
import com.guhecloud.cube.property.management.client.property.vo.property.AllotPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.DepositoryDetailedPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.impl.UserClientService;
import com.guhecloud.cube.srv.gen.property.management.server.common.util.ExcelUtile;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.DepositoryDetailedMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyAllotMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyDetailedMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.DepositoryDetailed;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyDetailed;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.FlowResult;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.OperateType;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertyStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.DepositoryDetailedService;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyDetailedService;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyHistoryService;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.assembly.ClassifyServiceImpl;
import com.guhecloud.cube.srv.gen.property.management.server.property.util.FilesUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DepositoryDetailedServiceImpl extends ServiceImpl<DepositoryDetailedMapper, DepositoryDetailed> implements DepositoryDetailedService {
    private final PropertyDetailedService propertyDetailedService;
    private final PropertyDetailedMapper propertyDetailedMapper;
    private final PropertyAllotMapper allotMapper;
    private final UserClientService userClientService;
    private final FileRefClient fileRefClient;
    private final FileClient fileClient;
    private final ClassifyServiceImpl classifyService;
    private final PropertyHistoryService propertyHistoryService;

    @Override
    public DepositoryDetailedPageVO queryById(Long id) {
        DepositoryDetailedPageVO data = baseMapper.getDepositoryDetailed(id);
        if (ObjectUtils.isEmpty(data)) {
            throw new RuntimeException("该数据详情不存在");
        }
        data.setCreateByName(userClientService.getCreateByName(data.getCreateBy(), data.getCreatorId(), data.getCreatorType()));
        // 查询附件
        data.setFile(fileClient.getByBusinessKey(data.getId()));
        for (PropertyDetailedVO ex : data.getPropertyDetailed()) {
            if (PropertyStatusEnum.MAKE_USE_OF.name().equals(ex.getStatus())) {
                AllotPageVO allot = allotMapper.queryByPropertyId(ex.getId());
                ex.setReceiveUserName(userClientService.getUser(allot.getReceiveUserId()));
                ex.setReceiveDepName(allot.getReceiveDepName());
            }
        }
        return data;
    }

    @Override
    public Pagination<DepositoryDetailedPageVO> getDepositoryDetailedPage(DepositoryDetailedPageParam param) {
        Page<DepositoryDetailedPageVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        Page<DepositoryDetailedPageVO> pageVo = baseMapper.getDepositoryDetailedPage(page, param);
        Pagination<DepositoryDetailedPageVO> pagination = new Pagination<>(page.getTotal(), pageVo.getCurrent(), pageVo.getRecords());
        pagination.getRecords().forEach(ex -> {
            List<PropertyDetailed> detailedList = propertyDetailedMapper.selectList(new LambdaQueryWrapper<PropertyDetailed>()
                                                                                            .select(PropertyDetailed::getName, PropertyDetailed::getSerialNumber)
                                                                                            .eq(PropertyDetailed::getDepositoryNumbers, ex.getDepositoryNumbers()));
            if (ObjectUtils.isNotEmpty(detailedList)) {
                ex.setPropertyDetailed(ConvertUtils.toList(PropertyDetailedVO.class, detailedList));
            }
            ex.setCreateByName(userClientService.getCreateByName(ex.getCreateBy(), ex.getCreatorId(), ex.getCreatorType()));
        });
        return pagination;
    }

    /**
     * 资产入库导出
     *
     * @param ids 筛选条件
     */
    @Override
    public void getDepositoryDetailedExcel(HttpServletResponse response, HttpServletRequest request, String ids) {
        Map<String, Map<String, String>> title = new HashMap<>();
        Map<String, List<?>> data = new HashMap<>();
        String[] array = ids.split(",");
        for (String id : array) {
            DepositoryDetailed detailed = baseMapper.selectById(Long.parseLong(id));
            Map<String, String> map = new HashMap<>();
            BigDecimal totalMoney = BigDecimal.ZERO;
            List<DepositoryDetailedExcelParam> detailedExcel = baseMapper.getDepositoryDetailedExcel(id);
            for (DepositoryDetailedExcelParam ex : detailedExcel) {
                ex.setMoney(BigDecimal.valueOf(ex.getNumber()).multiply(ex.getPrice()));
                totalMoney = totalMoney.add(ex.getMoney());
            }
            map.put("totalMoney", totalMoney.toString());
            map.put("depositoryNumbers", detailed.getDepositoryNumbers());
            String date = DateUtils.formatDate(detailed.getStartTime());
            map.put("date", date);
            title.put(id, map);
            data.put(id, detailedExcel);
        }
        try {
            log.info("----------------------开始导出固定资产入库清单----------------------");
            ExcelUtile.uploadExcelList(response, data, title, "固定资产入库清单.xlsx");
        } catch (Exception exception) {
            log.error("固定资产入库清单导出--{}", exception.getLocalizedMessage());
        }
    }

    @Override
    public Long insert(DepositoryDetailedParam param) {
        log.info("资产入库");
        DepositoryDetailed depository = ConvertUtils.convert(DepositoryDetailed.class, param);
        depository.setId(null);
        depository.setDepositoryNumbers(OperateType.getNumbers(OperateType.DEPOSITORY));
        depository.setStatus(FlowResult.UNDER.name());
        depository.setSourceName(classifyService.queryById(depository.getSourceId()).getName());
        log.info("基础数据开始入库，入库编号{}", depository.getDepositoryNumbers());
        Boolean bool = this.baseMapper.insert(depository) < 1;
        Assert.isParameterError(bool, "资产入库失败：基本信息有误！");
        if (ObjectUtils.isNotEmpty(param.getFiles())) {
            // 处理附件
            List<FileRefCreateParam> files = FilesUtil.buildFiles(depository.getId(), param.getFiles());
            fileRefClient.saveBatch(files);
        }
        bool = propertyDetailedService.insert(param.getPropertyDetailed(), depository);
        Assert.isParameterError(!bool, "资产入库失败：资产明细有误！");
        return depository.getId();
    }

    @Override
    public Boolean deleteById(Long id) {
        log.info("删除入库信息：{}", id);
        DepositoryDetailed depositoryDetailed = baseMapper.selectById(id);
        log.info("开始删除id:{}----入库基本信息", depositoryDetailed.getId());
        boolean bool = DatabaseContextHolder.softDelete(this, id);
        if (bool) {
            return propertyDetailedService.deleteByDepositoryNumbers(depositoryDetailed.getDepositoryNumbers());
        }
        return false;
    }

    @Override
    public Boolean update(WorkflowRelevance param) {
        DepositoryDetailed detailed = baseMapper.selectById(param.getPropertyId());
        detailed.setProcessInstanceId(param.getProcessInstanceId());
        return baseMapper.updateById(detailed) > 0;
    }

    @Override
    public Boolean submit(DepositoryDetailedParam param) {
        DepositoryDetailed detailed = baseMapper.selectOne(new LambdaQueryWrapper<DepositoryDetailed>()
                                                                   .eq(DepositoryDetailed::getProcessInstanceId, param.getProcessInstanceId()));
        detailed.setStatus(param.getStatus());
        // 修改资产状态
        if (FlowResult.PASS.name().equals(detailed.getStatus())) {
            propertyDetailedMapper.updateByDepositoryNumbers(detailed.getDepositoryNumbers());
            List<PropertyDetailed> ids = propertyDetailedMapper.getAllIdByDepository(detailed.getDepositoryNumbers());
            log.info("新增资产履历，共{}条数据:", ids.size());
            if (ObjectUtils.isNotEmpty(ids)) {
                propertyHistoryService.insert(ids);
            }
            // 发送新增资产消息
            RocketMQContextHolder.send(PropertyMessage.TOPIC, PropertyMessage.builder()
                    .id(detailed.getId())
                    .status(MessageTypeEnum.ADD.name())
                    .build());
        }
        return baseMapper.updateById(detailed) > 0;
    }
}
