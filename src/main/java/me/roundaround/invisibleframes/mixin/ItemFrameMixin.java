package me.roundaround.invisibleframes.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrame.class)
public abstract class ItemFrameMixin extends HangingEntity {
  @Shadow
  public abstract ItemStack getItem();

  protected ItemFrameMixin(EntityType<? extends HangingEntity> entityType, Level level) {
    super(entityType, level);
  }

  @Inject(
      method = "interact", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/world/entity/decoration/ItemFrame;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"
  ), cancellable = true
  )
  private void interact(
      Player player,
      InteractionHand hand,
      Vec3 location,
      CallbackInfoReturnable<InteractionResult> cir
  ) {
    if (player.isCrouching()) {
      this.setInvisible(!this.isInvisible());
      this.gameEvent(GameEvent.BLOCK_CHANGE, player);
      cir.setReturnValue(InteractionResult.SUCCESS);
    }
  }

  @Inject(
      method = "onSyncedDataUpdated", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/world/entity/decoration/ItemFrame;onItemChanged(Lnet/minecraft/world/item/ItemStack;)V"
  )
  )
  private void onItemChange(EntityDataAccessor<?> accessor, CallbackInfo ci) {
    if (this.getItem().isEmpty()) {
      this.setInvisible(false);
    }
  }
}
