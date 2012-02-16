package gameserver.network.aion.clientpackets;

import gameserver.model.gameobjects.Executor;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.instances.EmpyreanCrucible;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.AcademyBootcampService;
import gameserver.services.InstanceService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;

public class CM_ACADEMY_BOOTCAMP_EXIT extends AionClientPacket
{
	public CM_ACADEMY_BOOTCAMP_EXIT(int opcode)
	{
		super(opcode);
	}
	
	@Override
	protected void readImpl()
	{

	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();

		if(AcademyBootcampService.isAcademyBootcamp(player.getWorldId()))
		{
			long ticketCount = player.getInventory().getItemCountByItemId(186000124);
			if(ticketCount > 0)
				player.getInventory().removeFromBagByItemId(186000124, ticketCount);

			TeleportService.teleportToInstanceExit(player, player.getWorldId(), player.getInstanceId(), 0);

			if(player.isInGroup())
			{
				EmpyreanCrucible arena = (EmpyreanCrucible)InstanceService.getRegisteredInstance(300300000, player.getPlayerGroup().getGroupId());
				if(arena != null)
				{
					arena.doOnAllPlayers(new Executor<Player>(){
						@Override
						public boolean run(Player p)
						{
							if(p != player)
								PacketSendUtility.sendPacket(p, SM_SYSTEM_MESSAGE.STR_MSG_FRIENDLY_LEAVE_IDARENA(player.getName()));
							return true;
						}
					}, true);
				}
			}
		}		
	}
}
