package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao.DisinfectionKeyPlaceMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao.DisinfectionTaskExecuteRecordMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionKeyPlace;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionTaskExecuteRecord;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.insert.TaskCompletionRecordsParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.ExecuteRecordPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionTaskExecuteRecordService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.KeyPlaceExecuteRecordVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DisinfectionTaskExecuteRecordServiceImpl extends ServiceImpl<DisinfectionTaskExecuteRecordMapper, DisinfectionTaskExecuteRecord>
        implements DisinfectionTaskExecuteRecordService {
    private final DisinfectionKeyPlaceMapper disinfectionKeyPlaceMapper;
    private final UserClient userClient;
    private final FileClient fileClient;

    @Override
    public List<DisinfectionTaskExecuteRecord> listByTaskId(long taskId) {
        return lambdaQuery().eq(DisinfectionTaskExecuteRecord::getTaskId, taskId).list();
    }

    /**
     * 重点场所的执行记录
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<KeyPlaceExecuteRecordVO> KeyPlaceExecuteRecordPage(ExecuteRecordPageParam param) {
        Assert.isParameterError(param.getKeyPlaceId() == null, "场所不能为空！");
        /* 处理用户名条件 */
        List<Long> usreIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(param.getUserName())) {
            List<UserDTO> userDTOList = userClient.listUsers(param.getUserName());
            if (CollectionUtils.isEmpty(userDTOList)) {
                return Pagination.empty();
            }
            userDTOList.forEach(userDTO -> usreIdList.add(userDTO.getId()));
        }
        // 获取分页列表
        Page<DisinfectionTaskExecuteRecord> page = this.page(PagingUtils.convert(param), Wrappers
                                                                     .<DisinfectionTaskExecuteRecord>lambdaQuery().between(param.getPeriod() !=
                                                                                                                           null, DisinfectionTaskExecuteRecord::getCreateTime, ObjectUtils.ifNull(param.getPeriod(), Period::getBegin), ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                                                                     .eq(Boolean.TRUE.equals(param.getCompleted()), DisinfectionTaskExecuteRecord::getProcess, new BigDecimal("100"))
                                                                     .in(CollectionUtils.isNotEmpty(usreIdList), DisinfectionTaskExecuteRecord::getExecutorId, usreIdList)
                                                                     .like(param.getKeyPlaceId() != null, DisinfectionTaskExecuteRecord::getKeyPlaceRecord, "\"" + param.getKeyPlaceId() + "\"")
                                                            );
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        // 获取重点场所信息
        DisinfectionKeyPlace keyPlace = disinfectionKeyPlaceMapper.selectById(param.getKeyPlaceId());
        if (keyPlace == null) {
            keyPlace = new DisinfectionKeyPlace();
        }
        /* 遍历获取人员和文件id */
        List<Long> fileIdList = new ArrayList<>();
        List<Long> userIdList = new ArrayList<>();
        List<KeyPlaceExecuteRecordVO> recordList = new ArrayList<>();
        DisinfectionKeyPlace finalKeyPlace = keyPlace;
        page.getRecords().forEach(record -> {
            KeyPlaceExecuteRecordVO result = this.ExecuteRecordToPlaceExecuteRecordVO(record, finalKeyPlace);
            // 获取执行人id
            userIdList.add(record.getExecutorId());
            // 获取文件id
            handlerExecuteRecordKeyPlaceFileId(record, fileIdList, result);

            recordList.add(result);
        });
        // 查询文件和人员信息
        Map<Long, FileVO> fileMap = this.getFileMap(fileIdList);
        Map<Long, UserDTO> userMap = this.getUserMap(userIdList);
        /* 设值 */
        recordList.forEach(record -> {
            UserDTO userDTO = userMap.get(record.getExecutorId());
            record.setExecutorName(userDTO == null ? "" : userDTO.getName());
            if (CollectionUtils.isNotEmpty(record.getFileIdList())) {
                List<FileVO> fileVOList = new ArrayList<>();
                record.getFileIdList().forEach(fileId -> {
                    fileVOList.add(fileMap.get(fileId));
                });
                record.setFileVOList(fileVOList);
            }
        });
        return PagingUtils.convert(page, recordList);
    }

    /**
     * 获取重点场所已完成执行记录条数
     *
     * @param keyPlaceId 重点场所id
     *
     * @return 总条数
     */
    @Override
    public Integer keyPlaceExecuteCount(Long keyPlaceId) {
        return (int) this.count(Wrappers.<DisinfectionTaskExecuteRecord>lambdaQuery()
                                        .eq(DisinfectionTaskExecuteRecord::getProcess, new BigDecimal("100"))
                                        .like(DisinfectionTaskExecuteRecord::getKeyPlaceRecord, "\"" + keyPlaceId + "\""));
    }

    private Map<Long, FileVO> getFileMap(List<Long> fileIdList) {
        Map<Long, FileVO> fileMap = new HashMap<>();
        if (CollectionUtils.isEmpty(fileIdList)) {
            return fileMap;
        }
        List<FileVO> fileVOList = fileClient.getByIds(fileIdList);
        if (CollectionUtils.isEmpty(fileVOList)) {
            return fileMap;
        }
        fileVOList.forEach(fileVO -> fileMap.put(fileVO.getId(), fileVO));
        return fileMap;
    }

    private Map<Long, UserDTO> getUserMap(List<Long> userIdList) {
        Map<Long, UserDTO> userMap = new HashMap<>();
        if (CollectionUtils.isEmpty(userIdList)) {
            return userMap;
        }
        UserQuery userQuery = new UserQuery();
        userQuery.setUserIds(new HashSet<>(userIdList));
        List<UserDTO> userDTOList = userClient.postList(new UserQuery());
        if (CollectionUtils.isEmpty(userDTOList)) {
            return userMap;
        }

        userDTOList.forEach(u -> userMap.put(u.getId(), u));
        return userMap;
    }

    /**
     * 执行记录转重点场所记录VO
     *
     * @param record   记录
     * @param keyPlace 重点场所
     *
     * @return 重点场所执行记录VO
     */
    private KeyPlaceExecuteRecordVO ExecuteRecordToPlaceExecuteRecordVO(DisinfectionTaskExecuteRecord record,
                                                                        DisinfectionKeyPlace keyPlace
                                                                       ) {
        KeyPlaceExecuteRecordVO result = new KeyPlaceExecuteRecordVO();
        ObjectUtils.copying(record, result);
        result.setKeyPlaceId(keyPlace.getId());
        result.setPlaceName(keyPlace.getPlaceName());
        result.setPlaceTypeId(keyPlace.getPlaceTypeId());
        result.setPlaceTypeName(keyPlace.getPlaceTypeName());
        result.setPlaceAddress(keyPlace.getPlaceAddress());
        return result;
    }

    /**
     * 处理执行记录的重点场所文件id
     */
    private void handlerExecuteRecordKeyPlaceFileId(DisinfectionTaskExecuteRecord record, List<Long> fileIdList,
                                                    KeyPlaceExecuteRecordVO result
                                                   ) {
        if (StringUtils.isNotBlank(record.getKeyPlaceRecord())) {
            List<TaskCompletionRecordsParam.PlaceRecordParam> placeRecordList = JsonUtils.deserialize(record.getKeyPlaceRecord(), new TypeReference<List<TaskCompletionRecordsParam.PlaceRecordParam>>() {});
            if (placeRecordList != null) {
                placeRecordList.forEach(placeRecordParam -> {
                                            if (StringUtils.isNotBlank(placeRecordParam.getAppendix())) {
                                                List<Long> files = new ArrayList<>();
                                                for (String s : placeRecordParam.getAppendix().split(",")) {
                                                    files.add(Long.parseLong(s));
                                                }
                                                result.setFileIdList(files);
                                                fileIdList.addAll(files);
                                            }

                                        }
                                       );
            }
        }
    }
}