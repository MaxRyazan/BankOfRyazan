create table clients
(
    id           bigint auto_increment
        primary key,
    balance      double       not null,
    email        varchar(255) not null,
    first_name   varchar(255) not null,
    last_name    varchar(255) not null,
    phone_number varchar(255) not null,
    hash_pin     varchar(255) not null,
    balance_eur  double       not null,
    balance_usd  double       not null,
    constraint UK_bt1ji0od8t2mhp0thot6pod8u
        unique (phone_number),
    constraint UK_srv16ica2c1csub334bxjjb59
        unique (email)
)
    auto_increment = 9;

create table contribution
(
    id                     bigint auto_increment
        primary key,
    date_of_begin          varchar(255) not null,
    duration               int          not null,
    number_of_contribution varchar(255) not null,
    percent                double       not null,
    sum                    int          not null,
    sum_with_percent       double       null,
    contributor_id         bigint       null,
    date_of_end            varchar(255) not null,
    status                 varchar(255) null,
    constraint FKg4aw93p5ypk9s7itxx1p13c5b
        foreign key (contributor_id) references clients (id)
)
    auto_increment = 9;

create table credits
(
    id                bigint auto_increment
        primary key,
    credit_percent    double       null,
    date_of_begin     varchar(255) null,
    monthly_payment   double       null,
    credit_number     varchar(255) not null,
    number_of_pays    int          null,
    credit_sum        int          null,
    sum_with_percents double       null,
    borrower_id       bigint       null,
    rest_of_credit    double       null,
    status            varchar(255) null,
    constraint UK_mng63op492tddy8nb6h9g4nwg
        unique (credit_number),
    constraint FKfvtro7hotcxbbxj2bhjxll362
        foreign key (borrower_id) references clients (id)
)
    auto_increment = 30;

create table email_code_sender
(
    id       bigint auto_increment
        primary key,
    value    varchar(255) null,
    restorer bigint       null,
    constraint FK8whkg2mq61688nslfxev52shn
        foreign key (restorer) references clients (id)
)
    auto_increment = 5;

create table exchange_rate_class
(
    id            bigint auto_increment
        primary key,
    course_of_eur double       null,
    course_of_usd double       null,
    date          varchar(255) null
)
    auto_increment = 19;

create table investment
(
    id                       bigint auto_increment
        primary key,
    base_price_of_investment double       null,
    curr_price_of_investment double       null,
    date_of_investment       varchar(255) null,
    investment_size_         double       null,
    price_per_unit           double       not null,
    type                     varchar(255) not null,
    investment_id            bigint       null,
    margin                   double       null,
    constraint FKaaleli32m9uw04j6bcmhhfux2
        foreign key (investment_id) references clients (id)
)
    auto_increment = 3;

create table metal_rate
(
    id        bigint auto_increment
        primary key,
    date      varchar(255) null,
    gold      float        null,
    palladium float        null,
    platinum  float        null,
    rhodium   float        null,
    silver    float        null
)
    auto_increment = 15;

create table pay
(
    id        bigint auto_increment
        primary key,
    date      varchar(255) null,
    sum       double       null,
    credit_id bigint       null,
    constraint FKj0uyebq1h2o0xxfoonpmpt99w
        foreign key (credit_id) references credits (id)
)
    auto_increment = 94;

create table transactions
(
    transaction_id bigint auto_increment
        primary key,
    sum            int          null,
    timestamp      varchar(255) null,
    recipient_id   bigint       null,
    sender_id      bigint       null,
    constraint FK79cnfg1sjv7atktigc2ukm94k
        foreign key (sender_id) references clients (id),
    constraint FKaoi3w6t71ucsjcxiyvxy5so3y
        foreign key (recipient_id) references clients (id)
)
    auto_increment = 27;

