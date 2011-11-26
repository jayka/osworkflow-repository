
DROP TABLE IF EXISTS OS_PROPERTYENTRY;

CREATE TABLE OS_PROPERTYENTRY 
(
  GLOBAL_KEY varchar(255), 
  ITEM_KEY varchar(255), 
  ITEM_TYPE smallint, 
  STRING_VALUE varchar(255), 
  DATE_VALUE TIMESTAMP , 
  DATA_VALUE varbinary(2000), 
  FLOAT_VALUE float, 
  NUMBER_VALUE numeric, 
  primary key (GLOBAL_KEY, ITEM_KEY)
);



