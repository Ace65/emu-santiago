package chatserver.model.channel;

import chatserver.model.ChannelType;
import chatserver.model.Race;

/**
 * @author ATracer
 */
public class LfgChannel extends RaceChannel
{
	public LfgChannel(Race race)
	{
		super(ChannelType.GROUP, race);
	}
}
