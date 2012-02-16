package chatserver.configs;

import java.net.InetSocketAddress;
import java.util.Properties;

import org.apache.log4j.Logger;

import commons.configuration.ConfigurableProcessor;
import commons.configuration.Property;
import commons.utils.PropertiesUtils;


/**
 * @author ATracer
 */
public class Config
{
	/**
	 * Logger for this class.
	 */
	protected static final Logger	log	= Logger.getLogger(Config.class);

	/**
	 * Chat Server address
	 */
	@Property(key = "chatserver.network.client.address", defaultValue = "localhost:10241")
	public static InetSocketAddress	CHAT_ADDRESS;

	/**
	 * Game Server address
	 */
	@Property(key = "chatserver.network.gameserver.address", defaultValue = "localhost:9021")
	public static InetSocketAddress			GAME_ADDRESS;
	
	/**
	 * Game Server bind ip
	 */
	@Property(key = "chatserver.network.gameserver.password", defaultValue = "*")
	public static String			GAME_SERVER_PASSWORD;

	/**
	 * Load configs from files.
	 */
	public static void load()
	{
		try
		{
			Properties[] props = PropertiesUtils.loadAllFromDirectory("./config");
			ConfigurableProcessor.process(Config.class, props);
		}
		catch (Exception e)
		{
			log.fatal("Can't load chatserver configuration", e);
			throw new Error("Can't load chatserver configuration", e);
		}
	}
}
