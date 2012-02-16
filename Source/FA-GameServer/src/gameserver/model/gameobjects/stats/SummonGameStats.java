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
package gameserver.model.gameobjects.stats;

import gameserver.model.gameobjects.Summon;
import gameserver.model.templates.stats.SummonStatsTemplate;

/**
 * @author ATracer
 *
 */
public class SummonGameStats extends CreatureGameStats<Summon>
{

	/**
	 * 
	 * @param owner
	 * @param statsTemplate
	 */
	public SummonGameStats(Summon owner, SummonStatsTemplate statsTemplate)
	{
		super(owner);
		initStat(StatEnum.MAXHP, statsTemplate.getMaxHp());
		initStat(StatEnum.MAXMP, statsTemplate.getMaxMp());
		initStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK, statsTemplate.getMainHandAttack());
		initStat(StatEnum.PHYSICAL_DEFENSE, statsTemplate.getPdefense());
		initStat(StatEnum.ACCURACY, statsTemplate.getMainHandAccuracy());
		initStat(StatEnum.MAIN_HAND_CRITICAL, statsTemplate.getMainHandCritRate());
		initStat(StatEnum.MAGICAL_CRITICAL, statsTemplate.getMcrit());
		initStat(StatEnum.BOOST_MAGICAL_SKILL, 0);
		initStat(StatEnum.MAGICAL_ACCURACY, statsTemplate.getMagicAccuracy());
		initStat(StatEnum.PARRY, statsTemplate.getParry());
		initStat(StatEnum.EVASION, statsTemplate.getEvasion());
		initStat(StatEnum.MAGICAL_RESIST, statsTemplate.getMresist());
		initStat(StatEnum.ATTACK_SPEED, 2000);
		initStat(StatEnum.ATTACK_RANGE, (owner.getObjectTemplate().getAttackRange() * 1000));
		initStat(StatEnum.SPEED, Math.round(statsTemplate.getRunSpeed() * 1000));
		initStat(StatEnum.REGEN_HP, owner.getLevel() + 3);
		initStat(StatEnum.KNOWLEDGE, 100);
		initStat(StatEnum.ACCURACY, 1000);
		initStat(StatEnum.MAGICAL_ACCURACY, 1000);
	}
}
