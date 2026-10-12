package com.guhecloud.cube.srv.gen.business.info.sync.server.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo.ExcelVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo.OuaaUser;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.User;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.UserOneCard;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.UserOrganization;
import com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration.Gender;
import com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration.PaperType;
import com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration.StatusEnum;
import com.guhecloud.cube.srv.gen.business.info.sync.server.mapper.SyncOrganizationMapper;
import com.guhecloud.cube.srv.gen.business.info.sync.server.mapper.SyncUserMapper;
import com.guhecloud.cube.srv.gen.business.info.sync.server.mapper.SyncUserOneCardMapper;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserOneCardService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserOrganizationService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 15561
 * @description 针对表【user_one_card(一卡通开通表)】的数据库操作Service实现
 * @createDate 2024-12-17 16:11:56
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SyncUserOneCardServiceImpl extends ServiceImpl<SyncUserOneCardMapper, UserOneCard>
        implements SyncUserOneCardService {
    private final SyncUserOrganizationService userOrganizationService;
    private final SyncOrganizationMapper syncOrganizationMapper;
    private final SyncUserMapper syncUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String userOneCardInit(Integer tenant, MultipartFile file) {
        List<OuaaUser> selectInitInfo = syncUserMapper.selectInitInfo();
        Map<Long, OuaaUser> inintUserMap = selectInitInfo
                .stream().collect(Collectors.toMap(OuaaUser::getId, Function.identity()));
        Map<String, OuaaUser> initMobileMap = selectInitInfo
                .stream().collect(Collectors.toMap(OuaaUser::getMobile, Function.identity()));
        List<User> userList =
                syncUserMapper.selectList(Wrappers.<User>lambdaQuery()
                                                  .eq(User::getDeleted, 0));
        Map<String, List<User>> userMapList =
                userList.stream().collect(Collectors.groupingBy(User::getMobile));
        List<Long> deleteUserIdList = new ArrayList<>();
        for (Map.Entry<String, List<User>> entry : userMapList.entrySet()) {
            if (entry.getValue().size() > 1) {
                deleteUserIdList.add(entry.getValue().get(0).getId());
            }
        }
        List<UserOneCard> list = this.lambdaQuery()
                .list();
        Map<String, UserOneCard> userOneCardMap = list
                .stream().collect(Collectors.toMap(UserOneCard::getSerialNumber, Function.identity()));
        Map<Long, List<User>> longListMap = userList.stream().collect(Collectors.groupingBy(User::getId));
        Map<String, List<User>> userMoblieMap = userList.stream().collect(Collectors.groupingBy(User::getMobile));
        //读取EXCEL中数据
        // 创建Excel读取器并指定数据类型
        ExcelReaderBuilder reader = null;
        try {
            reader = EasyExcel.read(file.getInputStream());
        } catch (IOException e) {
            log.error("ouaa:导入用户读取excel失败{}", e);
            throw new ServiceException("文件读取失败");
        }
        // 读取第一个sheet的数据
        List<ExcelVO> datas = reader.sheet(0).head(ExcelVO.class).doReadSync();
        var now = LocalDateTime.now();
        List<UserOneCard> userOneCardList = new ArrayList<>();
        List<UserOneCard> updateUserOneCardList = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            ExcelVO excelVO = datas.get(i);
            if (StringUtils.isBlank(excelVO.getId()) || StringUtils.isBlank(excelVO.getType())) {
                continue;
            }
            Long userId = Long.valueOf(excelVO.getId().trim());
            //电话和id都不存在则忽略
            if (!inintUserMap.containsKey(userId) && !initMobileMap.containsKey(excelVO.getId())) {
                continue;
            }
            if (!userOneCardMap.containsKey(excelVO.getSerialNumber())) {
                UserOneCard userOneCard = new UserOneCard();
                userOneCard.setId(IdWorker.getId());
                List<User> users = longListMap.get(userId);
                if (CollectionUtils.isEmpty(users) && (inintUserMap.containsKey(userId) || initMobileMap.containsKey(excelVO.getId()))) {
                    //ouaa中的userId,不存在根据电话找
                    OuaaUser ouaaUser = inintUserMap.get(userId);
                    if (Objects.isNull(ouaaUser)) {
                        ouaaUser = initMobileMap.get(excelVO.getId());
                        userId = ouaaUser.getId();
                    }
                    //sync中的用户
                    users = userMoblieMap.get(ouaaUser.getMobile());
                    //sync中不存在则需要添加
                    if (CollectionUtils.isEmpty(users)) {
                        //新增用户
                        User insterUser = User.builder()
                                .id(ouaaUser.getId())
                                .name(ouaaUser.getName())
                                .gender(Objects.nonNull(ouaaUser.getGender()) ? ouaaUser.getGender() : Gender.NONE.name())
                                .mobile(ouaaUser.getMobile())
                                .paperType(PaperType.ID_CARD.name())
                                .paperNumber(ouaaUser.getPaperNumber())
                                .enabled(1)
                                .status(com.guhecloud.platform.base.util.StringUtils.isNotBlank(ouaaUser.getStatus()) ? ouaaUser.getStatus()
                                                                                                                      : "FORMAL")
                                .tenant(SessionContextHolder.getRequester().getTenant())
                                .createTime(now)
                                .updateTime(now)
                                .build();
                        syncUserMapper.insert(insterUser);
                        //新增用户组织关系
                        UserOrganization userOrganization = new UserOrganization();
                        userOrganization.setUserId(insterUser.getId());
                        userOrganization.setOrganizationId(ouaaUser.getOrganizationId());
                        userOrganization.setCreateTime(now);
                        userOrganization.setUpdateTime(now);
                        userOrganization.setTenant(SessionContextHolder.getRequester().getTenant());
                        userOrganizationService.save(userOrganization);
                    } else {
                        User user = users.get(0);
                        syncUserMapper.update(null, Wrappers.<User>lambdaUpdate()
                                .eq(User::getId, user.getId())
                                .set(User::getId, userId));
                    }
                }
                userOneCard.setUserId(userId);
                userOneCard.setTenant(tenant);
                userOneCard.setCreateTime(now);
                userOneCard.setUpdateTime(now);
                String serialNumber;
                if (excelVO.getType().contains("IC")) {
                    serialNumber = excelVO.getSerialNumber().trim();
                } else {
                    serialNumber = datas.get(i + 1).getSerialNumber().trim();
                }
                userOneCard.setSerialNumber(serialNumber);
                userOneCard.setStatus(StatusEnum.OPEN.name());
                userOneCard.setValidityDate(excelVO.getValidityDate());
                userOneCardList.add(userOneCard);
            }
        }
        if (CollectionUtils.isNotEmpty(userOneCardList)) {
            this.saveBatch(userOneCardList);
        }
        if (CollectionUtils.isNotEmpty(updateUserOneCardList)) {
            this.updateBatchById(updateUserOneCardList);
        }
        return null;
    }
}




