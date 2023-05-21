package mineucfuhc.uhc.events;

import mineucfuhc.uhc.UHC;
import mineucfuhc.uhc.UHC_Instance;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class leaveEvent implements Listener {

    @EventHandler
    public void playerLeave(PlayerQuitEvent e){

        UHC_Instance instance = UHC.isInAGame(e.getPlayer());
        if(instance != null){
            instance.removePlayer(e.getPlayer());
            instance.messageAll(instance.getPlayers(), "&4&l"+e.getPlayer().getName()+"&r&4 has disconnected from the match.");
        }


    }


}

