package com.nitron.nickname.cca;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class PlayerNickComponent implements AutoSyncedComponent, CommonTickingComponent {
    private final PlayerEntity player;

    private boolean hasNickname = false;
    private String nickname = "";
    private boolean hasColor = false;
    private String color = "";

    public PlayerNickComponent(PlayerEntity player) {
        this.player = player;
    }
    private void sync(){NicknameComponents.NICKNAME.sync(this.player);}
    public static PlayerNickComponent get(@NotNull PlayerEntity player) {return (PlayerNickComponent) NicknameComponents.NICKNAME.get(player);}


    @Override
    public void tick() {

    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        this.hasNickname = nbtCompound.getBoolean("hasNickname");
        this.hasColor = nbtCompound.getBoolean("hasColor");
        this.nickname = nbtCompound.getString("nickname");
        this.color = nbtCompound.getString("color");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbtCompound.putBoolean("hasNickname", this.hasNickname);
        nbtCompound.putBoolean("hasColor", this.hasColor);
        nbtCompound.putString("nickname", this.nickname);
        nbtCompound.putString("color", this.color);
    }

    public boolean isHasNickname() {
        return hasNickname;
    }

    public void setHasNickname(boolean hasNickname) {
        this.hasNickname = hasNickname;
        this.sync();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        this.sync();
    }

    public boolean isHasColor() {
        return hasColor;
    }

    public void setHasColor(boolean hasColor) {
        this.hasColor = hasColor;
        this.sync();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        this.sync();
    }
}
