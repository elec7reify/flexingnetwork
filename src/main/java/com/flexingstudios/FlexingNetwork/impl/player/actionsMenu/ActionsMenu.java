package com.flexingstudios.FlexingNetwork.impl.player.actionsMenu;

import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.util.*;
import com.flexingstudios.FlexingNetwork.friends.utils.FriendsManager;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;
import com.flexingstudios.FlexingNetwork.impl.player.MysqlPlayer;
import com.flexingstudios.FlexingNetwork.impl.player.actionsMenu.subMenu.BanMenu;
import com.flexingstudios.FlexingNetwork.impl.player.actionsMenu.subMenu.KickMenu;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

import java.util.Set;

public class ActionsMenu implements InvMenu {
    private static Inventory inv;
    private final String target;

    public ActionsMenu(String target) {
        this.target = target;
        inv = Bukkit.createInventory(this, 54, "Быстрые действия — " + target);

        ItemStack GLASS_PANE_BLUE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
        ItemMeta GLASS_PANE_BLUE_META = GLASS_PANE_BLUE.getItemMeta();
        GLASS_PANE_BLUE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE_BLUE.setItemMeta(GLASS_PANE_BLUE_META);

        Set<Integer> GLASS_PANE_BLUE_SLOTS = ImmutableSet.of(0, 1, 2, 6, 7, 8);
        GLASS_PANE_BLUE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_BLUE));

        ItemStack GLASS_PANE_WHITE = new ItemStack(Material.STAINED_GLASS_PANE, 1);
        ItemMeta GLASS_PANE_WHITE_META = GLASS_PANE_WHITE.getItemMeta();
        GLASS_PANE_WHITE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE_WHITE.setItemMeta(GLASS_PANE_WHITE_META);

        Set<Integer> GLASS_PANE_WHITE_SLOTS = ImmutableSet.of(
                3, 5, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 20, 21,
                23, 24, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36,
                38, 39, 41, 42, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
        GLASS_PANE_WHITE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_WHITE));

        GameProfile profile = new GameProfile(Bukkit.getPlayer(target).getUniqueId(), target);
        inv.setItem(4, Items.name(new ItemBuilder(SkullBuilder.getSkull(profile, 1)).build(), "", ""));
        inv.setItem(19, Items.name(Material.EMERALD, "", ""));
        inv.setItem(22, Items.name(new ItemStack(new Dye(DyeColor.LIME).toItemStack(1)), "Добавить в друзья", ""));
        inv.setItem(25, Items.name(Material.SULPHUR, "ignore", ""));
        inv.setItem(37, Items.name(Material.REDSTONE, "кикнуть игрока — " + target, ""));
        inv.setItem(40, Items.name(Material.REDSTONE_BLOCK, "Забанить игрока — " + target, ""));
    }

    private static int getSlot(int index) {
        return 10 + 9 * (index / 7) + index % 7;
    }

    private static int getIndex(int slot) {
        if (slot % 9 == 0 || (slot + 1) % 9 == 0)
            return -1;

        slot -= 10;
        if (slot < 0)
            return -1;

        int row = slot / 9;

        return row * 7 + (slot - row * 9) % 7;
    }

    @Override
    public void onClick(ItemStack itemStack, Player player, int slot, ClickType clickType) {
        MysqlPlayer flPlayer = (MysqlPlayer) FLPlayer.get(player);

        switch (slot) {
            case 22:
                FriendsManager.addFriendRequest(player.getName(), target);
                Utilities.msg(player, "&2Друзья » §aИгроку " + target + " &aотправлена заявка в друзья.");
                break;
            case 25:
                if (flPlayer.ignored.contains(target)) {
                    flPlayer.ignored.add(target);
                    player.closeInventory();
                    Utilities.msg(player, T.success("GOT IT!", "Теперь вы игнорируете игрока " + target));
                } else {
                    flPlayer.ignored.remove(target);
                    player.closeInventory();
                    Utilities.msg(player, T.success("GOT IT!", "Вы больше не игнорируете игрока " + target));
                }
                break;
            case 37:
                if (FlexingNetwork.hasRank(flPlayer.getBukkitPlayer(), Rank.CHIKIBAMBONYLA, true))
                    player.openInventory(new KickMenu(target).getInventory());
                break;
            case 40:
                if (FlexingNetwork.hasRank(flPlayer.getBukkitPlayer(), Rank.SPONSOR, true))
                    player.openInventory(new BanMenu(target).getInventory());
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}