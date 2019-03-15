
    create table user_entity (
       id bigint not null auto_increment,
        created_date datetime(6) not null,
        display_name varchar(255),
        first_name varchar(255),
        identifier varchar(255) not null,
        last_modified_date datetime(6) not null,
        last_name varchar(255),
        registered bit not null,
        user_id varchar(255) not null,
        version bigint,
        primary key (id)
    ) engine=InnoDB;

    alter table user_entity
       add constraint UK_User_Identifier unique (identifier);

    alter table user_entity
       add constraint UK_User_UserId unique (user_id);
