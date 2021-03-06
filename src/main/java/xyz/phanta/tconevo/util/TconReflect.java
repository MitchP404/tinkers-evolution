package xyz.phanta.tconevo.util;

import io.github.phantamanta44.libnine.util.helper.MirrorUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.mantle.util.RecipeMatchRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import slimeknights.tconstruct.library.traits.ITrait;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;

public class TconReflect {

    private static final Map<String, ModContainer> materialRegisteredByMod = MirrorUtils
            .<Map<String, ModContainer>>reflectField(TinkerRegistry.class, "materialRegisteredByMod").get(null);
    private static final Map<String, Material> materials = MirrorUtils
            .<Map<String, Material>>reflectField(TinkerRegistry.class, "materials").get(null);
    private static final List<MeltingRecipe> meltingRegistry = MirrorUtils
            .<List<MeltingRecipe>>reflectField(TinkerRegistry.class, "meltingRegistry").get(null);
    private static final List<ICastingRecipe> tableCastRegistry = MirrorUtils
            .<List<ICastingRecipe>>reflectField(TinkerRegistry.class, "tableCastRegistry").get(null);
    private static final List<ICastingRecipe> basinCastRegistry = MirrorUtils
            .<List<ICastingRecipe>>reflectField(TinkerRegistry.class, "basinCastRegistry").get(null);
    private static final List<AlloyRecipe> alloyRegistry = MirrorUtils
            .<List<AlloyRecipe>>reflectField(TinkerRegistry.class, "alloyRegistry").get(null);
    private static final MirrorUtils.IField<PriorityQueue<RecipeMatch>> fRecipeMatchRecipe_items = MirrorUtils.
            reflectField(RecipeMatchRegistry.class, "items");
    private static final MirrorUtils.IField<List<ItemStack>> fOredict_oredictEntry = MirrorUtils
            .reflectField(RecipeMatch.Oredict.class, "oredictEntry");
    private static final MirrorUtils.IField<Map<String, List<ITrait>>> fMaterial_traits = MirrorUtils
            .reflectField(Material.class, "traits");

    public static void overrideMaterialOwnerMod(Material material, Object modObj) {
        materialRegisteredByMod.put(material.identifier, FMLCommonHandler.instance().findContainerFor(modObj));
    }

    public static void removeMaterial(String identifier) {
        materials.remove(identifier);
    }

    public static ListIterator<MeltingRecipe> iterateMeltingRecipes() {
        return meltingRegistry.listIterator();
    }

    public static ListIterator<ICastingRecipe> iterateTableCastRecipes() {
        return tableCastRegistry.listIterator();
    }

    public static ListIterator<ICastingRecipe> iterateBasinCastRecipes() {
        return basinCastRegistry.listIterator();
    }

    public static ListIterator<AlloyRecipe> iterateAlloyRecipes() {
        return alloyRegistry.listIterator();
    }

    public static PriorityQueue<RecipeMatch> getItems(RecipeMatchRegistry recipeRegistry) {
        return fRecipeMatchRecipe_items.get(recipeRegistry);
    }

    public static List<ItemStack> getOreEntries(RecipeMatch.Oredict recipeMatch) {
        return fOredict_oredictEntry.get(recipeMatch);
    }

    public static Map<String, List<ITrait>> getTraits(Material material) {
        return fMaterial_traits.get(material);
    }

}
