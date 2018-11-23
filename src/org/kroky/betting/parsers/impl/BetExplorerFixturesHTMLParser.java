/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.parsers.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.kroky.betting.common.util.Utils;
import org.kroky.betting.db.objects.Provider;
import org.kroky.betting.parsers.AbstractParser;
import org.kroky.betting.parsers.ParseResult;
import org.kroky.betting.parsers.Parser;

/**
 *
 * @author Kroky
 */
public class BetExplorerFixturesHTMLParser extends AbstractParser {

    private static final Logger LOG = org.apache.log4j.Logger.getLogger(BetExplorerFixturesHTMLParser.class);
    /*
     <table class="result-table league-fixtures lmbzero" cellspacing="0">
     <tr class="rtitle first-row"><th class="left first-cell nobr" colspan="2">19. Round</th><th class="tv">&nbsp;</th><th class="bs">B's</th><th>1</th><th>X</th><th class="last-cell nobr">2</th></tr>
     <tr class="match-line first-row"><td class="first-cell date tl">12.08.2015 02:30</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/san-martin-s-j-lanus/OtbCrt41/">San Martin S.J. - Lanus</a></td><td class="tv">&nbsp;</td><td class="bs">21</td><td class="odds"><span><a href="/my_selections.php?action=3&amp;matchid=OtbCrt41&amp;outcomeid=2244qxv464x0x48p8f&amp;otheroutcomes=2244qxv498x0x0,2244qxv464x0x48p8g" onclick="return my_selections_click(this);" title="Add to My Selections" target="mySelections" class="mySelectionsTip" data-odd="2.78"></a></span></td><td class="odds"><span><a href="/my_selections.php?action=3&amp;matchid=OtbCrt41&amp;outcomeid=2244qxv498x0x0&amp;otheroutcomes=2244qxv464x0x48p8f,2244qxv464x0x48p8g" onclick="return my_selections_click(this);" title="Add to My Selections" target="mySelections" class="mySelectionsTip" data-odd="2.95"></a></span></td><td class="odds nobr last-cell"><span><a href="/my_selections.php?action=3&amp;matchid=OtbCrt41&amp;outcomeid=2244qxv464x0x48p8g&amp;otheroutcomes=2244qxv464x0x48p8f,2244qxv498x0x0" onclick="return my_selections_click(this);" title="Add to My Selections" target="mySelections" class="mySelectionsTip" data-odd="2.59"></a></span></td></tr>
     <tr class="rtitle"><th class="left first-cell nobr" colspan="2">20. Round</th><th class="tv">&nbsp;</th><th class="bs">B's</th><th>1</th><th>X</th><th class="last-cell nobr">2</th></tr>
     <tr class="match-line first-row"><td class="first-cell date tl">14.08.2015 22:00</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/belgrano-tigre/bXkNdWOB/">Belgrano - Tigre</a></td><td class="tv"><img src="/res/img/icon-tv.gif" class="streamsicon" data-eid="bXkNdWOB" /><div class="streamsdiv" id="streams-bXkNdWOB" data-eid="bXkNdWOB"><div class="head">Live streams</div><div class="body"><a href="/bookmaker/148/http://ads2.williamhill.com/redirect.aspx?pid=11753379&bid=66906317&lpid=13510190" target="_blank">William Hill</a><br /></div></div></td><td class="bs">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds nobr last-cell">&nbsp;</td></tr>
     <tr class="match-line strong"><td class="first-cell date tl">&nbsp;</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/independiente-defensa-y-justicia/G8MmDi9h/">Independiente - Defensa y Justicia</a></td><td class="tv"><img src="/res/img/icon-tv.gif" class="streamsicon" data-eid="G8MmDi9h" /><div class="streamsdiv" id="streams-G8MmDi9h" data-eid="G8MmDi9h"><div class="head">Live streams</div><div class="body"><a href="/bookmaker/148/http://ads2.williamhill.com/redirect.aspx?pid=11753379&bid=66906317&lpid=13510190" target="_blank">William Hill</a><br /></div></div></td><td class="bs">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds nobr last-cell">&nbsp;</td></tr>
     <tr class="match-line"><td class="first-cell date tl">&nbsp;</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/sarmiento-junin-olimpo-bahia-blanca/4UhVfAgO/">Sarmiento Junin - Olimpo Bahia Blanca</a></td><td class="tv"><img src="/res/img/icon-tv.gif" class="streamsicon" data-eid="4UhVfAgO" /><div class="streamsdiv" id="streams-4UhVfAgO" data-eid="4UhVfAgO"><div class="head">Live streams</div><div class="body"><a href="/bookmaker/148/http://ads2.williamhill.com/redirect.aspx?pid=11753379&bid=66906317&lpid=13510190" target="_blank">William Hill</a><br /></div></div></td><td class="bs">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds nobr last-cell">&nbsp;</td></tr>
     <tr class="match-line strong"><td class="first-cell date tl">15.08.2015 22:00</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/aldosivi-colon-santa-fe/MoNqEXgn/">Aldosivi - Colon Santa FE</a></td><td class="tv"><img src="/res/img/icon-tv.gif" class="streamsicon" data-eid="MoNqEXgn" /><div class="streamsdiv" id="streams-MoNqEXgn" data-eid="MoNqEXgn"><div class="head">Live streams</div><div class="body"><a href="/bookmaker/148/http://ads2.williamhill.com/redirect.aspx?pid=11753379&bid=66906317&lpid=13510190" target="_blank">William Hill</a><br /></div></div></td><td class="bs">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds nobr last-cell">&nbsp;</td></tr>
     <tr class="match-line"><td class="first-cell date tl">&nbsp;</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/godoy-cruz-banfield/8rcAaYvg/">Godoy Cruz - Banfield</a></td><td class="tv"><img src="/res/img/icon-tv.gif" class="streamsicon" data-eid="8rcAaYvg" /><div class="streamsdiv" id="streams-8rcAaYvg" data-eid="8rcAaYvg"><div class="head">Live streams</div><div class="body"><a href="/bookmaker/148/http://ads2.williamhill.com/redirect.aspx?pid=11753379&bid=66906317&lpid=13510190" target="_blank">William Hill</a><br /></div></div></td><td class="bs">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds nobr last-cell">&nbsp;</td></tr>
     <tr class="match-line strong"><td class="first-cell date tl">&nbsp;</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/lanus-estudiantes-l-p/CzGdBVw5/">Lanus - Estudiantes L.P.</a></td><td class="tv"><img src="/res/img/icon-tv.gif" class="streamsicon" data-eid="CzGdBVw5" /><div class="streamsdiv" id="streams-CzGdBVw5" data-eid="CzGdBVw5"><div class="head">Live streams</div><div class="body"><a href="/bookmaker/148/http://ads2.williamhill.com/redirect.aspx?pid=11753379&bid=66906317&lpid=13510190" target="_blank">William Hill</a><br /></div></div></td><td class="bs">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds nobr last-cell">&nbsp;</td></tr>
     <tr class="match-line"><td class="first-cell date tl">&nbsp;</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/nueva-chicago-atl-huracan/jRE0AkgB/">Nueva Chicago - Atl. Huracan</a></td><td class="tv"><img src="/res/img/icon-tv.gif" class="streamsicon" data-eid="jRE0AkgB" /><div class="streamsdiv" id="streams-jRE0AkgB" data-eid="jRE0AkgB"><div class="head">Live streams</div><div class="body"><a href="/bookmaker/148/http://ads2.williamhill.com/redirect.aspx?pid=11753379&bid=66906317&lpid=13510190" target="_blank">William Hill</a><br /></div></div></td><td class="bs">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds nobr last-cell">&nbsp;</td></tr>
     <tr class="match-line strong"><td class="first-cell date tl">&nbsp;</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/union-de-santa-fe-racing-club/G80IcC95/">Union de Santa Fe - Racing Club</a></td><td class="tv"><img src="/res/img/icon-tv.gif" class="streamsicon" data-eid="G80IcC95" /><div class="streamsdiv" id="streams-G80IcC95" data-eid="G80IcC95"><div class="head">Live streams</div><div class="body"><a href="/bookmaker/148/http://ads2.williamhill.com/redirect.aspx?pid=11753379&bid=66906317&lpid=13510190" target="_blank">William Hill</a><br /></div></div></td><td class="bs">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds nobr last-cell">&nbsp;</td></tr>
     <tr class="match-line"><td class="first-cell date tl">16.08.2015 22:00</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/arsenal-sarandi-boca-juniors/xI1Ebhfa/">Arsenal Sarandi - Boca Juniors</a></td><td class="tv"><img src="/res/img/icon-tv.gif" class="streamsicon" data-eid="xI1Ebhfa" /><div class="streamsdiv" id="streams-xI1Ebhfa" data-eid="xI1Ebhfa"><div class="head">Live streams</div><div class="body"><a href="/bookmaker/148/http://ads2.williamhill.com/redirect.aspx?pid=11753379&bid=66906317&lpid=13510190" target="_blank">William Hill</a><br /></div></div></td><td class="bs">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds nobr last-cell">&nbsp;</td></tr>
     <tr class="match-line strong"><td class="first-cell date tl">&nbsp;</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/gimnasia-l-p-argentinos-jrs/Uib60EPn/">Gimnasia L.P. - Argentinos Jrs</a></td><td class="tv"><img src="/res/img/icon-tv.gif" class="streamsicon" data-eid="Uib60EPn" /><div class="streamsdiv" id="streams-Uib60EPn" data-eid="Uib60EPn"><div class="head">Live streams</div><div class="body"><a href="/bookmaker/148/http://ads2.williamhill.com/redirect.aspx?pid=11753379&bid=66906317&lpid=13510190" target="_blank">William Hill</a><br /></div></div></td><td class="bs">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds nobr last-cell">&nbsp;</td></tr>
     <tr class="match-line"><td class="first-cell date tl">&nbsp;</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/newells-old-boys-temperley/rLiZgU8U/">Newells Old Boys - Temperley</a></td><td class="tv"><img src="/res/img/icon-tv.gif" class="streamsicon" data-eid="rLiZgU8U" /><div class="streamsdiv" id="streams-rLiZgU8U" data-eid="rLiZgU8U"><div class="head">Live streams</div><div class="body"><a href="/bookmaker/148/http://ads2.williamhill.com/redirect.aspx?pid=11753379&bid=66906317&lpid=13510190" target="_blank">William Hill</a><br /></div></div></td><td class="bs">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds nobr last-cell">&nbsp;</td></tr>
     <tr class="match-line strong"><td class="first-cell date tl">&nbsp;</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/san-lorenzo-crucero-del-norte/nT811fAt/">San Lorenzo - Crucero del Norte</a></td><td class="tv"><img src="/res/img/icon-tv.gif" class="streamsicon" data-eid="nT811fAt" /><div class="streamsdiv" id="streams-nT811fAt" data-eid="nT811fAt"><div class="head">Live streams</div><div class="body"><a href="/bookmaker/148/http://ads2.williamhill.com/redirect.aspx?pid=11753379&bid=66906317&lpid=13510190" target="_blank">William Hill</a><br /></div></div></td><td class="bs">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds nobr last-cell">&nbsp;</td></tr>
     <tr class="match-line"><td class="first-cell date tl">&nbsp;</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/velez-sarsfield-atl-rafaela/reOuFDvt/">Velez Sarsfield - Atl. Rafaela</a></td><td class="tv"><img src="/res/img/icon-tv.gif" class="streamsicon" data-eid="reOuFDvt" /><div class="streamsdiv" id="streams-reOuFDvt" data-eid="reOuFDvt"><div class="head">Live streams</div><div class="body"><a href="/bookmaker/148/http://ads2.williamhill.com/redirect.aspx?pid=11753379&bid=66906317&lpid=13510190" target="_blank">William Hill</a><br /></div></div></td><td class="bs">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds nobr last-cell">&nbsp;</td></tr>
     <tr class="match-line strong"><td class="first-cell date tl">17.08.2015 22:00</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/quilmes-rosario-central/A7gRejvI/">Quilmes - Rosario Central</a></td><td class="tv"><img src="/res/img/icon-tv.gif" class="streamsicon" data-eid="A7gRejvI" /><div class="streamsdiv" id="streams-A7gRejvI" data-eid="A7gRejvI"><div class="head">Live streams</div><div class="body"><a href="/bookmaker/148/http://ads2.williamhill.com/redirect.aspx?pid=11753379&bid=66906317&lpid=13510190" target="_blank">William Hill</a><br /></div></div></td><td class="bs">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds nobr last-cell">&nbsp;</td></tr>
     <tr class="match-line"><td class="first-cell date tl">&nbsp;</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/river-plate-san-martin-s-j/UBQiCBOb/">River Plate - San Martin S.J.</a></td><td class="tv"><img src="/res/img/icon-tv.gif" class="streamsicon" data-eid="UBQiCBOb" /><div class="streamsdiv" id="streams-UBQiCBOb" data-eid="UBQiCBOb"><div class="head">Live streams</div><div class="body"><a href="/bookmaker/148/http://ads2.williamhill.com/redirect.aspx?pid=11753379&bid=66906317&lpid=13510190" target="_blank">William Hill</a><br /></div></div></td><td class="bs">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds nobr last-cell">&nbsp;</td></tr>
     <tr class="rtitle"><th class="left first-cell nobr" colspan="2">21. Round</th><th class="tv">&nbsp;</th><th class="bs">B's</th><th>1</th><th>X</th><th class="last-cell nobr">2</th></tr>
     <tr class="match-line first-row"><td class="first-cell date tl">23.08.2015 22:00</td><td class="tl nobr"><a href="/soccer/argentina/primera-division/argentinos-jrs-san-lorenzo/WbB4vU7i/">Argentinos Jrs - San Lorenzo</a></td><td class="tv">&nbsp;</td><td class="bs">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds">&nbsp;</td><td class="odds nobr last-cell">&nbsp;</td></tr>
     */
    private static final Pattern PATTERN_DATE = Pattern.compile("class=\"table-main__datetime\">(\\d\\d\\.\\d\\d\\.\\d*|Today|Tomorrow) (\\d\\d:\\d\\d)</td>");
    //private static final Pattern PATTERN_DATE = Pattern.compile("class=\"table-main__datetime\">(\\d\\d\\.\\d\\d\\.\\d* \\d\\d:\\d\\d)</td>");
    private static final Pattern PATTERN_TEAMS = Pattern.compile("<a href=\"[^\"]*\" class=\"in-match\"><span>([^<]+)<[^<]+<span>([^<]+)<");

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public BetExplorerFixturesHTMLParser() {
        super("www.betexplorer.com Next Match (HTML)");
        LOG.debug("Using patterns: " + PATTERN_DATE.pattern() + " and " + PATTERN_TEAMS.pattern());
    }

    @Override
    public Set<ParseResult> parse(String text) {
        String message = "Parsing started";
        LOG.info(message);
        BufferedReader br = createBufferedReader(text);
        Set<ParseResult> results = new TreeSet<ParseResult>();
        int skipped = 0;
        int duplicates = 0;
        int lineCount = 0;
        try {
            String line;
            Provider currentProvider = null;
            String dateStr = null;
            while ((line = br.readLine()) != null) {
                lineCount++;
                setProgressBarValue(lineCount);
                Matcher matcher = PATTERN_DATE.matcher(line);
                Provider newProvider = getProvider(line);
                if (newProvider != null) {
                    currentProvider = newProvider;
                }
                if (matcher.find()) {
                    dateStr = matcher.group(1).trim();
                    continue;
                }
                matcher = PATTERN_TEAMS.matcher(line);
                if (matcher.find()) {
                    String homeTeamName = matcher.group(1).trim();
                    String awayTeamName = matcher.group(2).trim();
                    Timestamp date;
                    try {
                        date = new Timestamp(DATE_FORMAT.parse(dateStr).getTime());
                    } catch (ParseException ex) {
                        throw new Exception("Could not parse date: \"" + dateStr + "\" using formatter " + DATE_FORMAT.toPattern() + " and no previous date exist to be used instead. Current parsed line: " + line, ex);

                    }
                    String country = null;
                    String sport = null;
                    String league = null;
                    String season = null;
                    if (currentProvider != null) {
                        country = currentProvider.getCountry();
                        sport = currentProvider.getSport();
                        league = currentProvider.getLeague();
                        season = currentProvider.getSeason();
                    }
                    ParseResult result = new ParseResultImpl(date, homeTeamName, awayTeamName, country, sport, league, season);
                    if (date.before(Utils.now())) {
                        LOG.info("This result is in the past, skipping: " + result);
                        skipped++;
                        continue;
                    }
                    boolean change = results.add(result);
                    if (!change) {
                        duplicates++;
                        message = "Found duplicate: " + result;
                        LOG.debug(message);
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Error occured during parsing.", ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                //Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        message = lineCount + " lines, " + results.size() + " result(s), " + duplicates + " duplicates, " + skipped + " skipped";
        LOG.info(message);
        return results;
    }

    @Override
    public int compareTo(Parser o) {
        return super.compareTo(o);
    }

    @Override
    public Pattern getPattern() {
        return PATTERN_DATE;
    }
}
