package io.github.phateio.discordsrvmeproxy;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class DiscordSRVMeProxy extends JavaPlugin {

    DiscordSRV srv;
    String discord_format;
    String mc_format;

    @Override
    public void onEnable() {
        srv = DiscordSRV.getPlugin();
        FileConfiguration config = getConfig();
        config.addDefault("discord_format", "* _{player}_ _{message}_");
        config.addDefault("mc_format", "* {player} {message}");
        config.options().copyDefaults(true);
        saveConfig();
        discord_format = config.getString("discord_format");
        mc_format = config.getString("mc_format");
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!label.equalsIgnoreCase("me")) return true;
        if (args.length == 0) {
            sender.sendMessage("/me <msg>");
            return true;
        }
        String msg = String.join(" ", args);
        //noinspection deprecation
        getServer().broadcastMessage(
                mc_format.replace("{plyaer}", sender.getName())
                        .replace("{message}", msg)
        );
        TextChannel ch = srv.getMainTextChannel();
        if (ch == null) return true;
        ch.sendMessage(
                discord_format.replace("{player}", sender.getName())
                        .replace("{message}", DiscordUtil.escapeMarkdown(msg))
        ).queue();
        return true;
    }
}
