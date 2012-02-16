package gameserver.network.aion.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;


/**
 * 
 * @author kamui
 * 
 */
public class SM_STANCE_STATE extends AionServerPacket
{
	private int		playerObjectId;
	private int		stateId;

	public SM_STANCE_STATE(int playerObjectId, int stateId)
	{
		this.playerObjectId = playerObjectId;
		this.stateId = stateId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, playerObjectId);
		writeC(buf, stateId);
	}
}
