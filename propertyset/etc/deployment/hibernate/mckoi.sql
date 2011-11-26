
DROP TABLE IF EXISTS OS_PROPERTYENTRY;

create table OS_PROPERTYENTRY (
   entity_name VARCHAR(125) not null,
   entity_id BIGINT not null,
   entity_key VARCHAR(255) not null,
   key_type INTEGER,
   boolean_val BIT,
   double_val DOUBLE,
   string_val VARCHAR(255),
   long_val BIGINT,
   int_val INTEGER,
   date_val DATE,
   primary key (entity_name, entity_id, entity_key)
);



