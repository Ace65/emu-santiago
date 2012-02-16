/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.focus.utils;

import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.utils.PacketSendUtility;

/**
 * @author ambrosius / adapted DESTRUCTOR/HADESH
 *
 */
public class UIUtils
{
	public static void sendPopupRequest(Player player, String message, RequestResponseHandler handler)
	{
		boolean request = player.getResponseRequester().putRequest(901756, handler);
		if(request)
		{
			PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(901756, player.getObjectId(), message));
		}
	}
}
