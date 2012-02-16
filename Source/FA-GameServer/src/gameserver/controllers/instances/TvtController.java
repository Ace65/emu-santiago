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

package gameserver.controllers.instances;

import gameserver.model.gameobjects.player.Player;
import gameserver.model.instances.Tvt;
import gameserver.services.InstanceService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.WorldMapInstance;
import org.apache.log4j.Logger;

/*
 *
 * @author Khaos
 * DiamondCore Studio (http://www.diamondcore-mmorpgs.com)
 * Script exclusive for Aion Extreme
 *
*/

public class TvtController {

    private static final Logger log = Logger.getLogger(TvtController.class);
    private WorldMapInstance newinstance;
    private Tvt tvt = new Tvt();
    private boolean isStopped = false;

    public TvtController() {
        newinstance = InstanceService.getNextAvailableInstance(320090000);
    }

    public synchronized void registerPlayer(Player player) {
        if (tvt.isAlreadyRegister(player)) {
            log.warn("Exception in TvtInstanceService : Player shouldn't be registered twice");
            return;
        }
        if (tvt.addPlayer(player)) {
            InstanceService.registerPlayerWithInstance(newinstance, player);
        } else {
            log.warn("No player can be registered : " + player.getName());
            return;
        }
    }

    public void teleportSpec(Player p) {
        TeleportService.teleportTo(p, 320090000, newinstance.getInstanceId(), 276, 183, 162, 3000);
    }

    public void stop() {
        if (isStopped)
            return;
        isStopped = true;
        tvt.teleportOut();
        log.info("TvT is stopped");
    }

    public void start() {
        if (tvt.canStart()) {
            log.info("New event started TvT");
            tvt.teleportIn(newinstance, this);
            tvt.sendBeginMessage();
        } else {
            log.info("Not enough players to start the event TvT. Aborting.");
            tvt.sendSorryMessage();
        }
    }

    public void onDieEvent(Player p) {
    }

    public void updateScore(Player p, int value) {
        tvt.addScore(p, value);
    }

    public void onKillEvent(Player p) {
        tvt.setWinnerRace(p.getCommonData().getRace());
        this.stop();
    }

    public void onLeaveEvent(Player p) {
        PacketSendUtility.sendMessage(p, "Leaving TvT... not receive reward.");
        tvt.removePlayer(p);
    }
}
