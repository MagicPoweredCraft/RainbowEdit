package com.magicpowered.rainbowedit.lang;


import api.linlang.file.annotations.NamingStyle;
import api.linlang.file.implement.LocaleProvider;

@NamingStyle(NamingStyle.Style.KEBAB)
public final class ZhCN implements LocaleProvider<LangKeys> {
    public String locale(){ return "zh_CN"; }
    public void define(LangKeys k) {

        k.message.prefix = "§7[§e彩虹编辑§7]";
        k.message.noItemInHand = "&f您的主手中没有物品";
        k.message.itemHasNoLore = "&f此物品没有描述行";
        k.message.nameSetup = "&f成功设置物品名为 {name}";
        k.message.invalidLoreLine = "&f此物品没有指定的描述行 {line}";
        k.message.loreAdded = "&f成功添加新的描述行 {lore}";
        k.message.oldStringNotFound = "&f未找到要被替换的字符串 {old_lore}";
        k.message.loreReplaced = "&f成功替换 {line} 行 {old_lore} -> {new_lore}";
        k.message.loreLineSetup = "&f成功将第 {line} 设置为 {lore}";
        k.message.loreLineInserted = "&f成功在 {line} 行之 {insert_mode} 插入 {lore}";
        k.message.loreLineRemoved = "&f成功删除了描述行 {lore}";
        k.message.loreCleared = "&f成功清空了所有描述行";
        k.message.previewEnter = "&f已进入预览模式。使用 /re . 应用并退出；使用 /re : 撤销并退出。";
        k.message.previewIsDisable = "&f您未处于预览模式中";
        k.message.previewAlreadyEnabled = "&f您已经处于预览模式中";
        k.message.appliedPreviewChanges = "&f成功将修改应用至物品，预览模式已退出";
        k.message.canceledPreviewChanges = "&f您撤销了预览模式中的修改，预览模式已退出";
        k.message.cannotMovePreviewItem = "&f在预览模式下，您无法移动预览物品";
        k.message.cannotDropPreviewItem = "&f在预览模式下，您无法丢弃预览物品";
        k.message.noItemInHotbar = "&f您的快捷栏中没有任何物品";
        k.message.reloaded = "&f配置文件重新加载成功";

        k.previewUI.frames.put(1 , "&b彩虹编辑 &7[&c&l↓&7---------------------------] &b预览模式");
        k.previewUI.frames.put(2 , "&b彩虹编辑 &7[----&c&l↓&7-----------------------] &b预览模式");
        k.previewUI.frames.put(3 , "&b彩虹编辑 &7[-------&c&l↓&7--------------------] &b预览模式");
        k.previewUI.frames.put(4 , "&b彩虹编辑 &7[----------&c&l↓&7-----------------] &b预览模式");
        k.previewUI.frames.put(5 , "&b彩虹编辑 &7[--------------&c&l↓&7-------------] &b预览模式");
        k.previewUI.frames.put(6 , "&b彩虹编辑 &7[-----------------&c&l↓&7----------] &b预览模式");
        k.previewUI.frames.put(7 , "&b彩虹编辑 &7[--------------------&c&l↓&7-------] &b预览模式");
        k.previewUI.frames.put(8 , "&b彩虹编辑 &7[------------------------&c&l↓&7---] &b预览模式");
        k.previewUI.frames.put(9 , "&b彩虹编辑 &7[---------------------------&c&l↓&7] &b预览模式");

        }
}