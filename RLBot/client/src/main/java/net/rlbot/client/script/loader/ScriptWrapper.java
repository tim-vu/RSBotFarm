package net.rlbot.client.script.loader;

import lombok.Getter;
import lombok.SneakyThrows;
import net.rlbot.api.script.Script;
import net.rlbot.api.script.ScriptManifest;

public class ScriptWrapper {

    @SneakyThrows
    public Script newInstance() {
        return scriptClass.getConstructor().newInstance();
    }

    private final Class<? extends Script> scriptClass;

    @Getter
    private final String name;

    @Getter
    private final String author;

    @Getter
    private final double version;

    public ScriptWrapper(Class<? extends Script> scriptClass) {
        this.scriptClass = scriptClass;

        var annotation = scriptClass.getAnnotation(ScriptManifest.class);
        this.name = annotation.name();
        this.author = annotation.author();
        this.version = annotation.version();
    }

}
