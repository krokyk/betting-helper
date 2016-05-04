/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.common.util;

import org.kroky.betting.db.objects.Team;
import org.kroky.betting.db.objects.compositeIds.TeamMappingId;

/**
 *
 * @author pk2
 */
public class TeamNameMapper extends AbstractTeamNameMapper {
    private static final TeamNameMapper INSTANCE = new TeamNameMapper();
    
    private TeamNameMapper() {
        getMappings();
    }
    
    public Team getTeam(String externalName, String sport, String country) {
        return getTeam(new TeamMappingId(externalName, sport, country));
    }
    
    public Team getTeam(TeamMappingId id) {
        return getMappings().get(id);
    }
    
    public boolean containsMapping(String externalName, String sport, String country) {
        return containsMapping(new TeamMappingId(externalName, sport, country));
    }
    
    public boolean containsMapping(TeamMappingId id) {
        return getMappings().containsKey(id);
    }

    public static TeamNameMapper getInstance() {
        return INSTANCE;
    }
}
