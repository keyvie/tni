package gg.wzrds.tools.items;

import gg.wzrds.tools.CustomItems;
import gg.wzrds.tools.Tools;
import gg.wzrds.tools.config.Config;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.AABB;

import java.util.List;

public final class Magnet {
    public static final Identifier ID = Tools.id("magnet");

    public static void register() {
        CustomItems.register(ID, Magnet::create);
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            List<ServerPlayer> players = server.getPlayerList().getPlayers();
            for (ServerPlayer player : players) {
                if (player.isSpectator()) continue;
                ItemStack mainHand = player.getMainHandItem();
                ItemStack offHand = player.getOffhandItem();
                boolean inMainHand = CustomItems.is(mainHand, ID);
                boolean inOffHand = CustomItems.is(offHand, ID);
                if (!inMainHand && !inOffHand) {
                    continue;
                }
                AABB aabb = player.getBoundingBox().inflate(5);
                List<ItemEntity> items = player.level().getEntitiesOfClass(ItemEntity.class, aabb);
                for (ItemEntity item : items) {
                    item.playerTouch(player);
                }
            }
        });
    }

    public static ItemStack create(ServerLevel level) {
        ItemStack stack = new ItemStack(Items.HEART_OF_THE_SEA);

        stack.set(
            DataComponents.ITEM_NAME,
            Component.literal("Magnet")
        );
        stack.setDamageValue(0);
        stack.setCount(1);
        stack.set(DataComponents.MAX_STACK_SIZE, 1);

        stack.set(
            DataComponents.ITEM_MODEL,
            Identifier.fromNamespaceAndPath(
                Config.get().resource_pack_namespace,
                "magnet"
            )
        );

        // Mark it as a Magnet Item...
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(ID.toString(), true);
        CustomItems.addTag(tag);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }


}
