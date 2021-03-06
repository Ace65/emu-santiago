package gameserver.model.gameobjects;

import gameserver.ai.npcai.SkillAreaNpcAi;
import gameserver.controllers.NpcWithCreatorController;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author ViAl
 *
 */
public class SkillAreaNpc extends NpcWithCreator
{
	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
	public SkillAreaNpc(int objId, NpcWithCreatorController controller, SpawnTemplate spawnTemplate,
		VisibleObjectTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate);
		// TODO Auto-generated constructor stub
	}
	
	public NpcWithCreatorController getController()
	{
		return (NpcWithCreatorController)super.getController();
	}
	public SkillAreaNpc getOwner()
	{
		return (SkillAreaNpc)this;
	}
	@Override
	public void initializeAi()
	{
		this.ai = new SkillAreaNpcAi();
		ai.setOwner(this);
	}
	
	/**
	 * @return NpcObjectType.SKILLAREANPC
	 */
	@Override
	public NpcObjectType getNpcObjectType()
	{
		return NpcObjectType.SKILLAREANPC;
	}
}
