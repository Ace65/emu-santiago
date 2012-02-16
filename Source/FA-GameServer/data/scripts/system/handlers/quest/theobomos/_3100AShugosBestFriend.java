 /*
 * This file is part of aion-engine <aion-engine.org>.
 *
 * aion-engine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-engine.  If not, see <http://www.gnu.org/licenses/>.
 */

package quest.theobomos;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;

import java.util.Collections;

/**
 * @author Orpheo
 */
 
public class _3100AShugosBestFriend extends QuestHandler
{
	private final static int	questId	= 3100;
	
	public _3100AShugosBestFriend()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203792).addOnQuestStart(questId);
		qe.setNpcQuestData(203792).addOnTalkEvent(questId);
		qe.setNpcQuestData(798168).addOnTalkEvent(questId);
		qe.setNpcQuestData(798169).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE) 
		{
			if(targetId == 203792)
			{
				switch(env.getDialogId())
				{
					case 26:
					{
						return sendQuestDialog(env, 1011);
					}
					case 1002:
					{
						if(player.getInventory().getItemCountByItemId(182208072) == 0)
						{
							if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182208072, 1))))
							{
								return true;
							}
						}
					}
					default: return defaultQuestStartDialog(env);
				}
			}
		}
		
		if(qs == null)
			return false;
			
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 798168:
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							if(qs.getQuestVarById(0) == 0)
							{
								return sendQuestDialog(env, 1352);
							}
						}
						case 10000:
						{
							player.getInventory().removeFromBagByItemId(182208072, 1);
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798169)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
}