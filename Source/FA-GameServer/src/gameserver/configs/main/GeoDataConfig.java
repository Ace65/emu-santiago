/*
 * This file is part of Aion-Europe <aion-core.net>.
 *
 * Aion-Europe Emulator is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Europe Emulator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * @author BlueCryme
 *
 */

package gameserver.configs.main;

import commons.configuration.Property;

public class GeoDataConfig {
    @Property(key = "gameserver.geodata.enable", defaultValue = "false")
    public static boolean GEO_ENABLE;
}
