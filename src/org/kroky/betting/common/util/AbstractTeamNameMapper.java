/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.common.util;

import java.util.Map;
import org.apache.log4j.Logger;
import org.kroky.betting.db.DAO;
import static org.kroky.betting.db.DBState.*;
import org.kroky.betting.db.objects.Team;
import org.kroky.betting.db.objects.TeamMapping;
import org.kroky.betting.db.objects.compositeIds.TeamMappingId;

/**
 *
 * @author Kroky
 */
abstract class AbstractTeamNameMapper {
    private static final Logger LOG = org.apache.log4j.Logger.getLogger(AbstractTeamNameMapper.class);
    private Map<TeamMappingId, Team> mappings;
    
    //To trigger initialization
    static {
        setAdded(TeamMapping.class, true);
        setUpdated(TeamMapping.class, true);
    }
    
    protected AbstractTeamNameMapper() {}
    
    protected Map<TeamMappingId, Team> getMappings() {
        if(isAdded(TeamMapping.class) || isUpdated(TeamMapping.class)) {
            LOG.debug("Updating mappings from DB...");
            mappings = DAO.getTeamNameMappings();
            printMappings();
            setAdded(TeamMapping.class, false);
            setUpdated(TeamMapping.class, false);
        }
        return mappings;
    }
    
    private void printMappings() {
        LOG.debug("\nCurrent mappings:\n" + 
                  Utils.mapToString(mappings));
    }
}
