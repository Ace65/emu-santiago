package gameserver.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

/**
 * @author Travis Stark / adapted DESTRUCTOR/HADESH
 *
 */
public class AionArenaTeam
{
	private short type;
	private int teamId;
	
	private List<Integer> playersOids;
	
	private int ArenaTeamPoints = 0;
	
	private String teamName;
	
	public AionArenaTeam(int teamId, short type, String playerOidsStr)
	{
		this.teamId = teamId;
		this.type = type;
		this.playersOids = Collections.synchronizedList(new ArrayList<Integer>());
		for(String playerOid : playerOidsStr.split(","))
		{
			playersOids.add(Integer.parseInt(playerOid));
		}
	}
	
	public void broadcastToArenaTeam(String message)
	{
		for(int i=0; i < playersOids.size(); i++)
		{
			Player p = World.getInstance().findPlayer(playersOids.get(i));
			PacketSendUtility.sendPacket(p, new SM_MESSAGE(0, null, message, ChatType.SYSTEM_NOTICE));
		}
	}
	
	public static void broadcastToArenaPlayers(List<Player> players, String message)
    {
        for(Player p: players)
        {
            PacketSendUtility.sendPacket(p, new SM_MESSAGE(0, null, message, ChatType.SYSTEM_NOTICE));
        }
    }

	/**
	 * @return the type
	 */
	public short getType()
	{
		return type;
	}

	public int getTeamId()
	{
		return teamId;
	}

	public List<Integer> getPlayersOids()
	{
		return playersOids;
	}
	
	public void addPlayer(Integer pid)
	{
		playersOids.add(pid);
	}
	
	public void removePlayer(Integer pid)
    {
        playersOids.remove(pid);
    }

	/**
	 * @return the ArenaTeamPoints
	 */
	public int getTeamPoints()
	{
		return ArenaTeamPoints;
	}

	/**
	 * @param ArenaTeamPoints the ArenaTeamPoints to set
	 */
	public void setTeamPoints(int ArenaTeamPoints)
	{
		this.ArenaTeamPoints = ArenaTeamPoints;
	}

	/**
	 * @return the teamName
	 */
	public String getTeamName()
	{
		return teamName;
	}

	/**
	 * @param teamName the teamName to set
	 */
	public void setTeamName(String teamName)
	{
		this.teamName = teamName;
	}
	
}
