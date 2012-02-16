/**
 * This file is part of Aion Extreme <aion-core.net>
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package usercommands;

import gameserver.model.gameobjects.player.Player;
import gameserver.services.TvtInstanceService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.UserCommand;

/*
 *
 * @author Khaos
 * DiamondCore Studio (http://www.diamondcore-mmorpgs.com)
 * Script exclusive for Aion Extreme
 *
*/

public class Tvt extends UserCommand {
    public Tvt() {
        super("tvt");
    }

    @Override
    public void executeCommand(Player player, String params) {
        String[] args = params.split(" ");

        if (args.length != 1) {
            PacketSendUtility.sendMessage(player, "syntax: .tvt <register | unregister>");
            return;
        }

        if (args[0].equalsIgnoreCase("register")) {
            TvtInstanceService.getInstance().registerPlayer(player);
            return;
        }

        if (args[0].equalsIgnoreCase("unregister")) {
            TvtInstanceService.getInstance().unregisterPlayer(player);
            return;
        }

        PacketSendUtility.sendMessage(player, "syntax: .tvt <register | unregister>");

    }

}
