package net.rlbot.script.api.tree.behaviourtree;

import lombok.NonNull;
import net.rlbot.script.api.tree.behaviourtree.condition.ConditionNode;
import net.rlbot.script.api.tree.behaviourtree.decorator.AlwaysSucceed;
import net.rlbot.script.api.tree.behaviourtree.decorator.Inverter;
import net.rlbot.script.api.tree.behaviourtree.decorator.RepeatUntil;
import net.rlbot.script.api.tree.behaviourtree.decorator.RepeatWhile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

abstract class ParentNodeBuilder<TBuilder extends ParentNodeBuilder<?>> {

    protected final List<Function<BTNode, BTNode>> decorators;

    protected ParentNodeBuilder() {
        this.decorators = new ArrayList<>();
    }

    public TBuilder inverted() {
        this.decorators.add(Inverter::new);
        return (TBuilder) this;
    }

    public TBuilder repeatWhile(@NonNull ConditionNode condition) {
        this.decorators.add(n -> new RepeatWhile(condition, n));
        return (TBuilder) this;
    }

    public TBuilder repeatUntil(@NonNull ConditionNode condition) {
        this.decorators.add(n -> new RepeatUntil(condition, n));
        return (TBuilder) this;
    }

    public TBuilder alwaysSucceed() {
        this.decorators.add(AlwaysSucceed::new);
        return (TBuilder) this;
    }

    public TBuilder decorate(@NonNull Function<BTNode, BTNode> decorate) {
        this.decorators.add(decorate);
        return (TBuilder) this;
    }

    protected BTNode applyDecorators(BTNode node) {
        var result = node;

        for (var i = decorators.size() - 1; i >= 0; i--) {
            result = decorators.get(i).apply(result);
        }

        return result;
    }

    void onChildNodeBuilt(BTNode node) {

    };

}
