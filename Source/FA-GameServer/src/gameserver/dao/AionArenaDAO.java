package gameserver.dao;

import java.util.List;

import commons.database.dao.DAO;
import gameserver.model.AionArenaTeam;

/**
 * @author Travis Stark / adapted DESTRUCTOR/HADESH
 *
 */
public abstract class AionArenaDAO implements DAO
{

	@Override
	public String getClassName()
	{
		return AionArenaDAO.class.getName();
	}
	
	public abstract List<AionArenaTeam> getAllTeams();
	
	public abstract List<AionArenaTeam> getTeamsRank(short TeamType);
	
	public abstract AionArenaTeam getArenaTeamById(int teamId);
	
	public abstract void saveArenaTeams(List<AionArenaTeam> team);
	
    public abstract void deleteArenaTeam(AionArenaTeam team);
    
    public abstract void renameArenaTeam(AionArenaTeam team, String name);
	
}
