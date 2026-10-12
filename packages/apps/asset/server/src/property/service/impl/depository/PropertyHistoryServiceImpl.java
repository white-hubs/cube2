package com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.depository;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyHistoryPageParam;
import com.guhecloud.cube.property.management.client.property.vo.property.AllotPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyHistoryListVO;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.impl.UserClientService;
import com.guhecloud.cube.srv.gen.property.management.server.common.util.ExcelUtils;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyAllotMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyDetailedMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyHistoryMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyDetailed;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyHistory;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.HandleTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertyStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyHistoryService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyHistoryServiceImpl extends ServiceImpl<PropertyHistoryMapper, PropertyHistory> implements PropertyHistoryService {
    private final UserClientService userClientService;
    private final UserClient userClient;
    private final PropertyAllotMapper propertyAllotMapper;
    private final PropertyDetailedMapper detailedMapper;
    private final FileClient fileClient;

    @Override
    public Pagination<PropertyHistoryListVO> getPropertyHistoryList(PropertyHistoryPageParam param) {
        if (ObjectUtils.isEmpty(param.getId()) && ObjectUtils.isNotEmpty(param.getBimCode())) {
            PropertyDetailed propertyDetailed = detailedMapper.selectOne(new LambdaQueryWrapper<PropertyDetailed>()
                                                                                 .eq(PropertyDetailed::getDeviceNumber, param.getBimCode())
                                                                                 .last("limit 1"));
            Assert.isParameterError(Objects.isNull(propertyDetailed), "资产信息不存在");
            param.setId(propertyDetailed.getId());
        }
        if (ObjectUtils.isNotEmpty(param.getId())) {
            // 通过id查询该资产的所有履历
            List<PropertyHistory> data = new LambdaQueryChainWrapper<>(this.baseMapper)
                    .select()
                    .eq(PropertyHistory::getPropertyId, param.getId())
                    .eq(StringUtils.isNotEmpty(param.getHandleTime()), PropertyHistory::getHandleTime, param.getHandleTime())
                    .eq(StringUtils.isNotEmpty(param.getDepartmentId()), PropertyHistory::getDepartmentId, param.getDepartmentId())
                    .eq(StringUtils.isNotEmpty(param.getHandleType()), PropertyHistory::getHandleTypeId, param.getHandleType())
                    .list();
            Pagination<PropertyHistoryListVO> history = new Pagination<>(
                    data == null ? 0L : data.size(), 1L, ConvertUtils.toList(PropertyHistoryListVO.class, data));
            for (PropertyHistoryListVO ex : history.getRecords()) {
                ex.setCreateByName(userClientService.getCreateByName(ex.getCreateBy(), ex.getCreatorId(), ex.getCreatorType()));
            }
            return history;
        }
        // 查询所有资产履历
        param.getPage();
        Page<PropertyHistoryListVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.getPropertyHistoryPage(page, param);
        for (PropertyHistoryListVO ex : page.getRecords()) {
            if (PropertyStatusEnum.MAKE_USE_OF.getRemarks().equals(ex.getStatus())) {
                AllotPageVO allotPageVO = propertyAllotMapper.queryByPropertyId(ex.getPropertyId());
                ex.setReceiveUserName(allotPageVO.getReceiveUserName());
                ex.setReceiveDepName(allotPageVO.getReceiveDepName());
            }
            if (ObjectUtils.isNotEmpty(ex.getImgs())) {
                List<Long> fileIdds = Arrays.stream(ex.getImgs().split(","))
                        .map(s -> Long.parseLong(s.trim()))
                        .collect(Collectors.toList());
                List<FileVO> file = fileClient.getByIds(fileIdds);
                if (ObjectUtils.isNotEmpty(file)) {
                    ex.setImg(file);
                }
            }
            ex.setCreateByName(userClientService.getCreateByName(ex.getCreateBy(), ex.getCreatorId(), ex.getCreatorType()));
        }
        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords());
    }

    @Override
    public void getPropertyHistorygetExcel(PropertyHistoryPageParam propertyHistory, HttpServletRequest request,
                                           HttpServletResponse response
                                          ) {
        propertyHistory.setPageNumber(1);
        propertyHistory.setPageSize(10000);
        Pagination<PropertyHistoryListVO> page = this.getPropertyHistoryList(propertyHistory);
        List<PropertyHistoryListVO> datas = page.getRecords();
        if (ObjectUtils.isEmpty(datas)) {
            PropertyHistoryListVO extend = new PropertyHistoryListVO();
            datas.add(extend);
        } else {
            datas.forEach(ex -> {
                ex.setStatus(PropertyStatusEnum.getRemarks(ex.getStatus()));
            });
        }
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("name", "资产名称");
        writer.addHeaderAlias("serialNumber", "资产编码");
        writer.addHeaderAlias("typeName", "资产类型");
        writer.addHeaderAlias("status", "状态");
        writer.addHeaderAlias("price", "金额");
        writer.addHeaderAlias("createByName", "使用人");
        writer.addHeaderAlias("receiveDepName", "使用部门");
        writer.addHeaderAlias("purchaseDate", "购入日期");
        writer.addHeaderAlias("handleTypeName", "操作类型");
        writer.addHeaderAlias("createByName", "处理人");
        writer.addHeaderAlias("handleTime", "处理时间");
        writer.addHeaderAlias("remarks", "处理内容");
        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        writer.setColumnWidth(-1, 25);
        String fileName = "资产履历";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    @Override
    public Boolean insert(PropertyHistory propertyHistory) {
        return this.baseMapper.insert(propertyHistory) > 0;
    }

    @Override
    public Boolean insert(List<PropertyDetailed> propertyDetaileds) {
        log.info("共{}条资产开始新增履历", propertyDetaileds.size());
        if (ObjectUtils.isEmpty(propertyDetaileds)) {
            throw new ServiceException("新增履历：资产信息不能为空");
        }
        // 获取当前用户信息
        Principal principal = SecurityContextHolder.getPrincipal();
        UserDTO userDTO = userClient.getUser(principal.getId());
        for (PropertyDetailed ex : propertyDetaileds) {
            PropertyHistory propertyHistory = PropertyHistory.getPropertyHistory(HandleTypeEnum.DEPOSITORY, userDTO, ex, "入库了", true);
            baseMapper.insert(propertyHistory);
        }
        return true;
    }

    @Override
    public Boolean insertByIds(List<Long> ids, HandleTypeEnum handleTypeEnum, String remarks) {
        for (Long id : ids) {
            Principal principal = SecurityContextHolder.getPrincipal();
            UserDTO userDTO = userClient.getUser(principal.getId());
            PropertyDetailed detailed = detailedMapper.selectById(id);
            PropertyHistory propertyHistory = PropertyHistory.getPropertyHistory(handleTypeEnum, userDTO, detailed, remarks, true);
            baseMapper.insert(propertyHistory);
        }
        return true;
    }

    @Override
    public void getExcel() {

    }
}
