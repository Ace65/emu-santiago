/**
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
package gameserver.skill.effect;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.skill.model.Effect;
import gameserver.spawn.SpawnEngine;
import gameserver.utils.ThreadPoolManager;
import gameserver.model.templates.spawn.SpawnTemplate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * @author Simple
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SummonEffect")
public class SummonEffect extends EffectTemplate
{
	@XmlAttribute(name = "npc_id", required = true)
	protected int	npcId;
	@XmlAttribute(required = true)
	protected int	time;

	@Override
	public void applyEffect(Effect effect)
	{
		if (npcId == 201054 || npcId == 201055 || npcId == 201056 || npcId == 201057){ //Siege Weapons
			Creature effector = effect.getEffector();
			SpawnEngine spawnEngine = SpawnEngine.getInstance();
			float x = effector.getX();
			float y = effector.getY();
			float z = effector.getZ();
			byte heading = effector.getHeading();
			int worldId = effector.getWorldId();
			int instanceId = effector.getInstanceId();
		
			SpawnTemplate spawn = spawnEngine.addNewSpawn(worldId, instanceId, npcId, x, y, z, heading, 0, 0, true, true);
			final Npc weaponSiege = spawnEngine.spawnWeaponSiege(spawn, instanceId);
			weaponSiege.getKnownList().doUpdate();
			if (time != 0){
				ThreadPoolManager.getInstance().schedule(new Runnable(){

					@Override
					public void run()
					{
						weaponSiege.getLifeStats().reduceHp(10000, weaponSiege, true);
					}
				}, time * 1000);
			}
		} else {		
			Creature effected = effect.getEffected();	
			effected.getController().createSummon(npcId, effect.getSkillLevel());
		}
	}

	@Override
	public void calculate(Effect effect)
	{
		if(effect.getEffected() instanceof Player)
			effect.addSucessEffect(this);
	}
}
