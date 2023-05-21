package mineucfuhc.uhc.commands;


import mineucfuhc.uhc.Msg;
import mineucfuhc.uhc.CommandBase;
import mineucfuhc.uhc.UHC;
import mineucfuhc.uhc.files.Instances;
import org.bukkit.command.CommandSender;


public class UHC_delete {

    public UHC_delete(){

        new CommandBase("UHC_delete", 1){

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments){

                if(!sender.hasPermission("deleteInstance")){
                    Msg.send(sender, "&4You do not have permission to delete an instance.");
                    return true;
                }

                if(Instances.get().contains("instances."+arguments[0])){
                    Instances.get().set("instances."+arguments[0], null);
                    Instances.save();
                }

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
