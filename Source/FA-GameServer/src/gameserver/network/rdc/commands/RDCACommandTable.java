/**
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.network.rdc.commands;

import gameserver.network.rdc.commands.impl.AbyssStatus;
import javolution.util.FastMap;

/**
 * @author Sylar
 *
 */
public class RDCACommandTable
{
	private static FastMap<String, RDCCommand> commands = new FastMap<String, RDCCommand>();
	
	public static void initialize()
	{
		commands.put("AbyssStatus", new AbyssStatus());
	}
	
	public static RDCCommand getCommandByName(String name)
	{
		return commands.get(name);
	}
	
}