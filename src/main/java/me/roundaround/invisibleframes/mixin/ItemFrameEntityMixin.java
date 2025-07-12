package me.roundaround.invisibleframes.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin extends AbstractDecorationEntity {
  @Shadow
  public abstract ItemStack getHeldItemStack();

  protected ItemFrameEntityMixin(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
    super(entityType, world);
  }

  @Inject(
      method = "interact", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"
  ), cancellable = true
  )
  private void onInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    if (player.isSneaking()) {
      this.setInvisible(!this.isInvisible());
      this.emitGameEvent(GameEvent.BLOCK_CHANGE, player);
      cir.setReturnValue(ActionResult.SUCCESS);
    }
  }

  @Inject(
      method = "onTrackedDataSet", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;setAsStackHolder(Lnet/minecraft/item/ItemStack;)V"
  )
  )
  private void onItemChange(TrackedData<?> data, CallbackInfo ci) {
    if (this.getHeldItemStack().isEmpty()) {
      this.setInvisible(false);
    }
  }
}
