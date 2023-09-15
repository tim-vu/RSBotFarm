package net.rlbot.api.widgets;

import net.rlbot.internal.ApiContext;
import net.rlbot.api.common.Time;
import net.runelite.api.WorldType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class World {

    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    private final net.runelite.api.World world;

    public  World(net.runelite.api.World world) {
        this.world = world;
    }

    net.runelite.api.World getWorld() {
        return this.world;
    }

    public int getId() {
        return this.world.getId();
    }

    public boolean isMembers() {
        return this.world.getTypes().contains(WorldType.MEMBERS);
    }

    public boolean isBounty() {
        return this.world.getTypes().contains(WorldType.BOUNTY);
    }

    public boolean isDeadman() {
        return this.world.getTypes().contains(WorldType.DEADMAN);
    }

    public boolean isHighRisk() {
        return this.world.getTypes().contains(WorldType.HIGH_RISK);
    }

    public boolean isPVP() {
        return this.world.getTypes().contains(WorldType.PVP);
    }

    public boolean isSeasonDeadman() {
        return this.world.getTypes().contains(WorldType.SEASONAL);
    }

    public boolean isSkillTotal() {
        return this.world.getTypes().contains(WorldType.SKILL_TOTAL);
    }

    public boolean isTournament() {
        return this.world.getTypes().contains(WorldType.TOURNAMENT_WORLD);
    }

    public boolean isLastManStanding() {
        return this.world.getTypes().contains(WorldType.LAST_MAN_STANDING);
    }

    public Region getLocation() {
        return Region.fromLocationId(this.world.getLocation());
    }

    public enum Region {

        US(0),
        EU(1, 7);

        public Set<Integer> getLocationIds(){
            return Collections.unmodifiableSet(locationIds);
        }

        private final Set<Integer> locationIds;

        Region(int... locationIds){
            this.locationIds = new HashSet<>();

            for(int regionId : locationIds){
                this.locationIds.add(regionId);
            }
        }

        public static Region fromLocationId(int locationId) {
            for(var region : Region.values()) {

                if(!region.getLocationIds().contains(locationId)) {
                    continue;
                }

                return region;
            }

            return null;
        }
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        World other = (World) object;

        return getId() == other.getId();
    }

    @Override
    public int hashCode() {

        return Integer.hashCode(getId());
    }

}
