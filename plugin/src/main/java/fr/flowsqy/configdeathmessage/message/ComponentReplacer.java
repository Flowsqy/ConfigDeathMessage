package fr.flowsqy.configdeathmessage.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ComponentReplacer {

    @NotNull
    public void replace(@NotNull BaseComponent message, @NotNull String regex,
            @NotNull BaseComponent replacement) {
        replace(message, Pattern.compile(regex), replacement);
    }

    @NotNull
    public void replace(@NotNull BaseComponent message, @NotNull Pattern pattern,
            @NotNull BaseComponent replacement) {
        final var originalToExtras = originalToExtras(message, pattern, replacement);
        final var previousExtras = message.getExtra();
        final var extraSize = originalToExtras.size() + (previousExtras == null ? 0 : previousExtras.size());
        if (extraSize < 1) {
            return;
        }
        final var extras = new ArrayList<BaseComponent>(extraSize);
        extras.addAll(originalToExtras);
        if (previousExtras != null) {
            for (var extra : previousExtras) {
                replace(extra, pattern, replacement);
            }
            extras.addAll(previousExtras);
        }
        message.setExtra(extras);
    }

    @NotNull
    private List<BaseComponent> originalToExtras(@NotNull BaseComponent message, @NotNull Pattern pattern,
            @NotNull BaseComponent replacement) {
        if (!(message instanceof TextComponent textComponent)) {
            return Collections.emptyList();
        }
        final var text = textComponent.getText();
        final var matcher = pattern.matcher(text);
        if (!matcher.find()) {
            return Collections.emptyList();
        }
        textComponent.setText("");
        final var extras = new LinkedList<BaseComponent>();
        int cursor = 0;
        do {
            final var betweenString = text.substring(cursor, matcher.start());
            cursor = matcher.end();
            if (!betweenString.isEmpty()) {
                extras.add(new TextComponent(betweenString));
            }
            extras.add(replacement);
        } while (matcher.find());
        final var tailString = text.substring(cursor);
        if (!tailString.isEmpty()) {
            extras.add(new TextComponent(tailString));
        }
        return extras;
    }

}
