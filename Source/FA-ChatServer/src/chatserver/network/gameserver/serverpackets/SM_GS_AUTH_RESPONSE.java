package chatserver.network.gameserver.serverpackets;

import org.jboss.netty.buffer.ChannelBuffer;

import chatserver.configs.Config;
import chatserver.network.gameserver.AbstractGameServerPacket;
import chatserver.network.gameserver.GsAuthResponse;
import chatserver.network.netty.handler.GameChannelHandler;


/**
 * @author ATracer
 */
public class SM_GS_AUTH_RESPONSE extends AbstractGameServerPacket
{
	private GsAuthResponse	response;

	public SM_GS_AUTH_RESPONSE(GsAuthResponse resp)
	{
		super(0x00);
		this.response = resp;
	}

	@Override
	protected void writeImpl(GameChannelHandler gameChannelHandler, ChannelBuffer buf)
	{
		writeC(buf, getOpCode());
		writeC(buf, response.getResponseId());
		writeB(buf, Config.CHAT_ADDRESS.getAddress().getAddress());
		writeH(buf, Config.CHAT_ADDRESS.getPort());
	}

}
