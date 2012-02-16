package chatserver.model.channel;

import chatserver.model.ChannelType;
import chatserver.model.Race;

/**
 * @author ATracer
 */
public class TradeChannel extends RaceChannel
{
	public TradeChannel(Race race)
	{
		super(ChannelType.TRADE, race);
	}
}
