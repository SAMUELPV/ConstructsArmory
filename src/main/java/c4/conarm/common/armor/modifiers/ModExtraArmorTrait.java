/*
 * Copyright (c) 2018 <C4>
 *
 * This Java class is distributed as a part of the Construct's Armory mod.
 * Construct's Armory is open source and distributed under the GNU Lesser General Public License v3.
 * View the source code and license file on github: https://github.com/TheIllusiveC4/ConstructsArmory
 *
 * Some classes and assets are taken and modified from the parent mod, Tinkers' Construct.
 * Tinkers' Construct is open source and distributed under the MIT License.
 * View the source code on github: https://github.com/SlimeKnights/TinkersConstruct/
 * View the MIT License here: https://tldrlegal.com/license/mit-license
 */

package c4.conarm.common.armor.modifiers;

import c4.conarm.lib.armor.ArmorCore;
import c4.conarm.lib.modifiers.ArmorModifier;
import c4.conarm.lib.utils.RecipeMatchHolder;
import com.google.common.collect.ImmutableList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.modifiers.TinkerGuiException;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.ToolBuilder;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.tools.modifiers.ModExtraTrait;
import slimeknights.tconstruct.tools.modifiers.ToolModifier;

import java.util.*;
import java.util.stream.Collectors;

public class ModExtraArmorTrait extends ArmorModifier {

    private final Material material;
    public final Set<ArmorCore> armorCores;
    private final Collection<ITrait> traits;


    public ModExtraArmorTrait(Material material, Collection<ITrait> traits) {
        this(material, traits, generateIdentifier(material, traits));
    }

    public ModExtraArmorTrait(Material material, Collection<ITrait> traits, String customIdentifier) {
        super(ModExtraTrait.EXTRA_TRAIT_IDENTIFIER + customIdentifier, material.materialTextColor);

        this.material = material;
        this.armorCores = new HashSet<>();
        this.traits = traits;
        addAspects(new ModExtraArmorTrait.ExtraTraitAspect(), new ModifierAspect.SingleAspect(this), new ModifierAspect.DataAspect(this));
    }

    public <T extends Item & IToolPart> void addCombination(ArmorCore armorCore, T toolPart) {
        armorCores.add(armorCore);
        ItemStack toolPartItem = toolPart.getItemstackWithMaterial(material);
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(toolPartItem);
        stacks.addAll(ModExtraTrait.EMBOSSMENT_ITEMS);
        RecipeMatchHolder.addRecipeMatch(this, new RecipeMatch.ItemCombination(1, stacks.toArray(new ItemStack[stacks.size()])));
    }

    public static String generateIdentifier(Material material, Collection<ITrait> traits) {
        String traitString = traits.stream().map(ITrait::getIdentifier).sorted().collect(Collectors.joining());
        return material.getIdentifier() + traitString;
    }

    @Override
    public boolean canApplyCustom(ItemStack stack) {
        return stack.getItem() instanceof ArmorCore && armorCores.contains(stack.getItem());
    }

    @Override
    public String getLocalizedName() {
        return Util.translate(LOC_Name, ModExtraTrait.EXTRA_TRAIT_IDENTIFIER) + " (" + material.getLocalizedName() + ")";
    }

    @Override
    public String getLocalizedDesc() {
        return Util.translateFormatted(String.format(LOC_Desc, ModExtraTrait.EXTRA_TRAIT_IDENTIFIER), material.getLocalizedName());
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        traits.forEach(trait -> ToolBuilder.addTrait(rootCompound, trait, color));
    }

    @Override
    public boolean hasTexturePerMaterial() {
        return true;
    }

    private static class ExtraTraitAspect extends ModifierAspect {

        @Override
        public boolean canApply(ItemStack stack, ItemStack original) throws TinkerGuiException {
            NBTTagList modifierList = TagUtil.getModifiersTagList(original);
            for(int i = 0; i < modifierList.tagCount(); i++) {
                NBTTagCompound tag = modifierList.getCompoundTagAt(i);
                ModifierNBT data = ModifierNBT.readTag(tag);
                if(data.identifier.startsWith(ModExtraTrait.EXTRA_TRAIT_IDENTIFIER)) {
                    throw new TinkerGuiException(Util.translate("gui.error.already_has_extratrait"));
                }
            }
            return true;
        }

        @Override
        public void updateNBT(NBTTagCompound root, NBTTagCompound modifierTag) {
            //NO-OP
        }
    }
}
