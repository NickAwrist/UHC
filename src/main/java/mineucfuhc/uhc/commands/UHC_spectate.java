package mineucfuhc.uhc.commands;

import mineucfuhc.uhc.CommandBase;
import mineucfuhc.uhc.Msg;
import mineucfuhc.uhc.UHC;
import mineucfuhc.uhc.UHC_Instance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UHC_spectate {

    public UHC_spectate(){

        new CommandBase("UHC_spectate", 1){


            @Override
            public boolean onCommand(CommandSender sender, String[] arguments){

                UHC_Instance instance = UHC.getInstance(arguments[0]);

                if(instance == null)
                    Msg.send(sender, "That game does not exist.");
                else if(!instance.isActive())
                    Msg.send(sender, "That is not an active game.");
                else if(instance.getSpectators().contains((Player) sender))
                    Msg.send(sender, "You are already a spectator.");
                else
                    instance.addSpectator((Player) sender);

                return true;
            }

            @Override
            public String getUsage() {
                return "/UHC_spectate {ARENA NAME}";
            }
        };


    }

}
