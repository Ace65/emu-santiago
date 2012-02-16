package gameserver.skill.properties;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.skill.model.CreatureWithDistance;
import gameserver.skill.model.Skill;
import gameserver.utils.PacketSendUtility;

import java.util.Iterator;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * @author kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetSpeciesProperty")
public class TargetSpeciesProperty extends Property
{

	@XmlAttribute(required = true)
	protected TargetSpeciesAttribute	value;

	/**
	 * Gets the value of the value property.
	 * 
	 * @return possible object is {@link TargetSpeciesAttribute }
	 * 
	 */
	public TargetSpeciesAttribute getValue()
	{
		return value;
	}

	@Override
	public boolean set(Skill skill)
	{
		TreeSet<CreatureWithDistance> effectedList = skill.getEffectedList();
		if (skill.getFirstTarget() == null && skill.getTargetType() == 1)
			return true;
		else if (skill.getFirstTarget() == null)
			return false;
		
		switch(value)
		{
			case PC:
				if (!(skill.getFirstTarget() instanceof Player) && skill.getEffector() instanceof Player && !skill.checkNonTargetAOE())
				{
					PacketSendUtility.sendPacket((Player)skill.getEffector(), SM_SYSTEM_MESSAGE.INVALID_TARGET());
					return false;
				}
				for(Iterator<CreatureWithDistance> iter = effectedList.iterator(); iter.hasNext();)
				{
					Creature nextEffected = iter.next().getCreature();

					if (nextEffected instanceof Player)
						continue;

					iter.remove();
				}

				break;
			case NPC:
				if (!(skill.getFirstTarget() instanceof Npc) && !skill.checkNonTargetAOE())
				{
					if (skill.getEffector() instanceof Player)
						PacketSendUtility.sendPacket((Player)skill.getEffector(), SM_SYSTEM_MESSAGE.INVALID_TARGET());
					return false;
				}
				for(Iterator<CreatureWithDistance> iter = effectedList.iterator(); iter.hasNext();)
				{
					Creature nextEffected = iter.next().getCreature();

					if (nextEffected instanceof Npc)
						continue;

					iter.remove();
				}

				break;
			case ALL:
			case NONE:
				break;
		}
		
		return true;
	}
}
