package gameserver.skill.effect;

import gameserver.model.gameobjects.Creature;
import gameserver.skill.model.Effect;
import gameserver.skill.model.SkillType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * @author kecimis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BuffSilenceEffect")
public class BuffSilenceEffect extends EffectTemplate
{
	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();
	}

	@Override
	public void startEffect(Effect effect)
	{
		final Creature effected = effect.getEffected();
		effect.setAbnormal(EffectId.SILENCE.getEffectId());
		effected.getEffectController().setAbnormal(EffectId.SILENCE.getEffectId());
		if (effected.getCastingSkill() != null && effected.getCastingSkill().getSkillTemplate().getType() == SkillType.MAGICAL)
			effected.getController().cancelCurrentSkill();
	}

	@Override
	public void endEffect(Effect effect)
	{
		effect.getEffected().getEffectController().unsetAbnormal(EffectId.SILENCE.getEffectId());
	}
}
