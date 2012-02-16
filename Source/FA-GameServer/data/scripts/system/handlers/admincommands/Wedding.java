/*
 * Copyright (c) 2011 DeepBlue
 * 
 * This file is part of Aion Extreme <http://www.aion-core.net>.
 * 
 * Aion Extreme <http://www.aion-core.net> is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, version 3 of the License.
 * 
 * Aion Extreme <http://www.aion-core.net> is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with  Aion Extreme
 *  <http://www.aion-core.net>. If not, see <http://www.gnu.org/licenses/>.
 */
 
package admincommands;

import commons.database.dao.DAOManager;
import gameserver.configs.administration.AdminConfig;
import gameserver.dao.PlayerDAO;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

/**
 * @author ZornGottes
 * 
 */
public class Wedding extends AdminCommand {

    public Wedding() {
        super("wedding");
    }

    /**
     * TODO: - Only GMs can marry 2 people - Partner can port to his Partner
     * .gotolove - Couples get double AP while in party
     * Penalty) => Conf file plus both Yes / No - Marriage Clothing
     * to be married must have - Wedding Dress / Tuxedo => Taha drop
     */
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_WEDDING) {
            PacketSendUtility.sendMessage(admin,
                    "You dont have enough rights to execute this command");
            return;
        }
        if (params.length == 2) {
            PlayerDAO playerDAO = DAOManager.getDAO(PlayerDAO.class);
            // AionObject target = admin.getTarget();
            Player partner1 = World.getInstance().findPlayer(
                    Util.convertName(params[0]));
            Player partner2 = World.getInstance().findPlayer(
                    Util.convertName(params[1]));

            if (params[0].equalsIgnoreCase(params[1])) {
                PacketSendUtility.sendMessage(admin,
                        "You can't marry yourself dumb");
                return;
            }
            if (partner1 == null && partner2 == null) {
                PacketSendUtility.sendMessage(admin,
                        "Your Partner must be Online");
                return;
            }
            for (Item item : partner1.getEquipment().getEquippedItems()) {
                if (item.getItemId() == 110900135) {
                    partner1.getCommonData().setEngaged(true);
                } else if (item.getItemId() == 110900084) {
                    partner1.getCommonData().setEngaged(true);
                } else if (item.getItemId() == 110900060) {
                    partner1.getCommonData().setEngaged(true);
                } else if (item.getItemId() == 110900115) {
                    partner1.getCommonData().setEngaged(true);
                } else if (item.getItemId() == 110900078) {
                    partner1.getCommonData().setEngaged(true);
                }
            }
            for (Item item : partner2.getEquipment().getEquippedItems()) {
                if (item.getItemId() == 110900135) {
                    partner2.getCommonData().setEngaged(true);
                } else if (item.getItemId() == 110900084) {
                    partner2.getCommonData().setEngaged(true);
                } else if (item.getItemId() == 110900060) {
                    partner2.getCommonData().setEngaged(true);
                } else if (item.getItemId() == 110900115) {
                    partner2.getCommonData().setEngaged(true);
                } else if (item.getItemId() == 110900078) {
                    partner1.getCommonData().setEngaged(true);
                }
            }

            if (partner1.getCommonData().getPartner() != null
                    && partner2.getCommonData().getPartner() != null) {
                PacketSendUtility.sendMessage(admin,
                        "One of the candidate has allready a wife/husband");
                return;
            }
            if (partner1.getCommonData().getEngaged()
                    && partner2.getCommonData().getEngaged()) {
                partner1.getCommonData().setPartner(partner2.getName());
                partner2.getCommonData().setPartner(partner1.getName());
                partner1.getCommonData().setMarriage(true);
                partner2.getCommonData().setMarriage(true);
                playerDAO.storePlayer(partner1);
                playerDAO.storePlayer(partner2);
                TeleportService.teleportTo(partner1, partner1.getWorldId(),
                        partner1.getInstanceId(), partner1.getX(),
                        partner1.getY(), partner1.getZ(),
                        partner1.getHeading(), 0);
                TeleportService.teleportTo(partner2, partner2.getWorldId(),
                        partner2.getInstanceId(), partner2.getX(),
                        partner2.getY(), partner2.getZ(),
                        partner2.getHeading(), 0);
            } else {
                PacketSendUtility.sendMessage(admin,
                        "Both must Equiped with Wedding Dress/Tuxedo");
                return;
            }

        } else {
            PacketSendUtility.sendMessage(admin,
                    "syntax: //wedding <playerName1> <playerName2>");
            return;
        }
    }
}
