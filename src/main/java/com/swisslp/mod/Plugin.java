package com.swisslp.mod;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class Plugin extends JavaPlugin implements Listener {
    private final File backupFolder = new File(getDataFolder(), "backups");
    private final HashMap<Block, List<String>> chestLogs = new HashMap<>();
    private final List<String> blockBreakLogs = new ArrayList<>();
    private final HashMap<String, Inventory> pages = new HashMap<>();
    private final HashMap<ItemStack, Player> playerHeads = new HashMap<>();
    private final HashMap<UUID, LocalDateTime> timeoutList = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("SwissLPs Mod aktiviert!");

        // Erstelle Backup-Ordner, falls nicht vorhanden
        if (!backupFolder.exists()) {
            backupFolder.mkdirs();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("SwissLPs Mod deaktiviert!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Nur Spieler können diesen Befehl verwenden!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "Du hast keine Rechte für diesen Command!");
            return true;
        }

        switch (command.getName().toLowerCase()) {
            case "restorechest":
                if (args.length != 1) {
                    player.sendMessage(ChatColor.RED + "Benutzung: /restorechest <Uhrzeit/Tag>");
                    return true;
                }
                restoreChest(player, args[0]);
                return true;

            case "restoreinventory":
                if (args.length != 2) {
                    player.sendMessage(ChatColor.RED + "Benutzung: /restoreinventory <Name> <Uhrzeit/Tag>");
                    return true;
                }
                restoreInventory(player, args[0], args[1]);
                return true;

            case "listinventar":
                createPages(player);
                openPage(player, 1);
                return true;

            case "listkiste":
                Block targetBlock = player.getTargetBlockExact(5);
                if (targetBlock == null || !(targetBlock.getState() instanceof Chest)) {
                    player.sendMessage(ChatColor.RED + "Du musst auf eine Kiste schauen, um die Logs zu sehen!");
                    return true;
                }

                List<String> logs = chestLogs.getOrDefault(targetBlock, new ArrayList<>());
                if (logs.isEmpty()) {
                    player.sendMessage(ChatColor.YELLOW + "Keine Interaktionen mit dieser Kiste gefunden.");
                } else {
                    player.sendMessage(ChatColor.GOLD + "Kisten-Logs:");
                    for (String log : logs) {
                        player.sendMessage(ChatColor.GRAY + log);
                    }
                }
                return true;

            case "listblock":
                if (blockBreakLogs.isEmpty()) {
                    player.sendMessage(ChatColor.YELLOW + "Keine abgebauten Blöcke gefunden.");
                } else {
                    player.sendMessage(ChatColor.GOLD + "Block-Abbau-Logs:");
                    for (String log : blockBreakLogs) {
                        player.sendMessage(ChatColor.GRAY + log);
                    }
                }
                return true;

            case "timeout":
                if (args.length != 2) {
                    player.sendMessage(ChatColor.RED + "Benutzung: /timeout <spielername> <stunden>");
                    return true;
                }

                Player target = Bukkit.getPlayer(args[0]);
                if (target == null || !target.isOnline()) {
                    player.sendMessage(ChatColor.RED + "Der Spieler ist nicht online!");
                    return true;
                }

                int hours;
                try {
                    hours = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Bitte gib eine gültige Anzahl an Stunden an.");
                    return true;
                }

                LocalDateTime timeoutEnd = LocalDateTime.now().plusHours(hours);
                timeoutList.put(target.getUniqueId(), timeoutEnd);

                target.kickPlayer(ChatColor.RED + "Du wurdest für " + hours + " Stunden vom Server ausgeschlossen.");
                player.sendMessage(ChatColor.GREEN + target.getName() + " wurde für " + hours + " Stunden getimeoutet.");
                return true;

            default:
                return false;
        }
    }

    private void restoreChest(Player player, String timestamp) {
        Block targetBlock = player.getTargetBlockExact(5);
        if (targetBlock == null || !(targetBlock.getState() instanceof Chest)) {
            player.sendMessage(ChatColor.RED + "Du musst auf eine Kiste schauen, um sie wiederherzustellen!");
            return;
        }

        File backupFile = new File(backupFolder, "chest_backup_" + timestamp + ".txt");
        if (!backupFile.exists()) {
            player.sendMessage(ChatColor.RED + "Kein Backup für den angegebenen Zeitpunkt gefunden!");
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                try (BufferedReader reader = new BufferedReader(new FileReader(backupFile))) {
                    List<ItemStack> items = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(" x ");
                        if (parts.length == 2) {
                            Material material = Material.getMaterial(parts[0]);
                            int amount = Integer.parseInt(parts[1]);
                            if (material != null) {
                                items.add(new ItemStack(material, amount));
                            }
                        }
                    }

                    Bukkit.getScheduler().runTask(Plugin.this, () -> {
                        Chest chest = (Chest) targetBlock.getState();
                        Inventory chestInventory = chest.getInventory();
                        chestInventory.clear();
                        for (ItemStack item : items) {
                            chestInventory.addItem(item);
                        }
                        player.sendMessage(ChatColor.GREEN + "Kiste erfolgreich wiederhergestellt!");
                    });
                } catch (IOException e) {
                    player.sendMessage(ChatColor.RED + "Fehler beim Wiederherstellen der Kiste!");
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(this);
    }

    private void restoreInventory(Player player, String playerName, String timestamp) {
        File backupFile = new File(backupFolder, "inventory_backup_" + timestamp + ".txt");
        if (!backupFile.exists()) {
            player.sendMessage(ChatColor.RED + "Kein Backup für den angegebenen Zeitpunkt gefunden!");
            return;
        }

        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Spieler ist nicht online!");
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                try (BufferedReader reader = new BufferedReader(new FileReader(backupFile))) {
                    List<ItemStack> items = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(" x ");
                        if (parts.length == 2) {
                            Material material = Material.getMaterial(parts[0]);
                            int amount = Integer.parseInt(parts[1]);
                            if (material != null) {
                                items.add(new ItemStack(material, amount));
                            }
                        }
                    }

                    Bukkit.getScheduler().runTask(Plugin.this, () -> {
                        Inventory targetInventory = target.getInventory();
                        targetInventory.clear();
                        for (ItemStack item : items) {
                            targetInventory.addItem(item);
                        }
                        player.sendMessage(ChatColor.GREEN + "Inventar von " + target.getName() + " erfolgreich wiederhergestellt!");
                    });
                } catch (IOException e) {
                    player.sendMessage(ChatColor.RED + "Fehler beim Wiederherstellen des Inventars!");
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(this);
    }

    private void createPages(Player player) {
        List<Player> allPlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        int itemsPerPage = 45;
        int totalPages = (int) Math.ceil(allPlayers.size() / (double) itemsPerPage);

        for (int page = 1; page <= totalPages; page++) {
            Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Spieler-Seite " + page);

            int start = (page - 1) * itemsPerPage;
            int end = Math.min(start + itemsPerPage, allPlayers.size());

            for (int i = start; i < end; i++) {
                Player onlinePlayer = allPlayers.get(i);

                ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

                assert skullMeta != null;
                skullMeta.setOwningPlayer(onlinePlayer);
                skullMeta.setDisplayName(ChatColor.GREEN + onlinePlayer.getName());
                skull.setItemMeta(skullMeta);

                inventory.addItem(skull);
                playerHeads.put(skull, onlinePlayer);
            }

            pages.put(player.getUniqueId() + ":" + page, inventory);
        }
    }

    private void openPage(Player player, int page) {
        Inventory inventory = pages.get(player.getUniqueId() + ":" + page);
        if (inventory != null) {
            player.openInventory(inventory);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        String blockInfo = "Block: " + block.getType() +
                " bei X: " + block.getLocation().getBlockX() +
                ", Y: " + block.getLocation().getBlockY() +
                ", Z: " + block.getLocation().getBlockZ() +
                " von Spieler: " + player.getName();

        blockBreakLogs.add(blockInfo);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith(ChatColor.BLUE + "Spieler-Seite")) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
                Player targetPlayer = playerHeads.get(clickedItem);

                if (targetPlayer != null && targetPlayer.isOnline()) {
                    ((Player) event.getWhoClicked()).openInventory(targetPlayer.getInventory());
                }
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof Chest) {
            Chest chest = (Chest) event.getInventory().getHolder();
            Player player = (Player) event.getPlayer();

            chestLogs.putIfAbsent(chest.getBlock(), new ArrayList<>());
            chestLogs.get(chest.getBlock()).add(player.getName() + " hat die Kiste geöffnet.");
        }
    }
}