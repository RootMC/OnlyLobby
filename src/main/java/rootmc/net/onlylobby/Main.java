package rootmc.net.onlylobby;

import cn.nukkit.Player;
import cn.nukkit.block.BlockEndPortal;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.network.protocol.TransferPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.potion.Effect;
import cn.nukkit.scheduler.Task;
import gt.creeperface.holograms.Holograms;
import gt.creeperface.holograms.entity.HologramEntity;

import java.util.ArrayList;
import java.util.List;

public class Main extends PluginBase implements Listener {

    private List<String> endportal;

    public void onEnable() {
        this.endportal = new ArrayList();
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onjoin(PlayerJoinEvent event) {
        Effect effect;
        effect = Effect.getEffect(1);
        effect.setVisible(false);
        effect.setDuration(999999).setAmplifier(2);
        event.getPlayer().addEffect(effect);

        effect = Effect.getEffect(8);
        effect.setVisible(false);
        effect.setDuration(999999).setAmplifier(2);
        event.getPlayer().addEffect(effect);

        effect = Effect.getEffect(16);
        effect.setVisible(false);
        effect.setDuration(999999).setAmplifier(2);
        event.getPlayer().addEffect(effect);
    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void move(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (player.getLevel().getBlock(player.getFloorX(), player.getFloorY(), player.getFloorZ()) instanceof BlockEndPortal && player.y - (double) player.getFloorY() < 0.75D && !this.endportal.contains(player.getName())) {
            this.endportal.add(player.getName());
            this.getServer().getScheduler().scheduleDelayedTask(new Task() {
                public void onRun(int i) {
                    Main.this.endportal.remove(player.getName());
                }
            }, 100);
            HologramEntity he = Holograms.getInstance().findNearEntity(player);
            String var4 = he.getHologramId();
            switch (var4.toLowerCase()) {
                case "ps":
                    transfer(player, "a.rootmc.net", 300);//todo: make config
                    break;
                case "sb":
                    transfer(player, "a.rootmc.net", 200);
                    break;
                default:
                    player.sendMessage("Server comming soon !");
                    break;
            }
        }
    }

    public void transfer(Player p, String ip, int port) {
        TransferPacket pk = new TransferPacket();
        pk.address = ip;
        pk.port = port;
        p.dataPacket(pk);
    }

}
