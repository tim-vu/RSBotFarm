package net.rlbot.api.packet.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import net.runelite.api.Client;

@AllArgsConstructor
public class IsaacCipher {

    private static final String ISAAC_CIPHER = "ux";

    public static Class<?> getObfuscatedClass() {
        return clazz;
    }

    private static Class<?> clazz;

    @SneakyThrows
    public static void init(Client client) {
        clazz = client.getClass().getClassLoader().loadClass(ISAAC_CIPHER);
    }

    @Getter
    private final Object instance;



}
