package gameserver.model.gameobjects.player;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * @author
 *
 */
public class MotionList {
    
    private LinkedHashMap<Integer, Motion> motions;
    private Player owner;
    
    public MotionList()
    {
        this.motions = new LinkedHashMap<Integer, Motion>();
        this.owner = null;
        
    }
    
    public void setOwner(Player owner)
    {
            this.owner = owner;
    }
    
    public Player getOwner()
    {
            return owner;
    }
    
    public boolean add(int state, boolean active, long date, long expires_time)
    {
        if(!motions.containsKey(state))
        {
            motions.put(state, new Motion(state, active, date, expires_time));
            return true;
        }
        return false;
    }
    
    public void remove(int id)
    {
        if(motions.containsKey(id))
        {
            motions.remove(id);
        }
    }
    
    public Motion get(int state)
    {
        if(motions.containsKey(state))
            return motions.get(state);

        return null;
    }
    
    public boolean canAdd(int state)
    {
        if(motions.containsKey(state))
            return false;

        return true;
    }
    
    public Collection<Motion> getMotions()
    {
        return motions.values();
    }

}
