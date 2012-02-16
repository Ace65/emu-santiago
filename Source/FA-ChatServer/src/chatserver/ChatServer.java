package chatserver;

import chatserver.configs.Config;
import chatserver.utils.guice.ServiceInjectionModule;

import com.google.inject.Guice;
import commons.services.LoggingService;
import commons.utils.AEInfos;

/**
 * @author ATracer
 */
public class ChatServer
{
	/**
	* @param args
	*/
	public static void main(String[] args)
	{
		LoggingService.init();
		Config.load();

		Guice.createInjector(new ServiceInjectionModule());

		AEInfos.printAllInfos();
	}
}
