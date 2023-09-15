package net.rlbot.internal.managers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.internal.wrapper.ClientImpl;
import net.runelite.api.InventoryID;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Singleton
public class InventoryManager {

    @Getter
    private final Map<Integer, Item[]> cachedItemContainers = new HashMap<>();

    private final ClientImpl client;

    @Inject
    public InventoryManager(EventBus eventBus, ClientImpl client) {
        eventBus.register(this);
        this.client = client;
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged e) {

        var items = new HashSet<Item>(e.getItemContainer().count());

        for(var i = 0; i < e.getItemContainer().size(); i++) {
            var item = e.getItemContainer().getItem(i);

            if(item == null || item.getId() == -1) {
                continue;
            }

            items.add(new Item(item, i));
        }

        cachedItemContainers.put(e.getContainerId(), items.toArray(new Item[0]));

        if(e.getContainerId() == InventoryID.INVENTORY.getId()) {
            client.runScript(6009, 9764864, 28, 1, -1);
        }

    }

}
