/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;
import gameserver.world.WorldMapType;

/**
 * Admin moveplayerto command
 * 
 * @author D3x
 */

public class MovePlayerTo extends AdminCommand
{

	/**
	 * Constructor.
	 */

	public MovePlayerTo()
	{
		super("moveplayerto");
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_MOVEPLAYERTO)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params == null || params.length < 5)
		{
			PacketSendUtility.sendMessage(admin, "syntax //moveplayerto <player name> <world Id> <X> <Y> <Z>");
			return;
		}

		Player playerToMove = World.getInstance().findPlayer(Util.convertName(params[0]));

		if (playerToMove == null)
		{
			PacketSendUtility.sendMessage(admin, "The specified player is not online.");
			return;
		}
		
		int worldId;
		float x, y, z;
		
		try
		{
			worldId = Integer.parseInt(params[1]);
			x = Float.parseFloat(params[2]);
			y = Float.parseFloat(params[3]);
			z = Float.parseFloat(params[4]);
		}
		catch(NumberFormatException e)
		{
			PacketSendUtility.sendMessage(admin, "WorldID, x, y and z should be numbers!");
			return;
		}
		
		if (WorldMapType.getWorld(worldId) == null)
		{
			PacketSendUtility.sendMessage(admin, "Illegal WorldId %d " + worldId );
		}
		else
		{
			TeleportService.teleportTo(playerToMove, worldId, x, y, z, 0);
			PacketSendUtility.sendMessage(admin, "Teleported player " + playerToMove.getName() + " to " + x + " " + y + " " + z + " [" + worldId + "]");
			PacketSendUtility.sendMessage(playerToMove, "You have been teleported by an Administrator.");
		}
	}
}
