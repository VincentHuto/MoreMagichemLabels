package com.vincenthuto.moremagichemlabels.mixin.core;

import com.mna.api.tools.RLoc;
import com.mna.items.SpellIconList;
import com.vincenthuto.moremagichemlabels.MoreMagichemLabels;
import com.vincenthuto.moremagichemlabels.ResourceUtil;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Mixin(value = SpellIconList.class, priority = 1000) // Lower priority = runs later
public class MixinHardcodedSpellIconList {

    @Shadow
    @Mutable
    public static ResourceLocation[] ALL;

    @Inject(method = "<clinit>", at = @At("TAIL"), remap = false)
    private static void addCustomSpellIcons(CallbackInfo ci) {


        List<ResourceLocation> iconList = new ArrayList<>(Arrays.asList(ALL));

        iconList.add( ResourceUtil.rloc("spell_icon/rune_beast"));
        iconList.add( ResourceUtil.rloc("spell_icon/rune_beast_c"));
        iconList.add( ResourceUtil.rloc("spell_icon/rune_communion"));
        iconList.add( ResourceUtil.rloc("spell_icon/rune_corruption_c"));
        iconList.add( ResourceUtil.rloc("spell_icon/rune_guidance"));
        iconList.add( ResourceUtil.rloc("spell_icon/rune_heir"));
        iconList.add( ResourceUtil.rloc("spell_icon/rune_hunter_c"));
        iconList.add( ResourceUtil.rloc("spell_icon/rune_impurity_c"));
        iconList.add( ResourceUtil.rloc("spell_icon/rune_lake"));
        iconList.add( ResourceUtil.rloc("spell_icon/rune_metamorphosis"));
        iconList.add( ResourceUtil.rloc("spell_icon/rune_metamorphosis_cw"));
        iconList.add( ResourceUtil.rloc("spell_icon/rune_milkweed_c"));
        iconList.add( ResourceUtil.rloc("spell_icon/rune_oedon"));
        iconList.add( ResourceUtil.rloc("spell_icon/rune_radiance_c"));
        iconList.add( ResourceUtil.rloc("spell_icon/rune_rapture"));

        ALL = iconList.toArray(new ResourceLocation[0]);
    }
}