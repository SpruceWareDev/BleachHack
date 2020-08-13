/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDrinker420/bleachhack-1.14/).
 * Copyright (c) 2019 Bleach.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bleach.hack.module.mods;

import java.util.Random;

import com.google.common.eventbus.Subscribe;

import bleach.hack.event.events.EventSendPacket;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.packet.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.packet.PlayerMoveC2SPacket;

/**
 * @author sl
 */
public class Criticals extends Module {

	public Criticals() {
		super("Criticals", KEY_UNBOUND, Category.COMBAT, "Attempts to force Critical hits on entities you hit.");
	}

	@Subscribe
	public void sendPacket(EventSendPacket event) {
		if (event.getPacket() instanceof PlayerInteractEntityC2SPacket) {
			PlayerInteractEntityC2SPacket packet = (PlayerInteractEntityC2SPacket) event.getPacket();
			if (packet.getType() == PlayerInteractEntityC2SPacket.InteractionType.ATTACK) {
				this.doCritical();

				/* Lets fake some extra paricles to make the player feel good */
				Entity e = packet.getEntity(mc.world);
				Random r = new Random();
				for (int i = 0; i < 10; i++) {
					mc.particleManager.addParticle(ParticleTypes.CRIT, e.x, e.y + e.getHeight() / 2, e.z,
							r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5);
				}
			}
		}
	}

	private void doCritical() {
		if (!mc.player.onGround) return;
		if (mc.player.isInLava() || mc.player.isInWater()) return;
		double posX = mc.player.x;
		double posY = mc.player.y;
		double posZ = mc.player.z;
		mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionOnly(posX, posY + 0.0625, posZ, true));
		mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionOnly(posX, posY, posZ, false));
	}
}
