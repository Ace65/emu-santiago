package gameserver.model.gameobjects.player;

/**
 * @author
 *
 */
public class Motion 
{
    private int motion_state;
    private boolean motion_active;
    private long motion_date = 0;
    private long motion_expires_time = 0;
    
    
    /**
     * @param Motion 
     * 
     */       
    
    public Motion(int motion_state, boolean motion_active, long motion_date, long motion_expires_time)
    {
        this.motion_state               = motion_state;
        this.motion_active          = motion_active;
        this.motion_date            = motion_date;
        this.motion_expires_time    = motion_expires_time;
    }
    
    public int getMotionState()
    {
        return motion_state;
    }
    
    public boolean getActive()
    {
        return motion_active;
    }
    
    public long getMotionCreationTime()
    {
        return motion_date;
    }

    public long getMotionExpiresTime()
    {
        return motion_expires_time;
    }

    public void setMotionState(int motion_state)
    {
        this.motion_state = motion_state;
    }
    
    public void setActive(boolean en)
    {
        this.motion_active = en;
    }
    
    public long getMotionTimeLeft()
    {
        if(motion_expires_time == 0)
            return 0;

        long timeLeft = (motion_date + ((motion_expires_time )  * 1000L)) - System.currentTimeMillis();
        if(timeLeft < 0)
            timeLeft = 0;

        return timeLeft /1000L ;
    }

    public void setMotionDate(long motion_date)
    {
        this.motion_date = motion_date;
    }

    public void setMotionExpiresTime(long motion_expires_time)
    {
        this.motion_expires_time = motion_expires_time;
    }
}