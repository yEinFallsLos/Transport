package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.function.BiFunction;

public class EntityWorldPosCallable implements IWorldPosCallable {
    private final WeakReference<Entity> weakEntity;

    public EntityWorldPosCallable(Entity entity) {
        this.weakEntity = new WeakReference<>(entity);
    }

    @Override
    @Nonnull
    public <T> Optional<T> apply(@Nonnull BiFunction<World, BlockPos, T> blockPosTBiFunction) {
        Entity entity = weakEntity.get();
        if (entity != null && entity.isAlive()) {
            return Optional.ofNullable(blockPosTBiFunction.apply(entity.getEntityWorld(), entity.getPosition()));
        }
        return Optional.empty();
    }
}
