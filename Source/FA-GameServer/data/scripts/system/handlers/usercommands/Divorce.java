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

import commons.database.dao.DAOManager;
import gameserver.dao.PlayerDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.UserCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;
import gameserver.world.World;

/**
 * @author DeepBlue
 * 
 */
public class Divorce extends UserCommand {
    public Divorce() {
        super("divorce");
    }

    @Override
    public void executeCommand(Player player, String params) {
        if (!player.getCommonData().getMarriage()) {
            PacketSendUtility.sendMessage(player, LanguageHandler
                    .translate(CustomMessageId.WEDDING_NOT_MARRIED));
            return;
        }
        PlayerDAO playerDAO = DAOManager.getDAO(PlayerDAO.class);
        String partnerName = player.getCommonData().getPartner();
        Player player_partner = World.getInstance().findPlayer(
                Util.convertName(partnerName));
        if (player_partner == null) {
            PacketSendUtility.sendMessage(player, LanguageHandler
                    .translate(CustomMessageId.WEDDING_DIVORCE_OFFLINE));
            return;
        }
        PlayerCommonData partner = player_partner.getCommonData();

        player.getCommonData().setMarriage(false);
        partner.setMarriage(false);

        player.getCommonData().setPartner(null);
        partner.setPartner(null);

        playerDAO.storePlayer(player);
        playerDAO.storePlayer(partner.getPlayer());
        PacketSendUtility.sendMessage(player, LanguageHandler.translate(
                CustomMessageId.WEDDING_DIVORCE_ALL_OK,
                player_partner.getName()));
        TeleportService.teleportTo(player, player.getWorldId(),
                player.getInstanceId(), player.getX(), player.getY(),
                player.getZ(), player.getHeading(), 0);
        TeleportService.teleportTo(player_partner, player_partner.getWorldId(),
                player_partner.getInstanceId(), player_partner.getX(),
                player_partner.getY(), player_partner.getZ(),
                player_partner.getHeading(), 0);
    }
}
