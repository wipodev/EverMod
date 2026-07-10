package net.evermod.geckolib;

@FunctionalInterface
public interface EverAnimationPredicate<T extends EverAnimatable> {
  EverPlayState test(EverAnimationEvent<T> event);
}
