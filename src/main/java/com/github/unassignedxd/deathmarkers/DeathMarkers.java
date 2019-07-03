package com.github.unassignedxd.deathmarkers;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;

public final class DeathMarkers extends JavaPlugin implements Listener {

    public static ArrayList<PlayerInfo> playerInfoSet = new ArrayList<>();

    public static File path = new File("plugins//DeathMarkers");
    public static File cachedLastDeaths = new File("plugins//DeathMarkers//CachedLastDeaths.txt");

    @Override
    public void onEnable() {
        System.out.println("[Death Markers] Initializing Death Markers...");

        this.getServer().getPluginManager().registerEvents(this, this);

        if(!path.exists()) path.mkdir();

        if(!cachedLastDeaths.exists()) {
            try {
                cachedLastDeaths.createNewFile();
            } catch(Exception e) {
                System.out.println("[Death Markers] An error has occured while trying to create the cached death file! This should not happen!");
                e.printStackTrace();
            }
        }
        else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(cachedLastDeaths));
                playerInfoSet.clear();

                String read;
                while((read = reader.readLine()) != null) {
                    playerInfoSet.add(getPlayerInfoFromString(read));
                }
            } catch(Exception e) {
                System.out.println("[Death Markers] An error has occured while trying to read cached death data! This should not happen!");
                e.printStackTrace();
            }
        }

        System.out.println("[Death Markers] Initialization Complete!");
    }

    @Override
    public void onDisable() {
        System.out.println("[Death Markers] Shutting down Death Markers...");

        if (playerInfoSet.size() > 0) {
            try {
                System.out.println("[Death Markers] Writing Cached data!");
                new PrintWriter(cachedLastDeaths).close(); //clears the file

                BufferedWriter writer = new BufferedWriter(new FileWriter(cachedLastDeaths, true));
                for(PlayerInfo pInfo : playerInfoSet) {
                    writer.append(pInfo.getWriteableFormat() + "\n");
                }
                writer.close();
            } catch (Exception e) {
                System.out.println("An error has occured while trying to write cached deaht data! This should not happen!");
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(event.getEntity() != null) {
            Player p = event.getEntity();

            System.out.println("[Death Markers] A player death has occured! Notifying them of their position (X:"
                    + p.getLocation().getBlockX() + "; Y:" + p.getLocation().getBlockY() + "; Z:" + p.getLocation().getBlockZ() + ".");

            p.sendMessage(ChatColor.RED + "[Death Markers] You last died at: " + ChatColor.BOLD + ChatColor.GREEN + "X:" + p.getLocation().getBlockX() + "; Y:" + p.getLocation().getBlockY() + "; Z:" + p.getLocation().getBlockZ() + ChatColor.RESET + ".");
            for(PlayerInfo pInfo : playerInfoSet) {
                if(pInfo.getName().equalsIgnoreCase(p.getName())) {
                    playerInfoSet.remove(pInfo);
                    break;
                }
            }
            playerInfoSet.add(new PlayerInfo(p.getName(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ()));
            p.sendMessage(ChatColor.RED + "[Death Markers] To see this message again, use /deathmarker." + ChatColor.RESET);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(command.getName().equalsIgnoreCase("deathmarker")){
                for(PlayerInfo pInfo : playerInfoSet) {
                    if(pInfo.getName().equalsIgnoreCase(sender.getName())){
                        sender.sendMessage(ChatColor.RED + "[Death Markers] You last died at: " + ChatColor.BOLD + ChatColor.GREEN + "X: " + pInfo.getxCoord() + "; Y:" + pInfo.getyCoord() + "; Z:" + pInfo.getzCoord() + ChatColor.RESET + ".");
                        return true;
                    }
                }
                sender.sendMessage("You have no known last death location!");
                return true;
            }
        } else { System.out.println("Death Marker commands can only be run as the player!"); }

        return false;
    }

    //Format: name#xCoord#yCoord#zCoord#
    public PlayerInfo getPlayerInfoFromString(String decode) {
        String name = "";
        double xCoord = 0;
        double yCoord = 0;
        double zCoord = 0;

        String[] strings = decode.split("#");
        if(strings.length == 4){ //which it always should
            for(int i = 0; i < 4; i++) {
                if(i == 0) name = strings[0];
                if(i == 1) xCoord = Double.parseDouble(strings[1]);
                if(i == 2) yCoord = Double.parseDouble(strings[2]);
                if(i == 3) zCoord = Double.parseDouble(strings[3]);
            }
        }

        PlayerInfo returnInfo = new PlayerInfo(name, xCoord, yCoord, zCoord);
        return returnInfo;
    }

}
