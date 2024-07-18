package me.wacho.otaku.server.loadout.ability;

import lombok.Getter;
import lombok.Setter;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class AbilityManager {

    private List<Ability> abilities;

    public AbilityManager() {
        abilities = new ArrayList<>();
        Reflections reflections = new Reflections("me.wacho.otaku.server.loadout.ability.impl");
        Set<Class<? extends Ability>> abilityClasses = reflections.getSubTypesOf(Ability.class);

        for (Class<? extends Ability> abilityClass : abilityClasses) {
            try {
                abilities.add(abilityClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Ability getAbilityByName(String name) {
        return abilities.stream()
                .filter(ability -> ability.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
