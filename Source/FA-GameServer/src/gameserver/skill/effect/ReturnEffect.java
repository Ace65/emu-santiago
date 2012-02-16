/*
 * This file is part of aion-unique <aion-unique.com>.
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

import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.StorageType;
import gameserver.services.TeleportService;
import gameserver.skill.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import br.focus.battleground.BattleGround;


/**
 * @author ATracer
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReturnEffect")
public class ReturnEffect extends EffectTemplate
{

	@Override
	public void applyEffect(Effect effect)
	{
		Player player = (Player) effect.getEffector();
		BattleGround battleground = player.getBattleGround();
		if(battleground != null)
		{
			battleground.broadcastToBattleGround(player.getName() + " has left the battleground.", player.getCommonData().getRace());
			battleground.removePlayer(player);
			player.setBattleGround(null);
			if(player.battlegroundObserve != 0)
			{
			    if(player.battlegroundBetE > 0){
			    	player.getStorage(StorageType.CUBE.getId()).increaseKinah(player.battlegroundBetE);
			    	player.battlegroundBetE = 0;}
			    else if(player.battlegroundBetA > 0){
			    	player.getStorage(StorageType.CUBE.getId()).increaseKinah(player.battlegroundBetA);
			    	player.battlegroundBetA = 0;}
			    
			    player.battlegroundObserve = 0;
			}
		}
		
		TeleportService.moveToBindLocation((Player) effect.getEffector(), true, 500);
	}

	@Override
	public void calculate(Effect effect)
	{
		if(effect.getEffected().isSpawned())
			super.calculate(effect);
	}
}
