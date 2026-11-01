package gg.wzrds.tni.items;

import gg.wzrds.tni.ToolsAndItems;
import gg.wzrds.tni.config.Config;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Magnet extends CustomItem {
    public static final Identifier ID = ToolsAndItems.id("magnet");

    public Identifier id () {
        return ID;
    }

    @Override
    public Item vanillaItem() {
        return Items.MUSIC_DISC_5;
    } // DISC 5 Is used for items that support Unbreaking

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
    public void customize(ItemStack stack) {
        stack.remove(DataComponents.JUKEBOX_PLAYABLE);
        stack.set(DataComponents.MAX_DAMAGE, 2100);
        stack.setDamageValue(0);
    }

    @Override
    public void register() {
        ServerTickEvents.END_SERVER_TICK.register(this::onEndServerTick);
        EnchantmentEvents.ALLOW_ENCHANTING.register((this::onAllowEnchanting));
    }

    private void onEndServerTick(MinecraftServer server) {
        if (server.getTickCount() % 20 != 0) return;
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        for (ServerPlayer player : players) {
            if (player.isSpectator()) continue;
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack stack = player.getItemInHand(hand);
                if (!stackEqualsSelf(stack)) continue;
                AABB aabb = player.getBoundingBox().inflate(5);
                List<ItemEntity> items = player.level().getEntitiesOfClass(ItemEntity.class, aabb);
                for (ItemEntity item : items) {
                    int oldStackCount = item.getItem().getCount();
                    item.playerTouch(player);
                    int newStackCount = item.getItem().getCount();
                    if (item.isRemoved() || newStackCount < oldStackCount) {
                        stack.hurtAndBreak(1, player, hand);
                    }
                }
            }
        }
    }

    private TriState onAllowEnchanting(Holder<Enchantment> enchantment, ItemStack stack, EnchantingContext context) {
        if (!stackEqualsSelf(stack)) return TriState.DEFAULT;
        if (enchantment.is(Enchantments.UNBREAKING)) return TriState.TRUE;
        return TriState.FALSE;
    }
}
