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

import gameserver.model.gameobjects.BattleGroundAgent;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.utils.PacketSendUtility;
import br.focus.battleground.BattleGroundManager;

/**
 * @author ambrosius / Holgrabus
 *
 */
public class BattleGroundAgentController extends NpcController
{	
	@Override
	public void onDialogRequest(final Player player)
	{
		if(player.getCommonData().getRace() != getOwner().getObjectTemplate().getRace())
			return;
		if(player.battlegroundWaiting)
		{
			PacketSendUtility.sendMessage(player, "You are already registered in a battleground.");
		}
		else
		{
		    String message = "Do you want to register in a battleground ?";
	        RequestResponseHandler responseHandler = new RequestResponseHandler(player){

	            public void acceptRequest(Creature requester, Player responder)
	            {
	                BattleGroundManager.sendRegistrationForm(player);
	                return;
	            }

	            public void denyRequest(Creature requester, Player responder){ return; }
	        };
	        boolean requested = player.getResponseRequester().putRequest(902247, responseHandler);
	        if(requested){PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(902247, 0, message));return;}
		}
	}

	@Override
	public BattleGroundAgent getOwner()
	{
		return  (BattleGroundAgent) super.getOwner();
	}
}
