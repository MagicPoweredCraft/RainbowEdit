package com.magicpowered.rainbowedit.lang;

import api.linlang.file.file.FileType;
import api.linlang.file.file.LangMap;
import api.linlang.file.file.LangText;
import api.linlang.file.file.annotations.LangPack;
import api.linlang.file.file.annotations.NamingStyle;

/**
 * RainbowEdit 的语言字段结构。
 */
@LangPack(filePath = "lang", format = FileType.YAML, defaultLocale = "zh_CN")
@NamingStyle(NamingStyle.Style.KEBAB)
public final class LangKeys {

    public Message message = new Message();
    public Command command = new Command();
    public PreviewUi previewUi = new PreviewUi();

    /**
     * 插件运行时消息。
     */
    @NamingStyle(NamingStyle.Style.KEBAB)
    public static final class Message {
        public LangText prefix = LangText.of("&7[&e彩虹编辑&7]&r ");
        public LangText noItemInHand = LangText.of("&f您的主手中没有物品");
        public LangText itemHasNoLore = LangText.of("&f此物品没有描述行");
        public LangText nameSetup = LangText.of("&f成功设置物品名为 {name}");
        public LangText invalidLoreLine = LangText.of("&f此物品没有指定的描述行 {line}");
        public LangText loreAdded = LangText.of("&f成功添加新的描述行 {lore}");
        public LangText oldStringNotFound = LangText.of("&f未找到要被替换的字符串 {old_lore}");
        public LangText loreReplaced = LangText.of("&f成功替换第 {line} 行：{old_lore} -> {new_lore}");
        public LangText loreLineSetup = LangText.of("&f成功将第 {line} 行设置为 {lore}");
        public LangText loreLineInserted = LangText.of("&f成功在第 {line} 行之{insert_mode}插入 {lore}");
        public LangText loreLineRemoved = LangText.of("&f成功删除描述行 {lore}");
        public LangText loreCleared = LangText.of("&f成功清空所有描述行");
        public LangText previewEnter = LangText.of("&f已进入预览模式。使用 /re . 应用，或使用 /re : 撤销。");
        public LangText previewIsDisable = LangText.of("&f您未处于预览模式中");
        public LangText previewAlreadyEnabled = LangText.of("&f您已经处于预览模式中");
        public LangText appliedPreviewChanges = LangText.of("&f修改已应用，预览模式已退出");
        public LangText canceledPreviewChanges = LangText.of("&f修改已撤销，预览模式已退出");
        public LangText cannotMovePreviewItem = LangText.of("&f预览模式下无法移动或切换预览物品");
        public LangText cannotDropPreviewItem = LangText.of("&f预览模式下无法丢弃预览物品");
        public LangText noItemInHotbar = LangText.of("&f您的快捷栏中没有预览物品");
        public LangText reloaded = LangText.of("&f配置文件与语言文件已重新载入");
        public LangText currentLanguage = LangText.of("&f当前语言：{locale}");
        public LangText insertBefore = LangText.of("前");
        public LangText insertAfter = LangText.of("后");
    }

    /**
     * 命令帮助及参数标签。
     */
    @NamingStyle(NamingStyle.Style.KEBAB)
    public static final class Command {
        public LangText name = LangText.of("修改物品名称");
        public LangText add = LangText.of("添加新的描述行");
        public LangText set = LangText.of("修改指定描述行");
        public LangText before = LangText.of("在指定描述行前插入一行");
        public LangText after = LangText.of("在指定描述行后插入一行");
        public LangText replace = LangText.of("替换指定描述行中的文本");
        public LangText remove = LangText.of("删除指定描述行");
        public LangText clear = LangText.of("清空所有描述行");
        public LangText preview = LangText.of("进入预览模式");
        public LangText apply = LangText.of("应用修改并退出预览模式");
        public LangText cancel = LangText.of("撤销修改并退出预览模式");
        public LangText language = LangText.of("查看当前语言");
        public LangText testing = LangText.of("验证琳琅消息服务");
        public LangText reload = LangText.of("重新载入配置文件与语言文件");
        public LangText text = LangText.of("文本内容");
        public LangText line = LangText.of("描述行号");
        public LangText lore = LangText.of("描述内容");
        public LangText oldText = LangText.of("要替换的旧文本");
        public LangText newText = LangText.of("替换后的新文本");
    }

    /**
     * 预览模式动作栏内容。
     */
    @NamingStyle(NamingStyle.Style.KEBAB)
    public static final class PreviewUi {
        public LangMap frames = LangMap.of(
                "1", "&b彩虹编辑 &7[&c&l↓&7---------------------------] &b预览模式",
                "2", "&b彩虹编辑 &7[----&c&l↓&7-----------------------] &b预览模式",
                "3", "&b彩虹编辑 &7[-------&c&l↓&7--------------------] &b预览模式",
                "4", "&b彩虹编辑 &7[----------&c&l↓&7-----------------] &b预览模式",
                "5", "&b彩虹编辑 &7[--------------&c&l↓&7-------------] &b预览模式",
                "6", "&b彩虹编辑 &7[-----------------&c&l↓&7----------] &b预览模式",
                "7", "&b彩虹编辑 &7[--------------------&c&l↓&7-------] &b预览模式",
                "8", "&b彩虹编辑 &7[------------------------&c&l↓&7---] &b预览模式",
                "9", "&b彩虹编辑 &7[---------------------------&c&l↓&7] &b预览模式"
        );
    }
}
