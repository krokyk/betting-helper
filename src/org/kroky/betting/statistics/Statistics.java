/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.kroky.betting.common.enums.ProviderType;
import org.kroky.betting.common.util.Utils;
import org.kroky.betting.db.objects.Provider;
import org.kroky.betting.db.objects.Team;
import org.kroky.betting.parsers.ParseResult;
import org.kroky.betting.parsers.impl.BetExplorerResultsHTMLParser;

/**
 *
 * @author Peter Krokavec
 */
public class Statistics {

    private static final Logger LOG = Logger.getLogger(Statistics.class);

    public static void main(String[] args) throws Exception {
        List<String> urls = FileUtils.readLines(new File("urls.txt"));
        StringBuilder resultFileContent = new StringBuilder();
        BetExplorerResultsHTMLParser parser = new BetExplorerResultsHTMLParser();
        HashSet<Team2> teams = new HashSet<Team2>();
        for (String url : urls) {
            resultFileContent.append(url).append("\n");
            Provider p = new Provider(null, null, null, url, null, ProviderType.RESULTS);
//            http://www.betexplorer.com/soccer/argentina/primera-b-nacional-2012-2013/results/
//            http://www.betexplorer.com/soccer/angola/girabola-2014/results/
//            http://www.betexplorer.com/soccer/mexico/primera-division-2014-2015/results/?stage=lUlzipNe
//            http://www.betexplorer.com/soccer/mexico/primera-division-2014-2015/results/?stage=2Xhvj4x2
//            http://www.betexplorer.com/soccer/mexico/primera-division-2014-2015/results/?stage=8feVEmF2
//            http://www.betexplorer.com/soccer/mexico/primera-division-2014-2015/results/?stage=nofZD7U8
//            http://www.betexplorer.com/soccer/algeria/division-1-2014-2015/results/
            String[] split = url.split("soccer/")[1].split("/");
            String country = split[0];
            Pattern pat = Pattern.compile("(.+)([\\d]{4}-[\\d]{4})");
            Matcher mat = pat.matcher(split[1]);
            if (!mat.matches()) {
                pat = Pattern.compile("(.+)([\\d]{4})");
                mat = pat.matcher(split[1]);
                mat.matches();
            }

            String league = mat.group(1);
            String season = mat.group(2);
            String stage = "";
            if (split.length == 4) {
                stage = "!" + split[3].split("=")[1];
            }
            String filename = country + "!" + league + season + stage + ".html";
            File file = new File(filename);
            final String html;
            if (file.exists()) {
                html = FileUtils.readFileToString(file);
                LOG.info("Got HTML from file: " + file);
            } else {
                html = Utils.getHtmlFromUrl(url);
                FileUtils.write(file, html);
                LOG.info("Got HTML from url: " + url);
            }

            BufferedReader br = new BufferedReader(new StringReader(html));
            String line;
            Pattern pattern = Pattern.compile(".*&raquo;.*<a href=\"[^>]+>([^<]+)</a>");
            stop:
            while ((line = br.readLine()) != null) {
                Matcher m = pattern.matcher(line);
                if (m.matches()) {
                    p.setSport(m.group(1));
                    while ((line = br.readLine()) != null) {
                        m = pattern.matcher(line);
                        if (m.matches()) {
                            p.setCountry(m.group(1));
                            while ((line = br.readLine()) != null) {
                                m = pattern.matcher(line);
                                if (m.matches()) {
                                    String str = m.group(1);
                                    pattern = Pattern.compile("(.+)([\\d]{4}/[\\d]{4})");
                                    m = pattern.matcher(str);
                                    if (!m.matches()) {
                                        pattern = Pattern.compile("(.+)([\\d]{4})");
                                        m = pattern.matcher(str);
                                    }
                                    p.setLeague(m.group(1).trim());
                                    p.setSeason(m.group(2).trim());
                                    break stop;
                                }
                            }
                        }
                    }
                }
            }
            StringBuilder sb = new StringBuilder(p.getControlText());
            sb.append("\n").append(html);
            List<ParseResult> currentResults = new ArrayList<ParseResult>(parser.parse(sb.toString()));
            if (!Utils.isEmpty(currentResults)) {
                for (ParseResult r : currentResults) {
                    Team2 t = new Team2();
                    t.setName(r.getHomeTeamName());
                    t.setCountry(p.getCountry());
                    t.setLeague(p.getLeague());
                    t.setSeason(p.getSeason());
                    boolean found = false;
                    for (Team2 team : teams) {
                        if (team.equals(t)) {
                            team.setTotalMatches(team.getTotalMatches() + 1);
                            if (r.getHomeGoals().equals(r.getAwayGoals())) {
                                team.setDraws(team.getDraws() + 1);
                            }
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        t.setTotalMatches(t.getTotalMatches() + 1);
                        if (r.getHomeGoals().equals(r.getAwayGoals())) {
                            t.setDraws(t.getDraws() + 1);
                        }
                        teams.add(t);
                    }
                    //second team
                    t = new Team2();
                    t.setName(r.getAwayTeamName());
                    t.setCountry(p.getCountry());
                    t.setLeague(p.getLeague());
                    t.setSeason(p.getSeason());
                    found = false;
                    for (Team2 team : teams) {
                        if (team.equals(t)) {
                            team.setTotalMatches(team.getTotalMatches() + 1);
                            if (r.getHomeGoals().equals(r.getAwayGoals())) {
                                team.setDraws(team.getDraws() + 1);
                            }
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        t.setTotalMatches(t.getTotalMatches() + 1);
                        if (r.getHomeGoals().equals(r.getAwayGoals())) {
                            t.setDraws(t.getDraws() + 1);
                        }
                        teams.add(t);
                    }
                }
            }
        }
        resultFileContent.append("*****************************************\n");
        for (Team2 team : teams) {
            resultFileContent.append(team.getCountry()).append("\t");
            resultFileContent.append(team.getLeague()).append("\t");
            resultFileContent.append(team.getSeason()).append("\t");
            resultFileContent.append(team.getName()).append("\t");
            resultFileContent.append(team.getTotalMatches()).append("\t");
            resultFileContent.append(team.getDraws()).append("\n");
        }
        String filename = "results_" + System.currentTimeMillis() + ".txt";
        File file = new File(filename);
        FileUtils.write(file, resultFileContent);
    }

    public static class Team2 extends Team {

        private String season;
        private int totalMatches = 0;
        private int draws = 0;

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public int getTotalMatches() {
            return totalMatches;
        }

        public void setTotalMatches(int totalMatches) {
            this.totalMatches = totalMatches;
        }

        public int getDraws() {
            return draws;
        }

        public void setDraws(int draws) {
            this.draws = draws;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj instanceof Team2) {
                Team2 t = (Team2) obj;
                return t.getName().equals(this.getName()) && t.getCountry().equals(this.getCountry())
                        && t.getLeague().equals(this.getLeague()) && t.getSeason().equals(this.getSeason());
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            return hash;
        }

    }
}
