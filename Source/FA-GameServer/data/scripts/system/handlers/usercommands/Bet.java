package usercommands;

import org.apache.log4j.Logger;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.gameobjects.player.StorageType;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.UserCommand;

/**
 * @author Holgrabus
 *
 */
public class Bet extends UserCommand
{
	public Bet ()
	{
		super("bet");
	}
	
	@Override
	public void executeCommand(Player player, String params)
	{
	    String[] data = params.split(" ");
	    Storage inventory =  player.getStorage(StorageType.CUBE.getId());
        long PlayerKinah = inventory.getKinahCount();
        
        int amount;
		
        if(data.equals("") || data.equals(" ") || data.length != 2)
        {
            PacketSendUtility.sendMessage(player, "Use : .bet <e|a> <amount>");
            return;
        }
        
        try
        {
            amount = Integer.parseInt(data[1]);
        }
        catch(NumberFormatException e)
        {
            PacketSendUtility.sendMessage(player, "Use : .bet <e | a> <amount>");
            return;
        }
        			
        if (player.battlegroundObserve == 1 || player.battlegroundObserve == 2)
        {
	        if(PlayerKinah < amount)
	            PacketSendUtility.sendMessage(player, "You don't have enough kinah !");
	        
	        int maxBetAmount = 5000000;
	        if(player.battlegroundBetE + player.battlegroundBetA + amount > maxBetAmount)
	        	PacketSendUtility.sendMessage(player, "You can't bet more than " + maxBetAmount + " !");
	        
	        else if(player.battlegroundBetE == 0 && player.battlegroundBetA == 0)
            {
                if(data[0].equals("e"))
                {
                    player.battlegroundBetE = amount;
                    inventory.decreaseKinah(amount);
                    Logger.getLogger(UserCommand.class).info(String.format("[BET] - Player : " + player.getName() + " | Bet : " + data[1] + " | Faction : e"));
                    PacketSendUtility.sendMessage(player, "You have bet : " + player.battlegroundBetE + " for the elyos");
                }
                else if(data[0].equals("a"))
                {
                    player.battlegroundBetA = amount;
                    inventory.decreaseKinah(amount);
                    Logger.getLogger(UserCommand.class).info(String.format("[BET] - Player : " + player.getName() + " | Bet : " + data[1] + " | Faction : a"));
                    PacketSendUtility.sendMessage(player, "You have bet : " + player.battlegroundBetA + " for the asmodians");
                }
                else
                    PacketSendUtility.sendMessage(player, "Use : .bet <e | a> <amount>");        
            }
            else if(player.battlegroundBetE > 0)
            {
                if(data[0].equals("e"))
                {
                    player.battlegroundBetE += amount;
                    inventory.decreaseKinah(amount);
                    Logger.getLogger(UserCommand.class).info(String.format("[BET] - Player : " + player.getName() + " | Bet : " + data[1] + " | Faction : e"));
                    PacketSendUtility.sendMessage(player, "You have bet : " + player.battlegroundBetE + " for the elyos");
                }
                else if(data[0].equals("a"))
                    PacketSendUtility.sendMessage(player, "You have already bet for the elyos");
                else
                    PacketSendUtility.sendMessage(player, "Use : .bet <e | a> <amount>");        
            }
            else if(player.battlegroundBetA > 0)
            {
                if(data[0].equals("a"))
                {
                    player.battlegroundBetA += amount;
                    inventory.decreaseKinah(amount);
                    Logger.getLogger(UserCommand.class).info(String.format("[BET] - Player : " + player.getName() + " | Bet : " + data[1] + " | Faction : a"));
                    PacketSendUtility.sendMessage(player, "You have bet : " + player.battlegroundBetA + " for the asmodians");
                }
                else if(data[0].equals("e"))
                    PacketSendUtility.sendMessage(player, "You have already bet for the asmodians");
                else
                    PacketSendUtility.sendMessage(player, "Use : .bet <e | a> <amount>");        
            }
			else
                    PacketSendUtility.sendMessage(player, "Use : .bet <e | a> <amount>");
        }
	    
        else
            PacketSendUtility.sendMessage(player, "You can't bet for the moment");
	}
}
