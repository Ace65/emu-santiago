/*
 * This file is part of aion-unique <aion-unique.org>
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.greater_stigma_quest;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;


public class _21049StigmaExpansion extends QuestHandler
{
    private final static int questId = 21049;

    public _21049StigmaExpansion()
	{
        super(questId);
    }
	
    @Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(defaultQuestNoneDialog(env, 799208, 1011))
			return true;
			
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;
			
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 799208)
			{
				switch(env.getDialogId())
				{
					case 26:
						return sendQuestDialog(env, 2375);
					case 34:
						return defaultQuestItemCheck(env, 0, 0, true, 5, 2716);
					case 5:
						return defaultCloseDialog(env, 0, 1);
					case 2716:
						return defaultCloseDialog(env, 1, 0);
				}
			}
		}
		return defaultQuestRewardDialog(env, 799208, 5);
	}

    @Override
    public void register()
	{
        qe.setNpcQuestData(799208).addOnQuestStart(questId); //Garath
        qe.setNpcQuestData(799208).addOnTalkEvent(questId); //Garath
    }
}