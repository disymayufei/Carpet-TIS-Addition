/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionException;
import carpettisaddition.utils.mixin.testers.YeetUpdateSuppressionCrashTester;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Restriction(require = @Condition(type = Condition.Type.TESTER, tester = YeetUpdateSuppressionCrashTester.class))
@Mixin(targets = "net.minecraft.world.block.ChainRestrictedNeighborUpdater$SixWayEntry")
public abstract class ChainRestrictedNeighborUpdaterSixWayEntryMixin
{
	@SuppressWarnings("deprecation")
	@Redirect(
			method = "update",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/BlockState;neighborUpdate(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;Z)V"
			)
	)
	private void yeetUpdateSuppressionCrash_implOnSixWayEntryUpdate(BlockState instance, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean bl)
	{
		if (CarpetTISAdditionSettings.yeetUpdateSuppressionCrash)
		{
			try
			{
				instance.neighborUpdate(world, pos, neighborBlock, neighborPos, bl);
			}
			catch (Throwable throwable)
			{
				UpdateSuppressionException.wrapAndThrow(throwable, world, pos);
				// if the code runs here, it means the throwable is not caused by update suppression. Re-throw it
				throw throwable;
			}
		}
		else
		{
			// vanilla
			instance.neighborUpdate(world, pos, neighborBlock, neighborPos, bl);
		}
	}
}
