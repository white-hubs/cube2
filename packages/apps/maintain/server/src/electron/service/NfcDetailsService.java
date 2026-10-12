package com.guhecloud.cube.srv.gen.maintain.server.electron.service;//package com.guhecloud.cube.srv.gen.maintain.electron.server.server;

import com.guhecloud.cube.srv.gen.maintain.server.electron.param.NfcDetailsPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.NfcDetailsParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.NfcDetailsVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface NfcDetailsService {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return nfc信息
     */
    NfcDetailsVO queryById(Long id);

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return 分页数据
     */
    Pagination<NfcDetailsVO> queryNcfPage(NfcDetailsPageParam param);

    /**
     * 查询所有未绑定的芯片
     *
     * @param
     *
     * @return 芯片信息
     */
    List<NfcDetailsVO> queryNcfAll(Long deviceId);

    /**
     * 新增数据
     *
     * @param param nfc信息
     *
     * @return 新增结果
     */
    Boolean insert(NfcDetailsParam param);

    /**
     * 批量插入
     *
     * @param param nfc信息
     */
    Boolean insert(List<NfcDetailsParam> param);

    /**
     * 更新数据
     *
     * @param param nfc信息
     *
     * @return 更新结果
     */
    Boolean update(NfcDetailsParam param);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    Boolean deleteById(Long id);
}
