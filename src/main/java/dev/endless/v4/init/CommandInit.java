package dev.endless.v4.init;

import dev.endless.v4.Noctyra;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.CommandManager;

public class CommandInit {
    /* GENERAL */
    public static final String CODE_INCORRECT = register("code_incorrect");
    public static final String PLAYER_NOT_FOUND = register("player_not_found");

    /* TEAM */
    public static final String NOT_IN_TEAM = register("not_in_team");
    public static final String OTHER_NOT_IN_TEAM = register("other_not_in_team");
    public static final String NO_ALLIANCES = register("no_alliances");
    public static final String ALLY_CHAT = register("ally_chat");
    public static final String TEAM_CHAT = register("team_chat");
    public static final String TEAM_DOESNT_EXIST = register("team_doesnt_exist");
    public static final String TEAM_NOT_ALLY = register("team_not_ally");
    public static final String TEAM_NOW_NEUTRAL = register("team_now_neutral");
    public static final String NO_PERMISSION_TO_DENY_ALLIANCE = register("no_permission_to_deny_alliance");
    public static final String NO_PERMISSION_TO_ACCEPT_ALLIANCE = register("no_permission_to_accept_alliance");
    public static final String NO_PERMISSION_TO_MAKE_ALLIANCE = register("no_permission_to_make_alliance");
    public static final String NO_ALLIANCE_REQUEST_FOUND = register("no_alliance_request_found");
    public static final String ALLIANCE_DENIED = register("alliance_denied");
    public static final String ALLIANCE_ACCEPTED = register("alliance_accepted");
    public static final String ALLIANCE_DENIED_SELF = register("alliance_denied_self");
    public static final String ALLIANCE = register("alliance");
    public static final String CANNOT_ALLY_OWN = register("cannot_ally_own");
    public static final String NO_PLAYER_PERMISSION_ONLINE_ALLIANCE = register("no_player_permission_online_alliance");
    public static final String ALLIANCE_REQUEST_PREFIX = register("alliance_request_prefix");
    public static final String ALLIANCE_REQUEST_MESSAGE = register("alliance_request_message");
    public static final String ALLIANCE_REQUEST_MESSAGE_ND = register("alliance_request_message_nd");
    public static final String ALLIANCE_REQUEST_SENT = register("alliance_request_sent");
    public static final String RANK_SCOUT_DESC = register("rank_scout_desc");
    public static final String RANK_SCOUTMASTER_DESC = register("rank_scoutmaster_desc");
    public static final String RANK_CONSUL_DESC = register("rank_consul_desc");
    public static final String RANK_DOESNT_EXIST = register("rank_doesnt_exist");
    public static final String RANK_TITLE = register("rank_title");
    public static final String RANK_TOPLIST_INVALID_PAGE = register("rank_toplist_invalid_page");
    public static final String PAGE_DOESNT_EXIST = register("page_doesnt_exist");
    public static final String TEAM_TOPLIST_TITLE = register("team_toplist_title");
    public static final String TEAM_TOPLIST_PAGE = register("team_toplist_page");
    public static final String TEAM_TOPLIST_NEXT_PAGE = register("team_toplist_next_page");
    public static final String NO_PERMISSION_TO_INVITE = register("no_permission_to_invite");
    public static final String CANT_INVITE_SELF = register("cant_invite_self");
    public static final String PLAYER_ALREADY_IN_TEAM = register("player_already_in_team");
    public static final String INVITE_PLAYER_SUCCESS = register("invite_player_success");
    public static final String INVITE_MESSAGE = register("invite_message");
    public static final String INVITE_MESSAGE_ND = register("invite_message_nd");
    public static final String PLAYER_NOT_IN_YOUR_TEAM = register("player_not_in_your_team");
    public static final String CANNOT_PROMOTE_SELF = register("cannot_promote_self");

    /* GAMEMODE */
    public static final String GAMEMODE_USAGE = register("gamemode.usage");
    public static final String MODE_CREATIVE = register("mode_creative");
    public static final String MODE_SURVIVAL = register("mode_survival");
    public static final String MODE_ADVENTURE = register("mode_adventure");
    public static final String MODE_SPECTATOR = register("mode_spectator");
    public static final String GAMEMODE_SELF = register("gamemode.self");
    public static final String GAMEMODE_OTHER = register("gamemode.other");

    /* SPAWN */
    public static final String TELEPORTING_TIMED = register("spawn.teleport.timed");
    public static final String TELEPORT_CANCELLED = register("spawn.teleport.cancelled");
    public static final String TELEPORT_SUCCESSFUL = register("spawn.teleport.successful");
    public static final String TELEPORT_OTHERS = register("spawn.teleport.others");
    public static final String TELEPORT_SELF = register("spawn.teleport.self");
    public static final String TELEPORTED = register("spawn.teleported");
    public static final String SETSPAWN = register("spawn.set");
    public static final String SPAWN_NOT_SET_YET = register("spawn.notset");

    public static String register(String name) {
        return "command." + Noctyra.id(name);
    }

    public static void load() {}
}
