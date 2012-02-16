/*
 * Copyright (c) 2011 DeepBlue
 * 
 * This file is part of Open-Aion <http://open-aion.org>.
 * 
 * Open-Aion <http://open-aion.org> is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, version 3 of the License.
 * 
 * Open-Aion <http://open-aion.org> is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with Open-Aion
 * <http://open-aion.org>. If not, see <http://www.gnu.org/licenses/>.
 */

package usercommands;

import java.util.Map;

import javolution.util.FastMap;

import gameserver.configs.main.GSConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.UserCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;
import gameserver.world.World;

/**
 * @author DeepBlue, Dev101
 * 
 */
public class GotoLove extends UserCommand {

    public GotoLove() {
        super("gotolove");
    }
    private static Map<Integer, Long> lastUsage = new FastMap<Integer, Long>();
    @Override
    public void executeCommand(Player player, String params) {
    	 if(lastUsage.containsKey(player.getObjectId())) {
			 if((System.currentTimeMillis() - lastUsage.get(player.getObjectId())) < GSConfig.GOTOLOVE_COOLDOWN*1000) {
				 PacketSendUtility.sendMessage(player, "You cannot use this command more than every 10 Minutes! Seconds left untill you can use the command again:" + (GSConfig.GOTOLOVE_COOLDOWN*1000 -(System.currentTimeMillis() - lastUsage.get(player.getObjectId())))/1000);
				 return;
			 }
		 }
    	
    	if (!player.getCommonData().getMarriage()) {
            PacketSendUtility.sendMessage(player, LanguageHandler
                    .translate(CustomMessageId.WEDDING_NOT_MARRIED));
            return;
        }
        Player partner = World.getInstance().findPlayer(
                Util.convertName(player.getCommonData().getPartner()));

        if (partner == null) {
            PacketSendUtility.sendMessage(player, LanguageHandler
                    .translate(CustomMessageId.WEDDING_LOVE_NOT_ONLINE));
            return;
        }
        // If someone in Jail Disallow
        if (player.isInPrison() || partner.isInPrison()) {
            PacketSendUtility.sendMessage(player, LanguageHandler
                    .translate(CustomMessageId.WEDDING_LOVE_NOT_PORTALBLE));
            return;
        }
        // If in Instance Disallow
        if (player.isInInstance() || partner.isInInstance()) {
            PacketSendUtility.sendMessage(player, LanguageHandler
                    .translate(CustomMessageId.WEDDING_LOVE_NOT_PORTALBLE));
            return;
        }
        TeleportService.teleportTo(player, partner.getWorldId(),
                partner.getInstanceId(), partner.getX(), partner.getY(),
                partner.getZ(), partner.getHeading(), GSConfig.GOTOLOVE_TELEPORTDELAY*1000);
        lastUsage.put(player.getObjectId(), new Long(System.currentTimeMillis()));

    }

}
