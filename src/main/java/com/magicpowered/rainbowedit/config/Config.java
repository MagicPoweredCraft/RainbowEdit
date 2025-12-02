package com.magicpowered.rainbowedit.config;


import api.linlang.file.file.FileType;
import api.linlang.file.file.annotations.Comment;
import api.linlang.file.file.annotations.ConfigFile;
import api.linlang.file.file.annotations.ConfigVersion;
import api.linlang.file.file.annotations.NamingStyle;

@ConfigFile(name="config", path="", format= FileType.YAML)
@ConfigVersion(1)
@NamingStyle(NamingStyle.Style.KEBAB)
@Comment({"RainbowEdit 彩虹编辑",
        "感谢使用 妙控动力 MagicPowered 插件", ""})
public class Config {
    @Comment({
            "插件语言",
            "Plugin Language",
            "重启服务器使此项更改生效",
            "Restart the server for the changes to take effect ",
            "如果您自定义了语言文件，请将其更名为 language_REGION 的命名形式，并且放入 /lang 目录下。您即可以在这里使用它。",
            "If you have a custom language file, please rename it to the format of language-REGION and place it in the /lang path. Then you can use it here.",
            "中文（中国大陆）= zh_CN",
            "English (UK) = en_GB"
    })
    public String language = "zh_CN";
}