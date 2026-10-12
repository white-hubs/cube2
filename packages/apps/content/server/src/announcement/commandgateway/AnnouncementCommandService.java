package com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command.AnnouncementStatusCommand;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command.ModelTypeCommand;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command.SubmitAnnouncementCommand;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo.ModelTypeVo;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.po.Announcement;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.po.ModelType;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/20
 */
public interface AnnouncementCommandService extends IService<Announcement> {

    /**
     * 新增公告
     *
     * @param command 新增参数
     */
    void save(SubmitAnnouncementCommand command);

    /**
     * 公告修改
     *
     * @param command 公告修改
     */

    void updateAnnouncement(Long id, SubmitAnnouncementCommand command);

    /**
     * 删除公告
     *
     * @param id 主键
     */
    void remove(Long id);

    /**
     * 删除公告
     *
     * @param ids 主键
     */
    void removeBath(List<Long> ids);

    /**
     * @Description 类型新增
     */
    void add(ModelTypeCommand command);

    /**
     * @Description 类型修改
     */
    void updateModelType(Long id, ModelTypeCommand command);

    /**
     * @Description 类型删除
     */
    void removeModelType(Long id);

    List<ModelTypeVo> getList(String modelType, String keyword);

    List<ModelType> getLevel(String modelType);

    void updateAnnouncementHideAndTop(List<AnnouncementStatusCommand> listCommand);
}
