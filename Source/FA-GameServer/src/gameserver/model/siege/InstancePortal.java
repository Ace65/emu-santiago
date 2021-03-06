/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.model.siege;

import gameserver.controllers.PortalController;
import gameserver.model.gameobjects.Npc;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author Sylar, Dev101
 *
 */
public class InstancePortal extends Npc
{
	private int fortressId;	
	private int spawnStaticId;
	private SiegeRace race;	
	
	public InstancePortal(int objId, PortalController controller, SpawnTemplate spawn, VisibleObjectTemplate objectTemplate, int fortressId, int staticId, SiegeRace race) {
		super(objId, controller, spawn, objectTemplate);
		this.fortressId = fortressId;
		this.spawnStaticId = staticId;
		this.race = race;
	}
	
	public int getFortressId()
	{
		return fortressId;
	}
	
	public int getStaticId()
	{
		return spawnStaticId;
	}

    public SiegeRace getRace() {
    	return race;
    }	
	
}
