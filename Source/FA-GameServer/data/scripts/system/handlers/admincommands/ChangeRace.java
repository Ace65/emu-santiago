/*
 * Copyright (c) 2012 by Aion Fantasy
 *
 * This file is part of Aion Fantasy <http://aionfantasy.com>.
 *
 * Aion Fantasy <http://www.aionfantasy.com> is free software: you
 * can  redistribute  it and/or modify it under the terms
 * of  the GNU General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * Aion Fantasy <http://www.aionfantasy.com> is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without  even  the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See  the  GNU General Public License for more details.
 * You  should  have  received  a copy of the GNU General
 * Public License along with Aion Fantasy 
 * <http://www.aionfantasy.com>.If not,see <http://www.gnu.org/licenses/>.
 */

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.Race;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

/**
 * @author DoYrdenDzirt
 * 
 **/

public class ChangeRace extends AdminCommand {

    public ChangeRace() {
        super("crace");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        VisibleObject target = admin.getTarget();
        final Player player;
        
        if(admin.getAccessLevel() < AdminConfig.COMMAND_RACE)
        {
                PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
                        return;
        }
        
        if(target == null)
        {
                PacketSendUtility.sendMessage(admin, "You should select a player first!");
                return;
        }
        
        if(target instanceof Player)
                player = (Player) target;
        else
        {
                PacketSendUtility.sendMessage(admin, "Target must be an instance of Player!");
                return;
        }
        
        if(player.getCommonData().getRace() == Race.ASMODIANS)
                player.getCommonData().setRace(Race.ELYOS);
        else
                player.getCommonData().setRace(Race.ASMODIANS);

        PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
        World.getInstance().despawn(player);
        World.getInstance().spawn(player);
                
    }
    

}