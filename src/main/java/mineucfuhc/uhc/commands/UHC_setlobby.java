package mineucfuhc.uhc.commands;

import mineucfuhc.uhc.CommandBase;
import mineucfuhc.uhc.Msg;
import mineucfuhc.uhc.UHC;
import mineucfuhc.uhc.UHC_Instance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UHC_setlobby {

    public UHC_setlobby(){

        new CommandBase("UHC_setlobby", 1){

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments){

                UHC_Instance instance = UHC.getInstance(arguments[0]);

                if(instance == null){
                    Msg.send(sender, "&4That arena does not exist.");
                }else{
                    Msg.send(sender, "Lobby set.");
                    Player player = (Player) sender;
                    instance.setLobby(player.getLocation(), player.getWorld());
                }

                return true;
            }

            @Override
            public String getUsage() {
                return "&4/UHC_setlobby {ARENA NAME}";
            }
        };

    }

}
