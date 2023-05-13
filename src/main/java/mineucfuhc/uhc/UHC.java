package mineucfuhc.uhc;

import com.onarandombox.MultiverseCore.MultiverseCore;
import mineucfuhc.uhc.commands.*;
import mineucfuhc.uhc.events.killEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class UHC extends JavaPlugin {

    private static UHC plugin;
    public MultiverseCore MultiVersePlugin;

    @Override
    public void onEnable(){
        plugin = this;
        // Plugin startup logic
        Bukkit.getLogger().info("------------UHC!!!------------");

        MultiVersePlugin = getMultiVersePlugin();

        new UHC_create();
        new UHC_delete();
        new testScore();
        new UHC_join();
        new UHC_leave();
        new UHC_setlobby();
        new UHC_list();
        new UHC_vote();
        new UHC_spectate();
        new UHC_end();

        getServer().getPluginManager().registerEvents(new killEvent(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static UHC getPlugin(){
        return plugin;
    }

    public MultiverseCore getMultiVersePlugin(){
        Plugin mvPlugin = this.getServer().getPluginManager().getPlugin("MultiVerse-Core");

        if(mvPlugin instanceof MultiverseCore){
            return (MultiverseCore) mvPlugin;
        }
        throw new RuntimeException("MultiverseCore not found!!!");
    }

    // UHC Arena methods
    static ArrayList<UHC_Instance> UHCInstances = new ArrayList<>();

    // Returns an Arena when given its name
    public static UHC_Instance getInstance(String name){

        for(UHC_Instance instance : UHCInstances)
            if(instance.getName().equalsIgnoreCase(name))
                return instance;

        return null;
    }

    // Returns arena a player is in
    public static UHC_Instance isInAGame(Player player){
        for(UHC_Instance instance : UHCInstances)
            if(instance.checkPlayer(player))
                return instance;
        return null;
    }

    // Adds/Removes an Arena
    public static void addInstance(UHC_Instance instance){
        UHCInstances.add(instance);
    }
    public static void deleteInstance(String name){
        UHCInstances.removeIf(instance -> instance.getName().equalsIgnoreCase(name));
    }

    public static void printInstances(Player player){

        StringBuilder finalList = new StringBuilder();

        for(UHC_Instance instance : UHCInstances){

            String state = "INACTIVE";
            if(instance.isActive())
                state = "ACTIVE";

            finalList.append(instance.getName()).append("   ").append(state).append("\n");
        }

        Msg.send(player, finalList.toString());
    }


    /*      TO DO
          - Clean up
          - Better World creation, RAM?
     */
}
