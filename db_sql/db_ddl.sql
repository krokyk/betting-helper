--DROP TABLE matches;
--DROP TABLE bets;
--DROP TABLE team_mappings;
--DROP TABLE teams;
--DROP TABLE providers;

CREATE TABLE providers (
    url VARCHAR(1000) PRIMARY KEY,
    sport VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL,
    league VARCHAR(50) NOT NULL,
    season VARCHAR(50) NOT NULL,
    fl_results SMALLINT NOT NULL,
    fl_fixtures SMALLINT NOT NULL,
    CONSTRAINT mutually_exclusive_types CHECK (fl_results <> fl_fixtures)
);

CREATE TABLE teams (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    name VARCHAR(50) NOT NULL,
    sport VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL,
    fl_active SMALLINT NOT NULL WITH DEFAULT 0,
    result_updated TIMESTAMP,
    next_match_date TIMESTAMP,
    league VARCHAR(50),
    fixtures_provider VARCHAR(1000),
    CONSTRAINT uniq_name_sport_country UNIQUE (name, sport, country),
    CONSTRAINT fk_provider FOREIGN KEY (fixtures_provider) REFERENCES PROVIDERS (url)
);

CREATE TABLE team_mappings (
    external_name VARCHAR(50) NOT NULL,
    sport VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL,
    team_id INTEGER NOT NULL,
    PRIMARY KEY(external_name, sport, country),
    CONSTRAINT fk_team FOREIGN KEY (team_id) REFERENCES teams (id)
);

CREATE TABLE matches (
    date TIMESTAMP NOT NULL,
    home_team_id INTEGER NOT NULL,
    away_team_id INTEGER NOT NULL,
    fl_result SMALLINT NOT NULL,
    home_goals SMALLINT,
    away_goals SMALLINT,
    updated TIMESTAMP NOT NULL,
    season VARCHAR(50) NOT NULL,
    league VARCHAR(50) NOT NULL,
    CONSTRAINT fk_home_team FOREIGN KEY (home_team_id) REFERENCES teams (id),
    CONSTRAINT fk_away_team FOREIGN KEY (away_team_id) REFERENCES teams (id),
    PRIMARY KEY (date, home_team_id, away_team_id)
);

CREATE TABLE bets (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    match_date TIMESTAMP NOT NULL,
    home_team_id INTEGER NOT NULL,
    away_team_id INTEGER NOT NULL,
    odds NUMERIC(4,2) NOT NULL,
    bet_value NUMERIC(5,2) NOT NULL,
    home_team_goals SMALLINT,
    away_team_goals SMALLINT,
    fl_status SMALLINT NOT NULL WITH DEFAULT -1,
    active_team_id INTEGER NOT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL,
    CONSTRAINT fk_bets_home_team FOREIGN KEY (home_team_id) REFERENCES teams (id),
    CONSTRAINT fk_bets_away_team FOREIGN KEY (away_team_id) REFERENCES teams (id),
    CONSTRAINT fk_bets_active_team FOREIGN KEY (active_team_id) REFERENCES teams (id),
    CONSTRAINT uniq_created UNIQUE (created),
    CONSTRAINT uniq_updated UNIQUE (updated)
);