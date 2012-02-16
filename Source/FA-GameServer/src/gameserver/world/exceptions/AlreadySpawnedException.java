/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.world.exceptions;

/**
 * This exception will be thrown when object will be spawned more than one time (without despawning)
 * 
 * @author -Nemesiss-
 * 
 */
@SuppressWarnings("serial")
public class AlreadySpawnedException extends RuntimeException
{
	/**
	 * Constructs an <code>AlreadySpawnedException</code> with no detail message.
	 */
	public AlreadySpawnedException()
	{
		super();
	}

	/**
	 * Constructs an <code>AlreadySpawnedException</code> with the specified detail message.
	 * 
	 * @param s
	 *            the detail message.
	 */
	public AlreadySpawnedException(String s)
	{
		super(s);
	}

	/**
	 * Creates new error
	 * 
	 * @param message
	 *            exception description
	 * @param cause
	 *            reason of this exception
	 */
	public AlreadySpawnedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Creates new error
	 * 
	 * @param cause
	 *            reason of this exception
	 */
	public AlreadySpawnedException(Throwable cause)
	{
		super(cause);
	}
}