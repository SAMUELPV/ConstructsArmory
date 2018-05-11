package c4.conarm.armor.modifiers;

import c4.conarm.armor.ConstructsArmor;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.IModifierDisplay;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.tools.harvest.TinkerHarvestTools;
import slimeknights.tconstruct.tools.modifiers.ModExtraTrait;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ModExtraArmorTraitDisplay extends Modifier implements IModifierDisplay {

  public ModExtraArmorTraitDisplay() {
    super(ModExtraTrait.EXTRA_TRAIT_IDENTIFIER + "_armor");
  }

  @Override
  public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
    //NO-OP
  }

  @Override
  public int getColor() {
    return 0xdddddd;
  }

  @Override
  public List<List<ItemStack>> getItems() {
    return ConstructsArmor.chestplate.getRequiredComponents().stream()
                              .map(PartMaterialType::getPossibleParts)
                              .flatMap(Collection::stream)
                              .map(this::getItems)
                              .collect(Collectors.toList());
  }

  private List<ItemStack> getItems(IToolPart part) {
    List<Material> possibleMaterials = TinkerRegistry.getAllMaterials().stream()
                                                     .filter(part::canUseMaterial)
                                                     .collect(Collectors.toList());
    Material material = possibleMaterials.get(new Random().nextInt(possibleMaterials.size()));

    return ImmutableList.<ItemStack>builder()
        .add(part.getItemstackWithMaterial(material))
        .addAll(ModExtraTrait.EMBOSSMENT_ITEMS)
        .build();
  }
}