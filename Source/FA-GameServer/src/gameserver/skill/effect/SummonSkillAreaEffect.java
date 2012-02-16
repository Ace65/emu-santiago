package gameserver.skill.effect;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.SkillAreaNpc;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.skill.model.Effect;
import gameserver.spawn.SpawnEngine;
import gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;


/**
 * @author ViAl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SummonSkillAreaEffect")
public class SummonSkillAreaEffect extends SummonEffect
{
	@XmlAttribute(name = "skill_id", required = true)
	protected int	skillId;
	
	@Override
	public void calculate(Effect effect)
	{
		effect.addSucessEffect(this);
	}
	
	@Override
	public void applyEffect(Effect effect)
	{
		SpawnEngine spawnEngine = SpawnEngine.getInstance();
		final Creature effector = effect.getEffector();
		float x = effect.getX();
		float y = effect.getY();
		float z = effect.getZ();
		byte heading = effector.getHeading();
		int worldId = effector.getWorldId();
		int instanceId = effector.getInstanceId();
		
		final SpawnTemplate spawn = spawnEngine.addNewSpawn(worldId, instanceId, npcId, x, y, z, heading, 0, 0, true, true);
		if (spawn == null)
        {
            Logger.getLogger(SummonSkillAreaEffect.class).error("There is no template with id " + npcId);
            return;
        }
		final SkillAreaNpc npc = spawnEngine.spawnSkillAreaNpc(spawn, instanceId, effector, skillId);

		ThreadPoolManager.getInstance().schedule(new Runnable(){

			@Override
			public void run()
			{
				npc.getLifeStats().reduceHp(npc.getLifeStats().getCurrentHp() + 1, npc, true);
			}
		}, time * 1000);

	}

}
