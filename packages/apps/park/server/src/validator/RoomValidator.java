package com.guhecloud.cube.srv.gen.park.management.server.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.RoomParam;
import com.guhecloud.cube.srv.gen.park.management.server.dao.RoomMapper;
import com.guhecloud.cube.srv.gen.park.management.server.dao.RoomRentRecordMapper;
import com.guhecloud.cube.srv.gen.park.management.server.enumeration.RoomStatusEnum;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomValidator {

    private final RoomMapper mapper;

    private final RoomRentRecordMapper roomRentRecordMapper;

    /**
     * 验证相同名称
     *
     * @param name 名称
     */
    public void verifyIsExistByName(String name) {

        RoomPO po = mapper.selectOne(Wrappers.<RoomPO>lambdaQuery()
                                             .eq(RoomPO::getName, name)
                                    );
        Assert.isConflict(Objects.nonNull(po), "存在相同名称!");
    }

    /**
     * 验证更新操作相同名称
     *
     * @param id   主键
     * @param name 名称
     */
    public void verifyIsExistUpdateByName(Long id, String name, RoomParam param) {
        RoomPO po = mapper.selectOne(Wrappers.<RoomPO>lambdaQuery()
                                             .eq(RoomPO::getName, name)
                                    );
        Assert.isConflict(Objects.nonNull(po) && !Objects.equals(id, po.getId()), "存在相同名称!");
        if (param.getStatus().name().equals(RoomStatusEnum.STOPPING.name())) {
            List<Long> ids = roomRentRecordMapper.getIds(id);
            Assert.isNotAcceptable(!ids.isEmpty(), "已被租赁不能停用");
        }

    }

}
