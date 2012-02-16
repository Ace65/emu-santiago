/*
 * This file is part of Aion Extreme Emulator <aion-core.net>.
 *
 *  aion extreme emulator is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion extreme emulator is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.network.aion.clientpackets;

import gameserver.network.aion.AionClientPacket;

/**
 * @author zer0patches
 *
 */
public class CM_TWITTER_ADDON extends AionClientPacket {
        /**
        * Constructs new instance of <tt>CM_TWITTER_ADDON </tt> packet
        *
        * @param opcode
        */
        public CM_TWITTER_ADDON(int opcode)
        {
            super(opcode);
        }

        /**
        * {@inheritDoc}
        */
        @Override
        protected void readImpl()
        {
        }

        /**
        * {@inheritDoc}
        */
        @Override
        protected void runImpl()
        {
        }
    }
