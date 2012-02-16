/*
 * This file is part of Aion Fantasy Emulator <aionfantasy.com>.
 *
 *  aion fantasy emulator is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion fantasy emulator is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.network.aion;

import gameserver.network.aion.serverpackets.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class is holding opcodes for all server packets. It's used only to have all opcodes in one place
 *
 * @author Luno
 * @author alexa026
 * @author ATracer
 * @author avol
 * @author orz
 */
public class ServerPacketsOpcodes
{
    private static Map<Class<? extends AionServerPacket>, Integer>	opcodes	= new HashMap<Class<? extends AionServerPacket>, Integer>();

    static
    {
        Set<Integer> idSet = new HashSet<Integer>();
        addPacketOpcode(SM_STATS_INFO.class,0x0001, idSet);// 2.6 0x01, 2.7
        // 2.7 Unknown - 01 01 54 BE FE 00 00
        // 2.7 Unknown - 03 01 54 A0 FE 01 
        addPacketOpcode(SM_CHAT_INIT.class,0x0104, idSet);// 2.6 0x04, 2.7
		addPacketOpcode(SM_CHANNEL_INFO.class,0x0105, idSet);// 2.6 0x05, 2.7
		addPacketOpcode(SM_MACRO_RESULT.class,0x0106, idSet);// 2.6 0x06, 2.7
        addPacketOpcode(SM_MACRO_LIST.class,0x0107, idSet);// 2.6 0x07, 2.7
		addPacketOpcode(SM_NICKNAME_CHECK_RESPONSE.class,0x0109, idSet);// 2.6 0x09, 2.7
        addPacketOpcode(SM_RIFT_ANNOUNCE.class,0x010A, idSet);// 2.7
        addPacketOpcode(SM_SET_BIND_POINT.class,0x010B, idSet);// 2.6 0x0B, 2.7
		// 2.7 Unknown - 0C 01 54 AB FE 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
        addPacketOpcode(SM_ABYSS_RANK.class,0x010D, idSet);// 2.6 0x0D, 2.7
        addPacketOpcode(SM_FRIEND_UPDATE.class,0x000E, idSet);// 2.6 0x0E, 2.7
		addPacketOpcode(SM_PETITION.class,0x000F, idSet);// 2.6 0x0F, (Disabled in 2.7 Retail?)
		addPacketOpcode(SM_RECIPE_DELETE.class,0x0110, idSet);// 2.6 0x10, 2.7
        addPacketOpcode(SM_LEARN_RECIPE.class,0x0111, idSet);// 2.6 0x11, 2.7
		addPacketOpcode(SM_TELEPORT_LOC.class,0x0012, idSet);// 2.6 0x12, 2.7
        addPacketOpcode(SM_DELETE.class,0x0014, idSet);// 2.6 0x14, 2.7
        addPacketOpcode(SM_PLAYER_MOVE.class,0x0115, idSet);// 2.6 0x11, 2.7 Testing
		addPacketOpcode(SM_MESSAGE.class,0x0016, idSet);// 2.6 0x16, 2.7
        addPacketOpcode(SM_LOGIN_QUEUE.class,0x0017, idSet);// 2.6 0x17, 2.7 Testing
		addPacketOpcode(SM_INVENTORY_INFO.class,0x0018, idSet);// 2.6 0x18, 2.7
        addPacketOpcode(SM_SYSTEM_MESSAGE.class,0x0019, idSet);// 2.6 0x19, 2.7
        addPacketOpcode(SM_DELETE_ITEM.class,0x001A, idSet);// 2.6 0x1A, 2.7
        addPacketOpcode(SM_INVENTORY_UPDATE.class,0x001B, idSet);// 2.6 0x1B, 2.7
		addPacketOpcode(SM_UI_SETTINGS.class,0x001C, idSet);// 2.6 0x1C, 2.7
        addPacketOpcode(SM_UPDATE_ITEM.class,0x001D, idSet);// 2.6 0x1D, 2.7
		addPacketOpcode(SM_PLAYER_INFO.class,0x001E, idSet);// 2.6 0x1E, 2.7
        addPacketOpcode(SM_STANCE_STATE.class, 0x001F, idSet);// 2.6 0x1F, 2.7
		addPacketOpcode(SM_GATHER_STATUS.class,0x0020, idSet);// 2.6 0x20, 2.7
        addPacketOpcode(SM_CASTSPELL.class,0x0021, idSet);// 2.6 0x21, 2.7
        addPacketOpcode(SM_STATUPDATE_MP.class,0x0022, idSet);// 2.6 0x22, 2.7
        addPacketOpcode(SM_STATUPDATE_HP.class,0x0023, idSet);// 2.6 0x23, 2.7
        addPacketOpcode(SM_STATUPDATE_DP.class,0x0024, idSet);// 2.6 0x24, 2.7
        addPacketOpcode(SM_ATTACK_STATUS.class,0x0025, idSet);// 2.6 0x25, 2.7
		addPacketOpcode(SM_STATUPDATE_EXP.class,0x0026, idSet);// 2.6 0x26, 2.7
        addPacketOpcode(SM_DP_INFO.class,0x0027, idSet);// 2.6 0x27, 2.7
		addPacketOpcode(SM_LEGION_TABS.class,0x002A, idSet);// 2.6 0x2A, 2.7
        addPacketOpcode(SM_LEGION_UPDATE_NICKNAME.class,0x012B, idSet);// 2.6 0x2B, 2.7 Testing
		addPacketOpcode(SM_NPC_INFO.class,0x002C, idSet);// 2.6 0x2C, 2.7
        addPacketOpcode(SM_ENTER_WORLD_CHECK.class,0x002D, idSet);// 2.6 0x2D, 2.7
        addPacketOpcode(SM_PLAYER_SPAWN.class,0x002F, idSet);// 2.6 0x2F, 2.7
        addPacketOpcode(SM_GATHERABLE_INFO.class,0x0031, idSet);// 2.6 0x31, 2.7
        addPacketOpcode(SM_QUESTION_WINDOW.class,0x0032, idSet);// 2.6 0x32, 2.7
        addPacketOpcode(SM_SKILL_COOLDOWN.class,0x0033, idSet);// 2.6 0x33, 2.7
        addPacketOpcode(SM_ATTACK.class,0x0034, idSet);// 2.6 0x34, 2.7
        addPacketOpcode(SM_MOVE.class,0x0037, idSet);// 2.6 0x37, 2.7
		addPacketOpcode(SM_TRANSFORM.class,0x0038, idSet);// 2.6 0x38, 2.7
		// 2.7 Unknown - 39 00 54 F6 FF 0E 83 00 80 5A
		addPacketOpcode(SM_DIALOG_WINDOW.class,0x003A, idSet);// 2.6 0x3A, 2.7
		addPacketOpcode(SM_SELL_ITEM.class,0x003C, idSet);// 2.6 0x41, 2.7
		addPacketOpcode(SM_WEATHER.class,0x0040, idSet);// 2.6 0x40, 2.7
        addPacketOpcode(SM_VIEW_PLAYER_DETAILS.class,0x0041, idSet);// 2.6 0x41, 2.7
		addPacketOpcode(SM_UPDATE_PLAYER_APPEARANCE.class,0x0042, idSet);// 2.6 0x42, 2.7
        addPacketOpcode(SM_GATHER_UPDATE.class,0x0043, idSet);// 2.6 0x43, 2.7
		addPacketOpcode(SM_GAME_TIME.class,0x0044, idSet);// 2.6 0x44, 2.7
        addPacketOpcode(SM_EMOTION.class,0x0045, idSet);// 2.6 0x45, 2.7
		addPacketOpcode(SM_LOOKATOBJECT.class,0x0046, idSet);// 2.6 0x46, 2.7
		addPacketOpcode(SM_TIME_CHECK.class,0x0047, idSet);// 2.6 0x47, 2.7
        addPacketOpcode(SM_SKILL_CANCEL.class,0x0048, idSet);// 2.60x48, 2.7
		addPacketOpcode(SM_TARGET_SELECTED.class,0x0049, idSet);// 2.6 0x49, 2.7
		addPacketOpcode(SM_SKILL_LIST.class,0x004A, idSet);// 2.6 0x4A, 2.7
        addPacketOpcode(SM_CASTSPELL_END.class,0x004B, idSet);// 2.6 0x4B, 2.7
		addPacketOpcode(SM_SKILL_ACTIVATION.class,0x004C, idSet);// 2.6 0x4C, 2.7
        addPacketOpcode(SM_STIGMA_SKILL_REMOVE.class,0x004D, idSet);// 2.6 0x4D, 2.7
		addPacketOpcode(SM_ABNORMAL_EFFECT.class,0x0050, idSet);// 2.6 0x50, 2.7
        addPacketOpcode(SM_ABNORMAL_STATE.class,0x0051, idSet);// 2.6 0x51, 2.7

        addPacketOpcode(SM_PLASTIC_SURGERY.class,0x0153, idSet);// 2.6 0x53, 2.7
        addPacketOpcode(SM_FORTRESS_STATUS.class,0x0154, idSet);// 2.6 0x54, 2.7
        addPacketOpcode(SM_INFLUENCE_RATIO.class,0x0155, idSet);// 2.6 0x55, 2.7
		addPacketOpcode(SM_NAME_CHANGE.class,0x0156, idSet);// 2.6 0x56, 2.7 Testing
        addPacketOpcode(SM_GROUP_INFO.class,0x0158, idSet);// 2.6 0x58, 2.7 Testing
		addPacketOpcode(SM_SHOW_NPC_ON_MAP.class,0x0159, idSet);// 2.6 0x59, 2.7
        addPacketOpcode(SM_GROUP_MEMBER_INFO.class,0x015B, idSet);// 2.6 0x5B, 2.7
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO.class,0x015E, idSet);// 2.6 0x5E, 2.7 Testing
		addPacketOpcode(SM_QUIT_RESPONSE.class,0x0160, idSet);// 2.6 0x60, 2.7
		addPacketOpcode(SM_PLAYER_STATE.class,0x0162, idSet);// 2.6 0x62, 2.7
		// 2.7 Unknown - 63 00 54 00 FF 01 00 00 
		addPacketOpcode(SM_LEVEL_UPDATE.class,0x0164, idSet);// 2.7
		addPacketOpcode(SM_KEY.class,0x0166, idSet);// 2.6 0x66, 2.7
        addPacketOpcode(SM_STARTED_QUEST_LIST.class,0x0167, idSet);// 2.6 0x67, 2.7
        addPacketOpcode(SM_EXCHANGE_REQUEST.class,0x0168, idSet);// 2.6 0x68, 2.7
        addPacketOpcode(SM_SUMMON_PANEL_REMOVE.class,0x0169, idSet);// 2.6 0x69, 2.7
        addPacketOpcode(SM_EXCHANGE_ADD_ITEM.class,0x016B, idSet);// 2.6 0x6B, 2.7
        addPacketOpcode(SM_EXCHANGE_CONFIRMATION.class,0x016C, idSet);// 2.6 0x6C, 2.7
        addPacketOpcode(SM_EXCHANGE_ADD_KINAH.class,0x016D, idSet);// 2.6 0x6D, 2.7
        addPacketOpcode(SM_EMOTION_LIST.class,0x016F, idSet);// 2.6 0x63, 2.7
        addPacketOpcode(SM_TARGET_UPDATE.class,0x0171, idSet);// 2.6 0x71, 2.7
        // 2.7 Unknown - 73 01 54 30 FE 07 19
        addPacketOpcode(SM_LEGION_UPDATE_SELF_INTRO.class,0x0177, idSet);// 2.6 0x77, 2.7
		addPacketOpcode(SM_DREDGION_INSTANCE.class,0x0178, idSet);// 2.6 0x78, 2.7
		addPacketOpcode(SM_INSTANCE_SCORE.class, 0x0179, idSet);// 2.1 0x76, 2.7 Testing
		addPacketOpcode(SM_QUEST_ACCEPTED.class,0x017A, idSet);// 2.6 0x7A, 2.7
        addPacketOpcode(SM_QUEST_LIST.class,0x017B, idSet);// 2.6 0x7B, 2.7
		addPacketOpcode(SM_PING_RESPONSE.class,0x007E, idSet);// 2.6 0x7E, 2.7
        addPacketOpcode(SM_NEARBY_QUESTS.class,0x017F, idSet);// 2.6 0x7F, 2.7
		addPacketOpcode(SM_CUBE_UPDATE.class,0x0180, idSet);// 2.6 0x80, 2.7
		// 2.7 Unknown - 82 00 57 7D FF 
		// 2.7 Unknown - 83 01 54 20 FE 02 43 00 68 00 69... Reply to CM_CHAT_RECRUIT_GROUP
        addPacketOpcode(SM_PET.class,0x0185, idSet);// 2.6 0x85, 2.7
        addPacketOpcode(SM_UPDATE_NOTE.class,0x0186, idSet);// 2.6 0x86, 2.7
        addPacketOpcode(SM_ITEM_COOLDOWN.class,0x0187, idSet);// 2.6 0x87, 2.7
        addPacketOpcode(SM_PLAY_MOVIE.class,0x0189, idSet);// 2.6 0x89, 2.7
		addPacketOpcode(SM_LEGION_INFO.class,0x018C, idSet);// 2.6 0x8C, 2.7
        addPacketOpcode(SM_LEGION_LEAVE_MEMBER.class,0x018E, idSet);// 2.6 0x8E, 2.7 Testing
		addPacketOpcode(SM_LEGION_ADD_MEMBER.class,0x018F, idSet);// 2.6 0x8F, 2.7
        addPacketOpcode(SM_LEGION_UPDATE_TITLE.class,0x0190, idSet);// 2.6 0x90, 2.7
        addPacketOpcode(SM_LEGION_UPDATE_MEMBER.class,0x0191, idSet);// 2.6 0x91, 2.7
		addPacketOpcode(SM_PLAYER_MOTION.class, 0x0192, idSet);// 2.6 0x92, 2.7
		// 2.6 - Unknown Opcode 95, sent after Unknown client C7 after SM_MAIL_SERVICE: 95 54 52 01 07 00 00 00 01 00 00 
		//addPacketOpcode(SM_NPC_TRADE.class,0x0197, idSet); // 2.7
        addPacketOpcode(SM_SUMMON_OWNER_REMOVE.class,0x0198, idSet); // 2.6 0x98, 2.7
        addPacketOpcode(SM_SUMMON_PANEL.class,0x0199, idSet);// 2.6 0x99, 2.7
        addPacketOpcode(SM_SUMMON_UPDATE.class,0x019B, idSet);// 2.6 0x9B, 2.7
		addPacketOpcode(SM_LEGION_EDIT.class,0x019C, idSet);// 2.6 0x9C, 2.7
        addPacketOpcode(SM_LEGION_MEMBERLIST.class,0x019D, idSet);// 2.6 0x9D, 2.7
        addPacketOpcode(SM_INGAMESHOP_BALANCE.class, 0x019F, idSet);// 2.6 0x9F, 2.7
        addPacketOpcode(SM_SUMMON_USESKILL.class,0x01A0, idSet);// 2.6 0xA0, 2.7
        addPacketOpcode(SM_MAIL_SERVICE.class,0x01A1, idSet);// 2.6 0xA1, 2.7
		addPacketOpcode(SM_FRIEND_LIST.class,0x01A2, idSet);// 2.6 0xA2, 2.7

        addPacketOpcode(SM_PRIVATE_STORE.class,0x01A4, idSet); // 2.6 0xA4, 2.7
        addPacketOpcode(SM_ABYSS_RANK_UPDATE.class,0xA6, idSet);// 2.1
        addPacketOpcode(SM_GROUP_LOOT.class,0x01A7, idSet);// 2.7
        addPacketOpcode(SM_ABYSS_RANKING_PLAYERS.class,0x01A8, idSet);// 2.6 0xA8, 2.7
		addPacketOpcode(SM_MAY_LOGIN_INTO_GAME.class,0x01A9, idSet);// 2.6 0xA9, 2.7
        addPacketOpcode(SM_ACADEMY_BOOTCAMP_STAGE.class,0x01AA, idSet);
        addPacketOpcode(SM_ABYSS_RANKING_LEGIONS.class,0x01AB, idSet);// 2.6 0xAB, 2.7
		addPacketOpcode(SM_PONG.class,0x01AC, idSet);// 2.6 0xAC, 2.7
		addPacketOpcode(SM_INSTANCE_COOLDOWN.class,0x01AD, idSet);// 2.6 0xAD, 2.7
		addPacketOpcode(SM_KISK_UPDATE.class,0x01AE, idSet);// 2.6 0xAE, 2.7
		addPacketOpcode(SM_BROKER_ITEMS.class,0x01B0, idSet);// 2.6 0xB0, 2.7
        addPacketOpcode(SM_PRIVATE_STORE_NAME.class,0x01B1, idSet);// 2.6 0xB1, 2.7
		addPacketOpcode(SM_CRAFT_ANIMATION.class,0x01B2, idSet);// 2.6 0xB2, 2.7
		// 2.7 Unknown - B3 01 54 70 FE 00 00 00 00 00 01 00 01 00 00
        addPacketOpcode(SM_ASCENSION_MORPH.class,0x01B4, idSet);// 2.6 0xB4, 2.7 Testing
		addPacketOpcode(SM_CRAFT_UPDATE.class,0x01B5, idSet);// 2.6 0xB5, 2.7
        addPacketOpcode(SM_CUSTOM_SETTINGS.class,0x01B6, idSet);// 2.6 0xB6, 2.7
		addPacketOpcode(SM_ITEM_USAGE_ANIMATION.class,0x01B7, idSet);// 2.6 0xB7, 2.7
        addPacketOpcode(SM_DUEL.class,0x01B9, idSet);// 2.6 0xB9, 2.7
		addPacketOpcode(SM_PET_MOVE.class,0x01BB, idSet);// 2.6 0xBB, 2.7
		// 2.7 Unknown - BD 01 54 7A FE 00 00
        addPacketOpcode(SM_QUESTIONNAIRE.class,0x01BF, idSet);// 2.6 0xBF, 2.7
        addPacketOpcode(SM_RESURRECT.class,0x01C0, idSet);// 2.6 0xC0, 2.7
        addPacketOpcode(SM_DIE.class,0x01C1, idSet);// 2.6 0xC1, 2.7
		addPacketOpcode(SM_WINDSTREAM_LOCATIONS.class,0x01C2, idSet);// 2.6 0xC2, 2.7
        addPacketOpcode(SM_WINDSTREAM.class,0x01C3, idSet);// 2.6 0xC3, 2.7
        addPacketOpcode(SM_FIND_GROUP.class,0x01C4, idSet);// 2.6 0xC4, 2.7
        // 2.7 Unknown - C5 01 54 62 FE 01 00 00 
		addPacketOpcode(SM_WAREHOUSE_INFO.class,0x01C6, idSet);// 2.6 0xC6, 2.7
        addPacketOpcode(SM_REPURCHASE.class,0x01C7, idSet);// 2.6 0xC7, 2.7
		addPacketOpcode(SM_DELETE_WAREHOUSE_ITEM.class,0x01C8, idSet);// 2.6 0xC8, 2.7
		addPacketOpcode(SM_WAREHOUSE_UPDATE.class,0x01C9, idSet);// 2.6 0xC9, 2.7
        addPacketOpcode(SM_INGAMESHOP.class, 0x01CA, idSet);// 2.6 0xCA, 2.7
        addPacketOpcode(SM_UPDATE_WAREHOUSE_ITEM.class,0x01CB, idSet);// 2.6 0xCB, 2.7
		addPacketOpcode(SM_INGAMESHOP_ITEM.class, 0x01CC, idSet);// 2.6 Testing 0xCC, 2.7
        addPacketOpcode(SM_INGAMESHOP_ITEMS.class, 0x01CD, idSet);// 2.6 0xCD, 2.7
		addPacketOpcode(SM_TITLE_LIST.class,0x01CE, idSet);// 2.6 0xCE, 2.7
        addPacketOpcode(SM_CHARACTER_SELECT.class,0x01D1, idSet);// 2.6 0xD1, 2.7
        addPacketOpcode(SM_PLAYER_SEARCH.class,0x01D3, idSet);// 2.6 0xD3, 2.7
        addPacketOpcode(SM_LEGION_EMBLEM_SEND.class,0x01D4, idSet);// 2.6 0xD4, 2.7
		addPacketOpcode(SM_LEGION_EMBLEM.class,0x01D5, idSet);// 2.6 0xD5, 2.7
		// 2.7 Unknown - D6 01 54 95 FE 00 00 00 9E B3 5A CE 9E B3 5A CE
        addPacketOpcode(SM_LEGION_UPDATE_EMBLEM.class,0x01D7, idSet);// 2.6 0xD7, 2.7 Testing
		addPacketOpcode(SM_SIEGE_AETHERIC_FIELDS.class,0x01D8, idSet);// 2.6 0xD8, 2.7 Testing
		// 2.7 Unknown - D9 01 54 96 FE 6D F4 0B 0A 00 00 00 9E B3 5A CE
        addPacketOpcode(SM_ABYSS_ARTIFACT_INFO3.class, 0x01DA, idSet);// 2.6 0xDA, 2.7 Testing
		addPacketOpcode(SM_FRIEND_RESPONSE.class,0x00DC, idSet);// 2.6
		addPacketOpcode(SM_BLOCK_LIST.class,0x01DE, idSet);// 2.6 0xDE, 2.7
        addPacketOpcode(SM_BLOCK_RESPONSE.class,0x00DF, idSet);// 2.6
		addPacketOpcode(SM_FRIEND_NOTIFY.class,0x00E0, idSet);// 2.1
		addPacketOpcode(SM_TELEPORT_MAP.class,0x01E2, idSet);// 2.6 0xE2, 2.7
        addPacketOpcode(SM_FORCED_MOVE.class,0x01E3, idSet);// 2.6 0xE3, 2.7
        // 2.7 Unknown - E4 01 54 83 FE C3 B9 37 80 03 07 00 00 00 00 00 00 00
		addPacketOpcode(SM_USE_OBJECT.class,0x01E5, idSet);// 2.6 0xE5, 2.7
		addPacketOpcode(SM_CHARACTER_LIST.class,0x01E6, idSet);// 2.6 0xE6, 2.7
		addPacketOpcode(SM_L2AUTH_LOGIN_CHECK.class,0x01E7, idSet);// 2.6 0xE7, 2.7
		addPacketOpcode(SM_DELETE_CHARACTER.class,0x01E8, idSet);// 2.6 0xE8, 2.7
        addPacketOpcode(SM_CREATE_CHARACTER.class,0x01E9, idSet);// 2.6 0xE9, 2.7
        addPacketOpcode(SM_TARGET_IMMOBILIZE.class,0x01EA, idSet);// 2.6 0xEA, 2.7 Testing
        addPacketOpcode(SM_RESTORE_CHARACTER.class,0x01EB, idSet);// 2.6 0xEB, 2.7
		addPacketOpcode(SM_LOOT_ITEMLIST.class,0x01EC, idSet);// 2.6 0xEC, 2.7
        addPacketOpcode(SM_LOOT_STATUS.class,0x01ED, idSet);// 2.6 0xED, 2.7
		addPacketOpcode(SM_MANTRA_EFFECT.class,0x01EE, idSet);// 2.6 0xEE, 2.7
        addPacketOpcode(SM_RECIPE_LIST.class,0x01EF, idSet);// 2.6 0xEF, 2.7
        // 2.7 Unknown - F0 01 54 8F FE 6B 04 00 00 02 00 00 00 00   
		addPacketOpcode(SM_SIEGE_LOCATION_INFO.class,0x01F1, idSet);// 2.6 0xF1, 2.7
        addPacketOpcode(SM_FLY_TIME.class,0x01F2, idSet);// 2.6 0xF2, 2.7
        addPacketOpcode(SM_FORTRESS_INFO.class,0x01F3, idSet);// 2.6 0xF3, 2.7
        addPacketOpcode(SM_ALLIANCE_MEMBER_INFO.class,0x01F4, idSet);// 2.6 0xF5, 2.7
        addPacketOpcode(SM_ALLIANCE_INFO.class,0x01F5, idSet);// 2.6 0xF9, 2.7
        addPacketOpcode(SM_LEAVE_GROUP_MEMBER.class,0x01F7, idSet);// 2.6 0xF7, 2.7
        addPacketOpcode(SM_ALLIANCE_READY_CHECK.class,0x01F8, idSet);// 2.6 0xF8, 2.7 Testing
        addPacketOpcode(SM_SHOW_BRAND.class,0x01F9, idSet);// 2.6 0xF6, 2.7
		addPacketOpcode(SM_PRICES.class,0x01FA, idSet);// 2.6 0xFA, 2.7
		// 2.7 Unknown - FC 01 54 BB FE 3C 00 00 00  (Directly After CM_LEVEL_READY)
        addPacketOpcode(SM_TRADELIST.class,0x01FD, idSet);// 2.6 0xFD, 2.7
		addPacketOpcode(SM_VERSION_CHECK.class,0x00FE, idSet);// 2.6 0xFE, 2.7
        addPacketOpcode(SM_RECONNECT_KEY.class,0x01FF, idSet);// 2.6 0xFF, 2.7
        addPacketOpcode(SM_CUSTOM_PACKET.class,99999, idSet);// fake packet
    }

    static int getOpcode(Class<? extends AionServerPacket> packetClass)
    {
        Integer opcode = opcodes.get(packetClass);
        if(opcode == null)
            throw new IllegalArgumentException("There is no opcode for " + packetClass + " defined.");

        return opcode;
    }

    private static void addPacketOpcode(Class<? extends AionServerPacket> packetClass, int opcode, Set<Integer> idSet)
    {
        if(opcode < 0)
            return;

        if(idSet.contains(opcode))
            throw new IllegalArgumentException(String.format("There already exists another packet with id 0x%02X",
                    opcode));

        idSet.add(opcode);
        opcodes.put(packetClass, opcode);
    }
}
