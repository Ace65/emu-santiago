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
package gameserver.skill.effect;

import gameserver.controllers.attack.AttackStatus;
import gameserver.controllers.movement.AttackCalcObserver;
import gameserver.controllers.movement.AttackStatusObserver;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.skill.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import commons.utils.Rnd;
 

/**
 * @author ATracer
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BlindEffect")
public class BlindEffect extends EffectTemplate
{
	@XmlAttribute
	private int value;

	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();
	}

	@Override
	public void calculate(Effect effect)
	{
		super.calculate(effect, StatEnum.BLIND_RESISTANCE, null);
	}
	
	@Override
	public void startEffect(Effect effect)
	{
		AttackCalcObserver acObserver = new AttackStatusObserver(value, AttackStatus.DODGE)
		{

			@Override
			public boolean checkAttackerStatus(AttackStatus status)
			{
				return Rnd.get(100) <= value;
			}
			
		};
		effect.getEffected().getObserveController().addAttackCalcObserver(acObserver);
		effect.setAttackStatusObserver(acObserver, position);
		effect.getEffected().getEffectController().setAbnormal(EffectId.BLIND.getEffectId());
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		effect.getEffected().getEffectController().unsetAbnormal(EffectId.BLIND.getEffectId());
		AttackCalcObserver acObserver = effect.getAttackStatusObserver(position);
		if (acObserver != null)
			effect.getEffected().getObserveController().removeAttackCalcObserver(acObserver);
	}

}
