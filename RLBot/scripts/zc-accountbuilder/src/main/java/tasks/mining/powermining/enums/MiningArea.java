package tasks.mining.powermining.enums;

import lombok.Getter;
import net.rlbot.api.movement.position.Area;
import net.rlbot.script.api.common.requirements.CombatLevelRequirement;
import net.rlbot.script.api.common.requirements.HasRequirements;
import net.rlbot.script.api.common.requirements.Requirement;
import tasks.mining.powermining.data.Cluster;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public enum MiningArea implements HasRequirements {


    LUMBRIDGE_SWAMP_EAST_MINE(
            Area.rectangular(3220, 3152, 3232, 3143),
            Set.of(
                    new Cluster(Area.rectangular(3222, 3148, 3223, 3146), 3, Rock.TIN),
                    new Cluster(Area.rectangular(3230, 3148, 3229, 3147), 2, Rock.COPPER),
                    new Cluster(Area.rectangular(3228, 3145, 3229, 3144), 2, Rock.COPPER)
            )
    ),
    VARROCK_EAST_MINE(
            Area.rectangular(3276, 3372, 3295, 3355),
            Set.of(
                    new Cluster(Area.rectangular(3281, 3364, 3282, 3363), 2, Rock.TIN),
                    new Cluster(Area.rectangular(3281, 3364, 3282, 3363), 2, Rock.COPPER),
                    new Cluster(Area.rectangular(3289, 3363, 3290, 3362), 2, Rock.COPPER),
                    new Cluster(Area.rectangular(3287, 3364, 3286, 3365), 2, Rock.COPPER),
                    new Cluster(Area.rectangular(3285, 3369, 3286, 3368), 2, Rock.IRON)
            )
    ),
    AL_KHARID_MINEh(
            Area.rectangular(3290, 3275, 3307, 3319),
            Set.of(
                    new Cluster(Area.rectangular(3294, 3311, 3295, 3309), 3 ,Rock.IRON)
            ),
            Set.of(
                    new CombatLevelRequirement(29)
            )
    ),
    DWARVEN_MINE(
            Area.rectangular(3013, 9838, 3034, 9801),
            Set.of(
                    new Cluster(Area.rectangular(3032, 9826, 3033, 9825), 2, Rock.IRON),
                    new Cluster(Area.rectangular(3036, 9776, 3037, 9775), 2, Rock.IRON)
            ),
            Set.of(
                    new CombatLevelRequirement(65)
            )
    );

    @Getter
    private final Area area;

    public Set<Rock> getRocks(){
        return Collections.unmodifiableSet(this.rocks);
    }

    private final Set<Rock> rocks;

    public Set<Cluster> getClusters(){
        return Collections.unmodifiableSet(this.clusters);
    }

    private final Set<Cluster> clusters;

    @Getter
    private final Set<Requirement> requirements;

    MiningArea(Area area, Set<Cluster> clusters) {
        this(area, clusters, Collections.emptySet());
    }
    MiningArea(Area area, Set<Cluster> clusters, Set<Requirement> requirements){
        this.area = area;
        this.clusters = clusters;
        this.rocks = new HashSet<>();

        for(Cluster cluster : clusters){
            this.rocks.add(cluster.getRock());
        }

        this.requirements = requirements;
    }
}
