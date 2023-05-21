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
                    Msg.send(sender, "&4That arena does not exist.");
                }else if(instance.getPlayers().size() >= 20){
                    Msg.send(sender, "&4Game is full. Please use /UHC_spectate "+instance.getName());
                }else if(instance.isActive()){
                    Msg.send(sender, "&4Game is currently active. Please wait until it has finished. Please use /UHC_spectate "+instance.getName());
                }else if(UHC.isInAGame((Player) sender) != null){
                    Msg.send(sender, "&4You are already in a game. Please leave first");
                }else{
                    Msg.send(sender, "Joining game... Do /uhc_vote to start the game early.");
                    instance.messageAll(instance.getPlayers() ,"&l"+sender.getName()+"&r&a joined the game. "+(instance.getPlayers().size()+1)+"/20 Players");
                    instance.addPlayer((Player) sender);
                }

                return true;
            }

            @Override
            public String getUsage() {
                return "&4/UHC_join {ARENA NAME}";
            }
        };

    }

}
