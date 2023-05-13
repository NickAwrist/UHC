package mineucfuhc.uhc.events;

import mineucfuhc.uhc.UHC;
import mineucfuhc.uhc.UHC_Instance;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.ArrayList;
import java.util.Objects;

public class killEvent implements Listener {

    @EventHandler
    public void PlayerDeath(PlayerDeathEvent e){

        UHC_Instance instance = UHC.isInAGame(e.getPlayer());
        if(instance != null){
            Objective obj = instance.getObj();

            Player killer = e.getEntity().getKiller();

            if(e.getPlayer().getWorld().equals(instance.getWorld()) && killer != null){
                Score temp = obj.getScore(killer);
                temp.setScore(temp.getScore()+1);
                deathMessage(e, instance);
                e.deathMessage(Component.newline());
                instance.removePlayer(e.getPlayer());
            }
        }
    }

    private void deathMessage(PlayerDeathEvent e, UHC_Instance instance){
        instance.messageAll((ArrayList<Player>) instance.getWorld().getPlayers(), e.getPlayer().getName()+" was killed by "+ Objects.requireNonNull(e.getEntity().getKiller()).getName());
    }

}
