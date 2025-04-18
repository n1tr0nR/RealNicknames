package com.nitron.nickname.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.nitron.nickname.RealNickname;
import com.nitron.nickname.cca.PlayerNickComponent;
import com.nitron.nickname.config.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class NicknameCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("nick")
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("nick", StringArgumentType.string())
                                .executes(commandContext -> {
                                    ServerCommandSource source = (ServerCommandSource) commandContext.getSource();
                                    PlayerEntity player = source.getPlayer();
                                    PlayerNickComponent component = PlayerNickComponent.get(player);
                                    String nick = StringArgumentType.getString(commandContext, "nick");
                                    if(nick.isEmpty()){
                                        component.setNickname("");
                                        component.setHasNickname(false);
                                        player.sendMessage(Text.literal("Removed your nickname").formatted(Formatting.GRAY));
                                    } else {
                                        int length = nick.length();
                                        if(length > Config.maxNick){
                                            player.sendMessage(Text.literal("Nickname exceeds character limit").formatted(Formatting.RED));
                                        } else if(nick.equals(" ")) {
                                            player.sendMessage(Text.literal("Nickname cannot be empty").formatted(Formatting.RED));
                                        } else {
                                            component.setNickname(nick);
                                            component.setHasNickname(true);
                                            player.sendMessage(Text.literal("Set nickname to: " + nick).formatted(Formatting.GRAY));
                                        }
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(CommandManager.literal("clear")
                        .executes(commandContext -> {
                            ServerCommandSource source = (ServerCommandSource) commandContext.getSource();
                            PlayerEntity player = source.getPlayer();
                            PlayerNickComponent component = PlayerNickComponent.get(player);
                            component.setHasNickname(false);
                            component.setNickname("");
                            player.sendMessage(Text.literal("Removed your nickname").formatted(Formatting.GRAY));
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(CommandManager.literal("color").requires((serverCommandSource -> {
                            return Config.showColor;
                        })).then(CommandManager.argument("color", StringArgumentType.string())
                                .executes(commandContext -> {
                                    String color = StringArgumentType.getString(commandContext, "color");
                                    ServerCommandSource source = commandContext.getSource();
                                    PlayerEntity player = source.getPlayer();
                                    PlayerNickComponent component = PlayerNickComponent.get(player);
                                    if(!Config.showColor){
                                        player.sendMessage(Text.literal("Changing your color has been disabled")
                                                .formatted(Formatting.GRAY));
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    if (RealNickname.isValidHex(color)) {
                                        component.setColor(color);
                                        component.setHasColor(true);
                                        int fin = RealNickname.convertToHex(color);

                                        player.sendMessage(Text.literal("Changed name color to: ")
                                                .formatted(Formatting.GRAY)
                                                .append(Text.literal("0x" + color.toUpperCase())
                                                        .setStyle(Style.EMPTY.withColor(fin)))
                                        );

                                    } else {
                                        component.setColor("");
                                        component.setHasColor(false);
                                        player.sendMessage(Text.literal("Should be a valid hex code, removed your color.")
                                                .formatted(Formatting.GRAY));
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))));
    }
}
