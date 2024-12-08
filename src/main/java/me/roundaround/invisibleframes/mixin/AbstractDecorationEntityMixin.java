package me.roundaround.invisibleframes.mixin;

import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractDecorationEntity.class)
public abstract class AbstractDecorationEntityMixin {
  @Inject(method = "updateAttachmentPosition", at = @At("HEAD"))
  private void beforeUpdatingAttachmentPosition(CallbackInfo ci) {
    if (!(this.getSelf() instanceof ItemFrameEntity self)) {
      return;
    }

    if (self.getHeldItemStack().isEmpty()) {
      self.setInvisible(false);
    }
  }

  @Unique
  private AbstractDecorationEntity getSelf() {
    return (AbstractDecorationEntity) (Object) this;
  }
}
