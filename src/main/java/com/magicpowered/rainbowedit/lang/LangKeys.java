package com.magicpowered.rainbowedit.lang;

import api.linlang.file.annotations.Comment;
import api.linlang.file.annotations.I18nComment;
import api.linlang.file.annotations.NamingStyle;

import java.util.LinkedHashMap;

@NamingStyle(NamingStyle.Style.KEBAB)
@I18nComment(locale = "zh_CN", lines = {"中文（中国大陆）语言文件"})
@I18nComment(locale = "en_GB", lines = {"English (UK) language file"})
public class LangKeys {
    @I18nComment(locale = "zh_CN", lines = {"消息相关"})
    @I18nComment(locale = "en_GB", lines = {"Message section"})
    public Message message = new Message();
    
    @I18nComment(locale = "zh_CN", lines = {"预览模式提示"})
    @I18nComment(locale = "en_GB", lines = {"Preview mode prompt"})
    public PreviewUi previewUI = new PreviewUi();

    
    @NamingStyle(NamingStyle.Style.KEBAB)
    public static class Message {

        @I18nComment(locale = "zh_CN", lines = {"插件消息前缀"})
        @I18nComment(locale = "en_GB", lines = {"Prefix for all plugin messages"})
        public String prefix;

        @I18nComment(locale = "zh_CN", lines = {"主手为空时提示"})
        @I18nComment(locale = "en_GB", lines = {"Shown when main-hand is empty"})
        public String noItemInHand;
        @I18nComment(locale = "zh_CN", lines = {"物品无 lore 时提示"})
        @I18nComment(locale = "en_GB", lines = {"Shown when item has no lore"})
        public String itemHasNoLore;
        @I18nComment(locale = "zh_CN", lines = {"设置物品名称成功时提示"})
        @I18nComment(locale = "en_GB", lines = {"Shown when item display name is set successfully"})
        public String nameSetup;
        @I18nComment(locale = "zh_CN", lines = {"指定的 lore 行不存在时提示"})
        @I18nComment(locale = "en_GB", lines = {"Shown when the specified lore line does not exist"})
        public String invalidLoreLine;
        @I18nComment(locale = "zh_CN", lines = {"成功添加新的 lore 行时提示"})
        @I18nComment(locale = "en_GB", lines = {"Shown when a new lore line is added successfully"})
        public String loreAdded;
        @I18nComment(locale = "zh_CN", lines = {"替换时未找到旧的字串"})
        @I18nComment(locale = "en_GB", lines = {"Shown when the old substring was not found during replace"})
        public String oldStringNotFound;
        @I18nComment(locale = "zh_CN", lines = {"替换 lore 成功"})
        @I18nComment(locale = "en_GB", lines = {"Shown when lore text was replaced successfully"})
        public String loreReplaced;
        @I18nComment(locale = "zh_CN", lines = {"设置某一行 lore 成功"})
        @I18nComment(locale = "en_GB", lines = {"Shown when a lore line is set successfully"})
        public String loreLineSetup;
        @I18nComment(locale = "zh_CN", lines = {"在指定行插入 lore 成功；{insert_mode} = BEFORE/AFTER"})
        @I18nComment(locale = "en_GB", lines = {"Shown when lore was inserted at the specified line; {insert_mode} = BEFORE/AFTER"})
        public String loreLineInserted;
        @I18nComment(locale = "zh_CN", lines = {"删除某一行 lore 成功"})
        @I18nComment(locale = "en_GB", lines = {"Shown when a lore line is removed successfully"})
        public String loreLineRemoved;
        @I18nComment(locale = "zh_CN", lines = {"清空 lore"})
        @I18nComment(locale = "en_GB", lines = {"Shown when all lore lines were cleared"})
        public String loreCleared;
        @I18nComment(locale = "zh_CN", lines = {"进入预览模式"})
        @I18nComment(locale = "en_GB", lines = {"Shown when entering preview mode"})
        public String previewEnter;
        @I18nComment(locale = "zh_CN", lines = {"不在预览模式时，操作退出预览模式的提示"})
        @I18nComment(locale = "en_GB", lines = {"Shown when trying to exit preview mode while not in preview mode"})
        public String previewIsDisable;
        @I18nComment(locale = "zh_CN", lines = {"在预览模式时，操作进入预览模式的提示"})
        @I18nComment(locale = "en_GB", lines = {"Shown when trying to enter preview mode while already in preview mode"})
        public String previewAlreadyEnabled;
        @I18nComment(locale = "zh_CN", lines = {"退出预览模式，并且应用修改"})
        @I18nComment(locale = "en_GB", lines = {"Shown when exiting preview mode and applying changes"})
        public String appliedPreviewChanges;
        @I18nComment(locale = "zh_CN", lines = {"退出预览模式，但是撤销修改"})
        @I18nComment(locale = "en_GB", lines = {"Shown when exiting preview mode and discarding changes"})
        public String canceledPreviewChanges;
        @I18nComment(locale = "zh_CN", lines = {"预览模式禁止移动物品"})
        @I18nComment(locale = "en_GB", lines = {"Shown when moving items is blocked in preview mode"})
        public String cannotMovePreviewItem;
        @I18nComment(locale = "zh_CN", lines = {"预览模式禁止丢弃物品"})
        @I18nComment(locale = "en_GB", lines = {"Shown when dropping items is blocked in preview mode"})
        public String cannotDropPreviewItem;
        @I18nComment(locale = "zh_CN", lines = {"快捷栏中没有任何物品"})
        @I18nComment(locale = "en_GB", lines = {"There are no items in the hotbar"})
        public String noItemInHotbar;

        @I18nComment(locale = "zh_CN", lines = "配置文件重新加载成功")
        @I18nComment(locale = "en_GB", lines = "reloaded all plugin files")
        public String reloaded;
    }

    @NamingStyle(NamingStyle.Style.IDENTITY)
    public static class PreviewUi {
        public LinkedHashMap<Integer, String> frames = new LinkedHashMap<Integer, String>() ;
    }


}