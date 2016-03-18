package com.DrasticDemise.TerrainCrystals.Items;

import java.util.ArrayList;
import java.util.Random;

import com.DrasticDemise.TerrainCrystals.ConfigurationFile;

import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TerrainCrystalPlains extends TerrainCrystalAbstract{
	public TerrainCrystalPlains(){
		setUnlocalizedName("terrainCrystalPlains");
		setRegistryName("terrainCrystalPlains");
		setCreativeTab(CreativeTabs.tabBlock);
		setHarvestLevel("stone", 0);
		setMaxStackSize(1);
		setMaxDamage(ConfigurationFile.plainsCrystalDurability);
        GameRegistry.registerItem(this);
	}
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		return super.gatherBlockGenList(itemStackIn, worldIn, playerIn, ConfigurationFile.plainsCrystalDiameter, BiomeGenBase.plains, ConfigurationFile.plainsCrystalChangesBiome);
	}
	@Override
	protected int generateInWorld(BlockPos pos, World worldIn, EntityPlayer playerIn, int blocksGenerated,
			BiomeGenBase desiredBiome, boolean changeBiome){
		if(worldIn.getBlockState(pos) == Blocks.air.getDefaultState()){
			int posY = MathHelper.floor_double(playerIn.posY);
			if(posY - pos.getY() == 1){
				worldIn.setBlockState(pos, Blocks.grass.getDefaultState());
				decoratePlatform(worldIn, pos);
				setBiome(worldIn, pos, desiredBiome, changeBiome);
				blocksGenerated++;
			}else{
				worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
				blocksGenerated++;
			}
		}
		return blocksGenerated;
	}
	//Code taken from Lumien's Random Things Nature Core tile entity
	protected void decoratePlatform(World worldIn, BlockPos pos){
		if(ConfigurationFile.plainsCrystalGenerateTallGrass){
			IBlockState state = worldIn.getBlockState(pos);
			Random rand = new Random();
			//Try-catching our worries away!
			try{
				if(Math.random() < 0.10){
					if (state.getBlock() instanceof IGrowable)
					{
						IGrowable growable = (IGrowable) state.getBlock();
						if (growable.canGrow(worldIn, pos, state, worldIn.isRemote))
						{
							worldIn.playAuxSFX(2005, pos, 0);
							growable.grow(worldIn, rand, pos, state);
							if(Math.random() <= 0.1){
								growTree(worldIn, pos);
							}
						}
					}
				}
			}catch(IllegalArgumentException e){
				//System.out.println("Caught an error in tree growing! Tossing it out, goodbye chunk error!");
				return;
			}
		}
	}
	private void growTree(World worldIn, BlockPos pos){
		if(ConfigurationFile.plainsCrystalGenerateTrees){
			if (Blocks.sapling.canPlaceBlockAt(worldIn, pos.up())){
				if(Math.random() < .5){
					worldIn.setBlockState(pos.up(), Blocks.sapling.getStateFromMeta(2));
				}else{
					worldIn.setBlockState(pos.up(), Blocks.sapling.getDefaultState());
				}
				IGrowable growable = (IGrowable) worldIn.getBlockState(pos.up()).getBlock();
				Random rand = new Random();	
				while(worldIn.getBlockState(pos.up()) != Blocks.log.getDefaultState()){
					growable.grow(worldIn, rand, pos.up(), worldIn.getBlockState(pos.up()));
				}
			}
		}
	}
}