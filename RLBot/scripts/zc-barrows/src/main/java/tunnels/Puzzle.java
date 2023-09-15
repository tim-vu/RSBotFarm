package tunnels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.rlbot.api.adapter.component.Widget;
import net.rlbot.api.widgets.WidgetInfo;
import net.rlbot.api.widgets.Widgets;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
@FieldDefaults(makeFinal = true)
public class Puzzle {

    public static boolean isOpen() {
        return Widgets.isVisible(WidgetInfo.BARROWS_FIRST_PUZZLE);
    }

    private static final List<Integer> ANSWER_MODEL_COMPONENT_IDS = Arrays.asList(13, 15, 17);

    private static final List<WidgetInfo> POSSIBLE_SOLUTIONS = List.of(
            WidgetInfo.BARROWS_PUZZLE_ANSWER1,
            WidgetInfo.BARROWS_PUZZLE_ANSWER2,
            WidgetInfo.BARROWS_PUZZLE_ANSWER3
    );

    public static Widget getSolutionWidget() {

        if (!isOpen()) {
            return null;
        }

        var answer = Widgets.get(WidgetInfo.BARROWS_FIRST_PUZZLE).getModelId() - 3;

        for(var widgetInfo : POSSIBLE_SOLUTIONS) {

            var widget = Widgets.get(widgetInfo);

            if(widget == null) {
                continue;
            }

            if(widget.getModelId() != answer) {
                continue;
            }

            return widget;
        }

        return null;
    }
}

