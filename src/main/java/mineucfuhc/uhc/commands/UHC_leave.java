package mineucfuhc.uhc.commands;

import mineucfuhc.uhc.CommandBase;
import mineucfuhc.uhc.Msg;
import mineucfuhc.uhc.UHC;
import mineucfuhc.uhc.UHC_Instance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UHC_leave {

    public UHC_leave(){

        new CommandBase("UHC_leave"){

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments){

                UHC_Instance instance = UHC.isInAGame((Player) sender);
                if(instance == null){
                    Msg.send(sender, "&4You are not in a game.");
                }else{
                    if(instance.getSpectators().contains((Player) sender))
                        instance.removeSpectator((Player) sender);
                    else
                        instance.removePlayer((Player) sender);
                    instance.messageAll(instance.getPlayers() ,"&l&c"+sender.getName()+"&r&c left the game. "+instance.getPlayers().size()+"/20 Players");
                }


                return true;
            }

            @Override
            public String getUsage() {
                return "&4/UHC_leave";
            }
        };


    }

}
