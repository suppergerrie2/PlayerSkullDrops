package com.suppergerrie2.playerskullsdrops;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(PlayerSkullDrops.MOD_ID)
@Mod.EventBusSubscriber(modid = PlayerSkullDrops.MOD_ID)
public class PlayerSkullDrops {

    public static final String MOD_ID = "splayerskulldrops";

    @SubscribeEvent
    public static void onLivingDropsEvent(LivingDropsEvent event) {
        // Only needs to run for player
        if (!(event.getEntity() instanceof PlayerEntity) || event.getEntity().world.isRemote) {
            return;
        }

        // If the damage source was a creeper check if it can drop a skull
        if (event.getSource().getTrueSource() instanceof CreeperEntity) {
            CreeperEntity creeperEntity = (CreeperEntity) event.getSource().getTrueSource();

            if (creeperEntity.ableToCauseSkullDrop()) {

                // Create the skull item
                ItemStack stack = new ItemStack(Items.PLAYER_HEAD, 1);
                stack.getOrCreateTag()
                     .putString("SkullOwner", ((PlayerEntity) event.getEntity()).getGameProfile().getName());

                ItemEntity itementity = new ItemEntity(event.getEntity().world,
                                                       event.getEntity().getPosX(),
                                                       event.getEntity().getPosY(),
                                                       event.getEntity().getPosZ(), stack);


                // Add it to the drops
                event.getDrops().add(itementity);

                // Tell the creeper it dropped a skull
                creeperEntity.incrementDroppedSkulls();
            }
        }
    }
}