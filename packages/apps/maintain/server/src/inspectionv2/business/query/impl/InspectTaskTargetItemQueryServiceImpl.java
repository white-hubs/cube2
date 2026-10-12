package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectTaskTargetItemQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTaskTargetItemMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskTargetItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectResultVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionItemVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionStandardVO;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectTaskTargetItemQueryServiceImpl extends ServiceImpl<InspectTaskTargetItemMapper, InspectTaskTargetItemPO> implements InspectTaskTargetItemQueryService {
    /**
     * 获取任务对象的巡检内容
     *
     * @param taskId 任务id
     * @param objId  对象id
     *
     * @return 巡检内容
     */
    @Override
    public List<InspectionStandardVO> getObjStandardList(Long taskId, Long objId) {
        List<InspectionStandardVO> standardList = new ArrayList<>();
        List<InspectTaskTargetItemPO> itemPOList = this.list(Wrappers.<InspectTaskTargetItemPO>lambdaQuery()
                                                                     .eq(InspectTaskTargetItemPO::getTaskId, taskId)
                                                                     .eq(InspectTaskTargetItemPO::getTargetId, objId)
                                                            );
        if (CollectionUtils.isEmpty(itemPOList)) {
            return standardList;
        }
        itemPOList.stream().collect(Collectors.groupingBy(InspectTaskTargetItemPO::getStandardId)).forEach((standardId, list) -> {
            InspectTaskTargetItemPO tempPO = list.get(0);
            InspectionStandardVO standardVO = new InspectionStandardVO();
            standardVO.setId(tempPO.getStandardId());
            standardVO.setName(tempPO.getStandardName());
            standardVO.setCode(tempPO.getStandardCode());
            List<InspectionItemVO> itemList = new ArrayList<>();
            list.forEach(po -> {
                InspectionItemVO item = new InspectionItemVO();
                item.setId(po.getItemId());
                item.setName(po.getItemName());
                item.setItemTag(po.getItemTag());
                item.setResultItemValue(po.getResultItemValue());
                item.setRemarkResult(po.getRemarkResult());
                item.setImageResult(po.getImageResult());
                String resultText = po.getResultText();
                if (StringUtils.isNotBlank(resultText)) {
                    item.setResultVO(JSON.parseObject(resultText, InspectResultVO.class));
                }
                itemList.add(item);

            });
            standardVO.setStandardItems(itemList);
            standardList.add(standardVO);
        });
        return standardList;
    }
}
