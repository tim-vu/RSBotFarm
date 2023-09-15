package net.rlbot.internal.menu;

import lombok.NonNull;
import lombok.SneakyThrows;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.internal.ApiContext;

import java.lang.reflect.Method;

public class Menu {

    private static ApiContext API_CONTEXT;

    private static final String INVOKE_MENU_ACTION_CLASS_NAME = "bx";

    private static final String INVOKE_MENU_ACTION_METHOD_NAME = "ly";

    private static Method INVOKE_MENU_ACTION_METHOD;

    @SneakyThrows
    private static void init(@NonNull ApiContext apiContext) {
        API_CONTEXT = apiContext;

        var clazz = apiContext.getClient().wrappedClient.getClass().getClassLoader().loadClass(INVOKE_MENU_ACTION_CLASS_NAME);
        INVOKE_MENU_ACTION_METHOD = clazz.getDeclaredMethod(INVOKE_MENU_ACTION_METHOD_NAME, int.class, int.class, int.class, int.class, String.class, int.class);
    }

    private static boolean invokeWidgetDefaultMenuActionMethod(int identifier, int param0, int param1, int itemId, String target) {
        return API_CONTEXT.getClient().runOnClientThread(() -> {
            INVOKE_MENU_ACTION_METHOD.setAccessible(true);
            try {
                INVOKE_MENU_ACTION_METHOD.invoke(null, identifier, param0, param1, itemId, target, -49);
                return true;
            }
            catch(Exception ex) {
                return false;
            }
            finally {
                INVOKE_MENU_ACTION_METHOD.setAccessible(false);
            }
        });
    }

    @SneakyThrows
    public static boolean invokeWidgetDefaultMenuAction(int identifier, int param0, int param1, int itemId) {

        if (itemId != -1)
        {
            return invokeWidgetDefaultMenuActionMethod(identifier, param1, param0, itemId, "");
        }

        var widget = Widgets.get(param1);
        if (widget == null)
        {
            return false;
        }

        var child = param0 == -1 ? null : widget.getChild(param0);
        if (child == null)
        {
            return invokeWidgetDefaultMenuActionMethod(identifier, param1, param0, -1, "");
        }

        return invokeWidgetDefaultMenuActionMethod(identifier, param1, param0, child.getItemId(), "");
    }
}