package com.magicpowered.rainbowedit.config;

import api.linlang.file.file.migrator.Migrator;
import api.linlang.file.file.migrator.MutableDocument;

/**
 * 将未标记版本的旧配置接入第 1 版配置结构。
 */
public final class ConfigV0ToV1Migrator implements Migrator {

    @Override
    public int from() {
        return 0;
    }

    @Override
    public int to() {
        return 1;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type == Config.class;
    }

    @Override
    public void migrate(MutableDocument document) {
    }
}
