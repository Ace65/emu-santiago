package chatserver.network.gameserver;

import org.jboss.netty.buffer.ChannelBuffer;

import chatserver.common.netty.BaseServerPacket;
import chatserver.network.netty.handler.GameChannelHandler;


/**
 * @author ATracer
 */
public abstract class AbstractGameServerPacket extends BaseServerPacket
{
	/**
	 * 
	 * @param opCode
	 */
	public AbstractGameServerPacket(int opCode)
	{
		super(opCode);
	}
	
	/**
	 * 
	 * @param gameChannelHandler
	 * @param buf
	 */
	public void write(GameChannelHandler gameChannelHandler, ChannelBuffer buf)
	{
		writeH(buf, 0);
		writeImpl(gameChannelHandler, buf);
	}
	
	/**
	 * 
	 * @param gameChannelHandler
	 * @param buf
	 */
	protected abstract void writeImpl(GameChannelHandler gameChannelHandler, ChannelBuffer buf);
	
}
