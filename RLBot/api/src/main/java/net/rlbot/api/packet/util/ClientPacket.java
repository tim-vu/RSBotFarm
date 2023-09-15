package net.rlbot.api.packet.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import net.runelite.api.Client;

@AllArgsConstructor
public class ClientPacket {

    private static final String CLIENT_PACKET_NAME = "lw";

    public static Class<?> getObfuscatedClass() {
        return clazz;
    }

    private static Class<?> clazz;

    @SneakyThrows
    public static void init(Client client) {
        clazz = client.getClass().getClassLoader().loadClass(CLIENT_PACKET_NAME);

        MOUSE_CLICK = new ClientPacket(clazz, "bs");
        RESUME_COUNTDIALOG = new ClientPacket(clazz, "dv");
        RESUME_NAMEDIALOG = new ClientPacket(clazz, "ao");
        RESUME_PAUSE = new ClientPacket(clazz, "cs");
        MOVE_GAMECLICK = new ClientPacket(clazz, "bl");

        OPNPC1 = new ClientPacket(clazz, "bm");
        OPNPC2 = new ClientPacket(clazz, "cf");
        OPNPC3 = new ClientPacket(clazz, "du");
        OPNPC4 = new ClientPacket(clazz, "cd");
        OPNPC5 = new ClientPacket(clazz, "bf");
        OPNPCT = new ClientPacket(clazz, "dh");

        OPLOC1 = new ClientPacket(clazz, "dj");
        OPLOC2 = new ClientPacket(clazz, "at");
        OPLOC3 = new ClientPacket(clazz, "bp");

        OPLOC4 = new ClientPacket(clazz, "cz");
        OPLOC5 = new ClientPacket(clazz, "ay");
        OPLOCT = new ClientPacket(clazz, "al");

        OPPLAYER1 = new ClientPacket(clazz, "ad");
        OPPLAYER2 = new ClientPacket(clazz, "bd");
        OPPLAYER3 = new ClientPacket(clazz, "cv");
        OPPLAYER4 = new ClientPacket(clazz, "cu");
        OPPLAYER5 = new ClientPacket(clazz, "an");
        OPPLAYER6 = new ClientPacket(clazz, "bn");
        OPPLAYER7 = new ClientPacket(clazz, "as");
        OPPLAYER8 = new ClientPacket(clazz, "cq");
        OPPLAYERT = new ClientPacket(clazz, "cr");

        OPOBJ1 = new ClientPacket(clazz, "bk");
        OPOBJ2 = new ClientPacket(clazz, "bc");
        OPOBJ3 = new ClientPacket(clazz, "ak");
        OPOBJ4 = new ClientPacket(clazz, "be");
        OPOBJ5 = new ClientPacket(clazz, "cg");
        OPOBJT = new ClientPacket(clazz, "cc");

//        IF_BUTTON1 = new ClientPacket(clazz, "dh");
//        IF_BUTTON2 = new ClientPacket(clazz, "ah");
//        IF_BUTTON3 = new ClientPacket(clazz, "ad");
//        IF_BUTTON4 = new ClientPacket(clazz, "bv");
//        IF_BUTTON5 = new ClientPacket(clazz, "bs");
//        IF_BUTTON6 = new ClientPacket(clazz, "dx");
//        IF_BUTTON7 = new ClientPacket(clazz, "cq");
//        IF_BUTTON8 = new ClientPacket(clazz, "av");
//        IF_BUTTON9 = new ClientPacket(clazz, "bq");
//        IF_BUTTON10 = new ClientPacket(clazz, "bx");
        IF_BUTTONT = new ClientPacket(clazz, "ar");
    }

    public static ClientPacket MOUSE_CLICK;

    public static ClientPacket RESUME_COUNTDIALOG;

    public static ClientPacket RESUME_NAMEDIALOG;

    public static ClientPacket RESUME_PAUSE;

    public static ClientPacket MOVE_GAMECLICK;

    public static ClientPacket OPNPC1;

    public static ClientPacket OPNPC2;

    public static ClientPacket OPNPC3;

    public static ClientPacket OPNPC4;

    public static ClientPacket OPNPC5;

    public static ClientPacket OPNPCT;

    public static ClientPacket OPLOC1;

    public static ClientPacket OPLOC2;

    public static ClientPacket OPLOC3;

    public static ClientPacket OPLOC4;

    public static ClientPacket OPLOC5;

    public static ClientPacket OPLOCT;

    public static ClientPacket OPPLAYER1;

    public static ClientPacket OPPLAYER2;

    public static ClientPacket OPPLAYER3;

    public static ClientPacket OPPLAYER4;

    public static ClientPacket OPPLAYER5;

    public static ClientPacket OPPLAYER6;

    public static ClientPacket OPPLAYER7;

    public static ClientPacket OPPLAYER8;

    public static ClientPacket OPPLAYERT;

    public static ClientPacket OPOBJ1;

    public static ClientPacket OPOBJ2;

    public static ClientPacket OPOBJ3;

    public static ClientPacket OPOBJ4;

    public static ClientPacket OPOBJ5;

    public static ClientPacket OPOBJT;

    public static ClientPacket IF_BUTTON1;

    public static ClientPacket IF_BUTTON2;

    public static ClientPacket IF_BUTTON3;

    public static ClientPacket IF_BUTTON4;

    public static ClientPacket IF_BUTTON5;

    public static ClientPacket IF_BUTTON6;

    public static ClientPacket IF_BUTTON7;

    public static ClientPacket IF_BUTTON8;

    public static ClientPacket IF_BUTTON9;

    public static ClientPacket IF_BUTTON10;

    public static ClientPacket IF_BUTTONT;


    @Getter
    private Object instance;

    @SneakyThrows
    public ClientPacket(Class<?> clientPacketClass, String fieldName) {
        var field = clientPacketClass.getDeclaredField(fieldName);
        this.instance = field.get(null);
    }
}
