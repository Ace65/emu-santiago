package br.focus.battleground;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import gameserver.dataholders.DataManager;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.BattleGroundTemplate;
import gameserver.model.templates.SpawnInfo;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.services.HTMLService;
import gameserver.services.InstanceService;
import gameserver.spawn.SpawnEngine;
import gameserver.utils.PacketSendUtility;
import gameserver.world.WorldMapInstance;

/**
 * @author ambrosius / Holgrabus
 *
 */
public class BattleGroundManager
{
	private static final Logger			log = Logger.getLogger(BattleGroundManager.class);
	public static boolean				INITIALIZED = false;
	
	private static Map<Integer,ArrayList<Player>> elyosWaitList = new HashMap<Integer,ArrayList<Player>>();
	private static Map<Integer,ArrayList<Player>> asmodiansWaitList = new HashMap<Integer,ArrayList<Player>>();
	private static Map<Integer,ArrayList<Player>> observersWaitList = new HashMap<Integer,ArrayList<Player>>();
	
	public static List<BattleGround> currentBattleGrounds = new ArrayList<BattleGround>();
	
	public static void initialize()
	{
		try
		{
			for(BattleGroundTemplate template : DataManager.BATTLEGROUND_DATA.getAllTemplates())
			{
				elyosWaitList.put(template.getTplId(), new ArrayList<Player>());
				asmodiansWaitList.put(template.getTplId(), new ArrayList<Player>());
				observersWaitList.put(template.getTplId(), new ArrayList<Player>());
			}
			
			for(SpawnInfo si : DataManager.BATTLEGROUND_DATA.getAgentLocations())
			{
				SpawnTemplate st = SpawnEngine.getInstance().addNewSpawn(si.getWorldId(), 1, si.getNpcId(), si.getX(), si.getY(), si.getZ(), si.getH(), 0, 0, false);
				SpawnEngine.getInstance().spawnBGAgent(st, 1, si.getNpcId());
			}
			
			INITIALIZED = true;
			
			log.info("Started BattleGroundManager, loaded " + elyosWaitList.size() + " BG templates.");
			
		}
		catch(Exception e)
		{
			log.error("Cannot initialize Battleground Manager !", e);
		}
	}
	
	public static boolean registerPlayer(Player player, int tplId)
	{
	    BattleGroundTemplate template = DataManager.BATTLEGROUND_DATA.getBattleGroundTemplate(tplId);
	    if(player.isInGroup())
	    {
	        if(player.getPlayerGroup().getGroupLeader().getObjectId() != player.getObjectId())
            {
	            PacketSendUtility.sendMessage(player, "Only group leader can register group.");
	            return false;
            }
	        
	        ArrayList<Player> members = new ArrayList<Player>();
            members.addAll(player.getPlayerGroup().getMembers());
            Byte[] membersLvl = new Byte[members.size()];
            Integer[] memberBgPoints = new Integer[members.size()];
            Boolean[] memberWaiting = new Boolean[members.size()];
            int highestLevel = 0;
            int lowestLevel = 60;
            int highestBgPoints = 0;
            int lowestBgPoints = 2147483646;
            int MembersWaiting = 0;
            for(int i=0; i < members.size(); i++)
            {
                membersLvl[i] = members.get(i).getLevel();
                memberBgPoints[i] = members.get(i).getCommonData().getBattleGroundPoints();
                memberWaiting[i] = members.get(i).battlegroundWaiting;
                if (membersLvl[i] > highestLevel)
                    highestLevel = membersLvl[i];
                if (membersLvl[i] < lowestLevel)
                    lowestLevel = membersLvl[i];
                if (memberBgPoints[i] > highestBgPoints)
                    highestBgPoints = memberBgPoints[i];
                if (memberBgPoints[i] < lowestBgPoints)
                    lowestBgPoints = memberBgPoints[i];
                if (memberWaiting[i] == true)
                    MembersWaiting += 1;
            }
            
	        if(player.getCommonData().getRace() == Race.ELYOS && MembersWaiting == 0)
	        {
	            if(player.getPlayerGroup().size() <= (template.getNbPlayers() - elyosWaitList.get(tplId).size()))
	            {
                    if(highestLevel <= template.getJoinConditions().getMaxLevel()
                       && lowestLevel >= template.getJoinConditions().getRequiredLevel()
                       && highestBgPoints <= template.getJoinConditions().getMaxBgPoints()
                       && lowestBgPoints <= template.getJoinConditions().getRequiredBgPoints())
                    {
                        elyosWaitList.get(tplId).addAll(members);
                        Player[] member = new Player[members.size()];
                        for(int i=0; i < members.size(); i++)
                        {
                            member[i] = members.get(i);
                            member[i].battlegroundWaiting = true;
                            PacketSendUtility.sendMessage(member[i], "You are now registered for the battleground: " + template.getName());
                            PacketSendUtility.sendMessage(member[i], "Please wait while your team is in formation...");
                        }
                        computeReadiness(tplId);
                        return true;
                    }  
                    PacketSendUtility.sendMessage(player, "All members can't join this battleground.");
                    return false;
	            }
	            PacketSendUtility.sendMessage(player, "You are too many to register in this battleground now.");
                return false;
	        }
	        
	        if(player.getCommonData().getRace() == Race.ASMODIANS && MembersWaiting == 0)
            {
                if(player.getPlayerGroup().size() <= (template.getNbPlayers() - asmodiansWaitList.get(tplId).size()))
                {
                    if(highestLevel <= template.getJoinConditions().getMaxLevel()
                       && lowestLevel >= template.getJoinConditions().getRequiredLevel()
                       && highestBgPoints <= template.getJoinConditions().getMaxBgPoints()
                       && lowestBgPoints <= template.getJoinConditions().getRequiredBgPoints())
                    {
                        asmodiansWaitList.get(tplId).addAll(members);
                        Player[] member = new Player[members.size()];
                        for(int i=0; i < members.size(); i++)
                        {
                            member[i] = members.get(i);
                            member[i].battlegroundWaiting = true;
                            PacketSendUtility.sendMessage(member[i], "You are now registered for the battleground: " + template.getName());
                            PacketSendUtility.sendMessage(member[i], "Please wait while your team is in formation...");
                        }
                        computeReadiness(tplId);
                        return true;
                    }
                    PacketSendUtility.sendMessage(player, "All members can't join this battleground.");
                    return false;
                }
                PacketSendUtility.sendMessage(player, "You are too many to register in this battleground now.");
                return false;
            }
	        PacketSendUtility.sendMessage(player, "Some members are already registered in a battleground.");
	        return false;
        }
	    if(!player.isInGroup())
	    {
	        if((player.getCommonData().getRace() == Race.ELYOS && !elyosWaitList.get(tplId).contains(player))
			|| (player.getCommonData().getRace() == Race.ASMODIANS && !asmodiansWaitList.get(tplId).contains(player)))
	        {
	            if(player.getCommonData().getRace() == Race.ELYOS) 
	                elyosWaitList.get(tplId).add(player);
	            else
	                asmodiansWaitList.get(tplId).add(player);
	            player.battlegroundWaiting = true;
	            PacketSendUtility.sendMessage(player, "You are now registered for the battleground: " + template.getName());
                PacketSendUtility.sendMessage(player, "Please wait while your team is in formation...");
	            computeReadiness(tplId);
	            return true;
	        }		
	        return false;
	    }
	    return false;
	}
	
	public static boolean registerPlayerObs(Player player, int tplId)
    {
        BattleGroundTemplate template = DataManager.BATTLEGROUND_DATA.getBattleGroundTemplate(tplId);
        observersWaitList.get(tplId).add(player);
        player.battlegroundWaiting = true;
        player.battlegroundObserve = 1;
        PacketSendUtility.sendMessage(player, "You are now registered to observe the battleground: " + template.getName());
        PacketSendUtility.sendMessage(player, "Please wait until the battleground is full...");
        return true;
    }
	
	public static boolean unregisterPlayer(Player player)
	{
		for(ArrayList<Player> ew : elyosWaitList.values())
			ew.remove(player);
		for(ArrayList<Player> aw : asmodiansWaitList.values())
			aw.remove(player);
		
		player.battlegroundWaiting = false;
		
		return true;
	}
	
	public static boolean unregisterPlayerObs(Player player)
    {
        for(ArrayList<Player> ow : observersWaitList.values())
            ow.remove(player);
        
        player.battlegroundWaiting = false;
        player.battlegroundObserve = 0;
        
        return true;
    }
	
	private static void computeReadiness(int tplId)
	{
	    BattleGroundTemplate template = DataManager.BATTLEGROUND_DATA.getBattleGroundTemplate(tplId);
        
	    List<Player> elyos = elyosWaitList.get(tplId);
	    List<Player> asmodians = asmodiansWaitList.get(tplId);
	    List<Player> observers = observersWaitList.get(tplId);
		
	    int waitingElyos = elyos.size();
	    int waitingAsmos = asmodians.size();
	    int waitingObservers = observers.size();
	    int maxplayers = template.getNbPlayers();
		
	    // 6v6
	    if(waitingElyos >= maxplayers && waitingAsmos >= maxplayers)
	    {
	        List<Player> e = new ArrayList<Player>();
	        List<Player> a = new ArrayList<Player>();
	        List<Player> o = new ArrayList<Player>();
	        for(int i=0; i < maxplayers; i++)
	        {
	            e.add(elyos.get(0));
	            elyos.remove(0);
	            a.add(asmodians.get(0));
	            asmodians.remove(0);
	        }
	        for(int i=0; i < waitingObservers; i++)
	        {
                o.add(observers.get(0));
                observers.remove(0);
	        }
	    startBattleGround(tplId, e, a, o);
	    }
	}
	
	public static void startBattleGround(int tplId, List<Player> elyos, List<Player> asmodians, List<Player> observers)
	{
		BattleGround bg;
		WorldMapInstance instance = InstanceService.createBattleGroundInstance(DataManager.BATTLEGROUND_DATA.getBattleGroundTemplate(tplId).getWorldId());
        switch(DataManager.BATTLEGROUND_DATA.getBattleGroundTemplate(tplId).getType())
		{
			case ASSAULT:
				bg = new AssaultBattleGround(tplId, instance);
				break;
			case CTF:
				bg = new CTFBattleGround(tplId, instance);
				break;
			default:
				bg = new AssaultBattleGround(tplId, instance);
				break;
		}
		
		for(Player e : elyos)
		{
			bg.addPlayer(e);
			e.setBattleGround(bg);
		}
		for(Player a : asmodians)
		{
			bg.addPlayer(a);
			a.setBattleGround(bg);
		}
		for(Player o : observers)
        {
            bg.addPlayer(o);
            o.setBattleGround(bg);
        }
		bg.start();
	}
	
	public static int getRegistrationTplId(Player player)
	{
		Map<Integer, ArrayList<Player>> target;
		if(player.getCommonData().getRace() == Race.ELYOS)
			target = elyosWaitList;
		else
			target = asmodiansWaitList;
		synchronized(target)
		{
			for(Entry<Integer, ArrayList<Player>> entry : target.entrySet())
			{
				if(entry.getValue().contains(player))
					return entry.getKey();
			}
		}
		return 0;
	}
	
	// Registering form for players
	public static void sendRegistrationForm(Player player)
	{
		List<BattleGroundTemplate> acceptedTemplates = new ArrayList<BattleGroundTemplate>(); 
		for(BattleGroundTemplate template : DataManager.BATTLEGROUND_DATA.getAllTemplates())
		{
		    if(player.getLevel() < template.getJoinConditions().getRequiredLevel())
                continue;
            if(player.getCommonData().getBattleGroundPoints() < template.getJoinConditions().getRequiredBgPoints())
                continue;
            if(player.getLevel() > template.getJoinConditions().getMaxLevel())
                continue;
            if(player.getCommonData().getBattleGroundPoints() > template.getJoinConditions().getMaxBgPoints())
                continue;
            acceptedTemplates.add(template);
		}
		
		if(acceptedTemplates.size() < 1)
		{
			PacketSendUtility.sendMessage(player, "No battleground available for you with your level and your battleground points.");
			return;
		}
		
		String[] acceptedLocations = new String[acceptedTemplates.size()];
		for(int i=0; i < acceptedTemplates.size(); i++)
		{
			acceptedLocations[i] = acceptedTemplates.get(i).getName() + " (" + elyosWaitList.get(acceptedTemplates.get(i).getTplId()).size() + " - " + asmodiansWaitList.get(acceptedTemplates.get(i).getTplId()).size() + " / " + acceptedTemplates.get(i).getNbPlayers() + ")";
		}
		
		String html = HTMLService.HTMLTemplate("Register to battlegrounds", "You can register for the following battlegrounds :", acceptedLocations, 0, 0);
		HTMLService.showHTML(player, html, 150000001);
	}
	
	// Registering form for Observers
	public static void sendRegistrationFormObs(Player player)
    {
        List<BattleGroundTemplate> acceptedBattlegrounds = new ArrayList<BattleGroundTemplate>(); 
        for(BattleGroundTemplate template : DataManager.BATTLEGROUND_DATA.getAllTemplates())
        {
            acceptedBattlegrounds.add(template);
        }
        
        if(acceptedBattlegrounds.size() < 1)
        {
            PacketSendUtility.sendMessage(player, "No battleground available.");
            return;
        }
        
        String[] acceptedLoc = new String[acceptedBattlegrounds.size()];
        for(int i=0; i < acceptedBattlegrounds.size(); i++)
        {
            acceptedLoc[i] = acceptedBattlegrounds.get(i).getName() + " (" + elyosWaitList.get(acceptedBattlegrounds.get(i).getTplId()).size() + " - " + asmodiansWaitList.get(acceptedBattlegrounds.get(i).getTplId()).size() + " / " + acceptedBattlegrounds.get(i).getNbPlayers() + ")";
        }
        
        String html = HTMLService.HTMLTemplate("Register to battlegrounds", "You can register for the following battlegrounds :", acceptedLoc, 0, 0);
        HTMLService.showHTML(player, html, 151000001);
    }

	public static Map<Integer, ArrayList<Player>> getElyosWaitList()
	{
		return elyosWaitList;
	}

	public static void setElyosWaitList(Map<Integer, ArrayList<Player>> e)
	{
		elyosWaitList = e;
	}

	public static Map<Integer, ArrayList<Player>> getAsmodiansWaitList()
	{
		return asmodiansWaitList;
	}

	public static void setAsmodiansWaitList(Map<Integer, ArrayList<Player>> a)
	{
		asmodiansWaitList = a;
	}
	
	public static Map<Integer, ArrayList<Player>> getObserversWaitList()
    {
        return observersWaitList;
    }

    public static void setObserversWaitList(Map<Integer, ArrayList<Player>> o)
    {
        observersWaitList = o;
    }
	
	public static List<BattleGroundTemplate> getUnlockedBattleGrounds(int oldPoints, int newPoints)
	{
		ArrayList<BattleGroundTemplate> templates = new ArrayList<BattleGroundTemplate>();
		for(BattleGroundTemplate tpl : DataManager.BATTLEGROUND_DATA.getAllTemplates())
		{
			if(oldPoints >= tpl.getJoinConditions().getRequiredBgPoints())
				continue;
			if(newPoints >= tpl.getJoinConditions().getRequiredBgPoints())
				templates.add(tpl);
		}
		return templates;
	}
	
}
