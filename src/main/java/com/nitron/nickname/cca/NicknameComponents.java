package com.nitron.nickname.cca;

import com.nitron.nickname.RealNickname;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class NicknameComponents implements EntityComponentInitializer {
    public static final ComponentKey<PlayerNickComponent> NICKNAME = ComponentRegistry.getOrCreate(Identifier.of(RealNickname.MOD_ID, "nickname"), PlayerNickComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(NICKNAME, PlayerNickComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
