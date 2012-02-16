package gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

/**
 * @author kosyachok, Pashatr
 *
 */
public class SM_ACADEMY_BOOTCAMP_STAGE extends AionServerPacket
{
	private int arenaId;
    private int stage;
    private int round;
    private boolean win;
    
    public SM_ACADEMY_BOOTCAMP_STAGE(int stage, int round, boolean win)
    {
    	int arenaId = 1;
		switch(stage)
		{
			case 1:
			case 2:
			case 3:
			case 4:
				arenaId = 1;
				break;
			case 5:
				arenaId = 2;
				break;
			case 6:
				arenaId = 3;
				break;
			case 7:
				arenaId = 4;
				break;
			case 8:
				arenaId = 5;
				break;				
			case 9:
				arenaId = 6;
				break;
			case 10:
				arenaId = 7;
				break;
			default:
				arenaId = 1;
				break;				
		}
    	
    	this.arenaId = arenaId;
        this.stage = stage;
        this.round = round;
        this.win = win;
    }
    
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf)
    {
        writeC(buf, 2);
        writeD(buf, 0);
        
        int stagevalue = (100000 * arenaId) + (1000 * stage) + (win ? 100 : 0) + round; // F@#$ING NCSoft!!! WHY!?
        writeD(buf, stagevalue);
    }
}
