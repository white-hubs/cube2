package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.enumeration.SafetyEducationFileSceneCodeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.dao.SafetyCultivateMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.SafetyCultivate;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.SafetyCultivateParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.SafetyCultivateQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.SafetyCultivateService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.SafetyCultivateStatisticVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.SafetyCultivateVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wql
 * @describe 安全培训(二维码中转页)
 * @date 2024/3/19 19:35
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyCultivateServiceImpl extends ServiceImpl<SafetyCultivateMapper, SafetyCultivate> implements SafetyCultivateService {
    private final FileClient fileClient;
    private final UserClient userClient;
    private final FileOperateClient fileOperateClient;

    @Override
    public Long saveSafetyCultivate(SafetyCultivateParam param) {
        SafetyCultivate copying = ObjectUtils.copying(param, SafetyCultivate.class);
        copying.setId(IdWorker.getId());
        this.save(copying);
        if (StringUtils.isNotBlank(param.getPhoto())) {
            FileInduceParam fileInduceParam = new FileInduceParam();
            fileInduceParam.setSceneCode(SafetyEducationFileSceneCodeEnum.SAFETY_EDUCATION_CULTIVATE_PHOTO.getCode());
            fileInduceParam.setFileIdStr(param.getPhoto());
            fileInduceParam.setBusinessKey(copying.getId());
            fileOperateClient.fileInduceSingleBody(fileInduceParam);
        }
        return copying.getId();
    }

    @Override
    public Pagination<SafetyCultivateVO> safetyCultivateList(SafetyCultivateQueryParam param) {
        Page<SafetyCultivate> page = this.lambdaQuery()
                .eq(ObjectUtils.isNotNull(param.getObjId()), SafetyCultivate::getObjId, param.getObjId())
                .between(ObjectUtils.isNotNull(param.getPeriod()), SafetyCultivate::getCreateTime,
                         ObjectUtils.ifNull(param.getPeriod(), Period::getBegin),
                         ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                .orderByDesc(SafetyCultivate::getCultivateDate)
                .page(PagingUtils.convert(param));
        List<SafetyCultivateVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            vos = ObjectUtils.copying(page.getRecords(), SafetyCultivateVO.class);
            vos.forEach(vo -> {
                String photo = vo.getPhoto();
                if (StringUtils.isNotBlank(photo)) {
                    List<Long> fileIds = new ArrayList<>();
                    Arrays.stream(photo.split(",")).mapToLong(Long::parseLong).forEach(fileIds::add);
                    vo.setPhotoFiles(fileClient.getByIds(fileIds));
                }
                UserDTO userDTO;
                if (StringUtils.isNotBlank(vo.getCreatorId()) && ActorTypeEnum.USER.name().equals(vo.getCreatorType())) {
                    userDTO = userClient.getUser(Long.valueOf(vo.getCreatorId()));
                } else {
                    userDTO = userClient.getUserByUserName(vo.getCreateBy());
                }
                vo.setCreateBy(userDTO != null ? userDTO.getName() : null);
            });
        }
        return PagingUtils.convert(page, vos);
    }

    @Override
    public SafetyCultivateStatisticVO safetyCultivateStatistics(SafetyCultivateQueryParam param) {
        long total = this.lambdaQuery().eq(ObjectUtils.isNotNull(param.getObjId()), SafetyCultivate::getObjId, param.getObjId())
                .between(ObjectUtils.isNotNull(param.getPeriod()), SafetyCultivate::getCreateTime,
                         ObjectUtils.ifNull(param.getPeriod(), Period::getBegin),
                         ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                .count();
        return SafetyCultivateStatisticVO.builder().recordNum(total).build();
    }
}
