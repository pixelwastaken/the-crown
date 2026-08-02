//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.event;

import dirt.thecrown.TheCrown;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class CrownMobEffectUtil {
    public static int DEFAULT_DURATION = 30;
    private final MobEffectInstance effect;
    private MobEffectInstance optEffect;

    public CrownMobEffectUtil(Holder<MobEffect> effect, int amplifier) {
        this.effect = new MobEffectInstance(effect, DEFAULT_DURATION * 20, amplifier - 1);
    }

    public CrownMobEffectUtil(Holder<MobEffect> effect, int amplifier, int duration) {
        this.effect = new MobEffectInstance(effect, duration * 20, amplifier - 1);
    }

    public CrownMobEffectUtil(Holder<MobEffect> effect1, int amplifier1, Holder<MobEffect> effect2, int amplifier2) {
        this.effect = new MobEffectInstance(effect1, DEFAULT_DURATION * 20, amplifier1 - 1);
        this.optEffect = new MobEffectInstance(effect2, DEFAULT_DURATION * 20, amplifier2 - 1);
    }

    public CrownMobEffectUtil(Holder<MobEffect> effect1, int amplifier1, Holder<MobEffect> effect2, int amplifier2, int duration) {
        this.effect = new MobEffectInstance(effect1, duration * 20, amplifier1 - 1);
        this.optEffect = new MobEffectInstance(effect2, duration * 20, amplifier2 - 1);
    }

    public String getName() {
        if (this.optEffect != null) {
            String var10000 = String.valueOf(this.effect);
            return var10000 + ", " + String.valueOf(this.optEffect);
        } else {
            return this.effect.toString();
        }
    }

    public void apply(LivingEntity entity) {
        if (entity != null) {
            entity.addEffect(new MobEffectInstance(this.effect));
            if (this.optEffect != null) {
                entity.addEffect(new MobEffectInstance(this.optEffect));
            }
        } else {
            TheCrown.LOGGER.warn("Could not apply CrownMobEffect; entity param is null");
        }

    }
}
