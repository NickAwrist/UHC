package mineucfuhc.uhc;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.*;
import mineucfuhc.uhc.events.killEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class UHC_Instance {

    private final String name;
    private final ArrayList<Player> players;
    private final ArrayList<Player> spectators;
    private MultiverseWorld arenaWorld;
    private Location lobby;
    private boolean active;
    private int startGameVote;
    private final ArrayList<Player> votedPlayers;

    ScoreboardManager manager;
    Scoreboard board;
    Component boardTitle;
    Objective timeObj;

    private long time;

    private final MultiverseCore mvCore;

    // Constructor
    public UHC_Instance(String name){
        this.name = name;
        this.time = 0;
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.startGameVote = 0;
        votedPlayers = new ArrayList<>();
        mvCore= UHC.getPlugin().MultiVersePlugin;

        setUpScoreboard();
    }

    // Methods
    private void setUpScoreboard(){
        this.manager = Bukkit.getScoreboardManager();
        this.board = manager.getNewScoreboard();
        this.boardTitle = Component.text("UHC");
        boardTitle = boardTitle.color(TextColor.color(0xFF1C00));
        this.timeObj = board.registerNewObjective("UHC_stats", Criteria.DUMMY, boardTitle);
        timeObj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score timeScore = timeObj.getScore("Minutes:");
        timeScore.setScore(0);
        timeScore = timeObj.getScore("Seconds:");
        timeScore.setScore(0);
    }

    private void createWorld(){
        MultiverseCore mvCore= UHC.getPlugin().MultiVersePlugin;

        if(mvCore.getMVWorldManager().addWorld("uhcTEMP", World.Environment.NORMAL, null, WorldType.NORMAL, true, "Normal")){
            arenaWorld = mvCore.getMVWorldManager().getMVWorld("uhcTEMP");
            World dummy = arenaWorld.getCBWorld();
            dummy.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            dummy.setGameRule(GameRule.NATURAL_REGENERATION, false);
            dummy.setGameRule(GameRule.DO_INSOMNIA, false);
            dummy.setGameRule(GameRule.DO_PATROL_SPAWNING, false);

            int worldBorderSize = 1500;
            dummy.getWorldBorder().setCenter(0, 0);
            dummy.getWorldBorder().setSize(worldBorderSize);

        }
    }

    private void startUpWrapper(){
        messageAll(players, "&6The game is starting in &l15&r&6 seconds.");
        for(Player player : players)
            player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);

        active = true;

        new BukkitRunnable() {
            int startUpTime = 15;
            boolean final5 = false;

            public void run() {

                if(startUpTime == 1){
                    startGame();
                    cancel();
                }

                if(players.size() == 1){
                    messageAll(players, "&4Unable to start game.");
                    active = false;
                    cancel();
                }

                startUpTime--;
                if(!final5) {
                    if (startUpTime % 5 == 0) {
                        messageAll(players, "&6The game is starting in &l" + startUpTime + "&r&6 seconds.");
                        for(Player player : players)
                            player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }
                    if(startUpTime == 5)
                        final5 = true;
                }else{
                    messageAll(players, "&6The game is starting in &l" + startUpTime + "&r&6 seconds.");
                    for(Player player : players)
                        player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }

            }
        }.runTaskTimer(UHC.getPlugin(), 0L, 20L);
    }

    private void startGame(){

        new killEvent();
        createWorld();
        setUpScoreboard();
        int worldBorderSize = (int)arenaWorld.getCBWorld().getWorldBorder().getSize();

        for(Player player : players){
            Random random = new Random();
            Location randomLoc = new Location(arenaWorld.getCBWorld(), (double) (random.nextInt(worldBorderSize)-100)/2, 150, (double) (random.nextInt(worldBorderSize)-100)/2);

            while(new Location(arenaWorld.getCBWorld(), randomLoc.getX(), 59, randomLoc.getZ()).getBlock().isLiquid()){
                randomLoc = new Location(arenaWorld.getCBWorld(), (double) (random.nextInt(worldBorderSize)-100)/2, 150, (double) (random.nextInt(worldBorderSize)-100)/2);
            }

            mvCore.teleportPlayer(player, player, randomLoc);

            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60*20, 100));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20*20, 1));

            Score tempScore = timeObj.getScore(player.getName());
            tempScore.setScore(0);
            player.setScoreboard(this.board);
        }

        this.active = true;
        checkCompletion();

    }

    private void checkCompletion(){

        new BukkitRunnable() {
            public void run() {
                time++;
                long seconds = time%60;
                Score tempSeconds = timeObj.getScore("Seconds:");
                tempSeconds.setScore((int) seconds);

                if(seconds >= 59){
                    Score tempMinutes = timeObj.getScore("Minutes:");
                    tempMinutes.setScore((int) time/60 + 1);
                }

                if(time%300 == 0){
                    double worldSize = arenaWorld.getCBWorld().getWorldBorder().getSize();
                    arenaWorld.getCBWorld().getWorldBorder().setSize(worldSize - worldSize/5, TimeUnit.SECONDS, 150);
                    messageAll((ArrayList<Player>) arenaWorld.getCBWorld().getPlayers(),"&6The world border is shrinking to "+((int)worldSize - worldSize/5)+" blocks!");
                }

                if(players.size() <= 1){
                    active = false;
                }

                if(!isActive()) {
                    try {
                        messageAll((ArrayList<Player>) arenaWorld.getCBWorld().getPlayers(),"&l"+players.get(0).getName()+"&r&a has won the UHC match!");
                        endGame();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    cancel();
                }
            }
        }.runTaskTimer(UHC.getPlugin(), 0L, 20L);

    }

    public void endGame() throws IOException {

        if (Bukkit.getWorld("uhcTEMP") == null) return;

        for (Player player : Objects.requireNonNull(Bukkit.getWorld("uhcTEMP")).getPlayers()) {
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();
            player.setExp(0);
            mvCore.teleportPlayer(player, player, lobby);
            player.setScoreboard(manager.getNewScoreboard());
        }

        players.clear();
        spectators.clear();

        mvCore.getMVWorldManager().deleteWorld("uhcTEMP", false, true);
    }

    public void setLobby(Location cords, World lobbyWorld){
        if(cords != null){
            lobby = new Location(lobbyWorld, cords.getX(), cords.getY(), cords.getZ());
        }
    }

    public void addPlayer(Player player){
        if(player.isOnline()){
            players.add(player);
            mvCore.teleportPlayer(player, player, lobby);
        }

        if(players.size() >= 20){
            startUpWrapper();
        }
    }

    public void removePlayer(Player player){
        if(player.isOnline()){

            if(votedPlayers.contains(player)){
                votedPlayers.remove(player);
                startGameVote--;
            }
            player.setGameMode(GameMode.SURVIVAL);
            player.setScoreboard(manager.getNewScoreboard());
            players.remove(player);
            mvCore.teleportPlayer(player, player, lobby);
        }
    }

    public void removeSpectator(Player player){
        if(player.isOnline()){
            player.setGameMode(GameMode.SURVIVAL);
            player.setScoreboard(manager.getNewScoreboard());
            spectators.remove(player);
            mvCore.teleportPlayer(player, player, lobby);
        }
    }


    public void messageAll(ArrayList<Player> list ,String message){
        for(Player player : list)
            Msg.send(player, message);
    }
    public void addVote(Player player){

        votedPlayers.add(player);
        this.startGameVote++;

        if(players.size()/startGameVote <=2)
            startUpWrapper();
    }

    public boolean checkPlayer(Player player){
        return (player.isOnline() && (players.contains(player) || spectators.contains(player)));
    }
    public boolean isActive(){return active;}
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Player> getSpectators() {
        return spectators;
    }
    public ArrayList<Player> getVotedPlayers(){return votedPlayers;}

    public void addSpectator(Player player){
        mvCore.teleportPlayer(player, player, players.get((int) (Math.random()*players.size())).getLocation());
        player.setGameMode(GameMode.SPECTATOR);
        player.setScoreboard(this.board);
        spectators.add(player);
    }

    public String getName(){return name;}
    public Objective getObj(){return this.timeObj;}
    public int getStartGameVote(){return startGameVote;}
    public World getWorld(){return arenaWorld.getCBWorld();}
    public Location getLobby(){return lobby;}
}
