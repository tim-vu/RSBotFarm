package net.rlbot.client.script.loader;

import com.google.common.reflect.ClassPath;
import com.google.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.script.Script;
import net.rlbot.api.script.ScriptManifest;
import net.rlbot.client.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class ScriptLoader {

    public Set<ScriptWrapper> getScripts() {

        if(!this.loaded) {
            throw new IllegalStateException("scripts not loaded");
        }

        return Collections.unmodifiableSet(scripts);
    }

    private final Set<ScriptWrapper> scripts;

    @Getter
    private boolean loaded;

    public ScriptLoader() {
        this.scripts = new HashSet<>();
    }

    public void loadScripts() {

        this.scripts.clear();

        var scriptFiles = Configuration.SCRIPT_DIR.listFiles();

        if(scriptFiles == null) {
            return;
        }

        for(var file : scriptFiles) {

            if(!file.getName().endsWith(".jar")) {
                continue;
            }

            try {

                var classLoader = new ScriptClassLoader(file, getClass().getClassLoader());

                Set<Class<?>> scriptClasses = ClassPath.from(classLoader)
                        .getAllClasses()
                        .stream()
                        .map(ClassPath.ClassInfo::load)
                        .collect(Collectors.toSet());

                loadScript(scriptClasses);

            } catch (Exception e) {
                log.error("error loading script", e);
            }
        }

        this.loaded = true;
    }

    @SuppressWarnings("unchecked")
    private void loadScript(Set<Class<?>> scriptClasses) {

        var scriptClazz = scriptClasses.stream()
                .filter(c -> c.getAnnotation(ScriptManifest.class) != null)
                .filter(Script.class::isAssignableFrom)
                .map(c -> (Class<? extends Script>)c)
                .findFirst()
                .orElse(null);

        if(scriptClazz == null) {
            return;
        }

        scripts.add(new ScriptWrapper(scriptClazz));
    }

}
