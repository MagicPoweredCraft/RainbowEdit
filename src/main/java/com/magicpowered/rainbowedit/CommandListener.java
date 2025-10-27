package com.magicpowered.rainbowedit;

import adapter.linlang.bukkit.common.Messenger;
import api.linlang.command.LinCommand;
import api.linlang.command.LinCommand.Desc;
import api.linlang.command.LinCommand.ExecTarget;
import api.linlang.command.LinCommand.Permission;
import api.linlang.file.service.LangService;
import com.magicpowered.rainbowedit.lang.LangKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用 LinCommand 注册 RainbowEdit 的所有命令。
 * 在插件主类 onEnable 中调用：
 * new CommandListener(plugin, fileManager, itemEditor).register(registry);
 */
public class CommandListener {

    private final RainbowEdit plugin;
    private final ItemEditor itemEditor;
    private final LangKeys lang;
    private final Messenger ms;

    public CommandListener(RainbowEdit plugin) {
        this.plugin = plugin;
        this.itemEditor = plugin.getItemEditor();
        this.ms = plugin.getMs();
        this.lang = plugin.getLang();
    }

    private static String normalize(String raw) {
        // 兼容原先的 "_" 表示空格，"\\_" 表示下划线
        StringBuilder out = new StringBuilder();
        boolean esc = false;
        for (char c : raw.toCharArray()) {
            if (esc) {
                out.append(c == '_' ? '_' : ('\\' + String.valueOf(c)));
                esc = false;
                continue;
            }
            if (c == '\\') {
                esc = true;
                continue;
            }
            out.append(c == '_' ? ' ' : c);
        }
        if (esc) out.append('\\');
        return out.toString().replace('&', '§');
    }

    public void register(LinCommand registry) {
        // /re name <String>
        registry.register(
                "re name <text:string{.+} @i18n>",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    String name = normalize(ctx.get("text"));
                    itemEditor.setName(p, name);
                },
                Permission.perms("rainbowedit.name"),
                ExecTarget.PLAYER,
                Desc.desc("zh_CN", "修改名字", "en_GB", "edit name"),
                LinCommand.Labels.create()
                        .add("text", "zh_CN", "新名称")
                        .add("text", "en_GB", "new name")
        );
        // 符号映射：/re ! <String>
        registry.register(
                "re ! <text:string{.+} @i18n>",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    String name = normalize(ctx.get("text"));
                    itemEditor.setName(p, name);
                },
                Permission.perms("rainbowedit.name"), ExecTarget.PLAYER, Desc.desc("zh_CN", "修改名字", "en_GB", "edit name"),
                LinCommand.Labels.create()
                        .add("text", "zh_CN", "新名称")
                        .add("text", "en_GB", "new name")
        );

        // /re add <String>
        registry.register(
                "re add <line:string{.+} @i18n>",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    String lore = normalize(ctx.get("line"));
                    itemEditor.addLore(p, lore);
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "添加新的行", "en_GB", "add new lore"),
                LinCommand.Labels.create()
                        .add("line", "zh_CN", "添加的描述")
                        .add("line", "en_GB", "description to add")
        );
        // /re + <String>
        registry.register(
                "re + <line:string{.+} @i18n>",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    String lore = normalize(ctx.get("line"));
                    itemEditor.addLore(p, lore);
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "添加新的行", "en_GB", "add new lore"),
                LinCommand.Labels.create()
                        .add("line", "zh_CN", "添加的描述")
                        .add("line", "en_GB", "description to add")
        );

        // /re set <line:int[1..999]> <String>
        registry.register(
                "re set <idx:int[1..999] @i18n> <text:string{.+} @i18n>",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    int idx = ctx.get("idx");
                    String text = normalize(ctx.get("text"));
                    itemEditor.setLore(p, idx, text);
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "修改指定行", "en_GB", "edit specified lore"),
                LinCommand.Labels.create()
                        .add("idx", "zh_CN", "行号")
                        .add("idx", "en_GB", "line number")
                        .add("text", "zh_CN", "新的描述")
                        .add("text", "en_GB", "new description")
        );
        // /re @ <line> <String>
        registry.register(
                "re @ <idx:int[1..999] @i18n> <text:string{.+} @i18n>",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    int idx = ctx.get("idx");
                    String text = normalize(ctx.get("text"));
                    itemEditor.setLore(p, idx, text);
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "修改指定行", "en_GB", "edit specified lore"),
                LinCommand.Labels.create()
                        .add("idx", "zh_CN", "行号")
                        .add("idx", "en_GB", "line number")
                        .add("text", "zh_CN", "新的描述")
                        .add("text", "en_GB", "new description")
        );

        // /re before <line> <String>
        registry.register(
                "re before <idx:int[1..999] @i18n> <text:string{.+} @i18n>",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    int idx = ctx.get("idx");
                    String text = normalize(ctx.get("text"));
                    itemEditor.insertLoreBefore(p, idx, text);
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "在指定行前插入一行", "en_GB", "Insert a new lore before the specified lore"),
                LinCommand.Labels.create()
                        .add("idx", "zh_CN", "行号")
                        .add("idx", "en_GB", "line number")
                        .add("text", "zh_CN", "要插入的描述")
                        .add("text", "en_GB", "description to insert")
        );
        // /re > <line> <String>
        registry.register(
                "re > <idx:int[1..999] @i18n> <text:string{.+} @i18n>",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    int idx = ctx.get("idx");
                    String text = normalize(ctx.get("text"));
                    itemEditor.insertLoreBefore(p, idx, text);
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "在指定行前插入一行", "en_GB", "Insert a new lore before the specified lore"),
                LinCommand.Labels.create()
                        .add("idx", "zh_CN", "行号")
                        .add("idx", "en_GB", "line number")
                        .add("text", "zh_CN", "要插入的描述")
                        .add("text", "en_GB", "description to insert")
        );

        // /re after <line> <String>
        registry.register(
                "re after <idx:int[1..999] @i18n> <text:string{.+} @i18n>",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    int idx = ctx.get("idx");
                    String text = normalize(ctx.get("text"));
                    itemEditor.insertLoreAfter(p, idx, text);
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "在指定行后插入一行", "en_GB", "Insert a new lore after the specified lore"),
                LinCommand.Labels.create()
                        .add("idx", "zh_CN", "行号")
                        .add("idx", "en_GB", "line number")
                        .add("text", "zh_CN", "要插入的描述")
                        .add("text", "en_GB", "description to insert")
        );
        // /re < <line> <String>
        registry.register(
                "re `<` <idx:int[1..999] @i18n> <text:string{.+} @i18n>",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    int idx = ctx.get("idx");
                    String text = normalize(ctx.get("text"));
                    itemEditor.insertLoreAfter(p, idx, text);
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "在指定行后插入一行", "en_GB", "Insert a new lore after the specified lore"),
                LinCommand.Labels.create()
                        .add("idx", "zh_CN", "行号")
                        .add("idx", "en_GB", "line number")
                        .add("text", "zh_CN", "要插入的描述")
                        .add("text", "en_GB", "lore to insert")
        );

        // /re replace <line> <old> <new>
        registry.register(
                "re replace <idx:int[1..999] @i18n> <old:string{.+} @i18n> <text:string{.+} @i18n>",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    int idx = ctx.get("idx");
                    String oldText = normalize(ctx.get("old"));
                    String newText = normalize(ctx.get("text"));
                    itemEditor.replaceInLore(p, idx, oldText, newText);
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "替换指定行中的字符", "en_GB", "replace specified string in the specified line"),
                LinCommand.Labels.create()
                        .add("idx", "zh_CN", "行号")
                        .add("idx", "en_GB", "line number")
                        .add("old", "zh_CN", "旧文本")
                        .add("old", "en_GB", "old text")
                        .add("text", "zh_CN", "新文本")
                        .add("text", "en_GB", "new text")
        );
        // /re # <line> <old> <new>
        registry.register(
                "re # <idx:int[1..999] @i18n> <old:string{.+} @i18n> <text:string{.+} @i18n>",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    int idx = ctx.get("idx");
                    String oldText = normalize(ctx.get("old"));
                    String newText = normalize(ctx.get("text"));
                    itemEditor.replaceInLore(p, idx, oldText, newText);
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "替换指定行中的字符", "en_GB", "replace specified string in the specified line"),
                LinCommand.Labels.create()
                        .add("idx", "zh_CN", "行号")
                        .add("idx", "en_GB", "line number")
                        .add("old", "zh_CN", "旧文本")
                        .add("old", "en_GB", "old text")
                        .add("text", "zh_CN", "新文本")
                        .add("text", "en_GB", "new text")
        );

        // /re remove <line>
        registry.register(
                "re remove <idx:int[1..999] @i18n>",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    int idx = ctx.get("idx");
                    itemEditor.removeLoreLine(p, idx);
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "删除指定行", "en_GB", "remove specified lore line"),
                LinCommand.Labels.create()
                        .add("idx", "zh_CN", "行号")
                        .add("idx", "en_GB", "line number")
        );
        // /re - <line>
        registry.register(
                "re - <idx:int[1..999] @i18n>",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    int idx = ctx.get("idx");
                    itemEditor.removeLoreLine(p, idx);
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "删除指定行", "en_GB", "remove specified lore line"),
                LinCommand.Labels.create()
                        .add("idx", "zh_CN", "行号")
                        .add("idx", "en_GB", "line number")
        );

        // /re clear
        registry.register(
                "re clear",
                ctx -> {
                    itemEditor.clearLore((Player) ctx.sender());
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "清除所有的Lore", "en_GB", "clear all lore")
        );
        // /re %
        registry.register(
                "re %",
                ctx -> itemEditor.clearLore((Player) ctx.sender()),
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "清除所有的Lore", "en_GB", "clear all lore")
        );

        // /re preview
        registry.register(
                "re preview",
                ctx -> itemEditor.enterPreviewMode((Player) ctx.sender()),
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "进入预览模式", "en_GB", "enter preview mode")
        );
        // /re $
        registry.register(
                "re $",
                ctx -> itemEditor.enterPreviewMode((Player) ctx.sender()),
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "进入预览模式",  "en_GB", "enter preview mode")
        );

        // /re apply
        registry.register(
                "re apply",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    if (!itemEditor.isInPreviewMode(p)) {
                        p.sendMessage("§7错误, 您并不处于预览模式中");
                        return;
                    }
                    itemEditor.applyPreviewChanges(p);
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "应用更改并退出预览模式",  "en_GB", "apply changes and exit preview mode")
        );
        // /re .
        registry.register(
                "re .",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    if (!itemEditor.isInPreviewMode(p)) {
                        p.sendMessage("§7错误, 您并不处于预览模式中");
                        return;
                    }
                    itemEditor.applyPreviewChanges(p);
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "应用更改并退出预览模式",  "en_GB", "apply changes and exit preview mode")
        );

        // /re cancel
        registry.register(
                "re cancel",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    if (!itemEditor.isInPreviewMode(p)) {
                        p.sendMessage("§7错误, 您并不处于预览模式中");
                        return;
                    }
                    itemEditor.cancelPreviewChanges(p);
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "取消更改并退出预览模式", "en_GB", "cancel changes and exit preview mode")
        );
        // /re :
        registry.register(
                "re :",
                ctx -> {
                    Player p = (Player) ctx.sender();
                    if (!itemEditor.isInPreviewMode(p)) {
                        p.sendMessage("§7错误, 您并不处于预览模式中");
                        return;
                    }
                    itemEditor.cancelPreviewChanges(p);
                },
                Permission.perms("rainbowedit.lore"), ExecTarget.PLAYER, Desc.desc("zh_CN", "取消更改并退出预览模式", "en_GB", "cancel changes and exit preview mode")
        );

        // /re reload
        registry.register(
                "re reload",
                ctx -> {
                    CommandSender s = (CommandSender) ctx.sender();
                    plugin.reload();
                    ms.sendKey(s, lang.message.reloaded);
                },
                Permission.perms("rainbowedit.reload"), ExecTarget.ALL, Desc.desc("zh_CN", "重新载入配置文件", "en_GB", "reload all plugin files")
        );
    }
}
