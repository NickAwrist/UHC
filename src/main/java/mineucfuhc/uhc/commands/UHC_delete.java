package mineucfuhc.uhc.commands;


import mineucfuhc.uhc.Msg;
import mineucfuhc.uhc.CommandBase;
import mineucfuhc.uhc.UHC;
import org.bukkit.command.CommandSender;


public class UHC_delete {

    public UHC_delete(){

        new CommandBase("UHC_delete", 1){

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments){

                UHC.deleteInstance(arguments[0]);
                Msg.send(sender, arguments[0]+" has been deleted!");

                return true;
            }

            @Override
            public String getUsage() {
                return "&4/UHC_delete {NAME}";
            }
        };

    }

}
