package jp.oishi.armorsets;

import java.util.*;

public final class SetRegistry {
    public record ArmorSet(String key, String name, String school, int tier, Set<String> items) {}
    private static final List<ArmorSet> SETS = new ArrayList<>();
    private static final Map<String, ArmorSet> BY_ITEM = new HashMap<>();
    static {
        // Vanilla armor sets (L2Hostility-oriented physical bonuses)
        add("minecraft:leather", "革装備", "physical", 0, "minecraft:leather_helmet", "minecraft:leather_chestplate", "minecraft:leather_leggings", "minecraft:leather_boots");
        add("minecraft:golden", "金装備", "physical", 0, "minecraft:golden_helmet", "minecraft:golden_chestplate", "minecraft:golden_leggings", "minecraft:golden_boots");
        add("minecraft:chainmail", "チェーン装備", "physical", 0, "minecraft:chainmail_helmet", "minecraft:chainmail_chestplate", "minecraft:chainmail_leggings", "minecraft:chainmail_boots");
        add("minecraft:iron", "鉄装備", "physical", 1, "minecraft:iron_helmet", "minecraft:iron_chestplate", "minecraft:iron_leggings", "minecraft:iron_boots");
        add("minecraft:diamond", "ダイヤモンド装備", "physical", 2, "minecraft:diamond_helmet", "minecraft:diamond_chestplate", "minecraft:diamond_leggings", "minecraft:diamond_boots");
        add("minecraft:netherite", "ネザライト装備", "physical", 3, "minecraft:netherite_helmet", "minecraft:netherite_chestplate", "minecraft:netherite_leggings", "minecraft:netherite_boots");
        add("irons_spellbooks:wandering_magician", "Wandering Magician", "generic", 1, "irons_spellbooks:wandering_magician_helmet", "irons_spellbooks:wandering_magician_chestplate", "irons_spellbooks:wandering_magician_leggings", "irons_spellbooks:wandering_magician_boots");
        add("irons_spellbooks:pyromancer", "Pyromancer", "fire", 1, "irons_spellbooks:pyromancer_helmet", "irons_spellbooks:pyromancer_chestplate", "irons_spellbooks:pyromancer_leggings", "irons_spellbooks:pyromancer_boots");
        add("irons_spellbooks:electromancer", "Electromancer", "lightning", 1, "irons_spellbooks:electromancer_helmet", "irons_spellbooks:electromancer_chestplate", "irons_spellbooks:electromancer_leggings", "irons_spellbooks:electromancer_boots");
        add("irons_spellbooks:cultist", "Cultist", "blood", 1, "irons_spellbooks:cultist_helmet", "irons_spellbooks:cultist_chestplate", "irons_spellbooks:cultist_leggings", "irons_spellbooks:cultist_boots");
        add("irons_spellbooks:archevoker", "Archevoker", "evocation", 1, "irons_spellbooks:archevoker_helmet", "irons_spellbooks:archevoker_chestplate", "irons_spellbooks:archevoker_leggings", "irons_spellbooks:archevoker_boots");
        add("irons_spellbooks:cryomancer", "Cryomancer", "ice", 1, "irons_spellbooks:cryomancer_helmet", "irons_spellbooks:cryomancer_chestplate", "irons_spellbooks:cryomancer_leggings", "irons_spellbooks:cryomancer_boots");
        add("irons_spellbooks:shadowwalker", "Shadow-Walker", "ender", 1, "irons_spellbooks:shadowwalker_helmet", "irons_spellbooks:shadowwalker_chestplate", "irons_spellbooks:shadowwalker_leggings", "irons_spellbooks:shadowwalker_boots");
        add("irons_spellbooks:priest", "Priest", "holy", 1, "irons_spellbooks:priest_helmet", "irons_spellbooks:priest_chestplate", "irons_spellbooks:priest_leggings", "irons_spellbooks:priest_boots");
        add("irons_spellbooks:plagued", "Plagued", "generic", 1, "irons_spellbooks:plagued_helmet", "irons_spellbooks:plagued_chestplate", "irons_spellbooks:plagued_leggings", "irons_spellbooks:plagued_boots");
        add("irons_spellbooks:pumpkin", "Scarecrow", "generic", 1, "irons_spellbooks:pumpkin_helmet", "irons_spellbooks:pumpkin_chestplate", "irons_spellbooks:pumpkin_leggings", "irons_spellbooks:pumpkin_boots");
        add("irons_spellbooks:netherite_mage", "Netherite Battlemage", "generic", 2, "irons_spellbooks:netherite_mage_helmet", "irons_spellbooks:netherite_mage_chestplate", "irons_spellbooks:netherite_mage_leggings", "irons_spellbooks:netherite_mage_boots");
        add("irons_spellbooks:wizard", "Wizard", "generic", 2, "irons_spellbooks:wizard_helmet", "irons_spellbooks:wizard_chestplate", "irons_spellbooks:wizard_leggings", "irons_spellbooks:wizard_boots");
        add("hazennstuff:creaking", "Creaking", "nature", 1, "hazennstuff:creaking_helmet", "hazennstuff:creaking_chestplate", "hazennstuff:creaking_leggings", "hazennstuff:creaking_boots");
        add("hazennstuff:seraph", "Seraph", "holy", 1, "hazennstuff:seraph_helmet", "hazennstuff:seraph_chestplate", "hazennstuff:seraph_leggings", "hazennstuff:seraph_boots");
        add("hazennstuff:legionnaire", "Legionnaire", "generic", 1, "hazennstuff:legionnaire_helmet", "hazennstuff:legionnaire_chestplate", "hazennstuff:legionnaire_leggings", "hazennstuff:legionnaire_boots");
        add("hazennstuff:charged_scourge", "Scourge", "lightning", 1, "hazennstuff:charged_scourge_helmet", "hazennstuff:charged_scourge_chestplate", "hazennstuff:charged_scourge_leggings", "hazennstuff:charged_scourge_boots");
        add("hazennstuff:soul_flame", "Soul Flame", "fire", 1, "hazennstuff:soul_flame_helmet", "hazennstuff:soul_flame_chestplate", "hazennstuff:soul_flame_leggings", "hazennstuff:soul_flame_boots");
        add("hazennstuff:supreme_witch", "Supreme Witch", "eldritch", 3, "hazennstuff:supreme_witch_helmet", "hazennstuff:supreme_witch_chestplate", "hazennstuff:supreme_witch_leggings", "hazennstuff:supreme_witch_boots");
        add("hazennstuff:cryogenic_ruler", "Cryogenic Ruler's", "ice", 3, "hazennstuff:cryogenic_ruler_helmet", "hazennstuff:cryogenic_ruler_chestplate", "hazennstuff:cryogenic_ruler_leggings", "hazennstuff:cryogenic_ruler_boots");
        add("hazennstuff:ender_dragon", "Ender Dragon", "ender", 3, "hazennstuff:ender_dragon_helmet", "hazennstuff:ender_dragon_chestplate", "hazennstuff:ender_dragon_leggings", "hazennstuff:ender_dragon_boots");
        add("hazennstuff:flesh_mass", "Flesh Mass", "blood", 1, "hazennstuff:flesh_mass_helmet", "hazennstuff:flesh_mass_chestplate", "hazennstuff:flesh_mass_leggings", "hazennstuff:flesh_mass_boots");
        add("hazennstuff:dark_ritual_templar", "Some Woman's Hair", "blood", 1, "hazennstuff:dark_ritual_templar_helmet", "hazennstuff:dark_ritual_templar_chestplate", "hazennstuff:dark_ritual_templar_leggings", "hazennstuff:dark_ritual_templar_boots");
        add("hazennstuff:arbitrium_robes", "Arbitrium", "generic", 2, "hazennstuff:arbitrium_robes_helmet", "hazennstuff:arbitrium_robes_chestplate", "hazennstuff:arbitrium_robes_leggings", "hazennstuff:arbitrium_robes_boots");
        add("hazennstuff:ascended_arbitrium_robes", "Ascended Arbitrium", "generic", 3, "hazennstuff:ascended_arbitrium_robes_helmet", "hazennstuff:ascended_arbitrium_robes_chestplate", "hazennstuff:ascended_arbitrium_robes_leggings", "hazennstuff:ascended_arbitrium_robes_boots");
        add("hazennstuff:legacy_fireblossom", "Fireblossom Battlemage", "fire", 1, "hazennstuff:legacy_fireblossom_helmet", "hazennstuff:legacy_fireblossom_chestplate", "hazennstuff:legacy_fireblossom_leggings", "hazennstuff:legacy_fireblossom_boots");
        add("hazennstuff:fireblossom_gown", "Fireblossom", "fire", 1, "hazennstuff:fireblossom_gown_helmet", "hazennstuff:fireblossom_gown_chestplate", "hazennstuff:fireblossom_gown_leggings", "hazennstuff:fireblossom_gown_boots");
        add("hazennstuff:fireblossom_knight", "Fireblossom Knight helmet", "fire", 1, "hazennstuff:fireblossom_knight_helmet", "hazennstuff:fireblossom_knight_chestplate", "hazennstuff:fireblossom_knight_leggings", "hazennstuff:fireblossom_knight_boots");
        add("hazennstuff:fireblossom_battlemage", "Fireblossom Battlemage", "fire", 2, "hazennstuff:fireblossom_battlemage_helmet", "hazennstuff:fireblossom_battlemage_chestplate", "hazennstuff:fireblossom_battlemage_leggings", "hazennstuff:fireblossom_battlemage_boots");
        add("hazennstuff:frieren", "Frieren Hair", "generic", 1, "hazennstuff:frieren_helmet", "hazennstuff:frieren_chestplate", "hazennstuff:frieren_leggings", "hazennstuff:frieren_boots");
        add("hazennstuff:project_sekai", "Miku's Hair", "evocation", 1, "hazennstuff:project_sekai_helmet", "hazennstuff:project_sekai_chestplate", "hazennstuff:project_sekai_leggings", "hazennstuff:project_sekai_boots");
        add("hazennstuff:rotten_girl", "''Miku's'' Hair", "blood", 1, "hazennstuff:rotten_girl_helmet", "hazennstuff:rotten_girl_chestplate", "hazennstuff:rotten_girl_leggings", "hazennstuff:rotten_girl_boots");
        add("hazennstuff:synthesizer_v", "Teto's Hair", "evocation", 1, "hazennstuff:synthesizer_v_helmet", "hazennstuff:synthesizer_v_chestplate", "hazennstuff:synthesizer_v_leggings", "hazennstuff:synthesizer_v_boots");
        add("hazennstuff:utau", "Teto's Hair", "evocation", 1, "hazennstuff:utau_helmet", "hazennstuff:utau_chestplate", "hazennstuff:utau_leggings", "hazennstuff:utau_boots");
        add("hazennstuff:atlas", "Atlas's", "generic", 1, "hazennstuff:atlas_helmet", "hazennstuff:atlas_chestplate", "hazennstuff:atlas_leggings", "hazennstuff:atlas_boots");
        add("hazennstuff:miner", "Miner", "generic", 1, "hazennstuff:miner_helmet", "hazennstuff:miner_chestplate", "hazennstuff:miner_leggings", "hazennstuff:miner_boots");
        add("hazennstuff:spectral_spelunker", "Spectral Spelunker", "generic", 1, "hazennstuff:spectral_spelunker_helmet", "hazennstuff:spectral_spelunker_chestplate", "hazennstuff:spectral_spelunker_leggings", "hazennstuff:spectral_spelunker_boots");
        add("hazennstuff:calamitas", "Calamitas Hair", "fire", 1, "hazennstuff:calamitas_helmet", "hazennstuff:calamitas_chestplate", "hazennstuff:calamitas_leggings", "hazennstuff:calamitas_boots");
        add("hazennstuff:maverick", "Maverick Hair", "generic", 1, "hazennstuff:maverick_helmet", "hazennstuff:maverick_chestplate", "hazennstuff:maverick_leggings", "hazennstuff:maverick_boots");
        add("hazennstuff:slc_cat", "Magic Girl Hair", "generic", 1, "hazennstuff:slc_cat_helmet", "hazennstuff:slc_cat_chestplate", "hazennstuff:slc_cat_leggings", "hazennstuff:slc_cat_boots");
        add("hazennstuff:blazeborne", "Blazeborne", "fire", 1, "hazennstuff:blazeborne_helmet", "hazennstuff:blazeborne_chestplate", "hazennstuff:blazeborne_leggings", "hazennstuff:blazeborne_boots");
        add("hazennstuff:hazel", "Hazel's", "generic", 1, "hazennstuff:hazel_helmet", "hazennstuff:hazel_chestplate", "hazennstuff:hazel_leggings", "hazennstuff:hazel_boots");
        add("hazennstuff:mothic_witch", "Mothic Witch", "eldritch", 1, "hazennstuff:mothic_witch_helmet", "hazennstuff:mothic_witch_chestplate", "hazennstuff:mothic_witch_leggings", "hazennstuff:mothic_witch_boots");
        add("hazennstuff:crystal_arachnid", "Ice Spider", "generic", 1, "hazennstuff:crystal_arachnid_helmet", "hazennstuff:crystal_arachnid_chestplate", "hazennstuff:crystal_arachnid_leggings", "hazennstuff:crystal_arachnid_boots");
        add("hazennstuff:herta_puppet", "Herta's Puppet's Hair", "generic", 1, "hazennstuff:herta_puppet_helmet", "hazennstuff:herta_puppet_chestplate", "hazennstuff:herta_puppet_leggings", "hazennstuff:herta_puppet_boots");
        add("hazennstuff:thunder_prowler", "Thunder Prowler", "lightning", 1, "hazennstuff:thunder_prowler_helmet", "hazennstuff:thunder_prowler_chestplate", "hazennstuff:thunder_prowler_leggings", "hazennstuff:thunder_prowler_boots");
        add("hazennstuff:chlorophyte", "Chlorophyte", "nature", 1, "hazennstuff:chlorophyte_helmet", "hazennstuff:chlorophyte_chestplate", "hazennstuff:chlorophyte_leggings", "hazennstuff:chlorophyte_boots");
        add("hazennstuff:the_wither", "The Wither", "generic", 1, "hazennstuff:the_wither_helmet", "hazennstuff:the_wither_chestplate", "hazennstuff:the_wither_leggings", "hazennstuff:the_wither_boots");
        add("hazennstuff:dead_king", "Dead King Skull", "generic", 3, "hazennstuff:dead_king_helmet", "hazennstuff:dead_king_chestplate", "hazennstuff:dead_king_leggings", "hazennstuff:dead_king_boots");
        add("hazennstuff:gabriel_ultrakill", "Gabriel's", "holy", 1, "hazennstuff:gabriel_ultrakill_helmet", "hazennstuff:gabriel_ultrakill_chestplate", "hazennstuff:gabriel_ultrakill_leggings", "hazennstuff:gabriel_ultrakill_boots");
        add("hazennstuff:alchemist_supreme", "Alchemist Supreme", "generic", 3, "hazennstuff:alchemist_supreme_helmet", "hazennstuff:alchemist_supreme_chestplate", "hazennstuff:alchemist_supreme_leggings", "hazennstuff:alchemist_supreme_boots");
        add("hazennstuff:mycelium_guardian", "Mycelium Guardian", "nature", 1, "hazennstuff:mycelium_guardian_helmet", "hazennstuff:mycelium_guardian_chestplate", "hazennstuff:mycelium_guardian_leggings", "hazennstuff:mycelium_guardian_boots");
        add("hazennstuff:radiant_crystal", "Radiant Crystal Ball", "holy", 1, "hazennstuff:radiant_crystal_helmet", "hazennstuff:radiant_crystal_chestplate", "hazennstuff:radiant_crystal_leggings", "hazennstuff:radiant_crystal_boots");
        add("hazennstuff:glassweaver", "Glassweaver", "generic", 1, "hazennstuff:glassweaver_helmet", "hazennstuff:glassweaver_chestplate", "hazennstuff:glassweaver_leggings", "hazennstuff:glassweaver_boots");
        add("hazennstuff:shadow_scale", "Shadow Scale", "ender", 1, "hazennstuff:shadow_scale_helmet", "hazennstuff:shadow_scale_chestplate", "hazennstuff:shadow_scale_leggings", "hazennstuff:shadow_scale_boots");
        add("hazennstuff:masked_fool", "Masked Fool", "generic", 1, "hazennstuff:masked_fool_helmet", "hazennstuff:masked_fool_chestplate", "hazennstuff:masked_fool_leggings", "hazennstuff:masked_fool_boots");
        add("hazennstuff:shadow_jester", "Shadow Jester", "ender", 1, "hazennstuff:shadow_jester_helmet", "hazennstuff:shadow_jester_chestplate", "hazennstuff:shadow_jester_leggings", "hazennstuff:shadow_jester_boots");
        add("hazennstuff:cosmic_scholar", "Cosmic Scholar", "ender", 1, "hazennstuff:cosmic_scholar_helmet", "hazennstuff:cosmic_scholar_chestplate", "hazennstuff:cosmic_scholar_leggings", "hazennstuff:cosmic_scholar_boots");
        add("hazennstuff:astraconic_weaver", "Astraconic Weaver", "ender", 1, "hazennstuff:astraconic_weaver_helmet", "hazennstuff:astraconic_weaver_chestplate", "hazennstuff:astraconic_weaver_leggings", "hazennstuff:astraconic_weaver_boots");
        add("hazennstuff:nautilus_knight", "Nautilus Knight", "nature", 1, "hazennstuff:nautilus_knight_helmet", "hazennstuff:nautilus_knight_chestplate", "hazennstuff:nautilus_knight_leggings", "hazennstuff:nautilus_knight_boots");
        add("hazennstuff:elder_guardian", "Elder Guardian", "nature", 1, "hazennstuff:elder_guardian_helmet", "hazennstuff:elder_guardian_chestplate", "hazennstuff:elder_guardian_leggings", "hazennstuff:elder_guardian_boots");
        add("hazennstuff:infestation", "Infestation", "nature", 1, "hazennstuff:infestation_helmet", "hazennstuff:infestation_chestplate", "hazennstuff:infestation_leggings", "hazennstuff:infestation_boots");
        add("hazennstuff:pyrium", "Pyrium", "fire", 1, "hazennstuff:pyrium_helmet", "hazennstuff:pyrium_chestplate", "hazennstuff:pyrium_leggings", "hazennstuff:pyrium_boots");
        add("hazennstuff:pyrium_battlemage", "Pyrium Battlemage", "fire", 2, "hazennstuff:pyrium_battlemage_helmet", "hazennstuff:pyrium_battlemage_chestplate", "hazennstuff:pyrium_battlemage_leggings", "hazennstuff:pyrium_battlemage_boots");
        add("hazennstuff:legionnaire_ruler", "Legionnaire Ruler", "generic", 3, "hazennstuff:legionnaire_ruler_helmet", "hazennstuff:legionnaire_ruler_chestplate", "hazennstuff:legionnaire_ruler_leggings", "hazennstuff:legionnaire_ruler_boots");
        add("hazennstuff:soul_legionnaire_ruler", "Soul Legionnaire Ruler", "generic", 3, "hazennstuff:soul_legionnaire_ruler_helmet", "hazennstuff:soul_legionnaire_ruler_chestplate", "hazennstuff:soul_legionnaire_ruler_leggings", "hazennstuff:soul_legionnaire_ruler_boots");
        add("hazennstuff:legionnaire_commander", "Legionnaire Commander", "generic", 3, "hazennstuff:legionnaire_commander_helmet", "hazennstuff:legionnaire_commander_chestplate", "hazennstuff:legionnaire_commander_leggings", "hazennstuff:legionnaire_commander_boots");
        add("hazennstuff:soul_legionnaire_commander", "Soul Legionnaire Commander", "generic", 3, "hazennstuff:soul_legionnaire_commander_helmet", "hazennstuff:soul_legionnaire_commander_chestplate", "hazennstuff:soul_legionnaire_commander_leggings", "hazennstuff:soul_legionnaire_commander_boots");
        add("hazennstuff:garments_of_the_first_flamebearer", "Tyros", "fire", 3, "hazennstuff:garments_of_the_first_flamebearer_helmet", "hazennstuff:garments_of_the_first_flamebearer_chestplate", "hazennstuff:garments_of_the_first_flamebearer_leggings", "hazennstuff:garments_of_the_first_flamebearer_boots");
        add("hazennstuff:garments_of_the_first_flamebearer_soul", "Soul Tyros", "fire", 3, "hazennstuff:garments_of_the_first_flamebearer_soul_helmet", "hazennstuff:garments_of_the_first_flamebearer_soul_chestplate", "hazennstuff:garments_of_the_first_flamebearer_soul_leggings", "hazennstuff:garments_of_the_first_flamebearer_soul_boots");
        add("hazennstuff:neru", "Neru's Hair", "generic", 1, "hazennstuff:neru_helmet", "hazennstuff:neru_chestplate", "hazennstuff:neru_leggings", "hazennstuff:neru_boots");
        add("hazennstuff:giorno_giovanna", "Giorno Giovanna's Hair", "generic", 1, "hazennstuff:giorno_giovanna_helmet", "hazennstuff:giorno_giovanna_chestplate", "hazennstuff:giorno_giovanna_leggings", "hazennstuff:giorno_giovanna_boots");
        add("hazennstuff:frostbite_knight", "Frostbite Knight", "ice", 1, "hazennstuff:frostbite_knight_helmet", "hazennstuff:frostbite_knight_chestplate", "hazennstuff:frostbite_knight_leggings", "hazennstuff:frostbite_knight_boots");
        add("hazennstuff:dreadsteel_knight", "Dreadsteel Knight", "generic", 2, "hazennstuff:dreadsteel_knight_helmet", "hazennstuff:dreadsteel_knight_chestplate", "hazennstuff:dreadsteel_knight_leggings", "hazennstuff:dreadsteel_knight_boots");
        add("hazennstuff:bounty_hunter", "Bounty Hunter", "generic", 1, "hazennstuff:bounty_hunter_helmet", "hazennstuff:bounty_hunter_chestplate", "hazennstuff:bounty_hunter_leggings", "hazennstuff:bounty_hunter_boots");
        add("hazennstuff:frostbite_hunter", "Frostbite Hunter Hair", "ice", 1, "hazennstuff:frostbite_hunter_helmet", "hazennstuff:frostbite_hunter_chestplate", "hazennstuff:frostbite_hunter_leggings", "hazennstuff:frostbite_hunter_boots");
        add("hazennstuff:permafrost_prince", "Permafrost Prince", "ice", 1, "hazennstuff:permafrost_prince_helmet", "hazennstuff:permafrost_prince_chestplate", "hazennstuff:permafrost_prince_leggings", "hazennstuff:permafrost_prince_boots");
        add("hazennstuff:magehunter", "Magehunter", "generic", 2, "hazennstuff:magehunter_helmet", "hazennstuff:magehunter_chestplate", "hazennstuff:magehunter_leggings", "hazennstuff:magehunter_boots");
        add("hazennstuff:mithril_battlemage", "Mithril Battlemage", "generic", 2, "hazennstuff:mithril_battlemage_helmet", "hazennstuff:mithril_battlemage_chestplate", "hazennstuff:mithril_battlemage_leggings", "hazennstuff:mithril_battlemage_boots");
        add("hazennstuff:abberant_predator", "Abberant Predator", "eldritch", 1, "hazennstuff:abberant_predator_helmet", "hazennstuff:abberant_predator_chestplate", "hazennstuff:abberant_predator_leggings", "hazennstuff:abberant_predator_boots");
        add("hazennstuff:iron431", "Iron431", "generic", 2, "hazennstuff:iron431_helmet", "hazennstuff:iron431_chestplate", "hazennstuff:iron431_leggings", "hazennstuff:iron431_boots");
        add("hazennstuff:sacred_robes", "Sacred Robes", "holy", 2, "hazennstuff:sacred_robes_helmet", "hazennstuff:sacred_robes_chestplate", "hazennstuff:sacred_robes_leggings", "hazennstuff:sacred_robes_boots");
        add("hazennstuff:ascended_iron431", "Ascended Iron431", "generic", 3, "hazennstuff:ascended_iron431_helmet", "hazennstuff:ascended_iron431_chestplate", "hazennstuff:ascended_iron431_leggings", "hazennstuff:ascended_iron431_boots");
        add("hazennstuff:ascended_sacred_robes", "Ascended Sacred Robes", "holy", 3, "hazennstuff:ascended_sacred_robes_helmet", "hazennstuff:ascended_sacred_robes_chestplate", "hazennstuff:ascended_sacred_robes_leggings", "hazennstuff:ascended_sacred_robes_boots");
        add("hazennstuff:lemon_god", "Lemon God", "generic", 3, "hazennstuff:lemon_god_helmet", "hazennstuff:lemon_god_chestplate", "hazennstuff:lemon_god_leggings", "hazennstuff:lemon_god_boots");
        add("hazennstuff:ascended_lemon_god", "Ascended Lemon God", "generic", 3, "hazennstuff:ascended_lemon_god_helmet", "hazennstuff:ascended_lemon_god_chestplate", "hazennstuff:ascended_lemon_god_leggings", "hazennstuff:ascended_lemon_god_boots");
        add("cataclysm:ignitium", "Ignitium", "physical", 3, "cataclysm:ignitium_helmet", "cataclysm:ignitium_chestplate", "cataclysm:ignitium_leggings", "cataclysm:ignitium_boots");
        add("cataclysm:cursium", "Cursium", "physical", 3, "cataclysm:cursium_helmet", "cataclysm:cursium_chestplate", "cataclysm:cursium_leggings", "cataclysm:cursium_boots");
        add("gobber2:gobber", "Gobber", "physical", 1, "gobber2:gobber2_helmet", "gobber2:gobber2_chestplate", "gobber2:gobber2_leggings", "gobber2:gobber2_boots");
        add("gobber2:gobber_nether", "Gobber Nether", "physical", 2, "gobber2:gobber2_helmet_nether", "gobber2:gobber2_chestplate_nether", "gobber2:gobber2_leggings_nether", "gobber2:gobber2_boots_nether");
        add("gobber2:gobber_end", "Gobber End", "physical", 3, "gobber2:gobber2_helmet_end", "gobber2:gobber2_chestplate_end", "gobber2:gobber2_leggings_end", "gobber2:gobber2_boots_end");
        add("gobber2:gobber_dragon", "Gobber Dragon", "physical", 4, "gobber2:gobber2_helmet_dragon", "gobber2:gobber2_chestplate_dragon", "gobber2:gobber2_leggings_dragon", "gobber2:gobber2_boots_dragon");
        add("cataclysm_spellbooks:ignis", "Ignis Mage", "fire", 2, "cataclysm_spellbooks:ignis_helmet", "cataclysm_spellbooks:ignis_chestplate", "cataclysm_spellbooks:ignis_leggings", "cataclysm_spellbooks:ignis_boots");
        add("cataclysm_spellbooks:abyssal_warlock", "Abyssal Warlock", "eldritch", 2, "cataclysm_spellbooks:abyssal_warlock_helmet", "cataclysm_spellbooks:abyssal_warlock_chestplate", "cataclysm_spellbooks:abyssal_warlock_leggings", "cataclysm_spellbooks:abyssal_warlock_boots");
        add("cataclysm_spellbooks:cursium_mage", "Cursium Mage", "generic", 2, "cataclysm_spellbooks:cursium_mage_circlet", "cataclysm_spellbooks:cursium_mage_chestplate", "cataclysm_spellbooks:cursium_mage_skirt", "cataclysm_spellbooks:cursium_mage_boots");
        add("cataclysm_spellbooks:pharaoh", "Pharaoh Sacred", "generic", 2, "cataclysm_spellbooks:pharaoh_helmet", "cataclysm_spellbooks:pharaoh_chestplate", "cataclysm_spellbooks:pharaoh_leggings", "cataclysm_spellbooks:pharaoh_greaves");
        add("cataclysm_spellbooks:bloom_stone", "Bloom Stone", "nature", 1, "cataclysm_spellbooks:bloom_stone_hat", "cataclysm_spellbooks:bloom_stone_chestplate", "cataclysm_spellbooks:bloom_stone_skirt", "cataclysm_spellbooks:bloom_stone_greaves");
        add("cataclysm_spellbooks:engineer", "Engineer", "evocation", 1, "cataclysm_spellbooks:engineer_hood", "cataclysm_spellbooks:engineer_suit", "cataclysm_spellbooks:engineer_leggings", "cataclysm_spellbooks:engineer_boots");
        add("discerning_the_eldritch:apothic_acolyte", "Apothic Acolyte", "eldritch", 1, "discerning_the_eldritch:eldritch_warlock_hood", "discerning_the_eldritch:eldritch_warlock_robes", "discerning_the_eldritch:eldritch_warlock_leggings", "discerning_the_eldritch:eldritch_warlock_greaves");
        add("discerning_the_eldritch:crimson_stag", "Crimson Stag", "eldritch", 1, "discerning_the_eldritch:crimson_stag_antlers", "discerning_the_eldritch:crimson_stag_robes", "discerning_the_eldritch:crimson_stag_leggings", "discerning_the_eldritch:crimson_stag_boots");
    }
    private static void add(String key,String name,String school,int tier,String... items){
        ArmorSet s=new ArmorSet(key,name,school,tier,Set.of(items)); SETS.add(s); for(String i:items) BY_ITEM.put(i,s);
    }
    public static ArmorSet byItem(String id){ return BY_ITEM.get(id); }
    public static List<ArmorSet> all(){ return Collections.unmodifiableList(SETS); }
    private SetRegistry(){}
}
