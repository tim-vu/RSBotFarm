package net.rlbot.api.movement.pathfinder.model;

import lombok.Getter;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.widgets.WidgetAddress;
import net.rlbot.api.widgets.WidgetInfo;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.api.game.Vars;
import net.rlbot.api.movement.Position;
import net.runelite.api.Varbits;

@Getter
public enum FairyRingLocation
{
    AIR("AIR", new Position(2699, 3249, 0)),
    AIQ("AIQ", new Position(2995, 3112, 0)),
    AJR("AJR", new Position(2779, 3615, 0)),
    AJS("AJS", new Position(2499, 3898, 0)),
    AKP("AKP", new Position(3283, 2704, 0)),
    AKQ("AKQ", new Position(2318, 3617, 0)),
    AKS("AKS", new Position(2570, 2958, 0)),
    ALP("ALP", new Position(2502, 3638, 0)),
    ALQ("ALQ", new Position(3598, 3496, 0)),
    ALS("ALS", new Position(2643, 3497, 0)),
    BIP("BIP", new Position(3409, 3326, 0)),
    BIQ("BIQ", new Position(3248, 3095, 0)),
    BIS("BIS", new Position(2635, 3268, 0)),
    BJP("BJP", new Position(2264, 2976, 0)),
    BJS("BJS", new Position(2150, 3071, 0)),
    BKP("BKP", new Position(2384, 3037, 0)),
    BKR("BKR", new Position(3468, 3433, 0)),
    BLP("BLP", new Position(2432, 5127, 0)),
    BLR("BLR", new Position(2739, 3353, 0)),
    CIP("CIP", new Position(2512, 3886, 0)),
    CIR("CIR", new Position(1303, 3762, 0)),
    CIQ("CIQ", new Position(2527, 3129, 0)),
    CJR("CJR", new Position(2704, 3578, 0)),
    CKR("CKR", new Position(2800, 3003, 0)),
    CKS("CKS", new Position(3446, 3472, 0)),
    CLP("CLP", new Position(3081, 3208, 0)),
    CLS("CLS", new Position(2681, 3083, 0)),
    DIP("DIP", new Position(3039, 4757, 0)),
    DIS("DIS", new Position(3109, 3149, 0)),
    DJP("DJP", new Position(2658, 3229, 0)),
    DJR("DJR", new Position(1452, 3659, 0)),
    DKP("DKP", new Position(2899, 3113, 0)),
    DKR("DKR", new Position(3126, 3496, 0)),
    DKS("DKS", new Position(2743, 3721, 0)),
    DLQ("DLQ", new Position(3422, 3018, 0)),
    DLR("DLR", new Position(2212, 3101, 0)),
    CIS("CIS", new Position(1638, 3868, 0)),
    CLR("CLR", new Position(2737, 2739, 0)),
    ZANARIS("Zanaris", new Position(2411, 4436, 0));

    private final String code;
    private final Position position;

    FairyRingLocation(String code, Position position)
    {
        this.code = code;
        this.position = position;
    }

    private static final int[][] TURN_INDICES = {{19, 20}, {21, 22}, {23, 24}};
    public static final WidgetAddress CONFIRM_WIDGET = new WidgetAddress(WidgetInfo.FAIRY_RING.getGroupId(), 26);
    private static final String[][] CODES = {{"A", "D", "C", "B"}, {"I", "L", "K", "J"}, {"P", "S", "R", "Q"}};

    public boolean validateCode()
    {
        return getCurrentCode().equalsIgnoreCase(this.code);
    }

    public boolean setCode()
    {
        String currentCode = getCurrentCode();
        if (currentCode.charAt(0) != this.code.charAt(0))
        {
            var widget = Widgets.get(WidgetInfo.FAIRY_RING.getGroupId(), TURN_INDICES[0][0]);

            if(widget == null) {
                return false;
            }

            return widget.interact(Predicates.always());
        }

        if (currentCode.charAt(1) != this.code.charAt(1))
        {
            var widget = Widgets.get(WidgetInfo.FAIRY_RING.getGroupId(), TURN_INDICES[1][0]);

            if(widget == null) {
                return false;
            }

            return widget.interact(Predicates.always());
        }

        if (currentCode.charAt(2) != this.code.charAt(2))
        {
            var widget = Widgets.get(WidgetInfo.FAIRY_RING.getGroupId(), TURN_INDICES[2][0]);

            if(widget == null) {
                return false;
            }

            return widget.interact(Predicates.always());
        }

        return false;
    }

    public boolean travel()
    {
        if (getCurrentCode().equalsIgnoreCase(getCode()))
        {
            var widget = CONFIRM_WIDGET.resolve();

            if(widget == null) {
                return false;
            }

            //TODO: Verify if this works
            return widget.interact(Predicates.always())  &&
                    Time.sleepUntil(() -> getPosition().distance() < 5, 2400);
//            CONFIRM_WIDGET.get().interact(1, MenuAction.CC_OP.getId(), -1, 26083354);
        }

        return setCode();
    }

    public static String getCurrentCode()
    {
        return CODES[0][Vars.getBit(Varbits.FAIRY_RING_DIAL_ADCB)]
                + CODES[1][Vars.getBit(Varbits.FAIRY_RIGH_DIAL_ILJK)]
                + CODES[2][Vars.getBit(Varbits.FAIRY_RING_DIAL_PSRQ)];
    }

}
