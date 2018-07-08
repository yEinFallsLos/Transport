package xyz.brassgoggledcoders.transport.minecart.entity;

import com.teamacronymcoders.base.entities.EntityMinecartBase;
import com.teamacronymcoders.base.entities.dataserializers.BaseDataSerializers;
import net.minecraft.item.ItemMinecart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.CapabilityCargo;
import xyz.brassgoggledcoders.transport.api.cargo.ICargo;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.CargoCarrierEntity;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.minecart.item.ItemMinecartCargoCarrier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class EntityMinecartCargoCarrier extends EntityMinecartBase {
    @ObjectHolder(Transport.ID + ":minecart_cargo_carrier")
    private static ItemMinecartCargoCarrier itemMinecartCargoCarrier;

    private static final DataParameter<ResourceLocation> CARGO_REGISTRY_NAME =
            EntityDataManager.createKey(EntityMinecartCargoCarrier.class, BaseDataSerializers.RESOURCE_LOCATION);

    private CargoCarrierEntity cargoCarrier;

    public EntityMinecartCargoCarrier(World world) {
        super(world);
    }

    public EntityMinecartCargoCarrier(World world, ICargo cargo) {
        this(world);
        this.cargoCarrier = new CargoCarrierEntity(this, cargo);
        dataManager.set(CARGO_REGISTRY_NAME, cargo.getRegistryName());
    }

    @Override
    public void entityInit() {
        super.entityInit();
        dataManager.register(CARGO_REGISTRY_NAME, new ResourceLocation(Transport.ID, "empty"));
    }

    @Nonnull
    @Override
    public ItemMinecart getItem() {
        return itemMinecartCargoCarrier;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityCargo.CARRIER ||
                cargoCarrier.getCargoInstance().hasCapability(capability, facing) ||
                super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityCargo.CARRIER) {
            return CapabilityCargo.CARRIER.cast(cargoCarrier);
        }
        T cargoCap = cargoCarrier.getCargoInstance().getCapability(capability, facing);
        return cargoCap != null ? cargoCap : super.getCapability(capability, facing);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        ResourceLocation name = new ResourceLocation(compound.getString("cargo_name"));
        dataManager.set(CARGO_REGISTRY_NAME, name);
        this.cargoCarrier = new CargoCarrierEntity(this, TransportAPI.getCargoRegistry().getEntry(name));
        this.cargoCarrier.deserializeNBT(compound.getCompoundTag("cargo"));
    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setString("cargo_name", dataManager.get(CARGO_REGISTRY_NAME).toString());
        compound.setTag("cargo", cargoCarrier.serializeNBT());
    }

    @Override
    public Optional<NBTTagCompound> getCartItemNBT() {
        NBTTagCompound cartNBT = new NBTTagCompound();
        NBTTagCompound cargoNBT = new NBTTagCompound();
        cartNBT.setTag("cargo", cargoNBT);
        cargoNBT.setString("name", cargoCarrier.getCargo().getRegistryName().toString());
        return Optional.of(cartNBT);
    }

    public ICargoCarrier getCargoCarrier() {
        if (cargoCarrier == null) {
            ICargo cargo = TransportAPI.getCargoRegistry().getEntry(this.dataManager.get(CARGO_REGISTRY_NAME));
            cargoCarrier = new CargoCarrierEntity(this, cargo);
        }
        return cargoCarrier;
    }
}
