package quest.relic;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;

public class _1850RelicRewardAncientCrownCore extends QuestHandler {
	private final static int	questId	= 1850;
	private final static int	Items[][] =
	{{1011, 1352, 1693, 2034},
	{186000054, 186000053, 186000052, 186000051}};

    public _1850RelicRewardAncientCrownCore() {
        super(questId);
    }

	@Override
	public void register()
	{
		qe.setNpcQuestData(279059).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		int	removeItem = 0;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = 0;
		
		if(qs != null)
			var = qs.getQuestVarById(0);
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE || qs.getStatus() == QuestStatus.COMPLETE)
		{
			if(env.getTargetId() == 279059)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel()))
							return sendQuestDialog(env, 1011);
						else
							return sendQuestDialog(env, 3398);
					case 1011:
					case 1352:
					case 1693:
					case 2034:
						int i = 0;
						for(int id: Items[0])
						{
							if(id == env.getDialogId())
								break;
							i++;
						}
						removeItem = Items[1][i];
						var = i;
						
						if(player.getInventory().getItemCountByItemId(removeItem) > 0)
						{
							if(qs == null)
							{
								qs = new QuestState(questId, QuestStatus.REWARD, 0, 0);
								player.getQuestStateList().addQuest(questId, qs);
							}
							else
								qs.setStatus(QuestStatus.REWARD);
							qs.setQuestVar(var);
							defaultQuestRemoveItem(env, removeItem, 1);
							return sendQuestDialog(env, var + 5);
						}
						else
							return sendQuestDialog(env, 1009);
				}
			}
		}
		
		if(qs == null)
			return false;
		
		return defaultQuestRewardDialog(env, 279059, 0, var);
	}
}
