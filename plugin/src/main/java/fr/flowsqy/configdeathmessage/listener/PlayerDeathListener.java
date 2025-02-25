package fr.flowsqy.configdeathmessage.listener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import fr.flowsqy.configdeathmessage.message.MessagesManager;

public class PlayerDeathListener implements Listener {

    private final MessagesManager messagesManager;

    public PlayerDeathListener(MessagesManager messagesManager) {
        this.messagesManager = messagesManager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        final var victimPlayer = event.getEntity();
        final var lastDamageCause = victimPlayer.getLastDamageCause();
        if (lastDamageCause == null) {
            return;
        }
        final var damageSource = lastDamageCause.getDamageSource();
        final var message = messagesManager.getMessage(victimPlayer, damageSource.getDamageType(),
                damageSource.getCausingEntity());
        if (message == null) {
            return;
        }
        event.setDeathMessage(null);
        // Mimic vanilla behavior
        final var receiverPlayers = getReceivers(victimPlayer);
        for (var player : receiverPlayers) {
            player.spigot().sendMessage(message);
        }
    }

    private Iterable<? extends Player> getReceivers(Player victimPlayer) {
        final var victimTeam = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard()
                .getEntryTeam(victimPlayer.getName());
        if (victimTeam == null) {
            return Bukkit.getOnlinePlayers();
        }
        final var deathMessageVisibility = victimTeam.getOption(Option.DEATH_MESSAGE_VISIBILITY);
        if (deathMessageVisibility == OptionStatus.NEVER) {
            return Collections.emptyList();
        }
        if (deathMessageVisibility == OptionStatus.FOR_OTHER_TEAMS) {
            final var inTeamPlayers = new LinkedList<Player>();
            for (var player : Bukkit.getOnlinePlayers()) {
                if (victimTeam.hasEntry(player.getName())) {
                    inTeamPlayers.add(player);
                }
            }
            return inTeamPlayers;
        }
        if (deathMessageVisibility == OptionStatus.FOR_OWN_TEAM) {
            final var notInTeamPlayers = new LinkedList<Player>();
            for (var player : Bukkit.getOnlinePlayers()) {
                if (!victimTeam.hasEntry(player.getName())) {
                    notInTeamPlayers.add(player);
                }
            }
            return notInTeamPlayers;
        }
        return Bukkit.getOnlinePlayers();
    }

}
