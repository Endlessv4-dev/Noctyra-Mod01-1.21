package dev.endless.v4.command.util;

import dev.endless.v4.init.CommandInit;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class NoctyraTextUtil {
    public static Text PREFIX() {
        MutableText n = Text.literal("N")
                .setStyle(Style.EMPTY.withColor(0x6F4BD1).withBold(true));

        MutableText o = Text.literal("ᴏ")
                .setStyle(Style.EMPTY.withColor(0x7F5CD9).withBold(true));

        MutableText c = Text.literal("ᴄ")
                .setStyle(Style.EMPTY.withColor(0x8E6CE0).withBold(true));

        MutableText t = Text.literal("ᴛ")
                .setStyle(Style.EMPTY.withColor(0x9E7DE8).withBold(true));

        MutableText y = Text.literal("ʏ")
                .setStyle(Style.EMPTY.withColor(0xAE8DF0).withBold(true));

        MutableText r = Text.literal("ʀ")
                .setStyle(Style.EMPTY.withColor(0xBD9EF7).withBold(true));

        MutableText a = Text.literal("ᴀ")
                .setStyle(Style.EMPTY.withColor(0xCDAEFF).withBold(true));

        MutableText sepator = Text.literal(" | ")
                .setStyle(Style.EMPTY.withColor(0x555555).withBold(false));

        Text prefix = n.copy()
                .append(o)
                .append(c)
                .append(t)
                .append(y)
                .append(r)
                .append(a)
                .append(sepator);

        return prefix;
    }

    public static Text INCORRECT() {
        MutableText incorrect = Text.translatable(CommandInit.CODE_INCORRECT)
                .setStyle(Style.EMPTY.withColor(0xFF5555).withBold(true));

        return incorrect;
    }

    public static Text PLAYER_NOT_FOUND() {
        MutableText plr_not_fnd = Text.translatable(CommandInit.PLAYER_NOT_FOUND)
                .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

        return plr_not_fnd;
    }

    public static Text NOT_IN_TEAM() {
        MutableText notInTeam = Text.translatable(CommandInit.NOT_IN_TEAM)
                .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

        return notInTeam;
    }

    public static Text TEAM_DOESNT_EXIST() {
        MutableText teamDsntExist = Text.translatable(CommandInit.TEAM_DOESNT_EXIST)
                .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

        return teamDsntExist;
    }
}
