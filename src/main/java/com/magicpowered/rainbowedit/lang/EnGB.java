package com.magicpowered.rainbowedit.lang;


import api.linlang.file.implement.LocaleProvider;
import api.linlang.file.annotations.NamingStyle;

@NamingStyle(NamingStyle.Style.KEBAB)
public final class EnGB implements LocaleProvider<LangKeys> {
    public String locale(){ return "en_GB"; }
    public void define(LangKeys k) {

        k.message.prefix = "§7[§bRainbowEdit§7]";
        k.message.noItemInHand = "&fYou have no item in your main hand";
        k.message.itemHasNoLore = "&fThis item has no lore";
        k.message.nameSetup = "&fSuccessfully set the item name to {name}";
        k.message.invalidLoreLine = "&fThis item doesn't have the specified lore line {line}";
        k.message.loreAdded = "&fSuccessfully added new lore line {lore}";
        k.message.oldStringNotFound = "&fCould not find the string to replace: {old_lore}";
        k.message.loreReplaced = "&fSuccessfully replaced lore {old_lore} -> {new_lore}";
        k.message.loreLineSetup = "&fSuccessfully set line {line} to {lore}";
        k.message.loreLineInserted = "&fSuccessfully inserted {lore} at {insert_mode}";
        k.message.loreLineRemoved = "&fSuccessfully removed lore line {lore}";
        k.message.loreCleared = "&fSuccessfully cleared all lore lines";
        k.message.previewEnter = "&fEntered preview mode. Use /re . to apply and exit; use /re : to cancel and exit.";
        k.message.previewIsDisable = "&fYou are not in preview mode";
        k.message.previewAlreadyEnabled = "&fYou are already in preview mode";
        k.message.appliedPreviewChanges = "&fApplied changes to the item. Preview mode exited";
        k.message.canceledPreviewChanges = "&fYou cancelled the changes in preview mode. Preview mode exited";
        k.message.cannotMovePreviewItem = "&fYou cannot move the preview item while in preview mode";
        k.message.cannotDropPreviewItem = "&fYou cannot drop the preview item while in preview mode";
        k.message.noItemInHotbar = "&fYou have no item in your hotbar";

        k.previewUI.frames.put(1 , "&bEditing &7[&c&l↓&7---------------------------] &bPreview Mode");
        k.previewUI.frames.put(2 , "&bEditing &7[----&c&l↓&7-----------------------] &bPreview Mode");
        k.previewUI.frames.put(3 , "&bEditing &7[-------&c&l↓&7--------------------] &bPreview Mode");
        k.previewUI.frames.put(4 , "&bEditing &7[----------&c&l↓&7-----------------] &bPreview Mode");
        k.previewUI.frames.put(5 , "&bEditing &7[--------------&c&l↓&7-------------] &bPreview Mode");
        k.previewUI.frames.put(6 , "&bEditing &7[-----------------&c&l↓&7----------] &bPreview Mode");
        k.previewUI.frames.put(7 , "&bEditing &7[--------------------&c&l↓&7-------] &bPreview Mode");
        k.previewUI.frames.put(8 , "&bEditing &7[------------------------&c&l↓&7---] &bPreview Mode");
        k.previewUI.frames.put(9 , "&bEditing &7[---------------------------&c&l↓&7] &bPreview Mode");

        }
}