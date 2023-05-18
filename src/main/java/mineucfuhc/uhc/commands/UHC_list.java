package mineucfuhc.uhc.commands;

import mineucfuhc.uhc.CommandBase;
import mineucfuhc.uhc.UHC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UHC_list {

    public UHC_list(){

        new CommandBase("UHC_list"){

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments){

                UHC.printInstances((Player) sender);
                return true;
            }

            @Override
            public String getUsage() {
                return "&4/UHC_list";
            }
        };

    }

}
