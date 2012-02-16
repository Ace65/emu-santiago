package gameserver.itemengine.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_PLAYER_MOTION;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

/**
 * @author
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MotionAction")
public class MotionAction extends AbstractItemAction
{
    @XmlAttribute
    protected int expire;
    
    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem)
    {

        boolean valid = false;
        
        final int id = parentItem.getItemTemplate().getTemplateId();
        
        switch (id)
        {
            case 188508001 :      // already learned this motion?
                if(player.getMotionList().canAdd(1))
                    valid = true;
                else
                    PacketSendUtility.sendMessage(player, "You have already learned the Ninja Motions!");
            break;
                
            case 188508002 :      // already learned this motion?
                if(player.getMotionList().canAdd(5))
                    valid = true;
                else
                    PacketSendUtility.sendMessage(player, "You have already learned the Levitation Motions!");
            break;
         // as more movement styles are added, put them here 
        }
        
        return valid;
    }
    
    @Override
    public void act(final Player player, Item parentItem, Item targetItem)
    {
        final int itemObjId = parentItem.getObjectId();
        final int id = parentItem.getItemTemplate().getTemplateId();
        
        /*
         * If reading a motion manual, activate the motion by setting 
         * the appropriate playerData bits
         */
        //TODO: Retail Messages
        //TODO: Add support for Learning Temporary Motions that Expire
        
        int i;
        
        switch (id)
        {
            case 188508001 :
                i = 1;
                for (;i<=4;i++)
                {
                    if(player.getMotionList().canAdd(i))
                        player.getMotionList().add(i,  true, System.currentTimeMillis(),(expire  * 60L));
                    else
                        player.getMotionList().get(i).setActive(true);
                }
                break;
            case 188508002 :
                i = 5;
                for (;i<=8;i++)
                {
                    if(player.getMotionList().canAdd(i))
                        player.getMotionList().add(i,  false, System.currentTimeMillis(),(expire  * 60L));
                    else
                        player.getMotionList().get(i).setActive(false);
                }
                break;
                /** as more motion styles are added, put their activators here **/
        }
        
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 50, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable(){
            
            @Override
            public void run()
            {
                switch (id){
                    case 188508001:
                        if(player.getInventory().removeFromBagByItemId(id, 1)){
                            PacketSendUtility.sendMessage(player, "You have learned the Ninja Motions!");
                        }
                        break;
                    case 188508002:
                        if(player.getInventory().removeFromBagByItemId(id, 1)){
                            PacketSendUtility.sendMessage(player, "You have learned the Levitate Motions!");
                        }
                        break;
                }
                PacketSendUtility.sendPacket(player, new SM_PLAYER_MOTION(player, true));
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
            }
        }, 5000);
    }
}
