package com.vincenthuto.moremagichemlabels.client.event;

import com.aranaira.magichem.item.MateriaItem;
import com.aranaira.magichem.recipe.DistillationFabricationRecipe;
import com.mojang.datafixers.util.Either;
import com.vincenthuto.moremagichemlabels.MoreMagichemLabels;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MoreMagichemLabels.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler {

    @SubscribeEvent
    public static void comps(RenderTooltipEvent.GatherComponents e) {

        List<Either<FormattedText, TooltipComponent>> list = e.getTooltipElements();
        if (!(e.getItemStack().getItem() instanceof MateriaItem)) {
            DistillationFabricationRecipe drecipe = DistillationFabricationRecipe
                    .getDistillingRecipe(Minecraft.getInstance().level, new ItemStack(e.getItemStack().getItem()));
            if (drecipe != null) {
                e.getTooltipElements().add(list.size(), Either.right(new MateriaGlyphTooltipRenderer.MateriaGlyphTooltipComponent(e.getItemStack())));
            }
        }
    }


    @Mod.EventBusSubscriber(modid = MoreMagichemLabels.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
            event.register(MateriaGlyphTooltipRenderer.MateriaGlyphTooltipComponent.class, MateriaGlyphTooltipRenderer::new);
        }

    }

}
