package com.spoonypanda.ceramiccrocks.block.entity;

import com.spoonypanda.ceramiccrocks.screen.CrockMenu;
import com.spoonypanda.ceramiccrocks.util.CrockSize;
import com.spoonypanda.ceramiccrocks.util.ModTags;
import com.spoonypanda.ceramiccrocks.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class CrockBlockEntity extends BlockEntity implements MenuProvider {
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

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return !isBlacklisted(stack);
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
    public Component getDisplayName() {
        return Component.translatable("ceramiccrocks.large_crock");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CrockMenu(pContainerId,pPlayerInventory,this,this.data,size);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag,pRegistries);
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));
        if (dyeColor != null) {
            pTag.putString("dyecolor", dyeColor.getName());
        }
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag,pRegistries);
        itemHandler.deserializeNBT(pRegistries,pTag.getCompound(("inventory")));
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
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = super.getUpdateTag(pRegistries);
        this.saveAdditional(tag,pRegistries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag,HolderLookup.Provider pRegistries) {
        this.loadAdditional(tag,pRegistries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider pRegistiries) {
        this.loadAdditional(pkt.getTag(),pRegistiries);
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 3);
        }
    }

    private boolean isBlacklisted(ItemStack stack) {

        // Can use the config or the CROCK_BLACKLISTED tag, depending on how the modpack author wants to do things.
        Item item = stack.getItem();
        if (Config.crockBlacklist.contains(BuiltInRegistries.ITEM.getKey(item))) {
            return true;
        }

        return stack.is(ModTags.Items.CROCK_BLACKLISTED);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);

        components.set(DataComponents.BASE_COLOR, dyeColor);

        components.set(
                DataComponents.CONTAINER,
                ItemContainerContents.fromItems(
                        java.util.stream.IntStream.range(0, itemHandler.getSlots())
                                .mapToObj(itemHandler::getStackInSlot)
                                .toList()
                )
        );
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput input) {
        super.applyImplicitComponents(input);
        DyeColor color = input.get(DataComponents.BASE_COLOR);
        if (color != null) {
            this.dyeColor = color;
        }
        ItemContainerContents contents = input.get(DataComponents.CONTAINER);
        if (contents != null) {
            List<ItemStack> items = contents.stream().toList();
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack stack = i < items.size()
                        ? items.get(i)
                        : ItemStack.EMPTY;
                itemHandler.setStackInSlot(i, stack);
            }
        }
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("inventory");
        tag.remove("dyecolor");
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }
}
