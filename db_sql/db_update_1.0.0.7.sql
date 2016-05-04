/*@CONNECT 'jdbc:derby:BettingDB;user=betting;password=password';@*/

RENAME COLUMN teams.fl_active TO fl_status;
ALTER TABLE teams ADD CONSTRAINT chk_team_status CHECK (fl_status > -1 and fl_status < 3);

ALTER TABLE providers ADD fl_type SMALLINT;
UPDATE providers SET fl_type = 0 WHERE fl_results = 1;
UPDATE providers SET fl_type = 1 WHERE fl_fixtures = 1;
ALTER TABLE providers ALTER COLUMN fl_type NOT NULL;
ALTER TABLE providers ADD CONSTRAINT chk_provider_types CHECK (fl_type > -1 and fl_type < 2);
ALTER TABLE providers DROP CONSTRAINT mutually_exclusive_types;
ALTER TABLE providers DROP fl_results;
ALTER TABLE providers DROP fl_fixtures;

/*@DISCONNECT;@*/
/*@EXIT;@*/
