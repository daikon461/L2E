package jp.chatgpt.fantasyarmoriss;
import java.util.*;
public final class ArmorSetRegistry {
 public enum Profile { AERIAL, ASSASSIN, BERSERKER, BLOOD_HYBRID, BRUISER, CHARGER, DARK_HYBRID, DUELIST, EXECUTIONER, FORTRESS, HEAVY_SLAYER, HEROIC, HOLY_HYBRID, HUNTER, LANCER, OLD_GUARD, PALADIN, PURE_MAGE, RELIC_HYBRID, RONIN, SENTINEL, SHADOW_BRUISER, SILVER_GUARD, SOLDIER, TACTIC_TANK, THIEF, TWINNED, VAMPIRIC_BRUISER, WIND_MAGE }
 public record SetDef(String key, Profile profile, String helmet,String chestplate,String leggings,String boots){}
 private static final Map<String,SetDef> SETS=new LinkedHashMap<>();
 static {
  add("chess_board_knight",Profile.TACTIC_TANK);
  add("crucible_knight",Profile.BRUISER);
  add("dark_cover",Profile.SHADOW_BRUISER);
  add("dark_lord",Profile.DARK_HYBRID);
  add("dead_gladiator",Profile.BERSERKER);
  add("dragonslayer",Profile.HEAVY_SLAYER);
  add("eclipse_soldier",Profile.SOLDIER);
  add("evening_ghost",Profile.ASSASSIN);
  add("flesh_of_the_feaster",Profile.VAMPIRIC_BRUISER);
  add("fog_guard",Profile.FORTRESS);
  add("forgotten_trace",Profile.RELIC_HYBRID);
  add("gilded_hunt",Profile.HUNTER);
  add("golden_execution",Profile.EXECUTIONER);
  add("golden_horns",Profile.CHARGER);
  add("grave_sentinel",Profile.SENTINEL);
  add("hero",Profile.HEROIC);
  add("lady_maria",Profile.BLOOD_HYBRID);
  add("malenia",Profile.DUELIST);
  add("old_knight",Profile.OLD_GUARD);
  add("ornstein",Profile.LANCER);
  add("redeemer",Profile.PALADIN);
  add("ronin",Profile.RONIN);
  add("silver_knight",Profile.SILVER_GUARD);
  add("spark_of_dawn",Profile.HOLY_HYBRID);
  add("sunset_wings",Profile.AERIAL);
  add("thief",Profile.THIEF);
  add("twinned",Profile.TWINNED);
  add("wandering_wizard",Profile.PURE_MAGE);
  add("wind_worshipper",Profile.WIND_MAGE);
 }
 private static void add(String k,Profile p){SETS.put(k,new SetDef(k,p,
  "fantasy_armor:"+k+"_helmet","fantasy_armor:"+k+"_chestplate","fantasy_armor:"+k+"_leggings","fantasy_armor:"+k+"_boots"));}
 public static Collection<SetDef> all(){return SETS.values();}
 private ArmorSetRegistry(){}
}
