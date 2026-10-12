package com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.command.ChangeStaffCommand;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.command.SaveToBePerfectCommand;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.command.SubmitStaffCommand;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.tool.ExcelBeanUtil;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/08
 * @description
 */
public interface StaffCommandService extends IService<StaffPO> {

    /**
     * 保存或修改员工信息
     *
     * @param submitStaffCommand 提交数据
     *
     * @return
     */
    Long saveOrUpdate(SubmitStaffCommand submitStaffCommand);

    /**
     * 保存资料待完善的员工
     *
     * @param saveToBePerfectCommand
     *
     * @return
     */
    Long saveToBePerfect(SaveToBePerfectCommand saveToBePerfectCommand);

    /**
     * @param changeStaffCommand
     */
    void changeStaffByUser(ChangeStaffCommand changeStaffCommand);

    List<ExcelBeanUtil.ExcelMsg> importTemplate(MultipartFile file);

    /**
     * 更新员工部门
     *
     * @param staffId        员工ID
     * @param organizationId 部门ID
     */
    void updateStaffOrganization(Long staffId, Long organizationId);

    /**
     * 更新员工离职
     *
     * @param staffId         员工ID
     * @param leaveOfficeTime 离职时间
     */
    void updateStaffLeaveOffice(Long staffId, LocalDateTime leaveOfficeTime);

    /**
     * 删除员工
     *
     * @param staffId 员工ID
     */
    void remove(Long staffId);

}
