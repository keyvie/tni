package gg.wzrds.tni.items;

import gg.wzrds.tni.ToolsAndItems;
import gg.wzrds.tni.config.Config;
import gg.wzrds.tni.enchantments.InteractionReach;
import gg.wzrds.tni.enchantments.Swiftness;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;


public class BookOfReach extends CustomItem {
    public static final Identifier ID = ToolsAndItems.id("book_of_reach");

    public Identifier id () {
        return ID;
    }

    @Override
    public Item vanillaItem() {
        return Items.MUSIC_DISC_5;
    }

    @Override
    public String name() {
        return "Book of Reach";
    }

    @Override
    public Identifier itemModel() {
        return Identifier.fromNamespaceAndPath(
            Config.get().resource_pack_namespace,
            "book_of_reach"
        );
    }

    @Override
    public void customize(ItemStack stack) {
        stack.remove(DataComponents.JUKEBOX_PLAYABLE);
        stack.set(DataComponents.MAX_DAMAGE, 1000);
        stack.setDamageValue(0);
        stack.enchant(InteractionReach.get(ToolsAndItems.getServer().registryAccess()), 1);
    }

    public void register() {
        EnchantmentEvents.ALLOW_ENCHANTING.register((this::onAllowEnchanting));
    }

    private TriState onAllowEnchanting(Holder<Enchantment> enchantment, ItemStack stack, EnchantingContext context) {
        if (!stackEqualsSelf(stack)) return TriState.DEFAULT;
        if (enchantment.is(Enchantments.UNBREAKING)) return TriState.TRUE;
        return TriState.FALSE;
    }
}
