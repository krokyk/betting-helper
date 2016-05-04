/*@CONNECT 'jdbc:derby:BettingDB;user=betting;password=password';@*/

UPDATE matches SET fl_result = -1 WHERE home_goals IS NULL OR away_goals IS NULL;

/*@DISCONNECT;@*/
/*@EXIT;@*/
