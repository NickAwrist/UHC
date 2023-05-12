package mineucfuhc.uhc.events;

import mineucfuhc.uhc.UHC;
import mineucfuhc.uhc.UHC_Instance;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

public class killEvent implements Listener {

    @EventHandler
    public void PlayerDeath(PlayerDeathEvent e){

        UHC_Instance instance = UHC.isInAGame(e.getPlayer());
        assert instance != null;
        Objective obj = instance.getObj();

        Player killer = e.getEntity().getKiller();

        if(e.getPlayer().getWorld().equals(instance.getWorld()) && killer != null){
            Score temp = obj.getScore(killer);
            temp.setScore(temp.getScore()+1);
            instance.removePlayer(e.getPlayer());
        }

    }

}
