package gameserver.dao;

import gameserver.model.gameobjects.player.Motion;
import gameserver.model.gameobjects.player.MotionList;
import commons.database.dao.DAO;
import gameserver.model.gameobjects.player.Player;

/**
 * @author
 *
 */
public abstract class PlayerMotionListDAO implements DAO {
    
    @Override
    public final String getClassName()
    {
         return PlayerMotionListDAO.class.getName();
    }
    
    public abstract MotionList loadMotionList(int playerId);
    
    public abstract void addMotion(Player player, Motion motion);
    
    public abstract void updateMotion(Player player, Motion motion);
    
    public abstract boolean storeMotions(Player player);
    
    public abstract boolean supports(String databaseName, int majorVersion, int minorVersion);

}
