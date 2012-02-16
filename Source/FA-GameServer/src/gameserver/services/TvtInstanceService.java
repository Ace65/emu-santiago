/**
 * This file is part of Aion Extreme <aion-core.net>
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.services;

import gameserver.controllers.instances.TvtController;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.Executor;
import gameserver.model.group.PlayerGroup;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

/*
 *
 * @author Khaos
 * DiamondCore Studio (http://www.diamondcore-mmorpgs.com)
 * Script exclusive for Aion Extreme
 *
*/

public class TvtInstanceService {
    private static boolean isStarted = false;
    private static boolean autorun = false;

    Set<Player> regPlayers = new HashSet<Player>();
    Set<PlayerGroup> regGroups = new HashSet<PlayerGroup>();
    Timer timer;
    int remainingTime;

    public TvtInstanceService() {
        remainingTime = 10;
        timer = initTimer();
    }

    public int getPlayersSize() {
        return regPlayers.size();
    }

    public boolean loadTvt() {
        if (isStarted)
            return false;
        isStarted = true;
        if (remainingTime <= 0)
            remainingTime = 10;

        World.getInstance().doOnAllPlayers(new Executor<Player>() {
            @Override
            public boolean run(Player p) {
                if (p.getCommonData().getLevel() > 35)
                    PacketSendUtility.sendSysMessage(p, "Team vs Team event, to use the command register .tvt register and wait. Minimum required level +35 to 6 players per team. Time to start the event: " + remainingTime + " minutes.");
                return true;
            }
        });
        timer.start();
        return true;
    }

    public void setAuto(boolean b) {
        autorun = b;
        if (b)
            this.loadTvt();
    }

    public void setTimer(int time) {
        remainingTime = time;
    }

    public int getTimer() {
        if (isStarted)
            return remainingTime;
        else
            return -1;
    }

    private Timer initTimer() {
        ActionListener action = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                remainingTime--;
                TvtInstanceService.getInstance().sendRemainTime();
                if (remainingTime == 0) {
                    TvtInstanceService.getInstance().onTimerEnd();
                    timer.stop();
                }
            }

        };

        return new Timer(60 * 1000, action);
    }

    public void sendMsg(Player p, String message) {
        PacketSendUtility.sendMessage(p, message);
    }

    public void registerPlayer(Player player) {
        if (isStarted && player.getCommonData().getLevel() > 35) {
            if (player.isInGroup()) {
                if (player.getPlayerGroup().getGroupLeader() == player && player.getPlayerGroup().size() == 6) {
                    if (regGroups.add(player.getPlayerGroup())) {
                        for (Player p : player.getPlayerGroup().getMembers())
                            this.sendMsg(p, "You are now registered at the next TvT.");
                    } else
                        this.sendMsg(player, "The group was recorded at the vent TvT.");
                } else
                    this.sendMsg(player, "You are not allowed to register your group.");

            } else {
                if (regPlayers.add(player))
                    this.sendMsg(player, "You are now registered for the next event TvT.");
                else
                    this.sendMsg(player, "You have already registered for the event TvT.");
            }
        } else
            this.sendMsg(player, "Event TvT not available.");
    }

    public void unregisterPlayer(Player player) {
        if (regPlayers.contains(player)) {
            if (regPlayers.remove(player))
                this.sendMsg(player, "Succesfuly registration!.");
            else
                this.sendMsg(player, "Unknow error. Please contact administrator.");
        } else
            this.sendMsg(player, "You are not logged in the event of TvT");
    }

    public synchronized void makeOneTvt() {
        TvtController tvt = new TvtController();
        Set<Player> toRegister = getOneGroup();
        for (Player player : toRegister) {
            tvt.registerPlayer(player);
        }
        tvt.start();
        this.scheduleStop(tvt);

    }

    private void scheduleStop(final TvtController tvt) {
        ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                tvt.stop();
            }

        }, 20 * 60 * 1000);
    }

    private synchronized Set<Player> getOneGroup() {
        Set<Player> players = new HashSet<Player>();
        if (regGroups.isEmpty()) {
            for (Player p : regPlayers) {
                int asmos = 0;
                int elyos = 0;
                if (p.getCommonData().getRace() == Race.ASMODIANS && asmos < 6) {
                    asmos++;
                    players.add(p);
                } else if (p.getCommonData().getRace() == Race.ELYOS && elyos < 6) {
                    elyos++;
                    players.add(p);
                }
            }

            for (Player p : players) {
                regPlayers.remove(p);
            }
        } else {
            boolean asmos = false;
            boolean elyos = false;
            Set<PlayerGroup> toRemove = new HashSet<PlayerGroup>();
            for (PlayerGroup p : regGroups) {
                if (p.getGroupLeader().getCommonData().getRace() == Race.ASMODIANS && !asmos) {
                    asmos = true;
                    for (Player player : p.getMembers())
                        players.add(player);
                    toRemove.add(p);
                } else if (p.getGroupLeader().getCommonData().getRace() == Race.ELYOS && !elyos) {
                    elyos = true;
                    for (Player player : p.getMembers())
                        players.add(player);
                    toRemove.add(p);
                }
            }
            for (PlayerGroup pg : toRemove) {
                regGroups.remove(pg);
            }
        }
        return players;
    }

    public void sendRemainTime() {
        if (remainingTime == 7 || remainingTime == 5 || remainingTime == 3 || remainingTime == 2 || remainingTime == 1) {
            World.getInstance().doOnAllPlayers(new Executor<Player>() {
                @Override
                public boolean run(Player pl) {
                    PacketSendUtility.sendSysMessage(pl, "TvT begin in : " + remainingTime + " minutes/s. Sign up with the command .tvt register if you have lvl +35 and no group.");
                    return true;
                }
            });
        }
    }

    public void onTimerEnd() {
        isStarted = false;
        while (!regPlayers.isEmpty()) {
            this.makeOneTvt();
        }
        if (autorun) {
            remainingTime = 120;
            isStarted = true;
            timer.start();
        }
    }

    public void reset() {
        isStarted = false;
        timer.stop();
        regPlayers.clear();
        regGroups.clear();
        remainingTime = 10;
        autorun = false;
    }

    public static TvtInstanceService getInstance() {
        return SingletonHolder.dr;
    }

    private static class SingletonHolder {
        public static TvtInstanceService dr = new TvtInstanceService();
    }
}
