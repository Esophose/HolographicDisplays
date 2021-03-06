/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.gmail.filoghost.holograms.api.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.gmail.filoghost.holographicdisplays.api.line.TouchableLine;

@SuppressWarnings("deprecation")
public class HologramAdapter implements Hologram {
	
	public static Map<Plugin, Collection<HologramAdapter>> activeHolograms = new HashMap<>();
	
	private Plugin plugin;
	private com.gmail.filoghost.holographicdisplays.api.Hologram hologram;
	private TouchHandler touchHandler;
	
	public HologramAdapter(Plugin plugin, com.gmail.filoghost.holographicdisplays.api.Hologram delegate) {
		this.plugin = plugin;
		this.hologram = delegate;
		
		activeHolograms.computeIfAbsent(plugin, __ -> new ArrayList<>()).add(this);
	}
	
	@Override
	@Deprecated
	public boolean update() {
		return true;
	}

	@Override
	@Deprecated
	public void hide() {
		
	}

	@Override
	@Deprecated
	public void addLine(String text) {
		hologram.appendTextLine(text);
		updateTouchHandler();
	}

	@Override
	@Deprecated
	public void setLine(int index, String text) {
		hologram.removeLine(index);
		hologram.insertTextLine(index, text);
		updateTouchHandler();
	}

	@Override
	@Deprecated
	public void insertLine(int index, String text) {
		hologram.insertTextLine(index, text);
		updateTouchHandler();
	}

	@Override
	@Deprecated
	public String[] getLines() {
		String[] lines = new String[hologram.size()];
		for (int i = 0; i < lines.length; i++) {
			HologramLine lineObject = hologram.getLine(i);
			if (lineObject instanceof TextLine) {
				lines[i] = ((TextLine) lineObject).getText();
			} else {
				lines[i] = "";
			}
		}
		return lines;
	}

	@Override
	@Deprecated
	public int getLinesLength() {
		return hologram.size();
	}

	@Override
	@Deprecated
	public void setLocation(Location location) {
		hologram.teleport(location);
	}

	@Override
	@Deprecated
	public void setTouchHandler(TouchHandler handler) {
		this.touchHandler = handler;
		updateTouchHandler();
	}

	private void updateTouchHandler() {
		for (int i = 0; i < hologram.size(); i++) {
			HologramLine line = hologram.getLine(i);
			if (!(line instanceof TouchableLine)) {
				continue;
			}
			
			TouchableLine touchableLine = (TouchableLine) line;
			
			if (touchHandler != null) {
				touchableLine.setTouchHandler(new HologramTouchHandlerAdapter(this, touchHandler));
			} else {
				touchableLine.setTouchHandler(null);
			}
		}
	}

	@Override
	@Deprecated
	public TouchHandler getTouchHandler() {
		return touchHandler;
	}

	@Override
	@Deprecated
	public boolean hasTouchHandler() {
		return touchHandler != null;
	}

	@Override
	public void removeLine(int index) {
		hologram.removeLine(index);
	}

	@Override
	public void clearLines() {
		hologram.clearLines();
	}

	@Override
	public Location getLocation() {
		return hologram.getLocation();
	}

	@Override
	public double getX() {
		return hologram.getX();
	}

	@Override
	public double getY() {
		return hologram.getY();
	}

	@Override
	public double getZ() {
		return hologram.getZ();
	}

	@Override
	public World getWorld() {
		return hologram.getWorld();
	}

	@Override
	public void teleport(Location location) {
		hologram.teleport(location);
	}

	@Override
	public long getCreationTimestamp() {
		return hologram.getCreationTimestamp();
	}

	@Override
	public void delete() {
		hologram.delete();
		
		activeHolograms.get(plugin).remove(this);
	}

	@Override
	public boolean isDeleted() {
		return hologram.isDeleted();
	}

}
