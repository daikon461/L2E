package jp.oishi.armorsets;

import net.minecraft.core.Holder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.*;

@EventBusSubscriber(modid = ArmorSetsMod.MODID)
public final class SetBonuses {
    private static final List<EquipmentSlot> ARMOR = List.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);

    @SubscribeEvent
    public static void tick(PlayerTickEvent.Post event) {
        Player p = event.getEntity();
        if (p.level().isClientSide() || p.tickCount % 20 != 0) return;
        clear(p);
        Map<SetRegistry.ArmorSet,Integer> counts = equippedSets(p);
        counts.forEach((set,count) -> apply(p,set,count));
    }

    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(event.getItemStack().getItem());
        SetRegistry.ArmorSet set = SetRegistry.byItem(id.toString());
        if (set == null) return;
        boolean physical = isPhysical(set);
        int t = set.tier();
        int equipped = 0;
        Player player = event.getEntity();
        if (player != null) equipped = equippedSets(player).getOrDefault(set, 0);
        boolean hazen = set.key().startsWith("hazennstuff:");
        event.getToolTip().add(Component.literal("━━━━━━━━ セット効果 ━━━━━━━━").withStyle(ChatFormatting.DARK_GRAY));
        event.getToolTip().add(Component.literal("◆ " + set.name() + "  [" + equipped + "/4部位]").withStyle(ChatFormatting.GOLD));
        if (physical) {
            PhysicalBonus b = physicalBonus(set);
            bonusLine(event, 2, equipped, String.format(Locale.ROOT,"攻撃力 +%d%% / 最大体力 +%d%%", b.ad2, b.hp2));
            bonusLine(event, 3, equipped, String.format(Locale.ROOT,"攻撃力 +%d%% / 防御力 +%d / 防具強度 +%d", b.ad3, b.armor3, b.tough3));
            bonusLine(event, 4, equipped, String.format(Locale.ROOT,"攻撃力 +%d%% / 最大体力 +%d%%", b.ad4, b.hp4));
            event.getToolTip().add(Component.literal(String.format(Locale.ROOT,"     移動速度 +%d%% / ノックバック耐性 +%d%%", b.speed4, (int)Math.round(b.kb4*100))).withStyle(equipped >= 4 ? ChatFormatting.GREEN : ChatFormatting.DARK_GRAY));
        } else {
            String school = schoolName(set.school());
            bonusLine(event, 2, equipped, String.format(Locale.ROOT,"%s威力 +%d%% / 最大マナ +%d%%", school,24+t*9,23+t*9));
            bonusLine(event, 3, equipped, String.format(Locale.ROOT,"%s威力 +%d%% / マナ回復 +%d%%", school,18+t*7,30+t*11));
            event.getToolTip().add(Component.literal(String.format(Locale.ROOT,"     詠唱時間短縮 +%d%%",12+t*5)).withStyle(equipped >= 3 ? ChatFormatting.AQUA : ChatFormatting.DARK_GRAY));
            bonusLine(event, 4, equipped, String.format(Locale.ROOT,"%s威力 +%d%% / CT短縮 +%d%%", school,30+t*11,20+t*7));
            event.getToolTip().add(Component.literal(String.format(Locale.ROOT,"     魔法耐性 +%d%% / 最大体力 +%d%%",23+t*9,12+t*7)).withStyle(equipped >= 4 ? ChatFormatting.AQUA : ChatFormatting.DARK_GRAY));
        }
        String mastery = fullSetMasteryText(set);
        if (!mastery.isEmpty()) event.getToolTip().add(Component.literal("★ フルセット追加効果: " + mastery).withStyle(equipped >= 4 ? ChatFormatting.GOLD : ChatFormatting.DARK_GRAY));
        if (hazen) {
            String detail = hazenExtraText(set);
            if (!detail.isEmpty()) event.getToolTip().add(Component.literal("  固有パッシブ: " + detail).withStyle(equipped >= 4 ? ChatFormatting.YELLOW : ChatFormatting.DARK_GRAY));
            event.getToolTip().add(Component.literal("  固有アクティブ: 4部位装備時に戦闘で自動発動").withStyle(equipped >= 4 ? ChatFormatting.LIGHT_PURPLE : ChatFormatting.DARK_GRAY));
        }
        event.getToolTip().add(Component.literal("緑/水色: 発動中  |  灰色: 未解放").withStyle(ChatFormatting.GRAY));
    }

    private static void bonusLine(ItemTooltipEvent event, int required, int equipped, String description) {
        event.getToolTip().add(Component.literal((equipped >= required ? "● " : "○ ") + required + "部位: " + description)
                .withStyle(equipped >= required ? ChatFormatting.GREEN : ChatFormatting.DARK_GRAY));
    }

    private static String hazenExtraText(SetRegistry.ArmorSet set) {
        String id = set.key().substring("hazennstuff:".length());
        return switch (id) {
            case "seraph" -> "最大体力 +24% / 魔法耐性 +30%";
            case "charged_scourge" -> "攻撃速度 +26% / 移動速度 +15%";
            case "soul_flame" -> "炎魔法威力 +32% / 攻撃力 +12%";
            case "supreme_witch" -> "魔法威力 +36% / 最大マナ +38%";
            case "cryogenic_ruler" -> "防具強度 +6 / 最大体力 +25%";
            case "ender_dragon" -> "移動速度 +22% / CT短縮 +18%";
            case "flesh_mass" -> "最大体力 +42% / 防具強度 +5";
            case "legionnaire" -> "攻撃力 +32% / 防御力 +7";
            case "fireblossom_knight" -> "攻撃力 +26% / 炎魔法威力 +20%";
            case "creaking" -> "最大体力 +28% / 防御力 +5";
            default -> hazenRoleText(set);
        };
    }

    private static String hazenRoleText(SetRegistry.ArmorSet set) {
        String n = set.key().substring("hazennstuff:".length());
        int t = set.tier();
        if (n.contains("knight") || n.contains("legionnaire") || n.contains("commander") || n.contains("templar") || n.contains("guardian"))
            return "防御力 +" + (3+t*2) + " / 最大体力 +" + (10+t*5) + "%";
        if (n.contains("hunter") || n.contains("predator") || n.contains("scout") || n.contains("weaver"))
            return "移動速度 +" + (9+t*3) + "% / 攻撃速度 +" + (10+t*4) + "%";
        if (n.contains("robe") || n.contains("mage") || n.contains("witch") || n.contains("gown"))
            return "最大マナ +" + (14+t*7) + "% / マナ回復 +" + (12+t*6) + "%";
        if (set.school().equals("fire") || set.school().equals("lightning"))
            return "攻撃力 +" + (10+t*4) + "% / 攻撃速度 +" + (7+t*3) + "%";
        if (set.school().equals("ice") || set.school().equals("nature"))
            return "防具強度 +" + (2+t) + " / 最大体力 +" + (10+t*5) + "%";
        return "攻撃力 +" + (8+t*4) + "% / 最大体力 +" + (8+t*4) + "%";
    }

    private static Map<SetRegistry.ArmorSet,Integer> equippedSets(Player p) {
        Map<SetRegistry.ArmorSet,Integer> out = new HashMap<>();
        for (EquipmentSlot slot : ARMOR) {
            ItemStack s = p.getItemBySlot(slot); if (s.isEmpty()) continue;
            SetRegistry.ArmorSet set = SetRegistry.byItem(BuiltInRegistries.ITEM.getKey(s.getItem()).toString());
            if (set != null) out.merge(set,1,Integer::sum);
        }
        return out;
    }

    private static void apply(Player p, SetRegistry.ArmorSet set, int count) {
        int t=set.tier(); String key=set.key();
        if (isPhysical(set)) {
            PhysicalBonus b = physicalBonus(set);
            if(count>=2){ add(p,Attributes.ATTACK_DAMAGE,key+"/2/ad",pct(b.ad2)); add(p,Attributes.MAX_HEALTH,key+"/2/hp",pct(b.hp2)); }
            if(count>=3){ add(p,Attributes.ATTACK_DAMAGE,key+"/3/ad",pct(b.ad3)); addValue(p,Attributes.ARMOR,key+"/3/armor",b.armor3); addValue(p,Attributes.ARMOR_TOUGHNESS,key+"/3/tough",b.tough3); }
            if(count>=4){ add(p,Attributes.ATTACK_DAMAGE,key+"/4/ad",pct(b.ad4)); add(p,Attributes.MAX_HEALTH,key+"/4/hp",pct(b.hp4)); add(p,Attributes.MOVEMENT_SPEED,key+"/4/ms",pct(b.speed4)); add(p,Attributes.KNOCKBACK_RESISTANCE,key+"/4/kb",b.kb4); applyFullSetMastery(p,set); }
        } else {
            String power = set.school().equals("generic") ? "irons_spellbooks:spell_power" : "irons_spellbooks:"+set.school()+"_spell_power";
            if(count>=2){ addRL(p,power,key+"/2/sp",pct(24+t*9)); addRL(p,"irons_spellbooks:max_mana",key+"/2/mana",pct(23+t*9)); }
            if(count>=3){ addRL(p,power,key+"/3/sp",pct(18+t*7)); addRL(p,"irons_spellbooks:mana_regen",key+"/3/regen",pct(30+t*11)); addRL(p,"irons_spellbooks:cast_time_reduction",key+"/3/cast",pct(12+t*5)); }
            if(count>=4){ addRL(p,power,key+"/4/sp",pct(30+t*11)); addRL(p,"irons_spellbooks:cooldown_reduction",key+"/4/cd",pct(20+t*7)); addRL(p,"irons_spellbooks:spell_resist",key+"/4/res",pct(23+t*9)); add(p,Attributes.MAX_HEALTH,key+"/4/hp",pct(12+t*7)); applyFullSetMastery(p,set); }
        }
    }

    private record PhysicalBonus(int ad2,int hp2,int ad3,int armor3,int tough3,int ad4,int hp4,int speed4,double kb4) {}
    private static PhysicalBonus physicalBonus(SetRegistry.ArmorSet s){
        return switch(s.key()){
            case "minecraft:leather" -> new PhysicalBonus(4,5,3,0,0,5,6,7,.00);
            case "minecraft:golden" -> new PhysicalBonus(5,4,4,0,0,6,5,5,.00);
            case "minecraft:chainmail" -> new PhysicalBonus(5,6,4,1,0,7,7,3,.03);
            case "minecraft:iron" -> new PhysicalBonus(5,7,4,2,1,7,9,2,.04);
            case "minecraft:diamond" -> new PhysicalBonus(6,8,5,3,2,9,11,3,.05);
            case "minecraft:netherite" -> new PhysicalBonus(7,9,6,4,2,11,15,3,.08);
            default -> { int t=s.tier(); yield new PhysicalBonus(18+t*7,23+t*9,10+t*4,3+t*2,1+t,18+t*6,20+t*8,6+t*2,Math.min(.35,.08+t*.05)); }
        };
    }

    private static void applyFullSetMastery(Player p, SetRegistry.ArmorSet s) {
        String k=s.key();
        // Vanilla stays deliberately modest so modded progression remains meaningful.
        switch(k){
            case "minecraft:leather" -> add(p,Attributes.MOVEMENT_SPEED,k+"/mastery/ms",pct(3));
            case "minecraft:golden" -> add(p,Attributes.ATTACK_SPEED,k+"/mastery/as",pct(4));
            case "minecraft:chainmail" -> add(p,Attributes.ATTACK_SPEED,k+"/mastery/as",pct(3));
            case "minecraft:iron" -> addValue(p,Attributes.ARMOR,k+"/mastery/armor",1);
            case "minecraft:diamond" -> addValue(p,Attributes.ARMOR_TOUGHNESS,k+"/mastery/tough",1);
            case "minecraft:netherite" -> add(p,Attributes.KNOCKBACK_RESISTANCE,k+"/mastery/kb",.03);
            case "cataclysm:ignitium" -> { add(p,Attributes.ATTACK_DAMAGE,k+"/mastery/ad",pct(24)); add(p,Attributes.ATTACK_SPEED,k+"/mastery/as",pct(10)); }
            case "cataclysm:cursium" -> { add(p,Attributes.MAX_HEALTH,k+"/mastery/hp",pct(28)); add(p,Attributes.MOVEMENT_SPEED,k+"/mastery/ms",pct(12)); }
            case "gobber2:gobber" -> { add(p,Attributes.ATTACK_DAMAGE,k+"/mastery/ad",pct(12)); add(p,Attributes.MAX_HEALTH,k+"/mastery/hp",pct(15)); }
            case "gobber2:gobber_nether" -> { add(p,Attributes.ATTACK_DAMAGE,k+"/mastery/ad",pct(20)); addValue(p,Attributes.ARMOR_TOUGHNESS,k+"/mastery/tough",4); }
            case "gobber2:gobber_end" -> { add(p,Attributes.ATTACK_DAMAGE,k+"/mastery/ad",pct(25)); add(p,Attributes.MOVEMENT_SPEED,k+"/mastery/ms",pct(10)); }
            case "gobber2:gobber_dragon" -> { add(p,Attributes.ATTACK_DAMAGE,k+"/mastery/ad",pct(35)); add(p,Attributes.MAX_HEALTH,k+"/mastery/hp",pct(35)); addValue(p,Attributes.ARMOR_TOUGHNESS,k+"/mastery/tough",6); }
            default -> applyMagicMastery(p,s);
        }
    }

    private static void applyMagicMastery(Player p, SetRegistry.ArmorSet s) {
        String k=s.key(); int t=s.tier(); String school=s.school();
        // Hazennstuff: individual full-set specializations on top of the shared magic profile.
        if(k.startsWith("hazennstuff:")) {
            switch(k.substring("hazennstuff:".length())) {
                case "seraph" -> { add(p,Attributes.MAX_HEALTH,k+"/hazen/hp",pct(24)); addRL(p,"irons_spellbooks:spell_resist",k+"/hazen/res",pct(30)); }
                case "charged_scourge" -> { add(p,Attributes.ATTACK_SPEED,k+"/hazen/as",pct(26)); add(p,Attributes.MOVEMENT_SPEED,k+"/hazen/ms",pct(15)); }
                case "soul_flame" -> { addRL(p,"irons_spellbooks:fire_spell_power",k+"/hazen/fire",pct(32)); add(p,Attributes.ATTACK_DAMAGE,k+"/hazen/ad",pct(12)); }
                case "supreme_witch" -> { addRL(p,"irons_spellbooks:spell_power",k+"/hazen/sp",pct(36)); addRL(p,"irons_spellbooks:max_mana",k+"/hazen/mana",pct(38)); }
                case "cryogenic_ruler" -> { addValue(p,Attributes.ARMOR_TOUGHNESS,k+"/hazen/tough",6); add(p,Attributes.MAX_HEALTH,k+"/hazen/hp",pct(25)); }
                case "ender_dragon" -> { add(p,Attributes.MOVEMENT_SPEED,k+"/hazen/ms",pct(22)); addRL(p,"irons_spellbooks:cooldown_reduction",k+"/hazen/cd",pct(18)); }
                case "flesh_mass" -> { add(p,Attributes.MAX_HEALTH,k+"/hazen/hp",pct(42)); addValue(p,Attributes.ARMOR_TOUGHNESS,k+"/hazen/tough",5); }
                case "legionnaire" -> { add(p,Attributes.ATTACK_DAMAGE,k+"/hazen/ad",pct(32)); addValue(p,Attributes.ARMOR,k+"/hazen/armor",7); }
                case "fireblossom_knight" -> { add(p,Attributes.ATTACK_DAMAGE,k+"/hazen/ad",pct(26)); addRL(p,"irons_spellbooks:fire_spell_power",k+"/hazen/fire",pct(20)); }
                case "creaking" -> { add(p,Attributes.MAX_HEALTH,k+"/hazen/hp",pct(28)); addValue(p,Attributes.ARMOR,k+"/hazen/armor",5); }
                default -> applyHazenRole(p,s);
            }
        }
        // Every magic set gets a school-flavoured capstone. Values are intentionally high for L2Hostility.
        String power = school.equals("generic") ? "irons_spellbooks:spell_power" : "irons_spellbooks:"+school+"_spell_power";
        int powerPct = 12 + t*6, manaPct = 8 + t*4, regenPct = 10 + t*5, cdPct = 5 + t*3;
        if(k.contains("ascended") || k.contains("supreme") || k.contains("ruler") || k.contains("dragon")) powerPct += 8;
        if(k.contains("battlemage") || k.contains("knight") || k.contains("hunter") || k.contains("templar")) {
            add(p,Attributes.ATTACK_DAMAGE,k+"/mastery/ad",pct(12+t*7));
            add(p,Attributes.MAX_HEALTH,k+"/mastery/hp",pct(10+t*4));
            addValue(p,Attributes.ARMOR_TOUGHNESS,k+"/mastery/tough",1+t);
            powerPct -= 3;
        }
        if(k.contains("witch") || k.contains("robes") || k.contains("wizard") || k.contains("magician") || k.contains("scholar")) { manaPct += 10; regenPct += 10; }
        if(k.contains("scourge") || k.contains("thunder") || school.equals("lightning")) add(p,Attributes.MOVEMENT_SPEED,k+"/mastery/ms",pct(6+t*2));
        if(k.contains("frost") || k.contains("cryo") || k.contains("permafrost") || school.equals("ice")) addValue(p,Attributes.ARMOR,k+"/mastery/armor",2+t);
        if(school.equals("blood")) add(p,Attributes.MAX_HEALTH,k+"/mastery/bloodhp",pct(12+t*7));
        if(school.equals("holy")) addRL(p,"irons_spellbooks:spell_resist",k+"/mastery/res",pct(10+t*4));
        if(school.equals("ender") || school.equals("eldritch")) cdPct += 5;
        addRL(p,power,k+"/mastery/power",pct(Math.max(5,powerPct)));
        addRL(p,"irons_spellbooks:max_mana",k+"/mastery/mana",pct(manaPct));
        addRL(p,"irons_spellbooks:mana_regen",k+"/mastery/regen",pct(regenPct));
        addRL(p,"irons_spellbooks:cooldown_reduction",k+"/mastery/cd",pct(cdPct));
    }

    // Distinct passive combat roles for the remaining Hazen armor sets.
    // Stable modifier IDs, removed by clear() on every refresh.
    private static void applyHazenRole(Player p, SetRegistry.ArmorSet s) {
        String k=s.key(), n=k.substring("hazennstuff:".length());
        int t=s.tier();
        if (n.contains("knight") || n.contains("legionnaire") || n.contains("commander") || n.contains("templar") || n.contains("guardian")) {
            addValue(p,Attributes.ARMOR,k+"/hazenrole/armor",3+t*2);
            add(p,Attributes.MAX_HEALTH,k+"/hazenrole/hp",pct(10+t*5));
        } else if (n.contains("hunter") || n.contains("predator") || n.contains("scout") || n.contains("weaver")) {
            add(p,Attributes.MOVEMENT_SPEED,k+"/hazenrole/speed",pct(9+t*3));
            add(p,Attributes.ATTACK_SPEED,k+"/hazenrole/haste",pct(10+t*4));
        } else if (n.contains("robe") || n.contains("mage") || n.contains("witch") || n.contains("gown")) {
            addRL(p,"irons_spellbooks:max_mana",k+"/hazenrole/mana",pct(14+t*7));
            addRL(p,"irons_spellbooks:mana_regen",k+"/hazenrole/regen",pct(12+t*6));
        } else if (s.school().equals("fire") || s.school().equals("lightning")) {
            add(p,Attributes.ATTACK_DAMAGE,k+"/hazenrole/ad",pct(10+t*4));
            add(p,Attributes.ATTACK_SPEED,k+"/hazenrole/haste",pct(7+t*3));
        } else if (s.school().equals("ice") || s.school().equals("nature")) {
            addValue(p,Attributes.ARMOR_TOUGHNESS,k+"/hazenrole/tough",2+t);
            add(p,Attributes.MAX_HEALTH,k+"/hazenrole/hp",pct(10+t*5));
        } else {
            add(p,Attributes.ATTACK_DAMAGE,k+"/hazenrole/ad",pct(8+t*4));
            add(p,Attributes.MAX_HEALTH,k+"/hazenrole/hp",pct(8+t*4));
        }
    }

    private static String fullSetMasteryText(SetRegistry.ArmorSet s){
        String k=s.key();
        return switch(k){
            case "minecraft:leather" -> "身軽さ（移動速度 +3%）";
            case "minecraft:golden" -> "黄金の速攻（攻撃速度 +4%）";
            case "minecraft:chainmail" -> "軽戦士（攻撃速度 +3%）";
            case "minecraft:iron" -> "鉄壁（防御 +1）";
            case "minecraft:diamond" -> "堅牢（防具強度 +1）";
            case "minecraft:netherite" -> "不動（KB耐性 +3%）";
            case "cataclysm:ignitium" -> "灼熱の覇者（攻撃力 +24% / 攻撃速度 +10%）";
            case "cataclysm:cursium" -> "深淵の生存者（最大体力 +28% / 移動速度 +12%）";
            case "gobber2:gobber" -> "ゴバーの加護（攻撃力 +12% / 最大体力 +15%）";
            case "gobber2:gobber_nether" -> "獄炎の闘志（攻撃力 +20% / 防具強度 +4）";
            case "gobber2:gobber_end" -> "終焉の追撃（攻撃力 +25% / 移動速度 +10%）";
            case "gobber2:gobber_dragon" -> "竜王の権能（攻撃力 +35% / 最大体力 +35% / 防具強度 +6）";
            case "irons_spellbooks:pyromancer" -> "煉獄共鳴（炎魔法とマナ循環を大幅強化）";
            case "irons_spellbooks:cryomancer" -> "永久凍土（氷魔法強化 + 追加防御）";
            case "irons_spellbooks:electromancer" -> "雷霆疾駆（雷魔法強化 + 追加機動力）";
            case "irons_spellbooks:cultist" -> "血の盟約（血魔法強化 + 追加最大体力）";
            case "irons_spellbooks:shadowwalker" -> "虚空歩行（エンダー魔法 + CT短縮を強化）";
            case "irons_spellbooks:priest" -> "聖域（聖魔法 + 魔法耐性を強化）";
            case "irons_spellbooks:netherite_mage" -> "魔導重装（魔法強化 + 近接・耐久補正）";
            case "cataclysm_spellbooks:ignis" -> "イグニスの残火（炎魔法を大幅強化）";
            case "cataclysm_spellbooks:abyssal_warlock" -> "深淵契約（エルドリッチ魔法とCT短縮を強化）";
            case "cataclysm_spellbooks:cursium_mage" -> "カーシウム共鳴（エンダー魔法と生存力を強化）";
            case "discerning_the_eldritch:apothic_acolyte" -> "外なる叡智（エルドリッチ魔法とマナ循環を強化）";
            case "discerning_the_eldritch:crimson_stag" -> "深紅の儀式（エルドリッチ魔法とCT短縮を強化）";
            default -> generatedMasteryText(s);
        };
    }

    private static String generatedMasteryText(SetRegistry.ArmorSet s){
        String k=s.key(); String school=schoolName(s.school());
        if(k.contains("battlemage") || k.contains("knight") || k.contains("hunter") || k.contains("templar")) return "魔戦覚醒（"+school+"強化 + 攻撃・体力・防具強度）";
        if(k.contains("ascended") || k.contains("supreme") || k.contains("ruler") || k.contains("dragon")) return "上位共鳴（"+school+"威力とマナ循環を特大強化）";
        if(s.school().equals("ice")) return "氷晶障壁（"+school+"強化 + 追加防御）";
        if(s.school().equals("lightning")) return "雷光加速（"+school+"強化 + 追加機動力）";
        if(s.school().equals("blood")) return "血脈増幅（"+school+"強化 + 追加最大体力）";
        if(s.school().equals("holy")) return "聖なる守護（"+school+"強化 + 魔法耐性）";
        if(s.school().equals("ender") || s.school().equals("eldritch")) return "異界共鳴（"+school+"強化 + CT短縮）";
        return "魔力共鳴（"+school+"威力・最大マナ・回復・CTを強化）";
    }
    private static boolean isPhysical(SetRegistry.ArmorSet s){ return s.school().equals("physical"); }
    private static double pct(int p){ return p/100.0; }

    private static String schoolName(String s){ return switch(s){case "fire"->"炎魔法";case "ice"->"氷魔法";case "lightning"->"雷魔法";case "holy"->"聖魔法";case "ender"->"エンダー魔法";case "blood"->"血魔法";case "evocation"->"召喚魔法";case "nature"->"自然魔法";case "eldritch"->"エルドリッチ魔法";default->"魔法";}; }
    private static void clear(Player p) {
        for (AttributeInstance ai : p.getAttributes().getSyncableAttributes()) {
            List<AttributeModifier> rm = ai.getModifiers().stream().filter(m -> m.id().getNamespace().equals(ArmorSetsMod.MODID)).toList();
            rm.forEach(m -> ai.removeModifier(m.id()));
        }
    }
    private static void add(Player p, Holder<Attribute> attr,String path,double amount){ addInst(p.getAttribute(attr),path,amount,AttributeModifier.Operation.ADD_MULTIPLIED_BASE); }
    private static void addValue(Player p, Holder<Attribute> attr,String path,double amount){ addInst(p.getAttribute(attr),path,amount,AttributeModifier.Operation.ADD_VALUE); }
    private static void addRL(Player p,String attrId,String path,double amount){
        Holder<Attribute> attr = BuiltInRegistries.ATTRIBUTE.getHolder(ResourceLocation.parse(attrId)).orElse(null); if(attr!=null) addInst(p.getAttribute(attr),path,amount,AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }
    private static void addInst(AttributeInstance ai,String path,double amount,AttributeModifier.Operation op) {
        if(ai==null)return; ResourceLocation id=ResourceLocation.fromNamespaceAndPath(ArmorSetsMod.MODID, sanitize("set/"+path)); ai.addTransientModifier(new AttributeModifier(id,amount,op));
    }
    private static String sanitize(String s){ return s.toLowerCase(Locale.ROOT).replace(':','/').replaceAll("[^a-z0-9_./-]","_"); }
}
