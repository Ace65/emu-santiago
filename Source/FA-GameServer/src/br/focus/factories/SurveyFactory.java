package br.focus.factories;

import java.util.List;

import org.apache.log4j.Logger;

import commons.database.dao.DAOManager;

import gameserver.dao.PlayerDAO;
import gameserver.model.AionArenaTeam;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.gameobjects.player.StorageType;
import gameserver.model.templates.BattleGroundTemplate;
import gameserver.model.templates.BattleGroundType;
import gameserver.utils.chathandlers.UserCommand;
import br.focus.arena.ArenaManager;
import br.focus.battleground.BattleGround;
import br.focus.battleground.BattleGroundManager;

/**
 * @author ambrosius / Holgrabus
 *
 */
public class SurveyFactory
{
	public static String buildAssaultBattleGroundReport(Player player)
	{
		if(player == null || player.getBattleGround() == null)
			return "";
		BattleGround bg = player.getBattleGround();
		String html = "<poll>\n";
		html += "<poll_introduction>\n";
		html += "	<![CDATA[<font color='4CB1E5' size='+1'><center>Results of the Assault Battleground</center></font>]]>\n";
		html += "</poll_introduction>\n";
		html += "<poll_title>\n";
		html += "	<font color='ffc519'></font>\n";
		html += "</poll_title>\n";
		html += "<start_date>2010-08-08 00:00</start_date>\n";
		html += "<end_date>2010-09-14 01:00</end_date>\n";
		html += "<servers></servers>\n";
		html += "<order_num></order_num>\n";
		html += "<race></race>\n";
		html += "<main_class></main_class>\n";
		html += "<world_id></world_id>\n";
		html += "<item_id>";
		html += 0;
		html += "</item_id>\n";
		html += "<item_cnt>";
		html += 0;
		html += "</item_cnt>\n";
		html += "<level>1~55</level>\n";
		html += "<questions>\n";
		
		List<Player> elyos = bg.getRanking(Race.ELYOS, true);
		List<Player> asmos = bg.getRanking(Race.ASMODIANS, true);
		
		String winner = "";
		String winnerColor = "";
		
		int EPts = 0;
        int EKills = 0;
        int APts = 0;
        int AKills = 0;
        for(Player e : elyos)
        {
        	EPts += e.battlegroundSessionPoints;
        	EKills += e.battlegroundSessionKills;
        }
        for(Player a : asmos)
        {
        	APts += a.battlegroundSessionPoints;
        	AKills += a.battlegroundSessionKills;
        }
        
		if(EPts > APts){
			winner = "Elyos";
			winnerColor = "4CB1E5";}
		else if(EPts == APts){
            winner = "The two factions";
            winnerColor = "00C000";}
		else if(EPts < APts){
            winner = "Asmodians";
            winnerColor = "ffba75";}
				
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "<br><br>";
		html += "<font color='" + winnerColor + "' size='+2' style='font-weight: bold;'>" + winner + " win !</font>";
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>\n";
		html += "		</select>\n";
		html += "	</question>\n";
		
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "<br><br>";
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>\n";
		html += "		</select>\n";
		html += "	</question>\n";
		
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "<br><br>";
		html += "<font color='4CB1E5'><center>Elyos - Points: " + EPts + " - Kills:" + EKills + "</center></font>";
		html += "<br><br>";
		
        html += "<table border='0'>";
        html += "<tr><td><center>Pos.</center></td><td><center>Name</center></td><td><center>&nbsp;Points&nbsp;</center></td><td><center>&nbsp;&nbsp;Kills&nbsp;&nbsp;</center></td><td><center>&nbsp;&nbsp;Deaths&nbsp;&nbsp;</center></td>";
		int counter = 1;
		for(Player e : elyos)
		{
			html += "<tr><td>" + counter + ". </td><td>" + e.getName() + "&nbsp;&nbsp;</td><td>&nbsp; " + e.battlegroundSessionPoints + " &nbsp;</td><td>&nbsp; " + e.battlegroundSessionKills + " &nbsp;</td><td>&nbsp; " + e.battlegroundSessionDeaths + "</td></tr>";
			counter++;
		}
		html += "</table>";
		
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>\n";
		html += "		</select>\n";
		html += "	</question>\n";
		
		
		
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "<br><br>";
		html += "<font color='ffba75'><center>Asmodians - Points: " + APts + " - Kills:" + AKills + "</center></font>";
		html += "<br><br>";
		
		html += "<table border='0'>";
		html += "<tr><td><center>Pos.</center></td><td><center>Name</center></td><td><center>&nbsp;Points&nbsp;</center></td><td><center>&nbsp;&nbsp;Kills&nbsp;&nbsp;</center></td><td><center>&nbsp;Deaths&nbsp;</center></td>";
		counter = 1;
		for(Player a : asmos)
		{
			html += "<tr><td>" + counter + ". </td><td>" + a.getName() + "&nbsp;&nbsp;</td><td>&nbsp; " + a.battlegroundSessionPoints + " &nbsp;</td><td>&nbsp; " + a.battlegroundSessionKills + " &nbsp;</td><td>&nbsp; " + a.battlegroundSessionDeaths + "</td></tr>";
			counter++;
		}
		html += "</table>";
		
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>\n";
		html += "		</select>\n";
		html += "	</question>\n";
		
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "<br><br>";
		
		Storage inventory =  player.getStorage(StorageType.CUBE.getId());
		long Kinah0E = (long) (player.battlegroundBetE);
		long Kinah1E = (long) (player.battlegroundBetE * 0.8);
		long Kinah2E = (long) (player.battlegroundBetE * 1.8);
		long Kinah0A = (long) (player.battlegroundBetA);
        long Kinah1A = (long) (player.battlegroundBetA * 0.8);
        long Kinah2A = (long) (player.battlegroundBetA * 1.8);
		if(player.battlegroundObserve == 0)
		{
		    int oldPoints = player.getCommonData().getBattleGroundPoints();
		    int earnedPoints = player.battlegroundSessionPoints;
		
		    html += "You have earned " + earnedPoints + " BG points, ";
		
		    html += "your total BG points is now : " + (player.getCommonData().getBattleGroundPoints() + earnedPoints) + ".<br><br>";
		
		    for(BattleGroundTemplate template : BattleGroundManager.getUnlockedBattleGrounds(oldPoints, player.getCommonData().getBattleGroundPoints()))
		    {
			html += "You have unlocked the following battleground : " + template.getName() + " (" + template.getJoinConditions().getRequiredBgPoints() + " pts.)<br><br>";
		    }
		}
		
		else if(player.battlegroundObserve > 0)
        {
		    if(player.battlegroundBetE == 0 && player.battlegroundBetA == 0)
		    {
		        html += "You don't have bet kinah during this battleground.";
		    }
		    else if(player.battlegroundBetE > 0)
            {
                html += "You have bet : " + Kinah0E + " kinah for the elyos.<br><br>";
                if(winner.equals("Elyos")){
                    html += "" + winner + " win, so you have earned : " + Kinah1E + " kinah !";
                    inventory.increaseKinah(Kinah2E);
                    Logger.getLogger(UserCommand.class).info(String.format("[BET] - Player : " + player.getName() + " | Get : " + Kinah2E + " | Faction : e"));}
                else if(winner.equals("Asmodians"))
                    html += "" + winner + " win, so you have lost : " + Kinah0E + " kinah !";
                else if(winner.equals("The two factions")){
                    html += "" + winner + " win, so you have recovered your bet : " + Kinah0E + " kinah !";
                    inventory.increaseKinah(Kinah0E);
                    Logger.getLogger(UserCommand.class).info(String.format("[BET] - Player : " + player.getName() + " | Get : " + Kinah0E + " | Faction : 0"));}
            }
		    else if(player.battlegroundBetA > 0)
            {
                html += "You have bet : " + Kinah0A + " kinah for the asmodians.<br><br>";
                if(winner.equals("Asmodians")){
                    html += "" + winner + " win, so you have earned : " + Kinah1A + " kinah !";
                    inventory.increaseKinah(Kinah2A);
                    Logger.getLogger(UserCommand.class).info(String.format("[BET] - Player : " + player.getName() + " | Get : " + Kinah2A + " | Faction : a"));}
                else if(winner.equals("Elyos"))
                    html += "" + winner + " win, so you have lost : " + Kinah0A + " kinah !";
                else if(winner.equals("The two factions")){
                    html += "" + winner + " win, so you have recovered your bet : " + Kinah0A + " kinah !";
                    inventory.increaseKinah(Kinah0A);
                    Logger.getLogger(UserCommand.class).info(String.format("[BET] - Player : " + player.getName() + " | Get : " + Kinah0A + " | Faction : 0"));}
            }
        }
		
		bg.commitPoints(player);
		
		html += "\n";
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>\n";
		html += "			<input type='radio'>Close</input>";
		html += "		</select>\n";
		html += "	</question>\n";
		
		html += "</questions>\n";
		html += "</poll>\n";
		
		return html;
	}
	
	public static String buildCTFBattleGroundReport(Player player)
	{
		if(player == null || player.getBattleGround() == null)
			return "";
		BattleGround bg = player.getBattleGround();
		String html = "<poll>\n";
		html += "<poll_introduction>\n";
		html += "	<![CDATA[<font color='4CB1E5' size='+1'><center>Results of the Capture the flag Battleground</center></font>]]>\n";
		html += "</poll_introduction>\n";
		html += "<poll_title>\n";
		html += "	<font color='ffc519'></font>\n";
		html += "</poll_title>\n";
		html += "<start_date>2010-08-08 00:00</start_date>\n";
		html += "<end_date>2010-09-14 01:00</end_date>\n";
		html += "<servers></servers>\n";
		html += "<order_num></order_num>\n";
		html += "<race></race>\n";
		html += "<main_class></main_class>\n";
		html += "<world_id></world_id>\n";
		html += "<item_id>";
		html += 0;
		html += "</item_id>\n";
		html += "<item_cnt>";
		html += 0;
		html += "</item_cnt>\n";
		html += "<level>1~55</level>\n";
		html += "<questions>\n";
		
		List<Player> elyos = bg.getRanking(Race.ELYOS, true);
		List<Player> asmos = bg.getRanking(Race.ASMODIANS, true);
		
		String winner = "";
        String winnerColor = "";
        
        int EPts = 0;
        int EFlags = 0;
        int APts = 0;
        int AFlags = 0;
        for(Player e : elyos)
        {
        	EPts += e.battlegroundSessionPoints;
        	EFlags += e.battlegroundSessionFlags;
        }
        for(Player a : asmos)
        {
        	APts += a.battlegroundSessionPoints;
        	AFlags += a.battlegroundSessionFlags;
        }
        
        if(EFlags > AFlags){
            winner = "Elyos";
            winnerColor = "4CB1E5";}
        else if(EFlags == AFlags){   
            if(EPts > APts){
                winner = "Elyos";
                winnerColor = "4CB1E5";}
            else if(EPts == APts){
                winner = "The two factions";
                winnerColor = "00C000";}
            else if(EPts < APts){
                winner = "Asmodians";
                winnerColor = "ffba75";}
        }
        else if(EFlags < AFlags){
            winner = "Asmodians";
            winnerColor = "ffba75";}
				
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "<br><br>";
		html += "<font color='" + winnerColor + "' size='+2' style='font-weight: bold;'>" + winner + " win !</font>";
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>\n";
		html += "		</select>\n";
		html += "	</question>\n";
		
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "<br><br>";
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>\n";
		html += "		</select>\n";
		html += "	</question>\n";
		
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "<br><br>";
		html += "<font color='4CB1E5'><center>Elyos - Flags: " + EFlags + " - Points: " + EPts + "</center></font>";
		html += "<br><br>";
		
		html += "<table border='0'>";
		html += "<tr><td><center>Pos.</center></td><td><center>Name</center></td><td><center>&nbsp;Points&nbsp;</center></td><td><center>&nbsp;&nbsp;Kills&nbsp;&nbsp;</center></td><td><center>&nbsp;&nbsp;Flags&nbsp;&nbsp;</center></td>";
		int counter = 1;
		for(Player e : elyos)
		{
			html += "<tr><td>" + counter + ". </td><td>" + e.getName() + "&nbsp;&nbsp;</td><td>&nbsp; " + e.battlegroundSessionPoints + " &nbsp;</td><td>&nbsp; " + e.battlegroundSessionKills + " &nbsp;</td><td>&nbsp; " + e.battlegroundSessionFlags + "</td></tr>";
			counter++;
		}
		html += "</table>";
		
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>\n";
		html += "		</select>\n";
		html += "	</question>\n";
		
		
		
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "<br><br>";
		html += "<font color='ffba75'><center>Asmodians - Flags: " + AFlags + " - Points: " + APts + "</center></font>";
		html += "<br><br>";
		
		html += "<table border='0'>";
        html += "<tr><td><center>Pos.</center></td><td><center>Name</center></td><td><center>&nbsp;Points&nbsp;</center></td><td><center>&nbsp;&nbsp;Kills&nbsp;&nbsp;</center></td><td><center>&nbsp;&nbsp;Flags&nbsp;&nbsp;</center></td>";
		counter = 1;
		for(Player a : asmos)
		{
			html += "<tr><td>" + counter + ". </td><td>" + a.getName() + "&nbsp;&nbsp;</td><td>&nbsp; " + a.battlegroundSessionPoints + " &nbsp;</td><td>&nbsp; " + a.battlegroundSessionKills + " &nbsp;</td><td>&nbsp; " + a.battlegroundSessionFlags + "</td></tr>";
			counter++;
		}
		html += "</table>";
		
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>\n";
		html += "		</select>\n";
		html += "	</question>\n";
		
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "<br><br>";
		
		Storage inventory =  player.getStorage(StorageType.CUBE.getId());
        long Kinah0E = (long) (player.battlegroundBetE);
        long Kinah1E = (long) (player.battlegroundBetE * 0.8);
        long Kinah2E = (long) (player.battlegroundBetE * 1.8);
        long Kinah0A = (long) (player.battlegroundBetA);
        long Kinah1A = (long) (player.battlegroundBetA * 0.8);
        long Kinah2A = (long) (player.battlegroundBetA * 1.8);
        if(player.battlegroundObserve == 0)
        {
            int oldPoints = player.getCommonData().getBattleGroundPoints();
            int earnedPoints = player.battlegroundSessionPoints;
        
            html += "You have earned " + earnedPoints + " BG points, ";
        
            html += "your total BG points is now : " + (player.getCommonData().getBattleGroundPoints() + earnedPoints) + ".<br><br>";
        
            for(BattleGroundTemplate template : BattleGroundManager.getUnlockedBattleGrounds(oldPoints, player.getCommonData().getBattleGroundPoints()))
            {
            html += "You have unlocked the following battleground : " + template.getName() + " (" + template.getJoinConditions().getRequiredBgPoints() + " pts.)<br><br>";
            }
        }
        
        else if(player.battlegroundObserve > 0)
        {
            if(player.battlegroundBetE == 0 && player.battlegroundBetA == 0)
            {
                html += "You don't have bet kinah during this battleground.";
            }
            else if(player.battlegroundBetE > 0)
            {
                html += "You have bet : " + Kinah0E + " kinah for the elyos.<br><br>";
                if(winner.equals("Elyos")){
                    html += "" + winner + " win, so you have earned : " + Kinah1E + " kinah !";
                    inventory.increaseKinah(Kinah2E);
                    Logger.getLogger(UserCommand.class).info(String.format("[BET] - Player : " + player.getName() + " | Get : " + Kinah2E + " | Faction : e"));}
                else if(winner.equals("Asmodians"))
                    html += "" + winner + " win, so you have lost : " + Kinah0E + " kinah !";
                else if(winner.equals("The two factions")){
                    html += "" + winner + " win, so you have recovered your bet : " + Kinah0E + " kinah !";
                    inventory.increaseKinah(Kinah0E);
                    Logger.getLogger(UserCommand.class).info(String.format("[BET] - Player : " + player.getName() + " | Get : " + Kinah0E + " | Faction : 0"));}
            }
            else if(player.battlegroundBetA > 0)
            {
                html += "You have bet : " + Kinah0A + " kinah for the asmodians.<br><br>";
                if(winner.equals("Asmodians")){
                    html += "" + winner + " win, so you have earned : " + Kinah1A + " kinah !";
                    inventory.increaseKinah(Kinah2A);
                    Logger.getLogger(UserCommand.class).info(String.format("[BET] - Player : " + player.getName() + " | Get : " + Kinah2A + " | Faction : a"));}
                else if(winner.equals("Elyos"))
                    html += "" + winner + " win, so you have lost : " + Kinah0A + " kinah !";
                else if(winner.equals("The two factions")){
                    html += "" + winner + " win, so you have recovered your bet : " + Kinah0A + " kinah !";
                    inventory.increaseKinah(Kinah0A);
                    Logger.getLogger(UserCommand.class).info(String.format("[BET] - Player : " + player.getName() + " | Get : " + Kinah0A + " | Faction : 0"));}
            }
        }
        
        bg.commitPoints(player);
        
		html += "\n";
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>\n";
		html += "			<input type='radio'>Close</input>";
		html += "		</select>\n";
		html += "	</question>\n";
		
		html += "</questions>\n";
		html += "</poll>\n";
		
		return html;
	}
	
	public static String getBattleGroundRanking(BattleGround bg)
	{
		String html = "<poll>\n";
		html += "<poll_introduction>\n";
		html += "	<![CDATA[<font color='4CB1E5' size='+1'><center>Current rank</center></font>]]>\n";
		html += "</poll_introduction>\n";
		html += "<poll_title>\n";
		html += "	<font color='ffc519'></font>\n";
		html += "</poll_title>\n";
		html += "<start_date>2010-08-08 00:00</start_date>\n";
		html += "<end_date>2010-09-14 01:00</end_date>\n";
		html += "<servers></servers>\n";
		html += "<order_num></order_num>\n";
		html += "<race></race>\n";
		html += "<main_class></main_class>\n";
		html += "<world_id></world_id>\n";
		html += "<item_id>";
		html += 0;
		html += "</item_id>\n";
		html += "<item_cnt>";
		html += 0;
		html += "</item_cnt>\n";
		html += "<level>1~55</level>\n";
		html += "<questions>\n";
        
        List<Player> elyos = bg.getRanking(Race.ELYOS, false);
        List<Player> asmos = bg.getRanking(Race.ASMODIANS, false);
        
        String winner = "";
        String winnerColor = "";
        
        int EPts = 0;
        int EKills = 0;
        int EFlags = 0;
        int APts = 0;
        int AKills = 0;
        int AFlags = 0;
        for(Player e : elyos)
        {
        	EPts += e.battlegroundSessionPoints;
        	EKills += e.battlegroundSessionKills;
        	EFlags += e.battlegroundSessionFlags;
        }
        for(Player a : asmos)
        {
        	APts += a.battlegroundSessionPoints;
        	AKills += a.battlegroundSessionKills;
        	AFlags += a.battlegroundSessionFlags;
        }
        
        if(bg.getTemplate().getType() == BattleGroundType.CTF){
            if(EFlags > AFlags){
                winner = "Elyos";
                winnerColor = "4CB1E5";}
            else if(EFlags == AFlags){   
                if(EPts > APts){
                    winner = "Elyos";
                    winnerColor = "4CB1E5";}
                else if(EPts == APts){
                    winner = "The two factions";
                    winnerColor = "00C000";}
                else if(EPts < APts){
                    winner = "Asmodians";
                    winnerColor = "ffba75";}
            }
            else if(EFlags < AFlags){
                winner = "Asmodians";
                winnerColor = "ffba75";}
        }
        else if(bg.getTemplate().getType() == BattleGroundType.ASSAULT){
            if(EPts > APts){
                winner = "Elyos";
                winnerColor = "4CB1E5";}
            else if(EPts == APts){
                winner = "The two factions";
                winnerColor = "00C000";}
            else if(EPts < APts){
                winner = "Asmodians";
                winnerColor = "ffba75";}
        }
        
        html += "   <question>\n";
        html += "       <title>\n";
        html += "           <![CDATA[\n";
        html += "<br><br>";
        html += "<font size='+2' style='font-weight: bold;'>Current winner : </font><font color='" + winnerColor + "' size='+2' style='font-weight: bold;'>" + winner + "</font>";
        html += "           ]]>\n";
        html += "       </title>\n";
        html += "       <select>\n";
        html += "       </select>\n";
        html += "   </question>\n";
		
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "<br><br>";
		int counter = 1;
		if(bg.getTemplate().getType() == BattleGroundType.CTF){
		    html += "<font color='4CB1E5'><center>Elyos - Flags: " + EFlags + " - Points: " + EPts + "</center></font>";
		    html += "<br><br>";
		    
		    html += "<table border='0'>";
			html += "<tr><td><center>Pos.</center></td><td><center>Name</center></td><td><center>&nbsp;Points&nbsp;</center></td><td><center>&nbsp;&nbsp;Kills&nbsp;&nbsp;</center></td><td><center>&nbsp;&nbsp;Flags&nbsp;&nbsp;</center></td>";
			counter = 1;
			for(Player e : elyos)
			{
				html += "<tr><td>" + counter + ". </td><td>" + e.getName() + "&nbsp;&nbsp;</td><td>&nbsp; " + e.battlegroundSessionPoints + " &nbsp;</td><td>&nbsp; " + e.battlegroundSessionKills + " &nbsp;</td><td>&nbsp; " + e.battlegroundSessionFlags + "</td></tr>";
				counter++;
			}
			html += "</table>";
		}
		if(bg.getTemplate().getType() == BattleGroundType.ASSAULT){
		    html += "<font color='4CB1E5'><center>Elyos - Points: " + EPts + " - Kills: " + EKills + "</center></font>";
		    html += "<br><br>";
		    
		    html += "<table border='0'>";
			html += "<tr><td><center>Pos.</center></td><td><center>Name</center></td><td><center>&nbsp;Points&nbsp;</center></td><td><center>&nbsp;&nbsp;Kills&nbsp;&nbsp;</center></td><td><center>&nbsp;&nbsp;Deaths&nbsp;&nbsp;</center></td>";
			counter = 1;
			for(Player e : elyos)
			{
				html += "<tr><td>" + counter + ". </td><td>" + e.getName() + "&nbsp;&nbsp;</td><td>&nbsp; " + e.battlegroundSessionPoints + " &nbsp;</td><td>&nbsp; " + e.battlegroundSessionKills + " &nbsp;</td><td>&nbsp; " + e.battlegroundSessionDeaths + "</td></tr>";
				counter++;
			}
			html += "</table>";
		}
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>\n";
		html += "		</select>\n";
		html += "	</question>\n";
		
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "<br><br>";
		if(bg.getTemplate().getType() == BattleGroundType.CTF){
            html += "<font color='ffba75'><center>Asmodians - Flags: " + AFlags + " - Pts: " + APts + "</center></font>";
            html += "<br><br>";
            
            html += "<table border='0'>";
            html += "<tr><td><center>Pos.</center></td><td><center>Name</center></td><td><center>&nbsp;Points&nbsp;</center></td><td><center>&nbsp;&nbsp;Kills&nbsp;&nbsp;</center></td><td><center>&nbsp;&nbsp;Flags&nbsp;&nbsp;</center></td>";
    		counter = 1;
    		for(Player a : asmos)
    		{
    			html += "<tr><td>" + counter + ". </td><td>" + a.getName() + "&nbsp;&nbsp;</td><td>&nbsp; " + a.battlegroundSessionPoints + " &nbsp;</td><td>&nbsp; " + a.battlegroundSessionKills + " &nbsp;</td><td>&nbsp; " + a.battlegroundSessionFlags + "</td></tr>";
    			counter++;
    		}
    		html += "</table>";
        }
        if(bg.getTemplate().getType() == BattleGroundType.ASSAULT){
            html += "<font color='ffba75'><center>Asmodians - Points: " + APts + " - Kills: " + AKills + "</center></font>";
		    html += "<br><br>";
		    
		    html += "<table border='0'>";
	        html += "<tr><td><center>Pos.</center></td><td><center>Name</center></td><td><center>Points</center></td><td><center>Kills</center></td><td><center>Deaths</center></td>";
			counter = 1;
			for(Player a : asmos)
			{
				html += "<tr><td>" + counter + ". </td><td>" + a.getName() + "&nbsp;&nbsp;</td><td>&nbsp; " + a.battlegroundSessionPoints + " &nbsp;</td><td>&nbsp; " + a.battlegroundSessionKills + " &nbsp;</td><td>&nbsp; " + a.battlegroundSessionDeaths + "</td></tr>";
				counter++;
			}
			html += "</table>";
        }
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>\n";
		html += "		</select>\n";
		html += "	</question>\n";
		
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "<br><br>";
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>\n";
		html += "			<input type='radio' checked='checked'>Close</input>";
		html += "		</select>\n";
		html += "	</question>\n";
		
		html += "</questions>\n";
		html += "</poll>\n";
		return html;
		
	}
	
	public static String buildArenaTeamRegistrationForm()
	{
		String html = "<poll>\n";
		html += "<poll_introduction>\n";
		html += "	<![CDATA[<font color='4CB1E5'>Arena Team Registration</font>]]>\n";
		html += "</poll_introduction>\n";
		html += "<poll_title>\n";
		html += "	<font color='ffc519'></font>\n";
		html += "</poll_title>\n";
		html += "<start_date>2010-08-08 00:00</start_date>\n";
		html += "<end_date>2010-09-14 01:00</end_date>\n";
		html += "<servers></servers>\n";
		html += "<order_num></order_num>\n";
		html += "<race></race>\n";
		html += "<main_class></main_class>\n";
		html += "<world_id></world_id>\n";
		html += "<item_id>";
		html += 0;
		html += "</item_id>\n";
		html += "<item_cnt>";
		html += 0;
		html += "</item_cnt>\n";
		html += "<level>1~55</level>\n";
		html += "<questions>\n";
		
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "Choose your arena team name :<br><br>";
		html += "			]]>\n";
		html += "		</title>\n";
		html += "       <input type='text' />\n";
		html += "	</question>\n";
		
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "Choose battle type for this team :<br><br>";
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>\n";
		html += "			<input type='radio'>2v2</input>";
		html += "			<input type='radio'>3v3</input>";
		html += "			<input type='radio'>5v5</input>";
		html += "		</select>\n";
		html += "	</question>\n";
		
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "Creating an Arena Team costs 1.000.000 kinah.<br><br>";
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>";
		html += "       <input type='radio'>I'm fine with it !</input>\n";
		html += "		</select>";
		html += "	</question>\n";
		
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		html += "Are you sure to create a new team ?<br><br>";
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>";
		html += "       <input type='radio'>Yes, I am</input>\n";
		html += "       <input type='radio'>Cancel creation request</input>\n";
		html += "		</select>";
		html += "	</question>\n";
		
		html += "</questions>\n";
		html += "</poll>\n";
		return html;
	}
	
	public static String TeamRegistrationForm()
    {
        String html = "<poll>\n";
        html += "<poll_introduction>\n";
        html += "   <![CDATA[<font color='4CB1E5'>Team Registration for Battle</font>]]>\n";
        html += "</poll_introduction>\n";
        html += "<poll_title>\n";
        html += "   <font color='ffc519'></font>\n";
        html += "</poll_title>\n";
        html += "<start_date>2010-08-08 00:00</start_date>\n";
        html += "<end_date>2010-09-14 01:00</end_date>\n";
        html += "<servers></servers>\n";
        html += "<order_num></order_num>\n";
        html += "<race></race>\n";
        html += "<main_class></main_class>\n";
        html += "<world_id></world_id>\n";
        html += "<item_id>";
        html += 0;
        html += "</item_id>\n";
        html += "<item_cnt>";
        html += 0;
        html += "</item_cnt>\n";
        html += "<level>1~55</level>\n";
        html += "<questions>\n";
        
        html += "   <question>\n";
        html += "       <title>\n";
        html += "           <![CDATA[\n";
        
        html += "<br><br>";
        html += "Choose the team you want to fight with : ";
        html += "<br><br>";
        
        html += "           ]]>\n";
        html += "       </title>\n";
        html += "       <select>\n";
        
        html += "           <input type='radio'>Register with your team 2v2</input>";
        html += "           <input type='radio'>Register with your team 3v3</input>";
        html += "           <input type='radio'>Register with your team 5v5</input>";
        
        html += "           <input type='radio'>Close</input>";
        html += "       </select>\n";
        html += "   </question>\n";
        
        html += "</questions>\n";
        html += "</poll>\n";
        return html;        
    }
	
	public static String getRenameTeamMenu(String teamName)
    {
        String html = "<poll>\n";
        html += "<poll_introduction>\n";
        html += "   <![CDATA[<font color='4CB1E5'>Rename Arena Team</font>]]>\n";
        html += "</poll_introduction>\n";
        html += "<poll_title>\n";
        html += "   <font color='ffc519'></font>\n";
        html += "</poll_title>\n";
        html += "<start_date>2010-08-08 00:00</start_date>\n";
        html += "<end_date>2010-09-14 01:00</end_date>\n";
        html += "<servers></servers>\n";
        html += "<order_num></order_num>\n";
        html += "<race></race>\n";
        html += "<main_class></main_class>\n";
        html += "<world_id></world_id>\n";
        html += "<item_id>";
        html += 0;
        html += "</item_id>\n";
        html += "<item_cnt>";
        html += 0;
        html += "</item_cnt>\n";
        html += "<level>1~55</level>\n";
        html += "<questions>\n";
        
        html += "   <question>\n";
        html += "       <title>\n";
        html += "           <![CDATA[\n";
        html += "The previous name of your team was : " + teamName + "<br><br>";
        html += "Choose your new arena team name :<br><br>";
        html += "           ]]>\n";
        html += "       </title>\n";
        html += "       <input type='text' />\n";
        html += "   </question>\n";
        
        html += "   <question>\n";
        html += "       <title>\n";
        html += "           <![CDATA[\n";
        html += "Rename an Arena Team costs 500.000 kinah.<br><br>";
        html += "           ]]>\n";
        html += "       </title>\n";
        html += "       <select>";
        html += "       <input type='radio'>I'm fine with it !</input>\n";
        html += "       </select>";
        html += "   </question>\n";
        
        html += "   <question>\n";
        html += "       <title>\n";
        html += "           <![CDATA[\n";
        html += "Are you sure to rename your team ?<br><br>";
        html += "           ]]>\n";
        html += "       </title>\n";
        html += "       <select>";
        html += "       <input type='radio'>Yes, I am</input>\n";
        html += "       <input type='radio'>Cancel rename request</input>\n";
        html += "       </select>";
        html += "   </question>\n";
        
        html += "</questions>\n";
        html += "</poll>\n";
        return html;
    }
	
	public static String getRemovePlayerMenu()
	{
		String html = "<poll>\n";
        html += "<poll_introduction>\n";
        html += "   <![CDATA[<font color='4CB1E5'>Remove Team Player</font>]]>\n";
        html += "</poll_introduction>\n";
        html += "<poll_title>\n";
        html += "   <font color='ffc519'></font>\n";
        html += "</poll_title>\n";
        html += "<start_date>2010-08-08 00:00</start_date>\n";
        html += "<end_date>2010-09-14 01:00</end_date>\n";
        html += "<servers></servers>\n";
        html += "<order_num></order_num>\n";
        html += "<race></race>\n";
        html += "<main_class></main_class>\n";
        html += "<world_id></world_id>\n";
        html += "<item_id>";
        html += 0;
        html += "</item_id>\n";
        html += "<item_cnt>";
        html += 0;
        html += "</item_cnt>\n";
        html += "<level>1~55</level>\n";
        html += "<questions>\n";
        
        html += "   <question>\n";
        html += "       <title>\n";
        html += "           <![CDATA[\n";
        html += "<br><br>";
        html += "Choose which player do you want to remove from your team :<br><br>";
        html += "           ]]>\n";
        html += "       </title>\n";
        html += "       <input type='text' />\n";
        html += "   </question>\n";
        
        html += "   <question>\n";
        html += "       <title>\n";
        html += "           <![CDATA[\n";
        html += "Are you sure to remove this player ?<br><br>";
        html += "           ]]>\n";
        html += "       </title>\n";
        html += "       <select>";
        html += "       <input type='radio'>Yes, I am</input>\n";
        html += "       <input type='radio'>Cancel remove request</input>\n";
        html += "       </select>";
        html += "   </question>\n";
        
        html += "</questions>\n";
        html += "</poll>\n";
        return html;
	}
	
	public static String getManageTeamMenu2v2()
    {
        String html = "<poll>\n";
        html += "<poll_introduction>\n";
        html += "   <![CDATA[<font color='4CB1E5'>Manage your 2v2 Arena Team</font>]]>\n";
        html += "</poll_introduction>\n";
        html += "<poll_title>\n";
        html += "   <font color='ffc519'></font>\n";
        html += "</poll_title>\n";
        html += "<start_date>2010-08-08 00:00</start_date>\n";
        html += "<end_date>2010-09-14 01:00</end_date>\n";
        html += "<servers></servers>\n";
        html += "<order_num></order_num>\n";
        html += "<race></race>\n";
        html += "<main_class></main_class>\n";
        html += "<world_id></world_id>\n";
        html += "<item_id>";
        html += 0;
        html += "</item_id>\n";
        html += "<item_cnt>";
        html += 0;
        html += "</item_cnt>\n";
        html += "<level>1~55</level>\n";
        html += "<questions>\n";
        
        html += "   <question>\n";
        html += "       <title>\n";
        html += "           <![CDATA[\n";
        
        html += "<br><br>";
        html += "You can invite a player or remove one of your team with this menu.<br>";
        html += "What do you want to do ?";
        html += "<br><br>";
        
        html += "           ]]>\n";
        html += "       </title>\n";
        html += "       <select>\n";
        
        html += "           <input type='radio'>Invite in my 2v2 Arena Team</input>";
        
        html += "           <input type='radio'>Remove from my 2v2 Arena Team</input>";
        
        html += "           <input type='radio'>Delete my 2v2 Arena Team</input>";
        
        html += "           <input type='radio'>Rename my 2v2 Arena Team</input>";
        
        html += "           <input type='radio'>Close</input>";
        html += "       </select>\n";
        html += "   </question>\n";
        
        html += "</questions>\n";
        html += "</poll>\n";
        return html;        
    }
	
	public static String getManageTeamMenu3v3()
    {
        String html = "<poll>\n";
        html += "<poll_introduction>\n";
        html += "   <![CDATA[<font color='4CB1E5'>Manage your 2v2 Arena Team</font>]]>\n";
        html += "</poll_introduction>\n";
        html += "<poll_title>\n";
        html += "   <font color='ffc519'></font>\n";
        html += "</poll_title>\n";
        html += "<start_date>2010-08-08 00:00</start_date>\n";
        html += "<end_date>2010-09-14 01:00</end_date>\n";
        html += "<servers></servers>\n";
        html += "<order_num></order_num>\n";
        html += "<race></race>\n";
        html += "<main_class></main_class>\n";
        html += "<world_id></world_id>\n";
        html += "<item_id>";
        html += 0;
        html += "</item_id>\n";
        html += "<item_cnt>";
        html += 0;
        html += "</item_cnt>\n";
        html += "<level>1~55</level>\n";
        html += "<questions>\n";
        
        html += "   <question>\n";
        html += "       <title>\n";
        html += "           <![CDATA[\n";
        
        html += "<br><br>";
        html += "You can invite a player or remove one of your team with this menu.<br>";
        html += "What do you want to do ?";
        html += "<br><br>";
        
        html += "           ]]>\n";
        html += "       </title>\n";
        html += "       <select>\n";
        
        html += "           <input type='radio'>Invite in my 3v3 Arena Team</input>";
        
        html += "           <input type='radio'>Remove from my 3v3 Arena Team</input>";
        
        html += "           <input type='radio'>Delete my 3v3 Arena Team</input>";
        
        html += "           <input type='radio'>Rename my 3v3 Arena Team</input>";
        
        html += "           <input type='radio'>Close</input>";
        html += "       </select>\n";
        html += "   </question>\n";
        
        html += "</questions>\n";
        html += "</poll>\n";
        return html;        
    }
	
	public static String getManageTeamMenu5v5()
    {
        String html = "<poll>\n";
        html += "<poll_introduction>\n";
        html += "   <![CDATA[<font color='4CB1E5'>Manage your 5v5 Arena Team</font>]]>\n";
        html += "</poll_introduction>\n";
        html += "<poll_title>\n";
        html += "   <font color='ffc519'></font>\n";
        html += "</poll_title>\n";
        html += "<start_date>2010-08-08 00:00</start_date>\n";
        html += "<end_date>2010-09-14 01:00</end_date>\n";
        html += "<servers></servers>\n";
        html += "<order_num></order_num>\n";
        html += "<race></race>\n";
        html += "<main_class></main_class>\n";
        html += "<world_id></world_id>\n";
        html += "<item_id>";
        html += 0;
        html += "</item_id>\n";
        html += "<item_cnt>";
        html += 0;
        html += "</item_cnt>\n";
        html += "<level>1~55</level>\n";
        html += "<questions>\n";
        
        html += "   <question>\n";
        html += "       <title>\n";
        html += "           <![CDATA[\n";
        
        html += "<br><br>";
        html += "You can invite a player or remove one of your team with this menu.<br>";
        html += "What do you want to do ?";
        html += "<br><br>";
        
        html += "           ]]>\n";
        html += "       </title>\n";
        html += "       <select>\n";
        
        html += "           <input type='radio'>Invite in my 5v5 Arena Team</input>";
        
        html += "           <input type='radio'>Remove from my 5v5 Arena Team</input>";
        
        html += "           <input type='radio'>Delete my 5v5 Arena Team</input>";
        
        html += "           <input type='radio'>Rename my 5v5 Arena Team</input>";
        
        html += "           <input type='radio'>Close</input>";
        html += "       </select>\n";
        html += "   </question>\n";
        
        html += "</questions>\n";
        html += "</poll>\n";
        return html;        
    }
	
	public static String getArenaManagementMenu(Player player)
	{
		String html = "<poll>\n";
		html += "<poll_introduction>\n";
		html += "	<![CDATA[<font color='4CB1E5'>Arena Team Management</font>]]>\n";
		html += "</poll_introduction>\n";
		html += "<poll_title>\n";
		html += "	<font color='ffc519'></font>\n";
		html += "</poll_title>\n";
		html += "<start_date>2010-08-08 00:00</start_date>\n";
		html += "<end_date>2010-09-14 01:00</end_date>\n";
		html += "<servers></servers>\n";
		html += "<order_num></order_num>\n";
		html += "<race></race>\n";
		html += "<main_class></main_class>\n";
		html += "<world_id></world_id>\n";
		html += "<item_id>";
		html += 0;
		html += "</item_id>\n";
		html += "<item_cnt>";
		html += 0;
		html += "</item_cnt>\n";
		html += "<level>1~55</level>\n";
		html += "<questions>\n";
		
		html += "	<question>\n";
		html += "		<title>\n";
		html += "			<![CDATA[\n";
		if(player.getArenaTeam2v2() == null && player.getArenaTeam3v3() == null && player.getArenaTeam5v5() == null)
			html += "You are not a member of an Arena Team.<br><br><br><br>";
		else
			html += "My Arena Teams<br><br><br><br>";
		
		if(player.getArenaTeam2v2() != null)
		{
			html += "- 2v2: " + player.getArenaTeam2v2().getTeamName() + " (Points : " + player.getArenaTeam2v2().getTeamPoints() + ")<br><br>";
			for(Integer playerId : player.getArenaTeam2v2().getPlayersOids())
			{
                String pName = ArenaManager.getPlayerName(playerId);
                if(pName != null)
                    html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;> " + pName + "<br>";
            }
			html += "<br><br><br><br>";
		}
		
		if(player.getArenaTeam3v3() != null)
		{
			html += "- 3v3: " + player.getArenaTeam3v3().getTeamName() + " (Points : " + player.getArenaTeam3v3().getTeamPoints() + ")<br><br>";
			for(Integer playerId : player.getArenaTeam3v3().getPlayersOids())
			{
			    String pName = ArenaManager.getPlayerName(playerId);
                if(pName != null)
                    html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;> " + pName + "<br>";
            }
            html += "<br><br><br><br>";
		}
		
		if(player.getArenaTeam5v5() != null)
		{
			html += "- 5v5: " + player.getArenaTeam5v5().getTeamName() + " (Points : " + player.getArenaTeam5v5().getTeamPoints() + ")<br><br>";
			for(Integer playerId : player.getArenaTeam5v5().getPlayersOids())
			{
			    String pName = ArenaManager.getPlayerName(playerId);
                if(pName != null)
                    html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;> " + pName + "<br>";
            }
            html += "<br><br><br><br>";
		}
		
		html += "You have " + player.getCommonData().getArenaPoints() + " Arena Points<br><br><br><br>";
		
		html += "What do you want to do ?<br><br><br><br>\n";
		
		html += "			]]>\n";
		html += "		</title>\n";
		html += "		<select>\n";
		html += "			<input type='radio'>Create a new Arena Team</input>";
		
		html += "			<input type='radio'>Manage my team 2v2</input>";
		html += "           <input type='radio'>Manage my team 3v3</input>";
		html += "           <input type='radio'>Manage my team 5v5</input>";
		
		html += "           <input type='radio'>Register for Battle !</input>";
		
		html += "           <input type='radio'>Unregister your team from Battle !</input>";
		
		html += "           <input type='radio'>Top 10 Arena Teams !</input>";
		
		html += "			<input type='radio'>Close this window</input>";
		html += "		</select>\n";
		html += "	</question>\n";
		
		html += "</questions>\n";
		html += "</poll>\n";
		return html;		
	}
	
	public static String buildAionArenaTeamReport(Player player, List<Player> arenaPlayers, AionArenaTeam Team1, AionArenaTeam Team2)
    {
        String html = "<poll>\n";
        html += "<poll_introduction>\n";
        html += "   <![CDATA[<font color='4CB1E5' size='+1'><center>Final Ranking</center></font>]]>\n";
        html += "</poll_introduction>\n";
        html += "<poll_title>\n";
        html += "   <font color='ffc519'></font>\n";
        html += "</poll_title>\n";
        html += "<start_date>2010-08-08 00:00</start_date>\n";
        html += "<end_date>2010-09-14 01:00</end_date>\n";
        html += "<servers></servers>\n";
        html += "<order_num></order_num>\n";
        html += "<race></race>\n";
        html += "<main_class></main_class>\n";
        html += "<world_id></world_id>\n";
        html += "<item_id>";
        html += 0;
        html += "</item_id>\n";
        html += "<item_cnt>";
        html += 0;
        html += "</item_cnt>\n";
        html += "<level>1~55</level>\n";
        html += "<questions>\n";
        
        List<Player> arenaPlayers1 = ArenaManager.getRanking(arenaPlayers,1);
        List<Player> arenaPlayers2 = ArenaManager.getRanking(arenaPlayers,2);
        
        int Team1Pts = 0;
        int Team2Pts = 0;
        int Team1Kills = 0;
        int Team2Kills = 0;
        for(Player p1 : arenaPlayers1)
        {
        	Team1Pts += p1.ArenaTeamSessionPoints;
        	Team1Kills += p1.ArenaTeamSessionKills;
        }
        for(Player p2 : arenaPlayers2)
        {
        	Team2Pts += p2.ArenaTeamSessionPoints;
        	Team2Kills += p2.ArenaTeamSessionKills;
        }
        
        String winner = "";
        String winnerColor = "";
        
        if(Team1Pts > Team2Pts){
            winner = Team1.getTeamName();
            Race teamRace = DAOManager.getDAO(PlayerDAO.class).getPlayerRaceByObjId(Team1.getPlayersOids().get(0));
            if(teamRace == Race.ELYOS)
            	winnerColor = "4CB1E5";
        	else if(teamRace == Race.ASMODIANS)
        		winnerColor = "ffba75";
        }
        else if(Team1Pts == Team2Pts){
            winner = "The two factions";
            winnerColor = "00C000";}
        else if(Team1Pts < Team2Pts){
            winner = Team2.getTeamName();
            Race teamRace = DAOManager.getDAO(PlayerDAO.class).getPlayerRaceByObjId(Team1.getPlayersOids().get(0));
            if(teamRace == Race.ELYOS)
            	winnerColor = "4CB1E5";
        	else if(teamRace == Race.ASMODIANS)
        		winnerColor = "ffba75";
        }
        
        html += "   <question>\n";
        html += "       <title>\n";
        html += "           <![CDATA[\n";
        html += "<br><br>";
        html += "<font size='+2' style='font-weight: bold;'>Winner : </font><font color='" + winnerColor + "' size='+2' style='font-weight: bold;'>" + winner + "</font>";
        html += "           ]]>\n";
        html += "       </title>\n";
        html += "       <select>\n";
        html += "       </select>\n";
        html += "   </question>\n";
        
        html += "   <question>\n";
        html += "       <title>\n";
        html += "           <![CDATA[\n";
        html += "<br><br>";
        
        String teamColor = "";
    	Race teamRace1 = DAOManager.getDAO(PlayerDAO.class).getPlayerRaceByObjId(Team1.getPlayersOids().get(0));
    	Race teamRace2 = DAOManager.getDAO(PlayerDAO.class).getPlayerRaceByObjId(Team2.getPlayersOids().get(0));
    	if(teamRace1 == Race.ELYOS)
    		teamColor = "4CB1E5";
    	else if(teamRace1 == Race.ASMODIANS)
    		teamColor = "ffba75";
    	
        html += "<font color='" + teamColor + "'><center>" + Team1.getTeamName() + " - Points: " + Team1Pts + " - Kills: " + Team1Kills + "</center></font>";
        html += "<br><br>";
        
        html += "<table border='0'>";    
        html += "<tr><td><center>Pos.</center></td><td><center>Name</center></td><td><center>&nbsp;Points&nbsp;</center></td><td><center>&nbsp;&nbsp;Kills&nbsp;&nbsp;</center></td><td><center>&nbsp;&nbsp;Deaths&nbsp;&nbsp;</center></td>";
		int counter = 1;
        for(Player p1 : arenaPlayers1)
        {
            html += "<tr><td>" + counter + ". </td><td>" + p1.getName() + "&nbsp;&nbsp;</td><td>&nbsp; " + p1.ArenaTeamSessionPoints + " &nbsp;</td><td>&nbsp; " + p1.ArenaTeamSessionKills + " &nbsp;</td><td>&nbsp; " + p1.ArenaTeamSessionDeaths + "</td></tr>";
            counter++;
        }
        html += "</table>";
        
        html += "           ]]>\n";
        html += "       </title>\n";
        html += "       <select>\n";
        html += "       </select>\n";
        html += "   </question>\n";
        
        html += "   <question>\n";
        html += "       <title>\n";
        html += "           <![CDATA[\n";
        html += "<br><br>";
        
        if(teamRace2 == Race.ELYOS)
    		teamColor = "4CB1E5";
    	else if(teamRace2 == Race.ASMODIANS)
    		teamColor = "ffba75";
        
        html += "<font color='" + teamColor + "'><center>" + Team2.getTeamName() + " - Points: " + Team2Pts + " - Kills: " + Team2Kills + "</center></font>";
        html += "<br><br>";
        
        html += "<table border='0'>";
        html += "<tr><td><center>Pos.</center></td><td><center>Name</center></td><td><center>&nbsp;Points&nbsp;</center></td><td><center>&nbsp;&nbsp;Kills&nbsp;&nbsp;</center></td><td><center>&nbsp;&nbsp;Deaths&nbsp;&nbsp;</center></td>";
		counter = 1;
        for(Player p2 : arenaPlayers2)
        {
            html += "<tr><td>" + counter + ". </td><td>" + p2.getName() + "&nbsp;&nbsp;</td><td>&nbsp; " + p2.ArenaTeamSessionPoints + " &nbsp;</td><td>&nbsp; " + p2.ArenaTeamSessionKills + " &nbsp;</td><td>&nbsp; " + p2.ArenaTeamSessionDeaths + "</td></tr>";
            counter++;
        }
        html += "</table>";
        
        html += "           ]]>\n";
        html += "       </title>\n";
        html += "       <select>\n";
        html += "       </select>\n";
        html += "   </question>\n";
        
        html += "   <question>\n";
        html += "       <title>\n";
        html += "           <![CDATA[\n";
        html += "<br><br><br><br>";
        
        int earnedPoints = player.ArenaTeamSessionPoints;
        html += "You have earned " + earnedPoints + " Arena points, ";
        html += "your total Arena points is now : " + (player.getCommonData().getArenaPoints() + earnedPoints) + ".<br><br>";
        ArenaManager.commitPoints(player);
        
        html += "           ]]>\n";
        html += "       </title>\n";
        html += "       <select>\n";
        html += "           <input type='radio' checked='checked'>Close</input>";
        html += "       </select>\n";
        html += "   </question>\n";
        
        html += "</questions>\n";
        html += "</poll>\n";
        return html;
    }
	
	public static String TeamTopRank(List<AionArenaTeam> Teams2v2, List<AionArenaTeam> Teams3v3, List<AionArenaTeam> Teams5v5)
    {
        String html = "<poll>\n";
        html += "<poll_introduction>\n";
        html += "   <![CDATA[<font color='00C000' size='+1'><center>Top 10 Arena Teams</center></font>]]>\n";
        html += "</poll_introduction>\n";
        html += "<poll_title>\n";
        html += "   <font color='ffc519'></font>\n";
        html += "</poll_title>\n";
        html += "<start_date>2010-08-08 00:00</start_date>\n";
        html += "<end_date>2010-09-14 01:00</end_date>\n";
        html += "<servers></servers>\n";
        html += "<order_num></order_num>\n";
        html += "<race></race>\n";
        html += "<main_class></main_class>\n";
        html += "<world_id></world_id>\n";
        html += "<item_id>";
        html += 0;
        html += "</item_id>\n";
        html += "<item_cnt>";
        html += 0;
        html += "</item_cnt>\n";
        html += "<level>1~55</level>\n";
        html += "<questions>\n";
        
        html += "   <question>\n";
        html += "       <title>\n";
        html += "           <![CDATA[\n";
        
        html += "<br><br>";
        html += "<font size='+1'><center>2v2 Arena Teams</center></font><br><br>";
        html += "<table border='0'>";
        int counter = 1;
        for(AionArenaTeam team : Teams2v2)
        {
        	String teamColor = "";
        	Race teamRace = DAOManager.getDAO(PlayerDAO.class).getPlayerRaceByObjId(team.getPlayersOids().get(0));
        	if(teamRace == Race.ELYOS)
        		teamColor = "4CB1E5";
        	else if(teamRace == Race.ASMODIANS)
        		teamColor = "ffba75";
        	html += "<tr><td><font color='" + teamColor + "' style='font-weight: bold;'>" + counter + ". " + team.getTeamName() + "&nbsp;&nbsp;&nbsp;&nbsp;</td><td>" + team.getTeamPoints() + "</font></td></tr>";
        	counter++;
        }
        html += "</table>";
        
        html += "<br><br><br><br>";
        html += "<font size='+1'><center>3v3 Arena Teams</center></font><br><br>";
        html += "<table border='0'>";
        for(AionArenaTeam team : Teams3v3)
        {
        	String teamColor = "";
        	Race teamRace = DAOManager.getDAO(PlayerDAO.class).getPlayerRaceByObjId(team.getPlayersOids().get(0));
        	if(teamRace == Race.ELYOS)
        		teamColor = "4CB1E5";
        	else if(teamRace == Race.ASMODIANS)
        		teamColor = "ffba75";
        	html += "<tr><td><font color='" + teamColor + "' style='font-weight: bold;'>" + counter + ". " + team.getTeamName() + "&nbsp;&nbsp;&nbsp;&nbsp;</td><td>" + team.getTeamPoints() + "</font></td></tr>";
        	counter++;
        }
        html += "</table>";
        
        html += "<br><br><br><br>";
        html += "<font size='+1'><center>5v5 Arena Teams</center></font><br><br>";
        html += "<table border='0'>";
        for(AionArenaTeam team : Teams5v5)
        {
        	String teamColor = "";
        	Race teamRace = DAOManager.getDAO(PlayerDAO.class).getPlayerRaceByObjId(team.getPlayersOids().get(0));
        	if(teamRace == Race.ELYOS)
        		teamColor = "4CB1E5";
        	else if(teamRace == Race.ASMODIANS)
        		teamColor = "ffba75";
        	html += "<tr><td><font color='" + teamColor + "' style='font-weight: bold;'>" + counter + ". " + team.getTeamName() + "&nbsp;&nbsp;&nbsp;&nbsp;</td><td>" + team.getTeamPoints() + "</font></td></tr>";
        	counter++;
        }
        html += "</table>";
        
        html += "<br><br>";
        
        html += "           ]]>\n";
        html += "       </title>\n";
        html += "       <select>\n";
        html += "           <input type='radio' checked='checked'>Close</input>";
        html += "       </select>\n";
        html += "   </question>\n";
        
        html += "</questions>\n";
        html += "</poll>\n";
        return html;
    }
}
