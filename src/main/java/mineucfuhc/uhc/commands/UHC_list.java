package mineucfuhc.uhc.commands;

import mineucfuhc.uhc.CommandBase;
import mineucfuhc.uhc.Msg;
import mineucfuhc.uhc.UHC;
import mineucfuhc.uhc.UHC_Instance;
import org.bukkit.command.CommandSender;

public class UHC_list {

    public UHC_list(){

        new CommandBase("UHC_list"){

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments){

                StringBuilder finalList = new StringBuilder();

                for(UHC_Instance instance : UHC.getUHCInstances()){

                    String state = "INACTIVE";
                    if(instance.isActive())
                        state = "ACTIVE";

                    finalList.append(instance.getName()).append("   ").append(state).append("\n");
                }

                Msg.send(sender, finalList.toString());


                return true;
            }

            @Override
            public String getUsage() {
                return "&4/UHC_list";
            }
        };

    }

}
