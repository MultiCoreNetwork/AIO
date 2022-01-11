package it.multicoredev.aio;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.multicoredev.aio.api.Module;
import it.multicoredev.aio.api.*;
import it.multicoredev.aio.api.listeners.IListenerRegistry;
import it.multicoredev.aio.api.listeners.ListenerCompound;
import it.multicoredev.aio.api.tp.ITeleportManager;
import it.multicoredev.aio.commands.AIOCommand;
import it.multicoredev.aio.commands.AliasCommand;
import it.multicoredev.aio.commands.economy.EconomyCommand;
import it.multicoredev.aio.commands.player.*;
import it.multicoredev.aio.commands.player.kits.KitCommand;
import it.multicoredev.aio.commands.player.kits.KitsCommand;
import it.multicoredev.aio.commands.staff.CleanChatCommand;
import it.multicoredev.aio.commands.teleport.BackCommand;
import it.multicoredev.aio.commands.teleport.RTPCommand;
import it.multicoredev.aio.commands.teleport.TpallCommand;
import it.multicoredev.aio.commands.teleport.home.*;
import it.multicoredev.aio.commands.teleport.spawn.SetSpawnCommand;
import it.multicoredev.aio.commands.teleport.spawn.SpawnCommand;
import it.multicoredev.aio.commands.teleport.warp.DelWarpCommand;
import it.multicoredev.aio.commands.teleport.warp.SetWarpCommand;
import it.multicoredev.aio.commands.teleport.warp.WarpCommand;
import it.multicoredev.aio.commands.teleport.warp.WarpsCommand;
import it.multicoredev.aio.commands.utilities.*;
import it.multicoredev.aio.listeners.aio.PlayerPostTeleportListener;
import it.multicoredev.aio.listeners.aio.PlayerTeleportCancelledListener;
import it.multicoredev.aio.listeners.entity.EntityDamageListener;
import it.multicoredev.aio.listeners.player.*;
import it.multicoredev.aio.storage.config.Config;
import it.multicoredev.aio.storage.config.Localization;
import it.multicoredev.aio.storage.config.ModuleManager;
import it.multicoredev.aio.storage.config.adapters.LocationAdapter;
import it.multicoredev.aio.storage.config.modules.CommandAliasesModule;
import it.multicoredev.aio.storage.config.modules.SpawnModule;
import it.multicoredev.aio.storage.config.sections.StorageSection;
import it.multicoredev.aio.storage.data.FileStorage;
import it.multicoredev.aio.storage.data.KitStorage;
import it.multicoredev.aio.storage.data.WarpStorage;
import it.multicoredev.aio.tasks.ClearCacheTask;
import it.multicoredev.aio.tasks.SavePlayerDataTask;
import it.multicoredev.aio.utils.ReflectionUtils;
import it.multicoredev.mbcore.spigot.Chat;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright © 2021 - 2022 by Lorenzo Magni & Daniele Patella
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
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .registerTypeAdapter(Location.class, new LocationAdapter())
            .create();

    private static final Map<Module, File> modules = new HashMap<>();
    public static boolean VAULT;
    public static boolean LUCKPERMS;
    public static boolean PAPI;

    private final File root = getDataFolder();
    private final File modulesDir = new File(root, "modules");
    private final File configFile = new File(root, "config.yml");
    private final File localizationFile = new File(root, "localization.yml");
    private final File userMapFile = new File(root, "usermap.json");
    private final File kitsFile = new File(root, "kits.json");
    private final File warpsFile = new File(root, "warps.json");

    private Config config;
    private Localization localization;
    private ModuleManager moduleManager;
    private IStorage storage;
    private Map<String, UUID> usermap;
    private KitStorage kitStorage;
    private WarpStorage warpStorage;
    private final Map<UUID, User> usersCache = new HashMap<>();

    private final Map<String, BukkitTask> tasks = new HashMap<>();

    private CommandRegistry commandRegistry;
    private ListenerRegistry listenerRegistry;
    private TeleportManager tpManager;
    private Map<UUID, Map<String, Date>> commandsCooldown;
    private AIOEconomy economy;

    public static Permission vaultPerms;
    public static LuckPerms lp;

    public static boolean debug = true;

    //TODO Improve argument parsing and completions
    //TODO Enchant disenchant commands change output msg enchant name
    //TODO Fly speed (Save also walk speed and set on Join)
    //TODO Add to commands like heal or feed... the ability to user selectors
    //TODO Add error to preconditions

    @Override
    public void onEnable() {
        moduleManager = new ModuleManager(this);

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
        tpManager = new TeleportManager();

        if (config.commandsCooldown.cooldownEnabled) commandsCooldown = new HashMap<>();

        initDependencies();

        if (VAULT) {
            economy = new AIOEconomy(this);
            getServer().getServicesManager().register(IEconomy.class, economy, this, ServicePriority.Highest);
        }

        registerListeners();
        registerCommands();

        startTasks();

        if (debug) logPluginSettings();

        Chat.info("&eAIO &2enabled&e.");
    }

    @Override
    public void onDisable() {
        stopTasks();

        modules.clear();
        usersCache.clear();
        tasks.clear();

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

    public KitStorage getKitStorage() {
        return kitStorage;
    }

    public WarpStorage getWarpStorage() {
        return warpStorage;
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

    public Config getConfiguration() {
        return config;
    }

    public Localization getLocalization() {
        return localization;
    }

    public static <T> T deserialize(File file, Type type) throws Exception {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, type);
        }
    }

    public static synchronized void serialize(File file, Object obj) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(gson.toJson(obj));
            writer.flush();
        }
    }

//    public void addCommandCooldown(Player player, String command) {
//        if (!config.commandsCooldown.hasCommandCooldown(command)) return;
//        if (player.hasPermission("aio.no-commands-cooldown")) return;
//
//        UUID uuid = player.getUniqueId();
//        if (commandsCooldown.containsKey(uuid)) {
//            Map<String, Date> commands = commandsCooldown.get(uuid);
//            commands.put(command, new Date());
//        } else {
//            Map<String, Date> commands = new HashMap<>();
//            commands.put(command, new Date());
//            commandsCooldown.put(uuid, commands);
//        }
//    }

//    public boolean hasCommandCooldown(Player player, String command) {
//        if (!config.commandsCooldown.cooldownEnabled) return false;
//        UUID uuid = player.getUniqueId();
//
//        if (!commandsCooldown.containsKey(uuid)) return false;
//
//        Map<String, Date> commands = commandsCooldown.get(uuid);
//        if (!commands.containsKey(command)) return false;
//
//        Date date = commands.get(command);
//        int difference = (int) ((new Date().getTime() - date.getTime()) / 1000);
//        int cooldown = config.commandsCooldown.getCommandCooldown(command);
//
//        if (difference > cooldown) {
//            commands.remove(command);
//            return false;
//        }
//
//        Chat.send(localization.commandCooldown.replace("{TIME}", String.valueOf(cooldown - difference)), player);
//        return true;
//    }

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

            try {
                serialize(configFile, config);
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();
                return false;
            }
        } else {
            try {
                config = deserialize(configFile, Config.class);
                if (config == null) throw new NullPointerException("Config is null");
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();

                if (!backupFile(configFile)) return false;
                Chat.warning("&4Config file is corrupted, creating new one");

                config = new Config();

                try {
                    serialize(configFile, config);
                } catch (Exception e1) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e1.printStackTrace();
                    return false;
                }
            }

            if (config.completeMissing()) {
                try {
                    serialize(configFile, config);
                } catch (Exception e) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e.printStackTrace();
                    return false;
                }
            }
        }

        if (!localizationFile.exists() || !localizationFile.isFile()) {
            localization = new Localization();

            try {
                serialize(localizationFile, localization);
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();
                return false;
            }
        } else {
            try {
                localization = deserialize(localizationFile, Localization.class);
                if (localization == null) throw new NullPointerException("Localization is null");
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();

                if (!backupFile(localizationFile)) return false;
                Chat.warning("&4Localization file is corrupted, creating new one");

                localization = new Localization();

                try {
                    serialize(localizationFile, localization);
                } catch (Exception e1) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e1.printStackTrace();
                    return false;
                }
            }

            if (localization.completeMissing()) {
                try {
                    serialize(localizationFile, localization);
                } catch (Exception e) {
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

        for (Map.Entry<Class<? extends Module>, String> entry : ModuleManager.DEF_MODULES.entrySet()) {
            String name = entry.getValue();
            Class<? extends Module> clazz = entry.getKey();
            File moduleFile = new File(modulesDir, name + ".json");

            if (!moduleFile.exists() || !moduleFile.isFile()) {
                Module module;

                try {
                    module = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e.printStackTrace();
                    return false;
                }

                try {
                    serialize(moduleFile, module);
                } catch (Exception e) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e.printStackTrace();
                    return false;
                }

                if (config.modules.get(module.getName())) moduleManager.registerModule(module);
            } else {
                Module module;

                try {
                    module = deserialize(moduleFile, clazz);
                    if (module == null) throw new NullPointerException("Module is null");
                } catch (Exception e) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e.printStackTrace();

                    if (!backupFile(moduleFile)) return false;
                    Chat.warning("&4Module " + name + " file is corrupted, creating new one");

                    try {
                        module = clazz.newInstance();
                    } catch (InstantiationException | IllegalAccessException e1) {
                        Chat.severe("&4" + e1.getMessage());
                        if (debug) e1.printStackTrace();
                        return false;
                    }

                    try {
                        serialize(moduleFile, module);
                    } catch (Exception e1) {
                        Chat.severe("&4" + e1.getMessage());
                        if (debug) e1.printStackTrace();
                        return false;
                    }
                }

                if (module.completeMissing()) {
                    try {
                        serialize(moduleFile, module);
                    } catch (Exception e) {
                        Chat.severe("&4" + e.getMessage());
                        if (debug) e.printStackTrace();
                        return false;
                    }
                }

                if (config.modules.get(module.getName())) moduleManager.registerModule(module);
            }
        }

        if (!userMapFile.exists() || !userMapFile.isFile()) {
            usermap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

            try {
                serialize(userMapFile, usermap);
            } catch (Exception e) {
                Chat.severe("&cCannot save usermap.json");
                Chat.severe("&4" + e.getMessage());
                return false;
            }
        } else {
            try {
                usermap = deserialize(userMapFile, HashMap.class);
                if (usermap == null) throw new NullPointerException("Usermap is null");
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();

                if (!backupFile(userMapFile)) return false;
                Chat.warning("&4Usermap file is corrupted, creating new one");

                usermap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

                try {
                    serialize(userMapFile, usermap);
                } catch (Exception e1) {
                    Chat.severe("&cCannot save usermap.json");
                    if (debug) e1.printStackTrace();
                    return false;
                }
            }
        }

        if (!warpsFile.exists() || !warpsFile.isFile()) {
            warpStorage = new WarpStorage(this, warpsFile);

            try {
                serialize(warpsFile, warpStorage);
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();
                return false;
            }
        } else {
            try {
                warpStorage = deserialize(warpsFile, WarpStorage.class);
                if (warpStorage == null) throw new NullPointerException("WarpStorage is null");
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();

                if (!backupFile(warpsFile)) return false;
                Chat.warning("&4WarpStorage file is corrupted, creating new one");

                warpStorage = new WarpStorage(this, warpsFile);

                try {
                    serialize(warpsFile, warpStorage);
                } catch (Exception e1) {
                    Chat.severe("&4" + e1.getMessage());
                    if (debug) e1.printStackTrace();
                    return false;
                }
            }

            if (warpStorage.completeMissing()) {
                try {
                    serialize(warpsFile, warpStorage);
                } catch (Exception e) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e.printStackTrace();
                    return false;
                }
            }
        }

        if (!kitsFile.exists() || !kitsFile.isFile()) {
            kitStorage = new KitStorage(this, kitsFile);

            try {
                serialize(kitsFile, kitStorage);
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();
                return false;
            }
        } else {
            try {
                kitStorage = deserialize(kitsFile, KitStorage.class);
                if (kitStorage == null) throw new NullPointerException(("KitStorage is null"));
            } catch (Exception e) {
                Chat.severe("&4" + e.getMessage());
                if (debug) e.printStackTrace();

                if (!backupFile(kitsFile)) return false;
                Chat.warning("&4KitStorage file is corrupted, creating new one");

                kitStorage = new KitStorage(this, kitsFile);

                try {
                    serialize(kitsFile, kitStorage);
                } catch (Exception e1) {
                    Chat.severe("&4" + e1.getMessage());
                    if (debug) e1.printStackTrace();
                    return false;
                }
            }

            if (kitStorage.completeMissing()) {
                try {
                    serialize(kitsFile, kitStorage);
                } catch (Exception e) {
                    Chat.severe("&4" + e.getMessage());
                    if (debug) e.printStackTrace();
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

        if (VAULT) {
            RegisteredServiceProvider<Permission> serviceProvider = getServer().getServicesManager().getRegistration(Permission.class);
            if (serviceProvider != null) vaultPerms = serviceProvider.getProvider();
            if (vaultPerms != null) Chat.info("&aEstablished hook in Vault Permissions.");
            else Chat.info("&eCannot establish hook in Vault Permissions.");
        }

        if (LUCKPERMS) {
            RegisteredServiceProvider<LuckPerms> serviceProvider = getServer().getServicesManager().getRegistration(LuckPerms.class);
            if (serviceProvider != null) lp = serviceProvider.getProvider();
            if (lp != null) Chat.info("&aEstablished hook in LuckPerms.");
            else Chat.info("&eCannot establish hook in LuckPerms.");
        }
    }

    private void registerListeners() {
        listenerRegistry.registerListener(new ListenerCompound<>(new PlayerPostTeleportListener(this)), config.getEventPriority("PlayerPostTeleportEvent"), this);
        listenerRegistry.registerListener(new ListenerCompound<>(new PlayerTeleportCancelledListener(this)), config.getEventPriority("PlayerTeleportCancelledEvent"), this);

        listenerRegistry.registerListener(new ListenerCompound<>(new EntityDamageListener(this)), config.getEventPriority("EntityDamageEvent"), this);

        listenerRegistry.registerListener(new ListenerCompound<>(new AsyncPlayerChatListener(this)), config.getEventPriority("AsyncPlayerChatEvent"), this);
        listenerRegistry.registerListener(new ListenerCompound<>(new PlayerCommandPreprocessListener(this)), config.getEventPriority("PlayerCommandPreprocessEvent"), this);
        listenerRegistry.registerListener(new ListenerCompound<>(new PlayerDeathListener(this)), config.getEventPriority("PlayerDeathEvent"), this);
        listenerRegistry.registerListener(new ListenerCompound<>(new PlayerJoinListener(this)), config.getEventPriority("PlayerJoinEvent"), this);
        listenerRegistry.registerListener(new ListenerCompound<>(new PlayerQuitListener(this)), config.getEventPriority("PlayerQuitEvent"), this);
        listenerRegistry.registerListener(new ListenerCompound<>(new PlayerRespawnListener(this)), config.getEventPriority("PlayerRespawnEvent"), this);
        listenerRegistry.registerListener(new ListenerCompound<>(new PlayerTeleportListener(this)), config.getEventPriority("PlayerTeleportEvent"), this);
    }

    private void registerCommands() {
        commandRegistry.registerCommand(new AIOCommand(this), this);

        if (config.getCommandData("back").enabled)
            commandRegistry.registerCommand(new BackCommand(this), this);
        if (config.getCommandData("cleanchat").enabled)
            commandRegistry.registerCommand(new CleanChatCommand(this), this);
        if (config.getCommandData("day").enabled)
            commandRegistry.registerCommand(new DayCommand(this), this);
        if (config.getCommandData("delhome").enabled)
            commandRegistry.registerCommand(new DelHomeCommand(this), this);
        if (config.getCommandData("delwarp").enabled)
            commandRegistry.registerCommand(new DelWarpCommand(this), this);
        if (config.getCommandData("disenchant").enabled)
            commandRegistry.registerCommand(new DisenchantCommand(this), this);
        if (config.getCommandData("economy").enabled && VAULT)
            commandRegistry.registerCommand(new EconomyCommand(this), this);
        if (config.getCommandData("enchant").enabled)
            commandRegistry.registerCommand(new EnchantCommand(this), this);
        if (config.getCommandData("feed").enabled)
            commandRegistry.registerCommand(new FeedCommand(this), this);
        if (config.getCommandData("fly").enabled)
            commandRegistry.registerCommand(new FlyCommand(this), this);
        if (config.getCommandData("gamemode").enabled)
            commandRegistry.registerCommand(new GamemodeCommand(this), this);
        if (config.getCommandData("god").enabled)
            commandRegistry.registerCommand(new GodCommand(this), this);
        if (config.getCommandData("hat").enabled)
            commandRegistry.registerCommand(new HatCommand(this), this);
        if (config.getCommandData("heal").enabled)
            commandRegistry.registerCommand(new HealCommand(this), this);
        if (config.getCommandData("helpbook").enabled)
            commandRegistry.registerCommand(new HelpBookCommand(this), this);
        if (config.getCommandData("home").enabled)
            commandRegistry.registerCommand(new HomeCommand(this), this);
        if (config.getCommandData("homes").enabled)
            commandRegistry.registerCommand(new HomesCommand(this), this);
        if (config.getCommandData("kit").enabled)
            commandRegistry.registerCommand(new KitCommand(this), this);
        if (config.getCommandData("kits").enabled)
            commandRegistry.registerCommand(new KitsCommand(this), this);
        if (config.getCommandData("lightning").enabled)
            commandRegistry.registerCommand(new LightningCommand(this), this);
        if (config.getCommandData("nickname").enabled)
            commandRegistry.registerCommand(new NicknameCommand(this), this);
        if (config.getCommandData("night").enabled)
            commandRegistry.registerCommand(new NightCommand(this), this);
        if (config.getCommandData("playerhome").enabled)
            commandRegistry.registerCommand(new PlayerHomeCommand(this), this);
        if (config.getCommandData("rain").enabled)
            commandRegistry.registerCommand(new RainCommand(this), this);
        if (config.getCommandData("repair").enabled)
            commandRegistry.registerCommand(new RepairCommand(this), this);
        if (config.getCommandData("rtp").enabled)
            commandRegistry.registerCommand(new RTPCommand(this), this);
        if (config.getCommandData("runlater").enabled)
            commandRegistry.registerCommand(new RunLaterCommand(this), this);
        if (config.getCommandData("sethome").enabled)
            commandRegistry.registerCommand(new SetHomeCommand(this), this);
        if (config.getCommandData("setspawn").enabled && moduleManager.isModuleEnabled(SpawnModule.class))
            commandRegistry.registerCommand(new SetSpawnCommand(this), this);
        if (config.getCommandData("setwarp").enabled)
            commandRegistry.registerCommand(new SetWarpCommand(this), this);
        if (config.getCommandData("spawn").enabled && moduleManager.isModuleEnabled(SpawnModule.class))
            commandRegistry.registerCommand(new SpawnCommand(this), this);
        if (config.getCommandData("speed").enabled)
            commandRegistry.registerCommand(new SpeedCommand(this), this);
        if (config.getCommandData("sudo").enabled)
            commandRegistry.registerCommand(new SudoCommand(this), this);
        if (config.getCommandData("sun").enabled)
            commandRegistry.registerCommand(new SunCommand(this), this);
        if (config.getCommandData("thunder").enabled)
            commandRegistry.registerCommand(new ThunderCommand(this), this);
        if (config.getCommandData("tpall").enabled)
            commandRegistry.registerCommand(new TpallCommand(this), this);
        if (config.getCommandData("trash").enabled)
            commandRegistry.registerCommand(new TrashCommand(this), this);
        if (config.getCommandData("warp").enabled)
            commandRegistry.registerCommand(new WarpCommand(this), this);
        if (config.getCommandData("warps").enabled)
            commandRegistry.registerCommand(new WarpsCommand(this), this);

        if (moduleManager.isModuleEnabled(CommandAliasesModule.class)) {
            CommandAliasesModule aliasesModule = moduleManager.getModule(CommandAliasesModule.class);
            if (aliasesModule == null) return;

            aliasesModule.aliases.forEach(alias -> {
                if (alias.aliases.isEmpty()) return;
                if (alias.command.trim().isEmpty()) return;
                commandRegistry.registerCommand(new AliasCommand(
                        this,
                        alias.aliases.stream().map(a -> a.toLowerCase(Locale.ROOT)).collect(Collectors.toList()),
                        alias.command.toLowerCase(Locale.ROOT),
                        alias.permission != null ? alias.permission.toLowerCase(Locale.ROOT) : null,
                        alias.description,
                        alias.usage,
                        alias.addCompletions), this);
            });
        }
    }

    private void startTasks() {
        tasks.put("clear_player_cache", getServer().getScheduler().runTaskTimerAsynchronously(this, new ClearCacheTask(this), config.clearPlayersCache, config.clearPlayersCache));
        tasks.put("save_payer_data", getServer().getScheduler().runTaskTimerAsynchronously(this, new SavePlayerDataTask(this), config.savePlayersData, config.savePlayersData));
    }

    private void stopTasks() {
        tasks.forEach((name, task) -> task.cancel());
    }

    private void logPluginSettings() {
        Chat.info("&eAIO modules:");
        config.modules.forEach((module, enabled) -> Chat.info("&b" + module + (enabled ? " &2enabled" : " &4disabled") + "&b."));

        Chat.info("&eAIO listeners:");
        config.eventPriorities.forEach((event, priority) -> Chat.info("&b" + event + "&e" + priority + "&b."));

        Chat.info("&eAIO commands:");
        commandRegistry.getRegisteredCommands().forEach(command -> Chat.info("&b" + command.getName() + " aliases &e" + Arrays.toString(command.getAliases().toArray()) + "&b."));
    }
}
