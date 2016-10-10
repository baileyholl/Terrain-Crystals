package com.DrasticDemise.TerrainCrystals.Items.SkyCrystals;

import com.DrasticDemise.TerrainCrystals.Items.TerrainCrystalAbstract;
import com.DrasticDemise.TerrainCrystals.core.ConfigurationFile;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TerrainCrystalNether extends TerrainCrystalAbstract{
	public TerrainCrystalNether(){
		super("Nether");
		setMaxDamage(ConfigurationFile.netherCrystalDurability);
	}
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand){
		super.gatherBlockGenList(itemStackIn, worldIn, playerIn, ConfigurationFile.netherCrystalDiameter, Biomes.HELL, ConfigurationFile.netherCrystalChangesBiome);
		return new ActionResult(EnumActionResult.PASS, itemStackIn);
	}
	@Override
	protected int generateBlocksInWorld(BlockPos pos, World worldIn, EntityPlayer playerIn, int blocksGenerated,
										Biome desiredBiome, boolean changeBiome){
		if(eligibleStateLocation(worldIn, pos)){
			int posY = MathHelper.floor_double(playerIn.posY);
			if(posY - pos.getY() == 1){
				if(Math.random() < .9){
					worldIn.setBlockState(pos, Blocks.NETHERRACK.getDefaultState());
					if(ConfigurationFile.netherCrystalChangesBiome){
						setBiome(worldIn, pos, desiredBiome, changeBiome);
					}
					decoratePlatform(worldIn, pos);
				}else if (Math.random() < 0.3){
					worldIn.setBlockState(pos, Blocks.SOUL_SAND.getDefaultState());
					decoratePlatform(worldIn, pos);
				}else{
					worldIn.setBlockState(pos, Blocks.GRAVEL.getDefaultState());
				}
			}else{
				if(Math.random() < .95){
					worldIn.setBlockState(pos, Blocks.NETHERRACK.getDefaultState());
				}else{
					worldIn.setBlockState(pos, Blocks.SOUL_SAND.getDefaultState());
				}
			}
			blocksGenerated += 1;
		}
		return blocksGenerated;
	}
	protected void decoratePlatform(World worldIn, BlockPos pos){
		if(Blocks.BROWN_MUSHROOM.canPlaceBlockAt(worldIn, pos.up())){
			if(Math.random() < .10){
				if(Math.random() < .3){
					worldIn.setBlockState(pos.up(), Blocks.BROWN_MUSHROOM.getDefaultState());
				}else{
					worldIn.setBlockState(pos.up(), Blocks.RED_MUSHROOM.getDefaultState());
				}
			}
		}
	}
}
