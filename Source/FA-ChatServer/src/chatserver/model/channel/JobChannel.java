package chatserver.model.channel;

import java.nio.charset.Charset;

import chatserver.model.ChannelType;
import chatserver.model.PlayerClass;
import chatserver.model.Race;
import chatserver.service.GameServerService;


/**
 * @author ATracer
 */
public class JobChannel extends RaceChannel
{
	private PlayerClass	playerClass;
	private String frenchAlias;

	/**
	 * 
	 * @param playerClass
	 * @param race
	 */
	public JobChannel(PlayerClass playerClass, Race race)
	{
		super(ChannelType.JOB, race);
		this.playerClass = playerClass;
		frenchAlias = "@\u0001job_";
		switch(this.playerClass)
		{
			case ASSASSIN: frenchAlias += "Assassin"; break;
			case CHANTER: frenchAlias += "A\u00e8de"; break;
			case CLERIC: frenchAlias += "Clerc"; break;
			case GLADIATOR: frenchAlias += "Gladiateur[f:\"Gladiatrice\"]"; break;
			case RANGER: frenchAlias += "R\u00f4deur[f:\"R\u00f4deuse\"]"; break;
			case SORCERER: frenchAlias += "Sorcier[f:\"Sorci\u00e8re\"]"; break;
			case SPIRIT_MASTER: frenchAlias += "Spiritualiste"; break;
			case TEMPLAR: frenchAlias += "Templier[f:\"Templi\u00e8re\"]"; break;
		}
		frenchAlias += "\u0001";
		frenchAlias += GameServerService.GAMESERVER_ID;
		frenchAlias += ".";
		if(race == Race.ASMODIANS)
			frenchAlias += "1";
		else
			frenchAlias += "0";
		frenchAlias += ".AION.KOR";
	}
	
	public byte[] getFrenchAlias()
	{
		return frenchAlias.getBytes(Charset.forName("UTF-16le"));
	}

	/**
	 * @return the playerClass
	 */
	public PlayerClass getPlayerClass()
	{
		return playerClass;
	}
}
