package br.focus.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import commons.database.dao.DAOManager;

import br.focus.factories.SurveyFactory;
import gameserver.dao.PlayerDAO;
import gameserver.model.AionArenaTeam;
import gameserver.model.ChatType;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import gameserver.services.HTMLService;
import gameserver.services.InstanceService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import gameserver.world.WorldMapInstance;

/**
 * @author Holgrabus
 *
 */
public class ArenaManager
{
	private static final Logger			log = Logger.getLogger(ArenaManager.class);
	
	private static Map<Integer,ArrayList<AionArenaTeam>> teamWaitList = new HashMap<Integer,ArrayList<AionArenaTeam>>();
		
	public static int ArenaBattleTime = 480;
	
	public static void initialize()
    {
	    teamWaitList.put(2, new ArrayList<AionArenaTeam>());
	    teamWaitList.put(3, new ArrayList<AionArenaTeam>());
	    teamWaitList.put(5, new ArrayList<AionArenaTeam>());
    }
	
	public static boolean registerTeam2(Player player)
	{
    	if(!AionArenaService.isLeaderInTeam(player, 2)){
        	PacketSendUtility.sendMessage(player, "You are not the leader of this team.");
        	return false;
        }
    	
	    AionArenaTeam Team2v2 = player.getArenaTeam2v2();
	    ArrayList<Integer> TeamMembersIds = new ArrayList<Integer>();
	    ArrayList<Player> TeamMembers = new ArrayList<Player>();
	    TeamMembersIds.addAll(Team2v2.getPlayersOids());
	    Integer[] MembersWaiting = new Integer[TeamMembersIds.size()];
	    int TeamMembersOnline = 0;
        int TeamMembersWaiting = 0;
	    for(int i=0; i < TeamMembersIds.size(); i++)
        {
	    	Player p = ArenaManager.getPlayer(TeamMembersIds.get(i));
	    	if(p != null)
	    	{
	    		TeamMembers.add(p);
	    		MembersWaiting[i] = p.ArenaStatus;
	    		TeamMembersOnline += 1;
	    		TeamMembersWaiting += MembersWaiting[i];
	    	}
        }
        
        if(TeamMembersOnline == 2 && TeamMembersWaiting == 0)
	    {
	        Player[] members = new Player[TeamMembers.size()];
            for(int i=0; i < TeamMembers.size(); i++)
            {
                members[i] = TeamMembers.get(i);
                members[i].ArenaStatus = 1;
            }
            ArenaManager.getTeamWaitList().get(2).add(Team2v2);
            ArenaManager.computeReadiness(2, Team2v2);
            return true;
	    }
	    else
	    {
	        PacketSendUtility.sendMessage(player, "All team members are not online."); 
	        return false;
	    } 
	}
	
	public static boolean registerTeam3(Player player)
	{
    	if(!AionArenaService.isLeaderInTeam(player, 3)){
        	PacketSendUtility.sendMessage(player, "You are not the leader of this team.");
        	return false;
        }
    	
	    AionArenaTeam Team3v3 = player.getArenaTeam3v3();
	    ArrayList<Integer> TeamMembersIds = new ArrayList<Integer>();
	    ArrayList<Player> TeamMembers = new ArrayList<Player>();
	    TeamMembersIds.addAll(Team3v3.getPlayersOids());
	    Integer[] MembersWaiting = new Integer[TeamMembersIds.size()];
	    int TeamMembersOnline = 0;
        int TeamMembersWaiting = 0;
	    for(int i=0; i < TeamMembersIds.size(); i++)
        {
	    	Player p = ArenaManager.getPlayer(TeamMembersIds.get(i));
	    	if(p != null)
	    	{
	    		TeamMembers.add(p);
	    		MembersWaiting[i] = p.ArenaStatus;
	    		TeamMembersOnline += 1;
	    		TeamMembersWaiting += MembersWaiting[i];
	    	}
        }
        
        if(TeamMembersOnline == 3 && TeamMembersWaiting == 0)
	    {
	        Player[] members = new Player[TeamMembers.size()];
            for(int i=0; i < TeamMembers.size(); i++)
            {
                members[i] = TeamMembers.get(i);
                members[i].ArenaStatus = 1;
            }
            ArenaManager.getTeamWaitList().get(2).add(Team3v3);
            ArenaManager.computeReadiness(2, Team3v3);
            return true;
	    }
	    else
	    {
	        PacketSendUtility.sendMessage(player, "All team members are not online."); 
	        return false;
	    } 
	}
	
	public static boolean registerTeam5(Player player)
	{
    	if(!AionArenaService.isLeaderInTeam(player, 3)){
        	PacketSendUtility.sendMessage(player, "You are not the leader of this team.");
        	return false;
        }
    	
	    AionArenaTeam Team5v5 = player.getArenaTeam5v5();
	    ArrayList<Integer> TeamMembersIds = new ArrayList<Integer>();
	    ArrayList<Player> TeamMembers = new ArrayList<Player>();
	    TeamMembersIds.addAll(Team5v5.getPlayersOids());
	    Integer[] MembersWaiting = new Integer[TeamMembersIds.size()];
	    int TeamMembersOnline = 0;
        int TeamMembersWaiting = 0;
	    for(int i=0; i < TeamMembersIds.size(); i++)
        {
	    	Player p = ArenaManager.getPlayer(TeamMembersIds.get(i));
	    	if(p != null)
	    	{
	    		TeamMembers.add(p);
	    		MembersWaiting[i] = p.ArenaStatus;
	    		TeamMembersOnline += 1;
	    		TeamMembersWaiting += MembersWaiting[i];
	    	}
        }
        
        if(TeamMembersOnline == 5 && TeamMembersWaiting == 0)
	    {
	        Player[] members = new Player[TeamMembers.size()];
            for(int i=0; i < TeamMembers.size(); i++)
            {
                members[i] = TeamMembers.get(i);
                members[i].ArenaStatus = 1;
            }
            ArenaManager.getTeamWaitList().get(2).add(Team5v5);
            ArenaManager.computeReadiness(2, Team5v5);
            return true;
	    }
	    else
	    {
	        PacketSendUtility.sendMessage(player, "All team members are not online."); 
	        return false;
	    } 
	}
	
	public static boolean unregisterTeam(Player player)
    {
	    ArrayList<Integer> TeamMembersIds = new ArrayList<Integer>();
	    if(teamWaitList.get(2).contains(player.getArenaTeam2v2()))
	    {
	        teamWaitList.get(2).remove(player.getArenaTeam2v2());
	        TeamMembersIds.addAll(player.getArenaTeam2v2().getPlayersOids());
	        Player[] members = new Player[TeamMembersIds.size()];
	        for(int i=0; i < TeamMembersIds.size(); i++)
	        {
	            members[i] = getPlayer(TeamMembersIds.get(i));
	            members[i].ArenaStatus = 0;
	            PacketSendUtility.sendPacket(members[i], new SM_MESSAGE(0, null, "Your team : " + members[i].getArenaTeam2v2().getTeamName() + " has been unregistered by : " + player.getName(), ChatType.SYSTEM_NOTICE));
	        }
	        return true;
	    }
	    else if(teamWaitList.get(3).contains(player.getArenaTeam3v3()))
        {
            teamWaitList.get(3).remove(player.getArenaTeam3v3());
            TeamMembersIds.addAll(player.getArenaTeam3v3().getPlayersOids());
            Player[] members = new Player[TeamMembersIds.size()];
            for(int i=0; i < TeamMembersIds.size(); i++)
            {
            	members[i] = getPlayer(TeamMembersIds.get(i));
                members[i].ArenaStatus = 0;
	            PacketSendUtility.sendPacket(members[i], new SM_MESSAGE(0, null, "Your team : " + members[i].getArenaTeam3v3().getTeamName() + " has been unregistered by : " + player.getName(), ChatType.SYSTEM_NOTICE));
            }
            return true;
        }
	    else if(teamWaitList.get(5).contains(player.getArenaTeam5v5()))
        {
            teamWaitList.get(5).remove(player.getArenaTeam5v5());
            TeamMembersIds.addAll(player.getArenaTeam5v5().getPlayersOids());
            Player[] members = new Player[TeamMembersIds.size()];
            for(int i=0; i < TeamMembersIds.size(); i++)
            {
            	members[i] = getPlayer(TeamMembersIds.get(i));
                members[i].ArenaStatus = 0;
	            PacketSendUtility.sendPacket(members[i], new SM_MESSAGE(0, null, "Your team : " + members[i].getArenaTeam5v5().getTeamName() + " has been unregistered by : " + player.getName(), ChatType.SYSTEM_NOTICE));
            }
            return true;
        }
	    
	    return false;
    }
	
	public static void computeReadiness(int TeamSize, AionArenaTeam team)
	{
		int Level = 10;
        int Points = 500;
        
	    if(TeamSize == 2)
	    {
	        List<AionArenaTeam> Teams = ArenaManager.getTeamWaitList().get(2);
	        int launched = 0;
	        
            for(int i=0; i < Teams.size(); i++)
            {
                team.broadcastToArenaTeam("You are now registered for an Arena Team Battle with the team : " + team.getTeamName());
                
                if(Math.abs(maxTeamLevel(team) - maxTeamLevel(Teams.get(i))) < Level && Math.abs(team.getTeamPoints() - Teams.get(i).getTeamPoints()) < Points && team != Teams.get(i) && launched == 0)
                {
                    launched = 1;
                    startArenaTeam(team, Teams.get(i));
                    teamWaitList.get(2).remove(team);
                    teamWaitList.get(2).remove(Teams.get(i));
                }
                else
                {
                    team.broadcastToArenaTeam("No teams are available for now with the max level of your team. Please wait for the next battle...");
                }             
            }
	    }
	    else if(TeamSize == 3)
        {
	        List<AionArenaTeam> Teams = ArenaManager.getTeamWaitList().get(3);
            int launched = 0;
            
            for(int i=0; i < Teams.size(); i++)
            {
                team.broadcastToArenaTeam("You are now registered for an Arena Team Battle with the team : " + team.getTeamName());
                
                if(Math.abs(maxTeamLevel(team) - maxTeamLevel(Teams.get(i))) < Level && Math.abs(team.getTeamPoints() - Teams.get(i).getTeamPoints()) < Points && team != Teams.get(i) && launched == 0)
                {
                    launched = 1;
                    startArenaTeam(team, Teams.get(i));
                    teamWaitList.get(3).remove(team);
                    teamWaitList.get(3).remove(Teams.get(i));
                }
                else
                {
                    team.broadcastToArenaTeam("No teams are available for now with the max level of your team. Please wait for the next battle...");
                }                
            }
        }
	    else if(TeamSize == 5)
        {
	        List<AionArenaTeam> Teams = ArenaManager.getTeamWaitList().get(5);
            int launched = 0;
            
            for(int i=0; i < Teams.size(); i++)
            {
                team.broadcastToArenaTeam("You are now registered for an Arena Team Battle with the team : " + team.getTeamName());
                
                if(Math.abs(maxTeamLevel(team) - maxTeamLevel(Teams.get(i))) < Level && Math.abs(team.getTeamPoints() - Teams.get(i).getTeamPoints()) < Points && team != Teams.get(i) && launched == 0)
                {
                    launched = 1;
                    startArenaTeam(team, Teams.get(i));
                    teamWaitList.get(5).remove(team);
                    teamWaitList.get(5).remove(Teams.get(i));
                }
                else
                {
                    team.broadcastToArenaTeam("No teams are available for now with the max level of your team. Please wait for the next battle...");
                }                
            }
        }
	}
	
	public static void startArenaTeam(final AionArenaTeam Team1, final AionArenaTeam Team2)
    {
        //All players
        final List<Player> arenaPlayers = new ArrayList<Player>();
        //Create lists of players in team 1 and 2
        List<Player> PlayersTeam1 = new ArrayList<Player>();
        List<Player> PlayersTeam2 = new ArrayList<Player>();
        //Add players in lists
        PlayersTeam1.addAll(ArenaManager.getAllPlayers(Team1.getPlayersOids()));
        PlayersTeam2.addAll(ArenaManager.getAllPlayers(Team2.getPlayersOids()));
        //add global players
        arenaPlayers.addAll(PlayersTeam1);
        arenaPlayers.addAll(PlayersTeam2);
        
        for(Player p1 : PlayersTeam1)
        {
            p1.ArenaTeam = 1;
        }
        for(Player p2 : PlayersTeam2)
        {
            p2.ArenaTeam = 2;
        }
        
        for(Player player : arenaPlayers)
        {
            if(player.ArenaTeam == 1)
            {
                PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, "The next Arena Team Battle is now ready to start. You will be teleported in 30 seconds. Have fun ! You will fight versus the team : " + Team2.getTeamName(), ChatType.SYSTEM_NOTICE));
            }
            if(player.ArenaTeam == 2)
            {
            	PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, "The next Arena Team Battle is now ready to start. You will be teleported in 30 seconds. Have fun ! You will fight versus the team : " + Team1.getTeamName(), ChatType.SYSTEM_NOTICE));
            }
        }
        
        ArenaManager.invitePlayers(arenaPlayers);
        
        ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
            public void run()
            {
                AionArenaTeam.broadcastToArenaPlayers(arenaPlayers, "30 seconds before starting ...");
                
                for(Player p : arenaPlayers)
                {
                	//set who is enemy
                	p.clearKnownlist();
            		PacketSendUtility.sendPacket(p, new SM_PLAYER_INFO(p, false));
            		p.updateKnownlist();
                }
                
                for(Player p : arenaPlayers)
                {
                	//reset stats
                	p.getLifeStats().setCurrentHpPercent(100);
                    p.getLifeStats().setCurrentMpPercent(100);
                    p.getCommonData().setDp(0);
                    p.getEffectController().removeAllEffects();
                    //set state 2
                    p.ArenaStatus = 2;
                }
            }
        }, 31 * 1000);
        
        ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
            public void run()
            {
                AionArenaTeam.broadcastToArenaPlayers(arenaPlayers, "The Arena Battle is now started !");
                for(Player p : arenaPlayers)
                {
                    p.ArenaStatus = 3;
                }
            }
        }, 60 * 1000);
        
        ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
            public void run()
            {
                AionArenaTeam.broadcastToArenaPlayers(arenaPlayers, "The Arena Battle will end in 30 seconds !");
            }
        }, ArenaBattleTime * 1000);
        
        ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
            public void run()
            {
                ArenaManager.end(arenaPlayers, Team1,  Team2);
            }
        }, (ArenaBattleTime + 30) * 1000);
    }
	
	public static void invitePlayers(final List<Player> arenaPlayers)
    {
	    //create new instance
        final WorldMapInstance instance = InstanceService.createBattleGroundInstance(310080000);
        
	    ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
            public void run()
            {
                for(Player player : arenaPlayers)
                {
                	teleport(player, instance.getMapId(), instance.getInstanceId());
                }
            }
        }, 30 * 1000);
    }
	
	public static void teleport(Player player, int Mapid, int InstanceId)
	{
		if(player.ArenaTeam == 1)
            TeleportService.teleportTo(player, Mapid, InstanceId, (float) 312.98938, (float) 240.06432, (float) 158.89537, (byte) 60, 1000);
    
		else if(player.ArenaTeam == 2)
            TeleportService.teleportTo(player, Mapid, InstanceId, (float) 239.75197, (float) 239.94702, (float) 158.88121f, (byte) 0, 1000);
        
        else {
        	if(player.getCommonData().getRace() == Race.ELYOS)
                TeleportService.teleportTo(player, 110010000, 1374, 1399, 573, 0);
            else
                TeleportService.teleportTo(player, 120010000, 1324, 1550, 210, 0);
        }
	}
	
    public static void end(List<Player> arenaPlayers, AionArenaTeam Team1, AionArenaTeam Team2)
    {    
        AionArenaTeam.broadcastToArenaPlayers(arenaPlayers, "The battle is now ended ! Clic on the right bottom button to show the rank board. If you are dead, just use the spell Return and you will be teleported back.");
        registerTeamPoints(arenaPlayers, Team1, Team2);
        
        for(Player p : arenaPlayers)
        {
            HTMLService.showHTML(p, SurveyFactory.buildAionArenaTeamReport(p, arenaPlayers, Team1, Team2), 152000001);
            p.getEffectController().removeAllEffects();
            if(!p.getLifeStats().isAlreadyDead())
            {
                if(p.getCommonData().getRace() == Race.ELYOS)
                    TeleportService.teleportTo(p, 110010000, 1374, 1399, 573, 0);
                else
                    TeleportService.teleportTo(p, 120010000, 1324, 1550, 210, 0);
            }
        }
    }
    
	public static List<Player> getRanking(List<Player> players, int Team)
    {
        ArrayList<Player> ranking = new ArrayList<Player>();
        
        for(Player p : players)
        {
            if(p.ArenaTeam != Team)
                continue;
            if(ranking.size() == 0)
                ranking.add(p);
            else
            {
                for(int i=0; i < ranking.size(); i++)
                {
                    if(p.ArenaTeamSessionPoints > ranking.get(i).ArenaTeamSessionPoints)
                    {
                        ranking.add(i, p);
                        break;
                    }
                }
                if(!ranking.contains(p))
                    ranking.add(p);
            }
        }
        return ranking;
    }
    
    public static void commitPoints(final Player player)
    {
        player.getCommonData().setArenaPoints(player.getCommonData().getArenaPoints() + player.ArenaTeamSessionPoints);
        player.getEffectController().removeAllEffects();
        ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
            public void run()
            {
            	player.clearKnownlist();
        		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
        		player.updateKnownlist();
                player.ArenaTeamSessionPoints = 0;
                player.ArenaTeamSessionKills = 0;
                player.ArenaTeamSessionDeaths = 0;
                player.ArenaStatus = 0;
                player.ArenaTeam = 0;
            }
        }, 5 * 1000);
    }
    
    public static void registerTeamPoints(List<Player> arenaPlayers, AionArenaTeam Team1, AionArenaTeam Team2)
    {
    	List<Player> arenaPlayers1 = ArenaManager.getRanking(arenaPlayers,1);
        List<Player> arenaPlayers2 = ArenaManager.getRanking(arenaPlayers,2);
        
        int Team1Pts = 0;
        int Team2Pts = 0;
        for(Player p1 : arenaPlayers1)
        {
        	Team1Pts += p1.ArenaTeamSessionPoints;
        }
        for(Player p2 : arenaPlayers2)
        {
        	Team2Pts += p2.ArenaTeamSessionPoints;
        }
        
        Team1.setTeamPoints(Team1Pts);
        Team2.setTeamPoints(Team2Pts);
    }
    
    public static Integer maxTeamLevel(AionArenaTeam arenaTeam)
    {
        List<Player> Players = ArenaManager.getAllPlayers(arenaTeam.getPlayersOids());
        Byte[] PlayersLvl = new Byte[Players.size()];
        int highestLevel = 0;
        for(int i=0; i < Players.size(); i++)
        {
            PlayersLvl[i] = Players.get(i).getLevel();
            if (PlayersLvl[i] > highestLevel)
                highestLevel = PlayersLvl[i];
        }
        return highestLevel;
    }
    
    public static void increasePoints(Player player, int value)
    {
        PacketSendUtility.sendMessage(player, "You have earned " + value + " Arena points.");
        player.ArenaTeamSessionPoints += value;
        player.ArenaTeamSessionKills += 1;
    }
    
    public static void decreasePoints(Player player, int value)
    {
        PacketSendUtility.sendMessage(player, "You have lost " + value + " Arena points.");
        
        player.ArenaTeamSessionPoints -= value;
        player.ArenaTeamSessionDeaths += 1;
        
        if(player.ArenaTeamSessionPoints < 0)
            player.ArenaTeamSessionPoints = 0;
    }
    
    public static void onKillPlayer(Player killer, Player victim)
    {
        decreasePoints(victim, 2);
        increasePoints(killer, 5);
    }
    
	public static Player getPlayer(int objectId)
	{
	    return World.getInstance().findPlayer(objectId);
	}
	
	public static List<Player> getAllPlayers(List<Integer> objectId)
    {
	    List<Player> players = new ArrayList<Player>();
	    for(int i=0; i < objectId.size(); i++)
	    {
	        players.add(World.getInstance().findPlayer(objectId.get(i)));
	    }
        return players;
    }
	
	public static String getPlayerName(Integer playerId)
    {
        return DAOManager.getDAO(PlayerDAO.class).getPlayerNameByObjId(playerId);
    }
	
	public static void setTeamWaitList(Map<Integer,ArrayList<AionArenaTeam>> team)
    {
        teamWaitList = team;
    }

    public static Map<Integer,ArrayList<AionArenaTeam>> getTeamWaitList()
    {
        return teamWaitList;
    }
}
