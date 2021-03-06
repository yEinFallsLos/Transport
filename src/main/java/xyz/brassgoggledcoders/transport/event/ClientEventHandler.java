package xyz.brassgoggledcoders.transport.event;

import com.hrznstudio.titanium.client.screen.container.BasicAddonScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.DataPackRegistries;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportClientAPI;
import xyz.brassgoggledcoders.transport.api.renderer.CargoModuleRender;
import xyz.brassgoggledcoders.transport.api.renderer.ItemModuleRenderer;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportContainers;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.content.TransportModuleTypes;
import xyz.brassgoggledcoders.transport.model.item.ModularItemModelLoader;
import xyz.brassgoggledcoders.transport.renderer.minecart.CargoCarrierMinecartEntityRenderer;
import xyz.brassgoggledcoders.transport.renderer.boat.HulledBoatRender;
import xyz.brassgoggledcoders.transport.renderer.boat.ModularBoatRenderer;
import xyz.brassgoggledcoders.transport.renderer.tileentity.ModuleConfiguratorTileEntityRenderer;
import xyz.brassgoggledcoders.transport.screen.ModuleConfiguratorScreen;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@EventBusSubscriber(modid = Transport.ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientEventHandler {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(TransportBlocks.HOLDING_RAIL.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.DIAMOND_CROSSING_RAIL.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.ELEVATOR_SWITCH_RAIL.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.ELEVATOR_SWITCH_SUPPORT.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.SCAFFOLDING_RAIL.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.SCAFFOLDING_SLAB_BLOCK.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.SWITCH_RAIL.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.WYE_SWITCH_RAIL.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.BUMPER_RAIL.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.TIMED_HOLDING_RAIL.get(), RenderType.getCutout());

        ScreenManager.registerFactory(TransportContainers.MODULE.get(), BasicAddonScreen::new);
        ScreenManager.registerFactory(TransportContainers.MODULE_CONFIGURATOR.get(), ModuleConfiguratorScreen::new);

        EntityRendererManager rendererManager = Minecraft.getInstance().getRenderManager();
        rendererManager.register(TransportEntities.CARGO_MINECART.get(), new CargoCarrierMinecartEntityRenderer(rendererManager));
        rendererManager.register(TransportEntities.HULLED_BOAT.get(), new HulledBoatRender<>(rendererManager));
        rendererManager.register(TransportEntities.MODULAR_BOAT.get(), new ModularBoatRenderer(rendererManager));

        ClientRegistry.bindTileEntityRenderer(TransportBlocks.MODULE_CONFIGURATOR_TILE_ENTITY.get(),
                ModuleConfiguratorTileEntityRenderer::new);

        TransportClientAPI.setModuleTypeDefault(TransportModuleTypes.CARGO.get(), new CargoModuleRender());
        TransportClientAPI.setModuleTypeDefault(TransportModuleTypes.ENGINE.get(), new ItemModuleRenderer());
    }

    @SubscribeEvent
    public static void registerItemModelLoader(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(Transport.ID, "modular_item"), new ModularItemModelLoader());
    }
}
