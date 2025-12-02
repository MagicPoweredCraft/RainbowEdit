package com.magicpowered.rainbowedit;

import api.linlang.audit.LinLog;
import api.linlang.banner.LinBanner;
import api.linlang.messenger.LinMessenger;
import api.linlang.runtime.Lin;
import api.linlang.runtime.Linlang;
import com.magicpowered.rainbowedit.config.Config;
import com.magicpowered.rainbowedit.lang.EnGB;
import com.magicpowered.rainbowedit.lang.LangKeys;
import com.magicpowered.rainbowedit.lang.ZhCN;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.List;

public class RainbowEdit extends JavaPlugin implements Listener {

    @Getter
    private ItemEditor itemEditor;
    @Getter
    private Config cfg;
    @Getter
    private LangKeys lang;
    @Getter
    private LinMessenger ms;
    @Getter
    private Linlang lin;

    @Override
    public void onEnable() {
        try {
            lin = Lin.init(this);
            cfg = lin.linFile().config().bind(Config.class);
            lang = lin.linFile().language().bind(
                    LangKeys.class,
                    cfg.language,
                    List.of(
                            new ZhCN(),
                            new EnGB()
                    ));

            lin.settings()
                    .initialLocale(cfg.language)
                    .fixedPrefix(lang.message.prefix);


            ms = lin.linMessenger();
            ms.withPrefix(lang.message.prefix);


            itemEditor = new ItemEditor(this);

            new CommandListener(this).register(lin.linCommand());

            getServer().getPluginManager().registerEvents(this, this);
            getServer().getPluginManager().registerEvents(itemEditor, this);

            PluginDescriptionFile desc = getDescription();

            LinBanner.printWithLogs(LinBanner.options()
                    .initials("MP : RS")
                    .team("妙控动力", "MagicPowered")
                    .series("彩虹系列", "RainbowSeries")
                    .plugin("彩虹编辑", desc.getName(), desc.getVersion())
                    .developers(getDescription().getAuthors())
                    .site("https://magicpowered.cn")
                    .build());

            LinLog.flushStartupToConsole();


        } catch (Exception e) {
            Bukkit.getServer().getLogger().info("[彩虹编辑] 启动失败");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        lin.linFile().config().saveAll();
        lin.linFile().language().saveAll();
        lin.close();
        Bukkit.getServer().getLogger().info("[彩虹编辑] 插件已卸载，再会!");
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (itemEditor.isInPreviewMode(player)) {
            itemEditor.cancelPreviewChanges(player);
        }
    }


    /**
     * 刷新琳琅服务
     * <p>在</p>
     */
    public void reload() {
        cfg = lin.linFile().config().bind(Config.class);
        lang = lin.linFile().language().bind(
                LangKeys.class,
                cfg.language,
                List.of(
                        new ZhCN(),
                        new EnGB()
                )
        );
        lin.settings()
                .initialLocale(cfg.language)
                .fixedPrefix(lang.message.prefix);

        lin.reload();
    }
}
