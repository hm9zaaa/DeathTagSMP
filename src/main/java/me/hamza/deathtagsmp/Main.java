package me.hamza.deathtagsmp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Main extends JavaPlugin implements Listener {

    private final String display = ChatColor.GRAY + "Player";

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        p.setDisplayName(display);
        p.setPlayerListName(p.getName()); // vrai pseudo en tab
        p.setCustomName(display);
        p.setCustomNameVisible(false);

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Team t = board.registerNewTeam("anon");
        t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        t.addEntry(p.getName());
        p.setScoreboard(board);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer != null) {
            if (killer.getInventory().getItemInMainHand().hasItemMeta()) {
                ItemMeta meta = killer.getInventory().getItemInMainHand().getItemMeta();
                if (meta.hasDisplayName()) {
                    if (meta.getDisplayName().contains(e.getEntity().getName())) {
                        getServer().getBanList(org.bukkit.BanList.Type.NAME)
                                .addBan(killer.getName(), "Kill avec arme renommée", null, null);
                        killer.kickPlayer("Banni pour kill avec arme renommée !");
                    }
                }
            }
        }

        e.setDeathMessage(ChatColor.RED + display + " a tué " + display);
    }
}
