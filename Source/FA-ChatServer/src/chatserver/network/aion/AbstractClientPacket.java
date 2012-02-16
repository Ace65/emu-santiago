package chatserver.network.aion;

import org.jboss.netty.buffer.ChannelBuffer;

import chatserver.common.netty.BaseClientPacket;
import chatserver.network.netty.handler.ClientChannelHandler;


/**
 * @author ATracer
 */
public abstract class AbstractClientPacket extends BaseClientPacket
{
	protected ClientChannelHandler	clientChannelHandler;

	/**
	 * 
	 * @param channelBuffer
	 * @param clientChannelHandler
	 * @param opCode
	 */
	public AbstractClientPacket(ChannelBuffer channelBuffer, ClientChannelHandler clientChannelHandler, int opCode)
	{
		super(channelBuffer, opCode);
		this.clientChannelHandler = clientChannelHandler;
	}
}
