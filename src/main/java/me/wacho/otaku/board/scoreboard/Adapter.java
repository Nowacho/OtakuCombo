package me.wacho.otaku.board.scoreboard;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import me.wacho.otaku.Otaku;
import me.wacho.otaku.server.enums.PlayerStat;
import me.wacho.otaku.user.PlayerData;
import me.wacho.otaku.utils.Utilities;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static me.wacho.otaku.utils.text.CC.translate;

public class Adapter implements AssembleAdapter {

    @Override
    public String getTitle(Player player) {
        return translate("&c&lCombo");
    }

    @Override
    public List<String> getLines(Player player) {
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        List<String> scores  = new ArrayList<>();

        scores.add("&7" + Utilities.getCurrentFormattedDate());
        scores.add("");
        scores.add(translate("&c&l▏&fKills: &c" + playerData.getStat(PlayerStat.KILLS)));
        scores.add(translate("&c&l▏&fDeaths: &c" + playerData.getStat(PlayerStat.DEATHS)));
        scores.add(translate("&c&l▏&fBalance: &a$" + Utilities.getFormatNumber(playerData.getStat(PlayerStat.BALANCE))));
        if (Otaku.getInstance().getCombatManager().isCombat(player)) {
            scores.add("");
            scores.add(translate("&c&l▏&fCombat Tag: &f" + Otaku.getInstance().getCombatManager().getCombatTime(player) + "s"));
        }
        scores.add(translate(" "));
        scores.add(translate("&7otaku.combo"));

        return scores ;
    }
}
