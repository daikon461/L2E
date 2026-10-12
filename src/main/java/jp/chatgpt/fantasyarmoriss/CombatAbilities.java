package jp.chatgpt.fantasyarmoriss;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import java.util.*;

public final class CombatAbilities {
 private record Ability(String name,String trigger,int cooldown,double power){}
 private static final Map<String,Ability> A=new LinkedHashMap<>();
 private static final Map<UUID,Map<String,Long>> CD=new HashMap<>();
 static {
  add("chess_board_knight","王手の構え","DEFENSE",160,0.25);
  add("crucible_knight","坩堝の猛進","OFFENSE",120,0.22);
  add("dark_cover","深闇の追撃","OFFENSE",100,0.24);
  add("dark_lord","暗黒支配","LOW_HP",240,0.35);
  add("dead_gladiator","死闘の昂揚","KILL",120,0.3);
  add("dragonslayer","竜殺しの一撃","OFFENSE",160,0.4);
  add("eclipse_soldier","蝕の戦陣","OFFENSE",120,0.2);
  add("evening_ghost","黄昏の奇襲","OFFENSE",100,0.3);
  add("flesh_of_the_feaster","飽食の血肉","KILL",100,0.25);
  add("fog_guard","霧城の防壁","DEFENSE",160,0.4);
  add("forgotten_trace","忘却の残響","DEFENSE",140,0.25);
  add("gilded_hunt","黄金狩猟","OFFENSE",100,0.25);
  add("golden_execution","黄金処刑","OFFENSE",140,0.38);
  add("golden_horns","黄金角の突撃","OFFENSE",120,0.28);
  add("grave_sentinel","墓守の誓い","DEFENSE",180,0.42);
  add("hero","英雄の奮起","LOW_HP",220,0.4);
  add("lady_maria","血刃乱舞","OFFENSE",100,0.28);
  add("malenia","不敗の剣舞","OFFENSE",80,0.25);
  add("old_knight","古騎士の不屈","DEFENSE",180,0.38);
  add("ornstein","雷槍疾駆","OFFENSE",90,0.3);
  add("redeemer","贖罪の加護","DEFENSE",160,0.32);
  add("ronin","一閃","OFFENSE",100,0.32);
  add("silver_knight","銀壁反攻","DEFENSE",140,0.3);
  add("spark_of_dawn","暁光共鳴","LOW_HP",200,0.34);
  add("sunset_wings","落日の翼","OFFENSE",100,0.24);
  add("thief","影盗み","OFFENSE",80,0.22);
  add("twinned","双生共鳴","OFFENSE",120,0.25);
  add("wandering_wizard","魔力超過","LOW_HP",200,0.38);
  add("wind_worshipper","風神加速","OFFENSE",100,0.28);
 }
 private static void add(String k,String n,String t,int cd,double p){A.put(k,new Ability(n,t,cd,p));}

 @SubscribeEvent public static void damage(LivingDamageEvent.Pre e){
  // Defender abilities
  if(e.getEntity() instanceof Player defender){
   String set=fullSet(defender); Ability a=A.get(set);
   if(a!=null && a.trigger.equals("DEFENSE") && ready(defender,set,a.cooldown)){
    e.setNewDamage(Math.max(0f,(float)(e.getNewDamage()*(1.0-a.power))));
    proc(defender,set,a);
   }
   if(a!=null && a.trigger.equals("LOW_HP") && defender.getHealth() <= defender.getMaxHealth()*0.35f && ready(defender,set,a.cooldown)){
    defender.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,100,1));
    defender.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,100,1));
    defender.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,100,1));
    proc(defender,set,a);
   }
  }
  // Attacker abilities: modify final post-armor damage in a controlled way.
  if(e.getSource().getEntity() instanceof Player attacker){
   String set=fullSet(attacker); Ability a=A.get(set);
   if(a!=null && a.trigger.equals("OFFENSE") && ready(attacker,set,a.cooldown)){
    e.setNewDamage((float)(e.getNewDamage()*(1.0+a.power)));
    if(set.equals("ornstein")||set.equals("wind_worshipper"))
      attacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,80,1));
    if(set.equals("lady_maria")||set.equals("malenia"))
      attacker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,60,0));
    proc(attacker,set,a);
   }
  }
 }

 @SubscribeEvent public static void death(LivingDeathEvent e){
  if(e.getSource().getEntity() instanceof Player p){
   String set=fullSet(p); Ability a=A.get(set);
   if(a!=null && a.trigger.equals("KILL") && ready(p,set,a.cooldown)){
    p.heal((float)(p.getMaxHealth()*a.power));
    p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,100,1));
    proc(p,set,a);
   }
  }
 }

 private static boolean ready(Player p,String set,int cd){
  long now=p.level().getGameTime();
  long last=CD.computeIfAbsent(p.getUUID(),x->new HashMap<>()).getOrDefault(set,Long.MIN_VALUE/2);
  return now-last>=cd;
 }
 private static void proc(Player p,String set,Ability a){
  CD.computeIfAbsent(p.getUUID(),x->new HashMap<>()).put(set,p.level().getGameTime());
  if(p instanceof ServerPlayer sp) sp.displayClientMessage(Component.literal("§6◆ "+a.name+" §7発動"),true);
 }
 private static String fullSet(Player p){
  for(var s:ArmorSetRegistry.all()){
   if(id(p.getItemBySlot(EquipmentSlot.HEAD)).equals(s.helmet()) &&
      id(p.getItemBySlot(EquipmentSlot.CHEST)).equals(s.chestplate()) &&
      id(p.getItemBySlot(EquipmentSlot.LEGS)).equals(s.leggings()) &&
      id(p.getItemBySlot(EquipmentSlot.FEET)).equals(s.boots())) return s.key();
  }
  return "";
 }
 private static String id(net.minecraft.world.item.ItemStack st){
  var k=BuiltInRegistries.ITEM.getKey(st.getItem()); return k==null?"":k.toString();
 }
}
