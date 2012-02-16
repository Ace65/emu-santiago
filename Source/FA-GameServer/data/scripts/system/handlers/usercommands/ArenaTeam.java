package usercommands;

import gameserver.model.gameobjects.player.Player;
import gameserver.services.HTMLService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.UserCommand;

import br.focus.factories.SurveyFactory;

/**
 * @author Holgrabus
 *
 */
public class ArenaTeam extends UserCommand
{
	public ArenaTeam ()
	{
		super("arena");
	}
	
	@Override
	public void executeCommand(Player player, String params)
	{
		if(player == null)
			return;
		
		if(player.getBattleGround() != null)
		{
			PacketSendUtility.sendMessage(player, "You are already in a battleground.");
			PacketSendUtility.sendMessage(player, "Use your spell Return to leave the battleground.");
			return;
		}
		else if(player.battlegroundWaiting)
		{
			PacketSendUtility.sendMessage(player, "You are already registered in a battleground.");
			PacketSendUtility.sendMessage(player, "Use the command /bg unregister to cancel your registration.");
			return;
		}
		else
			HTMLService.showHTML(player, SurveyFactory.getArenaManagementMenu(player), 142000001);
	}

}
