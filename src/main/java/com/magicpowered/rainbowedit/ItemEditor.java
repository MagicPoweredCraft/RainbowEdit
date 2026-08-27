package com.magicpowered.rainbowedit;

import api.linlang.file.file.LangText;
import api.linlang.messenger.LinMessenger;
import com.magicpowered.rainbowedit.lang.LangKeys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 处理物品名称、描述和预览状态。
 */
public final class ItemEditor implements Listener {

    private final RainbowEdit plugin;
    private final Map<UUID, ItemStack> previewBackup = new HashMap<>();
    private final Map<UUID, Integer> previewTaskIds = new HashMap<>();
    private final Map<UUID, Integer> previewItemSlots = new HashMap<>();

    public ItemEditor(RainbowEdit plugin) {
        this.plugin = plugin;
    }

    /**
     * 为玩家手中的物品设置名称。
     *
     * @param player 玩家
     * @param name 新的物品名称
     */
    public void setName(Player player, String name) {
        ItemStack item = requireMainHandItem(player);
        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        messenger().send(player, language().message.nameSetup, "name", name);
    }

    /**
     * 向玩家手中的物品添加一行描述。
     *
     * @param player 玩家
     * @param loreToAdd 要添加的描述
     */
    public void addLore(Player player, String loreToAdd) {
        ItemStack item = requireMainHandItem(player);
        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        List<String> lore = mutableLore(meta);
        lore.add(loreToAdd);
        meta.setLore(lore);
        item.setItemMeta(meta);
        messenger().send(player, language().message.loreAdded, "lore", loreToAdd);
    }

    /**
     * 修改玩家手中物品的指定描述行。
     *
     * @param player 玩家
     * @param lineToSet 要修改的行号
     * @param newLore 新的描述内容
     */
    public void setLore(Player player, int lineToSet, String newLore) {
        ItemStack item = requireMainHandItem(player);
        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        List<String> lore = requireLore(player, meta);
        if (lore == null) {
            return;
        }
        if (!validLine(lore, lineToSet)) {
            invalidLine(player, lineToSet);
            return;
        }

        lore.set(lineToSet - 1, newLore);
        meta.setLore(lore);
        item.setItemMeta(meta);
        messenger().send(
                player,
                language().message.loreLineSetup,
                "line", lineToSet,
                "lore", newLore
        );
    }

    /**
     * 在指定描述行之前插入内容。
     *
     * @param player 玩家
     * @param line 插入位置
     * @param loreToInsert 要插入的内容
     */
    public void insertLoreBefore(Player player, int line, String loreToInsert) {
        insertLore(player, line, loreToInsert, true);
    }

    /**
     * 在指定描述行之后插入内容。
     *
     * @param player 玩家
     * @param line 插入位置
     * @param loreToInsert 要插入的内容
     */
    public void insertLoreAfter(Player player, int line, String loreToInsert) {
        insertLore(player, line, loreToInsert, false);
    }

    /**
     * 删除指定描述行。
     *
     * @param player 玩家
     * @param lineToRemove 要删除的行号
     */
    public void removeLoreLine(Player player, int lineToRemove) {
        ItemStack item = requireMainHandItem(player);
        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        List<String> lore = requireLore(player, meta);
        if (lore == null) {
            return;
        }
        if (!validLine(lore, lineToRemove)) {
            invalidLine(player, lineToRemove);
            return;
        }

        String removed = lore.remove(lineToRemove - 1);
        meta.setLore(lore);
        item.setItemMeta(meta);
        messenger().send(player, language().message.loreLineRemoved, "lore", removed);
    }

    /**
     * 删除所有描述行。
     *
     * @param player 玩家
     */
    public void clearLore(Player player) {
        ItemStack item = requireMainHandItem(player);
        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        meta.setLore(new ArrayList<>());
        item.setItemMeta(meta);
        messenger().send(player, language().message.loreCleared);
    }

    /**
     * 在指定描述行中替换文本。
     *
     * @param player 玩家
     * @param line 指定行号
     * @param oldString 要替换的文本
     * @param newString 新文本
     */
    public void replaceInLore(Player player, int line, String oldString, String newString) {
        ItemStack item = requireMainHandItem(player);
        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        List<String> lore = requireLore(player, meta);
        if (lore == null) {
            return;
        }
        if (!validLine(lore, line)) {
            invalidLine(player, line);
            return;
        }

        String currentLine = lore.get(line - 1);
        if (!currentLine.contains(oldString)) {
            messenger().send(
                    player,
                    language().message.oldStringNotFound,
                    "old_lore", oldString
            );
            return;
        }

        String replacedLine = currentLine.replace(oldString, newString);
        lore.set(line - 1, replacedLine);
        meta.setLore(lore);
        item.setItemMeta(meta);
        messenger().send(
                player,
                language().message.loreReplaced,
                "line", line,
                "old_lore", oldString,
                "new_lore", newString
        );
    }

    /**
     * 进入预览模式并备份当前物品。
     *
     * @param player 玩家
     */
    public void enterPreviewMode(Player player) {
        if (isInPreviewMode(player)) {
            messenger().send(player, language().message.previewAlreadyEnabled);
            return;
        }

        ItemStack item = requireMainHandItem(player);
        if (item == null) {
            return;
        }

        UUID playerId = player.getUniqueId();
        int slot = player.getInventory().getHeldItemSlot();
        if (slot < 0 || slot >= 9) {
            messenger().send(player, language().message.noItemInHotbar);
            return;
        }

        previewBackup.put(playerId, item.clone());
        previewItemSlots.put(playerId, slot);
        messenger().send(player, language().message.previewEnter);

        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            String frame = language().previewUi.frames.resolve().get(String.valueOf(slot + 1));
            if (frame != null && player.isOnline()) {
                messenger().actionBar(player, frame);
            }
        }, 0L, 20L);
        previewTaskIds.put(playerId, taskId);
    }

    /**
     * 应用修改并退出预览模式。
     *
     * @param player 玩家
     */
    public void applyPreviewChanges(Player player) {
        if (!isInPreviewMode(player)) {
            messenger().send(player, language().message.previewIsDisable);
            return;
        }
        cleanupPreviewMode(player.getUniqueId());
        messenger().send(player, language().message.appliedPreviewChanges);
    }

    /**
     * 恢复备份并退出预览模式。
     *
     * @param player 玩家
     */
    public void cancelPreviewChanges(Player player) {
        if (!isInPreviewMode(player)) {
            messenger().send(player, language().message.previewIsDisable);
            return;
        }
        restorePreview(player, true);
    }

    /**
     * 检查玩家是否处于预览模式。
     *
     * @param player 玩家
     * @return 是否处于预览模式
     */
    public boolean isInPreviewMode(Player player) {
        return previewBackup.containsKey(player.getUniqueId());
    }

    /**
     * 恢复所有预览物品并停止后台任务。
     */
    public void shutdown() {
        for (UUID playerId : Set.copyOf(previewBackup.keySet())) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                restorePreview(player, false);
            } else {
                cleanupPreviewMode(playerId);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (isInPreviewMode(event.getPlayer())) {
            restorePreview(event.getPlayer(), false);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player) || !isInPreviewMode(player)) {
            return;
        }
        Integer slot = previewItemSlots.get(player.getUniqueId());
        if (slot == null) {
            return;
        }

        boolean clickedPreviewSlot = event.getClickedInventory() != null
                && event.getClickedInventory().equals(player.getInventory())
                && event.getSlot() == slot;
        boolean hotbarSwapTargetsPreview = event.getHotbarButton() == slot;
        if (clickedPreviewSlot || hotbarSwapTargetsPreview) {
            event.setCancelled(true);
            messenger().send(player, language().message.cannotMovePreviewItem);
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Integer slot = previewItemSlots.get(event.getPlayer().getUniqueId());
        if (slot != null && event.getNewSlot() != slot) {
            event.setCancelled(true);
            messenger().send(event.getPlayer(), language().message.cannotMovePreviewItem);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (isInPreviewMode(event.getPlayer())) {
            event.setCancelled(true);
            messenger().send(event.getPlayer(), language().message.cannotMovePreviewItem);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Integer slot = previewItemSlots.get(player.getUniqueId());
        if (slot != null && player.getInventory().getHeldItemSlot() == slot) {
            event.setCancelled(true);
            messenger().send(player, language().message.cannotDropPreviewItem);
        }
    }

    private void insertLore(Player player, int line, String loreToInsert, boolean before) {
        ItemStack item = requireMainHandItem(player);
        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        List<String> lore = mutableLore(meta);
        boolean invalidBefore = before && (line < 1 || line > lore.size() + 1);
        boolean invalidAfter = !before && (line < 1 || line > lore.size());
        if (invalidBefore || invalidAfter) {
            invalidLine(player, line);
            return;
        }

        lore.add(before ? line - 1 : line, loreToInsert);
        meta.setLore(lore);
        item.setItemMeta(meta);
        LangText mode = before ? language().message.insertBefore : language().message.insertAfter;
        messenger().send(
                player,
                language().message.loreLineInserted,
                "insert_mode", mode,
                "line", line,
                "lore", loreToInsert
        );
    }

    private ItemStack requireMainHandItem(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) {
            messenger().send(player, language().message.noItemInHand);
            return null;
        }
        return item;
    }

    private List<String> requireLore(Player player, ItemMeta meta) {
        if (meta == null || !meta.hasLore() || meta.getLore() == null || meta.getLore().isEmpty()) {
            messenger().send(player, language().message.itemHasNoLore);
            return null;
        }
        return new ArrayList<>(meta.getLore());
    }

    private static List<String> mutableLore(ItemMeta meta) {
        List<String> lore = meta.getLore();
        return lore == null ? new ArrayList<>() : new ArrayList<>(lore);
    }

    private static boolean validLine(List<String> lore, int line) {
        return line >= 1 && line <= lore.size();
    }

    private void invalidLine(Player player, int line) {
        messenger().send(player, language().message.invalidLoreLine, "line", line);
    }

    private void restorePreview(Player player, boolean notify) {
        UUID playerId = player.getUniqueId();
        Integer slot = previewItemSlots.get(playerId);
        ItemStack backup = previewBackup.get(playerId);
        if (slot != null && backup != null && slot >= 0 && slot < 9) {
            player.getInventory().setItem(slot, backup);
        }
        cleanupPreviewMode(playerId);
        if (notify) {
            messenger().send(player, language().message.canceledPreviewChanges);
        }
    }

    private void cleanupPreviewMode(UUID playerId) {
        previewBackup.remove(playerId);
        previewItemSlots.remove(playerId);
        Integer taskId = previewTaskIds.remove(playerId);
        if (taskId != null) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }

    private LangKeys language() {
        return plugin.getLanguage();
    }

    private LinMessenger messenger() {
        return plugin.getMessenger();
    }
}
