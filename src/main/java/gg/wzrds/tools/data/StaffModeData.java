package gg.wzrds.tools.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gg.wzrds.tools.Tools;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StaffModeData extends SavedData {
    public record Position(String dimension ,double x, double y, double z, float yaw, float pitch) {}
    private final Map<UUID, Position> positions = new HashMap<>();

    private static final Codec<Position> CODEC_POSITION = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("dimension").forGetter(Position::dimension),
            Codec.DOUBLE.fieldOf("x").forGetter(Position::x),
            Codec.DOUBLE.fieldOf("y").forGetter(Position::y),
            Codec.DOUBLE.fieldOf("z").forGetter(Position::z),
            Codec.FLOAT.fieldOf("yaw").forGetter(Position::yaw),
            Codec.FLOAT.fieldOf("pitch").forGetter(Position::pitch)
        ).apply(instance, Position::new);
    });

    private static final Codec<StaffModeData> CODEC =
        Codec.unboundedMap(Codec.STRING, CODEC_POSITION).xmap(
            map -> {
                StaffModeData data = new StaffModeData();
                map.forEach((uuid, position) ->
                        data.positions.put(UUID.fromString(uuid), position)
                );
                return data;
            },
            data -> {
                Map<String, Position> map = new HashMap<>();
                data.positions.forEach((uuid, position) ->
                        map.put(uuid.toString(), position)
                );
                return map;
            }
        );

    private static final SavedDataType<StaffModeData> TYPE = new SavedDataType<>(
            Tools.id("staff_mode"),
            StaffModeData::new,
            CODEC,
            null
    );

    public static StaffModeData GetSelf(MinecraftServer server) {
        ServerLevel level = server.getLevel(ServerLevel.OVERWORLD);
        if (level == null) {
            return new StaffModeData();
        }
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    public final void setPosition(UUID uuid, Position position) {
        positions.put(uuid, position);
        setDirty();
    }

    public final Position getPosition(UUID uuid) {
        return positions.get(uuid);
    }

    public final void deletePosition(UUID uuid) {
        positions.remove(uuid);
        setDirty();
    }
}
