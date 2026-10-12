-- liquibase formatted sql


-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_key_place' AND column_name='region') AS column_exists;
ALTER TABLE disinfection_key_place ADD region varchar(200) NULL COMMENT '区';
ALTER TABLE disinfection_key_place CHANGE region region varchar(200) NULL COMMENT '区' AFTER place_type_name;

-- changeset chx:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_key_place' AND column_name='city') AS column_exists;
ALTER TABLE disinfection_key_place ADD city varchar(200) NULL COMMENT '市';
ALTER TABLE disinfection_key_place CHANGE city city varchar(200) NULL COMMENT '市' AFTER place_type_name;

-- changeset chx:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_key_place' AND column_name='province') AS column_exists;
ALTER TABLE disinfection_key_place ADD province varchar(200) NULL COMMENT '省';
ALTER TABLE disinfection_key_place CHANGE province province varchar(200) NULL COMMENT '省' AFTER place_type_name;








