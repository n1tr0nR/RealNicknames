package com.nitron.nickname.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.nitron.nickname.RealNickname;
import com.nitron.nickname.cca.PlayerNickComponent;
import com.nitron.nickname.config.Config;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method = "getDisplayName", at = @At("RETURN"))
    private Text changeNickname(Text original){
        PlayerEntity self = ((PlayerEntity) (Object) this);
        PlayerNickComponent component = PlayerNickComponent.get(self);

        if(component.isHasNickname()){
            if (component.isHasColor() && Config.showColor) {
                return Text.literal(component.getNickname()).setStyle(Style.EMPTY.withColor(RealNickname.convertToHex(component.getColor())));
            } else {
                return Text.literal(component.getNickname());
            }
        }
        return original;
    }
}
