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

package gameserver.model.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ambrosius
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BattleGroundRules")
public class BattleGroundRules
{
	@XmlAttribute(name = "kill_player", required = true)
	private int		killPlayer;
	
	@XmlAttribute(name = "die", required = true)
	private int		die;
	
	@XmlAttribute(name = "flag_cap", required = false)
    private int     flagCap;
	
	@XmlAttribute(name = "flag_base", required = false)
    private int     flagBase;
	
	@XmlAttribute(name = "flag_ground", required = false)
    private int     flagGround;
	
	@XmlAttribute(name = "CTF_reward", required = false)
    private int     CTFReward;

	public int getKillPlayer()
	{
		return killPlayer;
	}

	public int getDie()
	{
		return die;
	}
	
	public int getFlagCap()
    {
        return flagCap;
    }
	
	public int getFlagBase()
    {
        return flagBase;
    }
	
	public int getFlagGround()
    {
        return flagGround;
    }
	
	public int getCTFReward()
    {
        return CTFReward;
    }
}
