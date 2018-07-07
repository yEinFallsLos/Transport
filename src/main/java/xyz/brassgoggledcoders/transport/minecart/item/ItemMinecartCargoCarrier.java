package xyz.brassgoggledcoders.transport.minecart.item;

import com.teamacronymcoders.base.entities.EntityMinecartBase;
import com.teamacronymcoders.base.items.minecart.ItemMinecartBase;
import com.teamacronymcoders.base.util.CapUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.CapabilityCargo;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.CapabilityProviderCargoCarrierItem;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.minecart.entity.EntityMinecartCargoCarrier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemMinecartCargoCarrier extends ItemMinecartBase {
    public ItemMinecartCargoCarrier() {
        super("cargo_carrier");
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public EntityMinecartBase getEntityFromItem(World world, ItemStack itemStack) {
        return new EntityMinecartCargoCarrier(world, CapUtils.getOptional(itemStack, CapabilityCargo.CARRIER)
                .map(ICargoCarrier::getCargo)
                .orElse(TransportAPI.getCargoRegistry().getEmpty()));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new CapabilityProviderCargoCarrierItem(stack);
    }
}
