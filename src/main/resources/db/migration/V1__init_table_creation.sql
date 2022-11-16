DROP TABLE IF EXISTS `validationdb`.`payload_tracker`;
DROP TABLE IF EXISTS `validationdb`.`payload_tracker_detail`;
DROP TABLE IF EXISTS `validationdb`.`rule_executed`;
DROP TABLE IF EXISTS `validationdb`.`rule_execution_message`;
DROP TABLE IF EXISTS `validationdb`.`rule_set_implementation`;
DROP TABLE IF EXISTS `validationdb`.`rule_implementation`;
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
CREATE TABLE IF NOT EXISTS `validationdb`.`rule_set_implementation` (
    `rule_set_impl_sk` VARCHAR(36) NOT NULL COMMENT 'The primary key of the table',
    `rule_set_id` VARCHAR(50) NOT NULL COMMENT 'The unique id for the rule set',
    `rule_set_impl_name` VARCHAR(100) NOT NULL COMMENT 'The implementation name for the rule set',
    `created_date` DATETIME NULL COMMENT 'The date when the record was created',
    `updated_date` DATETIME NULL COMMENT 'The date when the record was updated',
    PRIMARY KEY (`rule_set_impl_sk`))
    ENGINE = InnoDB
    COMMENT = 'This table contains the implementation for each rule set';
CREATE TABLE IF NOT EXISTS `validationdb`.`rule_implementation` (
    `rule_impl_sk` VARCHAR(36) NOT NULL COMMENT 'Primary key of the table',
    `rule_set_impl_sk` VARCHAR(45) NOT NULL COMMENT 'Rule set that the rule belongs',
    `rule_id` VARCHAR(50) NOT NULL COMMENT 'The id of the rule',
    `rule_impl_name` VARCHAR(100) NOT NULL COMMENT 'The implementation for the rule',
    `created_date` DATETIME NULL COMMENT 'Date when the record was created',
    `updated_date` DATETIME NULL COMMENT 'Date when the record was updated',
    PRIMARY KEY (`rule_impl_sk`),
    INDEX `rule_set_imp_fk_idx` (`rule_set_impl_sk` ASC) VISIBLE,
    CONSTRAINT `rule_set_imp_fk`
    FOREIGN KEY (`rule_set_impl_sk`)
    REFERENCES `validationdb`.`rule_set_implementation` (`rule_set_impl_sk`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    COMMENT = 'This table contains the implementation names for each rule';