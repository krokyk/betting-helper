CONNECT 'jdbc:derby:BettingDB;user=betting;password=password;create=true';

AUTOCOMMIT ON;

run '@db.scripts.dir@\db_ddl.sql';
run '@db.scripts.dir@\db_update_1.0.0.7.sql';
run '@db.scripts.dir@\db_update_1.0.0.9.sql';

DISCONNECT;
EXIT;