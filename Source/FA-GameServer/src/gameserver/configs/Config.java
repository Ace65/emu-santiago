/**
 * This file is part of aionfantasy <aionfantasy.com>.
 *
 *  aionfantasy is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aionfantasy is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aionfantasy.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.configs;

//import gameserver.GameServer;
import gameserver.configs.administration.AdminConfig;
import gameserver.configs.main.CacheConfig;
import gameserver.configs.main.CustomConfig;
import gameserver.configs.main.DropConfig;
import gameserver.configs.main.EnchantsConfig;
import gameserver.configs.main.EventConfig;
import gameserver.configs.main.FallDamageConfig;
import gameserver.configs.main.GSConfig;
import gameserver.configs.main.GroupConfig;
import gameserver.configs.main.HTMLConfig;
import gameserver.configs.main.LegionConfig;
import gameserver.configs.main.NpcMovementConfig;
import gameserver.configs.main.PeriodicSaveConfig;
import gameserver.configs.main.PricesConfig;
import gameserver.configs.main.RateConfig;
import gameserver.configs.main.ShutdownConfig;
import gameserver.configs.main.SiegeConfig;
import gameserver.configs.main.TaskManagerConfig;
import gameserver.configs.main.ThreadConfig;
import gameserver.configs.network.FloodConfig;
import gameserver.configs.network.IPConfig;
import gameserver.configs.network.NetworkConfig;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import commons.configuration.ConfigurableProcessor;
import commons.database.DatabaseConfig;
import commons.utils.PropertiesUtils;


/**
 * @author -Nemesiss-
 * @author SoulKeeper
 */
public class Config
{
	/**
	 * Logger for this class.
	 */
	protected static final Logger	log	= Logger.getLogger(Config.class);
	
	/**
     * Files Configs Emu
     */
	public static final String FANTASY_GS = "./config/default.properties";
    public static final String ADMIN_CONFIG = "./config/admin.properties";
    public static final String LEGION_CONFIG = "./config/legions.properties";
    public static final String DROP_CONFIG = "./config/drop.properties";
    public static final String RATE_CONFIG = "./config/rates.properties";
    public static final String GENERAL_CONFIG = "./config/general.properties";
    public static final String GAMESERVER_CONFIG = "./config/gameserver.properties";
    public static final String CUSTOM_CONFIG = "./config/custom.properties";
    public static final String DATABASE_CONFIG = "./config/database.properties";
    public static final String NPC_CONFIG = "./config/npc.properties";
    public static final String CHARACTER_CONFIG = "./config/character.properties";
    public static final String SIEGE_CONFIG = "./config/siege.properties";
    public static final String NETWORK_CONFIG = "./config/network.properties";
    public static final String FLOOD_CONFIG = "./config/floodcontrol.properties";
    public static final String EVENT_CONFIG = "./config/event.properties";

	/**
	 * Initialize all configs in gameserver.configs package
	 */
	public static void load()
	{
		try
		{
			//Properties props = PropertiesUtils.load(GameServer.CONFIGURATION_FILE); 
			//ConfigurableProcessor.process(Config.class, props);			
			ConfigurableProcessor.process(AdminConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(LegionConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(DropConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(RateConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(CacheConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(ShutdownConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(TaskManagerConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(GroupConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(CustomConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(EnchantsConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(FallDamageConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(GSConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(NpcMovementConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(PeriodicSaveConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(PricesConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(SiegeConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(ThreadConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(NetworkConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(DatabaseConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(HTMLConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(FloodConfig.class, loadFile(FANTASY_GS));
			ConfigurableProcessor.process(EventConfig.class, loadFile(FANTASY_GS));
		}
		catch(Exception e)
		{
			log.fatal("Can't load gameserver configuration: ", e);
			throw new Error("Can't load gameserver configuration: ", e);
		}

		IPConfig.load();
	}
	
	private static Properties loadFile(String name) throws IOException
        {
        File cfg = new File(name);
            if (!cfg.exists())
            {
                log.fatal("File "+name+": No such file.");
                throw new Error("File "+name+": No such file.");
            }  
            else if (!cfg.canRead())
            {
                log.fatal("File "+name+": Unreadable file (check filesystem permissions)");
                throw new Error("File "+name+": Unreadable file (check filesystem permissions)");
            }
            return PropertiesUtils.load(cfg);
        }
}
