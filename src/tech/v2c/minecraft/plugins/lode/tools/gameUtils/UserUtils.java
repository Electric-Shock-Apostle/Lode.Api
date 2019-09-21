package tech.v2c.minecraft.plugins.lode.tools.gameUtils;

import cn.nukkit.Player;
import tech.v2c.minecraft.plugins.lode.Lode;

import java.util.UUID;

public class UserUtils {
    public static Player GetPlayerByName(String name){
        Player player = Lode.instance.getServer().getPlayer(name);

        return player;
    }

    public static Player GetPlayerByUuid(UUID uuid){
        Player player = Lode.instance.getServer().getPlayer(uuid).get();

        return player;
    }
}
