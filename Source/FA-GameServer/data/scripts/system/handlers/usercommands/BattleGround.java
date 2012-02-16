package usercommands;

import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureVisualState;
import gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import gameserver.services.HTMLService;
import gameserver.services.TeleportService;
import gameserver.skill.effect.EffectId;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.UserCommand;

import br.focus.battleground.BattleGroundManager;
import br.focus.factories.SurveyFactory;

/**
 * @author xitanium / Holgrabus
 *
 */
public class BattleGround extends UserCommand
{
	public BattleGround ()
	{
		super("bg");
	}
	
	@Override
	public void executeCommand(Player player, String params)
	{
		
		/*if(player.getAccessLevel() < 1)
		{
			PacketSendUtility.sendMessage(player, "Battlegrounds will be available soon");
			return;
		}*/
		
		if(!BattleGroundManager.INITIALIZED)
		{
			PacketSendUtility.sendMessage(player, "Battlegrounds are disabled.");
			return;
		}
		
		if(player.isInPrison())
		{
			PacketSendUtility.sendMessage(player, "You cannot register for battlegrounds while you are in prison.");
			return;
		}
				
		if(params.equals("register"))
		{
			if(player.getBattleGround() != null)
			{
				PacketSendUtility.sendMessage(player, "You are already in a battleground.");
				PacketSendUtility.sendMessage(player, "Use your spell Return to leave the battleground.");
				return;
			}
			else if(player.battlegroundWaiting)
			{
				PacketSendUtility.sendMessage(player, "You are already registered in a battleground.");
				PacketSendUtility.sendMessage(player, "Use the command .bg unregister to cancel your registration.");
				return;
			}
			else if(player.ArenaStatus == 1)
			{
				PacketSendUtility.sendMessage(player, "You are already registered in the Arena Team waiting list.");
				PacketSendUtility.sendMessage(player, "Use the command .arena to cancel your registration.");
				return;
			}
			else if(player.ArenaStatus > 1)
			{
				PacketSendUtility.sendMessage(player, "You are already registered in the Arena Team waiting list.");
				PacketSendUtility.sendMessage(player, "Use your spell Return to leave the Arena Team Battle.");
				return;
			}
			else
			{
				BattleGroundManager.sendRegistrationForm(player);
			}
		}
		else if(params.equals("observe"))
        {
            if(player.getBattleGround() != null)
            {
                PacketSendUtility.sendMessage(player, "You are already in a battleground.");
                PacketSendUtility.sendMessage(player, "Use your spell Return to leave the battleground.");
                return;
            }
            else if(player.battlegroundWaiting)
            {
                PacketSendUtility.sendMessage(player, "You are already registered in a battleground.");
                PacketSendUtility.sendMessage(player, "Use the command .bg unregister to cancel your registration.");
                return;
            }
            else
            {
                BattleGroundManager.sendRegistrationFormObs(player);
            }
        }
		else if(params.equals("stop"))
        {
            if(player.getBattleGround() != null && player.battlegroundObserve == 0)
            {
                PacketSendUtility.sendMessage(player, "You are playing in a battleground, not an observer.");
                return;
            }
            else if(player.getBattleGround() != null && player.battlegroundObserve > 0)
            {
                player.getEffectController().unsetAbnormal(EffectId.INVISIBLE_RELATED.getEffectId());
                player.unsetVisualState(CreatureVisualState.HIDE20);
                PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
                PacketSendUtility.sendMessage(player, "You are now visible.");
                player.setInvul(false);
                PacketSendUtility.sendMessage(player, "You are now mortal.");
                
                if(player.battlegroundBetE > 0 || player.battlegroundBetA > 0)
                {
                    PacketSendUtility.sendMessage(player, "You have lost your bet of " + (player.battlegroundBetE + player.battlegroundBetA) + "kinah.");
                    player.battlegroundBetE = 0;
                    player.battlegroundBetA = 0;
                }
                
                if(player.getCommonData().getRace() == Race.ELYOS)
                    TeleportService.teleportTo(player, 110010000, 1374, 1399, 573, 0);
                else
                    TeleportService.teleportTo(player, 120010000, 1324, 1550, 210, 0);
                
                return;
            }
            else
            {
                PacketSendUtility.sendMessage(player, "You are not observing any battleground.");
            }
        }
		else if(params.equals("rank"))
		{
			if(player.getBattleGround() == null || (!player.getBattleGround().running && !player.battlegroundWaiting))
			{
				PacketSendUtility.sendMessage(player, "You are not registered in any battleground or the battleground is over.");
				return;
			}
			else
			{
				player.battlegroundRequestedRank = true;
				HTMLService.showHTML(player, SurveyFactory.getBattleGroundRanking(player.getBattleGround()), 151000001);
			}
		}
		else if(params.equals("stat") && player.getAccessLevel() > 0)
		{
			PacketSendUtility.sendMessage(player, BattleGroundManager.getElyosWaitList().get(1).size() + " Elyos " + BattleGroundManager.getAsmodiansWaitList().get(1).size() + " Asmodians for Triniel");
			PacketSendUtility.sendMessage(player, BattleGroundManager.getElyosWaitList().get(2).size() + " Elyos " + BattleGroundManager.getAsmodiansWaitList().get(2).size() + " Asmodians for Sanctum");
			PacketSendUtility.sendMessage(player, BattleGroundManager.getElyosWaitList().get(3).size() + " Elyos " + BattleGroundManager.getAsmodiansWaitList().get(3).size() + " Asmodians for Haramel");
		}
		else if(params.startsWith("exchange"))
		{
		    if(player.getAccessLevel() < 1)
	        {
	            PacketSendUtility.sendMessage(player, "The exchange tool is not available.");
	            return;
	        }
		    
			if(params.equals("exchange"))
			{
				PacketSendUtility.sendMessage(player, "The exchange rate is 1 BG point for 3 Abyss points.");
				PacketSendUtility.sendMessage(player, "To exchange some points, write .bg exchange <bg_points_number>");
			}
			else
			{
				String[] data = params.split(" ");
				try
				{
					int bgPts = Integer.parseInt(data[1]);
					if(player.getCommonData().getBattleGroundPoints() < bgPts)
					{
						PacketSendUtility.sendMessage(player, "You don't have enough BG points.");
						return;
					}
					player.getCommonData().setBattleGroundPoints(player.getCommonData().getBattleGroundPoints() - bgPts);
					PacketSendUtility.sendMessage(player, "You have lost " + bgPts + " BG points.");
					player.getCommonData().addAp(bgPts * 3);
				}
				catch(Exception e)
				{
					PacketSendUtility.sendMessage(player, "Syntax error. Use .bg exchange <bg_points_number>.");
				}
			}
		}
		else if(params.equals("end") && player.getAccessLevel() > 0)
		{
			player.getBattleGround().end();
		}
		else if(params.equals("unregister"))
		{
			if(!player.battlegroundWaiting)
			{
				PacketSendUtility.sendMessage(player, "You are not registered in any battleground.");
				return;
			}
			if(player.battlegroundObserve == 0)
			    BattleGroundManager.unregisterPlayer(player);
			else if(player.battlegroundObserve > 0)
			    BattleGroundManager.unregisterPlayerObs(player);
			
			PacketSendUtility.sendMessage(player, "Registration canceled.");
		}
		else if(params.equals("help"))
		{
			PacketSendUtility.sendMessage(player, ".bg register : register in a BG");
			PacketSendUtility.sendMessage(player, ".bg observe : observe a battleground");
			PacketSendUtility.sendMessage(player, ".bg unregister : unregister from the BG (before starting)");
			PacketSendUtility.sendMessage(player, ".bg stop : stop observe and back to home");
			PacketSendUtility.sendMessage(player, ".bg rank : see your rank during a BG");
			PacketSendUtility.sendMessage(player, ".bg points : : to see your points");
			PacketSendUtility.sendMessage(player, ".bet : bet on a faction during observe mode");
		}
		else if(params.equals("points"))
        {
            PacketSendUtility.sendMessage(player, "You have actually " + (player.getCommonData().getBattleGroundPoints() + player.battlegroundSessionPoints) + " BG points" + (player.battlegroundSessionPoints > 0 ? ", including " + player.battlegroundSessionPoints + " in the current BG " : "") + ".");
        }
        else
        {
            PacketSendUtility.sendMessage(player, "This command doesn't exist, use .bg help");
        }
	}

}
