/*
 * Copyright (c) 2011 by Holgra
 *
 * This file is part of Aion Extreme <http://aion-core.net>.
 *
 * Aion Extreme <http://www.aion-core.net> is free software: you
 * can  redistribute  it and/or modify it under the terms
 * of  the GNU General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * Aion Extreme <http://www.aion-core.net> is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without  even  the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See  the  GNU General Public License for more details.
 * You  should  have  received  a copy of the GNU General
 * Public License along with Aion Extreme 
 * <http://www.aion-core.net>.If not,see <http://www.gnu.org/licenses/>.
 */



package gameserver.model.gameobjects;

import gameserver.ai.npcai.NpcAi;
import gameserver.controllers.BattleGroundAgentController;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author ambrosius
 *
 */
public class BattleGroundAgent extends Npc
{
	public BattleGroundAgent(int objId, BattleGroundAgentController controller, SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate);
	}

	@Override
	public BattleGroundAgentController getController()
	{
		return (BattleGroundAgentController) super.getController();
	}
	public BattleGroundAgent getOwner()
	{
		return (BattleGroundAgent)this;
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
	
}
