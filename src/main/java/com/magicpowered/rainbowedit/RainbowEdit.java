package com.magicpowered.rainbowedit;

import api.linlang.audit.log.LinLogger;
import api.linlang.banner.LinBanner;
import api.linlang.messenger.LinMessenger;
import api.linlang.runtime.Lin;
import api.linlang.runtime.LinOptions;
import api.linlang.runtime.Linlang;
import com.magicpowered.rainbowedit.config.Config;
import com.magicpowered.rainbowedit.config.ConfigV0ToV1Migrator;
import com.magicpowered.rainbowedit.lang.LangKeys;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * RainbowEdit 插件入口。
 */
public final class RainbowEdit extends JavaPlugin {

    private static final String DEFAULT_LOCALE = "zh_CN";

    private ItemEditor itemEditor;
    private Config config;
    private LangKeys language;
    private LinMessenger messenger;
    private Linlang lin;
    private LinLogger logger;
    private String activeLocale = DEFAULT_LOCALE;

    @Override
    public void onEnable() {
        try {
            initializeLinlang();
            itemEditor = new ItemEditor(this);
            registerCommands();
            getServer().getPluginManager().registerEvents(itemEditor, this);
            printBanner();
            logger.startup(
                    "RainbowEdit 已启用，Linlang Runtime={}",
                    lin.runtimeVersion()
            );
        } catch (Exception exception) {
            if (logger != null) {
                logger.error("RainbowEdit 启动失败", exception);
            } else {
                getLogger().log(Level.SEVERE, "RainbowEdit 启动失败", exception);
            }
            closeLinlang();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (itemEditor != null) {
            itemEditor.shutdown();
        }
        if (lin != null) {
            lin.linFile().config().saveAll();
            lin.linFile().language().saveAll();
        }
        closeLinlang();
        Bukkit.getServer().getLogger().info("[彩虹编辑] 插件已卸载，再会!");
    }

    /**
     * 重新读取配置与语言文件。
     */
    public void reloadServices() {
        lin.linFile().config().reload();
        String requestedLocale = configuredLocale();
        boolean localeChanged = !activeLocale.equalsIgnoreCase(requestedLocale);

        if (localeChanged) {
            lin.parameters().totalLocale(requestedLocale).apply();
            bindFiles();
            messenger = lin.linMessenger();
            activeLocale = requestedLocale;
            registerCommands();
        } else {
            lin.linFile().language().reload();
        }

        applyPrefix();
        lin.linAudit().record(
                "rainbowedit.files.reloaded",
                "locale", activeLocale,
                "localeChanged", localeChanged
        );
    }

    private void initializeLinlang() {
        lin = Lin.setup(
                this,
                new LinOptions()
                        .totalLocale(DEFAULT_LOCALE)
                        .pluginLogger(true)
        );
        logger = lin.linAudit().logger();
        lin.linFile().config().registerMigrator(new ConfigV0ToV1Migrator());
        bindFiles();

        String requestedLocale = configuredLocale();
        if (!DEFAULT_LOCALE.equalsIgnoreCase(requestedLocale)) {
            lin.parameters().totalLocale(requestedLocale).apply();
            bindFiles();
        }

        activeLocale = requestedLocale;
        messenger = lin.linMessenger();
        applyPrefix();
    }

    private void bindFiles() {
        config = lin.linFile().config().bind(Config.class);
        language = lin.linFile().language().bind(LangKeys.class);
    }

    private void applyPrefix() {
        lin.settings()
                .dynamicTotalPrefix(owner -> language.message.prefix.resolve())
                .apply();
    }

    private String configuredLocale() {
        if (config == null || config.language == null || config.language.isBlank()) {
            return DEFAULT_LOCALE;
        }
        return config.language.trim();
    }

    private void registerCommands() {
        new CommandListener(this).register(lin.linCommand());
    }

    private void printBanner() {
        PluginDescriptionFile description = getDescription();
        LinBanner.print(LinBanner.options()
                .initials("MP : RS")
                .plugin("彩虹编辑", description.getName(), description.getVersion())
                .developers(description.getAuthors())
                .site(null)
                .build());
    }

    private void closeLinlang() {
        if (lin == null) {
            return;
        }
        lin.close();
        lin = null;
    }

    public ItemEditor getItemEditor() {
        return itemEditor;
    }

    public Config getConfigData() {
        return config;
    }

    public LangKeys getLanguage() {
        return language;
    }

    public LinMessenger getMessenger() {
        return messenger;
    }

    public Linlang getLin() {
        return lin;
    }
}
