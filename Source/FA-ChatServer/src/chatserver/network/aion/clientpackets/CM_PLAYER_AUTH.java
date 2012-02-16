package chatserver.network.aion.clientpackets;

import org.jboss.netty.buffer.ChannelBuffer;

import chatserver.network.aion.AbstractClientPacket;
import chatserver.network.netty.handler.ClientChannelHandler;
import chatserver.service.ChatService;


/**
 * 
 * @author ATracer
 */
public class CM_PLAYER_AUTH extends AbstractClientPacket
{
	private ChatService	chatService;

	private int			playerId;
	private byte[]		token;
	private byte[]		identifier;
	@SuppressWarnings("unused")
	private byte[]		accountName;

	/**
	 * 
	 * @param channelBuffer
	 * @param gameChannelHandler
	 * @param opCode
	 */
	public CM_PLAYER_AUTH(ChannelBuffer channelBuffer, ClientChannelHandler clientChannelHandler,
		ChatService chatService)
	{
		super(channelBuffer, clientChannelHandler, 0x05);
		this.chatService = chatService;
	}

	@Override
	protected void readImpl()
	{
		readC(); // 0x40
		readH(); // 0x00
		readH(); // 0x01
		readH(); // 0x04
		readS(); // AION
		this.playerId = readD();
		readD(); // 0x00
		readD(); // 0x00
		int length = readH() * 2;
		identifier = readB(length);
		int accountLenght = readH() * 2;
		accountName = readB(accountLenght);
		int tokenLength = readH();
		token = readB(tokenLength);
	}

	@Override
	protected void runImpl()
	{
		chatService.registerPlayerConnection(playerId, token, identifier, clientChannelHandler);
	}
}
