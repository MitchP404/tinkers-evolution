package xyz.phanta.tconevo.trait.draconicevolution;

import io.github.phantamanta44.libnine.util.math.MathUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import xyz.phanta.tconevo.TconEvoConfig;
import xyz.phanta.tconevo.TconEvoMod;
import xyz.phanta.tconevo.constant.NameConst;
import xyz.phanta.tconevo.integration.draconicevolution.DraconicHooks;
import xyz.phanta.tconevo.network.SPacketEntitySpecialEffect;
import xyz.phanta.tconevo.util.ToolUtils;

import java.util.List;
import java.util.Objects;

public class ModifierFluxBurn extends ModifierTrait {

    public ModifierFluxBurn() {
        super(NameConst.MOD_FLUX_BURN, 0xaa2648, 5, 0);
    }

    private float getBurnFraction(int level) {
        return level / 100F;
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target,
                         float damageDealt, boolean wasCritical, boolean wasHit) {
        if (target.world.isRemote || !wasHit
                || (player instanceof EntityPlayer && ((EntityPlayer)player).getCooledAttackStrength(0.5F) < 0.95F)) {
            return;
        }
        int level = ToolUtils.getTraitLevel(tool, identifier);
        float burnAmount = getBurnFraction(level);
        int minBurn = level * 256, maxBurn = level * 320000;
        long totalBurned = 0;
        for (ItemStack stack : target.getArmorInventoryList()) {
            int burned = DraconicHooks.INSTANCE.burnArmourEnergy(stack, burnAmount, minBurn, maxBurn);
            if (burned > 0) {
                totalBurned += burned;
            } else if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
                IEnergyStorage energy = Objects.requireNonNull(stack.getCapability(CapabilityEnergy.ENERGY, null));
                burned = MathUtils.clamp((int)Math.ceil(energy.getEnergyStored() * burnAmount), minBurn, maxBurn);
                if (burned > 0) {
                    totalBurned += energy.extractEnergy(burned, false);
                }
            }
        }
        if (totalBurned > 0) {
            float burnDamage = (float)(totalBurned / (double)TconEvoConfig.moduleDraconicEvolution.fluxBurnEnergy);
            if (burnDamage >= 0.01F) { // no small damage
                target.hurtResistantTime = 0; // reset i-frames from the original attack
                target.attackEntityFrom(player instanceof EntityPlayer
                        ? DamageSource.causePlayerDamage((EntityPlayer)player)
                        : DamageSource.causeMobDamage(player), burnDamage);
            }
            TconEvoMod.PROXY.playEntityEffect(target, SPacketEntitySpecialEffect.EffectType.FLUX_BURN);
        }
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        return ToolUtils.formatExtraInfoPercent(this, getBurnFraction(ToolUtils.getTraitLevel(tool, identifier)));
    }

}
