package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.FriendList;
import gameserver.model.gameobjects.player.FriendList.Status;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

/**
 * @author ginho1
 *
 */
public class GMOnline extends AdminCommand
{
	public GMOnline()
	{
		super("gmonline");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < AdminConfig.COMMAND_GMONLINE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		int GMCount = 0;
		String sGMNames = "";
		for(Player player : World.getInstance().getPlayers())
		{
			if(player.isGM() && !player.isProtectionActive() && player.getFriendList().getStatus() != FriendList.Status.OFFLINE && player.getVisualState() < 20)
			{
				GMCount++;
				sGMNames += player.getName()+" : "+ returnStringStatus(player.getFriendList().getStatus()) +";\n";
			}
		}
		if(GMCount == 0)
		{
			PacketSendUtility.sendMessage(admin, "There is no GM online !");
		}
		else if(GMCount == 1)
		{
			PacketSendUtility.sendMessage(admin, "There is "+String.valueOf( GMCount )+" GM online !");
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "There are "+String.valueOf( GMCount )+" GMs online !");
		}
		if(GMCount != 0)
			PacketSendUtility.sendMessage(admin, "List : \n"+sGMNames);
	}
	private String returnStringStatus(Status p_status)
	{
		String return_string = "";
		if (p_status == FriendList.Status.ONLINE)
			return_string = "online";
		if (p_status == FriendList.Status.AWAY)
			return_string = "away";
		return return_string;
	}
}
