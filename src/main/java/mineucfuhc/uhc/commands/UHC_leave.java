package mineucfuhc.uhc.commands;

import mineucfuhc.uhc.CommandBase;
import mineucfuhc.uhc.Msg;
import mineucfuhc.uhc.UHC;
import mineucfuhc.uhc.UHC_Instance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UHC_leave {

    public UHC_leave(){

        new CommandBase("UHC_leave", 1){

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments){

                UHC_Instance instance = UHC.isInAGame((Player) sender);
                if(instance == null){
                    Msg.send(sender, "You are not in a game.");
                }else{
                    instance.removePlayer((Player) sender);
                }


                return true;
            }

            @Override
            public String getUsage() {
                return "/UHC_leave";
            }
        };


    }

}
