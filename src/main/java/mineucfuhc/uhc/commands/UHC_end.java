package mineucfuhc.uhc.commands;

import mineucfuhc.uhc.CommandBase;
import mineucfuhc.uhc.UHC;
import mineucfuhc.uhc.UHC_Instance;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class UHC_end {

    public UHC_end(){

        new CommandBase("uhc_end", 1){

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) throws IOException {

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
