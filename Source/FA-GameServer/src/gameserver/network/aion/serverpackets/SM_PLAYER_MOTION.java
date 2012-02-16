package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.player.Motion;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;



/**
 * @author sylar
 * 
 */
public class SM_PLAYER_MOTION extends AionServerPacket
{
    private boolean readFirst;
    private boolean toSelf;
    private int motionId;
    private int status;

    private Player player;
    private int objectId;

    public SM_PLAYER_MOTION(Player player, boolean readFirst)
    {
        this.player = player;
        this.readFirst = readFirst;
    }

    public SM_PLAYER_MOTION(Player player, int motionId, int status, boolean readFirst, boolean toSelf)
    {
        this.player = player;
        this.readFirst = readFirst;
        this.motionId = motionId;
        this.status = status;
        this.toSelf = toSelf;
    }

    public SM_PLAYER_MOTION(Player player, boolean readFirst, boolean toSelf)
    {
        this.player = player;
        this.readFirst = readFirst;
        this.toSelf = toSelf;
        this.objectId = player.getObjectId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf)
    {
        if (readFirst)
        {
            writeC(buf, 0x01); // 0x01 Learn Motion
            writeH(buf, 0x08); // Number of Motions
            
            for (Motion motion : player.getMotionList().getMotions()) 
            {
                writeC(buf, motion.getMotionState());
                writeD(buf, 0x00);
                if(motion.getActive() == true)
                    writeH(buf, 0x01);
                else
                    writeH(buf, 0x00);
            }
        }
        else
        {
            if (toSelf)
            {
                writeC(buf, 0x05); // 0x05 - Update Own Motion Status
                writeH(buf, motionId);
                writeC(buf, status);
            }
            else
            {
                writeC(buf, 0x07); // 0x07 - Update Motion Status for Other Players to See
                writeD(buf, objectId); // objectID of Player
                writeH(buf, player.getCurrentWaitingMotion());
                writeH(buf, player.getCurrentRunningMotion());
                writeH(buf, player.getCurrentJumpingMotion());
                writeH(buf, player.getCurrentRestMotion());
            }
        }
    }
}
