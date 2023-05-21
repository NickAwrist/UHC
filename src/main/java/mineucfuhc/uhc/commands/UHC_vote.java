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

                if(instance != null) {

                    if(instance.isActive()){
                        Msg.send(sender, "&4This game has already begun.");
                    } else if (!instance.getVotedPlayers().contains(sender)) {
                        instance.addVote((Player) sender);
                        instance.messageAll(instance.getPlayers(), "&l"+sender.getName()+"&r&a has voted to start early. Current vote is &n"+instance.getStartGameVote()+"/"+instance.getPlayers().size()/2);
                    } else{
                        Msg.send(sender, "&4You already voted.");
                    }

                }else {
                    Msg.send(sender, "&4That instance does not exist.");
                }

                return true;
            }

            @Override
            public String getUsage() {
                return "/UHC_vote";
            }
        };

    }

}
