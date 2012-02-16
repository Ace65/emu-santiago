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

package gameserver.services;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.staticdoor.StaticDoorTemplate;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.utils.PacketSendUtility;

/**
 * @author Pashatr
 */
 
public class StaticDoorService
{

	public static StaticDoorService getInstance()
	{
		return SingletonHolder.instance;
	}

	public void openStaticDoor(Player player, int doorId) {
		if(player.getAccessLevel() >= 1) {
			PacketSendUtility.sendMessage(player, "[Admin Info]Door ID: " + doorId);
		}
		StaticDoorTemplate template = DataManager.STATICDOOR_DATA.getStaticDoorTemplate(doorId);
		int keyId = 1;
		int mapId = 0;		

		if(template != null) {
			keyId = template.getKeyId();
			mapId = template.getMapId();
		}
		
		if(player.getAccessLevel() >= 1) {
			PacketSendUtility.sendMessage(player, "[Admin Info]Key ID: " + keyId);
		}
		if(checkStaticDoorKey(player, doorId, keyId, mapId))
			PacketSendUtility.broadcastPacketAndReceive(player, new SM_EMOTION(doorId));
		else
			PacketSendUtility.sendMessage(player,"For opening this door you need [item:" + keyId + "]");
	}

	public boolean checkStaticDoorKey(Player player, int doorId, int keyId, int mapId)
  {
    if (keyId == 0)
      return false;
    if (keyId == 1) {
      return true;
    }

    if (player.getWorldId() != mapId) {
      return true;
    }

    if (hasItem(player, keyId)) {
      player.getInventory().removeFromBagByItemId(keyId, 1);
      return true;
    }

    return false;
  }

  private boolean hasItem(Player player, int itemId) {
    return player.getInventory().getItemCountByItemId(itemId) > 0;
  }

  private static class SingletonHolder
  {
    protected static final StaticDoorService instance = new StaticDoorService();
  }
}