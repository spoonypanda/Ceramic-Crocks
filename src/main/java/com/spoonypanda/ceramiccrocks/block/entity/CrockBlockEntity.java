package com.spoonypanda.ceramiccrocks.block.entity;

import com.spoonypanda.ceramiccrocks.screen.CrockMenu;
import com.spoonypanda.ceramiccrocks.util.CrockSize;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CrockBlockEntity extends BlockEntity implements MenuProvider {

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private ItemStackHandler itemHandler;

    private final CrockSize size;
    private int numSlots;

    private DyeColor dyeColor = DyeColor.LIGHT_GRAY;

    protected final ContainerData data;

    public CrockBlockEntity(BlockPos pPos, BlockState pBlockState, CrockSize pSize) {
        super(
                pSize == CrockSize.LARGE ? ModBlockEntities.LARGE_CROCK_BE.get() : ModBlockEntities.SMALL_CROCK_BE.get(),
                pPos,
                pBlockState);

        this.size = pSize;
        this.numSlots = size.getSlotCount();

        itemHandler = new ItemStackHandler(numSlots) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                setChanged();
            }
        };


        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return 0;
            }

            @Override
            public void set(int pIndex, int pValue) {
            }

            @Override
            public int getCount() {
                return numSlots;
            }
        };
    }

    public static CrockBlockEntity createLarge(BlockPos pos, BlockState state) {
        return new CrockBlockEntity(pos, state, CrockSize.LARGE);
    }

    public static CrockBlockEntity createSmall(BlockPos pos, BlockState state) {
        return new CrockBlockEntity(pos, state, CrockSize.SMALL);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("ceramiccrocks.large_crock");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CrockMenu(pContainerId,pPlayerInventory,this,this.data,size);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", itemHandler.serializeNBT());
        if (dyeColor != null) {
            pTag.putString("dyecolor", dyeColor.getName());
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound(("inventory")));
        if (pTag.contains("dyecolor")) {
            dyeColor = DyeColor.byName(pTag.getString("dyecolor"), null);
        }
    }

    public void setDyeColor(DyeColor color) {
        if (Objects.equals(this.dyeColor, color)) return;

        this.dyeColor = color;
        setChanged();

        if (level != null && !level.isClientSide()) {
            BlockState state = level.getBlockState(worldPosition);
            level.getChunkAt(worldPosition).setBlockEntity(this);
            level.sendBlockUpdated(worldPosition, state, state, 3);
        }
    }

    public CrockSize getSize() {
        return size;
    }

    public @Nullable DyeColor getDyeColor() {
        return dyeColor;
    }



    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 3);
        }
    }
}
