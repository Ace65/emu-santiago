package gameserver.skill.effect;

import gameserver.model.gameobjects.Creature;
import gameserver.skill.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * @author kecimis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DiseaseEffect")
public class DiseaseEffect extends EffectTemplate
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
		effect.setAbnormal(EffectId.DISEASE.getEffectId());
		effected.getEffectController().setAbnormal(EffectId.DISEASE.getEffectId());
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		if (effect.getEffected().getEffectController().isAbnormalSet(EffectId.DISEASE))
			effect.getEffected().getEffectController().unsetAbnormal(EffectId.DISEASE.getEffectId());
	}
}
