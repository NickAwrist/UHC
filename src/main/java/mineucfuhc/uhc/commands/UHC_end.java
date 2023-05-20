package mineucfuhc.uhc.commands;

import mineucfuhc.uhc.CommandBase;
import mineucfuhc.uhc.Msg;
import mineucfuhc.uhc.UHC;
import mineucfuhc.uhc.UHC_Instance;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class UHC_end {

    public UHC_end(){

        new CommandBase("uhc_end", 1){

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) throws IOException {

                if(!sender.hasPermission("endGame")){
                    Msg.send(sender, "&4You do not have permission to end a game.");
                    return true;
                }

                UHC_Instance instance = UHC.getInstance(arguments[0]);
                if(instance != null){
                    instance.endGame();
                }

                return true;
            }

            @Override
            public String getUsage() {
                return "&4/uhc_end {ARENA}";
            }
        };


    }

}
