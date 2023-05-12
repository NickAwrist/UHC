package mineucfuhc.uhc;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandBase  extends BukkitCommand implements CommandExecutor {
    private List<String> delayedPlayers=null;
    private int delay=0;
    private final int minArguments;
    private final int maxArguments;
    private final boolean playerOnly;

    private List<String> ignoredPlayers=null;

    public CommandBase(String command){
        this(command, 0);
    }

    public CommandBase(String command, boolean playerOnly){
        this(command, 0, playerOnly);
    }

    public CommandBase(String command, int requiredArguments){
        this(command, requiredArguments, requiredArguments);
    }

    public CommandBase(String command, int minArguments, int maxArguments){
        this(command, minArguments, maxArguments, false);
    }

    public CommandBase(String command, int requiredArguments, boolean playerOnly){
        this(command, requiredArguments, requiredArguments, playerOnly);
    }

    public CommandBase(String command, int minArguments, int maxArguments, boolean playerOnly){
        super(command);
        this.minArguments=minArguments;
        this.maxArguments=maxArguments;
        this.playerOnly=playerOnly;

        CommandMap commandMap = getCommandMap();
        if(command!=null){
            commandMap.register(command, this);
        }
    }

    public CommandMap getCommandMap(){
        try{
            if(Bukkit.getPluginManager() instanceof SimplePluginManager){
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                return (CommandMap) field.get(Bukkit.getPluginManager());
            }
        } catch (NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }
        return null;
    }

    public void sendUsage(CommandSender sender){
        Msg.send(sender, getUsage());
    }

    public boolean execute(CommandSender sender, String alias, String[] arguments){
        if(arguments.length<minArguments || arguments.length<maxArguments && maxArguments != -1){
            sendUsage(sender);
            return true;
        }

        if(playerOnly && !(sender instanceof Player)){
            Msg.send(sender, "Only players can use this command");
            return true;
        }

        String permission = getPermission();
        if(permission != null && !sender.hasPermission(permission)){
            Msg.send(sender, "[UHC] &c&oYou do not have permission to use this reaction.");
            return true;
        }

        Player p_receiver = null;
        Player p_sender = (Player) sender;

        try {
            if(!onCommand(sender, arguments)){
                sendUsage(sender);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] arguments){
        try {
            return this.onCommand(sender, arguments);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public abstract boolean onCommand(CommandSender sender, String[] arguments) throws IOException;

    public abstract String getUsage ();



}
