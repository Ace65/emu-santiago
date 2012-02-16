package br.focus.battleground;

import java.util.ArrayList;
import java.util.List;

import gameserver.dataholders.DataManager;
import gameserver.model.ChatType;
import gameserver.model.Race;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.model.gameobjects.state.CreatureVisualState;
import gameserver.model.templates.BattleGroundTemplate;
import gameserver.model.templates.BattleGroundType;
import gameserver.network.aion.serverpackets.SM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.services.TeleportService;
import gameserver.skill.effect.EffectId;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.WorldMapInstance;

/**
 * @author ambrosius / Holgrabus
 *
 */
public abstract class BattleGround
{
	protected List<Player> 		players = new ArrayList<Player>();
	
	protected int					tplId;
	
	private long				startTime;
	
	protected WorldMapInstance	instance;
	
	public boolean 				running = false;
	
	protected BattleGroundTemplate template;
		
	public BattleGround(int tplId, WorldMapInstance instance)
	{
		startTime = System.currentTimeMillis() / 1000;
		this.tplId = tplId;
		this.instance = instance;
		template = DataManager.BATTLEGROUND_DATA.getBattleGroundTemplate(tplId);
		BattleGroundManager.currentBattleGrounds.add(this);
	}
	
	public void increasePoints(Player player, int value)
	{
		PacketSendUtility.sendMessage(player, "You have earned " + value + " BG points.");
		player.battlegroundSessionPoints += value;
		if(player.getBattleGround().getTemplate().getType() == BattleGroundType.CTF && value == player.getBattleGround().getTemplate().getRules().getFlagCap())
		    player.battlegroundSessionFlags += 1;
		else if(value == player.getBattleGround().getTemplate().getRules().getKillPlayer())
            player.battlegroundSessionKills += 1;
	}
	
	public void decreasePoints(Player player, int value)
	{
		PacketSendUtility.sendMessage(player, "You have lost " + value + " BG points.");
		
		player.battlegroundSessionPoints -= value;
		if(player.getBattleGround().getTemplate().getType() == BattleGroundType.ASSAULT && value == player.getBattleGround().getTemplate().getRules().getDie())
		    player.battlegroundSessionDeaths += 1;
		
		if(player.battlegroundSessionPoints < 0)
			player.battlegroundSessionPoints = 0;
		
	}
	
	public List<Player> getPlayers()
	{
		return players;
	}
	
	public void addPlayer(Player player)
	{
		players.add(player);
	}
	
	public void removePlayer(Player player)
	{
		players.remove(player);
	}
	
	public long getStartTime()
	{
		return startTime;
	}

	public WorldMapInstance getInstance()
	{
		return instance;
	}

	public void teleportPlayer(Player player)
	{
		BattleGroundTemplate template = DataManager.BATTLEGROUND_DATA.getBattleGroundTemplate(tplId);
		if(player.getCommonData().getRace() == Race.ELYOS)
			TeleportService.teleportTo(player, template.getWorldId(), instance.getInstanceId(), template.getInsertPoint().getXe(), template.getInsertPoint().getYe(), template.getInsertPoint().getZe(), template.getInsertPoint().getHe(), 1000);
		else
			TeleportService.teleportTo(player, template.getWorldId(), instance.getInstanceId(), template.getInsertPoint().getXa(), template.getInsertPoint().getYa(), template.getInsertPoint().getZa(), template.getInsertPoint().getHa(), 1000);
	}
	
	public void broadcastToBattleGround(final String message, final Race targetRace)
	{
		for(Player p : players)
		{
			if(targetRace == null || p.getCommonData().getRace() == targetRace)
				PacketSendUtility.sendPacket(p, new SM_MESSAGE(0, null, message, ChatType.SYSTEM_NOTICE));
		}
	}
	
	public void invitePlayer(final Player player)
	{
		PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, "The Battleground: " + template.getName() + " is now ready to start. You will be teleported in 30 seconds. Have fun :)", ChatType.SYSTEM_NOTICE));
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
					teleportPlayer(player);
			}
		}, 30000);
	}
	
	public void start()
	{
		
		for(Player p : players)
		{
			p.battlegroundWaiting = false;
			invitePlayer(p);
		}
		
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				broadcastToBattleGround(template.getWaitTime() + " seconds before starting ...", null);
				for(Player p : players)
		        {
					//reset stats
                	p.getLifeStats().setCurrentHpPercent(100);
                    p.getLifeStats().setCurrentMpPercent(100);
                    p.getCommonData().setDp(0);
                    p.getEffectController().removeAllEffects();
				    
				    if(p.battlegroundObserve == 1)
				    {
				        p.battlegroundObserve = 2;
				        p.getEffectController().setAbnormal(EffectId.INVISIBLE_RELATED.getEffectId());
			            p.setVisualState(CreatureVisualState.HIDE20);
			            PacketSendUtility.broadcastPacket(p, new SM_PLAYER_STATE(p), true);
			            PacketSendUtility.sendMessage(p, "You are now invisible.");
			            p.setInvul(true);
			            PacketSendUtility.sendMessage(p, "You are now immortal.");
				    }
		        }
			}
		}, 31 * 1000);
		
		ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
            public void run()
            {
                broadcastToBattleGround("The battleground is now open !", null);
                running = true;
            }
        }, (template.getWaitTime() + 30) * 1000);
		
		ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
            public void run()
            {
                if(running == true)
                broadcastToBattleGround("The battleground will end in 30 seconds !", null);
            }
        }, template.getBgTime() * 1000);
		
		ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
            public void run()
            {
                for(Player p : players)
                {
                    if(p.battlegroundObserve > 0)
                    {
                        p.battlegroundObserve = 3;
                        PacketSendUtility.sendMessage(p, "The bet time is now ended.");
                    }
                }
            }
        }, ((template.getBgTime() / 2) + 30) * 1000);
		
		ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
            public void run()
            {
                if(running == true)
                    end();
            }
        }, (template.getBgTime() + 30) * 1000);
	}
	
    public BattleGroundTemplate getTemplate()
	{
		return template;
	}

	public void setTemplate(BattleGroundTemplate template)
	{
		this.template = template;
	}

	public void end()
	{
		running = false;		
		broadcastToBattleGround("The battle is now ended ! Clic on the right bottom button to show the rank board. If you are dead, just use the spell Return and you will be teleported back.", null);
		for(Player p : players)
        {
		    if(p.battlegroundObserve > 0)
            {
		        p.getEffectController().unsetAbnormal(EffectId.INVISIBLE_RELATED.getEffectId());
                p.unsetVisualState(CreatureVisualState.HIDE20);
                PacketSendUtility.broadcastPacket(p, new SM_PLAYER_STATE(p), true);
                PacketSendUtility.sendMessage(p, "You are now visible.");
                p.setInvul(false);
                PacketSendUtility.sendMessage(p, "You are now mortal.");
            }
        }
	}
	
	public List<Player> getRanking(Race race, boolean reward)
	{
		ArrayList<Player> ranking = new ArrayList<Player>();
		
		for(Player p : players)
		{
			if(p.getCommonData().getRace() != race)
				continue;
			if(p.battlegroundObserve >= 1)
			    continue;
			if(ranking.size() == 0)
				ranking.add(p);
			else
			{
				for(int i=0; i < ranking.size(); i++)
				{
					if(p.battlegroundSessionPoints > ranking.get(i).battlegroundSessionPoints)
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
	
	public void commitPoints(Player player)
	{
		player.getCommonData().setBattleGroundPoints(player.getCommonData().getBattleGroundPoints() + player.battlegroundSessionPoints);
		player.getEffectController().removeAllEffects();
		ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
            public void run()
            {
                for(final Player p : players)
                {
                    p.battlegroundObserve = 0;
                    p.battlegroundSessionPoints = 0;
                    p.battlegroundSessionKills = 0;
                    p.battlegroundSessionDeaths = 0;
                    p.battlegroundSessionFlags = 0;
                    p.battlegroundBetE = 0;
                    p.battlegroundBetA = 0;
                    
                    if(p.getWorldId() == 110010000  || p.getWorldId() == 120010000)
                    {
                        String message = "Do you want to go play in a battleground again ?";
                        RequestResponseHandler responseHandler = new RequestResponseHandler(p){

                            public void acceptRequest(Creature requester, Player responder)
                            {
                                if(p.getBattleGround() != null)
                                {
                                    PacketSendUtility.sendMessage(p, "You are already registered in a battleground.");
                                    PacketSendUtility.sendMessage(p, "Use your spell Return to leave the battleground.");
                                    return;
                                }
                                else if(p.battlegroundWaiting)
                                {
                                    PacketSendUtility.sendMessage(p, "You are already registered in a battleground.");
                                    PacketSendUtility.sendMessage(p, "Use the command .bg unregister to cancel your registration.");
                                    return;
                                }
                                else
                                {
                                    BattleGroundManager.sendRegistrationForm(p);
                                }
                                return;
                            }

                            public void denyRequest(Creature requester, Player responder){ return; }
                        };
                        boolean requested = p.getResponseRequester().putRequest(902247, responseHandler);
                        if(requested){PacketSendUtility.sendPacket(p, new SM_QUESTION_WINDOW(902247, 0, message));return;}
                    }
                }
            }
        }, 5 * 1000);
		
	}
	
	public int getTplId()
    {
        return tplId;
    }
    
    public int getWorldId()
    {
        return DataManager.BATTLEGROUND_DATA.getBattleGroundTemplate(tplId).getWorldId();
    }
    
}