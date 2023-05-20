package mineucfuhc.uhc.commands;

import mineucfuhc.uhc.CommandBase;
import mineucfuhc.uhc.Msg;
import mineucfuhc.uhc.files.Instances;
import mineucfuhc.uhc.UHC_Instance;
import mineucfuhc.uhc.UHC;
import org.bukkit.command.CommandSender;

public class UHC_create {

    public UHC_create(){

        new CommandBase("UHC_create", 1){
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {

                if(!sender.hasPermission("createInstance")){
                    Msg.send(sender, "&4You do not have permission to create an instance.");
                    return true;
                }

                String name = arguments[0];
                if(Instances.get().contains(arguments[0])){
                    Msg.send(sender, "&4That instance already exists.");
                    return true;
                }

                UHC_Instance instance = new UHC_Instance(name);

                if(!Instances.get().contains("instances."+name)){
                    Instances.get().set("instances."+name+".name", name);
                    Instances.save();
                }


                UHC.addInstance(instance);



                Msg.send(sender, "UHC Instance "+name+" created! Now set a lobby using /UHC_setlobby "+name);

                return true;
            }

            @Override
            public String getUsage() {
                return "&4/UHC_create {NAME}";
            }
        };

    }
}
