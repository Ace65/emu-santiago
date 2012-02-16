/*
 * Copyright (c) 2011 by Aion Extreme
 *
 * This file is part of Aion Extreme <http://aion-core.net>.
 *
 * Aion Extreme <http://www.aion-core.net> is free software: you
 * can  redistribute  it and/or modify it under the terms
 * of  the GNU General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * Aion Extreme <http://www.aion-core.net> is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without  even  the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See  the  GNU General Public License for more details.
 * You  should  have  received  a copy of the GNU General
 * Public License along with Aion Extreme 
 * <http://www.aion-core.net>.If not,see <http://www.gnu.org/licenses/>.
 */

package gameserver.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javolution.util.FastMap;

//import org.apache.log4j.Logger;

import commons.utils.Rnd;
import gameserver.configs.main.CustomConfig;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Executor;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.PlayerGroup;
import gameserver.model.instances.EmpyreanCrucible;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.serverpackets.SM_ACADEMY_BOOTCAMP_STAGE;
import gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.spawn.SpawnEngine;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import gameserver.world.WorldMapInstance;

/**
 * @author kosyachok
 *
 */
public class AcademyBootcampService
{
	//private static final Logger	log	= Logger.getLogger(AcademyBootcampService.class);
    
    private SpawnLocation[] spawnCoords = 
    	{
    		new SpawnLocation(330f, 360f, 330f, 360f, 96f),//1 stage
    		new SpawnLocation(330f, 360f, 330f, 360f, 96f),//2 stage
    		new SpawnLocation(330f, 360f, 330f, 360f, 96f),//3 stage
    		new SpawnLocation(330f, 360f, 330f, 360f, 96f),//4 stage    		
    		new SpawnLocation(1245f, 1275f, 780f, 810f, 359f),//5 stage
    		new SpawnLocation(1610f, 1640f, 135f, 165f, 126f),//6 stage
    		new SpawnLocation(1770f, 1800f, 780f, 810f, 469f),//7 stage
    		new SpawnLocation(1760f, 1790f, 1740f, 1770f, 304f), //8 stage
    		new SpawnLocation(1295f, 1325f, 1720f, 1750f, 315f), //9 stage
    		new SpawnLocation(1750f, 1780f, 1270f, 1300f, 394f), //10 stage
    	};
    
    private SpawnLocation[] readyRooms = 
    {
    		new SpawnLocation(379f, 380f, 349f, 351f, 96.8f),//1 arena
    		new SpawnLocation(1259f, 1261f, 827f, 829f, 358.6f),//2,3 arena
    		new SpawnLocation(1815f, 1817f, 795f, 797f, 470.0f),//4 arena
    		new SpawnLocation(1777f, 1779f, 1726f, 1728f, 304.0f),//5 arena
    		new SpawnLocation(1355f, 1357f, 1746f, 1748f, 319.2f),//6 arena
    		new SpawnLocation(1750f, 1752f, 1255f, 1257f, 394.2f),//6 arena
    };
    
    
    public void onDialogSelect(Player requester, Npc keeper)
    {
        EmpyreanCrucible arena = (EmpyreanCrucible)World.getInstance().getWorldMap(300300000).getWorldMapInstanceById(keeper.getInstanceId());
        
        if(arena == null)
        	return;
        
        if(requester.getPlayerGroup() == null)
        {
        	return;
        }
        
        switch(keeper.getNpcId())
        {
            case 799567:
            	WeatherService.getInstance().changeRegionWeather(300300000, 0);
                startStage1(arena);
                break;
            case 799568:
                startStage2(arena);
                break;
            case 799569:
                startStage3(arena);
                break;
            case 205331:
                startStage4(arena);
                break;
            case 205332:
            	TeleportArenaGroupToStage(arena, 5);
                startStage5(arena);
                break;
            case 205333:
            	TeleportArenaGroupToStage(arena, 6);
                startStage6(arena);
                break;
            case 205334:
            	WeatherService.getInstance().changeRegionWeather(300300000, 1);
            	TeleportArenaGroupToStage(arena, 7);
                startStage7(arena);
                break;
            case 205335:
            	WeatherService.getInstance().changeRegionWeather(300300000, 0);
            	TeleportArenaGroupToStage(arena, 8);
                startStage8(arena);
                break;
            case 205336:
            	TeleportArenaGroupToStage(arena, 9);
                startStage9_1(arena);
                break;
            case 205337:
            	TeleportArenaGroupToStage(arena, 10);
                startStage10(arena);
                break;
            case 799573:
            	TeleportPlayerToCurrentStage(requester);
            	return;
            	
            default:
            	break;
        }
        
        final int stage = arena.getStage();
        
        arena.doOnAllPlayers(new Executor<Player>(){
    		@Override
    		public boolean run(Player player)
    		{
    			PacketSendUtility.sendPacket(player, new SM_ACADEMY_BOOTCAMP_STAGE(stage + 1, 0, false));
    			return true;
    		}
		});
        
        World.getInstance().despawn(keeper, true);
    }

    public void onReward(PlayerGroup winner, Creature monster)
    {
    	int instanceId = monster.getInstanceId();
    	
    	EmpyreanCrucible arena = (EmpyreanCrucible)World.getInstance().getWorldMap(300300000).getWorldMapInstanceById(instanceId);
    	
    	if(arena == null)
    	{
    		return;
    	}
    	
    	boolean allStagesDone = false;
    	
    	arena.onReward();
    	
    	if(arena.isStageDone())
    	{
    		if(!checkOnRewardRoundStart(winner, arena))
    		{
    			
    		int pointsReward = 0;
    		SpawnTemplate spawn;

    		switch(arena.getStage())
    		{
    		case 1:
    			pointsReward = 9700;
                spawn = SpawnEngine.getInstance().addNewSpawn(300300000, instanceId, 799568, 345f, 345f, 96f, (byte)0, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(spawn, instanceId);
                
                // Resurrection Boxes spawn
                SpawnTemplate readyRoomBoxSpawn1,readyRoomBoxSpawn2,readyRoomBoxSpawn3,readyRoomBoxSpawn4,readyRoomBoxSpawn5,readyRoomBoxSpawn6;
                
                readyRoomBoxSpawn1 = SpawnEngine.getInstance().addNewSpawn(300300000, instanceId, 217734, 340.8f, 354.8f, 96.0f, (byte)0, 0, 0, true);
                readyRoomBoxSpawn2 = SpawnEngine.getInstance().addNewSpawn(300300000, instanceId, 217734, 343.0f, 356.2f, 96.0f, (byte)0, 0, 0, true);
                readyRoomBoxSpawn3 = SpawnEngine.getInstance().addNewSpawn(300300000, instanceId, 217734, 345.6f, 358.5f, 96.0f, (byte)0, 0, 0, true);
                readyRoomBoxSpawn4 = SpawnEngine.getInstance().addNewSpawn(300300000, instanceId, 217734, 343.0f, 358.5f, 96.0f, (byte)0, 0, 0, true);
                readyRoomBoxSpawn5 = SpawnEngine.getInstance().addNewSpawn(300300000, instanceId, 217734, 340.9f, 357.1f, 96.0f, (byte)0, 0, 0, true);
                readyRoomBoxSpawn6 = SpawnEngine.getInstance().addNewSpawn(300300000, instanceId, 217734, 340.9f, 359.2f, 96.0f, (byte)0, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(readyRoomBoxSpawn1, instanceId);
                SpawnEngine.getInstance().spawnObject(readyRoomBoxSpawn2, instanceId);
                SpawnEngine.getInstance().spawnObject(readyRoomBoxSpawn3, instanceId);
                SpawnEngine.getInstance().spawnObject(readyRoomBoxSpawn4, instanceId);
                SpawnEngine.getInstance().spawnObject(readyRoomBoxSpawn5, instanceId);
                SpawnEngine.getInstance().spawnObject(readyRoomBoxSpawn6, instanceId);
    			break;
    		case 2:
    			pointsReward = 11200; //20900
                spawn = SpawnEngine.getInstance().addNewSpawn(300300000, instanceId, 799569, 345f, 345f, 96f, (byte)0, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(spawn, instanceId);
    			break;
    		case 3:
    			pointsReward = 12830; //33730
    			spawn = SpawnEngine.getInstance().addNewSpawn(300300000, instanceId, 205331, 345f, 345f, 96f, (byte)0, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(spawn, instanceId);
                
                //Ready room box spawn
                SpawnTemplate readyRoomBoxSpawn;
                SpawnLocation boxSpawnLoc = getReadyRoomLocation(3);
                readyRoomBoxSpawn = SpawnEngine.getInstance().addNewSpawn(300300000, instanceId, 217734, boxSpawnLoc.getCenterCoordX(), boxSpawnLoc.getCenterCoordY(), boxSpawnLoc.getZ(), (byte)0, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(readyRoomBoxSpawn, instanceId);
    			break;
    		case 4:
    			pointsReward = 14000; //47730
    			spawn = SpawnEngine.getInstance().addNewSpawn(300300000, instanceId, 205332, 345f, 345f, 96f, (byte)0, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(spawn, instanceId);
    			break;
    		case 5:
    			pointsReward = 15600; //63330
    			spawn = SpawnEngine.getInstance().addNewSpawn(300300000, instanceId, 205333, 1265f, 800f, 359f, (byte)0, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(spawn, instanceId);
    			break;
    		case 6:
    			pointsReward = 17170; //80500
    			spawn = SpawnEngine.getInstance().addNewSpawn(300300000, instanceId, 205334, 1626f, 151f, 126f, (byte)0, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(spawn, instanceId);
    			break;
    		case 7:
    			pointsReward = 24700; //105200
    			spawn = SpawnEngine.getInstance().addNewSpawn(300300000, instanceId, 205335, 1785f, 796f, 469f, (byte)0, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(spawn, instanceId);
    			break;
    		case 8:
    			pointsReward = 36200; //141400
    			spawn = SpawnEngine.getInstance().addNewSpawn(300300000, instanceId, 205336, 1776f, 1755f, 304f, (byte)0, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(spawn, instanceId);
    			break;
    		case 9:
    			pointsReward = 57300; //198700
    			spawn = SpawnEngine.getInstance().addNewSpawn(300300000, instanceId, 205337, 1311f, 1735f, 315f, (byte)0, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(spawn, instanceId);
    			break;
    		case 10:
    			pointsReward = 299500; //498200
    			allStagesDone = true;    	
    			break;		
    		}
    		
    		final EmpyreanCrucible finalArena = arena;
    		final int finalPointsReward = pointsReward;
    		arena.doOnAllPlayers(new Executor<Player>(){
        		@Override
        		public boolean run(Player player)
        		{
        			PacketSendUtility.sendPacket(player, new SM_ACADEMY_BOOTCAMP_STAGE(finalArena.getStage(), finalArena.getRound(), true));
        			if(player.getPlayerGroup() != null)
        			{
        				if(!finalArena.isInReadyRoom(player.getObjectId()))
        				{
        					finalArena.addPoints(player.getObjectId(), finalPointsReward);        					
        				}
        				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(300300000, finalArena.getArenaPoints(), player.getPlayerGroup(), new int[6], false));
        			}
        			return true;
        		}
			}, true);
    		
    		
    		if(allStagesDone)
    		{
    			onFinishGroupReward(arena, winner);
    			arena.setRewarded(true);
    		}
    		
    		//log.info("Bootcamp stage" + arena.getStage() + " round " + arena.getRound() + " by player " + winner.getName() + " completed");
    		}
    	}
    }
    
    private boolean checkOnRewardRoundStart(PlayerGroup winner, final EmpyreanCrucible arena)
    {
    	boolean started = true;
    	if(arena.getStage() == 1)
        {
            switch(arena.getRound())
            {
                case 4:
                    startStage1_5(arena);
                    break;
                    
                default:
                    started = false;
                    break;
            }
        }
    	else if(arena.getStage() == 2)
        {
            switch(arena.getRound())
            {
                case 4:
                    startStage2_5(arena);
                    break;
                    
                default:
                    started = false;
                    break;
            }
        }
    	else if(arena.getStage() == 3)
        {
            switch(arena.getRound())
            {
                case 4:
                    startStage3_5(arena);
                    break;
                    
                default:
                    started = false;
                    break;
            }
        }
    	else if(arena.getStage() == 4)
        {
            switch(arena.getRound())
            {
                case 4:
                    startStage4_4(arena);
                    break;
                    
                default:
                    started = false;
                    break;
            }
        }
    	else if(arena.getStage() == 5)
    	{
    	    switch(arena.getRound())
    	    {
    	        case 4:
    	            startStage5_5(arena);
    	            break;
    	            
    	        default:
    	            started = false;
    	            break;
    	    }
    	}
    	else if(arena.getStage() == 6)
        {
            switch(arena.getRound())
            {
                case 4:
                    startStage6_5(arena);
                    break;
                case 5:
                    startStage6_6(arena);
                    break;
                    
                default:
                    started = false;
                    break;
            }
        }
    	else if(arena.getStage() == 7)
        {
            switch(arena.getRound())
            {
                case 4:
                    startStage7_5(arena);
                    break;
                    
                default:
                    started = false;
                    break;
            }
        }
    	else if(arena.getStage() == 8)
        {
            switch(arena.getRound())
            {
                case 4:
                    startStage8_5(arena);
                    break;
                    
                default:
                    started = false;
                    break;
            }
        }
    	else if(arena.getStage() == 9)
    	{
    		switch(arena.getRound())
    		{
    		case 1:
    			startStage9_2(arena);
    			break;
    		case 2:
    			startStage9_3(arena);
    			break;
    		case 3:
    			startStage9_4(arena);
    			break;
    		case 4:
    			startStage9_5(arena);
    			break;
    			
    			default:
    				started = false;
    				break;
    		}
    	}
    	else if(arena.getStage() == 10)
    	{
    		
    		switch(arena.getRound())
    		{
    			case 2:
    				startStage10_3(arena);
    				break;
    			case 3:
    				final int pointsReward103 = 35700;
    				
    				arena.doOnAllPlayers(new Executor<Player>(){
    	        		@Override
    	        		public boolean run(Player player)
    	        		{
    	        			if(player.getPlayerGroup() == null)
    	        				return true;
    	        			arena.addPoints(player.getObjectId(), pointsReward103);
    	        			PacketSendUtility.sendPacket(player,  new SM_INSTANCE_SCORE(300300000, arena.getArenaPoints(), player.getPlayerGroup(), new int[6], false));
    	        			return true;
    	        		}
    				}, true);

    				startStage10_4(arena);    				
    				break;
    			case 4:
    				final int pointsReward104 = 44500;
    				
    				arena.doOnAllPlayers(new Executor<Player>(){
    	        		@Override
    	        		public boolean run(Player player)
    	        		{
    	        			if(player.getPlayerGroup() == null)
    	        				return true;
    	        			arena.addPoints(player.getObjectId(), pointsReward104);
    	        			PacketSendUtility.sendPacket(player,  new SM_INSTANCE_SCORE(300300000, arena.getArenaPoints(), player.getPlayerGroup(), new int[6], false));
    	        			return true;
    	        		}
    				}, true);
    				
    				startStage10_5(arena);	
    				break;
    				
    				default:
    					started = false;
    					break;
    		}
    	}
    	else
    		started = false;
    	
    	return started;
    }
    
    
    private void startStage1(EmpyreanCrucible arena) 
    {
    	if(!arena.isStageAllSpawned())
        	return;
    	
    	arena.setStageAllSpawned(false);
    	
        int spawnTime = 5000;

        // 1
        List<StageSpawn> spawn1 = new ArrayList<StageSpawn>();
        spawn1.add(new StageSpawn(217477 ,1));
        spawn1.add(new StageSpawn(217480 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn1, 1, 1, false), spawnTime);
        
        // 2
        List<StageSpawn> spawn2 = new ArrayList<StageSpawn>();
        spawnTime += 60000;
        spawn2.add(new StageSpawn(217483 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn2, 1, 2, false), spawnTime);
        
        // 3
        List<StageSpawn> spawn3 = new ArrayList<StageSpawn>();
        spawnTime += 60000;
        spawn3.add(new StageSpawn(217478 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn3, 1, 3, false), spawnTime);

        // 4        
        List<StageSpawn> spawn4 = new ArrayList<StageSpawn>();
        spawnTime += 60000;
        spawn4.add(new StageSpawn(217482 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn4, 1, 4, true), spawnTime);
    }
    
    private void startStage1_5(EmpyreanCrucible arena) 
    {
        List<StageSpawn> spawn5 = new ArrayList<StageSpawn>();
        int spawnTime = 5000;
        spawn5.add(new StageSpawn(217484 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn5, 1, 5, true), spawnTime);
    }

    private void startStage2(EmpyreanCrucible arena) 
    {
    	if(!arena.isStageAllSpawned())
        	return;
    	
    	arena.setStageAllSpawned(false);
    	
        int stageType = Rnd.get(0, 1);
        int spawnTime = 5000;

        if (stageType == 0) 
        {
            // 1
        	List<StageSpawn> spawn1 = new ArrayList<StageSpawn>();
            spawn1.add(new StageSpawn(217502 ,1));
            spawn1.add(new StageSpawn(217504 ,1));
            spawn1.add(new StageSpawn(217503 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn1, 2, 1, false), spawnTime);

            // 2
            spawnTime += 60000;
            List<StageSpawn> spawn2 = new ArrayList<StageSpawn>();
            spawn2.add(new StageSpawn(217508 ,1));
            spawn2.add(new StageSpawn(217502 ,1));
            spawn2.add(new StageSpawn(217504 ,1));
            spawn2.add(new StageSpawn(217507 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn2, 2, 2, false), spawnTime);


            // 3
            spawnTime += 60000;
            List<StageSpawn> spawn3 = new ArrayList<StageSpawn>();
            spawn3.add(new StageSpawn(217509 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn3, 2, 3, false), spawnTime);

            // 4
            spawnTime += 60000;
            List<StageSpawn> spawn4 = new ArrayList<StageSpawn>();
            spawn4.add(new StageSpawn(217482 ,1));
            spawn4.add(new StageSpawn(217505 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn4, 2, 4, false), spawnTime);

            spawnTime += 60000;
            List<StageSpawn> spawn41 = new ArrayList<StageSpawn>();
            spawn41.add(new StageSpawn(217482 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn41, 2, 4, false), spawnTime);

            spawnTime += 60000;            
            List<StageSpawn> spawn42 = new ArrayList<StageSpawn>();
            spawn42.add(new StageSpawn(217507 ,1));
            spawn42.add(new StageSpawn(217506 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn42, 2, 4, true), spawnTime);
        } 
        else 
        {        	
        	// 1
        	List<StageSpawn> spawn1 = new ArrayList<StageSpawn>();
            spawn1.add(new StageSpawn(217494 ,1));
            spawn1.add(new StageSpawn(217496 ,1));
            spawn1.add(new StageSpawn(217495 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn1, 2, 1, false), spawnTime);

            // 2
            spawnTime += 60000;
            List<StageSpawn> spawn2 = new ArrayList<StageSpawn>();
            spawn2.add(new StageSpawn(217497 ,1));
            spawn2.add(new StageSpawn(217499 ,1));
            spawn2.add(new StageSpawn(217494 ,1));
            spawn2.add(new StageSpawn(217495 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn2, 2, 2, false), spawnTime);
            

            // 3
            spawnTime += 60000;
            List<StageSpawn> spawn3 = new ArrayList<StageSpawn>();
            spawn3.add(new StageSpawn(217500 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn3, 2, 3, false), spawnTime);

            // 4
            spawnTime += 60000;
            List<StageSpawn> spawn4 = new ArrayList<StageSpawn>();
            spawn4.add(new StageSpawn(217497 ,1));
            spawn4.add(new StageSpawn(217499 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn4, 2, 4, false), spawnTime);

            spawnTime += 60000;
            List<StageSpawn> spawn41 = new ArrayList<StageSpawn>();
            spawn41.add(new StageSpawn(217498 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn41, 2, 4, false), spawnTime);

            spawnTime += 60000;            
            List<StageSpawn> spawn42 = new ArrayList<StageSpawn>();
            spawn42.add(new StageSpawn(217497 ,1));
            spawn42.add(new StageSpawn(217498 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn42, 2, 4, true), spawnTime);
        }
    }
    
    private void startStage2_5(EmpyreanCrucible arena) 
    {
        int stageType = Rnd.get(0, 1);
        int spawnTime = 5000;
        
        if(stageType == 0)
        {
            List<StageSpawn> spawn5 = new ArrayList<StageSpawn>();
            spawn5.add(new StageSpawn(217510 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn5, 2, 5, true), spawnTime);
        }
        else
        {
            List<StageSpawn> spawn5 = new ArrayList<StageSpawn>();
            spawn5.add(new StageSpawn(217501 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn5, 2, 5, true), spawnTime); 
        }
    }

    private void startStage3(EmpyreanCrucible arena) 
    {
    	if(!arena.isStageAllSpawned())
        	return;
    	
    	arena.setStageAllSpawned(false);
    	
        int spawnTime = 5000;

        // 1
        List<StageSpawn> spawn1 = new ArrayList<StageSpawn>();
        spawn1.add(new StageSpawn(217511 ,6));
        spawn1.add(new StageSpawn(217512 ,6));
        spawn1.add(new StageSpawn(217513 ,6));
        spawn1.add(new StageSpawn(217514 ,6));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn1, 3, 1, false), spawnTime);
        
        // 2
        spawnTime += 180000;        
        List<StageSpawn> spawn2 = new ArrayList<StageSpawn>();
        spawn2.add(new StageSpawn(217517 ,6));
        spawn2.add(new StageSpawn(217518 ,6));
        spawn2.add(new StageSpawn(217515 ,6));
        spawn2.add(new StageSpawn(217516 ,6));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn2, 3, 2, false), spawnTime);
        
        // 3
        spawnTime += 180000;
        
        List<StageSpawn> spawn3 = new ArrayList<StageSpawn>();
        spawn3.add(new StageSpawn(217520 ,2));
        spawn3.add(new StageSpawn(217522 ,2));
        spawn3.add(new StageSpawn(217519 ,1));
        spawn3.add(new StageSpawn(217521 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn3, 3, 3, false), spawnTime);
        
        // 4
        spawnTime += 180000;
        List<StageSpawn> spawn4 = new ArrayList<StageSpawn>();
        spawn4.add(new StageSpawn(217525 ,1));
        spawn4.add(new StageSpawn(217526 ,1));
        spawn4.add(new StageSpawn(217524 ,1));
        spawn4.add(new StageSpawn(217523 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn4, 3, 4, true), spawnTime);
    }
    
    private void startStage3_5(EmpyreanCrucible arena) 
    {
        int spawnTime = 5000;        
        List<StageSpawn> spawn5 = new ArrayList<StageSpawn>();
        spawn5.add(new StageSpawn(217527 ,1));
        spawn5.add(new StageSpawn(217528 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn5, 3, 5, true), spawnTime);
    }

    private void startStage4(EmpyreanCrucible arena) 
    {
    	if(!arena.isStageAllSpawned())
        	return;
    	
    	arena.setStageAllSpawned(false);
    	
        int spawnTime = 5000;

        // 1        
        List<StageSpawn> spawn1 = new ArrayList<StageSpawn>();
        spawn1.add(new StageSpawn(217557 ,2));
        spawn1.add(new StageSpawn(217559 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn1, 4, 1, false), spawnTime);

        spawnTime += 40000;
        List<StageSpawn> spawn11 = new ArrayList<StageSpawn>();
        spawn11.add(new StageSpawn(217558 ,2));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn11, 4, 1, false), spawnTime);
        
        // 2
        spawnTime += 60000;        
        List<StageSpawn> spawn2 = new ArrayList<StageSpawn>();
        spawn2.add(new StageSpawn(217562 ,1));
        spawn2.add(new StageSpawn(217559 ,1));
        spawn2.add(new StageSpawn(217560 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn2, 4, 2, false), spawnTime);

        spawnTime += 40000;        
        List<StageSpawn> spawn21 = new ArrayList<StageSpawn>();
        spawn21.add(new StageSpawn(217557 ,1));
        spawn21.add(new StageSpawn(217558 ,1));
        spawn21.add(new StageSpawn(217561 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn21, 4, 2, false), spawnTime);
        
        // 3
        spawnTime += 60000;        
        List<StageSpawn> spawn3 = new ArrayList<StageSpawn>();
        spawn3.add(new StageSpawn(217564 ,1));
        spawn3.add(new StageSpawn(217563 ,1));
        spawn3.add(new StageSpawn(217565 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn3, 4, 3, false), spawnTime);

        spawnTime += 40000;
        List<StageSpawn> spawn31 = new ArrayList<StageSpawn>();
        spawn31.add(new StageSpawn(217566 ,1));
        spawn31.add(new StageSpawn(217563 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn31, 4, 3, true), spawnTime);
    }
    
    private void startStage4_4(EmpyreanCrucible arena) 
    {
        int spawnTime = 5000;
        List<StageSpawn> spawn4 = new ArrayList<StageSpawn>();
        spawn4.add(new StageSpawn(217567 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn4, 4, 4, true), spawnTime);
    }
    
    private void startStage5(EmpyreanCrucible arena) 
    {
    	if(!arena.isStageAllSpawned())
        	return;
    	
    	arena.setStageAllSpawned(false);
    	
    	int stageType = Rnd.get(0, 1);
        int spawnTime = 5000;
        
        // 1
        List<StageSpawn> spawn1 = new ArrayList<StageSpawn>();
        spawn1.add(new StageSpawn(217529 ,3));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn1, 5, 1, false), spawnTime);
        
        spawnTime += 40000;
        List<StageSpawn> spawn11 = new ArrayList<StageSpawn>();
        spawn11.add(new StageSpawn(217529 ,2));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn11, 5, 1, false), spawnTime);

        if (stageType == 0) 
        {
        	// 2     
        	spawnTime += 120000;
        	List<StageSpawn> spawn2 = new ArrayList<StageSpawn>();
        	spawn2.add(new StageSpawn(217530 ,2));
        	spawn2.add(new StageSpawn(217535 ,1));
        	ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn2, 5, 2, false), spawnTime);
        	
        	// 3     
        	spawnTime += 120000;
        	List<StageSpawn> spawn3 = new ArrayList<StageSpawn>();
        	spawn3.add(new StageSpawn(217536 ,1));
        	spawn3.add(new StageSpawn(217531 ,1));
        	ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn3, 5, 3, false), spawnTime);
        	
        	// 4     
        	spawnTime += 60000;
        	List<StageSpawn> spawn4 = new ArrayList<StageSpawn>();
        	spawn4.add(new StageSpawn(217543 ,1));
        	ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn4, 5, 4, false), spawnTime);
        	
        	spawnTime += 40000;
        	List<StageSpawn> spawn41 = new ArrayList<StageSpawn>();
        	spawn41.add(new StageSpawn(217535 ,1));
        	spawn41.add(new StageSpawn(217534 ,1));
        	spawn41.add(new StageSpawn(217533 ,1));
        	ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn41, 5, 4, false), spawnTime);
        	
        	spawnTime += 40000;
        	List<StageSpawn> spawn42 = new ArrayList<StageSpawn>();
        	spawn42.add(new StageSpawn(217536 ,1));
        	spawn42.add(new StageSpawn(217532 ,1));
        	ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn42, 5, 4, true), spawnTime);
        }
        else
        {
        	// 2     
        	spawnTime += 60000;
        	List<StageSpawn> spawn2 = new ArrayList<StageSpawn>();
        	spawn2.add(new StageSpawn(217552 ,1));
        	ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn2, 5, 2, false), spawnTime);
        	
        	// 3     
        	spawnTime += 60000;
        	List<StageSpawn> spawn3 = new ArrayList<StageSpawn>();
        	spawn3.add(new StageSpawn(217553 ,1));
        	ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn3, 5, 3, false), spawnTime);
        	
        	// 4     
        	spawnTime += 60000;
        	List<StageSpawn> spawn4 = new ArrayList<StageSpawn>();
        	spawn4.add(new StageSpawn(217554 ,1));
        	ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn4, 5, 4, true), spawnTime);
        }
    }
    
    private void startStage5_5(EmpyreanCrucible arena) 
    {
        int stageType = Rnd.get(0, 1);
        
        int spawnTime = 5000;
        if(stageType == 0)
        {
            List<StageSpawn> spawn5 = new ArrayList<StageSpawn>();
            spawn5.add(new StageSpawn(217555 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn5, 5, 5, true), spawnTime);
        }
        else
        {
            List<StageSpawn> spawn5 = new ArrayList<StageSpawn>();
            spawn5.add(new StageSpawn(217544 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn5, 5, 5, true), spawnTime);
        }
    }
    
    
    private void startStage6(EmpyreanCrucible arena) 
    {
    	if(!arena.isStageAllSpawned())
        	return;
    	
    	arena.setStageAllSpawned(false);
    	
        int spawnTime = 10000;

        // 1        
        List<StageSpawn> spawn1 = new ArrayList<StageSpawn>();
        spawn1.add(new StageSpawn(217568 ,2));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn1, 6, 1, false), spawnTime);

        spawnTime += 40000;
        List<StageSpawn> spawn11 = new ArrayList<StageSpawn>();
        spawn11.add(new StageSpawn(217568 ,2));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn11, 6, 1, false), spawnTime);
        
        // 2
        spawnTime += 60000;        
        List<StageSpawn> spawn2 = new ArrayList<StageSpawn>();
        spawn2.add(new StageSpawn(217570 ,1));
        spawn2.add(new StageSpawn(217569 ,4));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn2, 6, 2, false), spawnTime);
        
        // 3
        spawnTime += 120000;        
        List<StageSpawn> spawn3 = new ArrayList<StageSpawn>();
        spawn3.add(new StageSpawn(217569 ,2));
        spawn3.add(new StageSpawn(217568 ,1));
        spawn3.add(new StageSpawn(217575 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn3, 6, 3, false), spawnTime);
        
        // 4
        spawnTime += 60000;
        List<StageSpawn> spawn4 = new ArrayList<StageSpawn>();
        spawn4.add(new StageSpawn(217573 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn4, 6, 4, true), spawnTime);
    }
    
    private void startStage6_5(EmpyreanCrucible arena) 
    {
        int spawnTime = 5000;
        
        List<StageSpawn> spawn5 = new ArrayList<StageSpawn>();
        spawn5.add(new StageSpawn(217569 ,4));
        spawn5.add(new StageSpawn(217568 ,1));
        spawn5.add(new StageSpawn(217573 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn5, 6, 5, true), spawnTime);
    }
    
    private void startStage6_6(EmpyreanCrucible arena) 
    {
        int spawnTime = 5000;
        List<StageSpawn> spawn6 = new ArrayList<StageSpawn>();
        spawn6.add(new StageSpawn(217750 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn6, 6, 6, true), spawnTime);
    }
    
    private void startStage7(EmpyreanCrucible arena) 
    {
    	
    	if(!arena.isStageAllSpawned())
        	return;
    	
    	arena.setStageAllSpawned(false);
    	
        int spawnTime = 10000;

        // 1        
        List<StageSpawn> spawn1 = new ArrayList<StageSpawn>();
        spawn1.add(new StageSpawn(217578 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn1, 7, 1, false), spawnTime);
        
        // 2
        spawnTime += 120000;        
        List<StageSpawn> spawn2 = new ArrayList<StageSpawn>();
        spawn2.add(new StageSpawn(217579 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn2, 7, 2, false), spawnTime);
        
        // 3
        spawnTime += 120000;        
        List<StageSpawn> spawn3 = new ArrayList<StageSpawn>();
        spawn3.add(new StageSpawn(217580 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn3, 7, 3, false), spawnTime);
        
        // 4
        spawnTime += 120000;
        List<StageSpawn> spawn4 = new ArrayList<StageSpawn>();
        spawn4.add(new StageSpawn(217581 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn4, 7, 4, true), spawnTime);
            
    }
    
    private void startStage7_5(EmpyreanCrucible arena) 
    {
        int spawnTime = 5000;
        List<StageSpawn> spawn5 = new ArrayList<StageSpawn>();
        spawn5.add(new StageSpawn(217586 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn5, 7, 5, true), spawnTime);    
    }
    
    private void startStage8(EmpyreanCrucible arena)
    {
    	if(!arena.isStageAllSpawned())
        	return;
    	
    	arena.setStageAllSpawned(false);
    	
    	
        int spawnTime = 10000;

        int stageType = Rnd.get(0, 1);
        
        if(stageType == 0)
        {
        	// 1        
            List<StageSpawn> spawn1 = new ArrayList<StageSpawn>();
            spawn1.add(new StageSpawn(217588 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn1, 8, 1, false), spawnTime);
        }
        else
        {
        	// 1        
            List<StageSpawn> spawn1 = new ArrayList<StageSpawn>();
            spawn1.add(new StageSpawn(217589 ,1));
            ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn1, 8, 1, false), spawnTime);
        }
        
        
        // 2
        spawnTime += 120000;        
        List<StageSpawn> spawn2 = new ArrayList<StageSpawn>();
        spawn2.add(new StageSpawn(217590 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn2, 8, 2, false), spawnTime);
        
        // 3
        spawnTime += 180000;        
        List<StageSpawn> spawn3 = new ArrayList<StageSpawn>();
        spawn3.add(new StageSpawn(217591 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn3, 8, 3, false), spawnTime);
        
        // 4
        spawnTime += 180000;
        List<StageSpawn> spawn4 = new ArrayList<StageSpawn>();
        spawn4.add(new StageSpawn(217592 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn4, 8, 4, true), spawnTime);
    }
    
    private void startStage8_5(EmpyreanCrucible arena) 
    {
        int spawnTime = 5000;
        List<StageSpawn> spawn5 = new ArrayList<StageSpawn>();
        spawn5.add(new StageSpawn(217593 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn5, 8, 5, true), spawnTime);    
    }
    
    private void startStage9_1(EmpyreanCrucible arena)
    {
    	int spawnTime = 10000;
    	
    	List<StageSpawn> spawn1 = new ArrayList<StageSpawn>();
        spawn1.add(new StageSpawn(217594 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn1, 9, 1, true), spawnTime);
    }
    
    private void startStage9_2(EmpyreanCrucible arena)
    {
    	int spawnTime = 10000;
    	
    	List<StageSpawn> spawn2 = new ArrayList<StageSpawn>();
        spawn2.add(new StageSpawn(217595 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn2, 9, 2, true), spawnTime);
    }
    
    private void startStage9_3(EmpyreanCrucible arena)
    {
    	int spawnTime = 10000;
    	
    	List<StageSpawn> spawn3 = new ArrayList<StageSpawn>();
        spawn3.add(new StageSpawn(217596 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn3, 9, 3, true), spawnTime);
    }
    
    private void startStage9_4(EmpyreanCrucible arena)
    {
    	int spawnTime = 10000;
    	
    	List<StageSpawn> spawn4 = new ArrayList<StageSpawn>();
        spawn4.add(new StageSpawn(217598 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn4, 9, 4, true), spawnTime);
    }
    
    private void startStage9_5(EmpyreanCrucible arena)
    {
    	int spawnTime = 10000;
    	
    	List<StageSpawn> spawn5 = new ArrayList<StageSpawn>();
        spawn5.add(new StageSpawn(217599 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn5, 9, 5, true), spawnTime);  
    }
    
    private void startStage10(EmpyreanCrucible arena) 
    {
    	if(!arena.isStageAllSpawned())
        	return;
    	
    	arena.setStageAllSpawned(false);
    	
        int spawnTime = 10000;
        
        // 1        
        List<StageSpawn> spawn1 = new ArrayList<StageSpawn>();
        spawn1.add(new StageSpawn(217602 ,1));
        spawn1.add(new StageSpawn(217600 ,1));
        spawn1.add(new StageSpawn(217601 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn1, 10, 1, false), spawnTime);        
        
        spawnTime += 40000;
        List<StageSpawn> spawn11 = new ArrayList<StageSpawn>();
        spawn11.add(new StageSpawn(217602 ,1));
        spawn11.add(new StageSpawn(217600 ,1));
        spawn11.add(new StageSpawn(217601 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn11, 10, 1, false), spawnTime);      
        
        // 2
        spawnTime += 120000;        
        List<StageSpawn> spawn2 = new ArrayList<StageSpawn>();
        spawn2.add(new StageSpawn(217604 ,1));
        spawn2.add(new StageSpawn(217605 ,1));
        spawn2.add(new StageSpawn(217606 ,1));        
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn2, 10, 2, false), spawnTime);
        
        spawnTime += 40000;        
        List<StageSpawn> spawn21 = new ArrayList<StageSpawn>();
        spawn21.add(new StageSpawn(217603 ,2));
        spawn21.add(new StageSpawn(217603 ,1));        
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn21, 10, 2, false), spawnTime);
        
        spawnTime += 40000;        
        List<StageSpawn> spawn22 = new ArrayList<StageSpawn>();
        spawn22.add(new StageSpawn(217603 ,1));        
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn22, 10, 2, true), spawnTime);               
    }
    
    private void startStage10_3(EmpyreanCrucible arena)
    {
    	int spawnTime = 10000;
    	
        List<StageSpawn> spawn3 = new ArrayList<StageSpawn>();
        spawn3.add(new StageSpawn(217607 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn3, 10, 3, true), spawnTime);
    }
    private void startStage10_4(EmpyreanCrucible arena) 
    {
    	int spawnTime = 10000;

        List<StageSpawn> spawn4 = new ArrayList<StageSpawn>();
        spawn4.add(new StageSpawn(217608 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn4, 10, 4, true), spawnTime);
    }
    private void startStage10_5(EmpyreanCrucible arena) 
    {
    	int spawnTime = 10000;
    	
    	List<StageSpawn> spawn5 = new ArrayList<StageSpawn>();
        spawn5.add(new StageSpawn(217609 ,1));
        ThreadPoolManager.getInstance().schedule(new stageSpawnTask(arena.getInstanceId(), spawn5, 10, 5, true), spawnTime); 
    }
    
    public SpawnLocation getSpawnLocation( int stage )
    {
    	stage -= 1;
    	
    	if(stage < 0)
    		stage = 0;
    	
    	if(spawnCoords[stage] != null)
    		return spawnCoords[stage];
    	else
    		return new SpawnLocation(330f, 360f, 330f, 360f, 96f);
    }
    
    public SpawnLocation getReadyRoomLocation( int stage )
    {
    	switch(stage)
		{
			case 1:
			case 2:
			case 3:
			case 4:
				return readyRooms[0];

			case 5:
			case 6:
				return readyRooms[1];
			case 7:
				return readyRooms[2];
			case 8:
				return readyRooms[3];
			case 9:
				return readyRooms[4];
			case 10:
				return readyRooms[5];
				
				default:
					return readyRooms[0];
		}
    }
    
    private void TeleportArenaGroupToStage(final EmpyreanCrucible arena, final int stage)
    {	    	
    	arena.doOnAllPlayers(new Executor<Player>(){
    		@Override
    		public boolean run(Player player)
    		{
    			if(arena.isInReadyRoom(player.getObjectId()))
    			{
    				SpawnLocation spawnLocation = getReadyRoomLocation(stage);
    				TeleportService.teleportTo(player, 300300000, spawnLocation.getCenterCoordX(), spawnLocation.getCenterCoordY(), spawnLocation.getZ(), 0);
    			}
    			else
    			{
    				SpawnLocation spawnLocation = getSpawnLocation(stage);
    				TeleportService.teleportTo(player, 300300000, spawnLocation.getCenterCoordX(), spawnLocation.getCenterCoordY(), spawnLocation.getZ(), 0);
    			}
    			return true;
    		}
		}, true);
    }
    
    
    private boolean checkAcademyCooldown(Player player, int worldId, int instanceId)
	{
		int instanceMapId = DataManager.WORLD_MAPS_DATA.getTemplate(worldId).getInstanceMapId();
		int mapname = DataManager.WORLD_MAPS_DATA.getTemplate(worldId).getMapNameId();
		
		if(!InstanceService.canEnterInstance(player, instanceMapId, instanceId) && CustomConfig.INSTANCE_COOLDOWN && player.getWorldId() != worldId)
		{
			int timeinMinutes = InstanceService.getTimeInfo(player).get(instanceMapId)/60;
			if (timeinMinutes >= 60 )
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_HOUR_CLIENT(mapname, timeinMinutes/60));
			else
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_MIN_CLIENT(mapname, timeinMinutes));
			
			return true;
		}
		return false;
	}
    
    private void setAcademyCooldown(Player player, int worldId, int instanceId)
	{
		int instanceMapId = DataManager.WORLD_MAPS_DATA.getTemplate(worldId).getInstanceMapId();
		
		if(player.getInstanceCD(instanceMapId) == null)
		{
			int cd = DataManager.WORLD_MAPS_DATA.getTemplate(worldId).getCooldown();
			Timestamp CDEndTime = new Timestamp(Calendar.getInstance().getTimeInMillis() + cd*60000);
			
			if(player.isInGroup())
				player.addInstanceCD(instanceMapId, CDEndTime, instanceId, player.getPlayerGroup().getGroupId());
			else
				player.addInstanceCD(instanceMapId, CDEndTime, instanceId, 0);
		}
	}
    
    
    public void TeleportPlayerToCurrentStage(Player requester) {
    	if(!requester.isInGroup())
		{
		PacketSendUtility.sendMessage(requester, "You must be in a group to enter.");
    	return;
	}
    	
    	WorldMapInstance inst = InstanceService.getRegisteredInstance(300300000, requester.getPlayerGroup().getGroupId());
    	if(inst != null)
    	{
    		EmpyreanCrucible arena = (EmpyreanCrucible)inst;
    		int stage = arena.getStage();
    		SpawnLocation spawnLocation = getSpawnLocation(stage);
    		
    		if(requester.getWorldId() == 300300000)
    		{
    			// Player re-enter from ready room if have resurrection ticket 
    			long ticketCount = requester.getInventory().getItemCountByItemId(186000124);
    			if(ticketCount > 0)
    			{
    				requester.getInventory().removeFromBagByItemId(186000124, 1);
    				arena.removeFromReadyRoom(requester.getObjectId());
    				TeleportService.teleportTo(requester, 300300000, inst.getInstanceId(), spawnLocation.getCenterCoordX(), spawnLocation.getCenterCoordY(), spawnLocation.getZ(), 0);
    				onResume(requester, arena);
    			}
    			else
    			{
    				// No tickets left
        			PacketSendUtility.sendMessage(requester, "You dont have enought resurrection tickets.");
    			}
    		}
    		else if(stage == 0 && requester.getWorldId() != 300300000)
    		{
    			if(checkAcademyCooldown(requester, 300300000, inst.getInstanceId()))
        			return;
    			
    			// Group member enter
    			setAcademyCooldown(requester, 300300000, inst.getInstanceId()); 
    			
    			long ticketCount = requester.getInventory().getItemCountByItemId(186000124);
    			if(ticketCount > 0)
    				requester.getInventory().removeFromBagByItemId(186000124, ticketCount);
    			
    			TeleportService.teleportTo(requester, 300300000, inst.getInstanceId(), spawnLocation.getCenterCoordX(), spawnLocation.getCenterCoordY(), spawnLocation.getZ(), 0);
    		}
    		else if(stage > 0)
    		{
    			// No enter when arena has been started
    			PacketSendUtility.sendMessage(requester, "Your group has already started training");
    		}
    	}
    	else
    	{
    		if(checkAcademyCooldown(requester, 300300000, 0))
    			return;
    		
    		//creating new instance and teleport. The door is not a portal anymore so it will not call PortalController.java.
    		WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300300000);
    		InstanceService.registerGroupWithInstance(newInstance, requester.getPlayerGroup());
        	SpawnLocation spawnLocation = getSpawnLocation(((EmpyreanCrucible)newInstance).getStage());
        	
        	long ticketCount = requester.getInventory().getItemCountByItemId(186000124);
			if(ticketCount > 0)
				requester.getInventory().removeFromBagByItemId(186000124, ticketCount);
        	
        	TeleportService.teleportTo(requester, 300300000, newInstance.getInstanceId(), spawnLocation.getCenterCoordX(), spawnLocation.getCenterCoordY(), spawnLocation.getZ(), 0);
        	
        	setAcademyCooldown(requester, 300300000, newInstance.getInstanceId());
    	}
    }

    public void onResume(final Player resumer, EmpyreanCrucible arena)
    {
    	arena.doOnAllPlayers(new Executor<Player>(){
    		@Override
    		public boolean run(Player player)
    		{
    			if(player != resumer)
    				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_FRIENDLY_MOVE_COMBATAREA_IDARENA(resumer.getName()));
    			return true;
    		}
    	}, true);
    }
    
    
    public void TeleportToReadyRoom(Player player)
    {
    	WorldMapInstance inst = InstanceService.getRegisteredInstance(300300000, player.getPlayerGroup().getGroupId());
    	if(inst != null)
    	{
    		EmpyreanCrucible arena = (EmpyreanCrucible)inst;
    		
    		int stage = arena.getStage();
    		arena.addToReadyRoom(player.getObjectId());
    		
    		SpawnLocation spawnLocation = getReadyRoomLocation(stage);    		
    		TeleportService.teleportTo(player, 300300000, inst.getInstanceId(), spawnLocation.getCenterCoordX(), spawnLocation.getCenterCoordY(), spawnLocation.getZ(), 0);
    	}
    	else
    		TeleportService.moveToBindLocation(player, true, 0);
    }
    
    public void onFinishGroupReward(EmpyreanCrucible arena, PlayerGroup group)
    {
    	int[] playersInsigmas = new int[6];
    	int i = 0;
    	
    	FastMap<Integer, Integer> arenaPoints = arena.getArenaPoints();
    	
    	for(Player member : group.getMembers())
    	{
    		if(member == null)
    			continue;
    		
    		long ticketCount = member.getInventory().getItemCountByItemId(186000124);
			if(ticketCount > 0)
				member.getInventory().removeFromBagByItemId(186000124, ticketCount);
    		
    		int insigniaCount = 0;
    		int playerPoints = arenaPoints.get(member.getObjectId()) == null ? 0 : arenaPoints.get(member.getObjectId());
    	
    		if(playerPoints >= 498200) //10 done
    			insigniaCount = 5332;
    		else if(playerPoints >= 278900) // 10_4 done
    			insigniaCount = 4688;
    		else if(playerPoints >= 234400) //10_3 done
    			insigniaCount = 2550;
    		
    		else if (playerPoints >= 198700) // 9 done
    			insigniaCount = 2330;
    		else if (playerPoints >= 141400) //8 done
    			insigniaCount = 1450;
    		else if (playerPoints >= 105200) //7 done
    			insigniaCount = 1350;
    		else if (playerPoints >= 80500) //6 done
    			insigniaCount = 999;
    		else if (playerPoints >= 20900) //2 done
    			insigniaCount = 500;
    		else if (playerPoints >= 9700) //1 done
    			insigniaCount = 350;
    		
    		insigniaCount *= member.getRates().getInsigniaRate();

    		playersInsigmas[i] = insigniaCount;
    		i++;
    		
    		ItemService.addItem(member, 186000130, insigniaCount);
    	}

    	for(Player member : group.getMembers())
    	{
    		PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(300300000, arena.getArenaPoints(), member.getPlayerGroup(), playersInsigmas, true));		
    	}
    }

    private static class stageSpawnTask implements Runnable 
    {    
    	private int instanceId;
        private List<StageSpawn> spawns;  
        private int stage;
        private int round;
        private boolean finalSpawn;

        public stageSpawnTask(int instanceId, List<StageSpawn> spawns, int stage, int round, boolean finalSpawn) 
        {
        	this.instanceId = instanceId;
            this.spawns = spawns;
            this.stage = stage;
            this.round = round;
            this.finalSpawn = finalSpawn;
        }

        @Override
        public void run() 
        {
        	EmpyreanCrucible arena = (EmpyreanCrucible)World.getInstance().getWorldMap(300300000).getWorldMapInstanceById(instanceId);
        	
            if(arena == null)
            	return;
            
        	arena.setStageRound(stage, round);
            
        	arena.doOnAllPlayers(new Executor<Player>(){
        		@Override
        		public boolean run(Player player)
        		{
        			PacketSendUtility.sendPacket(player, new SM_ACADEMY_BOOTCAMP_STAGE(stage, round, false));
        			return true;
        		}
			}, true);
        	
        	SpawnLocation spawnLocation = AcademyBootcampService.getInstance().getSpawnLocation(stage);
        	//log.info("Spawning Bootcamp stage " + stage + " round " + round + " for group " + group.getGroupId() + " at LOC:" + spawnLocation.getMinCoordX().intValue() + " " + spawnLocation.getMinCoordY().intValue());
            for (StageSpawn stageSpawn : spawns)
            {
                for (int i = 0; i < stageSpawn.getCount(); i++) 
                {
                    float x = Rnd.get(spawnLocation.getMinCoordX().intValue(), spawnLocation.getMaxCoordX().intValue());
                    float y = Rnd.get(spawnLocation.getMinCoordY().intValue(), spawnLocation.getMaxCoordY().intValue());
                    float z = spawnLocation.getZ();
                    byte h = (byte)Rnd.get(0, 120);
                    //log.info("Spawning " + stageSpawn.getObjId() + " " + x + " " + y + " " + z + " instance " + group.getGroupLeader().getInstanceId());
                    SpawnTemplate spawn;
                    spawn = SpawnEngine.getInstance().addNewSpawn(300300000, arena.getInstanceId(), stageSpawn.getObjId(), x, y, z, h, 0, 0, true);
                    SpawnEngine.getInstance().spawnObject(spawn, arena.getInstanceId());
                }
            }
            
            for(StageSpawn spawn : spawns)
            {
            	arena.addSpawnedCount(spawn.getCount());
            }
            
            spawns.clear();
            spawns = null;
            
            if(finalSpawn)
            	arena.setStageAllSpawned(true);
        }
    }
    
    private class StageSpawn
    {
        private int objId = 0;
        private int count = 0;
        
        public StageSpawn(int objId, int count)
        {
            this.objId = objId;
            this.count = count;
        }
        
        public int getObjId()
        {
            return objId;
        }
        
        public int getCount()
        {
            return count;
        }
    }
    
    private class SpawnLocation
    {
    	private Float minCoordX;
    	private Float maxCoordX;
    	private Float minCoordY;
    	private Float maxCoordY;
    	private Float z;
    	
    	public SpawnLocation(float minX, float maxX, float minY, float maxY, float z)
    	{
    		this.minCoordX = minX;
    		this.maxCoordX = maxX;
    		this.minCoordY = minY;
    		this.maxCoordY = maxY;
    		this.z = z;
    	}
    	
    	public Float getMinCoordX()
    	{
    		return minCoordX;
    	}
    	
    	public Float getMaxCoordX()
    	{
    		return maxCoordX;
    	}
    	
    	public Float getMinCoordY()
    	{
    		return minCoordY;
    	}
    	
    	public Float getMaxCoordY()
    	{
    		return maxCoordY;
    	}
    	
    	public Float getCenterCoordX()
    	{
    		return (minCoordX + maxCoordX) / 2f;
    	}
    	
    	public Float getCenterCoordY()
    	{
    		return (minCoordY + maxCoordY) / 2f;
    	}
    	
    	public Float getZ()
    	{
    		return z;
    	}
    }
    
    public static boolean isAcademyBootcamp(int mapId)
    {
    	return mapId == 300300000;
    }

    public static AcademyBootcampService getInstance() 
    {
        return SingletonHolder.instance;
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder 
    {
        protected static final AcademyBootcampService instance = new AcademyBootcampService();
    }
}
