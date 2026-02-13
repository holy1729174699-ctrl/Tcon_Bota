package com.holyicey.TconBota.handlers;

import com.holyicey.TconBota.TconBota;
import com.holyicey.TconBota.enchantments.ScavengerEnchantment;
import com.holyicey.TconBota.init.EnchantmentInit;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 拾荒者附魔逻辑处理器
 */
public class ScavengerHandler {

    /**
     * 监听实体掉落事件
     */
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onLivingDrops(LivingDropsEvent event) {
        // 1. 验证攻击源存在且是玩家
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof Player)) {
            return;
        }
        Player player = (Player) attacker;
        
        // 验证客户端/服务端
        if (player.level().isClientSide) {
            return;
        }

        // 2. 检查玩家是否持有拾荒者附魔
        int level = EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.SCAVENGER.get(), player);
        if (level <= 0) {
            return;
        }

        // 3. 计算触发概率
        // 概率 = 等级 * 2.5%
        // 例如：3级 = 7.5%
        if (player.level().random.nextInt(1000) < level * 25) { // 使用1000分位来处理小数 (2.5% = 25/1000)
            LivingEntity target = event.getEntity();
            
            try {
                // 4. 捕获掉落物
                // 开启捕获模式，传入一个新的列表来接收掉落物
                // 注意：Forge 的 captureDrops 可能会返回 null 或旧的列表，我们需要小心处理
                Collection<ItemEntity> capturedDrops = new ArrayList<>();
                Collection<ItemEntity> oldDrops = target.captureDrops(capturedDrops);
                
                // 5. 执行掉落逻辑 (通过反射调用 dropFromLootTable)
                ScavengerEnchantment enchantment = (ScavengerEnchantment) EnchantmentInit.SCAVENGER.get();
                enchantment.executeDrop(target, event.getSource());
                
                // 6. 恢复旧的捕获状态并获取捕获到的物品
                Collection<ItemEntity> newDrops = target.captureDrops(oldDrops);
                
                // 7. 将新捕获的物品添加到事件的掉落列表中
                if (newDrops != null) {
                    event.getDrops().addAll(newDrops);
                    // 也可以选择将 capturedDrops 添加进去，取决于 captureDrops 的具体实现行为
                    // 通常 newDrops 就是我们传入的那个 ArrayList (如果它被使用了)
                    // 或者 target.captureDrops(null) 返回的就是我们之前传入的列表
                }
                
                // 为了保险起见，如果 newDrops 是 null 或者空的，我们检查一下 capturedDrops
                // 因为有些实现可能会直接向传入的集合添加元素
                if ((newDrops == null || newDrops.isEmpty()) && !capturedDrops.isEmpty()) {
                     event.getDrops().addAll(capturedDrops);
                }
                
                // 调试日志
                // TconBota.LOGGER.debug("Scavenger activated! Added {} items.", capturedDrops.size());
                
            } catch (Throwable e) {
                TconBota.LOGGER.error("Failed to execute Scavenger enchantment logic", e);
            }
        }
    }
}
