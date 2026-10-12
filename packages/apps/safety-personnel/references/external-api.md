# External API Boundaries

## OUAA

OUAA is the source of user identity and organization display information in this app. Use OUAA `userId` as the stable identity key.

## Personnel HR

Personnel HR data is synchronized from OUAA and is only used here for leave/delete status synchronization. Confirm actual message payload fields before depending on `staffId`.

## Work Center

Work-center is the source for non-meeting safety resume records. The first version does not paginate resume records.

## Conference

Conference is the source for meeting resume records. Use `ConferenceResumeClient#listMeetingRecords` from `packages/apps/conference/client`.
