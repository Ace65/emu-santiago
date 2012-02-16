package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import commons.database.DatabaseFactory;
import gameserver.dao.AionArenaDAO;
import gameserver.model.AionArenaTeam;

/**
 * @author Travis Stark / adapted DESTRUCTOR/HADESH
 * 
 */
public class MySQL5AionArenaDAO extends AionArenaDAO
{    
	private static final Logger	log				= Logger.getLogger(MySQL5AionArenaDAO.class);
    
	@Override
	public AionArenaTeam getArenaTeamById(int teamId)
	{
		AionArenaTeam team = null;
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM arena_teams WHERE id = ?");
			stmt.setInt(1, teamId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
			{
				team = new AionArenaTeam(teamId, rs.getShort("type"), rs.getString("players"));
				team.setTeamPoints(rs.getInt("points"));
				team.setTeamName(rs.getString("name"));
			}
		}
		catch(Exception e)
		{
			log.error(e);
			team = null;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return team;
	}
	
	@Override
	public List<AionArenaTeam> getAllTeams()
	{
		List<AionArenaTeam> teams = null;
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM arena_teams");
			ResultSet rs = stmt.executeQuery();
			teams = new ArrayList<AionArenaTeam>();
			while(rs.next())
			{
				AionArenaTeam team = new AionArenaTeam(rs.getInt("id"), rs.getShort("type"), rs.getString("players"));
				team.setTeamPoints(rs.getInt("points"));
				team.setTeamName(rs.getString("name"));
				teams.add(team);
			}
		}
		catch(Exception e)
		{
			log.error(e);
			teams = null;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return teams;
	}
	
	@Override
    public List<AionArenaTeam> getTeamsRank(short TeamType)
    {
        List<AionArenaTeam> teams = null;
        Connection con = null;
        try
        {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM arena_teams WHERE type = ? ORDER BY points DESC LIMIT 10");
            stmt.setShort(1, TeamType);
            ResultSet rs = stmt.executeQuery();
            teams = new ArrayList<AionArenaTeam>();
            while(rs.next())
            {
                AionArenaTeam team = new AionArenaTeam(rs.getInt("id"), rs.getShort("type"), rs.getString("players"));
                team.setTeamPoints(rs.getInt("points"));
                team.setTeamName(rs.getString("name"));
                teams.add(team);
            }
        }
        catch(Exception e)
        {
            log.error(e);
            teams = null;
        }
        finally
        {
            DatabaseFactory.close(con);
        }
        return teams;
    }
	
	@Override
	public void saveArenaTeams(List<AionArenaTeam> teams)
	{
		Connection con = null;
		try
		{
			for(int i=0; i < teams.size(); i++)
			{
				AionArenaTeam team = teams.get(i);
				con = DatabaseFactory.getConnection();
				PreparedStatement stmt = con.prepareStatement("REPLACE INTO arena_teams(id,players,points,type,name) VALUES(?,?,?,?,?)");
				stmt.setInt(1, team.getTeamId());
				String players = "";
				for(int j=0; j < team.getPlayersOids().size(); j++)
				{
					players += team.getPlayersOids().get(j);
					if(j != team.getPlayersOids().size() - 1)
						players += ",";
				}
				stmt.setString(2, players);
				stmt.setInt(3, team.getTeamPoints());
				stmt.setShort(4, team.getType());
				stmt.setString(5, team.getTeamName());
				stmt.execute();
			}
			log.info("Saved " + teams.size() + " Arena teams");
		}
		catch(Exception e)
		{
			log.error(e);
			teams = null;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
    @Override
    public void deleteArenaTeam(AionArenaTeam team)
    {
        Connection con = null;
        try
        {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM arena_teams WHERE id = ?");
            stmt.setInt(1, team.getTeamId());
            stmt.execute();
        }
        catch(Exception e)
        {
            log.error(e);
        }
        finally
        {
            DatabaseFactory.close(con);
        }
    }
    
    @Override
    public void renameArenaTeam(AionArenaTeam team, String name)
    {
        Connection con = null;
        try
        {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("UPDATE arena_teams SET name=? WHERE id = ?");
            stmt.setString(1, name);
            stmt.setInt(2, team.getTeamId());
            stmt.execute();
        }
        catch(Exception e)
        {
            log.error(e);
        }
        finally
        {
            DatabaseFactory.close(con);
        }
    }
    
    
    /** 
     * {@inheritDoc} 
     */
    @Override
    public boolean supports(String databaseName, int majorVersion, int minorVersion)
    {
        return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
    }
}
