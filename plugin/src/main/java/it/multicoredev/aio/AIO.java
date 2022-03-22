package it.multicoredev.aio;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.google.gson.GsonBuilder;
import it.multicoredev.aio.api.*;
import it.multicoredev.aio.api.events.AfkToggleEvent;
import it.multicoredev.aio.api.events.PostCommandEvent;
import it.multicoredev.aio.api.listeners.IListenerRegistry;
import it.multicoredev.aio.api.models.CommandData;
import it.multicoredev.aio.api.models.Module;
import it.multicoredev.aio.api.models.User;
import it.multicoredev.aio.api.tp.ITeleportManager;
import it.multicoredev.aio.api.utils.IPlaceholdersUtils;
import it.multicoredev.aio.commands.AIOCommand;
import it.multicoredev.aio.commands.AliasCommand;
import it.multicoredev.aio.commands.economy.EconomyCommand;
import it.multicoredev.aio.commands.player.*;
import it.multicoredev.aio.commands.player.kits.KitCommand;
import it.multicoredev.aio.commands.player.kits.KitsCommand;
import it.multicoredev.aio.commands.teleport.BackCommand;
import it.multicoredev.aio.commands.teleport.RTPCommand;
import it.multicoredev.aio.commands.teleport.home.*;
import it.multicoredev.aio.commands.teleport.spawn.SetSpawnCommand;
import it.multicoredev.aio.commands.teleport.spawn.SpawnCommand;
import it.multicoredev.aio.commands.teleport.requests.*;
import it.multicoredev.aio.commands.teleport.warp.DelWarpCommand;
import it.multicoredev.aio.commands.teleport.warp.SetWarpCommand;
import it.multicoredev.aio.commands.teleport.warp.WarpCommand;
import it.multicoredev.aio.commands.teleport.warp.WarpsCommand;
import it.multicoredev.aio.commands.utilities.*;
import it.multicoredev.aio.commands.utilities.time_and_weather.*;
import it.multicoredev.aio.listeners.aio.AfkListener;
import it.multicoredev.aio.listeners.aio.PostCommandListener;
import it.multicoredev.aio.listeners.entity.EntityDamageListener;
import it.multicoredev.aio.listeners.player.*;
import it.multicoredev.aio.api.models.HelpBook;
import it.multicoredev.aio.storage.config.Commands;
import it.multicoredev.aio.storage.config.Config;
import it.multicoredev.aio.storage.config.Localization;
import it.multicoredev.aio.storage.config.ModuleManager;
import it.multicoredev.aio.storage.config.adapters.LocationAdapter;
import it.multicoredev.aio.storage.config.modules.*;
import it.multicoredev.aio.storage.config.sections.StorageSection;
import it.multicoredev.aio.storage.data.FileStorage;
import it.multicoredev.aio.storage.data.KitStorage;
import it.multicoredev.aio.storage.data.WarpStorage;
import it.multicoredev.aio.tasks.ClearCacheTask;
import it.multicoredev.aio.tasks.PlayerMoveTask;
import it.multicoredev.aio.tasks.SavePlayerDataTask;
import it.multicoredev.aio.utils.ReflectionUtils;
import it.multicoredev.aio.utils.perms.PermissionHandler;
import it.multicoredev.aio.utils.placeholders.PAPIPlaceholderHook;
import it.multicoredev.aio.utils.placeholders.PAPIPlaceholdersUtils;
import it.multicoredev.aio.utils.placeholders.StdPlaceholdersUtils;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mclib.json.GsonHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Copyright &copy; 2021 - 2022 by Lorenzo Magni &amp; Daniele Patella
 * This file is part of AIO.
 * AIO is under "The 3-Clause BSD License", you can find a copy <a href="https://opensource.org/licenses/BSD-3-Clause">here</a>.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
public class AIO extends it.multicoredev.aio.api.AIO {
    public static final GsonHelper gson = new GsonHelper(new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .registerTypeAdapter(Location.class, new LocationAdapter())
            .create());

    public static boolean VAULT;
    public static boolean LUCKPERMS;
    public static boolean PAPI;

    public static final Class<ChatModule> CHAT_MODULE = ChatModule.class;
    public static final Class<CommandAliasesModule> COMMAND_ALIASES_MODULE = CommandAliasesModule.class;
    public static final Class<EconomyModule> ECONOMY_MODULE = EconomyModule.class;
    public static final Class<JoinQuitModule> JOIN_QUIT_MODULE = JoinQuitModule.class;
    public static final Class<PingModule> PING_MODULE = PingModule.class;
    public static final Class<SpawnModule> SPAWN_MODULE = SpawnModule.class;

    private final File root = getDataFolder();
    private final File modulesDir = new File(root, "modules");
    private final File helpbooksDir = new File(root, "helpbooks");
    private final File configFile = new File(root, "config.json");
    private final File localizationFile = new File(root, "localization.json");
    private final File commandsFile = new File(root, "commands.json");
    private final File usermapFile = new File(root, "usermap.json");
    private final File kitsFile = new File(root, "kits.json");
    private final File warpsFile = new File(root, "warps.json");

    private Config config;
    private Localization localization;
    private ModuleManager moduleManager;
    private final List<HelpBook> helpbooks = new ArrayList<>();
    private Commands commands;
    private IStorage storage;
    private Map<String, UUID> usermap;
    private KitStorage kitStorage;
    private WarpStorage warpStorage;
    private final Map<UUID, User> usersCache = new HashMap<>();

    private final Map<String, BukkitTask> tasks = new HashMap<>();
    private final Map<UUID, BukkitTask> deferredCommands = new HashMap<>();

    private CommandRegistry commandRegistry;
    private ListenerRegistry listenerRegistry;
    private TeleportManager tpManager;
    private Map<UUID, Map<String, Date>> commandCooldown;
    private AIOEconomy economy;
    private PermissionHandler permissionHandler;
    private IPlaceholdersUtils placeholdersUtils;

    public static boolean debug = true;

    // Priorities: 🟢 - Low priority, 🟡 - Medium priority, 🔴 - High priority
    //TODO 🔴 Improve argument completions
    //TODO 🟢 Enchant disenchant commands change output msg enchant name
    //TODO 🔴 Fly speed (Save also walk speed and set on Join)
    //TODO 🔴 Change command syntax to /command [on|off|toggle] [player]
    //TODO 🔴 ALL Chat.send must have placeholderutils.replace....
    //TODO 🟡 Add the ability to log transactions inside AIOEconomy
    //TODO 🔴 Use this everywhere !hasSubPerm(sender, "other") && !sender.equals(target)
    //TODO 🟢 Runlater command should send feedback when the command is executed
    //TODO 🔴 Require economy module enabled in checks
    //TODO 🟢 Add Objects.requireNonNull(...) where needed
    //TODO 🔴 Cancel pending teleports and requests when the player quits
    //TODO 🔴 placeholderutils.replacePlaceholders does not have the player
    //TODO 🟡 Add vanilla tp command replacement (Priority low)
    //TODO 🟢 Add validate method to configs
    //TODO 🟢 Add exceptions in api javadoc
    //TODO 🟢 Add swim speed

    @Override
    public void onEnable() {
        aio = this;

        moduleManager = new ModuleManager(this, modulesDir);
        registerModules();

        if (!initConfigs()) {
            onDisable();
            return;
        }

        debug = config.debug;

        switch (config.storageSection.storageType) {
            case StorageSection.FILE:
                try {
                    storage = new FileStorage(this);
                } catch (Exception e) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e.printStackTrace();

                    onDisable();
                    return;
                }
                break;
            case StorageSection.MYSQL:
                //TODO MySQL
                break;
            case StorageSection.SQLITE:
                //TODO SQLite
                break;
        }

        try {
            commandRegistry = new CommandRegistry(ReflectionUtils.getCommandMap(this));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Chat.severe("&4" + e.getMessage());
            if (debug) e.printStackTrace();
            onDisable();
            return;
        }

        listenerRegistry = new ListenerRegistry();
        tpManager = new TeleportManager(this);

        if (config.cmdsCooldownSection.cooldownEnabled) commandCooldown = new HashMap<>();

        initDependencies();

        if (VAULT) {
            economy = new AIOEconomy(this);
            getServer().getServicesManager().register(IEconomy.class, economy, this, ServicePriority.Highest);
        }

        permissionHandler = new PermissionHandler(this);

        if (PAPI) {
            placeholdersUtils = new PAPIPlaceholdersUtils();
            new PAPIPlaceholderHook(this).register();
        } else placeholdersUtils = new StdPlaceholdersUtils();

        registerListeners();
        registerCommands();

        startTasks();

        if (debug) logPluginSettings();

        Chat.info("&eAIO &2enabled&e.");
    }

    @Override
    public void onDisable() {
        stopDeferredCommands();
        stopTasks();

        usersCache.clear();

        HandlerList.unregisterAll(this);

        if (commandRegistry != null) commandRegistry.unregisterCommands();

        Chat.info("&eAIO &cdisabled&e.");
        System.gc();
    }

    @Override
    public IModuleManager getModuleManager() {
        return moduleManager;
    }

    @Override
    public ICommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    @Override
    public IListenerRegistry getListenerRegistry() {
        return listenerRegistry;
    }

    @Override
    public ITeleportManager getTeleportManager() {
        return tpManager;
    }

    @Override
    public IStorage getStorage() {
        return storage;
    }

    @Override
    @Nullable
    public IEconomy getEconomy() {
        return economy;
    }

    @Override
    public IKits getKits() {
        return kitStorage;
    }

    @Override
    public IWarps getWarps() {
        return warpStorage;
    }

    @Override
    public List<HelpBook> getHelpBooks() {
        return Collections.unmodifiableList(helpbooks);
    }

    @Override
    @Nullable
    public HelpBook getHelpBook(String id) {
        for (HelpBook hb : helpbooks) {
            if (Objects.equals(hb.id, id)) return hb;
        }

        return null;
    }

    @Override
    public boolean addHelpBook(HelpBook helpBook) {
        if (getHelpBook(helpBook.id) != null) return false;
        helpbooks.add(helpBook);
        return true;
    }

    @Override
    public IPlaceholdersUtils getPlaceholdersUtils() {
        return placeholdersUtils;
    }

    public PermissionHandler getPermissionHandler() {
        return permissionHandler;
    }

    public Config getConfiguration() {
        return config;
    }

    public Localization getLocalization() {
        return localization;
    }

    public CommandData getCommandData(@NotNull String command) {
        return commands.getCommand(command);
    }

    public static void saveAsync(Object obj, File file) {
        new Thread(() -> {
            try {
                gson.save(obj, file);
            } catch (IOException e) {
                Chat.warning("&c" + e.getMessage());
                if (debug) e.printStackTrace();
            }
        }).start();
    }

    public Map<UUID, User> getUsersCache() {
        return usersCache;
    }

    public User getUserFromCache(UUID uuid) {
        return usersCache.get(uuid);
    }

    public void addUserToCache(User user) {
        usersCache.put(user.getUniqueId(), user);
    }

    public void removeUserFromCache(UUID uuid) {
        usersCache.remove(uuid);
    }

    @Nullable
    public UUID getUserUUID(String name) {
        if (usermap.containsKey(name)) {
            return usermap.get(name);
        } else {
            User user = storage.searchUser(name);
            return user != null ? user.getUniqueId() : null;
        }
    }

    public void addToUsermap(String name, UUID uuid) {
        usermap.put(name, uuid);
        saveAsync(usermap, usermapFile);
    }

    public void saveKits() {
        saveAsync(kitStorage, kitsFile);
    }

    public void saveWarps() {
        saveAsync(warpStorage, warpsFile);
    }

    public void addCommandCooldown(Player player, String command) {
        if (!config.cmdsCooldownSection.hasCommandCooldown(command)) return;
        if (player.hasPermission("aio.no-commands-cooldown")) return;

        UUID uuid = player.getUniqueId();
        if (commandCooldown.containsKey(uuid)) {
            Map<String, Date> commands = commandCooldown.get(uuid);
            commands.put(command, new Date());
        } else {
            Map<String, Date> commands = new HashMap<>();
            commands.put(command, new Date());
            commandCooldown.put(uuid, commands);
        }
    }

    public int hasCommandCooldown(Player player, String command) {
        UUID uuid = player.getUniqueId();
        if (!commandCooldown.containsKey(uuid)) return -1;

        Map<String, Date> commands = commandCooldown.get(uuid);
        if (!commands.containsKey(command)) return -1;

        Date date = commands.get(command);
        int difference = (int) ((new Date().getTime() - date.getTime()) / 1000);
        int cooldown = config.cmdsCooldownSection.getCommandCooldown(command);

        if (difference > cooldown) {
            commands.remove(command);
            return -1;
        }

        return cooldown - difference;
    }

    public void addDeferredCommand(CommandSender sender, String command, long delay) {
        UUID uuid = UUID.randomUUID();
        deferredCommands.put(uuid, Bukkit.getScheduler().runTaskLater(this, () -> {
            Bukkit.dispatchCommand(sender, command);
            removeCompletedCommand(uuid);
        }, delay * 20));
    }

    private void registerModules() {
        moduleManager.registerModule(new ChatModule());
        moduleManager.registerModule(new CommandAliasesModule());
        moduleManager.registerModule(new EconomyModule());
        moduleManager.registerModule(new JoinQuitModule());
        moduleManager.registerModule(new PingModule());
        moduleManager.registerModule(new SpawnModule());
    }

    private boolean initConfigs() {
        if (!root.exists() || !root.isDirectory()) {
            if (!root.mkdirs()) {
                Chat.severe("&4Cannot create data folder");
                return false;
            }
        }

        if (!configFile.exists() || !configFile.isFile()) {
            config = new Config();
            config.init();

            try {
                gson.save(config, configFile);
            } catch (IOException e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();
                return false;
            }
        } else {
            try {
                config = gson.load(configFile, Config.class);
                if (config == null) throw new NullPointerException("Config is null");
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();

                if (!backupFile(configFile)) return false;
                Chat.warning("&4Config file is corrupted, creating new one");

                config = new Config();
                config.init();

                try {
                    gson.save(config, configFile);
                } catch (IOException e1) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e1.printStackTrace();
                    return false;
                }
            }

            if (config.completeMissing()) {
                try {
                    gson.save(config, configFile);
                } catch (IOException e) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e.printStackTrace();
                    return false;
                }
            }
        }

        if (!localizationFile.exists() || !localizationFile.isFile()) {
            localization = new Localization();
            localization.init();

            try {
                gson.save(localization, localizationFile);
            } catch (IOException e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();
                return false;
            }
        } else {
            try {
                localization = gson.load(localizationFile, Localization.class);
                if (localization == null) throw new NullPointerException("Localization is null");
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();

                if (!backupFile(localizationFile)) return false;
                Chat.warning("&4Localization file is corrupted, creating new one");

                localization = new Localization();
                localization.init();

                try {
                    gson.save(localization, localizationFile);
                } catch (IOException e1) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e1.printStackTrace();
                    return false;
                }
            }

            if (localization.completeMissing()) {
                try {
                    gson.save(localization, localizationFile);
                } catch (IOException e) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e.printStackTrace();
                    return false;
                }
            }
        }

        if (!commandsFile.exists() || !commandsFile.isFile()) {
            commands = new Commands();
            commands.init();

            try {
                gson.save(commands, commandsFile);
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();
                return false;
            }
        } else {
            try {
                commands = gson.load(commandsFile, Commands.class);
                if (commands == null) throw new NullPointerException("commands is null");
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();

                if (!backupFile(commandsFile)) return false;
                Chat.warning("&4commands file is corrupted, creating new one");

                commands = new Commands();
                commands.init();

                try {
                    gson.save(commands, commandsFile);
                } catch (IOException e1) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e1.printStackTrace();
                    return false;
                }
            }

            if (commands.completeMissing()) {
                try {
                    gson.save(commands, commandsFile);
                } catch (IOException e) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e.printStackTrace();
                    return false;
                }
            }
        }

        if (!modulesDir.exists() || !modulesDir.isDirectory()) {
            if (!modulesDir.mkdir()) {
                Chat.severe("&4Cannot create module folder");
                return false;
            }
        }

        for (Module module : moduleManager.getModules()) {
            if (moduleManager.moduleFileExists(module.getName())) {
                if (!moduleManager.saveModule(module.getName())) return false;
            } else {
                if (!moduleManager.loadModule(module.getName())) return false;

                module = moduleManager.getModule(module.getName());
                if (module != null && module.completeMissing()) {
                    if (!moduleManager.saveModule(module.getName())) return false;
                }
            }
        }

        if (!helpbooksDir.exists() || !helpbooksDir.isDirectory()) {
            if (!helpbooksDir.mkdir()) {
                Chat.severe("&4Cannot create helpbooks folder");
                return false;
            }
        }

        File[] helpbooks = helpbooksDir.listFiles();
        if (helpbooks != null) {
            for (File hbf : helpbooks) {
                if (!hbf.isFile() || !hbf.getName().toLowerCase(Locale.ROOT).endsWith(".json"))
                    continue;

                try {
                    HelpBook hb = gson.load(hbf, HelpBook.class);
                    if (hb == null) throw new NullPointerException("Helpbook " + hbf.getName() + " is null");

                    if (hb.completeMissing()) {
                        try {
                            gson.save(hb, hbf);
                        } catch (IOException e) {
                            Chat.warning("&4" + e.getMessage());
                            if (debug) e.printStackTrace();
                        }
                    }

                    this.helpbooks.add(hb);
                } catch (Exception e) {
                    Chat.warning("&eHelpbook " + hbf.getName() + " is corrupted or has an invalid format.");
                }
            }
        }

        if (!warpsFile.exists() || !warpsFile.isFile()) {
            warpStorage = new WarpStorage();
            warpStorage.init();

            try {
                gson.save(warpStorage, warpsFile);
            } catch (IOException e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();
                return false;
            }
        } else {
            try {
                warpStorage = gson.load(warpsFile, WarpStorage.class);
                if (warpStorage == null) throw new NullPointerException("WarpStorage is null");
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();

                if (!backupFile(warpsFile)) return false;
                Chat.warning("&4WarpStorage file is corrupted, creating new one");

                warpStorage = new WarpStorage();
                warpStorage.init();

                try {
                    gson.save(warpStorage, warpsFile);
                } catch (IOException e1) {
                    Chat.severe("&4" + e1.getMessage());
                    if (debug) e1.printStackTrace();
                    return false;
                }
            }

            if (warpStorage.completeMissing()) {
                try {
                    gson.save(warpStorage, warpsFile);
                } catch (IOException e) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e.printStackTrace();
                    return false;
                }
            }
        }

        if (!kitsFile.exists() || !kitsFile.isFile()) {
            kitStorage = new KitStorage();
            kitStorage.init();

            try {
                gson.save(kitStorage, kitsFile);
            } catch (IOException e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();
                return false;
            }
        } else {
            try {
                kitStorage = gson.load(kitsFile, KitStorage.class);
                if (kitStorage == null) throw new NullPointerException(("KitStorage is null"));
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();

                if (!backupFile(kitsFile)) return false;
                Chat.warning("&4KitStorage file is corrupted, creating new one");

                kitStorage = new KitStorage();
                kitStorage.init();

                try {
                    gson.save(kitStorage, kitsFile);
                } catch (IOException e1) {
                    Chat.severe("&4" + e1.getMessage());
                    if (debug) e1.printStackTrace();
                    return false;
                }
            }

            if (kitStorage.completeMissing()) {
                try {
                    gson.save(kitStorage, kitsFile);
                } catch (IOException e) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e.printStackTrace();
                    return false;
                }
            }
        }

        if (!usermapFile.exists() || !usermapFile.isFile()) {
            usermap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

            try {
                gson.save(usermap, usermapFile);
            } catch (IOException e) {
                Chat.severe("&cCannot save usermap.json");
                Chat.severe("&4" + e.getMessage());
                return false;
            }
        } else {
            try {
                usermap = gson.load(usermapFile, TreeMap.class);
                if (usermap == null) throw new NullPointerException("Usermap is null");
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();

                if (!backupFile(usermapFile)) return false;
                Chat.warning("&4Usermap file is corrupted, creating new one");

                usermap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

                try {
                    gson.save(usermap, usermapFile);
                } catch (IOException e1) {
                    Chat.severe("&cCannot save usermap.json");
                    if (debug) e1.printStackTrace();
                    return false;
                }
            }
        }

        return true;
    }

    @SuppressWarnings("UnstableApiUsage")
    private boolean backupFile(@NotNull File file) {
        Preconditions.checkNotNull(file);
        Preconditions.checkArgument(file.exists() && file.isFile());

        File dst = new File(file.getParent(), file.getName() + ".bak");

        try {
            Files.move(file, dst);
        } catch (IOException e) {
            Chat.severe("&4" + e.getMessage());
            return false;
        }

        return true;
    }

    private void initDependencies() {
        VAULT = getServer().getPluginManager().getPlugin("Vault") != null;
        LUCKPERMS = getServer().getPluginManager().getPlugin("LuckPerms") != null;
        PAPI = getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;

        if (debug) {
            if (VAULT) Chat.info("&aVault found.");
            else Chat.info("&eVault not found.");

            if (LUCKPERMS) Chat.info("&aLuckPerms found.");
            else Chat.info("&eLuckPerms not found.");

            if (PAPI) Chat.info("&aPlaceholderAPI found.");
            else Chat.info("&ePlaceholderAPI not found.");
        }
    }

    private void removeCompletedCommand(UUID uuid) {
        deferredCommands.remove(uuid);
    }

    private void registerListeners() {
        listenerRegistry.registerListener(new PostCommandListener(PostCommandEvent.class, this), config.getEventPriority("PostCommandEvent"), this);
        listenerRegistry.registerListener(new AfkListener(AfkToggleEvent.class, this), config.getEventPriority("AfkToggleEvent"), this);

        listenerRegistry.registerListener(new EntityDamageListener(EntityDamageEvent.class, this), config.getEventPriority("EntityDamageEvent"), this);

        listenerRegistry.registerListener(new AsyncPlayerChatListener(AsyncPlayerChatEvent.class, this), config.getEventPriority("AsyncPlayerChatEvent"), this);
        listenerRegistry.registerListener(new PlayerCommandPreprocessListener(PlayerCommandPreprocessEvent.class, this), config.getEventPriority("PlayerCommandPreprocessEvent"), this);
        listenerRegistry.registerListener(new PlayerDeathListener(PlayerDeathEvent.class, this), config.getEventPriority("PlayerDeathEvent"), this);
        listenerRegistry.registerListener(new PlayerInteractListener(PlayerInteractEvent.class, this), config.getEventPriority("PlayerInteractEvent"), this);
        listenerRegistry.registerListener(new PlayerJoinListener(PlayerJoinEvent.class, this), config.getEventPriority("PlayerJoinEvent"), this);
        listenerRegistry.registerListener(new PlayerQuitListener(PlayerQuitEvent.class, this), config.getEventPriority("PlayerQuitEvent"), this);
        listenerRegistry.registerListener(new PlayerRespawnListener(PlayerRespawnEvent.class, this), config.getEventPriority("PlayerRespawnEvent"), this);
        listenerRegistry.registerListener(new PlayerTeleportListener(PlayerTeleportEvent.class, this), config.getEventPriority("PlayerTeleportEvent"), this);
    }

    private void registerCommands() {
        commandRegistry.registerCommand(new AIOCommand(this), this);

        if (commands.isEnabled("afk")) commandRegistry.registerCommand(new AfkCommand(this), this);
        if (commands.isEnabled("back")) commandRegistry.registerCommand(new BackCommand(this), this);
        if (commands.isEnabled("cleanchat")) commandRegistry.registerCommand(new CleanChatCommand(this), this);
        if (commands.isEnabled("day")) commandRegistry.registerCommand(new DayCommand(this), this);
        if (commands.isEnabled("delhome")) commandRegistry.registerCommand(new DelHomeCommand(this), this);
        if (commands.isEnabled("delwarp")) commandRegistry.registerCommand(new DelWarpCommand(this), this);
        if (commands.isEnabled("disenchant")) commandRegistry.registerCommand(new DisenchantCommand(this), this);
        if (commands.isEnabled("economy") && moduleManager.isModuleEnabled(ECONOMY_MODULE)) commandRegistry.registerCommand(new EconomyCommand(this), this);
        if (commands.isEnabled("enchant")) commandRegistry.registerCommand(new EnchantCommand(this), this);
        if (commands.isEnabled("entitylist")) commandRegistry.registerCommand(new EntitylistCommand(this), this);
        if (commands.isEnabled("feed")) commandRegistry.registerCommand(new FeedCommand(this), this);
        if (commands.isEnabled("fly")) commandRegistry.registerCommand(new FlyCommand(this), this);
        if (commands.isEnabled("gamemode")) commandRegistry.registerCommand(new GamemodeCommand(this), this);
        if (commands.isEnabled("god")) commandRegistry.registerCommand(new GodCommand(this), this);
        if (commands.isEnabled("hat")) commandRegistry.registerCommand(new HatCommand(this), this);
        if (commands.isEnabled("heal")) commandRegistry.registerCommand(new HealCommand(this), this);
        if (commands.isEnabled("helpbook")) commandRegistry.registerCommand(new HelpBookCommand(this), this);
        if (commands.isEnabled("home")) commandRegistry.registerCommand(new HomeCommand(this), this);
        if (commands.isEnabled("homes")) commandRegistry.registerCommand(new HomesCommand(this), this);
        if (commands.isEnabled("kit")) commandRegistry.registerCommand(new KitCommand(this), this);
        if (commands.isEnabled("kits")) commandRegistry.registerCommand(new KitsCommand(this), this);
        if (commands.isEnabled("lightning")) commandRegistry.registerCommand(new LightningCommand(this), this);
        if (commands.isEnabled("nickname")) commandRegistry.registerCommand(new NicknameCommand(this), this);
        if (commands.isEnabled("night")) commandRegistry.registerCommand(new NightCommand(this), this);
        if (commands.isEnabled("playerhome")) commandRegistry.registerCommand(new PlayerHomeCommand(this), this);
        if (commands.isEnabled("rain")) commandRegistry.registerCommand(new RainCommand(this), this);
        if (commands.isEnabled("repair")) commandRegistry.registerCommand(new RepairCommand(this), this);
        if (commands.isEnabled("rtp")) commandRegistry.registerCommand(new RTPCommand(this), this);
        if (commands.isEnabled("runlater")) commandRegistry.registerCommand(new RunLaterCommand(this), this);
        if (commands.isEnabled("sethome")) commandRegistry.registerCommand(new SetHomeCommand(this), this);
        if (commands.isEnabled("setspawn") && moduleManager.isModuleEnabled(SPAWN_MODULE))
            commandRegistry.registerCommand(new SetSpawnCommand(this), this);
        if (commands.isEnabled("setwarp")) commandRegistry.registerCommand(new SetWarpCommand(this), this);
        if (commands.isEnabled("spawn") && moduleManager.isModuleEnabled(SPAWN_MODULE))
            commandRegistry.registerCommand(new SpawnCommand(this), this);
        if (commands.isEnabled("speed")) commandRegistry.registerCommand(new SpeedCommand(this), this);
        if (commands.isEnabled("suicide")) commandRegistry.registerCommand(new SuicideCommand(this), this);
        if (commands.isEnabled("sudo")) commandRegistry.registerCommand(new SudoCommand(this), this);
        if (commands.isEnabled("sun")) commandRegistry.registerCommand(new SunCommand(this), this);
        if (commands.isEnabled("thunder")) commandRegistry.registerCommand(new ThunderCommand(this), this);
        if (commands.isEnabled("tpa")) commandRegistry.registerCommand(new TpaCommand(this), this);
        if (commands.isEnabled("tpacancel")) commandRegistry.registerCommand(new TpcCommand(this), this);
        if (commands.isEnabled("tpahere")) commandRegistry.registerCommand(new TpahereCommand(this), this);
        if (commands.isEnabled("tpall")) commandRegistry.registerCommand(new TpallCommand(this), this);
        if (commands.isEnabled("tpyes")) commandRegistry.registerCommand(new TpyesCommand(this), this);
        if (commands.isEnabled("tpno")) commandRegistry.registerCommand(new TpnoCommand(this), this);
        if (commands.isEnabled("trash")) commandRegistry.registerCommand(new TrashCommand(this), this);
        if (commands.isEnabled("warp")) commandRegistry.registerCommand(new WarpCommand(this), this);
        if (commands.isEnabled("warps")) commandRegistry.registerCommand(new WarpsCommand(this), this);

        if (moduleManager.isModuleEnabled(COMMAND_ALIASES_MODULE)) {
            CommandAliasesModule aliasesModule = moduleManager.getModule(COMMAND_ALIASES_MODULE);
            if (aliasesModule == null) return;

            aliasesModule.aliases.forEach(alias -> {
                if (alias.aliases.isEmpty()) return;
                if (alias.command.trim().isEmpty()) return;
                CommandData commandData = new CommandData(
                        alias.description,
                        alias.usages,
                        alias.aliases.toArray(new String[0])
                );

                commandRegistry.registerCommand(
                        new AliasCommand(
                                this,
                                commandData.getAlias().get(0),
                                commandData,
                                alias.command,
                                alias.permission,
                                alias.addCompletions,
                                alias.allowArgs
                        ), this);
            });
        }
    }

    private void startTasks() {
        tasks.put("player_move_task", getServer().getScheduler().runTaskTimer(this, new PlayerMoveTask(this), 20, 20));
        tasks.put("clear_player_cache", getServer().getScheduler().runTaskTimerAsynchronously(this, new ClearCacheTask(this), config.clearPlayersCache, config.clearPlayersCache));
        tasks.put("save_payer_data", getServer().getScheduler().runTaskTimerAsynchronously(this, new SavePlayerDataTask(this), config.savePlayersData, config.savePlayersData));
    }

    private void stopTasks() {
        tasks.forEach((name, task) -> task.cancel());
        tasks.clear();
    }

    private void stopDeferredCommands() {
        deferredCommands.values().forEach(task -> {
            if (!task.isCancelled()) task.cancel();
        });
        deferredCommands.clear();
    }

    private void logPluginSettings() {
        Chat.info("&eAIO listeners:");
        config.eventPriorities.forEach((event, priority) -> Chat.info("&b" + event + " &e" + priority + "&b priority."));
    }
}
