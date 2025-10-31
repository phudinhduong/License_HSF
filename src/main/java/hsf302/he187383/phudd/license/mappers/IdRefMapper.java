package hsf302.he187383.phudd.license.mappers;

import org.mapstruct.Named;
import java.util.UUID;

// Helper tạo "entity reference" chỉ với id (tránh query)
public interface IdRefMapper {

    @Named("uuid")
    default UUID toUUID(String id) { return id == null ? null : UUID.fromString(id); }

    @Named("toString")
    default String toString(UUID id) { return id == null ? null : id.toString(); }

    // Generic builder cho entity ref (dùng qua lambda trong mapper cụ thể)
    @Named("ref")
    default <T> T ref(UUID id, java.util.function.Function<UUID, T> factory) {
        return id == null ? null : factory.apply(id);
    }
}
