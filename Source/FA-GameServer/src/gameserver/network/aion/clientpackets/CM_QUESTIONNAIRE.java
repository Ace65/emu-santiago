/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.network.aion.clientpackets;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import gameserver.dataholders.DataManager;
import gameserver.network.aion.AionClientPacket;
import gameserver.services.HTMLService;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.BattleGroundTemplate;
import gameserver.utils.PacketSendUtility;
 
import br.focus.arena.AionArenaService;
import br.focus.arena.ArenaManager;
import br.focus.battleground.BattleGroundManager;
import br.focus.factories.SurveyFactory;

/**
 * @author lhw, Kaipo and ginho1
 */
public class CM_QUESTIONNAIRE extends AionClientPacket
{
     
     private int objectId;
     @SuppressWarnings("unused") //Unused yet, always 0
     private int unk;
     private String[] params;

    
	
	
	
	public CM_QUESTIONNAIRE(int opcode)
    {
        super(opcode);
    }

   
    /* (non-Javadoc)
     * @see commons.network.packet.BaseClientPacket#readImpl()
     */
    @Override
    protected void readImpl()
    {
        objectId = readD();
        unk = readH();
        String data = readS();
        params = data.split(",");
    }
 
    /* (non-Javadoc)
     * @see commons.network.packet.BaseClientPacket#runImpl()
     */
    @Override
    protected void runImpl()
    {
        Player player = getConnection().getActivePlayer();
        if(objectId > 0)
        {
            try
            {
                switch(objectId - player.SurveyCounter)
                {
                    // arena team registration
                    case 141000001:
                        if(!params[3].equals("\"2\""))
                            AionArenaService.processTeamCreationRequest(player, params[0].replace("\"", ""), Short.parseShort(params[1].replace("\"", "")), (params[2].equals("\"1\"")));
                        break;
                        // arena team menu (.arena)
                    case 142000001:
                        switch(Short.parseShort(params[0].replace("\"", "")))
                        {
                            // create new team
                            case 1:
                                HTMLService.showHTML(player, SurveyFactory.buildArenaTeamRegistrationForm(), 141000001);
                                break;
                                // manage team 2v2
                            case 2:
                                if(player.getArenaTeam2v2() != null)
                                    HTMLService.showHTML(player, SurveyFactory.getManageTeamMenu2v2(), 142000002);
                                else
                                    PacketSendUtility.sendMessage(player, "You don't have any 2v2 Arena Team");
                                break;
                                // manage team 3v3
                            case 3:
                                if(player.getArenaTeam3v3() != null)
                                    HTMLService.showHTML(player, SurveyFactory.getManageTeamMenu3v3(), 142000003);
                                else
                                    PacketSendUtility.sendMessage(player, "You don't have any 3v3 Arena Team");
                                break;
                                // manage team 5v5
                            case 4:
                                if(player.getArenaTeam5v5() != null)
                                    HTMLService.showHTML(player, SurveyFactory.getManageTeamMenu5v5(), 142000004);
                                else
                                    PacketSendUtility.sendMessage(player, "You don't have any 5v5 Arena Team");
                                break;
                                // register in battle
                            case 5:
                                HTMLService.showHTML(player, SurveyFactory.TeamRegistrationForm(), 142000005);
                                break;
                               // unregister from battle
                            case 6:
                                ArenaManager.unregisterTeam(player);
                                break;
                                // Show the top rank
                            case 7:
                            	AionArenaService.reloadTopRank();
                                HTMLService.showHTML(player, SurveyFactory.TeamTopRank(AionArenaService.TopTeams2v2, AionArenaService.TopTeams3v3, AionArenaService.TopTeams5v5), 152000001);
                                break;
                                // close window
                            case 8:
                            default:
                                break;
                        }
                        break;
                        // manage menu team 2v2
                    case 142000002:
                        switch(Short.parseShort(params[0].replace("\"", "")))
                        {
                                // invite
                            case 1:
                                if(player.getTarget() instanceof Player)
                                    AionArenaService.invitePlayer(player, (Player)player.getTarget(), (short)2);
                                break;
                                // remove
                            case 2:
                                HTMLService.showHTML(player, SurveyFactory.getRemovePlayerMenu(), 142000009);
                                break;
                                // delete
                            case 3:
                                AionArenaService.deleteTeam(player, (short)2);
                                break;
                                // rename
                            case 4:
                                HTMLService.showHTML(player, SurveyFactory.getRenameTeamMenu(player.getArenaTeam2v2().getTeamName()), 142000006);
                                break;
                                // close window
                            case 5:
                            default:
                                break;
                        }
                        break;
                        // manage menu team 3v3
                    case 142000003:
                        switch(Short.parseShort(params[0].replace("\"", "")))
                        {
                                // invite
                            case 1:
                                if(player.getTarget() instanceof Player)
                                    AionArenaService.invitePlayer(player, (Player)player.getTarget(), (short)3);
                                break;
                                // remove
                            case 2:
                                HTMLService.showHTML(player, SurveyFactory.getRemovePlayerMenu(), 142000010);
                                break;
                                // delete
                            case 3:
                                AionArenaService.deleteTeam(player, (short)3);
                                break;
                                // rename
                            case 4:
                                HTMLService.showHTML(player, SurveyFactory.getRenameTeamMenu(player.getArenaTeam3v3().getTeamName()), 142000007);
                                break;
                                // close window
                            case 5:
                            default:
                                break;
                        }
                        break;
                        // manage menu team 5v5
                    case 142000004:
                        switch(Short.parseShort(params[0].replace("\"", "")))
                        {
                                // invite
                            case 1:
                                if(player.getTarget() instanceof Player)
                                    AionArenaService.invitePlayer(player, (Player)player.getTarget(), (short)5);
                                break;
                                // remove
                            case 2:

                                HTMLService.showHTML(player, SurveyFactory.getRemovePlayerMenu(), 142000011);
                                break;
                                // delete
                            case 3:
                                AionArenaService.deleteTeam(player, (short)5);
                                break;
                                // rename
                            case 4:
                                HTMLService.showHTML(player, SurveyFactory.getRenameTeamMenu(player.getArenaTeam5v5().getTeamName()), 142000008);
                                break;
                                // close window
                            case 5:
                            default:
                                break;
                        }
                        break;
                        //Team Registering for Battle
                    case 142000005:
                        if(player.ArenaStatus > 0)
                            PacketSendUtility.sendMessage(player, "One of your team is already registered for the next battle !");
                        
                        else if(player.getBattleGround() != null)
            			{
            				PacketSendUtility.sendMessage(player, "You are already registered in a battleground.");
            				PacketSendUtility.sendMessage(player, "Use your spell Return to leave the battleground.");
            			}
            			else if(player.battlegroundWaiting)
            			{
            				PacketSendUtility.sendMessage(player, "You are already registered in a battleground.");
            				PacketSendUtility.sendMessage(player, "Use the command .bg unregister to cancel your registration.");
            			}
                        	
                        else
                        {
                            short action2 = Short.parseShort(params[0].replace("\"", ""));
                            switch(action2)
                            {
                                // Register with Team 2v2
                                case 1:
                                    if(player.getArenaTeam2v2().getPlayersOids().size() != 2 || player.getArenaTeam2v2().getPlayersOids() == null)
                                        PacketSendUtility.sendMessage(player, "You don't have a full team 2v2");
                                    else
                                        ArenaManager.registerTeam2(player);
                                    break;
                                // Register with Team 3v3
                                case 2:
                                    if(player.getArenaTeam3v3().getPlayersOids().size() != 3 || player.getArenaTeam3v3().getPlayersOids() == null)
                                        PacketSendUtility.sendMessage(player, "You don't have a full team 3v3");
                                    else
                                        ArenaManager.registerTeam3(player);
                                    break;
                                // Register with Team 5v5
                                case 3:
                                    if(player.getArenaTeam5v5().getPlayersOids().size() != 5 || player.getArenaTeam5v5().getPlayersOids() == null)
                                        PacketSendUtility.sendMessage(player, "You don't have a full team 5v5");
                                    else
                                        ArenaManager.registerTeam5(player);
                                    break;
                                // Close Window
                                case 4:
                                default:
                                    break;
                            }
                        }
                        break;
                        // Rename Arena Team 2v2
                    case 142000006:
                        if(!params[2].equals("\"2\""))
                            AionArenaService.renameTeam(player, (short) 2, params[0].replace("\"", ""), (params[1].equals("\"1\"")));
                        break;
                        // Rename Arena Team 3v3
                    case 142000007:
                        if(!params[2].equals("\"2\""))
                            AionArenaService.renameTeam(player, (short) 3, params[0].replace("\"", ""), (params[1].equals("\"1\"")));
                        break;
                        // Rename Arena Team 5v5
                    case 142000008:
                        if(!params[2].equals("\"2\""))
                            AionArenaService.renameTeam(player, (short) 5, params[0].replace("\"", ""), (params[1].equals("\"1\"")));
                        break;
                        // Remove Team Player 2v2
                    case 142000009:
                        if(!params[1].equals("\"2\""))
                        	AionArenaService.removePlayer(player, params[0].replace("\"", ""), (short)2);
                        // Remove Team Player 3v3
                    case 142000010:
                        if(!params[1].equals("\"2\""))
                        	AionArenaService.removePlayer(player, params[0].replace("\"", ""), (short)3);
                        // Remove Team Player 5v5
                    case 142000011:
                        if(!params[1].equals("\"2\""))
                        	AionArenaService.removePlayer(player, params[0].replace("\"", ""), (short)5);
                        // battleground registration
                    case 150000001:
                        List<BattleGroundTemplate> acceptedTemplates = new ArrayList<BattleGroundTemplate>(); 
                        for(BattleGroundTemplate template : DataManager.BATTLEGROUND_DATA.getAllTemplates())
                        {
                            if(player.getLevel() < template.getJoinConditions().getRequiredLevel())
                                continue;
                            if(player.getCommonData().getBattleGroundPoints() < template.getJoinConditions().getRequiredBgPoints())
                                continue;
                            if(player.getLevel() > template.getJoinConditions().getMaxLevel())
                                continue;
                            if(player.getCommonData().getBattleGroundPoints() > template.getJoinConditions().getMaxBgPoints())
                                continue;
                            acceptedTemplates.add(template);
                        }

                        if(acceptedTemplates.size() == 0)
                            return;
                        
                        int choice = Integer.parseInt(params[0].replace("\"", ""))-1;
                        BattleGroundTemplate tpl = acceptedTemplates.get(choice);

                        if(tpl == null)
                            return;

                        int tplId = tpl.getTplId();
                        BattleGroundManager.registerPlayer(player, tplId);
							
                        break;
                    case 151000001:
                        List<BattleGroundTemplate> acceptedBattlegrounds = new ArrayList<BattleGroundTemplate>(); 
                        for(BattleGroundTemplate template : DataManager.BATTLEGROUND_DATA.getAllTemplates())
                        {
                            acceptedBattlegrounds.add(template);
                        }

                        if(acceptedBattlegrounds.size() == 0)
                            return;
                        
                        int choiceObs = Integer.parseInt(params[0].replace("\"", ""))-1;
                        BattleGroundTemplate BgTpl = acceptedBattlegrounds.get(choiceObs);
                        
                        if(BgTpl == null)
                            return;

                        int BgTplId = BgTpl.getTplId();
                        BattleGroundManager.registerPlayerObs(player, BgTplId);
                        
                        break;
                    case 152000001:
                        break;
                    default:
                        HTMLService.getMessage(player, objectId);
                        break;
                }
            }
            catch(Exception e)
            {
                Logger.getLogger(CM_QUESTIONNAIRE.class).error(e);
            }
        }
    }
}
