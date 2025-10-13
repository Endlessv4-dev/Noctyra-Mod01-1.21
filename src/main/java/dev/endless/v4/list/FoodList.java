package dev.endless.v4.list;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class FoodList {
    public static final FoodComponent EXAMPLE_FOOD_COMPONENT = new FoodComponent.Builder()
            .nutrition(3)
            .saturationModifier(0.3F)
            .alwaysEdible()
            .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 20 * 60, 1), 0.75F)
            .build();
}
