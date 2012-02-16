/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
 
package gameserver.model.legion;

/**
 * @author Simple, Pashatr
 */
 
public enum LegionRank {

    /**
     * All Legion Ranks *
     */
    BRIGADE_GENERAL(0),
    DEPUTY(1),
    CENTURION(2),
    LEGIONARY(3),
    VOLUNTEER(4);

	/** Static Rights Information
    ----------------------------------
    * Static Rights Information for Permissions 1*
    **/
	private static final int	LP_LEGION_WAREHOUSE		= 0x04; // ok
	private static final int	LP_INVITE_TO_LEGION		= 0x08; // ok
	private static final int	LP_KICK_FROM_LEGION		= 0x10; // ok
    private static final int 	LP_COMBINATION_01_23 	= 0x18;
    private static final int 	LP_COMBINATION_01_24 	= 0x0C;
    private static final int 	LP_COMBINATION_01_34 	= 0x14;
    private static final int 	LP_COMBINATION_01_56 	= 0x18;
    private static final int 	LP_COMBINATION_01_234	= 0x1C;

    /**
     * Static Rights Information for Permissions 2*
     */
	private static final int	LP_EDIT_ANNOUNCEMENT	= 0x02; // ok
	private static final int	LP_ARTIFACT				= 0x04; // ok
	private static final int	LP_GATE_GUARDIAN_STONE	= 0x08; // ok
    private static final int 	LP_COMBINATION_02_1567 	= 0x1E;
    private static final int 	LP_COMBINATION_02_15 	= 0x12;
    private static final int	LP_COMBINATION_02_16 	= 0x06;
    private static final int 	LP_COMBINATION_02_17 	= 0x0A;
    private static final int 	LP_COMBINATION_02_56 	= 0x14;
    private static final int 	LP_COMBINATION_02_57 	= 0x18;
    private static final int 	LP_COMBINATION_02_67 	= 0x0C;
    private static final int 	LP_COMBINATION_02_156	= 0x16;
    private static final int 	LP_COMBINATION_02_567	= 0x1C;
	private static final int    LP_STORE_LEGION_WAREHOUSE = 0x10; // ok

	private byte				rank;

	private LegionRank(int rank)
	{
		this.rank = (byte) rank;
	}

	/**
	 * Returns client-side id for this
	 * 
	 * @return byte
	 */
	public byte getRankId()
	{
		return this.rank;
	}

    /**
     * @return true if legion member has enough rights for Use Gate Guardian Stone
     */
    public boolean canUseGateGuardianStone(final int deputyPermission2, final int centurionPermission2, final int legionaryPermission2, final int volunteerPermission2) {
        switch (this) {
            /** Legion Member is Deputy **/
            case DEPUTY:
                if (deputyPermission2 == LP_GATE_GUARDIAN_STONE
                        || deputyPermission2 == (LP_COMBINATION_02_1567)
                        || deputyPermission2 == (LP_COMBINATION_02_17)
                        || deputyPermission2 == (LP_COMBINATION_02_57)
                        || deputyPermission2 == (LP_COMBINATION_02_67)
                        || deputyPermission2 == (LP_COMBINATION_02_567))
                    return true;
                break;
            /** Legion Member is Centurion **/
            case CENTURION:
                if (centurionPermission2 == LP_GATE_GUARDIAN_STONE
                        || centurionPermission2 == (LP_COMBINATION_02_1567)
                        || centurionPermission2 == (LP_COMBINATION_02_17)
                        || centurionPermission2 == (LP_COMBINATION_02_57)
                        || centurionPermission2 == (LP_COMBINATION_02_67)
                        || centurionPermission2 == (LP_COMBINATION_02_567))
                    return true;
                break;
            /** Legion Member is Legionary **/
            case LEGIONARY:
                if (legionaryPermission2 == LP_GATE_GUARDIAN_STONE)
                    return true;
                break;
            /** Legion Member is Volunteer **/
            case VOLUNTEER:
                if (volunteerPermission2 == LP_GATE_GUARDIAN_STONE)
                    return true;
                break;
        }
        return false;
    }

    /**
     * @return true if legion member has enough rights for Use Artifact
     */
    public boolean canUseArtifact(final int deputyPermission2, final int centurionPermission2) {
        switch (this) {
            /** Legion Member is Deputy **/
            case DEPUTY:
                if (deputyPermission2 == LP_ARTIFACT
                        || deputyPermission2 == (LP_COMBINATION_02_1567)
                        || deputyPermission2 == (LP_COMBINATION_02_16)
                        || deputyPermission2 == (LP_COMBINATION_02_56)
                        || deputyPermission2 == (LP_COMBINATION_02_67)
                        || deputyPermission2 == (LP_COMBINATION_02_156)
                        || deputyPermission2 == (LP_COMBINATION_02_567))
                    return true;
                break;
            /** Legion Member is Centurion **/
            case CENTURION:
                if (centurionPermission2 == LP_ARTIFACT
                        || centurionPermission2 == (LP_COMBINATION_02_1567)
                        || centurionPermission2 == (LP_COMBINATION_02_16)
                        || centurionPermission2 == (LP_COMBINATION_02_56)
                        || centurionPermission2 == (LP_COMBINATION_02_67)
                        || centurionPermission2 == (LP_COMBINATION_02_156)
                        || centurionPermission2 == (LP_COMBINATION_02_567))
                    return true;
                break;
        }
        return false;
    }

    /**
     * @return true if legion member has enough rights for Edit Announcement
     */
    public boolean canEditAnnouncement(final int deputyPermission2, final int centurionPermission2) {
        switch (this) {
            /** Legion Member is Deputy **/
            case DEPUTY:
                if (deputyPermission2 == LP_EDIT_ANNOUNCEMENT
                        || deputyPermission2 == (LP_COMBINATION_02_1567)
                        || deputyPermission2 == (LP_COMBINATION_02_15)
                        || deputyPermission2 == (LP_COMBINATION_02_16)
                        || deputyPermission2 == (LP_COMBINATION_02_17)
                        || deputyPermission2 == (LP_COMBINATION_02_156))
                    return true;
                break;
            /** Legion Member is Centurion **/
            case CENTURION:
                if (centurionPermission2 == LP_EDIT_ANNOUNCEMENT
                        || centurionPermission2 == (LP_COMBINATION_02_1567)
                        || centurionPermission2 == (LP_COMBINATION_02_15)
                        || centurionPermission2 == (LP_COMBINATION_02_16)
                        || centurionPermission2 == (LP_COMBINATION_02_17)
                        || centurionPermission2 == (LP_COMBINATION_02_156))
                    return true;
                break;
        }
        return false;
    }

    /**
     * @return true if legion member has enough rights for Use Legion Warehouse
     */
    public boolean canUseLegionWarehouse(final int deputyPermission1, final int centurionPermission1, final int legionaryPermission1) {
        switch (this) {
            /** Legion Member is Deputy **/
            case DEPUTY:
                if (deputyPermission1 == LP_LEGION_WAREHOUSE
                        || deputyPermission1 == (LP_COMBINATION_01_24)
                        || deputyPermission1 == (LP_COMBINATION_01_34)
                        || deputyPermission1 == (LP_COMBINATION_01_234))
                    return true;
                break;
            /** Legion Member is Centurion **/
            case CENTURION:
                if (centurionPermission1 == LP_LEGION_WAREHOUSE
                        || centurionPermission1 == (LP_COMBINATION_01_24)
                        || centurionPermission1 == (LP_COMBINATION_01_34)
                        || centurionPermission1 == (LP_COMBINATION_01_234))
                    return true;
                break;
            /** Legion Member is Legionary **/
            case LEGIONARY:
                if (legionaryPermission1 == LP_LEGION_WAREHOUSE)
                    return true;
                break;
        }
        return false;
    }

	/**
	* @return true if legion member has enough rights for Store Items in Legion Warehouse
	*/
    public boolean canStoreLegionWarehouse(final int deputyPermission2, final int centurionPermission2, final int legionaryPermission2, final int volunteerPermission2) {
        switch (this) {
            /** Legion Member is Deputy **/
            case DEPUTY:
                if (deputyPermission2 == LP_STORE_LEGION_WAREHOUSE
                        || deputyPermission2 == (LP_COMBINATION_02_1567)
                        || deputyPermission2 == (LP_COMBINATION_02_15)
                        || deputyPermission2 == (LP_COMBINATION_02_56)
                        || deputyPermission2 == (LP_COMBINATION_02_57)
                        || deputyPermission2 == (LP_COMBINATION_02_67)
                        || deputyPermission2 == (LP_COMBINATION_02_567))
                    return true;
                break;
            /** Legion Member is Centurion **/
            case CENTURION:
                if (centurionPermission2 == LP_STORE_LEGION_WAREHOUSE
                        || centurionPermission2 == (LP_COMBINATION_02_1567)
                        || centurionPermission2 == (LP_COMBINATION_02_15)
                        || centurionPermission2 == (LP_COMBINATION_02_56)
                        || centurionPermission2 == (LP_COMBINATION_02_57)
                        || centurionPermission2 == (LP_COMBINATION_02_67)
                        || centurionPermission2 == (LP_COMBINATION_02_567))
                    return true;
                break;
            /** Legion Member is Legionary **/
            case LEGIONARY:
                if (legionaryPermission2 == LP_STORE_LEGION_WAREHOUSE
                        || legionaryPermission2 == (LP_COMBINATION_02_57))
                    return true;
                break;
            /** Legion Member is Volunteer **/
            case VOLUNTEER:
                if (volunteerPermission2 == LP_STORE_LEGION_WAREHOUSE
                        || volunteerPermission2 == (LP_COMBINATION_02_57))
                    return true;
                break;
        }
        return false;
    }

    /**
     * @return true if legion member has enough rights for Kick from Legion
     */
    public boolean canKickFromLegion(final int deputyPermission1, final int centurionPermission1) {
        switch (this) {
            /** Legion Member is Deputy **/
            case DEPUTY:
                if (deputyPermission1 == LP_KICK_FROM_LEGION
                        || deputyPermission1 == (LP_COMBINATION_01_23)
                        || deputyPermission1 == (LP_COMBINATION_01_34)
                        || deputyPermission1 == (LP_COMBINATION_01_234))
                    return true;
                break;
            /** Legion Member is Centurion **/
            case CENTURION:
                if (centurionPermission1 == LP_KICK_FROM_LEGION
                        || centurionPermission1 == (LP_COMBINATION_01_23)
                        || centurionPermission1 == (LP_COMBINATION_01_34)
                        || centurionPermission1 == (LP_COMBINATION_01_234))
                    return true;
                break;
        }
        return false;
    }

    /**
     * @return true if legion member has enough rights for Invite to Legion
     */
    public boolean canInviteToLegion(int deputyPermission1, int centurionPermission1) {
        switch (this) {
            /** Legion Member is Deputy **/
            case DEPUTY:
                if (deputyPermission1 == LP_INVITE_TO_LEGION
                        || deputyPermission1 == (LP_COMBINATION_01_23)
                        || deputyPermission1 == (LP_COMBINATION_01_24)
                        || deputyPermission1 == (LP_COMBINATION_01_234))
                    return true;
                break;
            /** Legion Member is Centurion **/
            case CENTURION:
                if (centurionPermission1 == LP_INVITE_TO_LEGION
                        || centurionPermission1 == (LP_COMBINATION_01_23)
                        || centurionPermission1 == (LP_COMBINATION_01_24)
                        || centurionPermission1 == (LP_COMBINATION_01_234))
                    return true;
                break;
        }
        return false;
    }
}
