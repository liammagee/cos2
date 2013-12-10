# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table ahp (
  id                        bigint not null,
  rdfid                     varchar(255),
  criteria_matrix_id        bigint,
  constraint pk_ahp primary key (id))
;

create table assessment (
  id                        bigint not null,
  rdfid                     varchar(255),
  created_at                timestamp,
  creator_id                bigint,
  project_id                bigint,
  constraint pk_assessment primary key (id))
;

create table assessment_value (
  id                        bigint not null,
  snapshot_id               bigint not null,
  rdfid                     varchar(255),
  value                     float,
  subdomain_id              bigint,
  constraint pk_assessment_value primary key (id))
;

create table criteria_matrix (
  id                        bigint not null,
  rdfid                     varchar(255),
  constraint pk_criteria_matrix primary key (id))
;

create table criterion (
  id                        integer not null,
  criteria_matrix_id        bigint not null,
  rdfid                     varchar(255),
  name                      varchar(255),
  description               varchar(255),
  initial_weight            integer,
  constraint pk_criterion primary key (id))
;

create table critical_issue (
  id                        bigint not null,
  rdfid                     varchar(255),
  name                      varchar(255),
  description               varchar(255),
  perceived_significance    integer,
  associated_objective_id   bigint,
  project_id                bigint,
  constraint pk_critical_issue primary key (id))
;

create table domain (
  id                        bigint not null,
  rdfid                     varchar(255),
  name                      varchar(255),
  description               varchar(255),
  constraint pk_domain primary key (id))
;

create table indicator (
  id                        bigint not null,
  rdfid                     varchar(255),
  name                      varchar(255),
  description               TEXT,
  source                    varchar(255),
  identifying_code          varchar(255),
  unit_of_measure           varchar(255),
  target_id                 bigint,
  indicator_set_id          bigint,
  category                  varchar(255),
  is_social                 boolean,
  is_natural                boolean,
  is_process                boolean,
  is_object                 boolean,
  is_aggregate              boolean,
  is_singular               boolean,
  is_spatial                boolean,
  is_temporal               boolean,
  constraint pk_indicator primary key (id))
;

create table indicator_set (
  id                        bigint not null,
  rdfid                     varchar(255),
  name                      varchar(255),
  description               varchar(255),
  source                    varchar(255),
  constraint pk_indicator_set primary key (id))
;

create table indicator_value (
  id                        bigint not null,
  snapshot_id               bigint not null,
  rdfid                     varchar(255),
  value                     float,
  indicator_id              bigint,
  assessment_id             bigint,
  constraint pk_indicator_value primary key (id))
;

create table issue_component (
  id                        bigint not null,
  rdfid                     varchar(255),
  name                      varchar(255),
  description               varchar(255),
  perceived_significance    integer,
  associated_objective_id   bigint,
  parent_issue_id           bigint,
  constraint pk_issue_component primary key (id))
;

create table issue_matrix (
  id                        bigint not null,
  rdfid                     varchar(255),
  criterion_id              integer,
  constraint pk_issue_matrix primary key (id))
;

create table matrix_cell (
  id                        bigint not null,
  matrix_row_id             bigint not null,
  rdfid                     varchar(255),
  value                     float,
  constraint pk_matrix_cell primary key (id))
;

create table matrix_row (
  id                        bigint not null,
  criteria_matrix_id        bigint not null,
  rdfid                     varchar(255),
  constraint pk_matrix_row primary key (id))
;

create table objective (
  id                        bigint not null,
  rdfid                     varchar(255),
  description               varchar(255),
  desired_direction         integer,
  constraint pk_objective primary key (id))
;

create table project (
  id                        bigint not null,
  rdfid                     varchar(255),
  project_name              varchar(255),
  project_description       varchar(255),
  general_issue             varchar(255),
  normative_goal            varchar(255),
  created_on                timestamp,
  creator_id                bigint,
  project_progress_id       bigint,
  ahp_id                    bigint,
  constraint pk_project primary key (id))
;

create table project_progress (
  id                        bigint not null,
  rdfid                     varchar(255),
  is_project_created        integer,
  has_initial_assessment_been_conducted integer,
  is_general_issue_defined  integer,
  is_normative_goal_defined integer,
  do_at_least_four_issues_exist integer,
  are_issues_ranked         integer,
  are_all_domains_covered_by_issues integer,
  do_at_least_four_indicators_exist integer,
  are_all_issues_measured_by_at_least_one_indicator integer,
  have_impacts_between_issues_been_analysed integer,
  is_report_compiled        integer,
  is_response_developed_and_monitored integer,
  is_model_reviewed_and_adapted integer,
  constraint pk_project_progress primary key (id))
;

create table rdf_model (
  rdfid                     varchar(255))
;

create table snapshot (
  id                        bigint not null,
  rdfid                     varchar(255),
  created_at                timestamp,
  creator_id                bigint,
  project_id                bigint,
  assessment_id             bigint,
  constraint pk_snapshot primary key (id))
;

create table subdomain (
  id                        bigint not null,
  rdfid                     varchar(255),
  subdomain_id              integer,
  name                      varchar(255),
  description               varchar(255),
  parent_domain_id          bigint,
  constraint pk_subdomain primary key (id))
;

create table target (
  id                        bigint not null,
  rdfid                     varchar(255),
  value                     varchar(255),
  desired_direction         integer,
  constraint pk_target primary key (id))
;

create table COS_USERS (
  id                        bigint not null,
  rdfid                     varchar(255),
  name                      varchar(255),
  username                  varchar(255),
  password                  varchar(255),
  constraint pk_COS_USERS primary key (id))
;


create table critical_issue_subdomain (
  critical_issue_id              bigint not null,
  subdomain_id                   bigint not null,
  constraint pk_critical_issue_subdomain primary key (critical_issue_id, subdomain_id))
;

create table indicator_critical_issue (
  indicator_id                   bigint not null,
  critical_issue_id              bigint not null,
  constraint pk_indicator_critical_issue primary key (indicator_id, critical_issue_id))
;

create table indicator_subdomain (
  indicator_id                   bigint not null,
  subdomain_id                   bigint not null,
  constraint pk_indicator_subdomain primary key (indicator_id, subdomain_id))
;

create table issue_component_indicator (
  issue_component_id             bigint not null,
  indicator_id                   bigint not null,
  constraint pk_issue_component_indicator primary key (issue_component_id, indicator_id))
;

create table issue_component_subdomain (
  issue_component_id             bigint not null,
  subdomain_id                   bigint not null,
  constraint pk_issue_component_subdomain primary key (issue_component_id, subdomain_id))
;
create sequence ahp_seq;

create sequence assessment_seq;

create sequence assessment_value_seq;

create sequence criteria_matrix_seq;

create sequence criterion_seq;

create sequence critical_issue_seq;

create sequence domain_seq;

create sequence indicator_seq;

create sequence indicator_set_seq;

create sequence indicator_value_seq;

create sequence issue_component_seq;

create sequence issue_matrix_seq;

create sequence matrix_cell_seq;

create sequence matrix_row_seq;

create sequence objective_seq;

create sequence project_seq;

create sequence project_progress_seq;

create sequence snapshot_seq;

create sequence subdomain_seq;

create sequence target_seq;

create sequence COS_USERS_seq;

alter table ahp add constraint fk_ahp_criteriaMatrix_1 foreign key (criteria_matrix_id) references criteria_matrix (id);
create index ix_ahp_criteriaMatrix_1 on ahp (criteria_matrix_id);
alter table assessment add constraint fk_assessment_creator_2 foreign key (creator_id) references COS_USERS (id);
create index ix_assessment_creator_2 on assessment (creator_id);
alter table assessment add constraint fk_assessment_project_3 foreign key (project_id) references project (id);
create index ix_assessment_project_3 on assessment (project_id);
alter table assessment_value add constraint fk_assessment_value_snapshot_4 foreign key (snapshot_id) references snapshot (id);
create index ix_assessment_value_snapshot_4 on assessment_value (snapshot_id);
alter table assessment_value add constraint fk_assessment_value_subdomain_5 foreign key (subdomain_id) references subdomain (id);
create index ix_assessment_value_subdomain_5 on assessment_value (subdomain_id);
alter table criterion add constraint fk_criterion_criteria_matrix_6 foreign key (criteria_matrix_id) references criteria_matrix (id);
create index ix_criterion_criteria_matrix_6 on criterion (criteria_matrix_id);
alter table critical_issue add constraint fk_critical_issue_associatedOb_7 foreign key (associated_objective_id) references objective (id);
create index ix_critical_issue_associatedOb_7 on critical_issue (associated_objective_id);
alter table critical_issue add constraint fk_critical_issue_project_8 foreign key (project_id) references project (id);
create index ix_critical_issue_project_8 on critical_issue (project_id);
alter table indicator add constraint fk_indicator_target_9 foreign key (target_id) references target (id);
create index ix_indicator_target_9 on indicator (target_id);
alter table indicator add constraint fk_indicator_indicatorSet_10 foreign key (indicator_set_id) references indicator_set (id);
create index ix_indicator_indicatorSet_10 on indicator (indicator_set_id);
alter table indicator_value add constraint fk_indicator_value_snapshot_11 foreign key (snapshot_id) references snapshot (id);
create index ix_indicator_value_snapshot_11 on indicator_value (snapshot_id);
alter table indicator_value add constraint fk_indicator_value_indicator_12 foreign key (indicator_id) references indicator (id);
create index ix_indicator_value_indicator_12 on indicator_value (indicator_id);
alter table indicator_value add constraint fk_indicator_value_assessment_13 foreign key (assessment_id) references assessment (id);
create index ix_indicator_value_assessment_13 on indicator_value (assessment_id);
alter table issue_component add constraint fk_issue_component_associated_14 foreign key (associated_objective_id) references objective (id);
create index ix_issue_component_associated_14 on issue_component (associated_objective_id);
alter table issue_component add constraint fk_issue_component_parentIssu_15 foreign key (parent_issue_id) references critical_issue (id);
create index ix_issue_component_parentIssu_15 on issue_component (parent_issue_id);
alter table issue_matrix add constraint fk_issue_matrix_criterion_16 foreign key (criterion_id) references criterion (id);
create index ix_issue_matrix_criterion_16 on issue_matrix (criterion_id);
alter table matrix_cell add constraint fk_matrix_cell_matrix_row_17 foreign key (matrix_row_id) references matrix_row (id);
create index ix_matrix_cell_matrix_row_17 on matrix_cell (matrix_row_id);
alter table matrix_row add constraint fk_matrix_row_criteria_matrix_18 foreign key (criteria_matrix_id) references criteria_matrix (id);
create index ix_matrix_row_criteria_matrix_18 on matrix_row (criteria_matrix_id);
alter table project add constraint fk_project_creator_19 foreign key (creator_id) references COS_USERS (id);
create index ix_project_creator_19 on project (creator_id);
alter table project add constraint fk_project_projectProgress_20 foreign key (project_progress_id) references project_progress (id);
create index ix_project_projectProgress_20 on project (project_progress_id);
alter table project add constraint fk_project_ahp_21 foreign key (ahp_id) references ahp (id);
create index ix_project_ahp_21 on project (ahp_id);
alter table snapshot add constraint fk_snapshot_creator_22 foreign key (creator_id) references COS_USERS (id);
create index ix_snapshot_creator_22 on snapshot (creator_id);
alter table snapshot add constraint fk_snapshot_project_23 foreign key (project_id) references project (id);
create index ix_snapshot_project_23 on snapshot (project_id);
alter table snapshot add constraint fk_snapshot_assessment_24 foreign key (assessment_id) references assessment (id);
create index ix_snapshot_assessment_24 on snapshot (assessment_id);
alter table subdomain add constraint fk_subdomain_parentDomain_25 foreign key (parent_domain_id) references domain (id);
create index ix_subdomain_parentDomain_25 on subdomain (parent_domain_id);



alter table critical_issue_subdomain add constraint fk_critical_issue_subdomain_c_01 foreign key (critical_issue_id) references critical_issue (id);

alter table critical_issue_subdomain add constraint fk_critical_issue_subdomain_s_02 foreign key (subdomain_id) references subdomain (id);

alter table indicator_critical_issue add constraint fk_indicator_critical_issue_i_01 foreign key (indicator_id) references indicator (id);

alter table indicator_critical_issue add constraint fk_indicator_critical_issue_c_02 foreign key (critical_issue_id) references critical_issue (id);

alter table indicator_subdomain add constraint fk_indicator_subdomain_indica_01 foreign key (indicator_id) references indicator (id);

alter table indicator_subdomain add constraint fk_indicator_subdomain_subdom_02 foreign key (subdomain_id) references subdomain (id);

alter table issue_component_indicator add constraint fk_issue_component_indicator__01 foreign key (issue_component_id) references issue_component (id);

alter table issue_component_indicator add constraint fk_issue_component_indicator__02 foreign key (indicator_id) references indicator (id);

alter table issue_component_subdomain add constraint fk_issue_component_subdomain__01 foreign key (issue_component_id) references issue_component (id);

alter table issue_component_subdomain add constraint fk_issue_component_subdomain__02 foreign key (subdomain_id) references subdomain (id);

# --- !Downs

drop table if exists ahp cascade;

drop table if exists assessment cascade;

drop table if exists assessment_value cascade;

drop table if exists criteria_matrix cascade;

drop table if exists criterion cascade;

drop table if exists critical_issue cascade;

drop table if exists indicator_critical_issue cascade;

drop table if exists critical_issue_subdomain cascade;

drop table if exists domain cascade;

drop table if exists indicator cascade;

drop table if exists indicator_subdomain cascade;

drop table if exists indicator_set cascade;

drop table if exists indicator_value cascade;

drop table if exists issue_component cascade;

drop table if exists issue_component_indicator cascade;

drop table if exists issue_component_subdomain cascade;

drop table if exists issue_matrix cascade;

drop table if exists matrix_cell cascade;

drop table if exists matrix_row cascade;

drop table if exists objective cascade;

drop table if exists project cascade;

drop table if exists project_progress cascade;

drop table if exists rdf_model cascade;

drop table if exists snapshot cascade;

drop table if exists subdomain cascade;

drop table if exists target cascade;

drop table if exists COS_USERS cascade;

drop sequence if exists ahp_seq;

drop sequence if exists assessment_seq;

drop sequence if exists assessment_value_seq;

drop sequence if exists criteria_matrix_seq;

drop sequence if exists criterion_seq;

drop sequence if exists critical_issue_seq;

drop sequence if exists domain_seq;

drop sequence if exists indicator_seq;

drop sequence if exists indicator_set_seq;

drop sequence if exists indicator_value_seq;

drop sequence if exists issue_component_seq;

drop sequence if exists issue_matrix_seq;

drop sequence if exists matrix_cell_seq;

drop sequence if exists matrix_row_seq;

drop sequence if exists objective_seq;

drop sequence if exists project_seq;

drop sequence if exists project_progress_seq;

drop sequence if exists snapshot_seq;

drop sequence if exists subdomain_seq;

drop sequence if exists target_seq;

drop sequence if exists COS_USERS_seq;

