package com.guhecloud.cube.srv.gen.safety.personnel.server.person.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.EmployeeCertService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo.EmployeeCertVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.PersonStatusEnum;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.PersonTypeCodeEnum;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.SafetyPersonnelOuaaFacadeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.model.OuaaUserSnapshot;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.domain.Person;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.domain.PersonTypeRel;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.mapper.PersonMapper;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.param.PersonLinkParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.param.PersonPoolQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.param.PersonQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.service.PersonService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.service.PersonTypeRelService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.PersonExportRowVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.PersonPoolUserVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.PersonProfileVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.PersonVO;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 安全人员台账服务实现。
 */
@Slf4j
@Service("safetyPersonnelPersonService")
@RequiredArgsConstructor
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements PersonService {

    private final PersonTypeRelService personTypeRelService;
    private final EmployeeCertService employeeCertService;
    private final SafetyPersonnelOuaaFacadeService ouaaFacade;

    @Override
    public Pagination<PersonVO> pagePersons(PersonQueryParam param) {
        List<Long> personIds = resolvePersonIdsByType(param.getTypeCode());
        if (personIds != null && personIds.isEmpty()) {
            return Pagination.empty();
        }
        if (StringUtils.isNotBlank(param.getStatus())) {
            PersonStatusEnum.normalize(param.getStatus());
        }
        Page<Person> page = lambdaQuery()
                .in(personIds != null, Person::getId, personIds)
                .and(StringUtils.isNotBlank(param.getKeyword()), wrapper -> wrapper
                        .like(Person::getUserName, param.getKeyword())
                        .or()
                        .like(Person::getPhone, param.getKeyword())
                        .or()
                        .like(Person::getDeptName, param.getKeyword()))
                .eq(StringUtils.isNotBlank(param.getStatus()), Person::getStatus, param.getStatus())
                .orderByDesc(Person::getLinkedAt)
                .page(PagingUtils.convert(param));
        List<PersonVO> records = page.getRecords().stream()
                .map(person -> PersonVO.of(person, listTypeCodes(person.getId())))
                .toList();
        return PagingUtils.convert(page, records);
    }

    @Override
    public void exportPersons(PersonQueryParam param, HttpServletResponse response) throws IOException {
        List<Long> personIds = resolvePersonIdsByType(param.getTypeCode());
        if (personIds != null && personIds.isEmpty()) {
            writePersonExport(response, param.getTypeCode(), List.of());
            return;
        }
        if (StringUtils.isNotBlank(param.getStatus())) {
            PersonStatusEnum.normalize(param.getStatus());
        }
        List<Person> persons = lambdaQuery()
                .in(personIds != null, Person::getId, personIds)
                .and(StringUtils.isNotBlank(param.getKeyword()), wrapper -> wrapper
                        .like(Person::getUserName, param.getKeyword())
                        .or()
                        .like(Person::getPhone, param.getKeyword())
                        .or()
                        .like(Person::getDeptName, param.getKeyword()))
                .eq(StringUtils.isNotBlank(param.getStatus()), Person::getStatus, param.getStatus())
                .orderByDesc(Person::getLinkedAt)
                .list();
        List<PersonExportRowVO> rows = persons.stream().map(this::toExportRow).toList();
        writePersonExport(response, param.getTypeCode(), rows);
    }

    @Override
    public Pagination<PersonPoolUserVO> pagePool(PersonPoolQueryParam param) {
        if (StringUtils.isNotBlank(param.getTypeCode())) {
            PersonTypeCodeEnum.normalizeForQuery(param.getTypeCode());
        }
        UserQueryParam query = new UserQueryParam();
        query.setPageNumber(param.getPageNumber());
        query.setPageSize(param.getPageSize());
        query.setKeyword(param.getKeyword());
        query.setOrganizationId(param.getOrganizationId());
        query.setEnabled(param.getEnabled());
        query.setFilterUserIds(listLinkedUserIds(param.getTypeCode()));
        Pagination<UserDTO> page = ouaaFacade.pageUsers(query);
        List<PersonPoolUserVO> records = page.getRecords().stream().map(PersonPoolUserVO::of).toList();
        return Pagination.<PersonPoolUserVO>builder().total(page.getTotal()).pages(page.getPages()).records(records).build();
    }

    @Override
    @Transactional
    public void link(PersonLinkParam param) {
        String typeCode = PersonTypeCodeEnum.normalizeForPersist(param.getTypeCode());
        List<Long> userIds = param.getUserIds().stream().filter(Objects::nonNull).distinct().toList();
        List<UserDTO> users = ouaaFacade.listUsersByIds(userIds);
        Map<Long, UserDTO> userMap = users.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity(), (left, right) -> left));
        Assert.isNotFound(userMap.size() != userIds.size(), "存在无效用户");
        for (Long userId : userIds) {
            Person person = lambdaQuery().eq(Person::getUserId, userId).last("limit 1").one();
            if (person == null) {
                person = createPerson(userMap.get(userId));
                save(person);
            } else {
                applySnapshot(person, ouaaFacade.buildSnapshot(userMap.get(userId)));
                updateById(person);
            }
            saveTypeRel(person.getId(), typeCode);
        }
    }

    @Override
    @Transactional
    public void removePerson(Long id) {
        Person person = getRequired(id);
        softDeleteTypeRels(person.getId());
        DatabaseContextHolder.softDelete(this, person);
    }

    @Override
    public PersonProfileVO profile(Long id) {
        Person person = getRequired(id);
        return PersonProfileVO.of(person, listTypeCodes(id));
    }

    @Override
    public List<EmployeeCertVO> certificates(Long id) {
        Person person = getRequired(id);
        return employeeCertService.listByUserId(person.getUserId());
    }

    private Person createPerson(UserDTO user) {
        Person person = new Person();
        person.setId(IdWorker.getId());
        person.setUserId(user.getId());
        person.setStatus(PersonStatusEnum.ACTIVE.getCode());
        person.setLinkedAt(LocalDateTime.now());
        applySnapshot(person, ouaaFacade.buildSnapshot(user));
        return person;
    }

    private void applySnapshot(Person person, OuaaUserSnapshot snapshot) {
        if (snapshot == null) {
            return;
        }
        person.setUserName(snapshot.getUserName());
        person.setPhone(snapshot.getPhone());
        person.setDeptName(snapshot.getDeptName());
        person.setPostName(snapshot.getPostName());
        person.setCompanyName(snapshot.getCompanyName());
    }

    private PersonExportRowVO toExportRow(Person person) {
        PersonExportRowVO row = new PersonExportRowVO();
        row.setUserName(person.getUserName());
        row.setPhone(person.getPhone());
        row.setCompanyName(person.getCompanyName());
        row.setDeptName(person.getDeptName());
        row.setPostName(person.getPostName());
        row.setStatusLabel(PersonStatusEnum.getDescription(person.getStatus()));
        row.setTypeLabels(formatTypeLabels(listTypeCodes(person.getId())));
        row.setLinkedAt(person.getLinkedAt());
        return row;
    }

    private String formatTypeLabels(List<String> typeCodes) {
        return typeCodes.stream()
                .map(code -> Arrays.stream(PersonTypeCodeEnum.values())
                        .filter(item -> item.getCode().equals(code))
                        .findFirst()
                        .map(PersonTypeCodeEnum::getDescription)
                        .orElse(code))
                .collect(Collectors.joining("，"));
    }

    private void writePersonExport(HttpServletResponse response, String typeCode, List<PersonExportRowVO> rows) throws IOException {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String sheetName = StringUtils.isBlank(typeCode) ? "安全人员" : typeCode + "安全人员";
            String fileName = URLEncoder.encode(sheetName, StandardCharsets.UTF_8).replace("+", "%20");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ExcelTypeEnum.XLSX.getValue());
            EasyExcel.write(response.getOutputStream(), PersonExportRowVO.class)
                    .sheet("人员台账")
                    .doWrite(rows);
        } catch (IOException e) {
            log.error("导出安全人员台账失败，typeCode={}", typeCode, e);
            throw new ServiceException("人员台账导出失败");
        }
    }

    private List<Long> resolvePersonIdsByType(String typeCode) {
        if (StringUtils.isBlank(typeCode) || PersonTypeCodeEnum.isAll(typeCode)) {
            return null;
        }
        PersonTypeCodeEnum.normalizeForQuery(typeCode);
        List<Long> personIds = personTypeRelService.lambdaQuery()
                .eq(PersonTypeRel::getTypeCode, typeCode)
                .list()
                .stream()
                .map(PersonTypeRel::getPersonId)
                .toList();
        return personIds;
    }

    private void saveTypeRel(Long personId, String typeCode) {
        boolean exists = personTypeRelService.lambdaQuery()
                .eq(PersonTypeRel::getPersonId, personId)
                .eq(PersonTypeRel::getTypeCode, typeCode)
                .exists();
        if (exists) {
            return;
        }
        PersonTypeRel rel = new PersonTypeRel();
        rel.setId(IdWorker.getId());
        rel.setPersonId(personId);
        rel.setTypeCode(typeCode);
        personTypeRelService.save(rel);
    }

    private void softDeleteTypeRels(Long personId) {
        List<PersonTypeRel> rels = personTypeRelService.lambdaQuery()
                .eq(PersonTypeRel::getPersonId, personId)
                .list();
        DatabaseContextHolder.softDelete(personTypeRelService, rels);
    }

    private Person getRequired(Long id) {
        Person person = getById(id);
        Assert.isNotFound(person, "安全人员不存在");
        return person;
    }

    private List<String> listTypeCodes(Long personId) {
        return personTypeRelService.lambdaQuery()
                .eq(PersonTypeRel::getPersonId, personId)
                .list()
                .stream()
                .map(PersonTypeRel::getTypeCode)
                .toList();
    }

    private List<Long> listLinkedUserIds(String typeCode) {
        List<Long> personIds = null;
        if (StringUtils.isNotBlank(typeCode) && !PersonTypeCodeEnum.isAll(typeCode)) {
            PersonTypeCodeEnum.normalizeForQuery(typeCode);
            personIds = personTypeRelService.lambdaQuery()
                    .eq(PersonTypeRel::getTypeCode, typeCode)
                    .list()
                    .stream()
                    .map(PersonTypeRel::getPersonId)
                    .toList();
            if (personIds.isEmpty()) {
                return List.of();
            }
        }
        return lambdaQuery()
                .in(personIds != null, Person::getId, personIds)
                .list()
                .stream()
                .map(Person::getUserId)
                .toList();
    }
}
