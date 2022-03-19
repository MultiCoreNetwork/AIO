package it.multicoredev.aio.api.models;

import com.google.common.base.Preconditions;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Copyright © 2022 by Daniele Patella. All rights reserved.
 * This file is part of AIO.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
public class ItemObject {
    private Material material;
    private Integer amount;
    private String name;
    private List<String> lore;
    private String nbt;

    /**
     * Create a serializable ItemStack used to be saved in the kits ore to any other json file.
     *
     * @param item the item to be converted
     */
    public ItemObject(ItemStack item) {
        this.material = item.getType();
        this.amount = item.getAmount();

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (!meta.getDisplayName().trim().isEmpty()) this.name = meta.getDisplayName();
            if (meta.getLore() != null && !meta.getLore().isEmpty()) this.lore = meta.getLore();
        }

        if (nbt != null && !nbt.trim().isEmpty() && !nbt.equals("{}")) this.nbt = new NBTItem(item).toString();
    }

    /**
     * Get the {@link Material} of the item.
     *
     * @return the material.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Set the {@link Material} of the item.
     *
     * @param material the material.
     * @return this object.
     */
    public ItemObject setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * Get the amount of items.
     *
     * @return the amount.
     */
    public Integer getAmount() {
        return amount != null ? amount : 1;
    }

    /**
     * Set the amount of items.
     *
     * @param amount the amount.
     * @return this object.
     */
    public ItemObject setAmount(Integer amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Get the item name.
     *
     * @return the name.
     */
    @Nullable
    public String getName() {
        return name;
    }

    /**
     * Set the item name.
     *
     * @param name the name.
     * @return this object.
     */
    public ItemObject setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get the item lore.
     *
     * @return the lore.
     */
    public List<String> getLore() {
        return lore;
    }

    /**
     * Set the item lore.
     *
     * @param lore the lore.
     * @return this object.
     */
    public ItemObject setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    /**
     * Add a line to the item lore.
     *
     * @param line the line to add.
     * @return this object.
     */
    public ItemObject addLore(@NotNull String line) {
        Preconditions.checkNotNull(line);

        this.lore.add(line);
        return this;
    }

    /**
     * Get the item nbt as a string.
     *
     * @return the nbt.
     */
    public String getNBT() {
        return nbt;
    }

    /**
     * Get the item nbt as a {@link NBTContainer}.
     *
     * @return the nbt as a {@link NBTContainer} or null if the nbt is invalid.
     */
    @Nullable
    public NBTContainer getNBTContainer() {
        if (nbt == null || nbt.trim().isEmpty()) return null;

        try {
            return new NBTContainer(nbt);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Set the item nbt.
     *
     * @param nbt the nbt as a string.
     * @return this object.
     */
    public ItemObject setNBT(String nbt) {
        this.nbt = nbt;
        return this;
    }

    /**
     * Get the item nbt.
     *
     * @param nbt the nbt as a {@link NBTContainer}.
     * @return this object.
     */
    public ItemObject setNBT(@NotNull NBTContainer nbt) {
        Preconditions.checkNotNull(nbt);

        this.nbt = nbt.toString();
        return this;
    }

    /**
     * Merge a nbt to the item nbt.
     *
     * @param nbt the nbt to merge.
     * @return this object.
     */
    public ItemObject mergeNBT(@NotNull String nbt) {
        Preconditions.checkNotNull(nbt);

        try {
            NBTContainer container = new NBTContainer(nbt);
            mergeNBT(container);
        } catch (Exception ignored) {
        }

        return this;
    }

    /**
     * Merge a nbt to the item nbt.
     *
     * @param nbt the nbt to merge as a {@link NBTContainer}.
     * @return this object.
     */
    public ItemObject mergeNBT(@NotNull NBTContainer nbt) {
        Preconditions.checkNotNull(nbt);

        NBTContainer container = getNBTContainer();
        if (container == null) setNBT(nbt);
        else container.mergeCompound(nbt);

        return this;
    }

    /**
     * Set a nbt to the item nbt.
     * Supported types are:
     * <ul>
     *     <li>{@link NBTCompound}</li>
     *     <li>{@link String}</li>
     *     <li>{@link Integer}</li>
     *     <li>{@link Double}</li>
     *     <li>{@link Byte}</li>
     *     <li>{@link Short}</li>
     *     <li>{@link Long}</li>
     *     <li>{@link Float}</li>
     *     <li>{@link Boolean}</li>
     *     <li>{@link UUID}</li>
     *     <li>byte[]</li>
     *     <li>int[]</li>
     *
     * @param key   the key of the nbt.
     * @param value the value of the nbt.
     * @return this object.
     */
    public ItemObject addNBT(@NotNull String key, @NotNull Object value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        Preconditions.checkArgument(key.trim().isEmpty(), "nbt key cannot be empty");
        Preconditions.checkArgument(
                value instanceof NBTCompound ||
                        value instanceof String ||
                        value instanceof Integer ||
                        value instanceof Double ||
                        value instanceof Byte ||
                        value instanceof Short ||
                        value instanceof Long ||
                        value instanceof Float ||
                        value instanceof Boolean ||
                        value instanceof UUID ||
                        value instanceof byte[] ||
                        value instanceof int[],
                "Invalid nbt type");

        NBTContainer container = getNBTContainer();
        if (container == null) container = new NBTContainer();

        if (value instanceof NBTCompound) {
            container.mergeCompound((NBTCompound) value);
        } else if (value instanceof String) {
            container.setString(key, (String) value);
        } else if (value instanceof Integer) {
            container.setInteger(key, (Integer) value);
        } else if (value instanceof Double) {
            container.setDouble(key, (Double) value);
        } else if (value instanceof Byte) {
            container.setByte(key, (Byte) value);
        } else if (value instanceof Short) {
            container.setShort(key, (Short) value);
        } else if (value instanceof Long) {
            container.setLong(key, (Long) value);
        } else if (value instanceof Float) {
            container.setFloat(key, (Float) value);
        } else if (value instanceof byte[]) {
            container.setByteArray(key, (byte[]) value);
        } else if (value instanceof int[]) {
            container.setIntArray(key, (int[]) value);
        } else if (value instanceof Boolean) {
            container.setBoolean(key, (Boolean) value);
        } else if (value instanceof UUID) {
            container.setUUID(key, (UUID) value);
        } else {
            container.setObject(key, value);
        }

        return this;
    }

    /**
     * Get the item as a {@link ItemStack}.
     *
     * @return the item as a {@link ItemStack}.
     */
    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(material, getAmount());

        if (name != null || (lore != null && !lore.isEmpty())) {
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                if (name != null) meta.setDisplayName(Chat.getTranslated(name));
                if (lore != null && !lore.isEmpty()) meta.setLore(Chat.getDiscolored(lore));

                item.setItemMeta(meta);
            }
        }

        if (nbt != null && !nbt.trim().isEmpty()) {
            NBTItem nbti = new NBTItem(item);

            try {
                NBTContainer container = new NBTContainer(nbt);
                nbti.mergeCompound(container);
                item = nbti.getItem();
            } catch (Exception ignored) {
            }
        }

        return item;
    }
}
