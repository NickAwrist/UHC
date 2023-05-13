package mineucfuhc.uhc.commands;

import mineucfuhc.uhc.CommandBase;
import mineucfuhc.uhc.Msg;
import mineucfuhc.uhc.UHC;
import mineucfuhc.uhc.UHC_Instance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UHC_vote {

    public UHC_vote(){


        new CommandBase("UHC_vote"){

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments){

                UHC_Instance instance = UHC.isInAGame((Player) sender);

                if(instance != null)
                    instance.addVote((Player) sender);
                else
                    Msg.send(sender, "That instance does not exist.");

                return true;
            }

            @Override
            public String getUsage() {
                return "/UHC_vote";
            }
        };

    }

}
