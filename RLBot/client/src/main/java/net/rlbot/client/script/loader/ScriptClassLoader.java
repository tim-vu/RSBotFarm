package net.rlbot.client.script.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ScriptClassLoader extends URLClassLoader {

    private final ClassLoader parent;

    public ScriptClassLoader(File file, ClassLoader parent) throws MalformedURLException {
        super(new URL[]{ file.toURI().toURL()}, null);
        this.parent = parent;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {

        try {
            return super.loadClass(name);
        } catch (ClassNotFoundException e) {
            return parent.loadClass(name);
        }

    }

}
