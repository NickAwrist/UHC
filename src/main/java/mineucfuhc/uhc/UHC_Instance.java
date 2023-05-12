package mineucfuhc.uhc;

import mineucfuhc.uhc.events.killEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.checkerframework.checker.units.qual.A;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class UHC_Instance {

    private final String name;
    private final ArrayList<Player> players;
    private final ArrayList<Player> spectators;
    private World arenaWorld;
    private Location lobby;
    private boolean active;
    private int startGameVote;

    ScoreboardManager manager;
    Scoreboard board;
    Component boardTitle;
    Objective timeObj;

    private long time;

    // Constructor
    public UHC_Instance(String name){
        this.name = name;
        this.time = 0;
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.startGameVote = 0;

        setUpScoreboard();
    }

    // Methods
    private void setUpScoreboard(){
        this.manager = Bukkit.getScoreboardManager();
        this.board = manager.getNewScoreboard();
        this.boardTitle = Component.text("UHC");
        boardTitle = boardTitle.color(TextColor.color(0x54A506));
        this.timeObj = board.registerNewObjective("UHC_stats", Criteria.DUMMY, boardTitle);
        timeObj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score timeScore = timeObj.getScore("Time:");
        timeScore.setScore(0);
    }

    private void createWorld(){
        WorldCreator matchWorldCreate = new WorldCreator("uhcTEMP");
        matchWorldCreate.environment(World.Environment.NORMAL);
        matchWorldCreate.type(WorldType.NORMAL);

        this.arenaWorld = matchWorldCreate.createWorld();
        assert arenaWorld != null;
        arenaWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        arenaWorld.setGameRule(GameRule.NATURAL_REGENERATION, false);
        arenaWorld.setGameRule(GameRule.DO_INSOMNIA, false);
        arenaWorld.setGameRule(GameRule.DO_PATROL_SPAWNING, false);

        int worldBorderSize = 1500;
        assert arenaWorld != null;
        arenaWorld.getWorldBorder().setCenter(0, 0);
        arenaWorld.getWorldBorder().setSize(worldBorderSize);
    }

    private void startGame(){

        new killEvent();
        createWorld();
        int worldBorderSize = (int)arenaWorld.getWorldBorder().getSize();

        for(Player player : players){
            Random random = new Random();
            Location randomLoc = new Location(arenaWorld, (double) (random.nextInt(worldBorderSize)-100)/2, 150, (double) (random.nextInt(worldBorderSize)-100)/2);

            while(new Location(arenaWorld, randomLoc.getX(), 60, randomLoc.getZ()).getBlock().isLiquid()){
                randomLoc = new Location(arenaWorld, (double) (random.nextInt(worldBorderSize)-100)/2, 150, (double) (random.nextInt(worldBorderSize)-100)/2);
            }

            player.teleport(randomLoc);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60*20, 100));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20*20, 1));

            player.setScoreboard(this.board);
        }

        this.active = true;
        checkCompletion();

    }

    private void checkCompletion(){

        new BukkitRunnable() {
            public void run() {
                time++;
                Score tempTime = timeObj.getScore("Time:");
                tempTime.setScore((int) time);

                if(time%300 == 0){
                    double worldSize = arenaWorld.getWorldBorder().getSize();
                    arenaWorld.getWorldBorder().setSize(worldSize - worldSize/5, TimeUnit.SECONDS, 150);
                    messageAll((ArrayList<Player>) arenaWorld.getPlayers(),"The world border is shrinking to "+((int)worldSize - worldSize/5)+" blocks!");
                }

//                if(players.size() <= 1){
//                    active = false;
//                }

                if(!isActive()) {
                    try {
                        messageAll((ArrayList<Player>) arenaWorld.getPlayers(),players.get(0).getName()+" has won the UHC match!");
                        endGame();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    cancel();
                }
            }
        }.runTaskTimer(UHC.getPlugin(), 0L, 20L);

    }

    private void endGame() throws IOException {

        if (Bukkit.getWorld("uhcTEMP") == null) return;

        for (Player player : Objects.requireNonNull(Bukkit.getWorld("uhcTEMP")).getPlayers()) {
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(lobby);
        }

        players.clear();
        spectators.clear();

        Bukkit.unloadWorld("uhcTEMP", false);

        File del = new File("uhcTEMP");

        Bukkit.getLogger().info(del.getPath()+" has been deleted.");

        FileUtils.deleteDirectory(del.getPath());
    }

    public void setLobby(Location cords, World lobbyWorld){
        if(cords != null){
            lobby = new Location(lobbyWorld, cords.getX(), cords.getY(), cords.getZ());
        }
    }

    public void addPlayer(Player player){
        if(player.isOnline()){
            players.add(player);
            player.teleport(lobby);
            messageAll(players ,player.getName()+" joined the game. "+players.size()+"/20 Players");

            Score tempScore = timeObj.getScore(player.getName());
            tempScore.setScore(0);
        }

        if(players.size() >= 20){
            startGame();
        }
    }

    public void removePlayer(Player player){
        if(player.isOnline()){
            messageAll(players ,player.getName()+" left the game. "+players.size()+"/20 Players");

            players.remove(player);
            player.teleport(lobby);
        }
    }

    private void messageAll(ArrayList<Player> list ,String message){
        for(Player player : list)
            Msg.send(player, message);
    }
    public void addVote(){
        this.startGameVote++;
        if(players.size()/startGameVote <=2)
            startGame();
    }

    public boolean checkPlayer(Player player){
        return (player.isOnline() && players.contains(player));
    }
    public boolean isActive(){return active;}
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Player> getSpectators() {
        return spectators;
    }

    public void addSpectator(Player player){
        player.teleport(players.get((int) (Math.random()*players.size())).getLocation());
        player.setGameMode(GameMode.SPECTATOR);
        spectators.add(player);
    }

    public String getName(){return name;}
    public Objective getObj(){return this.timeObj;}
    public World getWorld(){return arenaWorld;}

}
