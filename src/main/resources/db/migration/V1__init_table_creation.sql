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
COMMENT = 'The payload tracker detail table, that tracks all the responses received for an outbound payload and all the responses sent for an inbound payload'