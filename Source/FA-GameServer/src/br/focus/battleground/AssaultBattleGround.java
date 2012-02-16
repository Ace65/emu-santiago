package br.focus.battleground;

import gameserver.dataholders.DataManager;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.BattleGroundTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.services.HTMLService;
import gameserver.services.TeleportService;
import gameserver.spawn.SpawnEngine;
import gameserver.world.WorldMapInstance;

import br.focus.factories.SurveyFactory;

/**
 * @author ambrosius / Holgrabus
 * 
 */
public class AssaultBattleGround extends BattleGround
{
	
	private int 				totalScoreE = 0;
	private int                 totalScoreA = 0;
	
	public AssaultBattleGround(int tplId, WorldMapInstance instance)
	{
		super(tplId, instance);
	}
	
	@Override
	public void increasePoints(Player player, int value)
	{
		super.increasePoints(player, value);
		
		if(player.getCommonData().getRace() == Race.ELYOS)
		{
		    totalScoreE += value;
		    if(totalScoreE >= template.getTargetScore())
	        {
	            end();
	        }
		}
		else if(player.getCommonData().getRace() == Race.ASMODIANS)
		{
            totalScoreA += value;
            if(totalScoreA >= template.getTargetScore())
            {
                end();
            }
        }
	}
	
	@Override
	public void decreasePoints(Player player, int value)
	{
		super.decreasePoints(player, value);
		
		if(player.getCommonData().getRace() == Race.ELYOS && totalScoreE >= value)
			totalScoreE -= value;
		else if(player.getCommonData().getRace() == Race.ASMODIANS && totalScoreA >= value)
            totalScoreA -= value;
		else if(player.getCommonData().getRace() == Race.ELYOS && totalScoreE <= value)
            totalScoreE = 0;
		else if(player.getCommonData().getRace() == Race.ASMODIANS && totalScoreA >= value)
            totalScoreA = 0;
	}
	
	public void onKillPlayer(Player killer, Player victim)
	{
		decreasePoints(victim, template.getRules().getDie());
		increasePoints(killer, template.getRules().getKillPlayer());
	}
	
	@Override
	public void start()
	{
		BattleGroundTemplate bgTemplate = DataManager.BATTLEGROUND_DATA.getBattleGroundTemplate(tplId);
		/*
		 * Spawn Healers
		 */
		SpawnTemplate se = SpawnEngine.getInstance().addNewSpawn(bgTemplate.getWorldId(), instance.getInstanceId(), 203098, bgTemplate.getHealerLocation().getXe(),bgTemplate.getHealerLocation().getYe(),bgTemplate.getHealerLocation().getZe(), bgTemplate.getHealerLocation().getHe(), 0, 0, false);
		SpawnTemplate sa = SpawnEngine.getInstance().addNewSpawn(bgTemplate.getWorldId(), instance.getInstanceId(), 203557, bgTemplate.getHealerLocation().getXa(),bgTemplate.getHealerLocation().getYa(),bgTemplate.getHealerLocation().getZa(), bgTemplate.getHealerLocation().getHa(), 0, 0, false);
		
		SpawnEngine.getInstance().spawnBGHealer(se, instance.getInstanceId(), Race.ELYOS);
		SpawnEngine.getInstance().spawnBGHealer(sa, instance.getInstanceId(), Race.ASMODIANS);
		
		super.start();
		
	}
	
	@Override
	public void end()
	{
		super.end();
		
		for(Player p : players)
		{
			HTMLService.showHTML(p, SurveyFactory.buildAssaultBattleGroundReport(p), 152000001);
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
	
}
