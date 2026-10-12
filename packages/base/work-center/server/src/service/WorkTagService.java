package com.guhecloud.cube.biz.work.center.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.work.center.server.entity.WorkTag;
import com.guhecloud.cube.biz.work.center.server.entity.WorkTagReferBusinessType;
import com.guhecloud.cube.biz.work.center.server.param.WorkTagModifyParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTagQueryParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTagReferBusinessTypeModifyParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTagUpdateParam;

import java.util.List;
import java.util.Set;

public interface WorkTagService extends IService<WorkTag> {

    Long save(WorkTagModifyParam param);

    Long saveReferBusinessType(String tagCode, WorkTagReferBusinessTypeModifyParam param);

    void update(Long id, WorkTagUpdateParam param);

    List<WorkTag> listWorkTags(WorkTagQueryParam param);

    List<WorkTag> listWorkTagsByCodes(Set<String> workTagCodes);

    List<WorkTagReferBusinessType> listWorkTagReferBusinessTypesByCode(String workTagCode);

    List<WorkTagReferBusinessType> listWorkTagReferBusinessTypesByCodes(Set<String> workTagCodes);

    List<WorkTagReferBusinessType> listWorkTagReferBusinessTypes(Set<String> businessTypes);

    Set<String> listBusinessTypesByCode(String workTagCode);

    void removeById(Long id);

    Set<String> listHandoverBusinessTypes();
}
