/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.mixin.resource.loader;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.tag.TagManagerLoader;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.function.FunctionLoader;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;

@Mixin({
		/* public */
		RecipeManager.class, ServerAdvancementLoader.class, FunctionLoader.class, TagManagerLoader.class
		/* private */
})
public abstract class KeyedResourceReloadListenerMixin implements IdentifiableResourceReloadListener {
	private Identifier fabric$id;
	private Collection<Identifier> fabric$dependencies;

	@Override
	@SuppressWarnings({"ConstantConditions", "RedundantCast"})
	public Identifier getFabricId() {
		if (this.fabric$id == null) {
			Object self = this;

			if (self instanceof RecipeManager) {
				this.fabric$id = ResourceReloadListenerKeys.RECIPES;
			} else if (self instanceof ServerAdvancementLoader) {
				this.fabric$id = ResourceReloadListenerKeys.ADVANCEMENTS;
			} else if (self instanceof FunctionLoader) {
				this.fabric$id = ResourceReloadListenerKeys.FUNCTIONS;
			} else if (self instanceof TagManagerLoader) {
				this.fabric$id = ResourceReloadListenerKeys.TAGS;
			} else {
				this.fabric$id = Identifier.ofVanilla("private/" + self.getClass().getSimpleName().toLowerCase(Locale.ROOT));
			}
		}

		return this.fabric$id;
	}

	@Override
	@SuppressWarnings({"ConstantConditions", "RedundantCast"})
	public Collection<Identifier> getFabricDependencies() {
		if (this.fabric$dependencies == null) {
			Object self = this;

			if (self instanceof TagManagerLoader) {
				this.fabric$dependencies = Collections.emptyList();
			} else {
				this.fabric$dependencies = Collections.singletonList(ResourceReloadListenerKeys.TAGS);
			}
		}

		return this.fabric$dependencies;
	}
}
