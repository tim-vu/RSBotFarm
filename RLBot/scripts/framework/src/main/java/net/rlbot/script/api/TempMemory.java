package net.rlbot.script.api;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class TempMemory<T> {

    @Getter
    private final Supplier<T> source;

    @Getter
    private final int timeout;

    @Getter
    private T temporaryValue;

    private long lastUpdateMillis;

    public TempMemory(Supplier<T> source, int timeout) {

        this.source = source;
        this.timeout = timeout;
    }

    public void set(T value) {

        this.temporaryValue = value;
        this.lastUpdateMillis = System.currentTimeMillis();
    }

    public T get() {

        T temporaryValue = getTemporaryValue();

        if (temporaryValue != null && System.currentTimeMillis() - this.lastUpdateMillis < this.timeout) {
            return temporaryValue;
        }

        return this.source.get();
    }

}
