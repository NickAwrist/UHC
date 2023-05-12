package mineucfuhc.uhc.commands;

import mineucfuhc.uhc.CommandBase;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class testScore {

    public testScore(){

        new CommandBase("testscore", true){


            @Override
            public boolean onCommand(CommandSender sender, String[] arguments){

                ScoreboardManager manager = Bukkit.getScoreboardManager();
                Scoreboard board = manager.getNewScoreboard();

                Component test = Component.text("UHC");
                test = test.color(TextColor.color(0x54A506));

                Objective timeObj = board.registerNewObjective("TEST", Criteria.DUMMY, test);
                timeObj.setDisplaySlot(DisplaySlot.SIDEBAR);



                Score scoreTest = timeObj.getScore(sender.getName());
                scoreTest.setScore(5);

                Score scoreTest2 = timeObj.getScore("POOP");
                scoreTest2.setScore(69);



                Player temp = (Player) sender;
                temp.setScoreboard(board);


                return false;
            }

            @Override
            public String getUsage() {
                return "testscore";
            }
        };

    }
}
