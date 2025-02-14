package fr.flowsqy.configdeathmessage.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import fr.flowsqy.configdeathmessage.message.MessagesManager;

public class PlayerDeathListener implements Listener {

    private final MessagesManager messagesManager;

    public PlayerDeathListener(MessagesManager messagesManager) {
        this.messagesManager = messagesManager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        final var lastDamageCause = event.getEntity().getLastDamageCause();
        if (lastDamageCause == null) {
            return;
        }
        final var damageSource = lastDamageCause.getDamageSource();
        final var message = messagesManager.getMessage(event.getEntity(), damageSource.getDamageType(),
                damageSource.getCausingEntity());
        if (message == null) {
            return;
        }
        event.setDeathMessage(message);
    }

}
