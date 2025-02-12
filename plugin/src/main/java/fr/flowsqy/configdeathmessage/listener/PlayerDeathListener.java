package fr.flowsqy.configdeathmessage.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        final var lastDamageCause = event.getEntity().getLastDamageCause();
        if (lastDamageCause == null) {
            return;
        }
        final var damageSource = lastDamageCause.getDamageSource();
        final var damageType = damageSource.getDamageType();
    }

}
