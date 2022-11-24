package net.impleri.playerskills.integration.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.util.AttachedData;
import net.impleri.playerskills.api.Skill;
import net.impleri.playerskills.basic.BasicSkillType;
import net.impleri.playerskills.integration.kubejs.events.EventsBinding;
import net.impleri.playerskills.integration.kubejs.events.SkillChangedEventJS;
import net.impleri.playerskills.integration.kubejs.events.SkillsEventJS;
import net.impleri.playerskills.integration.kubejs.skills.BasicSkillJS;
import net.impleri.playerskills.integration.kubejs.skills.NumericSkillJS;
import net.impleri.playerskills.integration.kubejs.skills.SpecializedSkillJS;
import net.impleri.playerskills.integration.kubejs.skills.TieredSkillJS;
import net.impleri.playerskills.numeric.NumericSkillType;
import net.impleri.playerskills.specialized.SpecializedSkillType;
import net.impleri.playerskills.tiered.TieredSkillType;
import net.minecraft.world.entity.player.Player;

public class PlayerSkillsPlugin extends KubeJSPlugin {
    private final PlayerSkillsKubeJSWrapper skillWrapper = new PlayerSkillsKubeJSWrapper();

    @Override
    public void init() {
        Registries.SKILLS.addType(BasicSkillType.name.toString(), BasicSkillJS.Builder.class, BasicSkillJS.Builder::new);
        Registries.SKILLS.addType(NumericSkillType.name.toString(), NumericSkillJS.Builder.class, NumericSkillJS.Builder::new);
        Registries.SKILLS.addType(TieredSkillType.name.toString(), TieredSkillJS.Builder.class, TieredSkillJS.Builder::new);
        Registries.SKILLS.addType(SpecializedSkillType.name.toString(), SpecializedSkillJS.Builder.class, SpecializedSkillJS.Builder::new);
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("PlayerSkills", skillWrapper);
    }

    @Override
    public void registerEvents() {
        EventsBinding.GROUP.register();
    }

    public static void registerSkills() {
        EventsBinding.SKILLS.post(new SkillsEventJS(Registries.SKILLS.types));
    }

    public static <T> void onSkillChange(Player player, Skill<T> next, Skill<T> prev) {
        EventsBinding.SKILL_CHANGED.post(next.getName(), new SkillChangedEventJS<T>(player, next, prev));
    }

    @Override
    public void attachPlayerData(AttachedData<Player> event) {
        event.add("skills", new PlayerDataJS(event.getParent()));
    }
}
