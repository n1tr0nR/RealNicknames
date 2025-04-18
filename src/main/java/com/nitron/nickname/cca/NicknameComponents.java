package com.nitron.nickname.cca;

import com.nitron.nickname.RealNickname;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class NicknameComponents implements EntityComponentInitializer {
    public static final ComponentKey<PlayerNickComponent> NICKNAME = ComponentRegistry.getOrCreate(Identifier.of(RealNickname.MOD_ID, "nickname"), PlayerNickComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(NICKNAME, PlayerNickComponent::new, RespawnCopyStrategy.ALWAYS_COPY.ALWAYS_COPY);
    }
}
