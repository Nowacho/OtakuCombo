package me.wacho.otaku.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Default;
import me.wacho.otaku.Otaku;
import me.wacho.otaku.server.data.ServerData;
import me.wacho.otaku.server.menu.KitSelectionMenu;
import me.wacho.otaku.utils.other.LocationUtils;
import me.wacho.otaku.utils.text.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EssentialCommands extends BaseCommand {

    @CommandAlias("set")
    @CommandPermission("core.otaku.set")
    public void onSetCommand(Player player, String[] args) {
        String stringLocation = LocationUtils.getString(player.getLocation());

        if (args[0].toLowerCase().equals("spawn")) {
            Otaku.getInstance().getServerData().setSpawn(stringLocation);
            player.sendMessage(CC.translate("&aThe spawn was correctly established."));
        } else {
            player.sendMessage(CC.translate("&cInvalid subcommand. Use '/set help' for help."));
        }
    }

    @Default
    @CommandPermission("core.otaku.setcubo")
    public void onSetCuboCommand(Player player, String[] args) {
        String stringLocation = LocationUtils.getString(player.getLocation());
        ServerData serverData = Otaku.getInstance().getServerData();

        switch (args[0].toLowerCase()) {
            case "first":
                serverData.setFirstCI(stringLocation);
                player.sendMessage(CC.translate("&aFirst cuboid corner set successfully."));
                break;
            case "second":
                serverData.setSecondCI(stringLocation);
                player.sendMessage(CC.translate("&aSecond cuboid corner set successfully."));
                break;
            default:
                player.sendMessage(CC.translate("&cUnknown argument. Use '/setcubo' for help."));
                break;
        }

        serverData.saveServer();
    }

    @CommandAlias("kit|gkit")
    @Conditions("player")
    public void store(CommandSender sender) {
        Player player = (Player) sender;
        new KitSelectionMenu().openMenu(player);
    }
}
