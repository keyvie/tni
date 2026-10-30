package gg.wzrds.tools.items;

import gg.wzrds.tools.Tools;
import gg.wzrds.tools.config.Config;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Magnet extends CustomItem {
    public static final Identifier ID = Tools.id("magnet");

    public Identifier id () {
        return ID;
    }

    @Override
    public Item vanillaItem() {
        return Items.KNOWLEDGE_BOOK;
    }

    @Override
    public String name() {
        return "Magnet";
    }

    @Override
    public Identifier itemModel() {
        return Identifier.fromNamespaceAndPath(
            Config.get().resource_pack_namespace,
            "magnet"
        );
    }

    @Override
    public List<Integer> ticks() {
        return List.of(0, 10);
    }

    @Override
    public void onHoldingInHand(MinecraftServer server, ServerPlayer player, ItemStack stack) {
        AABB aabb = player.getBoundingBox().inflate(5);
        List<ItemEntity> items = player.level().getEntitiesOfClass(ItemEntity.class, aabb);
        for (ItemEntity item : items) {
            item.playerTouch(player);
        }
    }
}
