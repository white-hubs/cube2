package com.guhecloud.cube.srv.gen.maintain.server.electron.converts;

import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.NfcDetails;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolLocation;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.StrUtils;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolLocationVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/29 15:25
 * @Description:
 */
public final class PatrolLocationConvertToVO {
    private PatrolLocationConvertToVO() {
    }

    public static List<PatrolLocationVO> toPatrolLocationVO(List<PatrolLocation> patrolLocationList, List<AreaDTO> areaList,
                                                            List<NfcDetails> nfcDetailsList
                                                           ) {
        List<PatrolLocationVO> patrolLocationVOList = new ArrayList<>();
        for (var patrolLocation : patrolLocationList) {
            var patrolLocationVO = new PatrolLocationVO();
            patrolLocationVO.setId(patrolLocation.getId());
            patrolLocationVO.setDeviceId(patrolLocation.getDeviceId());
            patrolLocationVO.setPatrolLocationName(patrolLocation.getPatrolLocationName());
            patrolLocationVO.setNfcCode(patrolLocation.getNfcCode());
            patrolLocationVO.setAreaId(patrolLocation.getAreaId());
            patrolLocationVO.setDeviceId(patrolLocation.getDeviceId());
            patrolLocationVO.setBimCode(patrolLocation.getBimCode());
            patrolLocationVO.setRemarks(patrolLocation.getRemarks());
            patrolLocationVO.setPatrolLongitude(patrolLocation.getPatrolLongitude());
            patrolLocationVO.setPatrolDimension(patrolLocation.getPatrolDimension());
            patrolLocationVO.setCreateTime(patrolLocation.getCreateTime());
            patrolLocationVO.setBluetoothCode(patrolLocation.getBluetoothCode());
            if (StringUtils.isNotBlank(patrolLocationVO.getAreaId())) {
                patrolLocationVO.setArea(areaList.stream().filter(ex -> StrUtils.StrToList(patrolLocationVO.getAreaId())
                        .contains(ex.getId())).collect(Collectors.toList()));
            }
            var nfcDetails = nfcDetailsList.stream().filter(ex -> ex.getId()
                    .equals(patrolLocationVO.getDeviceId())).findFirst().orElse(null);
            if (ObjectUtils.isNotEmpty(nfcDetails)) {
                patrolLocationVO.setDeviceCode(nfcDetails.getDeviceCode());
                patrolLocationVO.setNfcCode(nfcDetails.getNfcCode());
                patrolLocationVO.setBluetoothCode(nfcDetails.getBluetoothCode());
            }
            patrolLocationVOList.add(patrolLocationVO);
        }
        return patrolLocationVOList;
    }
}
