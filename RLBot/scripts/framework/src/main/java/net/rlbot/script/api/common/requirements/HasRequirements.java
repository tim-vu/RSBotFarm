package net.rlbot.script.api.common.requirements;

import java.util.Set;

public interface HasRequirements {

    default boolean areRequirementsSatisfied() {
       return getRequirements().stream().allMatch(Requirement::isSatisfied);
    }

    Set<Requirement> getRequirements();

}
