package net.evermod.world.entity.ai;

import java.util.Objects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class AttributeBuilder {

  private final AttributeSupplier.Builder builder;

  private AttributeBuilder(AttributeSupplier.Builder builder) {
    this.builder = builder;
  }

  public static AttributeBuilder mob() {
    return new AttributeBuilder(Mob.createMobAttributes());
  }

  public AttributeBuilder add(Attribute attribute, double value) {
    builder.add(Objects.requireNonNull(attribute), value);
    return this;
  }

  public AttributeSupplier.Builder build() {
    return builder;
  }
}
