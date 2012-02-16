/*
 * Copyright (c) 2011 by Aion Extreme
 *
 * This file is part of Aion Extreme <http://aion-core.net>.
 *
 * Aion Extreme <http://www.aion-core.net> is free software: you
 * can  redistribute  it and/or modify it under the terms
 * of  the GNU General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * Aion Extreme <http://www.aion-core.net> is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without  even  the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See  the  GNU General Public License for more details.
 * You  should  have  received  a copy of the GNU General
 * Public License along with Aion Extreme 
 * <http://www.aion-core.net>.If not,see <http://www.gnu.org/licenses/>.
 */

package gameserver.controllers;

import gameserver.dataholders.DataManager;
import gameserver.model.Race;
import gameserver.model.TaskId;
import gameserver.model.gameobjects.BattleGroundFlag;
import gameserver.model.gameobjects.BattleGroundFlag.BattleGroundFlagState;
import gameserver.model.gameobjects.Executor;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.BattleGroundTemplate;
import gameserver.model.templates.ObjectLocation;
import gameserver.network.aion.serverpackets.SM_NPC_INFO;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;

import br.focus.battleground.CTFBattleGround;

/**
 * @author ambrosius / adapted DESTRUCTOR/HADESH
 *
 */
public class BattleGroundFlagController extends NpcController
{       

        public boolean dropped = true;  

        private float lastX,lastY,lastZ = 0;

        private ObjectLocation loc;
        
        protected BattleGroundTemplate template;

        private void onFlagCaptured(Player carrier)
        {
                int tplId = carrier.getBattleGround().getTplId();
                template = DataManager.BATTLEGROUND_DATA.getBattleGroundTemplate(tplId);
            
                carrier.getBattleGround().broadcastToBattleGround(carrier.getCommonData().getName() + (carrier.getLegion() != null ? " of " + carrier.getLegion().getLegionName() : "") + " has captured the " + (getOwner().getRace() == Race.ELYOS ? "Elyos" : "Asmodian") +  " flag !", null);
                
                carrier.getController().getTask(TaskId.BATTLEGROUND_CARRY_FLAG).cancel(true);
                carrier.getController().addTask(TaskId.BATTLEGROUND_CARRY_FLAG, null);
                
                carrier.getBattleGround().increasePoints(carrier, template.getRules().getFlagCap());
                
                carrier.battlegroundFlag.state = BattleGroundFlagState.AT_BASE;
                
                World.getInstance().updatePosition(getOwner(), getOwner().getSpawn().getX(), getOwner().getSpawn().getY(), getOwner().getSpawn().getZ(), getOwner().getSpawn().getHeading(), true);

                getOwner().setFlagHolder(null);
                dropped = true;
                carrier.battlegroundFlag = null;

                carrier.getBattleGround().getInstance().doOnAllPlayers(new Executor<Player>(){

                        @Override
                        public boolean run(Player object)
                        {
                                PacketSendUtility.sendPacket(object, new SM_NPC_INFO(getOwner(), object));
                                return true;
                        }
                }, true);

                ((CTFBattleGround)carrier.getBattleGround()).score(carrier.getCommonData().getRace());
                                
                carrier.getBattleGround().broadcastToBattleGround("The score is now : Ely. " + ((CTFBattleGround)carrier.getBattleGround()).getElyosFlagCount() + " - " + ((CTFBattleGround)carrier.getBattleGround()).getAsmosFlagCount() + " Asmo.", null);
                
        }

        @Override
        public void onDialogRequest(final Player player)
        {       
                int tplId = player.getBattleGround().getTplId();
                template = DataManager.BATTLEGROUND_DATA.getBattleGroundTemplate(tplId);
            
                if(getOwner() == null)
                        return;
                if(loc == null)
                {
                        loc = template.getFlagLocation();
                }
                if(player.getBattleGround() != null && player.getBattleGround().running)
                {
                        if(dropped && player.getController().getTask(TaskId.BATTLEGROUND_CARRY_FLAG) == null && player.getCommonData().getRace() != getOwner().getRace() && getOwner().getFlagHolder() == null)
                        {
                                getOwner().setFlagHolder(player);
                                dropped = false;
                                player.battlegroundFlag = getOwner();
                                
                                if(getOwner().state == BattleGroundFlagState.AT_BASE)
                                {
                                        getOwner().state = BattleGroundFlagState.ON_FIELD;
                                        
                                        // points for capturing flag in other base
                                        player.getBattleGround().increasePoints(player, template.getRules().getFlagBase());
                                }
                                else
                                        player.getBattleGround().increasePoints(player, template.getRules().getFlagGround());

                                player.getBattleGround().broadcastToBattleGround(player.getCommonData().getName() + (player.getLegion() != null ? " of " + player.getLegion().getLegionName() : "") + " got the " + (getOwner().getRace() == Race.ELYOS ? "Elyos" : "Asmodian") +  " flag !", null);

                                player.getController().addTask(TaskId.BATTLEGROUND_CARRY_FLAG, ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(){

                                        @Override
                                        public void run()
                                        {
                                                if(player.getX() == lastX && player.getY() == lastY && player.getZ() == lastZ)
                                                        return;
                                                World.getInstance().updatePosition(getOwner(), player.getX(), player.getY(), player.getZ(), player.getHeading(), false);
                                                lastX = player.getX();
                                                lastY = player.getY();
                                                lastZ = player.getZ();
                                                PacketSendUtility.broadcastPacket(player, new SM_NPC_INFO(getOwner(), player), true);

                                                if(loc != null)
                                                {
                                                        if(MathUtil.getDistance(getOwner(), loc.getXe(), loc.getYe(), loc.getZe()) <= 2f && getOwner().getRace() == Race.ASMODIANS)
                                                                onFlagCaptured(player);
                                                        else if(MathUtil.getDistance(getOwner(), loc.getXa(), loc.getYa(), loc.getZa()) <= 2f && getOwner().getRace() == Race.ELYOS)
                                                                onFlagCaptured(player);
                                                }

                                        }
                                }, 0, 15));
                        }
                        else if(!dropped && player.getController().getTask(TaskId.BATTLEGROUND_CARRY_FLAG) != null && player.getCommonData().getRace() != getOwner().getRace())
                        {
                                dropped = true;
                                getOwner().setFlagHolder(null);

                                player.getController().getTask(TaskId.BATTLEGROUND_CARRY_FLAG).cancel(true);
                                player.getController().addTask(TaskId.BATTLEGROUND_CARRY_FLAG, null);

                                player.getBattleGround().broadcastToBattleGround(player.getCommonData().getName() + (player.getLegion() != null ? " of " + player.getLegion().getLegionName() : "") + " has dropped the " + (player.battlegroundFlag.getRace() == Race.ELYOS ? "Elyos" : "Asmodian") + " flag !", null);
                                player.battlegroundFlag = null;
                        }
                        else if(dropped && player.getCommonData().getRace() == getOwner().getRace())
                        {
                                World.getInstance().updatePosition(getOwner(), getOwner().getSpawn().getX(), getOwner().getSpawn().getY(), getOwner().getSpawn().getZ(), getOwner().getSpawn().getHeading(), true);

                                getOwner().setFlagHolder(null);
                                
                                getOwner().state = BattleGroundFlagState.AT_BASE;

                                player.getBattleGround().getInstance().doOnAllPlayers(new Executor<Player>(){

                                        @Override
                                        public boolean run(Player object)
                                        {
                                                PacketSendUtility.sendPacket(object, new SM_NPC_INFO(getOwner(), object));
                                                return true;
                                        }
                                }, true);

                                player.getBattleGround().broadcastToBattleGround(player.getCommonData().getName() + (player.getLegion() != null ? " of " + player.getLegion().getLegionName() : "") + " has returned the " + (getOwner().getRace() == Race.ELYOS ? "Elyos" : "Asmodian") + " flag at its base !", null);

                        }
                        else
                                PacketSendUtility.sendMessage(player, "unhandled case");
                }
        }

        @Override
        public BattleGroundFlag getOwner()
        {
                return  (BattleGroundFlag) super.getOwner();
        }
}
