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
package gameserver.skill.effect;

import gameserver.configs.main.CustomConfig;
import gameserver.geo.GeoEngine;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_FORCED_MOVE;
import gameserver.skill.action.DamageType;
import gameserver.skill.model.Effect;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * @author ATracer
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BackDashEffect")
public class BackDashEffect extends DamageEffect
{
	
	@Override
	public void calculate(Effect effect)
	{
		super.calculate(effect, DamageType.PHYSICAL, true);
	}
	
	@Override
	public void applyEffect(Effect effect)
	{
		super.applyEffect(effect);
		if (CustomConfig.GEODATA_EFFECTS_ENABLED)
		{
			Player player = (Player)effect.getEffector();
			double radian = Math.toRadians(MathUtil.convertHeadingToDegree(player.getHeading()));
			float x = player.getX();
			float y = player.getY();
			float z = player.getZ();
			int worldId = player.getWorldId();

			float x2 = (float)(x + (25 * Math.cos(Math.PI+radian)));
			float y2 = (float)(y + (25 * Math.sin(Math.PI+radian)));
			float lastSee = 0;
			float lastNonSee = 25; //25 metres dash

			//if can see the final point then just dash
			if (GeoEngine.getInstance().canSee(worldId, x, y, z, x2, y2, z))
			{
				this.startDash(effect, x2, y2, GeoEngine.getInstance().getZ(worldId, x2, y2, z));
				return;
			}

			float temp = 0;

			while (lastNonSee - lastSee >= 0.5)
			{
				temp = (lastNonSee - lastSee)/2 + lastSee;
				x2 = (float)(x + (temp * Math.cos(Math.PI+radian)));
				y2 = (float)(y + (temp * Math.sin(Math.PI+radian)));

				if (GeoEngine.getInstance().canSee(worldId, x, y, z, x2, y2, z))
					lastSee = temp;
				else
					lastNonSee = temp;
			}

			//set final coordinates
			x2 = (float)(x + (lastSee * Math.cos(Math.PI+radian)));
			y2 = (float)(y + (lastSee * Math.sin(Math.PI+radian)));

			//start dash
			this.startDash(effect, x2, y2, GeoEngine.getInstance().getZ(worldId, x2, y2, z));
		}
	}

	private void startDash(Effect effect, float x, float y, float z)
	{
		Player player = (Player)effect.getEffector();
		World.getInstance().updatePosition(player, x, y, z,player.getHeading(), false);
		PacketSendUtility.broadcastPacket(player, new SM_FORCED_MOVE(player, x, y, z), true);
	}
}
