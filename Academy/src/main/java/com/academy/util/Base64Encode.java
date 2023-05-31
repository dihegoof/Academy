package com.academy.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import lombok.Getter;

public class Base64Encode {
	
	@Getter
	static Base64Encode instance = new Base64Encode();

	public String itemStackToBase64(ItemStack itemStack) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			dataOutput.writeObject(itemStack);
			dataOutput.close();
			return Base64.getEncoder().encodeToString(outputStream.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ItemStack itemStackFromBase64(String base64) {
		try {
			byte[] bytes = Base64.getDecoder().decode(base64);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			ItemStack itemStack = (ItemStack) dataInput.readObject();
			dataInput.close();
			return itemStack;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
