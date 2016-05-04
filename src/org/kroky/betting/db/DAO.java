/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.db;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kroky.betting.common.enums.BetStatus;
import org.kroky.betting.common.enums.MatchResult;
import org.kroky.betting.common.enums.ProviderType;
import org.kroky.betting.common.enums.TeamStatus;
import org.kroky.betting.common.exceptions.BettingRuntimeException;
import org.kroky.betting.common.util.Utils;
import static org.kroky.betting.db.DBState.*;
import org.kroky.betting.db.objects.*;
import org.kroky.betting.db.objects.compositeIds.MatchId;
import org.kroky.betting.db.objects.compositeIds.TeamMappingId;

/**
 *
 * @author Kroky
 */
public class DAO {
    private static final Logger LOG = Logger.getLogger(DAO.class);
    
    private static final Session session = DBManager.getSessionFactory().openSession();
    private static Transaction tx = null;
    
    /**
     * 
     * @return returns true if there was already an active transaction
     */
    public static boolean begin() {
        boolean present = tx != null;
        if (tx == null) {
            LOG.debug("Starting new DB transaction");
            tx = session.beginTransaction();
        } else {
            LOG.debug("Continuing in DB transaction");
        }
        return present;
    }
    
    public static void rollback() {
        if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
            LOG.debug("Trying to rollback DB transaction");
            tx.rollback();
            tx = null;
        } else {
            LOG.warn("Rollback attempted but there is no active DB transaction");
        }
    }
    
    public static void commit() {
        if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
            LOG.debug("Trying to commit DB transaction");
            tx.commit();
            tx = null;
        } else {
            LOG.warn("Commit attempted but there's no active DB transaction");
        }
    }
    
    public static void closeSession() {
        if (session != null) {
            LOG.debug("Closing DB session");
            session.close();
        } else {
            LOG.warn("Close session was called but there is no opened DB session");
        }
    }
    
    /**
     * FOR TESTING
     * @param args
     * @throws Exception 
     */
    //<editor-fold defaultstate="collapsed" desc="MAIN METHOD FOR TESTING PURPOSES">
    public static void main(String[] args) throws Exception {
        
//        System.out.println(1^1);
//        System.out.println(0^1);
        
//        File teamFile = new File("teams_for_mapping.txt");
//        File mappingFile = new File("team_mappings_for_mapping.txt");
//        File betsFile = new File("bets_for_mapping.txt");
//
//        ///////////////////
//        Map<String, Team> teamMap = new LinkedHashMap<String, Team>();
//        List<String> lines = FileUtils.readLines(teamFile, "UTF-8");
//        Pattern p = Pattern.compile("(.*)\\t(.*)\\t(.*)\\t(.*)\\t(.*)\\t(.*)\\t(.*)\\t(.*)");
//        for(String line : lines) {
//            Matcher m = p.matcher(line);
//            if(m.find()) {
//                String id = m.group(1);
//                String name = m.group(2);
//                String sport = m.group(3);
//                String country = m.group(4);
//                Team team = new Team(name, sport, country);
//                team.setId(new Integer(id));
//                teamMap.put(name, team);
//            }
//        }
//        //System.out.println(Utils.mapToString(teamMap));
//        //System.out.println(teamMap.size());
//
//        //////////////////////////////
//        ArrayList<String> inserts = new ArrayList<String>();
//        lines = FileUtils.readLines(mappingFile, "UTF-8");
//        p = Pattern.compile("(.*)\\*(.*)");
//        String insert;
//        for(String line : lines) {
//            Matcher m = p.matcher(line);
//            if(m.find()) {
//                String external = m.group(1);
//                String internal = m.group(2);
//                Team team = teamMap.get(internal);
//                String country = team.getCountry();
//                Integer id = team.getId();
//                external = external.replaceAll("'", "''");
//                insert = "INSERT INTO BETTING.TEAM_MAPPINGS (EXTERNAL_NAME, SPORT, COUNTRY, TEAM_ID) VALUES ('"+external+"', 'Soccer', '"+country+"', "+id+");";
//                inserts.add(insert);
//            }
//        }
//        FileUtils.writeLines(new File("mappingInserts.sql"), "UTF-8", inserts);
//
//        /////////////////////////////
//        //
//        //xxxxxx'2012-07-09 17:30:00.0', 'TPS Turku', 'Honka', 3.20, 1.00, NULL, NULL, 0, 'TPS Turku', '2012-07-29 16:46:47.588', '2012-08-19 11:00:04.507');
//        lines = FileUtils.readLines(betsFile, "UTF-8");
//        inserts = new ArrayList<String>();
//        p = Pattern.compile("[^']+'[^']+'[^']+'([^']+)'[^']+'([^']+)'[^']+'([^']+)'");
//        for(String line : lines) {
//            Matcher m = p.matcher(line);
//            if(m.find()) {
//                line = line.replace("apostrophe", "'");
//                String home = m.group(1).replace("apostrophe", "'");
//                String away = m.group(2).replace("apostrophe", "'");
//                String active = m.group(3).replace("apostrophe", "'");
//                line = line.replace("'" + home + "'", "" + teamMap.get(home).getId());
//                line = line.replace("'" + away + "'", "" + teamMap.get(away).getId());
//                line = line.replace("'" + active + "'", "" + teamMap.get(active).getId());
//                inserts.add(line);
//            }
//        }
//        FileUtils.writeLines(new File("betsInserts.sql"), "UTF-8", inserts);
    }
    //</editor-fold>

    
    public static <T extends DBObject> T get(Class<T> aClass, Serializable id) {
        T obj = null;
        try {
            obj = (T) session.get(aClass, id);
        } catch(HibernateException e) {
            throw new BettingRuntimeException("Failed to load " + aClass.getSimpleName() + " object from the database", "Failed to get " + aClass + " using id=[" + id + "]", e);
        }
        return obj;
    }
    
    public static TeamMapping insertTeamMapping(TeamMapping teamMapping) {
        boolean wasActive = begin();
        try {
            Serializable id = session.save(teamMapping);
            if(!wasActive) {
                commit();
            }
            teamMapping = (TeamMapping) session.get(TeamMapping.class, id);
            setAdded(TeamMapping.class, true);
            generalUpdate = true;
        } catch (HibernateException e) {
            if(!wasActive) {
                rollback();
            }
            throw new BettingRuntimeException("<html>Failed to insert new team mapping:<br/>" + teamMapping.toHtml() + "</html>", "Failed to insert new " + teamMapping.toStringForException(), e);
        }
        return teamMapping;
    }
    
    public static Team insertTeam(String name, String sport, String country, String league) {
        boolean wasActive = begin();
        Team team = null;
        try {
            Query q = session.createQuery(
                "from Team t "
                    + "where t.name    = :name "
                    + "and   t.sport   = :sport "
                    + "and   t.country = :country "
            );
            q.setString("name", name);
            q.setString("sport", sport);
            q.setString("country", country);
            team = (Team) q.uniqueResult();
            if (team == null) {
                team = new Team(name, sport, country, league);
                Serializable id = session.save(team);
                team = (Team) session.get(Team.class, id);
                generalUpdate = true;
                setAdded(Team.class, true);
            } else if (league != null && !league.equals(team.getLeague())) {
                team.setLeague(league);
                session.update(team);
                setUpdated(Team.class, true);
            }
            if(!wasActive) {
                commit();
            }
        } catch (HibernateException e) {
            if(!wasActive) {
                rollback();
            }
            throw new BettingRuntimeException("<html>Failed to insert new team:<br/>" + team.toHtml() + "</html>", "Failed to insert new " + team.toStringForException(), e);
        }
        return team;
    }
    
    /**
     * USE ONLY FOR BRAND NEW TEAMS WITHOUT ID ASSIGNED
     * @param team
     * @return 
     */
    public static Team insertTeam(Team team) {
        return insertTeam(team.getName(), team.getSport(), team.getCountry(), team.getLeague());
    }
    
    /**
     * USE ONLY FOR BRAND NEW PROVIDERS - INSERTED OBJECT IS NOT CHECKED FOR EXISTENCE
     * @param team
     * @return 
     */
    public static Provider insertProvider(Provider provider) {
        boolean wasActive = begin();
        try {
            session.save(provider);
            if(!wasActive) {
                commit();
            }
            setAdded(Provider.class, true);
        } catch (HibernateException e) {
            if(!wasActive) {
                rollback();
            }
            throw new BettingRuntimeException("<html>Failed to insert new provider:<br/>" + provider.toHtml() + "</html>", "Failed to insert new " + provider.toStringForException(), e);
        }
        return provider;
    }
    
    public static void saveOrUpdateTeam(Team team) {
        saveOrUpdateTeam(team, null, null);
    }
    
    public static void saveOrUpdateTeam(Team team, String columnName, Object newValue) {
        boolean wasActive = begin();
        try {
            session.saveOrUpdate(team);
            if(!wasActive) {
                commit();
            }
            setUpdated(Team.class, true);
        } catch (HibernateException e) {
            if(!wasActive) {
                rollback();
                refresh(team);
            }
            if(columnName != null && newValue != null) {
                throw new BettingRuntimeException("<html>Failed to update team:" + team.toHtml() + "</html>", "Failed to update " + team.toStringForException() + ", modelColumn=" + columnName + ", newValue=" + newValue, e);
            } else {
                throw new BettingRuntimeException("<html>Failed to save/update team:<br/>" + team.toHtml() + "</html>", "Failed to save/update new " + team.toStringForException(), e);
            }
        }
        generalUpdate = true;
    }
    
    public static void saveOrUpdateBet(Bet bet) {
        saveOrUpdateBet(bet, null, null);
    }
    
    public static void saveOrUpdateBet(Bet bet, String columnName, Object newValue) {
        boolean wasActive = begin();
        try {
            session.saveOrUpdate(bet);
            if(!wasActive) {
                commit();
            }
            setUpdated(Bet.class, true);
        } catch (HibernateException e) {
            if(!wasActive) {
                rollback();
                refresh(bet);
            }
            if(columnName != null && newValue != null) {
                throw new BettingRuntimeException("<html>Failed to update bet:" + bet.toHtml() + "</html>", "Failed to update " + bet.toStringForException() + ", modelColumn=" + columnName + ", newValue=" + newValue, e);
            } else {
                throw new BettingRuntimeException("<html>Failed to save/update bet:<br/>" + bet.toHtml() + "</html>", "Failed to save/update " + bet.toStringForException(), e);
            }
        }
        generalUpdate = true;
    }
    
    public static void saveOrUpdateProvider(Provider provider) {
        saveOrUpdateProvider(provider, null, null);
    }
    
    public static void saveOrUpdateProvider(Provider provider, String columnName, Object newValue) {
        boolean wasActive = begin();
        try {
            session.saveOrUpdate(provider);
            if(!wasActive) {
                commit();
            }
            setUpdated(Provider.class, true);
        } catch (HibernateException e) {
            if(!wasActive) {
                rollback();
                refresh(provider);
            }
            if(columnName != null && newValue != null) {
                throw new BettingRuntimeException("<html>Failed to update provider:" + provider.toHtml() + "</html>", "Failed to update " + provider.toStringForException() + ", modelColumn=" + columnName + ", newValue=" + newValue, e);
            } else {
                throw new BettingRuntimeException("<html>Failed to save/update provider:<br/>" + provider.toHtml() + "</html>", "Failed to save/update " + provider.toStringForException(), e);
            }
        }
        generalUpdate = true;
    }
    
    public static void deleteProvider(Provider provider) {
        boolean wasActive = begin();
        try {
            session.delete(provider);
            if(!wasActive) {
                commit();
            }
            setRemoved(Provider.class, true);
        } catch (HibernateException e) {
            if(!wasActive) {
                rollback();
            }
            throw new BettingRuntimeException("<html>Failed to delete provider:<br/>" + provider.toHtml() + "</html>", "Failed to delete " + provider.toStringForException(), e);
        }
        generalUpdate = true;
    }
    
    public static Match addMatchToTeam(Timestamp date, Team homeTeam, Team awayTeam, Integer homeGoals, Integer awayGoals, String season, String league, MatchResult result) {
        boolean wasActive = begin();
        Match match = null;
        try {
            match = new Match(date, homeTeam, awayTeam, homeGoals, awayGoals, season, league, result);
            //match = (Match) session.merge(match);
            boolean added = homeTeam.addHomeMatch(match);
            //1 is enough, if match is added to one, it has to be added to the other as well
            if (added) {
                awayTeam.addAwayMatch(match);
                session.save(homeTeam);
                session.save(awayTeam);
                setAdded(Match.class, true);
                generalUpdate = true;
            } else {
                //check if we should update anything
                Match persistedMatch = get(Match.class, match.getId());
                if(!persistedMatch.getSeason().equals(match.getSeason()) || !persistedMatch.getLeague().equals(match.getLeague()) || !persistedMatch.getResult().equals(match.getResult())) {
                    persistedMatch.setUpdated(Utils.now());
                    persistedMatch.setSeason(match.getSeason());
                    persistedMatch.setLeague(match.getLeague());
                    persistedMatch.setResult(match.getResult());
                    saveOrUpdateMatch(persistedMatch);
                    setUpdated(Match.class, false);
                }
            }
            if(!wasActive) {
                commit();
            }
        } catch (HibernateException e) {
            if(!wasActive) {
                rollback();
            }
            throw new BettingRuntimeException("<html>Failed to save/update match:<br/>" + match.toHtml() + "</html>", "Failed to save/update " + match.toStringForException(), e);
        }
        return match;
    }
    
    public static void saveOrUpdateMatch(Match match) {
        boolean wasActive = begin();
        try {
            session.saveOrUpdate(match);
            if(!wasActive) {
                commit();
            }
            setUpdated(Match.class, true);
        } catch (HibernateException e) {
            if(!wasActive) {
                rollback();
                refresh(match);
            }
            throw new BettingRuntimeException("<html>Failed to save/update match:<br/>" + match.toHtml() + "</html>", "Failed to save/update " + match.toStringForException(), e);
        }
        generalUpdate = true;
    }
    
    /**
     * 
     * @return All teams from database
     */
    public static List<Team> getAllRegularTeams() {
        try {
            Query q = session.createQuery(
                "from Team t where t.flStatus <> :flStatus"
            );
            q.setInteger("flStatus", TeamStatus.DELETED.getCode());
            return q.list();
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to retrieve regular and active teams", "Failed to retrieve regular and active teams", e);
        }
    }
    
    public static Map<TeamMappingId, Team> getTeamNameMappings() {
        try {
            Query q = session.createQuery(
                "from TeamMapping tm "
            );
            Map<TeamMappingId, Team> map = new HashMap<TeamMappingId, Team>();
            List<TeamMapping> list = q.list();
            for (TeamMapping mapping : list) {
                map.put(mapping.getId(), mapping.getTeam());
            }
            return map;
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to retrieve team mappings", "Failed to retrieve team mappings", e);
        }
    }
    
    /**
     * 
     * @return list of all sports from Providers table
     */
    public static List<String> getAllCountriesFromProviders() {
        try {
            Query q = session.createQuery(
                "select distinct p.country "
                    + "from Provider p "
            );
            return q.list();
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to retrieve countries for providers", "Failed to retrieve countries for providers", e);
        }
    }
    
    /**
     * 
     * @return list of all sports from Teams table
     */
    public static List<String> getAllCountriesFromTeams() {
        try {
            Query q = session.createQuery(
                "select distinct t.country "
                    + "from Team t "
            );
            return q.list();
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to retrieve countries for teams", "Failed to retrieve countries for teams", e);
        }
    }
    
    /**
     * List of all result providers
     * @return 
     */
    public static List<Provider> getResultProviders() {
        try {
            Query q = session.createQuery(
                "from Provider p "
                    + "where  p.flType = :type "
            );
            q.setInteger("type", ProviderType.RESULTS.getCode());
            List<Provider> list = q.list();
            return list;
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to retrieve result providers", "Failed to retrieve result providers", e);
        }
    }
    
    /**
     * List of all fixtures providers
     * @return 
     */
    public static List<Provider> getFixturesProviders() {
        try {
            Query q = session.createQuery(
                "from Provider p "
                    + "where  p.flType = :type "
            );
            q.setInteger("type", ProviderType.FIXTURES.getCode());
            List<Provider> list = q.list();
            return list;
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to retrieve fixtures providers", "Failed to retrieve fixtures providers", e);
        }
    }

    public static boolean exists(Class<? extends DBObject> c, Serializable id) {
        boolean exists = false;
        try {
            exists = session.get(c, id) != null;
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to verify existence of " + c.getSimpleName(), "Failed to verify existence of " + c.getSimpleName() + " using id=[" + id + "]", e);
        }
        return exists;
    }

    /**
     * Ordered by match date, descending
     * @return 
     */
    public static List<Bet> getActiveBets() {
        try {
            Query q = session.createQuery(
                "from Bet b "
                    + "where b.flStatus = :flStatus "
                    + "order by b.matchDate desc "
            );
            q.setInteger("flStatus", BetStatus.ACTIVE.getCode());
            List<Bet> list = q.list();
            return list;
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to retrieve active bets", "Failed to retrieve active bets", e);
        }
    }

    public static List<Provider> getFixtureProviders(Team team) {
        try {
            Query q = session.createQuery(
                "select distinct p from Provider p "
                    + "where  p.flType = :type "
                    + "and    p.sport = :sport "
                    + "and    p.country = :country"
            );
            q.setInteger("type", ProviderType.FIXTURES.getCode());
            q.setString("sport", team.getSport());
            q.setString("country", team.getCountry());
            List<Provider> list = q.list();
            return list;
        } catch (HibernateException e) {
            throw new BettingRuntimeException("<html>Failed to determine list of suitable fixtures providers for team:<br/>" + team.toHtml() + "</html>", "Failed to determine list of suitable fixtures providers for team: " + team.toStringForException(), e);
        }
    }

    /**
     * 
     * @return teams that are currently flagged as active (FL_ACTIVE = 1)
     */
    public static List<Team> getActiveTeams() {
        try {
            Query q = session.createQuery(
                    "from Team t "
                    + "where t.flStatus = :flStatus ");
            q.setInteger("flStatus", TeamStatus.ACTIVE.getCode());
            return q.list();
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to retrieve active teams", "Failed to retrieve active teams", e);
        }
    }

    public static void deleteBet(Bet bet) {
        boolean wasActive = begin();
        try {
            session.delete(bet);
            if(!wasActive) {
                commit();
            }
            setRemoved(Bet.class, true);
        } catch (HibernateException e) {
            if(!wasActive) {
                rollback();
            }
            throw new BettingRuntimeException("<html>Failed to delete bet:<br/>" + bet.toHtml() + "</html>", "Failed to delete bet: " + bet.toStringForException(), e);
        }
        generalUpdate = true;
    }

    public static void refresh(DBObject obj) {
        try {
            session.refresh(obj);
        } catch (HibernateException e) {
            throw new BettingRuntimeException("<html>Failed to refresh " + obj.getClass().getSimpleName() + " state for:<br/>" + obj.toHtml() + "</html>", "Failed to refresh state for: " + obj.toStringForException(), e);
        }
    }

    public static List<Bet> getAllBets() {
        try {
            Query q = session.createQuery(
                    "from Bet b ");
            return q.list();
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to retrieve all bets", "Failed to retrieve all bets", e);
        }
    }

    public static Match getLastMatch(Team team) {
        try {
            Query q = session.createQuery(
                    "from Match m "
                    + "where m.id.homeTeam.id = :teamId "
                    + "or    m.id.awayTeam.id = :teamId "
                    + "order by m.id.date desc ");
            q.setInteger("teamId", team.getId());
            List<Match> list = q.list();
            return list.isEmpty() ? null : list.get(0);
        } catch (HibernateException e) {
            throw new BettingRuntimeException("<html>Failed to get last match for team:<br/>" + team.toHtml() + "</html>", "Failed to get last match for team: " + team.toStringForException(), e);
        }
    }

    public static Match getMatchForBet(Bet bet) {
        try {
            Timestamp dayPrecisionDate = Utils.getTimestampAtDayPrecision(bet.getMatchDate());
            MatchId id = new MatchId(dayPrecisionDate, bet.getHomeTeam(), bet.getAwayTeam());
            return get(Match.class, id);
        } catch (BettingRuntimeException e) {
            throw new BettingRuntimeException("<html>Failed to retrieve match for bet:<br/>" + bet.toHtml() + "</html>", "Failed to retrieve match for bet: " + bet.toStringForException(), e);
        }
    }
    
    /**
     * 
     * @return list of all sports from Providers table
     */
    public static List<String> getAllSportsFromProviders() {
        try {
            Query q = session.createQuery(
                    "select distinct p.sport "
                    + "from Provider p ");
            return q.list();
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to retrieve sports for providers", "Failed to retrieve sports for providers", e);
        }
    }
    
    /**
     * 
     * @return list of all sports from Teams table
     */
    public static List<String> getAllSportsFromTeams() {
        try {
            Query q = session.createQuery(
                "select distinct t.sport "
                    + "from Team t "
            );
            return q.list();
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to retrieve sports for teams", "Failed to retrieve sports for teams", e);
        }
    }
    
    public static Team getTeam(String name, String sport, String country) {
        try {
            Query q = session.createQuery(
                    "from Team t "
                    + "where t.name    = :name "
                    + "and   t.sport   = :sport "
                    + "and   t.country = :country ");
            q.setString("name", name);
            q.setString("sport", sport);
            q.setString("country", country);
            return (Team) q.uniqueResult();
        } catch (HibernateException e) {
            throw new BettingRuntimeException("<html>Failed to load team based on:<br/>"
                    + "Name: " + name + "<br/>"
                    + "Sport: " + sport + "<br/>"
                    + "Country: " + country + "</html>", "Failed to get unique team based on name=" + name + ", sport=" + sport + ", country=" + country, e);
        }
    }
    
    /**
     * 
     * @return list of all leagues from Providers table
     */
    public static List<String> getAllLeaguesFromProviders() {
        try {
            Query q = session.createQuery(
                "select distinct p.league "
                    + "from Provider p "
            );
            return q.list();
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to retrieve leagues for providers", "Failed to retrieve leagues for providers", e);
        }
    }
    
    /**
     * 
     * @return list of all leagues from Teams table
     */
    public static List<String> getAllLeaguesFromTeams() {
        try {
            Query q = session.createQuery(
                "select distinct t.league "
                    + "from Team t ");
            return q.list();
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to retrieve leagues for teams", "Failed to retrieve leagues for teams", e);
        }
    }

    public static List<String> getAllSeasonsFromProviders() {
        try {
            Query q = session.createQuery(
                    "select distinct p.season "
                    + "from Provider p ");
            return q.list();
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to retrieve seasons for providers", "Failed to retrieve seasons for providers", e);
        }
    }
    
    public static List<Team> getAllDeletedTeams() {
        try {
            Query q = session.createQuery(
                    "from Team t where t.flStatus = :flStatus");
            q.setInteger("flStatus", TeamStatus.DELETED.getCode());
            return q.list();
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to retrieve list of deleted teams", "Failed to retrieve list of deleted teams", e);
        }
    }
    
    public static List<Team> getAllTeams() {
        try {
            Query q = session.createQuery(
                "from Team t "
            );
            return q.list();
        } catch (HibernateException e) {
            throw new BettingRuntimeException("Failed to retrieve list of all teams", "Failed to retrieve list of all teams", e);
        }
    }

    public static List<Team> getAllTeams(String sport, String country, String league) {
        try {
            Query q = session.createQuery(
                    "from Team t "
                    + "where t.sport    = :sport "
                    + "and   t.country   = :country "
                    + "and   t.league = :league ");
            q.setString("sport", sport);
            q.setString("country", country);
            q.setString("league", league);
            return q.list();
        } catch (HibernateException e) {
            throw new BettingRuntimeException("<html>Failed to load team based on:<br/>"
                    + "Sport: " + sport + "<br/>"
                    + "Country: " + country + "<br/>"
                    + "League: " + league + "</html>", "Failed to get unique team based on sport=" + sport + ", country=" + country + ", league=" + league, e);
        }
    }

    public static void deleteTeam(Team team) {
        TeamStatus origStatus = team.getStatus();
        team.setStatus(TeamStatus.DELETED);
        try {
            saveOrUpdateTeam(team);
        } catch (BettingRuntimeException e) {
            throw new BettingRuntimeException("<html>Failed to move team to the list of deleted teams. Team:<br/>" + team.toHtml() + "</html>", "Failed to update team with DELETED flag: " + team.toStringForException() + " Flag before was: " + origStatus, e);
        }
    }

    public static void restoreTeam(Team team) {
        TeamStatus origStatus = team.getStatus();
        if(team.hasActiveBets() || team.hasIncompleteChain()) {
            team.setStatus(TeamStatus.ACTIVE);
        } else {
            team.setStatus(TeamStatus.REGULAR);
        }
        try {
            saveOrUpdateTeam(team);
        } catch (BettingRuntimeException e) {
            throw new BettingRuntimeException("<html>Failed to restore team:<br/>" + team.toHtml() + "</html>", "Failed to update team with ACTIVE/REGULAR flag: " + team.toStringForException() + " Flag before was: " + origStatus, e);
        }
    }
}