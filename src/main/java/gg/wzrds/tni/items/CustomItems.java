package gg.wzrds.tni.items;

import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;


public class CustomItems {
    public static Map<Identifier, CustomItem> registry = new HashMap<>();
    private static <T extends CustomItem> T register(T item) {
        registry.put(item.id(), item);
        return item;
    }

    public static void register() {
        ASCENDANT_BOOTS.register();
        BOOK_OF_REACH.register();
        MAGNET.register();
    }

    public static final AscendantBoots ASCENDANT_BOOTS = register(new AscendantBoots());
    public static final BookOfReach BOOK_OF_REACH = register(new BookOfReach());
    public static final Magnet MAGNET = register(new Magnet());
}
