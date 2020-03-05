/*@CONNECT 'jdbc:derby:BettingDB;user=betting;password=password';@*/

ALTER TABLE bets ADD COLUMN odds_new NUMERIC(6,2);
ALTER TABLE bets ADD COLUMN bet_value_new NUMERIC(10,2);
UPDATE bets SET odds_new = odds, bet_value_new = bet_value;
ALTER TABLE bets DROP COLUMN odds;
ALTER TABLE bets DROP COLUMN bet_value;
RENAME COLUMN bets.odds_new TO odds;
RENAME COLUMN bets.bet_value_new TO bet_value;
ALTER TABLE bets ALTER COLUMN odds NOT NULL;
ALTER TABLE bets ALTER COLUMN bet_value NOT NULL;

/*@DISCONNECT;@*/
/*@EXIT;@*/
