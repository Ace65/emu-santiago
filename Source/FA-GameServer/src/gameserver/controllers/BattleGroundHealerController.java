/*
 * Copyright (c) 2011 by Aion Extreme
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

package gameserver.controllers;

import java.util.Collection;

import br.focus.factories.SurveyFactory;
import gameserver.controllers.attack.AttackStatus;
import gameserver.model.Race;
import gameserver.model.gameobjects.BattleGroundHealer;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.services.HTMLService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

/**
 * @author ambrosius / Holgrabus
 *
 */
public class BattleGroundHealerController extends NpcController
{	
	@Override
	public void onAttack(final Creature creature, int skillId, TYPE type, int damage, AttackStatus status, boolean notifyAttackedObservers)
	{
		return;
	}

	@Override
	public void onDialogRequest(final Player player)
	{
	    if(player.getCommonData().getRace() != getOwner().getRace())
            return;
	    
	    Collection<Player> players = World.getInstance().getPlayers();
	    int BgMap = player.getWorldId();
	    int BgInstanceId = player.getInstanceId();
	    int BattleGroundPlayers = 0;
		for(Player p : players)
		{
			if (p.getWorldId() == BgMap && p.getInstanceId() == BgInstanceId)
				BattleGroundPlayers += 1;
		}
		
	    if(BattleGroundPlayers <= 1)
		{
			if(player.getCommonData().getRace() == Race.ELYOS)
                TeleportService.teleportTo(player, 110010000, 1374, 1399, 573, 0);
            else
                TeleportService.teleportTo(player, 120010000, 1324, 1550, 210, 0);
			
			PacketSendUtility.sendMessage(player, "You were alone in the battleground, you have been teleported back.");
		}
	    else if(!player.getBattleGround().running && !player.battlegroundWaiting)
		{
			if(player.getCommonData().getRace() == Race.ELYOS)
                TeleportService.teleportTo(player, 110010000, 1374, 1399, 573, 0);
            else
                TeleportService.teleportTo(player, 120010000, 1324, 1550, 210, 0);
			
			PacketSendUtility.sendMessage(player, "You were alone in the battleground, you have been teleported back.");
		}
	    else
	    {
	    	//Show rank
	    	player.battlegroundRequestedRank = true;
	    	HTMLService.showHTML(player, SurveyFactory.getBattleGroundRanking(player.getBattleGround()), 152000001);
	    }
	}

	@Override
	public BattleGroundHealer getOwner()
	{
		return  (BattleGroundHealer) super.getOwner();
	}
}
