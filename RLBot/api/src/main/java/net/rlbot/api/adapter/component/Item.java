package net.rlbot.api.adapter.component;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.common.BaseItem;
import net.rlbot.api.adapter.common.Interactable;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.adapter.scene.SceneObject;
import net.rlbot.api.definitions.Definitions;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.packet.MousePackets;
import net.rlbot.api.packet.NpcPackets;
import net.rlbot.api.packet.ObjectPackets;
import net.rlbot.api.packet.WidgetPackets;
import net.rlbot.api.widgets.*;
import net.rlbot.internal.ApiContext;
import net.rlbot.internal.Interaction;
import net.rlbot.internal.menu.Menu;
import net.runelite.api.InventoryID;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.WidgetItem;

import java.awt.*;
import java.util.Arrays;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class Item implements BaseItem, Interactable {

	private static ApiContext API_CONTEXT;

	private static void init(@NonNull ApiContext apiContext) {

		API_CONTEXT = apiContext;
	}

	private final net.runelite.api.Item item;

	@Getter
	@Setter
	private int widgetId;

	@Getter
	private final int slot;

	private final ItemDefinition def;

	public Item(@NonNull net.runelite.api.Item item, int slot) {

		this.item = item;
		this.slot = slot;
		this.def = Definitions.getItemDefinition(item.getId());
	}

	public int getId() {

		return this.item.getId();
	}

	public int getNotedId() {
		return this.def.getNotedId();
	}

	@Override
	public int getQuantity() {

		return this.item.getQuantity();
	}

	public String getName() {

		return this.def.getName();
	}

	public boolean isPlaceholder() {

		return this.def.getPlaceholderTemplateId() > - 1;
	}

	public boolean isTradeable() {
		return this.def.isTradeable();
	}

	public boolean isStackable() {
		return this.def.isStackable();
	}

	public boolean isMembers() {
		return this.def.isMembers();
	}

	public boolean isNoted() {
		return this.def.isNoted();
	}

	public int getStorePrice() {
		return this.def.getPrice();
	}

	private static final int BASE_ACTION_PARAM = 451;
	private static final int MAX_CUSTOM_ACTIONS = 8;

	@Override
	public String[] getActions() {

		if (getType() == Type.EQUIPMENT)
		{
			String[] actions = new String[8];
			actions[0] = "Remove";

			int index = 1;
			for (var i = 0; i < 7; i++)
			{
				actions[index++] = this.def.getCustomActions()[i];
			}
			return actions;
		}

		var widget = Widgets.get(widgetId);

		if (widget != null)
		{
			var itemChild = widget.getChild(slot);

			if (itemChild != null)
			{
				return itemChild.getActions();
			}

			return widget.getActions();
		}

		return null;
	}

	@Override
	public boolean interact(int index) {


		if(index < 0 || index > 9) {
			return false;
		}

		int num = index + 1;

		var slot = getType() == Type.EQUIPMENT ? -1 : this.slot;

		Interaction.log("WIDGET", kv("menuIndex", index), kv("itemId", getId()));
		MousePackets.queueClickPacket();
		return Menu.invokeWidgetDefaultMenuAction(num, slot, widgetId, getId());
	}

	public boolean useOn(Interactable interactable) {

		if(interactable instanceof SceneObject sceneObject) {
			return useOn(sceneObject);
		}

		if(interactable instanceof Item itm) {
			return useOn(itm);
		}

		if(interactable instanceof Npc npc) {
			return useOn(npc);
		}

		return false;
	}

	public boolean useOn(SceneObject sceneObject) {

		if(getType() != Type.INVENTORY) {
			throw new IllegalStateException("useOn is only valid for inventory items");
		}

		var p = sceneObject.getMenuPoint();
		LocalPoint lp = new LocalPoint(p.x, p.y);
		WorldPoint wp = WorldPoint.fromScene(API_CONTEXT.getClient(), lp.getX(), lp.getY(), sceneObject.getPlane());

		Interaction.log("USE_ITEM_ON_OBJECT", kv("itemId", getId()), kv("objectId", sceneObject.getId()));
		MousePackets.queueClickPacket();
		ObjectPackets.queueWidgetOnObject(sceneObject.getId(), wp.getX(), wp.getY(), this.slot, getId(), this.widgetId, false);
		return true;
	}

	public boolean useOn(@NonNull Item item) {

		if(getType() != Type.INVENTORY || item.getType() != Type.INVENTORY) {
			throw new IllegalStateException("useOn is only valid for inventory items");
		}

		Interaction.log("USE_ITEM_ON_ITEM", kv("itemId", getId()), kv("itemId", item.getId()));
		MousePackets.queueClickPacket();
		WidgetPackets.queueWidgetOnWidget(this.widgetId, this.slot, getId(), item.widgetId, item.slot, item.getId());
		return true;
	}

	public boolean useOn(@NonNull Npc npc) {

		if(getType() != Type.INVENTORY) {
			throw new IllegalStateException("useOn is only valid for inventory items");
		}

		Interaction.log("USE_ITEM_ON_NPC", kv("itemId", getId()), kv("npcId", npc.getId()));
		MousePackets.queueClickPacket();
		NpcPackets.queueWidgetOnNpc(npc.getIndex(), this.slot, this.getId(), this.widgetId, false);
		return true;
	}

	public int calculateWidgetId(net.rlbot.api.adapter.component.Widget containerWidget)
	{
		if (containerWidget == null)
		{
			return -1;
		}

		var children = containerWidget.getChildren();
		if (children == null)
		{
			return -1;
		}

		return Arrays.stream(children)
				.filter(x -> x.getItemId() == getId()).findFirst()
				.map(net.rlbot.api.adapter.component.Widget::getId)
				.orElse(-1);
	}

	private static final Map<Type, Tab> TYPE_TO_TAB = Map.ofEntries(
			Map.entry(Type.INVENTORY, Tab.INVENTORY),
			Map.entry(Type.EQUIPMENT, Tab.EQUIPMENT)
	);

	private static void openTab(Type type) {

		var tab = TYPE_TO_TAB.getOrDefault(type, null);

		if(tab == null) {
			return;
		}

		if(Tabs.isOpen(tab)) {
			return;
		}

		Tabs.open(tab);
	}

	private Rectangle getBounds() {

		var widget = API_CONTEXT.getClient().getWidget(this.widgetId);

		if (widget == null) {
			return null;
		}

		if (getType() != Type.EQUIPMENT) {
			var slot = widget.getChild(getSlot());
			if (slot != null) {
				return slot.getBounds() != null ? slot.getBounds() : null;
			}
		}

		Rectangle bounds = widget.getBounds();

		if (bounds == null) {
			Rectangle itemBounds = getWidgetItem(widget, getSlot()).getCanvasBounds();
			return itemBounds != null ? itemBounds : new Rectangle(-1, -1, 0, 0);
		}

		return bounds;
	}

	private static final int ITEM_SLOT_SIZE = 32;

	private WidgetItem getWidgetItem(net.runelite.api.widgets.Widget widget, int index) {

		var child = widget.getDynamicChildren()[index];

		Rectangle bounds = child.getBounds();
		bounds.setBounds(bounds.x - 1, bounds.y - 1, ITEM_SLOT_SIZE, ITEM_SLOT_SIZE);
		Rectangle dragBounds = child.getBounds();
		dragBounds.setBounds(bounds.x, bounds.y, ITEM_SLOT_SIZE, ITEM_SLOT_SIZE);
		return new WidgetItem(child.getItemId(), child.getItemQuantity(), bounds, child, dragBounds);
	}

	public Type getType() {

		return Type.get(widgetId);
	}

	public enum Type {
		INVENTORY(InventoryID.INVENTORY),
		EQUIPMENT(InventoryID.EQUIPMENT),
		BANK(InventoryID.BANK),
		BANK_INVENTORY(InventoryID.INVENTORY),
		TRADE(InventoryID.TRADE),
		TRADE_INVENTORY(InventoryID.INVENTORY),
		UNKNOWN(null);

		private final InventoryID inventoryID;

		Type(InventoryID inventoryID) {

			this.inventoryID = inventoryID;
		}

		private static Type get(int widgetId) {

			return switch (WidgetInfo.TO_GROUP(widgetId)) {
				case WidgetID.PLAYER_TRADE_SCREEN_GROUP_ID -> TRADE;
				case WidgetID.PLAYER_TRADE_INVENTORY_GROUP_ID -> TRADE_INVENTORY;
				case WidgetID.EQUIPMENT_GROUP_ID -> EQUIPMENT;
				case WidgetID.BANK_GROUP_ID -> BANK;
				case WidgetID.BANK_INVENTORY_GROUP_ID -> BANK_INVENTORY;
				case WidgetID.INVENTORY_GROUP_ID -> INVENTORY;
				default -> UNKNOWN;
			};
		}

		public InventoryID getInventoryID() {

			return inventoryID;
		}
	}
}
