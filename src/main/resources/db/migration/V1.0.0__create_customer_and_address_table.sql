create table if not exists customer
(
    id          bigserial
        primary key,
    first_name   varchar(255),
    last_name    varchar(255),
    phone_number varchar(60),
    username    varchar(255) not null
);

create table if not exists address
(
    customer_id  bigint,
    constraint fk_customer_address
        foreign key (customer_id)
            references customer (id),
    id           bigserial
        primary key,
    city         varchar(255)
        constraint address_city_check
            check ((city)::text = ANY
                   ((ARRAY ['HO_CHI_MINH'::character varying, 'HA_NOI'::character varying, 'NEW_YORK'::character varying, 'LONDON'::character varying, 'TOKYO'::character varying, 'SEOUL'::character varying])::text[])),
    district     varchar(255),
    nation       varchar(255)
        constraint address_nation_check
            check ((nation)::text = ANY
                   ((ARRAY ['VIE'::character varying, 'US'::character varying, 'UK'::character varying, 'JP'::character varying, 'SOUTH_KOREAN'::character varying])::text[])),
    street_number varchar(255),
    type         varchar(255)
        constraint address_type_check
            check ((type)::text = ANY ((ARRAY ['HOME'::character varying, 'WORK'::character varying])::text[])),
    ward         varchar(255)
);