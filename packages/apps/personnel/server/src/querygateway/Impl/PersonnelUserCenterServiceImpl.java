package com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.StaffTypeCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.dto.excel.UserExcelDTO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.UserCenterVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffTypePO;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.PersonnelUserCenterService;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.StaffQueryService;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.query.UserCenterParam;
import com.guhecloud.cube.srv.gen.visitor.management.client.ReservationRecordClient;
import com.guhecloud.cube.srv.gen.visitor.management.client.param.ReservationRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.client.vo.ReservationRecordResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DesensitizeUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/6 17:18
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class PersonnelUserCenterServiceImpl implements PersonnelUserCenterService {
    private static final String VISITOR_TYPE = "访客";
    private final static Integer MAX_PAGE_SIZE = 10000;
    private final StaffQueryService staffQueryService;
    private final ReservationRecordClient reservationRecordClient;
    private final StaffTypeCommandService staffTypeCommandService;
    private final UserClient userClient;
    private final FileClient fileClient;
    private final ExcelUtils excelUtils;

    private static String phoneEncryption(String phone, Boolean phoneEncryption) {
        if (ObjectUtils.isNotEmpty(phoneEncryption) && phoneEncryption && ObjectUtils.isNotEmpty(phone)) {
            try {
                return DesensitizeUtils.desensitize(phone, DesensitizeUtils.DESENSITIZE_MODE_MOBILE);
            } catch (Exception e) {
                log.error("staff:员工脱敏失败", e);
            }
        }
        return phone;
    }

    @Override
    public Pagination<UserCenterVO> getUserCenterPage(UserCenterParam userCenterParam) {
        boolean onlyVisitor = false;
        boolean limitType = false;
        if (StringUtils.isNotBlank(userCenterParam.getType())) {
            String type = staffTypeCommandService.valueOfType(userCenterParam.getType());
            onlyVisitor = VISITOR_TYPE.equals(type);
            limitType = !onlyVisitor;
        }
        List<UserCenterVO> result = new ArrayList<>();
        Integer pageL = userCenterParam.getPageNumber();
        Integer pageSizeL = userCenterParam.getPageSize();
        //员工总数
        long count = !onlyVisitor ? staffQueryService.lambdaQuery()
                .like(StringUtils.isNotBlank(userCenterParam.getKeyword()), StaffPO::getName, userCenterParam.getKeyword())
                .eq(StringUtils.isNotBlank(userCenterParam.getType()), StaffPO::getType, userCenterParam.getType())
                .count() : 0;
        //访客总数
        ReservationRecordParam recordParam = new ReservationRecordParam();
        recordParam.setName(userCenterParam.getKeyword());
        Integer visitorCount = !limitType ? reservationRecordClient.reservationRecordCount(recordParam) : 0;

        //获取分页数据
        long total = count + visitorCount;
        Page<StaffPO> staffPOPage = new Page<>();
        if (!onlyVisitor) {
            staffPOPage = staffQueryService.lambdaQuery()
                    .select(StaffPO::getName, StaffPO::getId, StaffPO::getType, StaffPO::getUserId, StaffPO::getMobile)
                    .like(StringUtils.isNotBlank(userCenterParam.getKeyword()), StaffPO::getName, userCenterParam.getKeyword())
                    .eq(StringUtils.isNotBlank(userCenterParam.getType()), StaffPO::getType, userCenterParam.getType())
                    .page(PagingUtils.convert(userCenterParam));
            if (CollectionUtils.isEmpty(staffPOPage.getRecords())) {
                return new Pagination<>();
            }
            Set<Long> userIds = new HashSet<>();
            Set<String> types = new HashSet<>();
            Map<String, String> staffTypeMap = new HashMap<>();
            Map<Long, Long> userPhotoMap = new HashMap<>();
            Map<Long, List<FileVO>> photographMap = new HashMap<>();
            for (StaffPO record : staffPOPage.getRecords()) {
                if (Objects.nonNull(record.getUserId())) {
                    userIds.add(record.getUserId());
                }
                if (StringUtils.isNotBlank(record.getType())) {
                    types.add(record.getType());
                }
            }
            if (!CollectionUtils.isEmpty(userIds)) {
                List<UserDTO> userDTOS = userClient.listEnableUserByIds(userIds);
                List<Long> photographKey = new ArrayList<>();
                for (UserDTO userDTO : userDTOS) {
                    if (Objects.nonNull(userDTO.getPhotographKey())) {
                        photographKey.add(userDTO.getPhotographKey());
                        userPhotoMap.put(userDTO.getId(), userDTO.getPhotographKey());
                    }
                }
                if (!CollectionUtils.isEmpty(photographKey)) {
                    BusinessKeysParam businessKeysParam = new BusinessKeysParam();
                    businessKeysParam.setBusinessKeys(photographKey);
                    photographMap = fileClient.getByBusinessKeys(businessKeysParam);
                }

            }

            if (!CollectionUtils.isEmpty(types)) {
                staffTypeMap = staffTypeCommandService.lambdaQuery().in(StaffTypePO::getCode, types).list()
                        .stream().collect(Collectors.toMap(StaffTypePO::getCode, StaffTypePO::getName));
            }
            for (StaffPO record : staffPOPage.getRecords()) {
                result.add(UserCenterVO.builder()
                                   .id(record.getUserId())
                                   .name(record.getName())
                                   .phone(record.getMobile())
                                   .type(record.getType())
                                   .typeName(staffTypeMap.get(record.getType()))
                                   .fileVO(photographMap.get(userPhotoMap.getOrDefault(record.getUserId(), 0L)))
                                   .build());
            }
            if (limitType) {
                return buildResult(result, total, pageSizeL);
            }

        }
        // 当前页面数据不足分页条件
        if (staffPOPage.getRecords().size() < pageSizeL) {
            int needPageSize = pageSizeL - staffPOPage.getRecords().size();
            recordParam.setPageNumber(userCenterParam.getPageNumber());
            recordParam.setPageSize(needPageSize);
            recordParam.setName(userCenterParam.getKeyword());
            Pagination<ReservationRecordResult> reservationRecordResultPagination = reservationRecordClient.reservationPersonnelPage(recordParam);
            for (ReservationRecordResult record : reservationRecordResultPagination.getRecords()) {
                result.add(UserCenterVO.builder()
                                   .name(record.getName())
                                   .phone(record.getPhone())
                                   .typeName(VISITOR_TYPE)
                                   .build());
            }
        }
        return buildResult(result, total, pageSizeL);
    }

    @Override
    public void exportUsers(@NonNull UserCenterParam param) {
        Integer pageNumber = param.getPageNumber();
        param.setPageNumber(pageNumber == null || pageNumber == 0 ? 1 : pageNumber);
        param.setPageSize(MAX_PAGE_SIZE);
        String filename = "用户列表";
        Pagination<UserCenterVO> page = this.getUserCenterPage(param);
        List<UserCenterVO> records = page.getRecords();
        exportPositionData(filename, records);

    }

    private void exportPositionData(String filename, List<UserCenterVO> records) {
        List<UserExcelDTO> userExcelDTOList = ListUtils.convertItem(records, vo -> ObjectUtils.copying(vo, UserExcelDTO.class));

        //导出
        excelUtils.downloadExcel(filename, userExcelDTOList, UserExcelDTO.class);
    }

    private Pagination<UserCenterVO> buildResult(List<UserCenterVO> result, long total, Integer pageSizeL) {
        Set<Long> resultUserIds = result.stream().map(UserCenterVO::getId).collect(Collectors.toSet());
        //获取加密配置
        Map<Long, Boolean> sensitiveConfig = staffQueryService.getSensitiveConfig(resultUserIds);
        result.forEach(t -> t.setPhone(phoneEncryption(t.getPhone(), sensitiveConfig.get(t.getId()))));
        return Pagination.<UserCenterVO>builder()
                .total(total)
                .pages(((total / pageSizeL) + 1))
                .records(result)
                .build();
    }
}
