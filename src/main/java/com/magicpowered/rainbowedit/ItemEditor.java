package com.magicpowered.rainbowedit;

import adapter.linlang.bukkit.common.Messenger;
import com.magicpowered.rainbowedit.lang.LangKeys;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemEditor implements Listener {

    private final RainbowEdit plugin;
    private final LangKeys lang;
    private Messenger ms;

    private Map<UUID, ItemStack> previewBackup = new HashMap<>();

    public ItemEditor(RainbowEdit plugin) {
        this.plugin = plugin;
        lang = plugin.getLang();
        ms = plugin.getMs();
    }

    /**
     * 为玩家手中的物品设置名称。
     *
     * @param player 玩家
     * @param name 新的物品名称
     */
    public void setName(Player player, String name) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            ms.sendKey(player, lang.message.noItemInHand);
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
            ms.sendKey(player, lang.message.nameSetup,"name", name);
        }
    }

    /**
     * 向玩家手中的物品添加一行Lore。
     *
     * @param player 玩家
     * @param loreToAdd 要添加的Lore
     */
    public void addLore(Player player, String loreToAdd) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            ms.sendKey(player, lang.message.noItemInHand);
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lore = meta.getLore();
            if (lore == null) lore = new ArrayList<>();

            lore.add(loreToAdd);
            meta.setLore(lore);
            item.setItemMeta(meta);
            ms.sendKey(player, lang.message.loreAdded, "lore", loreToAdd);
        }
    }

    /**
     * 修改玩家手中物品的指定行Lore。
     *
     * @param player 玩家
     * @param lineToSet 要修改的Lore行号
     * @param newLore 要设置的Lore内容
     */
    public void setLore(Player player, int lineToSet, String newLore) {
        ItemStack item = player.getInventory().getItemInMainHand();

        // 检查玩家手中是否有物品
        if (item.getType() == Material.AIR) {
            ms.sendKey(player, lang.message.noItemInHand);
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lore = meta.getLore();

            // 检查物品是否有Lore
            if (lore == null || lore.isEmpty()) {
                ms.sendKey(player, lang.message.itemHasNoLore);
                return;
            }

            // 检查指定的lore行是否存在
            if (lineToSet < 1 || lineToSet > lore.size()) {
                ms.sendKey(player, lang.message.invalidLoreLine, "line_invalid", lineToSet);
                return;
            }

            // 设置lore的内容
            lore.set(lineToSet - 1, newLore); // List是0基索引，而我们的行号是1基
            meta.setLore(lore);
            item.setItemMeta(meta);
            ms.sendKey(player, lang.message.loreLineSetup, "line", lineToSet, "lore", newLore);
        }
    }

    /**
     * 在指定行之前插入Lore。
     *
     * @param player 玩家
     * @param line 插入位置的行号
     * @param loreToInsert 要插入的Lore内容
     */
    public void insertLoreBefore(Player player, int line, String loreToInsert) {
        handleLoreInsertion(player, line, loreToInsert, true);
    }

    /**
     * 在指定行之后插入Lore。
     *
     * @param player 玩家
     * @param line 插入位置的行号
     * @param loreToInsert 要插入的Lore内容
     */
    public void insertLoreAfter(Player player, int line, String loreToInsert) {
        handleLoreInsertion(player, line, loreToInsert, false);
    }

    private void handleLoreInsertion(Player player, int line, String loreToInsert, boolean before) {
        ItemStack item = player.getInventory().getItemInMainHand();

        // 检查玩家手中是否有物品
        if (item.getType() == Material.AIR) {
            ms.sendKey(player, lang.message.noItemInHand);
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lore = meta.getLore();

            // 检查物品是否有Lore
            if (lore == null) {
                lore = new ArrayList<>();
            }

            // 检查插入的行是否在有效范围内
            if (before && (line < 1 || line > lore.size() + 1) || !before && (line < 1 || line > lore.size())) {
                ms.sendKey(player, lang.message.invalidLoreLine, "line", line);
                return;
            }

            if (before) {
                lore.add(line - 1, loreToInsert);
            } else {
                lore.add(line, loreToInsert);
            }

            meta.setLore(lore);
            item.setItemMeta(meta);

            String mode = "前";
            if (!before) mode = "后";
            ms.sendKey(player, lang.message.loreLineInserted,"insert_mode", mode, " line", line, "lore", loreToInsert);
        }
    }

    /**
     * 删除指定行的Lore。
     *
     * @param player 玩家
     * @param lineToRemove 要删除的Lore行号
     */
    public void removeLoreLine(Player player, int lineToRemove) {
        ItemStack item = player.getInventory().getItemInMainHand();

        // 检查玩家手中是否有物品
        if (item.getType() == Material.AIR) {
            ms.sendKey(player, lang.message.noItemInHand);
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lore = meta.getLore();

            // 检查物品是否有Lore
            if (lore == null || lore.isEmpty()) {
                ms.sendKey(player, lang.message.itemHasNoLore);
                return;
            }

            // 检查指定的lore行是否存在
            if (lineToRemove < 1 || lineToRemove > lore.size()) {
                ms.sendKey(player, lang.message.invalidLoreLine, "line", lineToRemove);
                return;
            }

            // 移除指定行的lore
            lore.remove(lineToRemove - 1); // List是0基索引，而我们的行号是1基
            meta.setLore(lore);
            item.setItemMeta(meta);
            ms.sendKey(player, lang.message.loreLineRemoved, "lore", lineToRemove);
        }
    }

    /**
     * 删除所有Lore。
     *
     * @param player 玩家
     */
    public void clearLore(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        // 检查玩家手中是否有物品
        if (item.getType() == Material.AIR) {
            ms.sendKey(player, lang.message.noItemInHand);
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            // 移除所有lore
            meta.setLore(new ArrayList<>());
            item.setItemMeta(meta);
            ms.sendKey(player, lang.message.loreLineRemoved);
        }
    }

    /**
     * 在指定的 Lore 行中替换 OldString 为 NewString。
     *
     * @param player 玩家
     * @param line 指定的Lore行
     * @param oldString 要被替换的字符串
     * @param newString 新的字符串
     */
    public void replaceInLore(Player player, int line, String oldString, String newString) {
        ItemStack item = player.getInventory().getItemInMainHand();

        // 检查玩家手中是否有物品
        if (item.getType() == Material.AIR) {
            ms.sendKey(player, lang.message.noItemInHand);
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lore = meta.getLore();

            // 检查物品是否有Lore
            if (lore == null || lore.isEmpty()) {
                ms.sendKey(player, lang.message.itemHasNoLore);
                return;
            }

            // 检查指定的lore行是否存在
            if (line < 1 || line > lore.size()) {
                ms.sendKey(player, lang.message.invalidLoreLine, "line", line);
                return;
            }

            // 检查OldString是否存在于指定的lore行
            String currentLine = lore.get(line - 1);
            if (!currentLine.contains(oldString)) {
                ms.sendKey(player, lang.message.oldStringNotFound, "old_lore", oldString);
                return;
            }

            // 替换OldString为NewString
            String replacedLine = currentLine.replace(oldString, newString.replace("&", "§"));
            lore.set(line - 1, replacedLine);
            meta.setLore(lore);
            item.setItemMeta(meta);
            ms.sendKey(player, lang.message.loreReplaced, "line", line, "old_lore", oldString, "new_lore", newString);
        }
    }

    /*
     #############################################################################################################

                                                       预览模式处理开始

     #############################################################################################################
     */

    private Map<UUID, Integer> previewTaskIds = new HashMap<>();
    private Map<UUID, Integer> previewItemSlots = new HashMap<>();


    /**
     * 为玩家发送编辑时消息
     *
     * @param player 玩家
     * @param message 消息
     */
    private void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    /**
     * 为玩家启用预览模式并备份物品。
     *
     * @param player 玩家
     */
    public void enterPreviewMode(Player player) {
        if (isInPreviewMode(player)) {
            ms.sendKey(player, lang.message.previewIsDisable);
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) {
            ms.sendKey(player, lang.message.noItemInHand);
            return;
        }

        // 备份物品
        ItemStack backup = item.clone();
        previewBackup.put(player.getUniqueId(), backup);

        ms.sendKey(player, lang.message.previewEnter);

        int slot = getHotbarSlot(player, item);

        if (slot == -1) {
            ms.sendKey(player, lang.message.noItemInHotbar);
            return;
        }

        previewItemSlots.put(player.getUniqueId(), slot);

        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            String message = lang.previewUI.frames.get(slot);
            sendActionBar(player, message);
        }, 0L, 20L);

        previewTaskIds.put(player.getUniqueId(), taskId);
    }

    /**
     * 应用玩家的修改并退出预览模式。
     *
     * @param player 玩家
     */
    public void applyPreviewChanges(Player player) {
        if (!isInPreviewMode(player)) {
            ms.sendKey(player, lang.message.previewIsDisable);
            return;
        }

        // 获取物品所在的快捷栏位置
        int slot = previewItemSlots.get(player.getUniqueId()) - 1;
        if (slot >= 0 && slot < 9) {
            // 应用更改到正确的快捷栏位置
            player.getInventory().setItem(slot, player.getInventory().getItemInMainHand());
        }

        // 清理
        cleanupPreviewMode(player);
        ms.sendKey(player, lang.message.appliedPreviewChanges);
    }

    /**
     * 取消玩家的修改，还原物品，并退出预览模式。
     *
     * @param player 玩家
     */
    public void cancelPreviewChanges(Player player) {
        if (!isInPreviewMode(player)) {
            ms.sendKey(player, lang.message.previewIsDisable);
            return;
        }

        // 获取物品所在的快捷栏位置
        int slot = previewItemSlots.get(player.getUniqueId()) - 1;
        if (slot >= 0 && slot < 9) {
            // 用备份的物品还原正确的快捷栏位置
            player.getInventory().setItem(slot, previewBackup.get(player.getUniqueId()));
        }

        // 清理
        cleanupPreviewMode(player);
        ms.sendKey(player, lang.message.canceledPreviewChanges);
    }

    private void cleanupPreviewMode(Player player) {
        // 删除备份并退出预览模式
        previewBackup.remove(player.getUniqueId());
        previewItemSlots.remove(player.getUniqueId());

        // 停止ActionBar消息发送任务
        Integer taskId = previewTaskIds.get(player.getUniqueId());
        if (taskId != null) {
            Bukkit.getScheduler().cancelTask(taskId);
            previewTaskIds.remove(player.getUniqueId());
        }
    }

    /**
     * 检查玩家是否在预览模式。
     *
     * @param player 玩家
     * @return 是否在预览模式
     */
    public boolean isInPreviewMode(Player player) {
        return previewBackup.containsKey(player.getUniqueId());
    }


    /**
     * 寻找物品的位置。
     *
     * @param player 玩家
     * @param item 要查询的物品
     * @return 物品的位置
     */
    private int getHotbarSlot(Player player, ItemStack item) {
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < 9; i++) {
            if (item.equals(inventory.getItem(i))) {
                return i + 1; // 返回 1 到 9，对应快捷栏的位置
            }
        }
        return -1; // 如果物品不在快捷栏，返回-1
    }


    // 当玩家打开了预览模式但退出游戏
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (isInPreviewMode(player)) {
            cancelPreviewChanges(player);
        }
    }

    // 防止玩家切换处于预览模式的物品的位置
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        UUID playerUUID = player.getUniqueId();

        if (isInPreviewMode(player)) {
            // 获取预览模式物品的快捷栏位置
            Integer slot = previewItemSlots.get(playerUUID);
            if (slot == null) {
                return;
            }

            // 如果玩家尝试从预览模式物品的快捷栏位置操作物品，取消事件
            if (event.getSlot() == slot - 1) {
                event.setCancelled(true);
                ms.sendKey(player, lang.message.cannotMovePreviewItem);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // 检查玩家是否在预览模式中
        if (isInPreviewMode(player)) {
            // 获取预览模式物品的快捷栏位置
            Integer slot = previewItemSlots.get(playerUUID);
            if (slot == null) {
                return;
            }

            // 检查玩家是否尝试丢弃锁定格子中的物品
            if (event.getPlayer().getInventory().getHeldItemSlot() == slot - 1) { // 格子位置从0开始计算
                event.setCancelled(true);
                ms.sendKey(player, lang.message.cannotDropPreviewItem);
            }
        }
    }



}

