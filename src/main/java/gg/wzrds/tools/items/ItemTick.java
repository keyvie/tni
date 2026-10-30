package gg.wzrds.tools.items;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.List;
import java.util.Optional;

public class ItemTick {
    private static CustomItem getItemFromStack(ItemStack itemStack) {
        CustomData data = itemStack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return null;
        CompoundTag tag = data.copyTag();
        if (!tag.getBooleanOr("custom_item", false)) return null;
        Optional<String> stringId = tag.getString("custom_id");
        if (stringId.isEmpty()) return null;
        Identifier id = Identifier.tryParse(stringId.get());
        return CustomItems.registry.get(id);
    }

    private static boolean shouldTick(CustomItem item, int tickCount) {
        return item.ticks().contains(tickCount % 20);
    }

    public static void tickInHand(MinecraftServer server, ServerPlayer player, ItemStack stack, int tickCount) {
        if (player.isSpectator()) return;
        if (stack.isEmpty()) return;
        CustomItem item = getItemFromStack(stack);
        if (item == null) return;
        if (!shouldTick(item, tickCount)) return;
        item.onHoldingInHand(server, player, stack);
    }

    public static void tick(MinecraftServer server) {
        int tickCount = server.getTickCount();
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        for (ServerPlayer player : players) {
            ItemStack mainHandStack = player.getMainHandItem();
            tickInHand(server, player, mainHandStack, tickCount);
            ItemStack offHandStack = player.getOffhandItem();
            tickInHand(server, player, offHandStack, tickCount);
        }
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(ItemTick::tick);
    }
}
