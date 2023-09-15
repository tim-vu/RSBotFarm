package tasks.agility.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tasks.agility.behaviour.course.Course;

@AllArgsConstructor
public class Context {

    @Getter
    private final Course course;

    @Getter
    private final int foodItemId;
}
