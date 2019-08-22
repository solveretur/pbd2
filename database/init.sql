SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS access_roles_dict;
DROP TABLE IF EXISTS applications;
DROP TABLE IF EXISTS athletes;
DROP TABLE IF EXISTS coaches;
DROP TABLE IF EXISTS event_contestants;
DROP TABLE IF EXISTS athlete_to_start_groups;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS event_ranks_dict;
DROP TABLE IF EXISTS exercise;
DROP TABLE IF EXISTS exercise_execution;
DROP TABLE IF EXISTS exercise_to_training_plan;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS profile_types_dict;
DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS professionals;
DROP TABLE IF EXISTS run_results;
DROP TABLE IF EXISTS run_types_dict;
DROP TABLE IF EXISTS seasons;
DROP TABLE IF EXISTS season_types_dict;
DROP TABLE IF EXISTS sport_class_dict;
DROP TABLE IF EXISTS start_groups;
DROP TABLE IF EXISTS teams;
DROP TABLE IF EXISTS team_applications;
DROP TABLE IF EXISTS training_plan;
DROP TABLE IF EXISTS training_plans_sessions;

SET FOREIGN_KEY_CHECKS = 1;

#==================TWORZENIE TABELI=====================

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE seasons (
  season_id      INT NOT NULL UNIQUE AUTO_INCREMENT,
  year           INT NOT NULL,
  season_type_id INT NOT NULL,
  PRIMARY KEY (season_id),
  FOREIGN KEY (season_type_id) REFERENCES season_types_dict (season_type_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE season_types_dict (
  season_type_id INT         NOT NULL UNIQUE AUTO_INCREMENT,
  season_type    VARCHAR(11) NOT NULL UNIQUE,
  PRIMARY KEY (season_type_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE event_ranks_dict (
  event_rank_id INT         NOT NULL UNIQUE AUTO_INCREMENT,
  event_rank    VARCHAR(25) NOT NULL UNIQUE,
  PRIMARY KEY (event_rank_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE events (
  event_id                  INT         NOT NULL UNIQUE AUTO_INCREMENT,
  event_rank_id             INT         NOT NULL,
  town                      VARCHAR(20) NOT NULL,
  district                  VARCHAR(20) NOT NULL,
  occur_date                DATE        NOT NULL,
  max_number_of_contestants INT         NOT NULL,
  season_id                 INT         NOT NULL,
  PRIMARY KEY (event_id),
  FOREIGN KEY (season_id) REFERENCES seasons (season_id),
  FOREIGN KEY (event_rank_id) REFERENCES event_ranks_dict (event_rank_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE payments (
  payment_id      INT      NOT NULL UNIQUE AUTO_INCREMENT,
  creation_date   DATETIME NOT NULL        DEFAULT CURRENT_TIMESTAMP,
  accounting_date DATETIME                 DEFAULT NULL,
  athlete_id      INT      NOT NULL,
  event_id        INT      NOT NULL,
  PRIMARY KEY (payment_id),
  FOREIGN KEY (athlete_id) REFERENCES athletes (person_id),
  FOREIGN KEY (event_id) REFERENCES events (event_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE access_roles_dict (
  access_role_id INT         NOT NULL UNIQUE AUTO_INCREMENT,
  access_role    VARCHAR(25) NOT NULL UNIQUE,
  PRIMARY KEY (access_role_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE profile_types_dict (
  profile_type_id INT         NOT NULL UNIQUE AUTO_INCREMENT,
  profile_type    VARCHAR(25) NOT NULL UNIQUE,
  PRIMARY KEY (profile_type_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE person (
  person_id       INT          NOT NULL UNIQUE AUTO_INCREMENT,
  name            VARCHAR(11)  NOT NULL,
  surname         VARCHAR(11)  NOT NULL,
  birthday        DATE         NOT NULL,
  town            VARCHAR(11)  NOT NULL,
  sex             VARCHAR(11)  NOT NULL,
  username        VARCHAR(50)  NOT NULL UNIQUE,
  password_hash   VARCHAR(100) NOT NULL,
  email           VARCHAR(50)  NOT NULL UNIQUE,
  access_role_id  INT          NOT NULL        DEFAULT 1,
  profile_type_id INT                          DEFAULT NULL,
  PRIMARY KEY (person_id),
  FOREIGN KEY (access_role_id) REFERENCES access_roles_dict (access_role_id),
  FOREIGN KEY (profile_type_id) REFERENCES profile_types_dict (profile_type_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE teams (
  team_id INT         NOT NULL UNIQUE AUTO_INCREMENT,
  name    VARCHAR(11) NOT NULL UNIQUE,
  town    VARCHAR(11) NOT NULL,
  PRIMARY KEY (team_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE training_plan_type_dict (
  training_plan_type_id INT         NOT NULL UNIQUE AUTO_INCREMENT,
  plan_type             VARCHAR(50) NOT NULL,
  PRIMARY KEY (training_plan_type_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE training_plan (
  training_plan_id      INT     NOT NULL UNIQUE AUTO_INCREMENT,
  plan_name             VARCHAR(50)             DEFAULT NULL,
  plan_description      TEXT    NOT NULL,
  goal                  INT(11) NOT NULL,
  creator_id            INT(11) NOT NULL,
  training_plan_type_id INT(11) NOT NULL,
  PRIMARY KEY (training_plan_id),
  FOREIGN KEY (creator_id) REFERENCES coaches (person_id),
  FOREIGN KEY (training_plan_type_id) REFERENCES training_plan_type_dict (training_plan_type_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE training_plans_sessions (
  training_plans_sessions_id INT      NOT NULL UNIQUE AUTO_INCREMENT,
  start_date                 DATETIME NOT NULL,
  end_time                   DATE                     DEFAULT NULL,
  training_plan_id           INT      NOT NULL,
  PRIMARY KEY (training_plans_sessions_id),
  FOREIGN KEY (training_plan_id) REFERENCES training_plan (training_plan_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE athletes (
  person_id       INT  NOT NULL,
  is_professional BOOL NOT NULL        DEFAULT FALSE,
  professional_id INT                  DEFAULT NULL,
  PRIMARY KEY (person_id),
  FOREIGN KEY (person_id) REFERENCES person (person_id),
  FOREIGN KEY (professional_id) REFERENCES professionals (professional_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE coaches (
  person_id INT         NOT NULL,
  team_id   INT  DEFAULT NULL,
  is_active BOOL DEFAULT TRUE,
  licence   VARCHAR(11) NOT NULL UNIQUE,
  PRIMARY KEY (person_id),
  FOREIGN KEY (person_id) REFERENCES person (person_id),
  FOREIGN KEY (team_id) REFERENCES teams (team_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE sport_class_dict (
  sport_class_id INT         NOT NULL UNIQUE AUTO_INCREMENT,
  sport_class    VARCHAR(11) NOT NULL,
  PRIMARY KEY (sport_class_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE professionals (
  professional_id   INT  NOT NULL UNIQUE AUTO_INCREMENT,
  career_start_date DATE NOT NULL,
  career_end_date   DATE                 DEFAULT NULL,
  current_team_id   INT                  DEFAULT NULL,
  training_plan_id  INT                  DEFAULT NULL,
  licence           VARCHAR(11)          DEFAULT NULL,
  sport_class_id    INT                  DEFAULT NULL,
  is_active         BOOL                 DEFAULT TRUE,
  athlete_id        INT  NOT NULL,
  current_coach_id  INT                  DEFAULT NULL,
  PRIMARY KEY (professional_id),
  FOREIGN KEY (athlete_id) REFERENCES athletes (person_id),
  FOREIGN KEY (current_team_id) REFERENCES teams (team_id),
  FOREIGN KEY (training_plan_id) REFERENCES training_plans_sessions (training_plans_sessions_id),
  FOREIGN KEY (current_coach_id) REFERENCES coaches (person_id),
  FOREIGN KEY (sport_class_id) REFERENCES sport_class_dict (sport_class_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE event_contestants (
  contestant_id    INT NOT NULL UNIQUE AUTO_INCREMENT,
  event_id         INT NOT NULL,
  athlete_id       INT NOT NULL,
  is_valid_for_run BOOL                DEFAULT FALSE,
  run_number       VARCHAR(11)         DEFAULT NULL,
  PRIMARY KEY (contestant_id),
  FOREIGN KEY (event_id) REFERENCES events (event_id),
  FOREIGN KEY (athlete_id) REFERENCES athletes (person_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE run_types_dict (
  run_type_id INT         NOT NULL UNIQUE AUTO_INCREMENT,
  run_type    VARCHAR(11) NOT NULL,
  PRIMARY KEY (run_type_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE run_results (
  result_id              INT        NOT NULL UNIQUE AUTO_INCREMENT,
  run_type_id            INT        NOT NULL,
  result_time_in_seconds BIGINT(11) NOT NULL,
  athlete_id             INT        NOT NULL,
  event_id               INT        NOT NULL,
  PRIMARY KEY (result_id),
  FOREIGN KEY (run_type_id) REFERENCES run_types_dict (run_type_id),
  FOREIGN KEY (athlete_id) REFERENCES athletes (person_id),
  FOREIGN KEY (event_id) REFERENCES events (event_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE start_groups (
  start_groups_id      INT         NOT NULL UNIQUE AUTO_INCREMENT,
  max_number_of_people INT         NOT NULL,
  start_group_type     VARCHAR(11) NOT NULL,
  event_id             INT         NOT NULL,
  PRIMARY KEY (start_groups_id),
  FOREIGN KEY (event_id) REFERENCES events (event_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE athlete_to_start_groups (
  athlete_to_start_groups_id INT NOT NULL UNIQUE AUTO_INCREMENT,
  start_groups_id            INT NOT NULL,
  athlete_id                 INT NOT NULL,
  PRIMARY KEY (athlete_to_start_groups_id),
  FOREIGN KEY (start_groups_id) REFERENCES start_groups (start_groups_id),
  FOREIGN KEY (athlete_id) REFERENCES athletes (person_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE exercise_type (
  exercise_type_id INT         NOT NULL UNIQUE AUTO_INCREMENT,
  exercise_type    VARCHAR(11) NOT NULL,
  PRIMARY KEY (exercise_type_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE exercise (
  exercise_id      INT  NOT NULL UNIQUE AUTO_INCREMENT,
  exercise_type_id INT  NOT NULL,
  expected_result  INT  NOT NULL,
  description      TEXT NOT NULL,
  PRIMARY KEY (exercise_id),
  FOREIGN KEY (exercise_type_id) REFERENCES exercise_type (exercise_type_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE exercise_execution (
  exercise_execution_id INT      NOT NULL UNIQUE AUTO_INCREMENT,
  exercise_id           INT      NOT NULL,
  professional_id       INT      NOT NULL,
  result                INT      NOT NULL,
  execution_date        DATETIME NOT NULL        DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (exercise_execution_id),
  FOREIGN KEY (exercise_id) REFERENCES exercise (exercise_id),
  FOREIGN KEY (professional_id) REFERENCES professionals (professional_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE exercise_to_training_plan (
  exercise_to_training_plan_id INT NOT NULL UNIQUE AUTO_INCREMENT,
  exercise_id                  INT NOT NULL,
  training_plan_id             INT NOT NULL,
  PRIMARY KEY (exercise_id),
  FOREIGN KEY (exercise_id) REFERENCES exercise (exercise_id),
  FOREIGN KEY (training_plan_id) REFERENCES training_plan (training_plan_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE applications (
  application_id   INT      NOT NULL UNIQUE AUTO_INCREMENT,
  event_id         INT      NOT NULL,
  athlete_id       INT      NOT NULL,
  application_date DATETIME NOT NULL        DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (application_id),
  FOREIGN KEY (event_id) REFERENCES events (event_id),
  FOREIGN KEY (athlete_id) REFERENCES athletes (person_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE team_applications (
  team_application_id INT      NOT NULL UNIQUE AUTO_INCREMENT,
  team_id             INT      NOT NULL,
  event_id            INT      NOT NULL,
  application_date    DATETIME NOT NULL        DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (team_application_id),
  FOREIGN KEY (event_id) REFERENCES events (event_id),
  FOREIGN KEY (team_id) REFERENCES teams (team_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

SET FOREIGN_KEY_CHECKS = 1;

#==================WRZUCANIE_DANYCH_Z_CSV=====================
SET FOREIGN_KEY_CHECKS = 0;

LOAD DATA INFILE '/var/lib/mysql-files/athletes.csv'
INTO TABLE athletes
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/person.csv'
INTO TABLE person
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/coaches.csv'
INTO TABLE coaches
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/teams.csv'
INTO TABLE teams
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/professionals.csv'
INTO TABLE professionals
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/events.csv'
INTO TABLE events
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/event_ranks.csv'
INTO TABLE event_ranks_dict
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/access_roles.csv'
INTO TABLE access_roles_dict
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/profile_types.csv'
INTO TABLE profile_types_dict
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/event_contestants.csv'
INTO TABLE event_contestants
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/run_results.csv'
INTO TABLE run_results
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/seasons.csv'
INTO TABLE seasons
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/season_types_dict.csv'
INTO TABLE season_types_dict
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/run_types_dict.csv'
INTO TABLE run_types_dict
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/sport_class_dict.csv'
INTO TABLE sport_class_dict
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/exercise_type.csv'
INTO TABLE exercise_type
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/exercise.csv'
INTO TABLE exercise
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/exercise_to_training_plan.csv'
INTO TABLE exercise_to_training_plan
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/training_plan_type_dict.csv'
INTO TABLE training_plan_type_dict
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA INFILE '/var/lib/mysql-files/training_plan.csv'
INTO TABLE training_plan
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';

SET FOREIGN_KEY_CHECKS = 1;
