package com.holyicey.TconBota.handlers;

import com.holyicey.TconBota.init.EnchantmentInit;
import com.holyicey.TconBota.TconBota;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.Collection;

/**
 * 阅历附魔逻辑处理器
 * <p>
 * 处理 LivingDropsEvent 事件，实现阅历附魔的核心逻辑：
 * 统计掉落物 -> 清除掉落物 -> 给予经验
 */
public class KnowledgeHandler {

    /**
     * 监听实体掉落事件
     * 使用 LOWest 优先级，确保我们在其他模组添加完掉落物之后再执行，
     * 以便统计到尽可能多的掉落物（包括抢夺附魔产生的额外掉落）。
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDrops(LivingDropsEvent event) {
        // 1. 验证被击杀实体不是玩家
        if (event.getEntity() instanceof Player) {
            return;
        }

        // 2. 验证攻击源存在且是玩家
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof Player)) {
            return;
        }
        Player player = (Player) attacker;

        // 3. 检查玩家是否持有阅历附魔
        // getEnchantmentLevel 会自动检查主手等有效槽位
        int level = EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.KNOWLEDGE.get(), player);
        if (level <= 0) {
            return;
        }

        // 4. 获取掉落物列表
        Collection<ItemEntity> drops = event.getDrops();
        if (drops == null || drops.isEmpty()) {
            return;
        }

        // 5. 统计所有物品总数量
        int totalItemCount = 0;
        for (ItemEntity drop : drops) {
            if (drop != null && !drop.getItem().isEmpty()) {
                totalItemCount += drop.getItem().getCount();
            }
        }

        // 如果没有有效物品，直接返回
        if (totalItemCount == 0) {
            return;
        }

        // 6. 清空所有掉落物
        // 直接清空集合，阻止掉落物实体生成，减少服务器负载
        drops.clear();

        // 7. 计算经验值
        // 公式：物品总数 × 附魔等级 × 25
        int xpToGive = totalItemCount * level * 25;

        // 8. 给予玩家经验
        player.giveExperiencePoints(xpToGive);

        // 9. 播放音效反馈 (可选，增加游戏体验)
        // 使用经验球拾取音效，音调随机略微变化
        player.level().playSound(
                null, 
                player.getX(), 
                player.getY(), 
                player.getZ(), 
                SoundEvents.EXPERIENCE_ORB_PICKUP, 
                SoundSource.PLAYERS, 
                0.1F, 
                0.5F * ((player.level().random.nextFloat() - player.level().random.nextFloat()) * 0.7F + 1.8F)
        );
        
        // 调试日志 (可选，开发阶段使用，发布时可移除或设为 debug)
        // TconBota.LOGGER.debug("Knowledge Enchantment: Converted {} items to {} XP for {}", totalItemCount, xpToGive, player.getName().getString());
    }
}
