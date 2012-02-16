package chatserver.network.netty.handler;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;

import chatserver.network.gameserver.AbstractGameClientPacket;
import chatserver.network.gameserver.AbstractGameServerPacket;
import chatserver.network.gameserver.GameServerPacketHandler;

import com.google.inject.Inject;

/**
 * @author ATracer
 */
public class GameChannelHandler extends AbstractChannelHandler
{
	/**
	 * Default logger
	 */
	private static final Logger				log	= Logger.getLogger(GameChannelHandler.class);

	/**
	 * Packet handler for game channel
	 */
	private final GameServerPacketHandler	gameServerPacketHandler;

	/**
	 * Current state of channel
	 */
	private State							state;

	/**
	 * 
	 * @param gameServerPacketHandler
	 */
	@Inject
	public GameChannelHandler(GameServerPacketHandler gameServerPacketHandler)
	{
		this.gameServerPacketHandler = gameServerPacketHandler;
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		super.channelConnected(ctx, e);
		state = State.CONNECTED;
		channel = ctx.getChannel();
		inetAddress = ((InetSocketAddress) e.getChannel().getRemoteAddress()).getAddress();
		log.info("Channel connected Ip:" + inetAddress.getHostAddress());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
	{
		super.messageReceived(ctx, e);
		/**
		 * Packet is frame decoded and decrypted at this stage Here packet will be read and submitted to execution
		 */
		AbstractGameClientPacket gameServerPacket = gameServerPacketHandler
			.handle((ChannelBuffer) e.getMessage(), this);
		log.info("Received packet: " + gameServerPacket);
		if (gameServerPacket != null && gameServerPacket.read())
		{
			gameServerPacket.run();
		}
	}

	/**
	 * 
	 * @param packet
	 */
	public void sendPacket(AbstractGameServerPacket packet)
	{
		ChannelBuffer cb = ChannelBuffers.buffer(ByteOrder.LITTLE_ENDIAN, 2 * 8192);
		packet.write(this, cb);
		channel.write(cb);
		log.info("Sent packet: " + packet);
	}

	public static enum State
	{
		/**
		 * Means that GameServer just connected, but is not authenticated yet
		 */
		CONNECTED,
		/**
		 * GameServer is authenticated
		 */
		AUTHED
	}

	/**
	 * @return the state
	 */
	public State getState()
	{
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(State state)
	{
		this.state = state;
	}
}
