package com.guhecloud.cube.deliver.project.merchant.controller.system;

import cn.hutool.core.util.ObjectUtil;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.BizCodeUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.PasswordUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.system.WeChatStoreUserDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.ProjectUser;
import com.guhecloud.cube.deliver.project.merchant.entity.UserDeliver;
import com.guhecloud.cube.deliver.project.merchant.entity.system.PurchaserDeliver;
import com.guhecloud.cube.deliver.project.merchant.enums.UserTypeEnum;
import com.guhecloud.cube.deliver.project.merchant.service.CompanyUserService;
import com.guhecloud.cube.deliver.project.merchant.service.UserDeliverService;
import com.guhecloud.cube.deliver.project.merchant.service.system.ProjectUserService;
import com.guhecloud.cube.deliver.project.merchant.service.system.PurchaserDeliverService;
import com.guhecloud.cube.deliver.project.merchant.service.system.UserLinkService;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.PinYinUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

/**
 * 微信商户小程序
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeChatStoreController {

    private final PasswordEncoder passwordEncoder;

    private final UserDeliverService userDeliverService;
    private final ProjectUserService projectUserService;
    private final UserLinkService userLinkService;
    private final PurchaserDeliverService purchaserDeliverService;
    private final UserClient userClient;
    private final CompanyUserService companyUserService;

    @GetMapping("/public/wx/getToken")
    public String getToken(@RequestParam("mobile") String mobile) {
        return "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnRzIjpbIjU4MTk3MjYwIiwiNTgxOTcyNjAiLCI1OTk5NjA1MiJdLCJwYXNzd29yZF9leHBpcmVkIjpmYWxzZSwidXNlcl9uYW1lIjoiMTczMTEzNjk3ODIiLCJhZG1pbiI6ZmFsc2UsInBhc3N3b3JkX2ZvcmNlZCI6ZmFsc2UsImF1dGhvcml0aWVzIjpbIjE2NTE4NDY3OTE2OTM0MTQ0MDEiLCJST0xFX0FDVElWSVRJX1VTRVIiLCIxNjAyODQ3ODI2NjM3ODI4MDk3Il0sImNsaWVudF9pZCI6ImNsaWVudF8yIiwicGFzc3dvcmRfZXhwaXJhdGlvbl9yZW1pbmRlZCI6ZmFsc2UsImFkbWluaXN0cmF0ZWRfdGVuYW50cyI6WyI1ODE5NzI2MCIsIjU5OTk2MDUyIl0sInNjb3BlIjpbInNlbGVjdCJdLCJuYW1lIjoi6LaF57qn566h55CG5ZGYIiwicGFzc3dvcmRfZXhwaXJhdGlvbl9kYXRlIjoxNzE1MTYxMzg0MDAwLCJpZCI6MTQ3ODYzNjg4Njk4NDUyMzc3NywiZXhwIjoxNzk0NDEzODUyLCJqdGkiOiI0ZWFmYjFjZi0wNDBjLTQwNjgtYjAwYi02M2RkOTg5MDM2MzAifQ.gy2W3_Q0zgZGBfZlg4joy1GkRhv3_hi_Rvp2QQ1npjV3a6eJAtgJxP8K1_7PHyICemC9Buwg8AYj7jOkZBKtf0KLq_AT3dnAYkhWVyLrZ0TpHJYGhns_YGO1yPSs4oqjNhkOIXasq1_zwMD74CVTqyctVekdfyrAQL0eBCk0q8VZk3f3iQxqiwwCJreC9oye6tW7CclySuMmoSDuPhfUqDXpY_eDAk7hhCofwBLUM57S5g1Dr_IkEmTjV2YvGq_-eLEh1oMoz5TWl15WAyb03-nN40Z5WKrbXxM-SsLfp8PgWZGxx0p9QlWc-bKqafuCnAYcrXYq5RbpVkXZ57xdIA";
    }

    @PostMapping("/public/wx/company/user")
    public String save(@Valid @RequestBody WeChatStoreUserDTO dto) {
        String userId = "";
        if (UserTypeEnum.USER_TYPE_PURCHASER.getCode().equals(dto.getUserType())) {
            PurchaserDeliver one = purchaserDeliverService.lambdaQuery().eq(PurchaserDeliver::getUsername, dto.getMobile()).one();
            if (one != null) {
                one.setNickname(dto.getNickname());
                one.setPinyin(PinYinUtils.getHead(dto.getNickname()));
                purchaserDeliverService.updateById(one);
                return one.getId();
            }
            // 向ouaa插入用户
            UserModifyParam userModifyParam = new UserModifyParam();
            userModifyParam.setName(dto.getNickname());
            userModifyParam.setUsername(dto.getMobile());
            userModifyParam.setMobile(dto.getMobile());
            userModifyParam.setGender(Gender.FEMALE);
            userModifyParam.setEnabled(Boolean.TRUE);
            try {
                userModifyParam.setTenant(SessionContextHolder.getRequester().getTenant());
            } catch (Exception e) {

            }
            UserDTO userDTO = userClient.save(userModifyParam);
            // 采购商注册
            PurchaserDeliver purchaserDeliver = new PurchaserDeliver();
            purchaserDeliver.setId(userDTO.getId().toString());
            purchaserDeliver.setUsername(dto.getMobile());
            purchaserDeliver.setPassword(passwordEncoder.encode(BizCodeUtils.generate("PWD")));
            purchaserDeliver.setEnabled(true);
            purchaserDeliver.setMobile(dto.getMobile());
            purchaserDeliver.setGender(dto.getGender());
            purchaserDeliver.setNickname(dto.getNickname());
            purchaserDeliver.setType(UserTypeEnum.USER_TYPE_PURCHASER.getCode());
            purchaserDeliver.setPinyin(PinYinUtils.getHead(dto.getNickname()));
            purchaserDeliver.setDeleted(false);
            purchaserDeliverService.save(purchaserDeliver);
            userId = purchaserDeliver.getId();
        } else {
            // 供应商注册
            // 向ouaa插入用户
            UserModifyParam userModifyParam = new UserModifyParam();
            userModifyParam.setName(dto.getNickname());
            userModifyParam.setUsername(dto.getMobile());
            userModifyParam.setMobile(dto.getMobile());
            userModifyParam.setGender(Gender.FEMALE);
            userModifyParam.setEnabled(Boolean.TRUE);
            try {
                userModifyParam.setTenant(SessionContextHolder.getRequester().getTenant());
            } catch (Exception e) {

            }
            UserDTO userDTO = userClient.save(userModifyParam);
            // 1. 创建用户，mobile做username
            userId = this.createUser(dto.getMobile(), dto.getNickname(), dto.getGender(), userDTO.getId().toString());
        }
        // 2. 关联项目
        this.saveUserProject(dto.getProjectId(), userId);
        // 3. 关联user_link
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        if (ObjectUtil.isNotEmpty(tenant)) {
            dto.setTenant(tenant);
        }
        this.saveUserLink(userId, dto.getWxopenId(), dto.getTenant());
        //         4. 调用@PostMapping("/companies/user")接口保存商户用户关系，采购商不需调用
        return userId;
    }

    //    @PostMapping("/public/wx/company/employee")
    //        //    public String saveEmployee(@Valid @RequestBody WeChatStoreUserDTO dto) {
    //        // 1. 创建用户，mobile做username
    //        String userId = this.createUser(dto.getMobile(), dto.getNickname(), dto.getGender());
    //        // 2. 关联项目
    //        this.saveUserProject(dto.getProjectId(), userId);
    //        return userId;
    //    }

    private String createUser(String mobile, String nickname, Integer gender, String userId) {
        // 获取当前用户信息
        Principal principal = null;
        try {
            principal = SecurityContextHolder.getPrincipal();
        } catch (Exception e) {

        }
        UserDeliver byUserName = userDeliverService.lambdaQuery().eq(UserDeliver::getUsername, mobile).one();
        UserDeliver byMobile = userDeliverService.lambdaQuery().eq(UserDeliver::getMobile, mobile).one();
        if (byMobile == null && byUserName == null) {
            UserDeliver user = new UserDeliver();
            user.setId(userId);
            user.setUsername(mobile);
            user.setNickname(nickname);
            user.setMobile(mobile);
            user.setGender(gender == null ? 9 : gender);
            user.setType(UserTypeEnum.USER_TYPE_COMPANY.getCode());
            // 默认密码
            user.setPassword(passwordEncoder.encode(PasswordUtils.getRandomPassword(12)));
            user.setPinyin(PinYinUtils.getHead(nickname));
            user.setDeleted(false);
            user.setCreateTime(LocalDateTime.now());
            user.setCreateBy(ObjectUtil.isNotEmpty(principal) ? principal.getName() : null);
            userDeliverService.save(user);
        } else {
            String type1 = (byUserName == null ? "" : byUserName.getType());
            String type2 = (byMobile == null ? "" : byMobile.getType());
            if (!UserTypeEnum.USER_TYPE_COMPANY.getCode().equals(type1) || !UserTypeEnum.USER_TYPE_COMPANY.getCode().equals(type2)) {
                throw new ServiceException("手机号已注册为员工!");
            }
            userId = (byUserName == null ? byMobile.getId() : byUserName.getId());
        }
        return userId;
    }

    private void saveUserProject(String projectId, String userId) {
        ProjectUser saved = projectUserService.lambdaQuery().eq(ProjectUser::getProjectId, projectId).eq(ProjectUser::getUserId, userId)
                .one();
        if (saved == null) {
            ProjectUser projectUser = new ProjectUser();
            projectUser.setProjectId(projectId);
            projectUser.setUserId(userId);
            projectUser.setManager(false);
            projectUserService.save(projectUser);
        }
    }

    private void saveUserLink(String userId, String wxopenId, Integer tenant) {
        userLinkService.saveOrUpdate(userId, wxopenId, "", tenant);
    }

}
