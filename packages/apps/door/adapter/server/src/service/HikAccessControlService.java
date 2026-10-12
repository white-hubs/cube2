package com.guhecloud.cube.srv.gen.adapter.access.control.server.service;

import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.KSDJRecordParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.TKRecordParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.VisitingRecordParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AccessRecordVO;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.CardVO;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.OrgVO;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.VisitingRecordVO;

import java.util.Collections;
import java.util.List;

public interface HikAccessControlService extends AccessService {
    /**
     * 卡信息
     *
     * @return
     */
    default List<CardVO> getDoorCard() {
        return Collections.emptyList();
    }

    /**
     * 组织信息
     *
     * @return
     */
    default List<OrgVO> getOrg() {
        return Collections.emptyList();
    }

    /**
     * 来访记录
     *
     * @return
     */
    default List<VisitingRecordVO> getVisitingRecord(VisitingRecordParam param) {
        return Collections.emptyList();
    }

    /**
     * 梯控记录
     *
     * @return
     */
    default List<AccessRecordVO> getTKRecord(TKRecordParam param) {
        return Collections.emptyList();
    }

    /**
     * 可视对讲记录
     *
     * @return
     */
    default List<AccessRecordVO> getKSDJRecord(KSDJRecordParam param) {
        return Collections.emptyList();
    }
}
