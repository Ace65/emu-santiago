/*
 * This file is part of aion-extreme <aion-core.net>.
 *
 *  aion-extreme is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-extreme is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-extreme.  If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.services;

import gameserver.model.gameobjects.Monster;
import gameserver.model.group.PlayerGroup;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.spawn.SpawnEngine;

/**
 * @author Khaos, 
 * http://www.diamondcore-mmorpgs.com
* 
*/


public class SplinterInstanceService
{
    public void onGroupReward(Monster monster, PlayerGroup group)
    {
        // When kills Pazuzu spawn chests
        if(monster.getObjectTemplate().getTemplateId() == 216951)
        {
            SpawnTemplate chest1, chest2, chest3, chest4;
            
            chest1 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700937, 673f, 360f, 466f, (byte)88, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest1, group.getGroupLeader().getInstanceId());

            chest2 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700934, 667f, 360f, 466f, (byte)78, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest2, group.getGroupLeader().getInstanceId());

            chest3 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700937, 686f, 308f, 465f, (byte)37, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest3, group.getGroupLeader().getInstanceId());
			
            chest4 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700934, 687f, 314f, 465f, (byte)78, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest4, group.getGroupLeader().getInstanceId());
        }
        
        //When kills Kaluva the Fourth Fragment spawn chests
        if(monster.getObjectTemplate().getTemplateId() == 216950)
        {
            SpawnTemplate chest1, chest2, chest3, chest4, chest5, chest6;
            
            chest1 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700937, 664f, 510f, 422f, (byte)39, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest1, group.getGroupLeader().getInstanceId());

            chest2 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700937, 658f, 509f, 422f, (byte)43, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest2, group.getGroupLeader().getInstanceId());

            chest3 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700934, 654f, 511f, 422f, (byte)21, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest3, group.getGroupLeader().getInstanceId());
			
            chest4 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700937, 633f, 513f, 422f, (byte)43, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest4, group.getGroupLeader().getInstanceId());

            chest5 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700934, 627f, 513f, 422f, (byte)29, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest5, group.getGroupLeader().getInstanceId());
			
            chest6 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700937, 608f, 519f, 422f, (byte)18, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest6, group.getGroupLeader().getInstanceId());
        }
        
        //When kills Dayshade spawns Boss Rukril & Ebonsoul
        if(monster.getObjectTemplate().getTemplateId() == 282010)
        {
            SpawnTemplate rukril, ebonsoul;
            
            rukril = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 216948, 415f, 664f, 437f, (byte)10, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(rukril, group.getGroupLeader().getInstanceId());

            ebonsoul = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 216949, 447f, 735f, 437f, (byte)94, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(ebonsoul, group.getGroupLeader().getInstanceId());
        }

        //When kills Rukril spawns chests
        if(monster.getObjectTemplate().getTemplateId() == 216948)
        {
            SpawnTemplate chest1, chest2, chest3, chest4, chest5;
            
            chest1 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700859, 348f, 728f, 420f, (byte)0, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest1, group.getGroupLeader().getInstanceId());

            chest2 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700859, 349f, 736f, 365f, (byte)40, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest2, group.getGroupLeader().getInstanceId());

            chest3 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700859, 309f, 770f, 343f, (byte)99, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest3, group.getGroupLeader().getInstanceId());
			
            chest4 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700859, 351f, 790f, 320f, (byte)74, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest4, group.getGroupLeader().getInstanceId());

            chest5 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700859, 384f, 761f, 274f, (byte)92, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest5, group.getGroupLeader().getInstanceId());
        }

        //When kills Ebonsoul spawns chests
        if(monster.getObjectTemplate().getTemplateId() == 216949)
        {
            SpawnTemplate chest1, chest2, chest3, chest4, chest5;
            
            chest1 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700859, 338f, 783f, 397f, (byte)111, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest1, group.getGroupLeader().getInstanceId());

            chest2 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700859, 362f, 776f, 347f, (byte)117, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest2, group.getGroupLeader().getInstanceId());

            chest3 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700859, 267f, 741f, 319f, (byte)6, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest3, group.getGroupLeader().getInstanceId());
			
            chest4 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700859, 279f, 769f, 287f, (byte)116, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest4, group.getGroupLeader().getInstanceId());

            chest5 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700859, 292f, 731f, 253f, (byte)2, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest5, group.getGroupLeader().getInstanceId());
        }

        //When kills Yamennes Painflare spawns chests
        if(monster.getObjectTemplate().getTemplateId() == 216960)
        {
            SpawnTemplate chest1, chest2, chest3, chest4, chest5;
            
            chest1 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700938, 299f, 752f, 197f, (byte)106, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest1, group.getGroupLeader().getInstanceId());

            chest2 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700938, 309f, 756f, 196f, (byte)91, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest2, group.getGroupLeader().getInstanceId());

            chest3 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700937, 346f, 761f, 196f, (byte)82, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest3, group.getGroupLeader().getInstanceId());
			
            chest4 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700938, 354f, 760f, 196f, (byte)104, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest4, group.getGroupLeader().getInstanceId());

            chest5 = SpawnEngine.getInstance().addNewSpawn(300220000, group.getGroupLeader().getInstanceId(), 700938, 365f, 762f, 196f, (byte)81, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest5, group.getGroupLeader().getInstanceId());
        }
	
    }
    
    public static SplinterInstanceService getInstance()
    {
        return SingletonHolder.instance;
    }
    
    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder
    {
        protected static final SplinterInstanceService instance = new SplinterInstanceService();
    }
    
}
