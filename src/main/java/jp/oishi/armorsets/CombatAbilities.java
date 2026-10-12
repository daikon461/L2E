package jp.oishi.armorsets;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.*;

/** Runtime capstones for complete armor sets. Server-side only. */
@EventBusSubscriber(modid = ArmorSetsMod.MODID)
public final class CombatAbilities {
    private static final List<EquipmentSlot> ARMOR = List.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
    private static final Map<UUID, Map<String, Long>> COOLDOWNS = new HashMap<>();

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Post event) {
        if (event.getEntity().level().isClientSide()) return;

        Entity source = event.getSource().getEntity();
        if (source instanceof Player attacker) {
            SetRegistry.ArmorSet set = fullSet(attacker);
            if (set != null) onSuccessfulHit(attacker, set, event.getEntity().isDeadOrDying());
        }

        if (event.getEntity() instanceof Player victim) {
            SetRegistry.ArmorSet set = fullSet(victim);
            if (set != null) onDamaged(victim, set);
        }
    }

    @SubscribeEvent
    public static void onTick(PlayerTickEvent.Post event) {
        Player p = event.getEntity();
        if (p.level().isClientSide() || p.tickCount % 10 != 0) return;
        SetRegistry.ArmorSet set = fullSet(p);
        if (set == null || isVanilla(set)) return;

        // Emergency capstone: only modded sets get this. 25% HP threshold, long cooldown.
        if (p.getHealth() <= p.getMaxHealth() * 0.25f && ready(p, "crisis/" + set.key(), 20L * (42 - Math.min(12, set.tier() * 4)))) {
            int t = set.tier();
            p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100 + t * 20, Math.min(2, t), false, true, true));
            p.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80 + t * 20, Math.min(2, Math.max(0, t - 1)), false, true, true));
            if (isMagic(set)) p.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 120 + t * 20, Math.min(3, t), false, true, true));
        }
    }

    private static void onSuccessfulHit(Player p, SetRegistry.ArmorSet set, boolean killed) {
        if (isVanilla(set)) return; // vanilla capstones remain intentionally modest/stat-only
        if (dedicatedHit(p, set, killed)) return;
        int t = set.tier();
        String school = set.school();

        if (isPhysical(set)) {
            if (ready(p, "hit/" + set.key(), 20L * Math.max(3, 7 - t))) {
                p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60 + t * 20, Math.min(3, t), false, true, true));
                if (set.key().contains("cursium") || set.key().contains("end"))
                    p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60 + t * 20, Math.min(2, t - 1), false, true, true));
            }
        } else {
            // Spell hits also resolve through the normal damage pipeline, so this works without hard-linking spell classes.
            if (ready(p, "spellhit/" + set.key(), 20L * Math.max(4, 9 - t))) {
                if (school.equals("fire") || school.equals("lightning") || school.equals("evocation"))
                    p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 80 + t * 20, Math.min(2, t), false, true, true));
                if (school.equals("ice") || school.equals("holy") || school.equals("nature"))
                    p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 80 + t * 20, Math.min(2, t - 1), false, true, true));
                if (school.equals("ender") || school.equals("eldritch"))
                    p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80 + t * 20, Math.min(2, t), false, true, true));
                if (school.equals("blood"))
                    p.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60 + t * 20, Math.min(2, t - 1), false, true, true));
                if (school.equals("generic"))
                    p.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 80 + t * 20, Math.min(2, t), false, true, true));
            }
        }

        if (killed && ready(p, "kill/" + set.key(), 20L * Math.max(6, 12 - t))) {
            p.heal(Math.min(p.getMaxHealth() * (0.04f + 0.02f * t), 12.0f));
            if (isMagic(set)) p.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100 + t * 20, Math.min(2, t), false, true, true));
        }
    }

    private static void onDamaged(Player p, SetRegistry.ArmorSet set) {
        if (isVanilla(set)) return;
        if (dedicatedHurt(p, set)) return;
        int t = set.tier();
        if (!ready(p, "hurt/" + set.key(), 20L * Math.max(7, 14 - t))) return;
        if (isPhysical(set)) {
            p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 50 + t * 20, Math.min(2, t - 1), false, true, true));
        } else {
            p.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 60 + t * 20, Math.min(2, t), false, true, true));
        }
    }

    /** Dedicated 4/4 capstones. Returning true suppresses the generic fallback for that trigger. */
    private static boolean dedicatedHit(Player p, SetRegistry.ArmorSet set, boolean killed) {
        String k = set.key();
        switch (k) {
            case "cataclysm:ignitium" -> {
                if (!ready(p, "dedicated/hit/" + k, 80)) return true;
                buff(p, MobEffects.DAMAGE_BOOST, 140, 3); buff(p, MobEffects.FIRE_RESISTANCE, 180, 0);
                fx(p, "煉獄の覇気", ParticleTypes.FLAME, SoundEvents.BLAZE_SHOOT); return true;
            }
            case "cataclysm:cursium" -> {
                if (!ready(p, "dedicated/hit/" + k, 90)) return true;
                buff(p, MobEffects.MOVEMENT_SPEED, 140, 2); buff(p, MobEffects.DAMAGE_BOOST, 120, 2);
                fx(p, "呪鋼の追撃", ParticleTypes.SOUL_FIRE_FLAME, SoundEvents.WITHER_SHOOT); return true;
            }
            case "gobber2:gobber_nether" -> {
                if (!ready(p, "dedicated/hit/" + k, 100)) return true;
                buff(p, MobEffects.DAMAGE_BOOST, 160, 2); buff(p, MobEffects.FIRE_RESISTANCE, 200, 0);
                fx(p, "ネザー・オーバードライブ", ParticleTypes.FLAME, SoundEvents.BLAZE_SHOOT); return true;
            }
            case "gobber2:gobber_end" -> {
                if (!ready(p, "dedicated/hit/" + k, 90)) return true;
                buff(p, MobEffects.MOVEMENT_SPEED, 160, 2); buff(p, MobEffects.ABSORPTION, 140, 2);
                fx(p, "エンド・フェイズ", ParticleTypes.PORTAL, SoundEvents.ENDERMAN_TELEPORT); return true;
            }
            case "gobber2:gobber_dragon" -> {
                if (!ready(p, "dedicated/hit/" + k, 80)) return true;
                buff(p, MobEffects.DAMAGE_BOOST, 180, 3); buff(p, MobEffects.DAMAGE_RESISTANCE, 140, 2); buff(p, MobEffects.MOVEMENT_SPEED, 140, 1);
                if (killed) p.heal(Math.min(14.0f, p.getMaxHealth() * 0.10f));
                fx(p, "竜王の猛攻", ParticleTypes.DRAGON_BREATH, SoundEvents.ENDER_DRAGON_GROWL); return true;
            }
            case "irons_spellbooks:pyromancer", "cataclysm_spellbooks:ignis" -> {
                if (!ready(p, "dedicated/hit/" + k, 100)) return true;
                buff(p, MobEffects.DAMAGE_BOOST, 140, 2); buff(p, MobEffects.FIRE_RESISTANCE, 180, 0);
                fx(p, "煉獄共鳴", ParticleTypes.FLAME, SoundEvents.FIRECHARGE_USE); return true;
            }
            case "irons_spellbooks:cryomancer" -> {
                if (!ready(p, "dedicated/hit/" + k, 100)) return true;
                buff(p, MobEffects.DAMAGE_RESISTANCE, 160, 2); buff(p, MobEffects.MOVEMENT_SPEED, 120, 1);
                fx(p, "永久凍土", ParticleTypes.SNOWFLAKE, SoundEvents.GLASS_BREAK); return true;
            }
            case "irons_spellbooks:electromancer" -> {
                if (!ready(p, "dedicated/hit/" + k, 80)) return true;
                buff(p, MobEffects.MOVEMENT_SPEED, 140, 3); buff(p, MobEffects.DAMAGE_BOOST, 100, 1);
                fx(p, "雷霆疾駆", ParticleTypes.ELECTRIC_SPARK, SoundEvents.LIGHTNING_BOLT_THUNDER); return true;
            }
            case "irons_spellbooks:cultist" -> {
                if (!ready(p, "dedicated/hit/" + k, 100)) return true;
                p.heal(Math.min(8.0f, p.getMaxHealth() * 0.06f)); buff(p, MobEffects.DAMAGE_BOOST, 120, 2);
                fx(p, "血の盟約", ParticleTypes.DAMAGE_INDICATOR, SoundEvents.WITHER_AMBIENT); return true;
            }
            case "irons_spellbooks:shadowwalker" -> {
                if (!ready(p, "dedicated/hit/" + k, 80)) return true;
                buff(p, MobEffects.MOVEMENT_SPEED, 160, 3); buff(p, MobEffects.INVISIBILITY, 50, 0);
                fx(p, "虚空歩行", ParticleTypes.PORTAL, SoundEvents.ENDERMAN_TELEPORT); return true;
            }
            case "irons_spellbooks:priest" -> {
                if (!ready(p, "dedicated/hit/" + k, 120)) return true;
                buff(p, MobEffects.REGENERATION, 140, 2); buff(p, MobEffects.ABSORPTION, 160, 2);
                fx(p, "聖域", ParticleTypes.END_ROD, SoundEvents.PLAYER_LEVELUP); return true;
            }
            case "irons_spellbooks:archevoker", "cataclysm_spellbooks:engineer" -> {
                if (!ready(p, "dedicated/hit/" + k, 90)) return true;
                buff(p, MobEffects.DAMAGE_BOOST, 120, 2); buff(p, MobEffects.ABSORPTION, 120, 1);
                fx(p, "術式過負荷", ParticleTypes.ENCHANT, SoundEvents.EVOKER_CAST_SPELL); return true;
            }
            case "irons_spellbooks:netherite_mage" -> {
                if (!ready(p, "dedicated/hit/" + k, 90)) return true;
                buff(p, MobEffects.DAMAGE_BOOST, 140, 2); buff(p, MobEffects.DAMAGE_RESISTANCE, 140, 2);
                fx(p, "魔導重装", ParticleTypes.ENCHANT, SoundEvents.ANVIL_LAND); return true;
            }
            case "irons_spellbooks:wizard", "irons_spellbooks:wandering_magician" -> {
                if (!ready(p, "dedicated/hit/" + k, 100)) return true;
                buff(p, MobEffects.ABSORPTION, 140, 2); buff(p, MobEffects.MOVEMENT_SPEED, 100, 1);
                fx(p, "大魔導循環", ParticleTypes.ENCHANT, SoundEvents.ENCHANTMENT_TABLE_USE); return true;
            }
            case "cataclysm_spellbooks:abyssal_warlock", "discerning_the_eldritch:apothic_acolyte" -> {
                if (!ready(p, "dedicated/hit/" + k, 100)) return true;
                buff(p, MobEffects.DAMAGE_BOOST, 120, 2); buff(p, MobEffects.ABSORPTION, 160, 2);
                fx(p, "深淵同調", ParticleTypes.REVERSE_PORTAL, SoundEvents.WARDEN_SONIC_BOOM); return true;
            }
            case "discerning_the_eldritch:crimson_stag" -> {
                if (!ready(p, "dedicated/hit/" + k, 90)) return true;
                buff(p, MobEffects.MOVEMENT_SPEED, 160, 2); buff(p, MobEffects.REGENERATION, 100, 1);
                fx(p, "深紅の狩猟", ParticleTypes.CRIMSON_SPORE, SoundEvents.RAVAGER_ROAR); return true;
            }
            case "cataclysm_spellbooks:bloom_stone" -> {
                if (!ready(p, "dedicated/hit/" + k, 120)) return true;
                buff(p, MobEffects.REGENERATION, 160, 2); buff(p, MobEffects.DAMAGE_RESISTANCE, 120, 1);
                fx(p, "生命開花", ParticleTypes.HAPPY_VILLAGER, SoundEvents.AMETHYST_BLOCK_CHIME); return true;
            }
            case "cataclysm_spellbooks:cursium_mage", "cataclysm_spellbooks:pharaoh" -> {
                if (!ready(p, "dedicated/hit/" + k, 100)) return true;
                buff(p, MobEffects.ABSORPTION, 160, 2); buff(p, MobEffects.DAMAGE_BOOST, 100, 1);
                fx(p, "王権術式", ParticleTypes.ENCHANT, SoundEvents.TOTEM_USE); return true;
            }
            default -> { return false; }
        }
    }

    private static boolean dedicatedHurt(Player p, SetRegistry.ArmorSet set) {
        String k = set.key();
        switch (k) {
            case "cataclysm:ignitium", "gobber2:gobber_dragon", "irons_spellbooks:netherite_mage" -> {
                if (!ready(p, "dedicated/hurt/" + k, 240)) return true;
                buff(p, MobEffects.DAMAGE_RESISTANCE, 120, 3); buff(p, MobEffects.ABSORPTION, 120, 2);
                fx(p, "不屈の防壁", ParticleTypes.TOTEM_OF_UNDYING, SoundEvents.SHIELD_BLOCK); return true;
            }
            case "irons_spellbooks:priest", "cataclysm_spellbooks:bloom_stone" -> {
                if (!ready(p, "dedicated/hurt/" + k, 260)) return true;
                buff(p, MobEffects.REGENERATION, 160, 2); buff(p, MobEffects.ABSORPTION, 140, 2);
                fx(p, "守護の祝福", ParticleTypes.END_ROD, SoundEvents.TOTEM_USE); return true;
            }
            case "irons_spellbooks:shadowwalker", "gobber2:gobber_end", "discerning_the_eldritch:crimson_stag" -> {
                if (!ready(p, "dedicated/hurt/" + k, 220)) return true;
                buff(p, MobEffects.MOVEMENT_SPEED, 140, 3); buff(p, MobEffects.INVISIBILITY, 40, 0);
                fx(p, "位相離脱", ParticleTypes.PORTAL, SoundEvents.ENDERMAN_TELEPORT); return true;
            }
            default -> { return false; }
        }
    }

    private static void buff(Player p, net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> effect, int ticks, int amplifier) {
        p.addEffect(new MobEffectInstance(effect, ticks, amplifier, false, true, true));
    }

    private static void fx(Player p, String name, net.minecraft.core.particles.SimpleParticleType particle, net.minecraft.sounds.SoundEvent sound) {
        p.displayClientMessage(Component.literal("◆ " + name + " 発動"), true);
        if (p.level() instanceof ServerLevel level) {
            level.sendParticles(particle, p.getX(), p.getY() + 1.0, p.getZ(), 24, 0.45, 0.7, 0.45, 0.04);
            level.playSound(null, p.blockPosition(), sound, SoundSource.PLAYERS, 0.28f, 1.0f);
        }
    }

    private static SetRegistry.ArmorSet fullSet(Player p) {
        SetRegistry.ArmorSet found = null;
        for (EquipmentSlot slot : ARMOR) {
            ItemStack stack = p.getItemBySlot(slot);
            if (stack.isEmpty()) return null;
            SetRegistry.ArmorSet current = SetRegistry.byItem(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            if (current == null) return null;
            if (found == null) found = current;
            else if (found != current) return null;
        }
        return found;
    }

    private static boolean ready(Player p, String key, long cooldownTicks) {
        long now = p.level().getGameTime();
        Map<String, Long> map = COOLDOWNS.computeIfAbsent(p.getUUID(), u -> new HashMap<>());
        long next = map.getOrDefault(key, 0L);
        if (now < next) return false;
        map.put(key, now + cooldownTicks);
        return true;
    }

    private static boolean isVanilla(SetRegistry.ArmorSet s) { return s.key().startsWith("minecraft:"); }
    private static boolean isPhysical(SetRegistry.ArmorSet s) { return s.school().equals("physical"); }
    private static boolean isMagic(SetRegistry.ArmorSet s) { return !isPhysical(s); }
    private CombatAbilities() {}
}
