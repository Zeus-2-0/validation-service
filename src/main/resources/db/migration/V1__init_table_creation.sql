CREATE TABLE IF NOT EXISTS `validationdb`.`payload_tracker` (
  `payload_tracker_sk` VARCHAR(36) NOT NULL,
  `payload_id` VARCHAR(45) NOT NULL,
  `payload_key` VARCHAR(50) NOT NULL COMMENT 'The key for the type of payload, like account number for account payload and zeus transaction control number for transaction payload\n',
  `payload_key_type_code` VARCHAR(45) NOT NULL COMMENT 'Identifies the type of payload like ACCOUNT, TRANSACTION, FILE etc',
  `payload` LONGTEXT NOT NULL COMMENT 'The payload as a string',
  `payload_direction_type_code` VARCHAR(45) NOT NULL COMMENT 'Identifies the direction of the payload',
  `src_dest` VARCHAR(100) NOT NULL COMMENT 'Identifies the source if the payload is inbound and destination if the payload is outbound',
  `created_date` DATETIME NULL COMMENT 'The date when the record was created',
  `updated_date` DATETIME NULL COMMENT 'The date when the record was updated',
  PRIMARY KEY (`payload_tracker_sk`))
ENGINE = InnoDB
COMMENT = 'This table tracks all the inbound and outbound payloads in the validation service';
CREATE TABLE IF NOT EXISTS `validationdb`.`payload_tracker_detail` (
    `payload_tracker_detail_sk` VARCHAR(36) NOT NULL COMMENT 'Primary key of the table',
    `payload_tracker_sk` VARCHAR(36) NOT NULL COMMENT 'The foreign key of the payload tracker table',
    `response_type_code` VARCHAR(45) NOT NULL COMMENT 'The type of response received or sent. e.g. ACK, RESULT etc',
    `response_payload` LONGTEXT NOT NULL,
    `response_payload_id` VARCHAR(45) NULL,
    `payload_direction_type_code` VARCHAR(45) NOT NULL COMMENT 'Identifies the direction of the payload INBOUND or OUTBOUND',
    `src_dest` VARCHAR(100) NOT NULL COMMENT 'Identifies the source of the  payload if direction is inbound and destination if the direction is outbound',
    `created_date` DATETIME NULL COMMENT 'The date when the record was created',
    `updated_date` DATETIME NULL COMMENT 'The date when the record was updated',
    PRIMARY KEY (`payload_tracker_detail_sk`),
    INDEX `payload_tracker_fk_idx` (`payload_tracker_sk` ASC) VISIBLE,
    CONSTRAINT `payload_tracker_fk`
    FOREIGN KEY (`payload_tracker_sk`)
    REFERENCES `validationdb`.`payload_tracker` (`payload_tracker_sk`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    COMMENT = 'The payload tracker detail table, that tracks all the responses received for an outbound payload and all the responses sent for an inbound payload';
CREATE TABLE IF NOT EXISTS `validationdb`.`rule_category` (
    `rule_category_sk` VARCHAR(36) NOT NULL COMMENT 'The primary key of the table',
    `rule_category_id` VARCHAR(50) NOT NULL COMMENT 'The unique Id of the category',
    `rule_category_name` VARCHAR(100) NOT NULL COMMENT 'The name of the category',
    `rule_category_desc` VARCHAR(200) NOT NULL COMMENT 'A short description of the category',
    `created_date` DATETIME NULL COMMENT 'Date when the record was created',
    `updated_date` DATETIME NULL COMMENT 'Date when the record was updated',
    PRIMARY KEY (`rule_category_sk`))
    ENGINE = InnoDB
    COMMENT = 'This table contains the list of all the rule categories like “ACCOUNT”, “TRANSACTION”, “FILE” etc';
CREATE TABLE IF NOT EXISTS `validationdb`.`rule_set` (
    `rule_set_sk` VARCHAR(36) NOT NULL COMMENT 'The primary key of the rule set table',
    `rule_category_sk` VARCHAR(36) NOT NULL COMMENT 'The rule category that the rule set belongs',
    `rule_set_id` VARCHAR(50) NOT NULL COMMENT 'Unique id associated with the rule set',
    `rule_set_name` VARCHAR(100) NOT NULL COMMENT 'The name of the rule set',
    `rule_set_desc` VARCHAR(200) NOT NULL COMMENT 'A short description of the ruleset',
    `active` BOOLEAN NOT NULL COMMENT 'Indicates if the rule set is actively in use or not',
    `rule_set_impl_name` VARCHAR(100) NOT NULL,
    `created_date` DATETIME NULL COMMENT 'Date when the record was created',
    `updated_date` DATETIME NULL COMMENT 'Date when the record was updated',
    PRIMARY KEY (`rule_set_sk`),
    INDEX `rule_category_fk_idx` (`rule_category_sk` ASC) VISIBLE,
    CONSTRAINT `rule_category_fk`
    FOREIGN KEY (`rule_category_sk`)
    REFERENCES `validationdb`.`rule_category` (`rule_category_sk`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    COMMENT = 'The rule sets that belong to a category';
CREATE TABLE IF NOT EXISTS `validationdb`.`rule` (
    `rule_sk` VARCHAR(36) NOT NULL COMMENT 'Primary key of the table',
    `rule_set_sk` VARCHAR(36) NOT NULL COMMENT 'Ruleset that the rule is associated',
    `rule_id` VARCHAR(50) NOT NULL COMMENT 'A unique id assigned to the rule',
    `rule_name` VARCHAR(100) NOT NULL COMMENT 'The name of the rule',
    `rule_desc` VARCHAR(200) NOT NULL COMMENT 'A short description of the rule',
    `active` BOOLEAN NOT NULL COMMENT 'Indicates if the rule is active',
    `rule_impl_name` VARCHAR(100) NOT NULL,
    `created_date` DATETIME NULL COMMENT 'Date when the record is created',
    `updated_date` DATETIME NULL COMMENT 'Date when the record was updated',
    PRIMARY KEY (`rule_sk`),
    INDEX `rule_set_fk_idx` (`rule_set_sk` ASC) VISIBLE,
    CONSTRAINT `rule_set_fk`
    FOREIGN KEY (`rule_set_sk`)
    REFERENCES `validationdb`.`rule_set` (`rule_set_sk`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    COMMENT = 'This table contains the list of all the rules that are associated with a rule set.';
CREATE TABLE IF NOT EXISTS `validationdb`.`rule_executed` (
    `rule_executed_sk` VARCHAR(36) NOT NULL COMMENT 'The primary key of the table',
    `payload_tracker_sk` VARCHAR(36) NOT NULL COMMENT 'The payload for which the rules were executed',
    `rule_id` VARCHAR(50) NOT NULL COMMENT 'The id of the rule',
    `rule_passed` BOOLEAN NOT NULL COMMENT 'Indicates if the rule passed or failed',
    `created_date` DATETIME NULL COMMENT 'The date when the record was created',
    `updated_date` DATETIME NULL COMMENT 'The date when the record was updated',
    PRIMARY KEY (`rule_executed_sk`),
    INDEX `payload_tracker_fk_idx` (`payload_tracker_sk` ASC) VISIBLE,
    CONSTRAINT `payload_tracker_fk1`
    FOREIGN KEY (`payload_tracker_sk`)
    REFERENCES `validationdb`.`payload_tracker` (`payload_tracker_sk`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    COMMENT = 'This table contains the list of all the rules that are executed for the payload';
CREATE TABLE IF NOT EXISTS `validationdb`.`rule_execution_message` (
    `rule_execution_message_sk` VARCHAR(36) NOT NULL COMMENT 'The primary key of the table',
    `rule_executed_sk` VARCHAR(36) NOT NULL COMMENT 'The rule result for which the message was generated',
    `message_code` VARCHAR(50) NOT NULL COMMENT 'The message code of the message',
    `created_date` DATETIME NULL COMMENT 'The date when the record was created',
    `updated_date` DATETIME NULL COMMENT 'The date when the record was updated',
    PRIMARY KEY (`rule_execution_message_sk`),
    INDEX `rule_result_fk_idx` (`rule_executed_sk` ASC) VISIBLE,
    CONSTRAINT `rule_executed_fk`
    FOREIGN KEY (`rule_executed_sk`)
    REFERENCES `validationdb`.`rule_executed` (`rule_executed_sk`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    COMMENT = 'The messages that are generated for the table';