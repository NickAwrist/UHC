package mineucfuhc.uhc.commands;


import mineucfuhc.uhc.Msg;
import mineucfuhc.uhc.CommandBase;
import mineucfuhc.uhc.UHC;
import mineucfuhc.uhc.UHC_Instance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UHC_join {

    public UHC_join(){

        new CommandBase("UHC_join", 1){

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments){

                UHC_Instance instance = UHC.getInstance(arguments[0]);

                if(instance == null){
                    Msg.send(sender, "That arena does not exist.");
                }else if(instance.getPlayers().size() >= 20){
                    Msg.send(sender, "Game is full. Please use /UHC_spectate "+instance.getName());
                }else if(instance.isActive()){
                    Msg.send(sender, "Game is currently active. Please wait until it has finished. Please use /UHC_spectate "+instance.getName());
                }else if(UHC.isInAGame((Player) sender) != null){
                    Msg.send(sender, "You are already in a game. Please leave first");
                }else{
                    Msg.send(sender, "Joining game");
                    instance.messageAll(instance.getPlayers() ,sender.getName()+" joined the game. "+instance.getPlayers().size()+"/20 Players");
                    instance.addPlayer((Player) sender);
                }

                return true;
            }

            @Override
            public String getUsage() {
                return "/UHC_join {ARENA NAME}";
            }
        };

    }

}
