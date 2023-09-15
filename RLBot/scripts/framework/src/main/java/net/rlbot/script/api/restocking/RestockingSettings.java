package net.rlbot.script.api.restocking;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@FieldDefaults(makeFinal = true)
@EqualsAndHashCode
@Builder
@Getter
public class RestockingSettings {

    boolean sellBeforeBuy;

    boolean sellItems;

    Set<Tradeable> tradeables;

}
