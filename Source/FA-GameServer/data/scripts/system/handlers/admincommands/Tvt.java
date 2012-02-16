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

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.TvtInstanceService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

/*
 *
 * @author Khaos
 * DiamondCore Studio (http://www.diamondcore-mmorpgs.com)
 * Script exclusive for Aion Extreme
 *
*/

public class Tvt extends AdminCommand {
    public Tvt() {
        super("tvt");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        // TODO Auto-generated method stub

        if (admin.getAccessLevel() < AdminConfig.COMMAND_TVT) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
            return;
        }
        if (params.length == 0) {
            PacketSendUtility.sendMessage(admin, "Syntax : //tvt <register | unregister>");
            PacketSendUtility.sendMessage(admin, "Syntax : //tvt <start | reset | time | autorun>");
            return;
        }
        if (params[0].equals("register")) {
            TvtInstanceService.getInstance().registerPlayer(admin);
            return;
        }
        if (params[0].equals("unregister")) {
            TvtInstanceService.getInstance().unregisterPlayer(admin);
            return;
        } else if (params[0].equals("start")) {
            if (TvtInstanceService.getInstance().loadTvt()) {
                PacketSendUtility.sendMessage(admin, "TvT successfully started");
            } else {
                PacketSendUtility.sendMessage(admin, "An error has occured");
            }
        } else if (params[0].equals("reset")) {
            TvtInstanceService.getInstance().reset();
            PacketSendUtility.sendMessage(admin, "TvT successfully resetted");
        } else if (params[0].equals("time")) {
            try {
                int time = Integer.parseInt(params[1]);
                TvtInstanceService.getInstance().setTimer(time);
            }
            catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "Time must be an integer (in minutes)");
            }
        } else if (params[0].equals("autorun")) {
            if (params.length == 1) {
                PacketSendUtility.sendMessage(admin, "Syntax : //tvt autorun <on | off>");
            } else if (params[1].equals("on")) {
                TvtInstanceService.getInstance().setAuto(true);
                PacketSendUtility.sendMessage(admin, "TvT is now set to autostart");
            } else if (params[1].equals("off")) {
                TvtInstanceService.getInstance().setAuto(false);
                PacketSendUtility.sendMessage(admin, "TvT is now set to manual");
            } else {
                PacketSendUtility.sendMessage(admin, "Synthax : //tvt autorun <on | off>");
            }
        }
    }
}
