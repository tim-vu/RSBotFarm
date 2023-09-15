package net.rlbot.api.script;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ScriptManifest {

	String name();

	String author();

	double version() default 1.0;
}
