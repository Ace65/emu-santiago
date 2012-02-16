package br.focus.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
import commons.database.dao.DAOManager;
import gameserver.dao.AionArenaDAO;
import gameserver.dao.PlayerDAO;
import gameserver.model.AionArenaTeam;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.idfactory.IDFactory;
import gameserver.world.World;

/**
 * @author Travis Stark / Holgrabus
 *
 */
public class AionArenaService
{
	
	private static Map<Integer,AionArenaTeam> arenaTeams = new HashMap<Integer,AionArenaTeam>();
	
	public static ArrayList<AionArenaTeam> TopTeams2v2 = new ArrayList<AionArenaTeam>();
	public static ArrayList<AionArenaTeam> TopTeams3v3 = new ArrayList<AionArenaTeam>();
	public static ArrayList<AionArenaTeam> TopTeams5v5 = new ArrayList<AionArenaTeam>();
	
	private static final Logger log = Logger.getLogger(AionArenaService.class);
	
	public static void initialize()
	{
		List<AionArenaTeam> databaseTeams = DAOManager.getDAO(AionArenaDAO.class).getAllTeams();
		for(AionArenaTeam team : databaseTeams)
		{
			arenaTeams.put(team.getTeamId(), team);
		}
		databaseTeams.clear();
		
		log.info("Started AionArenaService, loaded " + arenaTeams.size() + " teams.");
		
		ArenaManager.initialize();
	}
	
	public static void saveData()
	{
		DAOManager.getDAO(AionArenaDAO.class).saveArenaTeams(new ArrayList<AionArenaTeam>(arenaTeams.values()));
	}
	
	public static void reloadTopRank()
	{
		saveData();
		TopTeams2v2.clear();
		TopTeams2v2.addAll(DAOManager.getDAO(AionArenaDAO.class).getTeamsRank((short) 2));
		TopTeams3v3.clear();
		TopTeams3v3.addAll(DAOManager.getDAO(AionArenaDAO.class).getTeamsRank((short) 3));
		TopTeams5v5.clear();
		TopTeams5v5.addAll(DAOManager.getDAO(AionArenaDAO.class).getTeamsRank((short) 5));
	}
	
	public static void processTeamCreationRequest(Player creator, String teamName, short typeOffset, boolean okKinah)
	{
		if(creator == null)
			return;
		if(teamName.equals(""))
		{
			PacketSendUtility.sendMessage(creator, "You must enter a name for your team.");
			return;
		}
		if(typeOffset > 3 || typeOffset < 1)
		{
			PacketSendUtility.sendMessage(creator, "Invalid battle type.");
			return;
		}
		if(!okKinah)
		{
			PacketSendUtility.sendMessage(creator, "You must pay 1.000.000 kinah to create a team.");
			return;
		}
		if(creator.getInventory().getKinahCount() < 1000000)
		{
			PacketSendUtility.sendMessage(creator, "You don't have enough Kinah to create an Arena Team.");
			return;
		}
		
		short type;
		switch(typeOffset)
		{
			case 1: type = 2; break;
			case 2: type = 3; break;
			case 3: type = 5; break;
			default: type = 0; break;
		}
		
		if(type == 2 && creator.getArenaTeam2v2() != null)
		{
			PacketSendUtility.sendMessage(creator, "You are already member of a 2v2 team. Please register in another category.");
			return;
		}
		else if(type == 3 && creator.getArenaTeam3v3() != null)
		{
			PacketSendUtility.sendMessage(creator, "You are already member of a 3v3 team. Please register in another category.");
			return;
		}
		else if(type == 5 && creator.getArenaTeam5v5() != null)
		{
			PacketSendUtility.sendMessage(creator, "You are already member of a 5v5 team. Please register in another category.");
			return;
		}
		
		if(AionArenaService.isTeamNameExisting(teamName))
		{
			PacketSendUtility.sendMessage(creator, "The specified team name already exists. Please choose another one.");
			return;
		}
				
		creator.getInventory().decreaseKinah(1000000);
		
		AionArenaTeam team = createArenaTeam(type, creator, teamName);
		switch(type)
		{
			case 2: creator.setArenaTeam2v2(team); break;
			case 3: creator.setArenaTeam3v3(team); break;
			case 5: creator.setArenaTeam5v5(team); break;
			default: return;
		}
		PacketSendUtility.sendMessage(creator, "Congratulations ! Your new Arena team is now created.");
		PacketSendUtility.sendMessage(creator, "Name: " + team.getTeamName());
		saveData();
	}
	
	public static void invitePlayer(final Player inviter, final Player target, final short inviteType)
	{
	    if(!isLeaderInTeam(inviter,inviteType))
        {
            PacketSendUtility.sendMessage(inviter, "You are not leader of your team " + inviteType + "v" + inviteType + ".");
            return;
        }
	    
		if(inviter.getCommonData().getRace() != target.getCommonData().getRace())
		{
			PacketSendUtility.sendMessage(inviter, "You cannot invite a player from the opposite faction.");
			return;
		}
		
		final AionArenaTeam Team;
		switch(inviteType)
		{
			case 2: Team = inviter.getArenaTeam2v2(); break;
			case 3: Team = inviter.getArenaTeam3v3(); break;
			case 5: Team = inviter.getArenaTeam5v5(); break;
			default: Team = null; break;
		}
		
		if(Team == null)
		{
			PacketSendUtility.sendMessage(inviter, "You are not a member of such a team.");
			return;
		}
		
		if(Team.getPlayersOids().size() == inviteType)
        {
            PacketSendUtility.sendMessage(inviter, "You team is already full.");
            return;
        }
		
		AionArenaTeam targetTeam = null;
		switch(inviteType)
		{
			case 2: targetTeam = target.getArenaTeam2v2(); break;
			case 3: targetTeam = target.getArenaTeam3v3(); break;
			case 5: targetTeam = target.getArenaTeam5v5(); break;
		}
		
		if(targetTeam != null)
		{
			PacketSendUtility.sendMessage(inviter, "The target player is already a member of this type of team.");
			return;
		}
		
		String message = "Are you sure you want to invite " + target.getName() + " to your " + inviteType + "v" + inviteType + " Arena Team ?";
        RequestResponseHandler responseHandler = new RequestResponseHandler(inviter){

            public void acceptRequest(Creature requester, Player responder)
            {
                String message = inviter.getName() + " has invited you to join the " + inviteType + "v" + inviteType + " Arena Team : " + Team.getTeamName() +". Accept the invitation?";
                RequestResponseHandler responseHandler = new RequestResponseHandler(target){

                    public void acceptRequest(Creature requester, Player responder)
                    {
                        switch(inviteType)
                        {
                            case 2: target.setArenaTeam2v2(Team); break;
                            case 3: target.setArenaTeam3v3(Team); break;
                            case 5: target.setArenaTeam5v5(Team); break;
                        }
                        Team.addPlayer(target.getObjectId());
                        Team.broadcastToArenaTeam(target.getName() + " has joined the " + inviteType + "v" + inviteType + " Arena Team !");
                        return;
                    }

                    public void denyRequest(Creature requester, Player responder){ return; }
                };
                boolean requested = target.getResponseRequester().putRequest(902247, responseHandler);
                if(requested){PacketSendUtility.sendPacket(target, new SM_QUESTION_WINDOW(902247, 0, message));return;}
                return;
            }

            public void denyRequest(Creature requester, Player responder){ return; }
        };
        boolean requested = inviter.getResponseRequester().putRequest(902247, responseHandler);
        if(requested){PacketSendUtility.sendPacket(inviter, new SM_QUESTION_WINDOW(902247, 0, message));return;}
	}
	
	public static void removePlayer(final Player inviter, String targetName, final short inviteType)
    {
		final PlayerCommonData targetCommonData = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonDataByName(targetName);
	    if(!isLeaderInTeam(inviter,inviteType))
        {
            PacketSendUtility.sendMessage(inviter, "You are not leader of your " + inviteType + "v" + inviteType + " Arena Team.");
            return;
        }
        
        if(inviter.getCommonData().getRace() != targetCommonData.getRace())
        {
            PacketSendUtility.sendMessage(inviter, "You cannot invite a player from the opposite faction.");
            return;
        }
        
        final AionArenaTeam Team;
        switch(inviteType)
        {
            case 2: Team = inviter.getArenaTeam2v2(); break;
            case 3: Team = inviter.getArenaTeam3v3(); break;
            case 5: Team = inviter.getArenaTeam5v5(); break;
            default: Team = null; break;
        }
        
        if(Team == null)
        {
            PacketSendUtility.sendMessage(inviter, "You are not a member of such a team.");
            return;
        }
        
        if(!Team.getPlayersOids().contains(targetCommonData.getPlayerObjId()))
        {
            PacketSendUtility.sendMessage(inviter, "The target player is not a member of your team.");
            return;
        }
        
        else
        {
        	String message = "Are you sure you want to remove " + targetCommonData.getName() + " from your 2v2 Arena Team ?";
            RequestResponseHandler responseHandler = new RequestResponseHandler(inviter){

                public void acceptRequest(Creature requester, Player responder)
                {
                	Player target = World.getInstance().findPlayer(targetCommonData.getName());
                	if(target != null)
                	{
                		switch(inviteType)
                		{
                        	case 2: target.setArenaTeam2v2(null); break;
                        	case 3: target.setArenaTeam3v3(null); break;
                        	case 5: target.setArenaTeam5v5(null); break;
                		}
                	}
                    Team.removePlayer(targetCommonData.getPlayerObjId());
                    Team.broadcastToArenaTeam(targetCommonData.getName() + " has been removed from the " + inviteType + "v" + inviteType + " Arena Team !");
                    if(target != null)
                    	PacketSendUtility.sendMessage(target, "You have been removed from your " + inviteType + "v" + inviteType + " Arena Team !");
                    return;
                }

                public void denyRequest(Creature requester, Player responder){ return; }
            };
            boolean requested = inviter.getResponseRequester().putRequest(902247, responseHandler);
            if(requested){PacketSendUtility.sendPacket(inviter, new SM_QUESTION_WINDOW(902247, 0, message));return;}
        
        	saveData();
        }
    }
	
	public static void deleteTeam(final Player player, final short teamType)
    {
	    if(!isLeaderInTeam(player, teamType))
        {
            PacketSendUtility.sendMessage(player, "You are not leader of your team " + teamType + "v" + teamType + ".");
            return;
        }
	    AionArenaTeam team = null;
	    switch(teamType)
        {
            case 2: team = player.getArenaTeam2v2(); break;
            case 3: team = player.getArenaTeam3v3(); break;
            case 5: team = player.getArenaTeam5v5(); break;
        }
	    final AionArenaTeam Team = team;
	    
	    String message = "Are you sure you want to delete your " + teamType + "v" + teamType + " Arena Team : " + team.getTeamName() + " ?";
        RequestResponseHandler responseHandler = new RequestResponseHandler(player){

            public void acceptRequest(Creature requester, Player responder)
            {
                arenaTeams.remove(Team.getTeamId());
                DAOManager.getDAO(AionArenaDAO.class).deleteArenaTeam(Team);
                for(int playerId : Team.getPlayersOids())
                {
                	if(World.getInstance().findPlayer(playerId) != null)
                	{
                		switch(teamType)
                		{
                        	case 2: World.getInstance().findPlayer(playerId).setArenaTeam2v2(null); break;
                        	case 3: World.getInstance().findPlayer(playerId).setArenaTeam3v3(null); break;
                        	case 5: World.getInstance().findPlayer(playerId).setArenaTeam5v5(null); break;
                		}
                	}
                }
                Team.broadcastToArenaTeam("Your " + teamType + "v" + teamType + " Arena Team has been deleted by " + player.getName() + " !");
                return;
            }

            public void denyRequest(Creature requester, Player responder){ return; }
        };
        boolean requested = player.getResponseRequester().putRequest(902247, responseHandler);
        if(requested){PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(902247, 0, message));return;}
        saveData();
    }
	
	public static void renameTeam(Player player, short teamType, String teamName, boolean okKinah)
    {
	    if(!isLeaderInTeam(player, teamType))
        {
            PacketSendUtility.sendMessage(player, "You are not leader of your team " + teamType + "v" + teamType + ".");
            return;
        }
	    if(teamName.equals(""))
        {
            PacketSendUtility.sendMessage(player, "You must enter a new name for your team.");
            return;
        }
	    if(!okKinah)
        {
            PacketSendUtility.sendMessage(player, "You must pay 500.000 kinah to create a team.");
            return;
        }
        if(player.getInventory().getKinahCount() < 500000)
        {
            PacketSendUtility.sendMessage(player, "You don't have enough Kinah to rename an Arena Team.");
            return;
        }
        if(AionArenaService.isTeamNameExisting(teamName))
        {
            PacketSendUtility.sendMessage(player, "The specified team name already exists. Please choose another one.");
            return;
        }
        AionArenaTeam team = null;
        switch(teamType)
        {
            case 2: team = player.getArenaTeam2v2(); break;
            case 3: team = player.getArenaTeam3v3(); break;
            case 5: team = player.getArenaTeam5v5(); break;
        }
        team.setTeamName(teamName);
        DAOManager.getDAO(AionArenaDAO.class).renameArenaTeam(team, teamName);
        player.getInventory().decreaseKinah(500000);
        PacketSendUtility.sendMessage(player, "Your " + teamType + "v" + teamType + " Arena Team's name is now : " + teamName + " !");
    }
	
	public static void onPlayerLogin(Player player)
	{
		ArrayList<AionArenaTeam> teams = new ArrayList<AionArenaTeam>(arenaTeams.values());
		for(AionArenaTeam team : teams)
		{
			if(team.getPlayersOids().contains(player.getObjectId()))
			{
				switch(team.getType())
				{
					case 2:
						player.setArenaTeam2v2(team);
						break;
					case 3:
						player.setArenaTeam3v3(team);
						break;
					case 5:
						player.setArenaTeam5v5(team);
						break;
				}
			}
		}
		teams.clear();
	}
	
	private static AionArenaTeam createArenaTeam(short type, Player creator, String teamName)
	{
		AionArenaTeam team = new AionArenaTeam(IDFactory.getInstance().nextId(), type, creator.getObjectId().toString());
		team.setTeamName(teamName);
		
		arenaTeams.put(team.getTeamId(), team);
		
		return team;
	}
	
	public static boolean isTeamNameExisting(String name)
	{
		ArrayList<AionArenaTeam> teams = new ArrayList<AionArenaTeam>(arenaTeams.values());
		for(AionArenaTeam team : teams)
		{
			if(team.getTeamName().equals(name))
				return true;
		}
		return false;
	}
	
	public static boolean isLeaderInTeam(Player player, int TeamType)
	{
        AionArenaTeam team;
        switch(TeamType)
        {
            case 2: team = player.getArenaTeam2v2(); break;
            case 3: team = player.getArenaTeam3v3(); break;
            case 5: team = player.getArenaTeam5v5(); break;
            default: team = null; break;
        }
        if(player == ArenaManager.getPlayer(team.getPlayersOids().get(0)))
            return true;
        else
            return false;
    }
}
