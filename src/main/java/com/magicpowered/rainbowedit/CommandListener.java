package com.magicpowered.rainbowedit;

import api.linlang.command.LinCommand;
import api.linlang.command.group.CommandFailure;
import api.linlang.command.group.CommandRoot;
import api.linlang.file.file.LangText;
import com.magicpowered.rainbowedit.lang.LangKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static api.linlang.command.CommandOptions.options;

/**
 * RainbowEdit 命令注册器。
 */
public final class CommandListener {

    private final RainbowEdit plugin;
    private final ItemEditor itemEditor;

    public CommandListener(RainbowEdit plugin) {
        this.plugin = plugin;
        this.itemEditor = plugin.getItemEditor();
    }

    /**
     * 注册插件根命令及其所有叶子命令。
     *
     * @param registry 琳琅命令服务
     */
    public void register(LinCommand registry) {
        CommandRoot root = registry.root("re")
                .permissionPrefix("rainbowedit.use")
                .onSuccess(context -> plugin.getLin().linAudit().record(
                        "rainbowedit.command.succeeded",
                        "sender", senderName(context.sender())
                ))
                .onFailure(this::auditFailure);

        registerNameCommands(root);
        registerLoreCommands(root);
        registerPreviewCommands(root);
        registerServiceCommands(root);
    }

    private void registerNameCommands(CommandRoot root) {
        registerName(root, "name");
        registerName(root, "!");
    }

    private void registerName(CommandRoot root, String literal) {
        LangKeys.Command text = plugin.getLanguage().command;
        root.register(
                literal + " <text:text(regex=.+)>",
                context -> itemEditor.setName(
                        (Player) context.sender(),
                        normalize(context.get("text"))
                ),
                options()
                        .relativePermission("name")
                        .player()
                        .i18n(text.name, "text", text.text)
        );
    }

    private void registerLoreCommands(CommandRoot root) {
        registerAdd(root, "add");
        registerAdd(root, "+");
        registerSet(root, "set");
        registerSet(root, "@");
        registerInsert(root, "before", true);
        registerInsert(root, ">", true);
        registerInsert(root, "after", false);
        registerInsert(root, "`<`", false);
        registerReplace(root, "replace");
        registerReplace(root, "#");
        registerRemove(root, "remove");
        registerRemove(root, "-");
        registerSimpleLore(root, "clear", plugin.getLanguage().command.clear,
                player -> itemEditor.clearLore(player));
        registerSimpleLore(root, "%", plugin.getLanguage().command.clear,
                player -> itemEditor.clearLore(player));
    }

    private void registerAdd(CommandRoot root, String literal) {
        LangKeys.Command text = plugin.getLanguage().command;
        root.register(
                literal + " <line:text(regex=.+)>",
                context -> itemEditor.addLore(
                        (Player) context.sender(),
                        normalize(context.get("line"))
                ),
                options()
                        .relativePermission("lore")
                        .player()
                        .i18n(text.add, "line", text.lore)
        );
    }

    private void registerSet(CommandRoot root, String literal) {
        LangKeys.Command text = plugin.getLanguage().command;
        root.register(
                literal + " <idx:int(1..999)> <text:text(regex=.+)>",
                context -> itemEditor.setLore(
                        (Player) context.sender(),
                        context.get("idx"),
                        normalize(context.get("text"))
                ),
                options()
                        .relativePermission("lore")
                        .player()
                        .i18n(text.set, "idx", text.line, "text", text.text)
        );
    }

    private void registerInsert(CommandRoot root, String literal, boolean before) {
        LangKeys.Command text = plugin.getLanguage().command;
        LangText description = before ? text.before : text.after;
        root.register(
                literal + " <idx:int(1..999)> <text:text(regex=.+)>",
                context -> {
                    Player player = (Player) context.sender();
                    int line = context.get("idx");
                    String value = normalize(context.get("text"));
                    if (before) {
                        itemEditor.insertLoreBefore(player, line, value);
                    } else {
                        itemEditor.insertLoreAfter(player, line, value);
                    }
                },
                options()
                        .relativePermission("lore")
                        .player()
                        .i18n(description, "idx", text.line, "text", text.lore)
        );
    }

    private void registerReplace(CommandRoot root, String literal) {
        LangKeys.Command text = plugin.getLanguage().command;
        root.register(
                literal + " <idx:int(1..999)> <old:string(regex=.+)> <text:string(regex=.+)>",
                context -> itemEditor.replaceInLore(
                        (Player) context.sender(),
                        context.get("idx"),
                        normalize(context.get("old")),
                        normalize(context.get("text"))
                ),
                options().relativePermission("lore").player().i18n(
                        text.replace,
                        "idx", text.line,
                        "old", text.oldText,
                        "text", text.newText
                )
        );
    }

    private void registerRemove(CommandRoot root, String literal) {
        LangKeys.Command text = plugin.getLanguage().command;
        root.register(
                literal + " <idx:int(1..999)>",
                context -> itemEditor.removeLoreLine(
                        (Player) context.sender(),
                        context.get("idx")
                ),
                options().relativePermission("lore").player().i18n(text.remove, "idx", text.line)
        );
    }

    private void registerPreviewCommands(CommandRoot root) {
        registerSimpleLore(root, "preview", plugin.getLanguage().command.preview,
                player -> itemEditor.enterPreviewMode(player));
        registerSimpleLore(root, "$", plugin.getLanguage().command.preview,
                player -> itemEditor.enterPreviewMode(player));
        registerSimpleLore(root, "apply", plugin.getLanguage().command.apply,
                player -> itemEditor.applyPreviewChanges(player));
        registerSimpleLore(root, ".", plugin.getLanguage().command.apply,
                player -> itemEditor.applyPreviewChanges(player));
        registerSimpleLore(root, "cancel", plugin.getLanguage().command.cancel,
                player -> itemEditor.cancelPreviewChanges(player));
        registerSimpleLore(root, ":", plugin.getLanguage().command.cancel,
                player -> itemEditor.cancelPreviewChanges(player));
    }

    private void registerSimpleLore(CommandRoot root, String literal, LangText description,
                                    PlayerAction action) {
        root.register(
                literal,
                context -> action.run((Player) context.sender()),
                options().relativePermission("lore").player().desc(description)
        );
    }

    private void registerServiceCommands(CommandRoot root) {
        LangKeys.Command text = plugin.getLanguage().command;
        root.register(
                "language",
                context -> plugin.getMessenger().send(
                        context.sender(),
                        plugin.getLanguage().message.currentLanguage,
                        "locale", plugin.getConfigData().language
                ),
                options()
                        .permission("rainbowedit.language")
                        .all()
                        .desc(text.language)
        );
        root.register(
                "testing",
                context -> plugin.getMessenger().send(
                        context.sender(),
                        plugin.getConfigData().testing
                ),
                options()
                        .permission("rainbowedit.testing")
                        .all()
                        .desc(text.testing)
        );
        root.register(
                "reload",
                context -> {
                    plugin.reloadServices();
                    plugin.getMessenger().send(
                            context.sender(),
                            plugin.getLanguage().message.reloaded
                    );
                },
                options()
                        .permission("rainbowedit.reload")
                        .all()
                        .desc(text.reload)
        );
    }

    private void auditFailure(CommandFailure failure) {
        plugin.getLin().linAudit().record(
                "rainbowedit.command.failed",
                "sender", senderName(failure.context().sender()),
                "command", failure.command(),
                "reason", failure.reason().name(),
                "cause", failure.cause() == null ? "" : failure.cause().getClass().getName()
        );
    }

    private static String senderName(Object sender) {
        if (sender instanceof CommandSender commandSender) {
            return commandSender.getName();
        }
        return String.valueOf(sender);
    }

    private static String normalize(String raw) {
        StringBuilder output = new StringBuilder();
        boolean escaped = false;
        for (char character : raw.toCharArray()) {
            if (escaped) {
                output.append(character == '_' ? '_' : "\\" + character);
                escaped = false;
                continue;
            }
            if (character == '\\') {
                escaped = true;
                continue;
            }
            output.append(character == '_' ? ' ' : character);
        }
        if (escaped) {
            output.append('\\');
        }
        return output.toString().replace('&', '§');
    }

    @FunctionalInterface
    private interface PlayerAction {
        void run(Player player);
    }
}
