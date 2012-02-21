/*
 * This file is part of aionfantasy <aionfantasy.com>.
 *
 *  aionfantasy is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aionfantasy is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package usercommands;

import commons.database.dao.DAOManager;

import gameserver.controllers.PlayerController;
import gameserver.dao.PlayerDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.UserCommand;
import gameserver.world.World;
import gameserver.world.WorldPosition;

public class Repair extends UserCommand {
        
        public Repair() {
        super("repair");
    }

    @Override
    public void executeCommand(Player player, String arg) {
        WorldPosition position  = null;
        Player repairPlayer             = null;
        PlayerCommonData pcd    = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonDataByName(arg);
        int accountId                   = DAOManager.getDAO(PlayerDAO.class).getAccountIdByName(arg);
        
        if(accountId != player.getAccount().getId())
        {
                PacketSendUtility.sendMessage(player, "You cannot repair that player");
                return;
        }
        
        if(player.getName().toLowerCase().equalsIgnoreCase(pcd.getName().toLowerCase()))
        {
                PacketSendUtility.sendMessage(player, "You cannot repair yourself");
                return;
        }
        
        switch(pcd.getRace())
        {
                case ELYOS:
                        position = World.getInstance().createPosition(210010000, 1212.9423f, 1044.8516f, 140.75568f, (byte) 32);
                        pcd.setPosition(position, true);
                        break;
                case ASMODIANS:
                        position = World.getInstance().createPosition(220010000, 571.0388f, 2787.3420f, 299.8750f, (byte) 32);
                        pcd.setPosition(position, true);
                        break;
        }
        
        repairPlayer = new Player(new PlayerController(), pcd, null, player.getAccount());
        
        DAOManager.getDAO(PlayerDAO.class).storePlayer(repairPlayer);
        PacketSendUtility.sendMessage(player, "Player " + pcd.getName() + " was repaired");
    }

}