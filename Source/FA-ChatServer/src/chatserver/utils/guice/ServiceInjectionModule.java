package chatserver.utils.guice;


import chatserver.network.aion.ClientPacketHandler;
import chatserver.network.gameserver.GameServerPacketHandler;
import chatserver.network.netty.NettyServer;
import chatserver.network.netty.pipeline.LoginToClientPipeLineFactory;
import chatserver.network.netty.pipeline.LoginToGamePipelineFactory;
import chatserver.service.BroadcastService;
import chatserver.service.ChatService;
import chatserver.service.GameServerService;
import chatserver.utils.IdFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * 
 * @author ATracer
 *
 */
public class ServiceInjectionModule extends AbstractModule
{

	@Override
	protected void configure()
	{		
		bind(IdFactory.class).asEagerSingleton();
		
		bind(GameServerPacketHandler.class).in(Scopes.SINGLETON);
		bind(ClientPacketHandler.class).in(Scopes.SINGLETON);
		
		bind(LoginToClientPipeLineFactory.class).in(Scopes.SINGLETON);
		bind(LoginToGamePipelineFactory.class).in(Scopes.SINGLETON);
		
		bind(NettyServer.class).asEagerSingleton();
		
		bind(GameServerService.class).in(Scopes.SINGLETON);
		bind(BroadcastService.class).in(Scopes.SINGLETON);
		bind(ChatService.class).in(Scopes.SINGLETON);
	}
}
