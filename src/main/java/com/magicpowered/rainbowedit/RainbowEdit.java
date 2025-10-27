package com.magicpowered.rainbowedit;

import adapter.linlang.bukkit.LinlangBukkitBootstrap;
import adapter.linlang.bukkit.audit.common.BukkitAuditProvider;
import adapter.linlang.bukkit.banner.BukkitBanner;
import adapter.linlang.bukkit.common.Messenger;
import adapter.linlang.bukkit.file.common.file.BukkitFsHotReloader;
import adapter.linlang.bukkit.file.common.file.BukkitPathResolver;
import api.linlang.banner.AsciiFont;
import api.linlang.banner.BannerOptions;
import api.linlang.file.called.LinFile;
import api.linlang.file.service.ConfigService;
import api.linlang.file.service.LangService;
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

    private CommandListener commandListener;
    @Getter
    private ItemEditor itemEditor;
    @Getter
    private Config cfg;
    @Getter
    private LangKeys lang;
    @Getter
    private ConfigService configService;
    @Getter
    private LangService langService;

    @Getter
    private Messenger ms;

    LinlangBukkitBootstrap bootstrap;

    @Override
    public void onEnable() {
        try {
            bootstrap = LinlangBukkitBootstrap.install(this);

            BukkitPathResolver resolver = new BukkitPathResolver(this);

            configService = LinFile.services().config();
            langService = LinFile.services().lang();

            cfg = configService.bind(Config.class);
            lang = langService.bind(LangKeys.class, cfg.language, List.of(
                    new ZhCN(),
                    new EnGB()
            ));

            bootstrap.withCommandPrefix(lang.message.prefix);
            bootstrap.withInitialLanguage(cfg.language);
            bootstrap.commands.withPreferredLocaleTag(cfg.language);

            ms = new Messenger(langService::tr);
            ms.withPrefix(lang.message.prefix);

            itemEditor = new ItemEditor(this);

            new CommandListener(this).register(bootstrap.commands);

            BukkitFsHotReloader hot = new BukkitFsHotReloader(this);

//            Path root = resolver.root();
//            hot.watchConfigDir(root.resolve("config"), () -> {
//                cfg = configService.bind(Config.class);
//            });
//            hot.watchLangDir(root.resolve("lang"), () -> {
//                langService.bind(LangKeys.class, cfg.language, List.of(new ZhCN(), new EnGB()));
//                ms = new Messenger(langService::tr);
//            });

            getServer().getPluginManager().registerEvents(this, this);
            getServer().getPluginManager().registerEvents(itemEditor, this);

            PluginDescriptionFile desc = getDescription();

            BannerOptions options = BannerOptions.builder()
                    .initials("MP : RS")
                    .team("妙控动力","MagicPowered")
                    .series("彩虹系列", "RainbowSeries")
                    .plugin("彩虹编辑", desc.getName(), desc.getVersion())
                    .developers(getDescription().getAuthors())
                    .site("https://magicpowered.cn")
                    .build();
            BukkitBanner.printWithLogs(options);

            BukkitAuditProvider.flushStartupToConsole();


        } catch (Exception e) {
            Bukkit.getServer().getLogger().info("[彩虹编辑] 启动失败");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        configService.saveAll();
        langService.saveAll();
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


    public void reload() {
        cfg = configService.bind(Config.class);
        lang = langService.bind(LangKeys.class, cfg.language, List.of(new ZhCN(), new EnGB()));
        bootstrap.withCommandPrefix(lang.message.prefix);
        bootstrap.reload();
    }
}
