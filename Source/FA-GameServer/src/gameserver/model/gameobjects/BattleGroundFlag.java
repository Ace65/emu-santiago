package gameserver.model.gameobjects;

import gameserver.ai.npcai.NpcAi;
import gameserver.controllers.BattleGroundFlagController;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author ambrosius
 *
 */
public class BattleGroundFlag extends Npc
{
	
	public enum BattleGroundFlagState
	{
		ON_FIELD,
		AT_BASE
	}
	
	public BattleGroundFlagState state = BattleGroundFlagState.AT_BASE;
	
	private Player flagHolder = null;
	
	public BattleGroundFlag(int objId, BattleGroundFlagController controller, SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate);
	}

	@Override
	public BattleGroundFlagController getController()
	{
		return (BattleGroundFlagController) super.getController();
	}
	public BattleGroundFlag getOwner()
	{
		return (BattleGroundFlag)this;
	}
	
	@Override
	public void initializeAi()
	{
		this.ai = new NpcAi();
		ai.setOwner(this);
	}
	
	@Override
	public boolean isEnemy(VisibleObject visibleObject)
	{
		return false;
	}
	
	@Override
	protected boolean isEnemyNpc(Npc visibleObject)
	{
		return false;
	}

	@Override
	protected boolean isEnemyPlayer(Player visibleObject)
	{
		return false;
	}
	
	@Override
	protected boolean isEnemySummon(Summon summon)
	{
		return false;
	}

	@Override
	public NpcObjectType getNpcObjectType()
	{
		return NpcObjectType.NORMAL;
	}
	
	private Race race;

	public Race getRace()
	{
		return race;
	}

	public void setRace(Race race)
	{
		this.race = race;
	}

	public Player getFlagHolder()
	{
		return flagHolder;
	}

	public void setFlagHolder(Player flagHolder)
	{
		this.flagHolder = flagHolder;
	}
	
}
