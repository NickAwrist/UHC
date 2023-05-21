package mineucfuhc.uhc;

import com.onarandombox.MultiverseCore.MultiverseCore;
import mineucfuhc.uhc.commands.*;
import mineucfuhc.uhc.events.killEvent;
import mineucfuhc.uhc.events.leaveEvent;
import mineucfuhc.uhc.files.Instances;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
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

        MultiVersePlugin = getMultiVersePlugin();

        Instances.setup();
        Instances.get().options().copyDefaults();
        Instances.save();
        loadInstances();

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
        getServer().getPluginManager().registerEvents(new leaveEvent(), this);


        Bukkit.getLogger().info("------------Enabled UHC!!!------------");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("------------Disabled UHC!------------");
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

    public static ArrayList<UHC_Instance> getUHCInstances(){return UHCInstances;}

    private static void loadInstances(){

        ConfigurationSection instances = Instances.get().getConfigurationSection("instances");

        if(instances != null){
            for(String instance: instances.getKeys(false)){

                UHC_Instance temp = new UHC_Instance(instance);
                Location lobby = Instances.get().getLocation("instances."+instance+".lobby");
                assert lobby != null;
                Bukkit.getLogger().warning(lobby.toString());
                temp.setLobby(lobby, lobby.getWorld());
                UHCInstances.add(temp);
            }
        }



    }



    /*      TO DO
          - Clean up
          - Better World creation, RAM?
     */
}
