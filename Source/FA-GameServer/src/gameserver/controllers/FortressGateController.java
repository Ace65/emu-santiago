/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.controllers;

import gameserver.model.Race;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Executor;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.Trap;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.model.siege.FortressGate;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.TeleportService;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;


/**
 * @author Sylar, Ritsu
 *
 */
public class FortressGateController extends NpcController
{	

	@Override
	public void onDie(Creature lastAttacker)
	{
		final Player destroyer;
		if(lastAttacker instanceof Player)
			destroyer = (Player)lastAttacker;
		else if(lastAttacker instanceof Trap)
			destroyer = (Player)((Trap)lastAttacker).getMaster();
		else if(lastAttacker instanceof Summon)
			destroyer = ((Summon)lastAttacker).getMaster();
		else
			destroyer = null;
		
		if(destroyer != null)
		{
			getOwner().getKnownList().doOnAllPlayers(new Executor<Player>(){
				
				@Override
				public boolean run(Player object)
				{
					PacketSendUtility.sendPacket(object, new SM_SYSTEM_MESSAGE(1400305, destroyer.getLegion().getLegionName(), destroyer.getName()));
					return true;
				}
			}, true);
		}
		super.onDelete();
	}
	
	@Override
	public void onDialogRequest(Player p)
	{
		
		if((p.getCommonData().getRace() == Race.ELYOS && (getOwner().getObjectTemplate().getRace() == Race.PC_LIGHT_CASTLE_DOOR || getOwner().getObjectTemplate().getTribe().contains("_LIGHT"))) ||
			(p.getCommonData().getRace() == Race.ASMODIANS && (getOwner().getObjectTemplate().getRace() == Race.PC_DARK_CASTLE_DOOR || getOwner().getObjectTemplate().getTribe().contains("_DARK"))))
		
		{
			RequestResponseHandler gateHandler = new RequestResponseHandler(p)
			{
				@Override
				public void denyRequest(Creature requester, Player p)
				{
					// Close window
				}
				
				@Override
				public void acceptRequest(Creature requester, Player p)
				{
					double radian = Math.toRadians(MathUtil.convertHeadingToDegree(p.getHeading()));
					int worldId = getOwner().getWorldId();
					float x = (float)(p.getX() + (10 * Math.cos(radian)));
					float y = (float)(p.getY() + (10 * Math.sin(radian)));
					float z = p.getZ() + 0.5f;
					TeleportService.teleportTo(p, worldId, x, y, z, 1);
				}
			};
			if(p.getResponseRequester().putRequest(160017, gateHandler))
			{
				PacketSendUtility.sendPacket(p, new SM_QUESTION_WINDOW(160017, p.getObjectId()));
			}
		}
	}
	
	@Override
	public void onRespawn()
	{
		super.onRespawn();
	}

	@Override
	public FortressGate getOwner()
	{
		return (FortressGate) super.getOwner();
	}
}
