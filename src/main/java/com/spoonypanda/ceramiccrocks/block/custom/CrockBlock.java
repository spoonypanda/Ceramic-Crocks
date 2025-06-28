package com.spoonypanda.ceramiccrocks.block.custom;

import com.spoonypanda.ceramiccrocks.block.entity.CrockBlockEntity;
import com.spoonypanda.ceramiccrocks.util.CrockSize;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.spoonypanda.ceramiccrocks.Config.allowDye;

public class CrockBlock extends BaseEntityBlock{
    private final VoxelShape shape;
    private final CrockSize size;
    private final int numSlots;
    public static final EnumProperty<DyeColor> DYE_COLOR = EnumProperty.create("color", DyeColor.class);

    public CrockBlock(Properties pProperties,CrockSize pSize){
        super(pProperties);
        size = pSize;
        shape = Block.box(size.getWidthOffset(),0, size.getWidthOffset(),
                            size.getWidth(),size.getHeight(),size.getWidth());
        numSlots = size.getSlotCount();
        this.registerDefaultState(this.stateDefinition.any().setValue(DYE_COLOR,DyeColor.LIGHT_GRAY));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return shape;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CrockBlockEntity(blockPos,blockState,size);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        pTooltip.add(Component.translatable("tooltip.ceramiccrocks." + size.getSerializedName() + "_crock"));

        CompoundTag tag = pStack.getTag();
        if (tag != null && tag.contains("BlockEntityTag")) {
            CompoundTag blockEntityTag = tag.getCompound("BlockEntityTag");

            if (blockEntityTag.contains("inventory")) {
                ItemStackHandler tempHandler = new ItemStackHandler(numSlots);
                tempHandler.deserializeNBT(blockEntityTag.getCompound("inventory"));

                int usedSlots = 0;
                for (int i = 0; i < tempHandler.getSlots(); i++) {
                    if (!tempHandler.getStackInSlot(i).isEmpty()) {
                        usedSlots++;
                    }
                }

                if (usedSlots > 0) {
                    pTooltip.add(Component.literal(usedSlots + " / " + tempHandler.getSlots() + " slots used"));
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(DYE_COLOR);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        ItemStack heldItem = pPlayer.getItemInHand(pHand);

        if (pPlayer.isShiftKeyDown() && heldItem.getItem() instanceof DyeItem dyeItem && allowDye == true) {
            if (!pLevel.isClientSide()) {
                BlockEntity be = pLevel.getBlockEntity(pPos);
                if (be instanceof CrockBlockEntity crock) {
                    crock.setDyeColor(dyeItem.getDyeColor());
                    if (!pPlayer.isCreative()) {
                        heldItem.shrink(1);
                    }
                }
            }
            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        }
        else{
            if(!pLevel.isClientSide()) {
                BlockEntity entity = pLevel.getBlockEntity(pPos);
                if(entity instanceof CrockBlockEntity){
                    NetworkHooks.openScreen(((ServerPlayer)pPlayer),(CrockBlockEntity)entity,pPos);
                }
                else {
                    throw new IllegalStateException("Our Container provider is missing!");
                }
            }
            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        }
    }

}
