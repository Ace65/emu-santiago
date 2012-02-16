package gameserver.network.aion.clientpackets;

import org.apache.log4j.Logger;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_PLAYER_MOTION;
import gameserver.utils.PacketSendUtility;


/**
 * 
 * @author sylar
 * 
 */
public class CM_PLAYER_MOTION_SET extends AionClientPacket
{
    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(CM_PLAYER_MOTION_SET.class);
    @SuppressWarnings("unused")
    private int unk = 0;
    private int motionId = 0;
    private int status=0;

    public CM_PLAYER_MOTION_SET(int opcode)
    {
        super(opcode);
    }

    @Override
    protected void readImpl()
    {
        unk = readC(); // Always 0x04
        motionId = readH(); // New Motion ID to Set = 0~8
        status = readC(); // Player Motion Type to Update = 1~4
    }

    @Override
    protected void runImpl()
    {
        Player player = getConnection().getActivePlayer();
        if(status !=0)
        {
            switch(motionId)
            {
                case 0:
                    switch(status)
                    {
                        case 1:
                            player.setCurrentWaitingMotion(motionId);
                            break;
                        case 2:
                            player.setCurrentRunningMotion(motionId);
                            break;
                        case 3:
                            player.setCurrentJumpingMotion(motionId);
                            break;
                        case 4:
                            player.setCurrentRestMotion(motionId);
                            break;
                    }
                    break;
                case 1: case 5:
                    player.setCurrentWaitingMotion(motionId);
                    break;
                case 2: case 6:
                    player.setCurrentRunningMotion(motionId);
                    break;
                case 3: case 7:
                    player.setCurrentJumpingMotion(motionId);
                    break;
                case 4: case 8:
                    player.setCurrentRestMotion(motionId);
                    break;
            }

        }
        PacketSendUtility.sendPacket(player, new SM_PLAYER_MOTION(player, motionId, status, false, true));
        PacketSendUtility.broadcastPacket(player, new SM_PLAYER_MOTION(player, false, false));
    }
}
