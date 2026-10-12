package jp.chatgpt.fantasyarmoriss;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import java.util.*;

public final class ArmorSetHandler {
 private static final String NS="fantasy_armor_iss";
 @SubscribeEvent public static void tick(PlayerTickEvent.Post e){
  Player p=e.getEntity(); if(p.level().isClientSide())return;
  clear(p);
  for(var s:ArmorSetRegistry.all()){int n=count(p,s); if(n>=2)apply(p,s,n);}
 }
 private static int count(Player p,ArmorSetRegistry.SetDef s){
  int n=0;
  if(id(p.getItemBySlot(EquipmentSlot.HEAD)).equals(s.helmet()))n++;
  if(id(p.getItemBySlot(EquipmentSlot.CHEST)).equals(s.chestplate()))n++;
  if(id(p.getItemBySlot(EquipmentSlot.LEGS)).equals(s.leggings()))n++;
  if(id(p.getItemBySlot(EquipmentSlot.FEET)).equals(s.boots()))n++;
  return n;
 }
 private static String id(net.minecraft.world.item.ItemStack st){var k=BuiltInRegistries.ITEM.getKey(st.getItem());return k==null?"":k.toString();}
 private static void apply(Player p,ArmorSetRegistry.SetDef s,int n){
  switch(s.profile()){
case TACTIC_TANK -> {
 tier(p,s,n, 0.07200, 0.04350, 0.20000, 3.60000, 3.60000, 0.02500, 0.18750, 0.00000, 0.00000, 0.00000, 0.00000);
}
case BRUISER -> {
 tier(p,s,n, 0.28800, 0.04350, 0.16000, 1.80000, 3.60000, 0.00000, 0.12500, 0.00000, 0.00000, 0.00000, 0.00000);
}
case SHADOW_BRUISER -> {
 tier(p,s,n, 0.25200, 0.11600, 0.10000, 0.00000, 1.80000, 0.08750, 0.00000, 0.07400, 0.00000, 0.00000, 0.00000);
}
case DARK_HYBRID -> {
 tier(p,s,n, 0.19800, 0.07250, 0.14000, 0.00000, 1.80000, 0.03750, 0.00000, 0.25900, 0.21000, 0.13600, 0.06750);
}
case BERSERKER -> {
 tier(p,s,n, 0.39600, 0.14500, 0.08000, 0.00000, 0.00000, 0.03750, 0.00000, 0.00000, 0.00000, 0.00000, 0.00000);
}
case HEAVY_SLAYER -> {
 tier(p,s,n, 0.45000, -0.05800, 0.16000, 1.80000, 5.40000, -0.02500, 0.25000, 0.00000, 0.00000, 0.00000, 0.00000);
}
case SOLDIER -> {
 tier(p,s,n, 0.21600, 0.08700, 0.16000, 1.80000, 1.80000, 0.03750, 0.10000, 0.00000, 0.00000, 0.00000, 0.00000);
}
case ASSASSIN -> {
 tier(p,s,n, 0.25200, 0.23200, 0.00000, 0.00000, 0.00000, 0.16250, 0.00000, 0.00000, 0.00000, 0.00000, 0.00000);
}
case VAMPIRIC_BRUISER -> {
 tier(p,s,n, 0.27000, 0.07250, 0.28000, 0.00000, 1.80000, 0.02500, 0.00000, 0.07400, 0.00000, 0.00000, 0.00000);
}
case FORTRESS -> {
 tier(p,s,n, 0.05400, 0.00000, 0.44000, 7.20000, 7.20000, -0.03750, 0.37500, 0.00000, 0.00000, 0.00000, 0.00000);
}
case RELIC_HYBRID -> {
 tier(p,s,n, 0.14400, 0.05800, 0.16000, 1.80000, 1.80000, 0.03750, 0.00000, 0.20350, 0.17500, 0.13600, 0.05400);
}
case HUNTER -> {
 tier(p,s,n, 0.21600, 0.17400, 0.06000, 0.00000, 0.00000, 0.12500, 0.00000, 0.00000, 0.00000, 0.00000, 0.00000);
}
case EXECUTIONER -> {
 tier(p,s,n, 0.43200, -0.02900, 0.12000, 0.00000, 3.60000, 0.00000, 0.15000, 0.00000, 0.00000, 0.00000, 0.00000);
}
case CHARGER -> {
 tier(p,s,n, 0.27000, 0.05800, 0.16000, 1.80000, 1.80000, 0.12500, 0.18750, 0.00000, 0.00000, 0.00000, 0.00000);
}
case SENTINEL -> {
 tier(p,s,n, 0.10800, 0.02900, 0.36000, 5.40000, 5.40000, 0.00000, 0.31250, 0.00000, 0.00000, 0.00000, 0.00000);
}
case HEROIC -> {
 tier(p,s,n, 0.27000, 0.11600, 0.24000, 1.80000, 3.60000, 0.06250, 0.12500, 0.00000, 0.00000, 0.00000, 0.00000);
}
case BLOOD_HYBRID -> {
 tier(p,s,n, 0.18000, 0.17400, 0.12000, 0.00000, 1.80000, 0.10000, 0.00000, 0.24050, 0.14000, 0.17000, 0.05400);
}
case DUELIST -> {
 tier(p,s,n, 0.30600, 0.26100, 0.06000, 0.00000, 0.00000, 0.12500, 0.00000, 0.00000, 0.00000, 0.00000, 0.00000);
}
case OLD_GUARD -> {
 tier(p,s,n, 0.16200, 0.02900, 0.32000, 3.60000, 5.40000, -0.01250, 0.25000, 0.00000, 0.00000, 0.00000, 0.00000);
}
case LANCER -> {
 tier(p,s,n, 0.28800, 0.29000, 0.08000, 0.00000, 1.80000, 0.11250, 0.06250, 0.00000, 0.00000, 0.00000, 0.00000);
}
case PALADIN -> {
 tier(p,s,n, 0.16200, 0.04350, 0.30000, 3.60000, 3.60000, 0.00000, 0.22500, 0.14800, 0.17500, 0.13600, 0.04050);
}
case RONIN -> {
 tier(p,s,n, 0.32400, 0.20300, 0.06000, 0.00000, 0.00000, 0.13750, 0.00000, 0.00000, 0.00000, 0.00000, 0.00000);
}
case SILVER_GUARD -> {
 tier(p,s,n, 0.19800, 0.07250, 0.24000, 3.60000, 3.60000, 0.02500, 0.18750, 0.00000, 0.00000, 0.00000, 0.00000);
}
case HOLY_HYBRID -> {
 tier(p,s,n, 0.14400, 0.07250, 0.20000, 1.80000, 1.80000, 0.05000, 0.10000, 0.27750, 0.24500, 0.20400, 0.08100);
}
case AERIAL -> {
 tier(p,s,n, 0.18000, 0.17400, 0.08000, 0.00000, 0.00000, 0.20000, 0.00000, 0.11100, 0.08750, 0.00000, 0.00000);
}
case THIEF -> {
 tier(p,s,n, 0.18000, 0.29000, 0.00000, 0.00000, 0.00000, 0.22500, 0.00000, 0.00000, 0.00000, 0.00000, 0.00000);
}
case TWINNED -> {
 tier(p,s,n, 0.23400, 0.14500, 0.18000, 1.80000, 1.80000, 0.06250, 0.10000, 0.12950, 0.10500, 0.06800, 0.02700);
}
case PURE_MAGE -> {
 tier(p,s,n, 0.00000, 0.00000, 0.08000, 0.00000, 0.00000, 0.03750, 0.00000, 0.51800, 0.52500, 0.37400, 0.16200);
}
case WIND_MAGE -> {
 tier(p,s,n, 0.05400, 0.08700, 0.06000, 0.00000, 0.00000, 0.22500, 0.00000, 0.40700, 0.35000, 0.30600, 0.13500);
}
  }
 }
 // Totals are intentionally distributed 35% / 30% / 35% across 2p,3p,4p.
 private static void tier(Player p,ArmorSetRegistry.SetDef s,int n,double atk,double asp,double hp,double armor,double tough,double move,double kb,double spell,double mana,double regen,double cd){
  if(n>=2) stats(p,s.key()+"_2",.35,atk,asp,hp,armor,tough,move,kb,spell,mana,regen,cd);
  if(n>=3) stats(p,s.key()+"_3",.30,atk,asp,hp,armor,tough,move,kb,spell,mana,regen,cd);
  if(n>=4) stats(p,s.key()+"_4",.35,atk,asp,hp,armor,tough,move,kb,spell,mana,regen,cd);
 }
 private static void stats(Player p,String k,double q,double atk,double asp,double hp,double armor,double tough,double move,double kb,double spell,double mana,double regen,double cd){
  mult(p,Attributes.ATTACK_DAMAGE,k+"_atk",atk*q); mult(p,Attributes.ATTACK_SPEED,k+"_asp",asp*q);
  mult(p,Attributes.MAX_HEALTH,k+"_hp",hp*q); value(p,Attributes.ARMOR,k+"_armor",armor*q);
  value(p,Attributes.ARMOR_TOUGHNESS,k+"_tough",tough*q); mult(p,Attributes.MOVEMENT_SPEED,k+"_move",move*q);
  mult(p,Attributes.KNOCKBACK_RESISTANCE,k+"_kb",kb*q);
  magic(p,"spell_power",k+"_spell",spell*q); magic(p,"max_mana",k+"_mana",mana*q);
  magic(p,"mana_regen",k+"_regen",regen*q); magic(p,"cooldown_reduction",k+"_cd",cd*q);
 }
 private static void clear(Player p){
  for(var h:p.getAttributes().getSyncableAttributes()){
   var i=p.getAttribute(h.getAttribute()); if(i==null)continue;
   var ids=new ArrayList<ResourceLocation>();
   for(var m:i.getModifiers())if(m.id().getNamespace().equals(NS))ids.add(m.id());
   for(var id:ids)i.removeModifier(id);
  }
 }
 private static void mult(Player p,Holder<Attribute>a,String id,double x){if(x!=0)add(p,a,id,x,AttributeModifier.Operation.ADD_MULTIPLIED_BASE);}
 private static void value(Player p,Holder<Attribute>a,String id,double x){if(x!=0)add(p,a,id,x,AttributeModifier.Operation.ADD_VALUE);}
 private static void add(Player p,Holder<Attribute>a,String id,double x,AttributeModifier.Operation op){
  var i=p.getAttribute(a);if(i==null)return;var rl=ResourceLocation.fromNamespaceAndPath(NS,id);
  if(i.getModifier(rl)==null)i.addTransientModifier(new AttributeModifier(rl,x,op));
 }
 private static void magic(Player p,String path,String id,double x){
  if(x==0)return; var key=ResourceLocation.fromNamespaceAndPath("irons_spellbooks",path);
  var h=BuiltInRegistries.ATTRIBUTE.getHolder(key);if(h.isPresent())add(p,h.get(),id,x,AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
 }
}
