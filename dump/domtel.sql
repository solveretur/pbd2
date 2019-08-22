-- Adminer 4.7.0 MySQL dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

DROP TABLE IF EXISTS `access_roles_dict`;
CREATE TABLE `access_roles_dict` (
  `access_role_id` int(11) NOT NULL AUTO_INCREMENT,
  `access_role` varchar(25) NOT NULL,
  PRIMARY KEY (`access_role_id`),
  UNIQUE KEY `access_role_id` (`access_role_id`),
  UNIQUE KEY `access_role` (`access_role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `access_roles_dict` (`access_role_id`, `access_role`) VALUES
(2,	'ROLE_ADMIN'),
(1,	'ROLE_USER\r');

DROP TABLE IF EXISTS `applications`;
CREATE TABLE `applications` (
  `application_id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) NOT NULL,
  `athlete_id` int(11) NOT NULL,
  `application_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`application_id`),
  UNIQUE KEY `application_id` (`application_id`),
  KEY `event_id` (`event_id`),
  KEY `athlete_id` (`athlete_id`),
  CONSTRAINT `applications_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`),
  CONSTRAINT `applications_ibfk_2` FOREIGN KEY (`athlete_id`) REFERENCES `athletes` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `athletes`;
CREATE TABLE `athletes` (
  `person_id` int(11) NOT NULL,
  `is_professional` tinyint(1) NOT NULL DEFAULT '0',
  `professional_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`person_id`),
  KEY `professional_id` (`professional_id`),
  CONSTRAINT `athletes_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`),
  CONSTRAINT `athletes_ibfk_2` FOREIGN KEY (`professional_id`) REFERENCES `professionals` (`professional_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `athletes` (`person_id`, `is_professional`, `professional_id`) VALUES
(1,	1,	1),
(3,	1,	2),
(4,	0,	NULL);

DROP TABLE IF EXISTS `athlete_to_start_groups`;
CREATE TABLE `athlete_to_start_groups` (
  `athlete_to_start_groups_id` int(11) NOT NULL AUTO_INCREMENT,
  `start_groups_id` int(11) NOT NULL,
  `athlete_id` int(11) NOT NULL,
  PRIMARY KEY (`athlete_to_start_groups_id`),
  UNIQUE KEY `athlete_to_start_groups_id` (`athlete_to_start_groups_id`),
  KEY `start_groups_id` (`start_groups_id`),
  KEY `athlete_id` (`athlete_id`),
  CONSTRAINT `athlete_to_start_groups_ibfk_1` FOREIGN KEY (`start_groups_id`) REFERENCES `start_groups` (`start_groups_id`),
  CONSTRAINT `athlete_to_start_groups_ibfk_2` FOREIGN KEY (`athlete_id`) REFERENCES `athletes` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `coaches`;
CREATE TABLE `coaches` (
  `person_id` int(11) NOT NULL,
  `team_id` int(11) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `licence` varchar(11) NOT NULL,
  PRIMARY KEY (`person_id`),
  UNIQUE KEY `licence` (`licence`),
  KEY `team_id` (`team_id`),
  CONSTRAINT `coaches_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`),
  CONSTRAINT `coaches_ibfk_2` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `coaches` (`person_id`, `team_id`, `is_active`, `licence`) VALUES
(2,	NULL,	1,	'AZE_131\r');

DROP TABLE IF EXISTS `events`;
CREATE TABLE `events` (
  `event_id` int(11) NOT NULL AUTO_INCREMENT,
  `event_rank_id` int(11) NOT NULL,
  `town` varchar(20) NOT NULL,
  `district` varchar(20) NOT NULL,
  `occur_date` date NOT NULL,
  `max_number_of_contestants` int(11) NOT NULL,
  `season_id` int(11) NOT NULL,
  PRIMARY KEY (`event_id`),
  UNIQUE KEY `event_id` (`event_id`),
  KEY `season_id` (`season_id`),
  KEY `event_rank_id` (`event_rank_id`),
  CONSTRAINT `events_ibfk_1` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`season_id`),
  CONSTRAINT `events_ibfk_2` FOREIGN KEY (`event_rank_id`) REFERENCES `event_ranks_dict` (`event_rank_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `events` (`event_id`, `event_rank_id`, `town`, `district`, `occur_date`, `max_number_of_contestants`, `season_id`) VALUES
(1,	1,	'Warszawa',	'Mazowieckie',	'2018-12-06',	30,	1),
(2,	3,	'Poznan  ',	'Wielkopolskie',	'2018-12-06',	30,	1),
(3,	1,	'Gdansk',	'Pomorskie',	'2019-01-01',	30,	1),
(4,	2,	'Warszawa',	'Mazowieckie',	'2018-12-01',	30,	1),
(5,	3,	'Lomza   ',	'Mazowieckie  ',	'2019-01-28',	30,	1),
(6,	2,	'Michalow',	'Mazowieckie  ',	'2019-01-25',	30,	1),
(7,	3,	'Warszawa',	'Mazowieckie  ',	'2019-02-26',	30,	1);

DROP TABLE IF EXISTS `event_contestants`;
CREATE TABLE `event_contestants` (
  `contestant_id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) NOT NULL,
  `athlete_id` int(11) NOT NULL,
  `is_valid_for_run` tinyint(1) DEFAULT '0',
  `run_number` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`contestant_id`),
  UNIQUE KEY `contestant_id` (`contestant_id`),
  KEY `event_id` (`event_id`),
  KEY `athlete_id` (`athlete_id`),
  CONSTRAINT `event_contestants_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`),
  CONSTRAINT `event_contestants_ibfk_2` FOREIGN KEY (`athlete_id`) REFERENCES `athletes` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `event_contestants` (`contestant_id`, `event_id`, `athlete_id`, `is_valid_for_run`, `run_number`) VALUES
(1,	1,	1,	1,	'3406\r'),
(2,	2,	1,	1,	'5748\r'),
(3,	1,	3,	1,	'3121\r'),
(4,	2,	3,	1,	'3214\r'),
(5,	2,	4,	1,	'1314');

DROP TABLE IF EXISTS `event_ranks_dict`;
CREATE TABLE `event_ranks_dict` (
  `event_rank_id` int(11) NOT NULL AUTO_INCREMENT,
  `event_rank` varchar(25) NOT NULL,
  PRIMARY KEY (`event_rank_id`),
  UNIQUE KEY `event_rank_id` (`event_rank_id`),
  UNIQUE KEY `event_rank` (`event_rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `event_ranks_dict` (`event_rank_id`, `event_rank`) VALUES
(1,	'MISTRZOSTWA_POLSKI\r'),
(2,	'MISTRZOSTWA_WOJEWODZKIE\r'),
(3,	'REGULARNY_MEETING');

DROP TABLE IF EXISTS `exercise`;
CREATE TABLE `exercise` (
  `exercise_id` int(11) NOT NULL AUTO_INCREMENT,
  `exercise_type_id` int(11) NOT NULL,
  `expected_result` int(11) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`exercise_id`),
  UNIQUE KEY `exercise_id` (`exercise_id`),
  KEY `exercise_type_id` (`exercise_type_id`),
  CONSTRAINT `exercise_ibfk_1` FOREIGN KEY (`exercise_type_id`) REFERENCES `exercise_type` (`exercise_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `exercise` (`exercise_id`, `exercise_type_id`, `expected_result`, `description`) VALUES
(1,	1,	100,	'Wykonaj 100 pompek na raz\r'),
(2,	1,	30,	'Wykonaj 30 pompek na raz\r'),
(3,	1,	300,	'Wykonal 300 pompek w seriach 3x100\r'),
(4,	2,	10,	'Wykonaj przysiad 10 razy\r'),
(5,	2,	20,	'Wykonaj przysiad 30 razy\r'),
(6,	3,	600,	'Przebiegnij 1500m w 3 minuty');

DROP TABLE IF EXISTS `exercise_execution`;
CREATE TABLE `exercise_execution` (
  `exercise_execution_id` int(11) NOT NULL AUTO_INCREMENT,
  `exercise_id` int(11) NOT NULL,
  `professional_id` int(11) NOT NULL,
  `result` int(11) NOT NULL,
  `execution_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`exercise_execution_id`),
  UNIQUE KEY `exercise_execution_id` (`exercise_execution_id`),
  KEY `exercise_id` (`exercise_id`),
  KEY `professional_id` (`professional_id`),
  CONSTRAINT `exercise_execution_ibfk_1` FOREIGN KEY (`exercise_id`) REFERENCES `exercise` (`exercise_id`),
  CONSTRAINT `exercise_execution_ibfk_2` FOREIGN KEY (`professional_id`) REFERENCES `professionals` (`professional_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `exercise_to_training_plan`;
CREATE TABLE `exercise_to_training_plan` (
  `exercise_to_training_plan_id` int(11) NOT NULL AUTO_INCREMENT,
  `exercise_id` int(11) NOT NULL,
  `training_plan_id` int(11) NOT NULL,
  PRIMARY KEY (`exercise_id`),
  UNIQUE KEY `exercise_to_training_plan_id` (`exercise_to_training_plan_id`),
  KEY `training_plan_id` (`training_plan_id`),
  CONSTRAINT `exercise_to_training_plan_ibfk_1` FOREIGN KEY (`exercise_id`) REFERENCES `exercise` (`exercise_id`),
  CONSTRAINT `exercise_to_training_plan_ibfk_2` FOREIGN KEY (`training_plan_id`) REFERENCES `training_plan` (`training_plan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `exercise_to_training_plan` (`exercise_to_training_plan_id`, `exercise_id`, `training_plan_id`) VALUES
(1,	1,	1),
(4,	2,	2),
(2,	3,	1),
(5,	4,	2),
(3,	5,	1),
(6,	6,	2);

DROP TABLE IF EXISTS `exercise_type`;
CREATE TABLE `exercise_type` (
  `exercise_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `exercise_type` varchar(11) NOT NULL,
  PRIMARY KEY (`exercise_type_id`),
  UNIQUE KEY `exercise_type_id` (`exercise_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `exercise_type` (`exercise_type_id`, `exercise_type`) VALUES
(1,	'PUSH_UP\r'),
(2,	'SQUAT\r'),
(3,	'RUN_1500M');

DROP TABLE IF EXISTS `payments`;
CREATE TABLE `payments` (
  `payment_id` int(11) NOT NULL AUTO_INCREMENT,
  `creation_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `accounting_date` datetime DEFAULT NULL,
  `athlete_id` int(11) NOT NULL,
  `event_id` int(11) NOT NULL,
  PRIMARY KEY (`payment_id`),
  UNIQUE KEY `payment_id` (`payment_id`),
  KEY `athlete_id` (`athlete_id`),
  KEY `event_id` (`event_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`athlete_id`) REFERENCES `athletes` (`person_id`),
  CONSTRAINT `payments_ibfk_2` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
  `person_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(11) NOT NULL,
  `surname` varchar(11) NOT NULL,
  `birthday` date NOT NULL,
  `town` varchar(11) NOT NULL,
  `sex` varchar(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(100) NOT NULL,
  `email` varchar(50) NOT NULL,
  `access_role_id` int(11) NOT NULL DEFAULT '1',
  `profile_type_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`person_id`),
  UNIQUE KEY `person_id` (`person_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  KEY `access_role_id` (`access_role_id`),
  KEY `profile_type_id` (`profile_type_id`),
  CONSTRAINT `person_ibfk_1` FOREIGN KEY (`access_role_id`) REFERENCES `access_roles_dict` (`access_role_id`),
  CONSTRAINT `person_ibfk_2` FOREIGN KEY (`profile_type_id`) REFERENCES `profile_types_dict` (`profile_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `person` (`person_id`, `name`, `surname`, `birthday`, `town`, `sex`, `username`, `password_hash`, `email`, `access_role_id`, `profile_type_id`) VALUES
(1,	'Adam  ',	'Stec  ',	'1996-10-22',	'Warszawa',	'M',	'adam ',	'$2a$10$yKK/nzAp9tKEucf/I8QROe7rsO4blJiCMhSWiF6/n.Ly.WqM/NBeO',	'adam@stec.com  ',	1,	2),
(2,	'Mister',	'Marian',	'1950-01-02',	'Wroclaw ',	'M',	'admin',	'$2a$10$EYYWmNqv.Qd7Muxjw5E0..j3rTItw0lvH6wh3c5KruztMEINm9djq',	'admin@admin.com',	2,	1),
(3,	'Jan   ',	'Kowalski',	'1996-05-08',	'Zakopane',	'M',	'kowal',	'test                                                          ',	'kowal@gmail.com       ',	1,	2),
(4,	'Jan   ',	'Kowalski',	'1989-01-12',	'Leszno  ',	'M',	'jjay ',	'test                                                          ',	'jan.kowalski@gmail.com',	1,	2);

DROP TABLE IF EXISTS `professionals`;
CREATE TABLE `professionals` (
  `professional_id` int(11) NOT NULL AUTO_INCREMENT,
  `career_start_date` date NOT NULL,
  `career_end_date` date DEFAULT NULL,
  `current_team_id` int(11) DEFAULT NULL,
  `training_plan_id` int(11) DEFAULT NULL,
  `licence` varchar(11) DEFAULT NULL,
  `sport_class_id` int(11) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `athlete_id` int(11) NOT NULL,
  `current_coach_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`professional_id`),
  UNIQUE KEY `professional_id` (`professional_id`),
  KEY `athlete_id` (`athlete_id`),
  KEY `current_team_id` (`current_team_id`),
  KEY `training_plan_id` (`training_plan_id`),
  KEY `current_coach_id` (`current_coach_id`),
  KEY `sport_class_id` (`sport_class_id`),
  CONSTRAINT `professionals_ibfk_1` FOREIGN KEY (`athlete_id`) REFERENCES `athletes` (`person_id`),
  CONSTRAINT `professionals_ibfk_2` FOREIGN KEY (`current_team_id`) REFERENCES `teams` (`team_id`),
  CONSTRAINT `professionals_ibfk_3` FOREIGN KEY (`training_plan_id`) REFERENCES `training_plans_sessions` (`training_plans_sessions_id`),
  CONSTRAINT `professionals_ibfk_4` FOREIGN KEY (`current_coach_id`) REFERENCES `coaches` (`person_id`),
  CONSTRAINT `professionals_ibfk_5` FOREIGN KEY (`sport_class_id`) REFERENCES `sport_class_dict` (`sport_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `professionals` (`professional_id`, `career_start_date`, `career_end_date`, `current_team_id`, `training_plan_id`, `licence`, `sport_class_id`, `is_active`, `athlete_id`, `current_coach_id`) VALUES
(1,	'2010-10-10',	NULL,	1,	NULL,	'AZE_1313',	1,	1,	1,	2),
(2,	'2010-01-01',	NULL,	NULL,	NULL,	'LPZ_4312',	1,	1,	3,	NULL);

DROP TABLE IF EXISTS `profile_types_dict`;
CREATE TABLE `profile_types_dict` (
  `profile_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `profile_type` varchar(25) NOT NULL,
  PRIMARY KEY (`profile_type_id`),
  UNIQUE KEY `profile_type_id` (`profile_type_id`),
  UNIQUE KEY `profile_type` (`profile_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `profile_types_dict` (`profile_type_id`, `profile_type`) VALUES
(2,	'ATHLETE'),
(1,	'NONE\r');

DROP TABLE IF EXISTS `run_results`;
CREATE TABLE `run_results` (
  `result_id` int(11) NOT NULL AUTO_INCREMENT,
  `run_type_id` int(11) NOT NULL,
  `result_time_in_seconds` bigint(11) NOT NULL,
  `athlete_id` int(11) NOT NULL,
  `event_id` int(11) NOT NULL,
  PRIMARY KEY (`result_id`),
  UNIQUE KEY `result_id` (`result_id`),
  KEY `run_type_id` (`run_type_id`),
  KEY `athlete_id` (`athlete_id`),
  KEY `event_id` (`event_id`),
  CONSTRAINT `run_results_ibfk_1` FOREIGN KEY (`run_type_id`) REFERENCES `run_types_dict` (`run_type_id`),
  CONSTRAINT `run_results_ibfk_2` FOREIGN KEY (`athlete_id`) REFERENCES `athletes` (`person_id`),
  CONSTRAINT `run_results_ibfk_3` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `run_results` (`result_id`, `run_type_id`, `result_time_in_seconds`, `athlete_id`, `event_id`) VALUES
(1,	1,	183,	1,	1),
(2,	3,	313,	1,	1),
(3,	1,	194,	1,	2),
(4,	3,	212,	1,	2),
(5,	1,	193,	3,	1),
(6,	3,	243,	3,	1),
(7,	1,	211,	3,	2),
(8,	1,	190,	4,	2),
(9,	3,	350,	4,	2);

DROP TABLE IF EXISTS `run_types_dict`;
CREATE TABLE `run_types_dict` (
  `run_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `run_type` varchar(11) NOT NULL,
  PRIMARY KEY (`run_type_id`),
  UNIQUE KEY `run_type_id` (`run_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `run_types_dict` (`run_type_id`, `run_type`) VALUES
(1,	'M_1500\r'),
(2,	'M_800\r'),
(3,	'M_3000');

DROP TABLE IF EXISTS `seasons`;
CREATE TABLE `seasons` (
  `season_id` int(11) NOT NULL AUTO_INCREMENT,
  `year` int(11) NOT NULL,
  `season_type_id` int(11) NOT NULL,
  PRIMARY KEY (`season_id`),
  UNIQUE KEY `season_id` (`season_id`),
  KEY `season_type_id` (`season_type_id`),
  CONSTRAINT `seasons_ibfk_1` FOREIGN KEY (`season_type_id`) REFERENCES `season_types_dict` (`season_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `seasons` (`season_id`, `year`, `season_type_id`) VALUES
(1,	2018,	1),
(2,	2018,	2),
(3,	2019,	1),
(4,	2019,	2),
(5,	2017,	1),
(6,	2017,	2);

DROP TABLE IF EXISTS `season_types_dict`;
CREATE TABLE `season_types_dict` (
  `season_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `season_type` varchar(11) NOT NULL,
  PRIMARY KEY (`season_type_id`),
  UNIQUE KEY `season_type_id` (`season_type_id`),
  UNIQUE KEY `season_type` (`season_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `season_types_dict` (`season_type_id`, `season_type`) VALUES
(2,	'INDOOR'),
(1,	'OUTDOOR\r');

DROP TABLE IF EXISTS `sport_class_dict`;
CREATE TABLE `sport_class_dict` (
  `sport_class_id` int(11) NOT NULL AUTO_INCREMENT,
  `sport_class` varchar(11) NOT NULL,
  PRIMARY KEY (`sport_class_id`),
  UNIQUE KEY `sport_class_id` (`sport_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `sport_class_dict` (`sport_class_id`, `sport_class`) VALUES
(1,	'MM');

DROP TABLE IF EXISTS `start_groups`;
CREATE TABLE `start_groups` (
  `start_groups_id` int(11) NOT NULL AUTO_INCREMENT,
  `max_number_of_people` int(11) NOT NULL,
  `start_group_type` varchar(11) NOT NULL,
  `event_id` int(11) NOT NULL,
  PRIMARY KEY (`start_groups_id`),
  UNIQUE KEY `start_groups_id` (`start_groups_id`),
  KEY `event_id` (`event_id`),
  CONSTRAINT `start_groups_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `teams`;
CREATE TABLE `teams` (
  `team_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(11) NOT NULL,
  `town` varchar(11) NOT NULL,
  PRIMARY KEY (`team_id`),
  UNIQUE KEY `team_id` (`team_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `teams` (`team_id`, `name`, `town`) VALUES
(1,	'LEGIA PANY',	'Warszawa\r');

DROP TABLE IF EXISTS `team_applications`;
CREATE TABLE `team_applications` (
  `team_application_id` int(11) NOT NULL AUTO_INCREMENT,
  `team_id` int(11) NOT NULL,
  `event_id` int(11) NOT NULL,
  `application_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`team_application_id`),
  UNIQUE KEY `team_application_id` (`team_application_id`),
  KEY `event_id` (`event_id`),
  KEY `team_id` (`team_id`),
  CONSTRAINT `team_applications_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`),
  CONSTRAINT `team_applications_ibfk_2` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `training_plan`;
CREATE TABLE `training_plan` (
  `training_plan_id` int(11) NOT NULL AUTO_INCREMENT,
  `plan_name` varchar(50) DEFAULT NULL,
  `plan_description` text NOT NULL,
  `goal` int(11) NOT NULL,
  `creator_id` int(11) NOT NULL,
  `training_plan_type_id` int(11) NOT NULL,
  PRIMARY KEY (`training_plan_id`),
  UNIQUE KEY `training_plan_id` (`training_plan_id`),
  KEY `creator_id` (`creator_id`),
  KEY `training_plan_type_id` (`training_plan_type_id`),
  CONSTRAINT `training_plan_ibfk_1` FOREIGN KEY (`creator_id`) REFERENCES `coaches` (`person_id`),
  CONSTRAINT `training_plan_ibfk_2` FOREIGN KEY (`training_plan_type_id`) REFERENCES `training_plan_type_dict` (`training_plan_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `training_plan` (`training_plan_id`, `plan_name`, `plan_description`, `goal`, `creator_id`, `training_plan_type_id`) VALUES
(1,	'Pudzian Plan   ',	'Stworzony dla Pudziana - zapewnia wzrost masy o 100%',	130,	2,	1),
(2,	'Usain Bolt Plan',	'Oryginalny plan Usaina Bolta                        ',	100,	2,	2);

DROP TABLE IF EXISTS `training_plans_sessions`;
CREATE TABLE `training_plans_sessions` (
  `training_plans_sessions_id` int(11) NOT NULL AUTO_INCREMENT,
  `start_date` datetime NOT NULL,
  `end_time` date DEFAULT NULL,
  `training_plan_id` int(11) NOT NULL,
  PRIMARY KEY (`training_plans_sessions_id`),
  UNIQUE KEY `training_plans_sessions_id` (`training_plans_sessions_id`),
  KEY `training_plan_id` (`training_plan_id`),
  CONSTRAINT `training_plans_sessions_ibfk_1` FOREIGN KEY (`training_plan_id`) REFERENCES `training_plan` (`training_plan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `training_plan_type_dict`;
CREATE TABLE `training_plan_type_dict` (
  `training_plan_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `plan_type` varchar(50) NOT NULL,
  PRIMARY KEY (`training_plan_type_id`),
  UNIQUE KEY `training_plan_type_id` (`training_plan_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `training_plan_type_dict` (`training_plan_type_id`, `plan_type`) VALUES
(1,	'MORE_STRENGHT\r'),
(2,	'MORE_SPEED');

-- 2019-01-21 11:07:39
