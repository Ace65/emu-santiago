package chatserver.network.gameserver;

import org.jboss.netty.buffer.ChannelBuffer;

import chatserver.common.netty.BaseClientPacket;
import chatserver.network.netty.handler.GameChannelHandler;


/**
 * @author ATracer
 */
public abstract class AbstractGameClientPacket extends BaseClientPacket
{
	protected GameChannelHandler gameChannelHandler;

	/**
	 * 
	 * @param channelBuffer
	 * @param gameChannelHandler
	 * @param opCode
	 */
	public AbstractGameClientPacket(ChannelBuffer channelBuffer,
		GameChannelHandler gameChannelHandler, int opCode)
	{
		super(channelBuffer, opCode);
		this.gameChannelHandler = gameChannelHandler;
	}
}
