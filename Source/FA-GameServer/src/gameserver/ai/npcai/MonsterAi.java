/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.ai.npcai;

import gameserver.ai.events.EventHandlers;
import gameserver.ai.state.StateHandlers;

/**
 * @author ATracer
 *
 */
 public class MonsterAi extends NpcAi
 {
   public MonsterAi()
   {
     addEventHandler(EventHandlers.ATTACKED_EH.getHandler());
     addEventHandler(EventHandlers.TIREDATTACKING_EH.getHandler());
     addEventHandler(EventHandlers.MOST_HATED_CHANGED_EH.getHandler());
     addEventHandler(EventHandlers.BACKHOME_EH.getHandler());
     addEventHandler(EventHandlers.RESTOREDHEALTH_EH.getHandler());
 
     addStateHandler(StateHandlers.MOVINGTOHOME_SH.getHandler());
     addStateHandler(StateHandlers.NONE_MONSTER_SH.getHandler());
     addStateHandler(StateHandlers.ATTACKING_SH.getHandler());
     addStateHandler(StateHandlers.THINKING_SH.getHandler());
     addStateHandler(StateHandlers.RESTING_SH.getHandler());
   }
 }

