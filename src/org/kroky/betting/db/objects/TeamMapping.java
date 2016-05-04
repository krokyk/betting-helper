/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.db.objects;

import org.kroky.betting.db.objects.compositeIds.TeamMappingId;

/**
 *
 * @author Kroky
 */
public class TeamMapping extends DBObject {
    private TeamMappingId id;
    private Team team;
    
    private transient String league;
    
    public TeamMapping() {};

    public TeamMapping(TeamMappingId id, Team team) {
        this.id = id;
        this.team = team;
        this.league = team.getLeague();
    }
    
    public TeamMapping(TeamMappingId id, String league, String teamName) {
        this.id = id;
        this.team = new Team(teamName, id.getSport(), id.getCountry(), league);
        this.league = league;
    }
    
    /**
     * USE ONLY WHEN ALSO CREATING TEAM BECAUSE THERE'S NO ID HERE
     * @param externalName
     * @param sport
     * @param country
     * @param internalName 
     */
    public TeamMapping(String externalName, String country, String sport, String league, String internalName) {
        this(new TeamMappingId(externalName, sport, country), league, internalName);
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public void setInternalName(String name) {
        team.setName(name);
    }
    
    public String getInternalName() {
        return team.getName();
    }

    public void setSport(String sport) {
        getId().setSport(sport);
    }

    public void setExternalName(String externalName) {
        getId().setExternalName(externalName);
    }

    public void setCountry(String country) {
        getId().setCountry(country);
    }

    public String getSport() {
        return getId().getSport();
    }

    public String getExternalName() {
        return getId().getExternalName();
    }

    public String getCountry() {
        return getId().getCountry();
    }

    public TeamMappingId getId() {
        return id;
    }

    public void setId(TeamMappingId id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TeamMapping)) {
            return false;
        }
        final TeamMapping other = (TeamMapping) obj;
        if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "TeamMapping{" + "id=" + getId() + ", team=" + getTeam().getName() + ", league=" + getLeague() + '}';
    }

    @Override
    public String toStringForException() {
        return toString();
    }

    @Override
    public String toHtml() {
        StringBuilder sb = new StringBuilder("External name: ").append(getId().getExternalName()).append("<br/>")
                .append("Internal name: ").append(getInternalName()).append("<br/>")
                .append("Country: ").append(getCountry()).append("<br/>")
                .append("League: ").append(getLeague()).append("<br/>");
        return sb.toString();
    }
}
