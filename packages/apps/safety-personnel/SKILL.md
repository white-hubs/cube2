---
name: cube2-app-safety-personnel
description: 安全生产统一智管平台人员管理应用；处理安全人员库、安全生产委员会、证书管理、组织配置首页展示和安全履历聚合。Use when working under packages/apps/safety-personnel or related PRD/计划 questions.
---

# Safety Personnel

## Boundaries

- This app only treats OUAA `userId` as personnel identity. Personnel HR staff data is used only for leave/delete synchronization and must not become the business identity.
- `sp_org_node` is for the home page organization presentation and configuration only. Business user organization/dept display comes from OUAA redundant fields.
- Do not physically delete records. Use logical delete and include `deleted` in unique indexes so deleted data can be re-added.
- Version 1 does not implement data permission. Keep tenant/login/auth isolation only.
- Safety resume is a read-model aggregation. Do not create a local `sp_safety_record` fact table unless product explicitly changes the scope.

## Integration Notes

- External services (OUAA, file, work-center, conference, message, personnel HR) must be called through `common.facade.SafetyPersonnel*FacadeService` beans, not Feign `*Client` in service/controller layers.
- Work-center provides non-meeting resume records such as patrol, training, approval, and other tasks (`SafetyPersonnelResumeFacadeService`).
- Conference meeting records come from `packages/apps/conference/client` via `SafetyPersonnelResumeFacadeService`.
- Personnel ledger HTTP API is exposed by `SafetyPersonController` (`/persons` paths), not a generic `PersonController` name.
- Message scene code: `SAFETY_PERSONNEL::CERT_WARN`.
- File scenes: `SAFETY_PERSONNEL::COMMITTEE_ATTACHMENT`, `SAFETY_PERSONNEL::CERT_ATTACHMENT`; bucket: `safety-personnel`.
- 附件仅存 `file_id` 关联（证书在 `sp_employee_cert.attachment_file_id`）；展示与下载统一走 `FileClient` 返回的 `FileVO`，不在业务表重复存文件名/大小/类型。安委会附件按 `businessKey=committee_id` 由 file 服务维护，不再写 `sp_committee_attachment`。

## Code Rules

- Spring beans (`@Service`, `@Component`, `@RestController`, `@Bean`) must use explicit names prefixed with `safetyPersonnel` (e.g. `safetyPersonnelOuaaFacade`, `safetyPersonnelHomeController`). Do not rely on default names like `ouaaUserFacadeService` or `homeController` — server-one scans `com.guhecloud` and will collide with other apps.
- Every exposed controller/client method needs a method comment.
- Request parameter classes and response VO/DTO classes need field comments.
- Avoid cross-service direct table joins and MySQL views.
