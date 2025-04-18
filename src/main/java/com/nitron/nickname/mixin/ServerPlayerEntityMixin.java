package com.nitron.nickname.mixin;

import com.mojang.authlib.GameProfile;
import com.nitron.nickname.RealNickname;
import com.nitron.nickname.cca.PlayerNickComponent;
import com.nitron.nickname.config.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "onSpawn", at = @At("TAIL"))
    private void spawn(CallbackInfo ci){
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        updateTabList(player);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci){
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        updateTabList(player);
    }

    @Inject(method = "getPlayerListName", at = @At("TAIL"), cancellable = true)
    private void replaceNameOnTablist(CallbackInfoReturnable<Text> cir){
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        PlayerNickComponent component = PlayerNickComponent.get(player);
        if(component.isHasNickname()){
            if (component.isHasColor() && Config.showColor) {
                cir.setReturnValue(Text.literal(component.getNickname()).setStyle(Style.EMPTY.withColor(RealNickname.convertToHex(component.getColor()))));
            } else {
                cir.setReturnValue(Text.literal(component.getNickname()));
            }
        }
    }

    @Unique
    private static void updateTabList(ServerPlayerEntity player){
        ServerPlayNetworkHandler handler = player.networkHandler;
        if(handler != null){
            MinecraftServer server = player.getServer();
            if(server != null){
                ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(player.getUuid());
                if(playerEntity != null){
                    server.getPlayerManager().sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, playerEntity));
                }
            }
        }
    }
}
