package me.roundaround.invisibleframes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin {
  @Inject(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V", shift = At.Shift.BEFORE), cancellable = true)
  private void onInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
    if (player.isSneaking()) {
      ((ItemFrameEntity) (Object) this).setInvisible(!((ItemFrameEntity) (Object) this).isInvisible());

      info.setReturnValue(ActionResult.CONSUME);
    }
  }

  @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;dropHeldStack(Lnet/minecraft/entity/Entity;Z)V", shift = At.Shift.BEFORE))
  private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
    ((ItemFrameEntity) (Object) this).setInvisible(false);
  }
}
