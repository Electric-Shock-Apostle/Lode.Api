package tech.v2c.minecraft.plugins.jsonApi.RESTful.actions;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import tech.v2c.minecraft.plugins.jsonApi.JsonApi;
import tech.v2c.minecraft.plugins.jsonApi.RESTful.global.BaseAction;
import tech.v2c.minecraft.plugins.jsonApi.RESTful.global.annotations.ApiRoute;
import tech.v2c.minecraft.plugins.jsonApi.RESTful.global.entities.JsonData;
import tech.v2c.minecraft.plugins.jsonApi.RESTful.global.entities.server.ServerDTO;
import tech.v2c.minecraft.plugins.jsonApi.tools.PropsUtils;
import tech.v2c.minecraft.plugins.jsonApi.tools.results.JsonResult;

import java.util.Timer;
import java.util.TimerTask;

public class ServerAction extends BaseAction {
    @ApiRoute(Path = "/api/Server/GetServerInfo")
    public JsonResult GetServerInfo() {
        Server server = JsonApi.instance.getServer();

        ServerDTO serverInfo = new ServerDTO();
        serverInfo.setPort(server.getPort());
        ;
        serverInfo.setVersion(server.getVersion());
        serverInfo.setOnlinePlayerCount(server.getOnlinePlayers().size());
        serverInfo.setIp(server.getIp());
        serverInfo.setMaxPlayerCount(server.getMaxPlayers());
        serverInfo.setMotd(server.getMotd());
        serverInfo.setBukkitVersion(server.getBukkitVersion());
        serverInfo.setApiVersion(server.getVersion());
        serverInfo.setGameMode(server.getDefaultGameMode().getValue());
        serverInfo.setDifficulty(Integer.parseInt(PropsUtils.GetProps("difficulty")));
        serverInfo.setPluginCount(server.getPluginManager().getPlugins().length);
        serverInfo.setHasWhiteList(server.hasWhitelist());

        return new JsonResult(serverInfo);
    }

    @ApiRoute(Path = "/api/Server/ExecuteCommand")
    public JsonResult ExecuteCommand(JsonData data) {
        String cmd = data.Data.get("command").toString();
        server.getScheduler().scheduleSyncDelayedTask(JsonApi.instance, () -> {
            server.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        });

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/Server/ReloadServer")
    public JsonResult ReloadServer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                server.reload();
            }
        }, 5000);

        return new JsonResult(null, 200, "Server will have reload after 5 seconds.");
    }

    @ApiRoute(Path = "/api/Server/SetMaxPlayer")
    public JsonResult SetMaxPlayer(JsonData data) {
        Integer maxPlayer = (int) Double.parseDouble(data.Data.get("maxPlayer").toString());
        PropsUtils.SetProps("max-players", maxPlayer.toString());
        // server.reload();
        return new JsonResult(null, 200, "将在下次启动时生效");
    }

    @ApiRoute(Path = "/api/Server/SendBroadcastMessage")
    public JsonResult SendBroadcastMessage(JsonData data) {
        String message = data.Data.get("message").toString();

        return new JsonResult(server.broadcastMessage(message));
    }

    @ApiRoute(Path = "/api/Server/SetServerProps")
    public JsonResult SetServerProps(JsonData data) {
        String key = data.Data.get("key").toString();
        String value = data.Data.get("value").toString();

        PropsUtils.SetProps(key, value);

        return new JsonResult(null, 200, "将在下次启动时生效");
    }
}
