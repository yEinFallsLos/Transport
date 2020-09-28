package xyz.brassgoggledcoders.transport.api.manager;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IManager extends INBTSerializable<CompoundNBT> {
    @Nonnull
    BlockPos getPosition();

    @Nonnull
    UUID getUniqueId();

    @Nonnull
    ManagerType getType();

    boolean addManagedObject(@Nonnull ManagedObject managedObject);

    @Nonnull
    Collection<ManagedObject> getManagedObjects();

    @Nonnull
    AxisAlignedBB getBoundary();

    boolean handleUnloading(@Nonnull Entity leader, @Nonnull List<Entity> followers);

    boolean handleLoading(@Nonnull Entity leader, @Nonnull List<Entity> followers);
}
