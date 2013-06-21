drop table if exists OS_WFENTRY cascade;
drop table if exists OS_PROPERTYENTRY cascade;
drop table if exists OS_CURRENTSTEP cascade;
drop table if exists OS_HISTORYSTEP cascade;

CREATE TABLE `OS_WFENTRY` (
    `id`  bigint(20) NOT NULL AUTO_INCREMENT ,
    `version`  int(11) NOT NULL ,
    `name`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
    `state`  int(11) NULL DEFAULT NULL ,
    PRIMARY KEY (`id`)
)
ENGINE=InnoDB;


CREATE TABLE `OS_PROPERTYENTRY` (
    `entity_name`  varchar(125) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL ,
    `entity_id`  bigint(20) NOT NULL ,
    `entity_key`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL ,
    `key_type`  int(11) NULL DEFAULT NULL ,
    `boolean_val`  bit(1) NULL DEFAULT NULL ,
    `double_val`  double NULL DEFAULT NULL ,
    `string_val`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
    `long_val`  bigint(20) NULL DEFAULT NULL ,
    `int_val`  int(11) NULL DEFAULT NULL ,
    `date_val`  datetime NULL DEFAULT NULL ,
    PRIMARY KEY (`entity_name`, `entity_id`, `entity_key`)
) ENGINE=InnoDB;


CREATE TABLE `OS_CURRENTSTEP` (
    `id`  bigint(20) NOT NULL AUTO_INCREMENT ,
    `action_Id`  int(11) NULL DEFAULT NULL ,
    `caller`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
    `finish_Date`  datetime NULL DEFAULT NULL ,
    `start_Date`  datetime NULL DEFAULT NULL ,
    `due_Date`  datetime NULL DEFAULT NULL ,
    `owner`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
    `status`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
    `step_Id`  int(11) NULL DEFAULT NULL ,
    `entry_Id`  bigint(20) NULL DEFAULT NULL ,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`entry_Id`) REFERENCES `OS_WFENTRY` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB;


CREATE TABLE `OS_HISTORYSTEP` (
    `id`  bigint(20) NOT NULL AUTO_INCREMENT ,
    `action_Id`  int(11) NULL DEFAULT NULL ,
    `caller`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
    `finish_Date`  datetime NULL DEFAULT NULL ,
    `start_Date`  datetime NULL DEFAULT NULL ,
    `due_Date`  datetime NULL DEFAULT NULL ,
    `owner`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
    `status`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
    `step_Id`  int(11) NULL DEFAULT NULL ,
    `entry_Id`  bigint(20) NULL DEFAULT NULL ,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`entry_Id`) REFERENCES `OS_WFENTRY` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB;
