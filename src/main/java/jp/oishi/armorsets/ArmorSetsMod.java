package jp.oishi.armorsets;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
@Mod(ArmorSetsMod.MODID)
public final class ArmorSetsMod {
 public static final String MODID="l2h_equipment_overhaul";
 public ArmorSetsMod(){
  NeoForge.EVENT_BUS.register(SetBonuses.class);
  NeoForge.EVENT_BUS.register(CombatAbilities.class);
  NeoForge.EVENT_BUS.register(jp.chatgpt.fantasyarmoriss.ArmorSetHandler.class);
  NeoForge.EVENT_BUS.register(jp.chatgpt.fantasyarmoriss.CombatAbilities.class);
  NeoForge.EVENT_BUS.register(jp.chatgpt.fantasyarmoriss.ArmorTooltipHandler.class);
 }
}
