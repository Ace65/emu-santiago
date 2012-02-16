package gameserver.dataholders;

import gnu.trove.TIntObjectHashMap;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import gameserver.model.templates.BattleGroundTemplate;
import gameserver.model.templates.SpawnInfo;

/**
 * 
 * @author ambrosius / adapted DESTRUCTOR/HADESH
 * 
 */
@XmlRootElement(name = "battlegrounds")
@XmlAccessorType(XmlAccessType.FIELD)
public class BattleGroundData
{
	@XmlElement(name = "battleground")
	private List<BattleGroundTemplate> bgList;
	
	@XmlElement(name = "bg_agent")
	private List<SpawnInfo> agentLocations;
	
	private TIntObjectHashMap<BattleGroundTemplate>	bgData	= new TIntObjectHashMap<BattleGroundTemplate>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for(BattleGroundTemplate bg: bgList)
		{
			bgData.put(bg.getTplId(), bg);
		}
	}

	public int size()
	{
		return bgData.size();
	}
	
	public List<BattleGroundTemplate> getAllTemplates()
	{
		return bgList;
	}

	public BattleGroundTemplate getBattleGroundTemplate(int tplId)
	{
		return bgData.get(tplId);
	}

	public List<BattleGroundTemplate> getBgList()
	{
		return bgList;
	}

	public List<SpawnInfo> getAgentLocations()
	{
		return agentLocations;
	}

	public TIntObjectHashMap<BattleGroundTemplate> getBgData()
	{
		return bgData;
	}

    /**
     * @param battlegroundsTemplates
     */
    public void setTemplates(List<BattleGroundTemplate> battlegroundsTemplates)
    {
        for(BattleGroundTemplate bg: battlegroundsTemplates)
        {
            bgData.put(bg.getTplId(), bg);
        }
    }
	
}