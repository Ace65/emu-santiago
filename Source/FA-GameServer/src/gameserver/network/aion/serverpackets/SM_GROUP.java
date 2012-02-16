/*
 * This file is part of aion-unique <aion-unique.org>.
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

import gameserver.model.group.PlayerGroup;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;



/**
 * @author zer0patches
 * Unknown use at this point. Sends task 1 with player id and group id to each player in group.
 */
public class SM_GROUP extends AionServerPacket
{
	private int groupid;
	private int playerId;
	private int task;


	public SM_GROUP(PlayerGroup group, int playerId, int task)
	{
		this.groupid = group.getGroupId();
		this.playerId = playerId;
		this.task = task;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
	    switch(task){
           case 0: 
                // Player Logout from Group? Group Expire? Unknown at this point.
                // May be used to exclude player from group benefits? Out of range?
                writeC(buf, 0x00);
                writeD(buf, playerId);
                writeD(buf, 0x00);
                return;
	        case 1: 
	            // Add Players to Group, Sent to each group member for each member.
	            writeC(buf, 0x01);
	            writeD(buf, playerId);
                writeD(buf, groupid);
	            return;
	    }
	}
}
