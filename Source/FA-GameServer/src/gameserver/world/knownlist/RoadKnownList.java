 /*
 * Copyright (c) 2011 Aion Extreme
 * 
 * This file is part of Aion Extreme <http://www.aion-core.net>.
 * 
 * Aion Extreme <http://www.aion-core.net> is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, version 3 of the License.
 * 
 * Aion Extreme <http://www.aion-core.net> is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with  Aion Extreme
 *  <http://www.aion-core.net>. If not, see <http://www.gnu.org/licenses/>.
 */

 /*
 * author^
 * Fr0st;
 * Mr.Chayka.
 * Implementation GosthMan
*/
 
package gameserver.world.knownlist;
     

import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.road.Road;
import gameserver.utils.MathUtil;
     
public class RoadKnownList extends KnownList {
	public RoadKnownList(VisibleObject owner) {
		super(owner);
	}
     
	@Override
	protected boolean checkObjectInRange(VisibleObject owner, VisibleObject newObject) {
		Road r = (Road)owner;
		if (MathUtil.isIn3dRange(owner, newObject, r.getTemplate().getRadius() * 2)) {
			return true;
		}
		return false;
	}
}
