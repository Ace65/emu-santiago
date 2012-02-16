package chatserver.network.aion;

import org.jboss.netty.buffer.ChannelBuffer;

import chatserver.common.netty.BaseServerPacket;
import chatserver.network.netty.handler.ClientChannelHandler;


/**
 * @author ATracer
 */
public abstract class AbstractServerPacket extends BaseServerPacket
{
	/**
	 * 
	 * @param opCode
	 */
	public AbstractServerPacket(int opCode)
	{
		super(opCode);
	}
	
	/**
	 * 
	 * @param clientChannelHandler
	 * @param buf
	 */
	public void write(ClientChannelHandler clientChannelHandler, ChannelBuffer buf)
	{
		buf.writeShort((short)0);
		writeImpl(clientChannelHandler, buf);
	}
	
	/**
	 * 
	 * @param cHandler
	 * @param buf
	 */
	protected abstract void writeImpl(ClientChannelHandler cHandler, ChannelBuffer buf);

}
