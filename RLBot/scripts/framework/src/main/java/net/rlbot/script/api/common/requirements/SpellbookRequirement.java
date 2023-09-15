package net.rlbot.script.api.common.requirements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.rlbot.api.magic.SpellBook;

@AllArgsConstructor
@ToString
public class SpellbookRequirement implements Requirement {

    @Getter
    private final SpellBook book;

    @Override
    public boolean isSatisfied() {
        return SpellBook.getCurrent() == this.book;
    }
}
