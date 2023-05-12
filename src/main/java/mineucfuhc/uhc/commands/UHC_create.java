package mineucfuhc.uhc.commands;

import mineucfuhc.uhc.CommandBase;
import mineucfuhc.uhc.Msg;
import mineucfuhc.uhc.UHC_Instance;
import mineucfuhc.uhc.UHC;
import org.bukkit.command.CommandSender;

public class UHC_create {

    public UHC_create(){

        new CommandBase("UHC_create", 1){
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {

                String name = arguments[0];
                UHC_Instance instance = new UHC_Instance(name);

                UHC.addInstance(instance);
                Msg.send(sender, "UHC Instance "+name+" created! Now set a lobby using /UHC_setlobby "+name);

                return true;
            }

            @Override
            public String getUsage() {
                return "/UHC_create {NAME}";
            }
        };

    }
}
