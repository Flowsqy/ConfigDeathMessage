package fr.flowsqy.configdeathmessage.message;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ComponentReplacer {

    @NotNull
    public BaseComponent replace(@NotNull BaseComponent message, @NotNull String regex,
            @NotNull BaseComponent replacement) {
        return replace(message, Pattern.compile(regex), replacement);
    }

    @NotNull
    public BaseComponent replace(@NotNull BaseComponent message, @NotNull Pattern pattern,
            @NotNull BaseComponent replacement) {
        final var newComponent = new TextComponent();
        newComponent.copyFormatting(message);
        if (message instanceof TextComponent textComponent) {
            final var matcher = pattern.matcher(textComponent.getText());
            // TODO Build a new TextComponent with extra named after
            /*
             * boolean result = matcher.find();
             * if (result) {
             * do {
             * matcher.start();
             * appendReplacement(sb, replacement);
             * result = find();
             * } while (result);
             * appendTail(sb);
             * return sb.toString();
             * }
             * return text.toString();
             */
        }
        final var extras = new ArrayList<BaseComponent>(message.getExtra().size());
        for (var extra : message.getExtra()) {
            extras.add(replace(extra, pattern, replacement));
        }
        newComponent.setExtra(extras);
        return newComponent;
    }
}
