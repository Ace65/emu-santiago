/**
 * This file is part of aion-unique <aion-unique.com>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.network.aion.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.quest.model.QuestCookie;
import gameserver.services.QuestService;

import java.nio.ByteBuffer;
import java.util.List;


/**
 * @author MrPoke
 */

public class SM_NEARBY_QUESTS extends AionServerPacket
{
	private Integer[] questIds;
	private int size;
	
	public SM_NEARBY_QUESTS(List<Integer> questIds)
	{
		this.questIds = questIds.toArray(new Integer[questIds.size()]);
		this.size = questIds.size();
	}


	@Override
 	protected void writeImpl(AionConnection con, ByteBuffer buf)
 	{
		if(questIds == null || con.getActivePlayer() == null)
			return;

		writeC(buf, 0x00);
		writeH(buf, 0xFFFF - size - 1);

  		for(int id : questIds)
  		{
			writeH(buf, id);
			if (QuestService.canStart(new QuestCookie(null, con.getActivePlayer(), id, 0)))
				writeH(buf, 0);
			else
				writeH(buf, 2);
  		}
 	}
}
