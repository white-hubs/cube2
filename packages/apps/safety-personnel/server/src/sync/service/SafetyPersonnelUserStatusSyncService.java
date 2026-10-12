package com.guhecloud.cube.srv.gen.safety.personnel.server.sync.service;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.message.UserUpdateMessage;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.EmployeeCert;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.EmployeeCertService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain.CommitteeMember;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.service.CommitteeMemberService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.CertStatusEnum;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.PersonStatusEnum;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.SafetyPersonnelOuaaFacadeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.model.OuaaUserSnapshot;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.domain.OrgNodeMember;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.service.OrgNodeMemberService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.domain.Person;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.domain.PersonTypeRel;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.service.PersonService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.service.PersonTypeRelService;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * OUAA用户与人事状态同步服务。
 */
@Slf4j
@Service("safetyPersonnelUserStatusSyncService")
@RequiredArgsConstructor
public class SafetyPersonnelUserStatusSyncService {

    private final SafetyPersonnelOuaaFacadeService ouaaFacade;
    private final PersonService personService;
    private final PersonTypeRelService personTypeRelService;
    private final EmployeeCertService employeeCertService;
    private final CommitteeMemberService committeeMemberService;
    private final OrgNodeMemberService orgNodeMemberService;

    /**
     * 刷新OUAA用户快照，并处理启用/禁用联动。
     *
     * @param message OUAA用户更新消息
     */
    @Transactional
    public void handleOuaaUserUpdate(UserUpdateMessage message) {
        if (message == null || message.getId() == null) {
            return;
        }
        UserDTO user = ouaaFacade.getUser(message.getId());
        refreshSnapshot(message, user);
        syncEnabledStatus(message.getId(), message.getEnabled());
    }

    /**
     * 标记OUAA用户已离职。
     *
     * @param userId OUAA用户ID
     */
    @Transactional
    public void markLeft(Long userId) {
        if (userId == null) {
            return;
        }
        personService.lambdaUpdate()
                .eq(Person::getUserId, userId)
                .set(Person::getStatus, PersonStatusEnum.LEFT.getCode())
                .update();
        committeeMemberService.lambdaUpdate()
                .eq(CommitteeMember::getUserId, userId)
                .set(CommitteeMember::getActive, Boolean.FALSE)
                .update();
        employeeCertService.lambdaUpdate()
                .eq(EmployeeCert::getUserId, userId)
                .set(EmployeeCert::getCertStatus, CertStatusEnum.LEFT.getCode())
                .update();
        softDeleteOrgNodeMembers(userId);
    }

    /**
     * 标记OUAA用户已删除。
     *
     * @param userId OUAA用户ID
     */
    @Transactional
    public void markDeleted(Long userId) {
        if (userId == null) {
            return;
        }
        Person person = personService.lambdaQuery().eq(Person::getUserId, userId).last("limit 1").one();
        if (person != null) {
            softDeletePersonTypeRels(person.getId());
            DatabaseContextHolder.softDelete(personService, person);
        }
        committeeMemberService.lambdaUpdate()
                .eq(CommitteeMember::getUserId, userId)
                .set(CommitteeMember::getActive, Boolean.FALSE)
                .update();
        employeeCertService.lambdaUpdate()
                .eq(EmployeeCert::getUserId, userId)
                .set(EmployeeCert::getCertStatus, CertStatusEnum.DISABLED.getCode())
                .update();
        softDeleteOrgNodeMembers(userId);
    }

    private void refreshSnapshot(UserUpdateMessage message, UserDTO user) {
        Long userId = message.getId();
        OuaaUserSnapshot snapshot = user == null
                ? OuaaUserSnapshot.builder()
                .userName(message.getName())
                .phone(message.getMobile())
                .companyName(ouaaFacade.resolveCurrentTenantCompanyName())
                .build()
                : ouaaFacade.buildSnapshot(user);

        personService.lambdaUpdate()
                .eq(Person::getUserId, userId)
                .set(snapshot.getUserName() != null, Person::getUserName, snapshot.getUserName())
                .set(snapshot.getPhone() != null, Person::getPhone, snapshot.getPhone())
                .set(user != null, Person::getDeptName, snapshot.getDeptName())
                .set(user != null, Person::getPostName, snapshot.getPostName())
                .set(snapshot.getCompanyName() != null, Person::getCompanyName, snapshot.getCompanyName())
                .update();
        committeeMemberService.lambdaUpdate()
                .eq(CommitteeMember::getUserId, userId)
                .set(snapshot.getUserName() != null, CommitteeMember::getUserName, snapshot.getUserName())
                .set(snapshot.getPhone() != null, CommitteeMember::getPhone, snapshot.getPhone())
                .set(user != null, CommitteeMember::getDeptName, snapshot.getDeptName())
                .set(user != null, CommitteeMember::getPostName, snapshot.getPostName())
                .update();
        orgNodeMemberService.lambdaUpdate()
                .eq(OrgNodeMember::getUserId, userId)
                .set(snapshot.getUserName() != null, OrgNodeMember::getUserName, snapshot.getUserName())
                .set(snapshot.getPhone() != null, OrgNodeMember::getMobile, snapshot.getPhone())
                .set(user != null, OrgNodeMember::getDeptName, snapshot.getDeptName())
                .update();
        employeeCertService.lambdaUpdate()
                .eq(EmployeeCert::getUserId, userId)
                .set(snapshot.getUserName() != null, EmployeeCert::getUserName, snapshot.getUserName())
                .update();
    }

    private void syncEnabledStatus(Long userId, Boolean enabled) {
        if (enabled == null) {
            return;
        }
        if (Boolean.TRUE.equals(enabled)) {
            personService.lambdaUpdate()
                    .eq(Person::getUserId, userId)
                    .eq(Person::getStatus, PersonStatusEnum.DISABLED.getCode())
                    .set(Person::getStatus, PersonStatusEnum.ACTIVE.getCode())
                    .update();
            employeeCertService.lambdaUpdate()
                    .eq(EmployeeCert::getUserId, userId)
                    .eq(EmployeeCert::getCertStatus, CertStatusEnum.DISABLED.getCode())
                    .set(EmployeeCert::getCertStatus, CertStatusEnum.VALID.getCode())
                    .update();
            if (!hasLeftPerson(userId)) {
                committeeMemberService.lambdaUpdate()
                        .eq(CommitteeMember::getUserId, userId)
                        .set(CommitteeMember::getActive, Boolean.TRUE)
                        .update();
            }
            return;
        }

        personService.lambdaUpdate()
                .eq(Person::getUserId, userId)
                .ne(Person::getStatus, PersonStatusEnum.LEFT.getCode())
                .set(Person::getStatus, PersonStatusEnum.DISABLED.getCode())
                .update();
        committeeMemberService.lambdaUpdate()
                .eq(CommitteeMember::getUserId, userId)
                .set(CommitteeMember::getActive, Boolean.FALSE)
                .update();
        employeeCertService.lambdaUpdate()
                .eq(EmployeeCert::getUserId, userId)
                .ne(EmployeeCert::getCertStatus, CertStatusEnum.LEFT.getCode())
                .set(EmployeeCert::getCertStatus, CertStatusEnum.DISABLED.getCode())
                .update();
    }

    private boolean hasLeftPerson(Long userId) {
        return personService.lambdaQuery()
                .eq(Person::getUserId, userId)
                .eq(Person::getStatus, PersonStatusEnum.LEFT.getCode())
                .exists();
    }

    private void softDeleteOrgNodeMembers(Long userId) {
        List<OrgNodeMember> members = orgNodeMemberService.lambdaQuery()
                .eq(OrgNodeMember::getUserId, userId)
                .list();
        DatabaseContextHolder.softDelete(orgNodeMemberService, members);
    }

    private void softDeletePersonTypeRels(Long personId) {
        List<PersonTypeRel> rels = personTypeRelService.lambdaQuery()
                .eq(PersonTypeRel::getPersonId, personId)
                .list();
        DatabaseContextHolder.softDelete(personTypeRelService, rels);
    }
}
