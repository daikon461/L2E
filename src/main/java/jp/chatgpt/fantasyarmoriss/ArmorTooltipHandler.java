package jp.chatgpt.fantasyarmoriss;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public final class ArmorTooltipHandler {
 @SubscribeEvent public static void tooltip(ItemTooltipEvent e){
  ItemStack stack=e.getItemStack();
  var key=BuiltInRegistries.ITEM.getKey(stack.getItem());
  if(key==null || !key.getNamespace().equals("fantasy_armor"))return;
  String full="fantasy_armor:"+key.getPath();
  ArmorSetRegistry.SetDef s=null;
  for(var x:ArmorSetRegistry.all()){
   if(full.equals(x.helmet())||full.equals(x.chestplate())||full.equals(x.leggings())||full.equals(x.boots())){s=x;break;}
  }
  if(s==null)return;
  e.getToolTip().add(Component.empty());
  e.getToolTip().add(Component.literal("◆ セット効果【"+profileName(s.profile())+"】").withStyle(ChatFormatting.GOLD));
  e.getToolTip().add(Component.literal("2/4  "+tier2(s.profile())).withStyle(ChatFormatting.GREEN));
  e.getToolTip().add(Component.literal("3/4  "+tier3(s.profile())).withStyle(ChatFormatting.AQUA));
  e.getToolTip().add(Component.literal("4/4  "+tier4(s.profile())).withStyle(ChatFormatting.LIGHT_PURPLE));
  e.getToolTip().add(Component.literal("★ "+abilityName(s.key())).withStyle(ChatFormatting.GOLD));
  e.getToolTip().add(Component.literal("  "+abilityInfo(s.key())).withStyle(ChatFormatting.GRAY));
 }
 private static String profileName(ArmorSetRegistry.Profile p){return switch(p){
   case TACTIC_TANK -> "戦術防御";
   case BRUISER -> "重戦士";
   case SHADOW_BRUISER -> "影の重戦士";
   case DARK_HYBRID -> "闇の魔法戦士";
   case BERSERKER -> "狂戦士";
   case HEAVY_SLAYER -> "大型討伐";
   case SOLDIER -> "万能兵士";
   case ASSASSIN -> "暗殺";
   case VAMPIRIC_BRUISER -> "吸血重戦士";
   case FORTRESS -> "要塞";
   case RELIC_HYBRID -> "遺物魔法戦士";
   case HUNTER -> "狩人";
   case EXECUTIONER -> "処刑人";
   case CHARGER -> "突撃";
   case SENTINEL -> "守護";
   case HEROIC -> "英雄";
   case BLOOD_HYBRID -> "血の魔法戦士";
   case DUELIST -> "決闘";
   case OLD_GUARD -> "古参守護";
   case LANCER -> "高速槍兵";
   case PALADIN -> "聖騎士";
   case RONIN -> "浪人";
   case SILVER_GUARD -> "銀騎士";
   case HOLY_HYBRID -> "聖なる魔法戦士";
   case AERIAL -> "空戦機動";
   case THIEF -> "盗賊";
   case TWINNED -> "万能双生";
   case PURE_MAGE -> "純魔法";
   case WIND_MAGE -> "風魔法";
  };}
 private static String tier2(ArmorSetRegistry.Profile p){return switch(p){
   case TACTIC_TANK -> "攻撃 +3% / 攻速 +2% / 最大HP +7% / 防御 +1.3 / 防具強度 +1.3 / 移動 +1% / KB耐性 +7%";
   case BRUISER -> "攻撃 +10% / 攻速 +2% / 最大HP +6% / 防御 +0.6 / 防具強度 +1.3 / KB耐性 +4%";
   case SHADOW_BRUISER -> "攻撃 +9% / 攻速 +4% / 最大HP +3% / 防具強度 +0.6 / 移動 +3% / 魔力 +3%";
   case DARK_HYBRID -> "攻撃 +7% / 攻速 +3% / 最大HP +5% / 防具強度 +0.6 / 移動 +1% / 魔力 +9% / 最大マナ +7% / マナ回復 +5% / CT短縮 +2%";
   case BERSERKER -> "攻撃 +14% / 攻速 +5% / 最大HP +3% / 移動 +1%";
   case HEAVY_SLAYER -> "攻撃 +16% / 攻速 -2% / 最大HP +6% / 防御 +0.6 / 防具強度 +1.9 / 移動 -1% / KB耐性 +9%";
   case SOLDIER -> "攻撃 +8% / 攻速 +3% / 最大HP +6% / 防御 +0.6 / 防具強度 +0.6 / 移動 +1% / KB耐性 +3%";
   case ASSASSIN -> "攻撃 +9% / 攻速 +8% / 移動 +6%";
   case VAMPIRIC_BRUISER -> "攻撃 +9% / 攻速 +3% / 最大HP +10% / 防具強度 +0.6 / 移動 +1% / 魔力 +3%";
   case FORTRESS -> "攻撃 +2% / 最大HP +15% / 防御 +2.5 / 防具強度 +2.5 / 移動 -1% / KB耐性 +13%";
   case RELIC_HYBRID -> "攻撃 +5% / 攻速 +2% / 最大HP +6% / 防御 +0.6 / 防具強度 +0.6 / 移動 +1% / 魔力 +7% / 最大マナ +6% / マナ回復 +5% / CT短縮 +2%";
   case HUNTER -> "攻撃 +8% / 攻速 +6% / 最大HP +2% / 移動 +4%";
   case EXECUTIONER -> "攻撃 +15% / 攻速 -1% / 最大HP +4% / 防具強度 +1.3 / KB耐性 +5%";
   case CHARGER -> "攻撃 +9% / 攻速 +2% / 最大HP +6% / 防御 +0.6 / 防具強度 +0.6 / 移動 +4% / KB耐性 +7%";
   case SENTINEL -> "攻撃 +4% / 攻速 +1% / 最大HP +13% / 防御 +1.9 / 防具強度 +1.9 / KB耐性 +11%";
   case HEROIC -> "攻撃 +9% / 攻速 +4% / 最大HP +8% / 防御 +0.6 / 防具強度 +1.3 / 移動 +2% / KB耐性 +4%";
   case BLOOD_HYBRID -> "攻撃 +6% / 攻速 +6% / 最大HP +4% / 防具強度 +0.6 / 移動 +3% / 魔力 +8% / 最大マナ +5% / マナ回復 +6% / CT短縮 +2%";
   case DUELIST -> "攻撃 +11% / 攻速 +9% / 最大HP +2% / 移動 +4%";
   case OLD_GUARD -> "攻撃 +6% / 攻速 +1% / 最大HP +11% / 防御 +1.3 / 防具強度 +1.9 / 移動 -0% / KB耐性 +9%";
   case LANCER -> "攻撃 +10% / 攻速 +10% / 最大HP +3% / 防具強度 +0.6 / 移動 +4% / KB耐性 +2%";
   case PALADIN -> "攻撃 +6% / 攻速 +2% / 最大HP +10% / 防御 +1.3 / 防具強度 +1.3 / KB耐性 +8% / 魔力 +5% / 最大マナ +6% / マナ回復 +5% / CT短縮 +1%";
   case RONIN -> "攻撃 +11% / 攻速 +7% / 最大HP +2% / 移動 +5%";
   case SILVER_GUARD -> "攻撃 +7% / 攻速 +3% / 最大HP +8% / 防御 +1.3 / 防具強度 +1.3 / 移動 +1% / KB耐性 +7%";
   case HOLY_HYBRID -> "攻撃 +5% / 攻速 +3% / 最大HP +7% / 防御 +0.6 / 防具強度 +0.6 / 移動 +2% / KB耐性 +3% / 魔力 +10% / 最大マナ +9% / マナ回復 +7% / CT短縮 +3%";
   case AERIAL -> "攻撃 +6% / 攻速 +6% / 最大HP +3% / 移動 +7% / 魔力 +4% / 最大マナ +3%";
   case THIEF -> "攻撃 +6% / 攻速 +10% / 移動 +8%";
   case TWINNED -> "攻撃 +8% / 攻速 +5% / 最大HP +6% / 防御 +0.6 / 防具強度 +0.6 / 移動 +2% / KB耐性 +3% / 魔力 +5% / 最大マナ +4% / マナ回復 +2% / CT短縮 +1%";
   case PURE_MAGE -> "最大HP +3% / 移動 +1% / 魔力 +18% / 最大マナ +18% / マナ回復 +13% / CT短縮 +6%";
   case WIND_MAGE -> "攻撃 +2% / 攻速 +3% / 最大HP +2% / 移動 +8% / 魔力 +14% / 最大マナ +12% / マナ回復 +11% / CT短縮 +5%";
  };}
 private static String tier3(ArmorSetRegistry.Profile p){return switch(p){
   case TACTIC_TANK -> "攻撃 +2% / 攻速 +1% / 最大HP +6% / 防御 +1.1 / 防具強度 +1.1 / 移動 +1% / KB耐性 +6%";
   case BRUISER -> "攻撃 +9% / 攻速 +1% / 最大HP +5% / 防御 +0.5 / 防具強度 +1.1 / KB耐性 +4%";
   case SHADOW_BRUISER -> "攻撃 +8% / 攻速 +3% / 最大HP +3% / 防具強度 +0.5 / 移動 +3% / 魔力 +2%";
   case DARK_HYBRID -> "攻撃 +6% / 攻速 +2% / 最大HP +4% / 防具強度 +0.5 / 移動 +1% / 魔力 +8% / 最大マナ +6% / マナ回復 +4% / CT短縮 +2%";
   case BERSERKER -> "攻撃 +12% / 攻速 +4% / 最大HP +2% / 移動 +1%";
   case HEAVY_SLAYER -> "攻撃 +14% / 攻速 -2% / 最大HP +5% / 防御 +0.5 / 防具強度 +1.6 / 移動 -1% / KB耐性 +8%";
   case SOLDIER -> "攻撃 +6% / 攻速 +3% / 最大HP +5% / 防御 +0.5 / 防具強度 +0.5 / 移動 +1% / KB耐性 +3%";
   case ASSASSIN -> "攻撃 +8% / 攻速 +7% / 移動 +5%";
   case VAMPIRIC_BRUISER -> "攻撃 +8% / 攻速 +2% / 最大HP +8% / 防具強度 +0.5 / 移動 +1% / 魔力 +2%";
   case FORTRESS -> "攻撃 +2% / 最大HP +13% / 防御 +2.2 / 防具強度 +2.2 / 移動 -1% / KB耐性 +11%";
   case RELIC_HYBRID -> "攻撃 +4% / 攻速 +2% / 最大HP +5% / 防御 +0.5 / 防具強度 +0.5 / 移動 +1% / 魔力 +6% / 最大マナ +5% / マナ回復 +4% / CT短縮 +2%";
   case HUNTER -> "攻撃 +6% / 攻速 +5% / 最大HP +2% / 移動 +4%";
   case EXECUTIONER -> "攻撃 +13% / 攻速 -1% / 最大HP +4% / 防具強度 +1.1 / KB耐性 +4%";
   case CHARGER -> "攻撃 +8% / 攻速 +2% / 最大HP +5% / 防御 +0.5 / 防具強度 +0.5 / 移動 +4% / KB耐性 +6%";
   case SENTINEL -> "攻撃 +3% / 攻速 +1% / 最大HP +11% / 防御 +1.6 / 防具強度 +1.6 / KB耐性 +9%";
   case HEROIC -> "攻撃 +8% / 攻速 +3% / 最大HP +7% / 防御 +0.5 / 防具強度 +1.1 / 移動 +2% / KB耐性 +4%";
   case BLOOD_HYBRID -> "攻撃 +5% / 攻速 +5% / 最大HP +4% / 防具強度 +0.5 / 移動 +3% / 魔力 +7% / 最大マナ +4% / マナ回復 +5% / CT短縮 +2%";
   case DUELIST -> "攻撃 +9% / 攻速 +8% / 最大HP +2% / 移動 +4%";
   case OLD_GUARD -> "攻撃 +5% / 攻速 +1% / 最大HP +10% / 防御 +1.1 / 防具強度 +1.6 / 移動 -0% / KB耐性 +8%";
   case LANCER -> "攻撃 +9% / 攻速 +9% / 最大HP +2% / 防具強度 +0.5 / 移動 +3% / KB耐性 +2%";
   case PALADIN -> "攻撃 +5% / 攻速 +1% / 最大HP +9% / 防御 +1.1 / 防具強度 +1.1 / KB耐性 +7% / 魔力 +4% / 最大マナ +5% / マナ回復 +4% / CT短縮 +1%";
   case RONIN -> "攻撃 +10% / 攻速 +6% / 最大HP +2% / 移動 +4%";
   case SILVER_GUARD -> "攻撃 +6% / 攻速 +2% / 最大HP +7% / 防御 +1.1 / 防具強度 +1.1 / 移動 +1% / KB耐性 +6%";
   case HOLY_HYBRID -> "攻撃 +4% / 攻速 +2% / 最大HP +6% / 防御 +0.5 / 防具強度 +0.5 / 移動 +2% / KB耐性 +3% / 魔力 +8% / 最大マナ +7% / マナ回復 +6% / CT短縮 +2%";
   case AERIAL -> "攻撃 +5% / 攻速 +5% / 最大HP +2% / 移動 +6% / 魔力 +3% / 最大マナ +3%";
   case THIEF -> "攻撃 +5% / 攻速 +9% / 移動 +7%";
   case TWINNED -> "攻撃 +7% / 攻速 +4% / 最大HP +5% / 防御 +0.5 / 防具強度 +0.5 / 移動 +2% / KB耐性 +3% / 魔力 +4% / 最大マナ +3% / マナ回復 +2% / CT短縮 +1%";
   case PURE_MAGE -> "最大HP +2% / 移動 +1% / 魔力 +16% / 最大マナ +16% / マナ回復 +11% / CT短縮 +5%";
   case WIND_MAGE -> "攻撃 +2% / 攻速 +3% / 最大HP +2% / 移動 +7% / 魔力 +12% / 最大マナ +10% / マナ回復 +9% / CT短縮 +4%";
  };}
 private static String tier4(ArmorSetRegistry.Profile p){return switch(p){
   case TACTIC_TANK -> "攻撃 +3% / 攻速 +2% / 最大HP +7% / 防御 +1.3 / 防具強度 +1.3 / 移動 +1% / KB耐性 +7%";
   case BRUISER -> "攻撃 +10% / 攻速 +2% / 最大HP +6% / 防御 +0.6 / 防具強度 +1.3 / KB耐性 +4%";
   case SHADOW_BRUISER -> "攻撃 +9% / 攻速 +4% / 最大HP +3% / 防具強度 +0.6 / 移動 +3% / 魔力 +3%";
   case DARK_HYBRID -> "攻撃 +7% / 攻速 +3% / 最大HP +5% / 防具強度 +0.6 / 移動 +1% / 魔力 +9% / 最大マナ +7% / マナ回復 +5% / CT短縮 +2%";
   case BERSERKER -> "攻撃 +14% / 攻速 +5% / 最大HP +3% / 移動 +1%";
   case HEAVY_SLAYER -> "攻撃 +16% / 攻速 -2% / 最大HP +6% / 防御 +0.6 / 防具強度 +1.9 / 移動 -1% / KB耐性 +9%";
   case SOLDIER -> "攻撃 +8% / 攻速 +3% / 最大HP +6% / 防御 +0.6 / 防具強度 +0.6 / 移動 +1% / KB耐性 +3%";
   case ASSASSIN -> "攻撃 +9% / 攻速 +8% / 移動 +6%";
   case VAMPIRIC_BRUISER -> "攻撃 +9% / 攻速 +3% / 最大HP +10% / 防具強度 +0.6 / 移動 +1% / 魔力 +3%";
   case FORTRESS -> "攻撃 +2% / 最大HP +15% / 防御 +2.5 / 防具強度 +2.5 / 移動 -1% / KB耐性 +13%";
   case RELIC_HYBRID -> "攻撃 +5% / 攻速 +2% / 最大HP +6% / 防御 +0.6 / 防具強度 +0.6 / 移動 +1% / 魔力 +7% / 最大マナ +6% / マナ回復 +5% / CT短縮 +2%";
   case HUNTER -> "攻撃 +8% / 攻速 +6% / 最大HP +2% / 移動 +4%";
   case EXECUTIONER -> "攻撃 +15% / 攻速 -1% / 最大HP +4% / 防具強度 +1.3 / KB耐性 +5%";
   case CHARGER -> "攻撃 +9% / 攻速 +2% / 最大HP +6% / 防御 +0.6 / 防具強度 +0.6 / 移動 +4% / KB耐性 +7%";
   case SENTINEL -> "攻撃 +4% / 攻速 +1% / 最大HP +13% / 防御 +1.9 / 防具強度 +1.9 / KB耐性 +11%";
   case HEROIC -> "攻撃 +9% / 攻速 +4% / 最大HP +8% / 防御 +0.6 / 防具強度 +1.3 / 移動 +2% / KB耐性 +4%";
   case BLOOD_HYBRID -> "攻撃 +6% / 攻速 +6% / 最大HP +4% / 防具強度 +0.6 / 移動 +3% / 魔力 +8% / 最大マナ +5% / マナ回復 +6% / CT短縮 +2%";
   case DUELIST -> "攻撃 +11% / 攻速 +9% / 最大HP +2% / 移動 +4%";
   case OLD_GUARD -> "攻撃 +6% / 攻速 +1% / 最大HP +11% / 防御 +1.3 / 防具強度 +1.9 / 移動 -0% / KB耐性 +9%";
   case LANCER -> "攻撃 +10% / 攻速 +10% / 最大HP +3% / 防具強度 +0.6 / 移動 +4% / KB耐性 +2%";
   case PALADIN -> "攻撃 +6% / 攻速 +2% / 最大HP +10% / 防御 +1.3 / 防具強度 +1.3 / KB耐性 +8% / 魔力 +5% / 最大マナ +6% / マナ回復 +5% / CT短縮 +1%";
   case RONIN -> "攻撃 +11% / 攻速 +7% / 最大HP +2% / 移動 +5%";
   case SILVER_GUARD -> "攻撃 +7% / 攻速 +3% / 最大HP +8% / 防御 +1.3 / 防具強度 +1.3 / 移動 +1% / KB耐性 +7%";
   case HOLY_HYBRID -> "攻撃 +5% / 攻速 +3% / 最大HP +7% / 防御 +0.6 / 防具強度 +0.6 / 移動 +2% / KB耐性 +3% / 魔力 +10% / 最大マナ +9% / マナ回復 +7% / CT短縮 +3%";
   case AERIAL -> "攻撃 +6% / 攻速 +6% / 最大HP +3% / 移動 +7% / 魔力 +4% / 最大マナ +3%";
   case THIEF -> "攻撃 +6% / 攻速 +10% / 移動 +8%";
   case TWINNED -> "攻撃 +8% / 攻速 +5% / 最大HP +6% / 防御 +0.6 / 防具強度 +0.6 / 移動 +2% / KB耐性 +3% / 魔力 +5% / 最大マナ +4% / マナ回復 +2% / CT短縮 +1%";
   case PURE_MAGE -> "最大HP +3% / 移動 +1% / 魔力 +18% / 最大マナ +18% / マナ回復 +13% / CT短縮 +6%";
   case WIND_MAGE -> "攻撃 +2% / 攻速 +3% / 最大HP +2% / 移動 +8% / 魔力 +14% / 最大マナ +12% / マナ回復 +11% / CT短縮 +5%";
  };}
 private static String abilityName(String k){return switch(k){
   case "chess_board_knight" -> "王手の構え";
   case "crucible_knight" -> "坩堝の猛進";
   case "dark_cover" -> "深闇の追撃";
   case "dark_lord" -> "暗黒支配";
   case "dead_gladiator" -> "死闘の昂揚";
   case "dragonslayer" -> "竜殺しの一撃";
   case "eclipse_soldier" -> "蝕の戦陣";
   case "evening_ghost" -> "黄昏の奇襲";
   case "flesh_of_the_feaster" -> "飽食の血肉";
   case "fog_guard" -> "霧城の防壁";
   case "forgotten_trace" -> "忘却の残響";
   case "gilded_hunt" -> "黄金狩猟";
   case "golden_execution" -> "黄金処刑";
   case "golden_horns" -> "黄金角の突撃";
   case "grave_sentinel" -> "墓守の誓い";
   case "hero" -> "英雄の奮起";
   case "lady_maria" -> "血刃乱舞";
   case "malenia" -> "不敗の剣舞";
   case "old_knight" -> "古騎士の不屈";
   case "ornstein" -> "雷槍疾駆";
   case "redeemer" -> "贖罪の加護";
   case "ronin" -> "一閃";
   case "silver_knight" -> "銀壁反攻";
   case "spark_of_dawn" -> "暁光共鳴";
   case "sunset_wings" -> "落日の翼";
   case "thief" -> "影盗み";
   case "twinned" -> "双生共鳴";
   case "wandering_wizard" -> "魔力超過";
   case "wind_worshipper" -> "風神加速";
   default -> "固有能力";
  };}
 private static String abilityInfo(String k){return switch(k){
   case "chess_board_knight" -> "被弾時：被ダメ -25% / CT 8秒";
   case "crucible_knight" -> "攻撃時：与ダメ +22% / CT 6秒";
   case "dark_cover" -> "攻撃時：与ダメ +24% / CT 5秒";
   case "dark_lord" -> "HP35%以下：攻撃・防御・移動強化 / CT 12秒";
   case "dead_gladiator" -> "撃破時：HP 30%回復＋攻撃強化 / CT 6秒";
   case "dragonslayer" -> "攻撃時：与ダメ +40% / CT 8秒";
   case "eclipse_soldier" -> "攻撃時：与ダメ +20% / CT 6秒";
   case "evening_ghost" -> "攻撃時：与ダメ +30% / CT 5秒";
   case "flesh_of_the_feaster" -> "撃破時：HP 25%回復＋攻撃強化 / CT 5秒";
   case "fog_guard" -> "被弾時：被ダメ -40% / CT 8秒";
   case "forgotten_trace" -> "被弾時：被ダメ -25% / CT 7秒";
   case "gilded_hunt" -> "攻撃時：与ダメ +25% / CT 5秒";
   case "golden_execution" -> "攻撃時：与ダメ +38% / CT 7秒";
   case "golden_horns" -> "攻撃時：与ダメ +28% / CT 6秒";
   case "grave_sentinel" -> "被弾時：被ダメ -42% / CT 9秒";
   case "hero" -> "HP35%以下：攻撃・防御・移動強化 / CT 11秒";
   case "lady_maria" -> "攻撃時：与ダメ +28% / CT 5秒";
   case "malenia" -> "攻撃時：与ダメ +25% / CT 4秒";
   case "old_knight" -> "被弾時：被ダメ -38% / CT 9秒";
   case "ornstein" -> "攻撃時：与ダメ +30% / CT 4秒";
   case "redeemer" -> "被弾時：被ダメ -32% / CT 8秒";
   case "ronin" -> "攻撃時：与ダメ +32% / CT 5秒";
   case "silver_knight" -> "被弾時：被ダメ -30% / CT 7秒";
   case "spark_of_dawn" -> "HP35%以下：攻撃・防御・移動強化 / CT 10秒";
   case "sunset_wings" -> "攻撃時：与ダメ +24% / CT 5秒";
   case "thief" -> "攻撃時：与ダメ +22% / CT 4秒";
   case "twinned" -> "攻撃時：与ダメ +25% / CT 6秒";
   case "wandering_wizard" -> "HP35%以下：攻撃・防御・移動強化 / CT 10秒";
   case "wind_worshipper" -> "攻撃時：与ダメ +28% / CT 5秒";
   default -> "4/4限定";
  };}
}
