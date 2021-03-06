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
package gameserver.configs.main;

import commons.configuration.Property;

public class EnchantsConfig
{
	/**
	 * ManaStone Rates
	 */
	 @Property(key = "gameserver.manastone.percent.slot1", defaultValue = "98")
	 public static int MS_SLOT1;
	 @Property(key = "gameserver.manastone.percent.slot2", defaultValue = "85")
	 public static int MS_SLOT2;
	 @Property(key = "gameserver.manastone.percent.slot3", defaultValue = "75")
	 public static int MS_SLOT3;
	 @Property(key = "gameserver.manastone.percent.slot4", defaultValue = "65")
	 public static int MS_SLOT4;
	 @Property(key = "gameserver.manastone.percent.slot5", defaultValue = "55")
	 public static int MS_SLOT5;
	 @Property(key = "gameserver.manastone.percent.slot6", defaultValue = "45")
	 public static int MS_SLOT6;
	 @Property(key = "gameserver.manastone.percent.slot7", defaultValue = "35")
	 public static int MS_SLOT7;
 
	
	/**
	 * Supplement Additional Success Rates
	 */
	@Property(key = "gameserver.supplement.lesser", defaultValue = "10")
	public static int		LSSUP;
	@Property(key = "gameserver.supplement.regular", defaultValue = "15")
	public static int		RGSUP;
	@Property(key = "gameserver.supplement.greater", defaultValue = "20")
	public static int		GRSUP;

	/**
	 * Success Socketing Rates by Khaos
	 */
	@Property(key = "gameserver.success.socketing.fabled", defaultValue = "100")
	public static int		FELSOCFAB;

	@Property(key = "gameserver.success.socketing.eternal", defaultValue = "100")
	public static int		FELSOCETE;
}
