package br.focus.battleground;

import gameserver.dataholders.DataManager;
import gameserver.model.Race;
import gameserver.model.gameobjects.Executor;
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
public class CTFBattleGround extends BattleGround
{
	
	private int elyosFlagCount = 0;
	private int asmosFlagCount = 0;
	
	public CTFBattleGround(int tplId, WorldMapInstance instance)
	{
		super(tplId, instance);
	}
	
	public void score(Race race)
	{
		if(race == Race.ELYOS)
			elyosFlagCount++;
		else
			asmosFlagCount++;
		if(elyosFlagCount >= template.getTargetScore() || asmosFlagCount >= template.getTargetScore())
			end();
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
		
		/*
		 * Spawn Flags
		 */
		SpawnTemplate sfe = SpawnEngine.getInstance().addNewSpawn(bgTemplate.getWorldId(), instance.getInstanceId(), 700336, bgTemplate.getFlagLocation().getXe(),bgTemplate.getFlagLocation().getYe(),bgTemplate.getFlagLocation().getZe(), bgTemplate.getFlagLocation().getHe(), 0, 0, false);
		SpawnTemplate sfa = SpawnEngine.getInstance().addNewSpawn(bgTemplate.getWorldId(), instance.getInstanceId(), 700037, bgTemplate.getFlagLocation().getXa(),bgTemplate.getFlagLocation().getYa(),bgTemplate.getFlagLocation().getZa(), bgTemplate.getFlagLocation().getHa(), 0, 0, false);
		
		SpawnEngine.getInstance().spawnBGHealer(se, instance.getInstanceId(), Race.ELYOS);
		SpawnEngine.getInstance().spawnBGHealer(sa, instance.getInstanceId(), Race.ASMODIANS);
		
		SpawnEngine.getInstance().spawnBGFlag(sfe, instance.getInstanceId(), Race.ELYOS);
		SpawnEngine.getInstance().spawnBGFlag(sfa, instance.getInstanceId(), Race.ASMODIANS);
		
		super.start();
		
	}
	
	@Override
	public void end()
	{
		super.end();
		
		if(elyosFlagCount > asmosFlagCount)
		{
			instance.doOnAllPlayers(new Executor<Player>(){
				@Override
				public boolean run(Player player)
				{
					if(player.getCommonData().getRace() == Race.ELYOS)
						player.getBattleGround().increasePoints(player, player.getBattleGround().getTemplate().getRules().getCTFReward());
					return true;
				}
			}, true);
		}
		else if(elyosFlagCount == asmosFlagCount)
        {
            instance.doOnAllPlayers(new Executor<Player>(){
                @Override
                public boolean run(Player player)
                {
                    player.getBattleGround().increasePoints(player, player.getBattleGround().getTemplate().getRules().getCTFReward());
                    return true;
                }
            }, true);
        }
		else
		{
			instance.doOnAllPlayers(new Executor<Player>(){
				@Override
				public boolean run(Player player)
				{
					if(player.getCommonData().getRace() == Race.ASMODIANS)
						player.getBattleGround().increasePoints(player, player.getBattleGround().getTemplate().getRules().getCTFReward());
					return true;
				}
			}, true);
		}
		
		instance.doOnAllPlayers(new Executor<Player>(){
			
			@Override
			public boolean run(Player object)
			{
				HTMLService.showHTML(object, SurveyFactory.buildCTFBattleGroundReport(object), 152000001);
	            object.getEffectController().removeAllEffects();
	            if(!object.getLifeStats().isAlreadyDead())
	            {
	                if(object.getCommonData().getRace() == Race.ELYOS)
	                    TeleportService.teleportTo(object, 110010000, 1374, 1399, 573, 0);
	                else
	                    TeleportService.teleportTo(object, 120010000, 1324, 1550, 210, 0);
	            }
				return true;
			}
		});
		
	}

	public int getElyosFlagCount()
	{
		return elyosFlagCount;
	}

	public int getAsmosFlagCount()
	{
		return asmosFlagCount;
	}
	
}
